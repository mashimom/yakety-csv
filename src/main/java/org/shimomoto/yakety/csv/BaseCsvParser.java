package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter(AccessLevel.PROTECTED)
@Slf4j
abstract class BaseCsvParser<S> implements CsvParser<S> {
	Pattern fieldRegex;
	@Deprecated
	char quote; //TODO: move to column by column option
	@Deprecated
	boolean trim; //TODO: move to column by column option
	Pattern escapeQuoteRegex;
	QuotedLineSplitter lineSplitter;

	protected BaseCsvParser(@NotNull final FileFormatConfiguration configuration) {
		this.quote = configuration.getQuote();
		this.trim = configuration.isTrim();
		this.fieldRegex = getPattern(configuration.getParserLocale(),
						Character.toString(configuration.getSeparator()),
						configuration.getQuote());
		this.escapeQuoteRegex = Pattern.compile(String.format("%c{2}", quote));
		this.lineSplitter = new QuotedLineSplitter(configuration.getLineBreak(), configuration.getQuote());
	}

	protected static Pattern getPattern(final Locale parserLocale, final String separator, final char quote) {
		final String separatorString = StringEscapeUtils.escapeJava(separator);

		return Pattern.compile(String.format(
						parserLocale,
						"%s(?=([^%1c]*%1c[^%1c]*%1c)*[^%1c]*$)",
						separatorString,
						quote, quote, quote, quote, quote));
	}

	public abstract @NotNull S parse(@NotNull final String content);

	public abstract @NotNull S parse(@NotNull final InputStream input);

	public abstract @NotNull S parse(@NotNull final File file);

	@Nullable
	@Deprecated
	protected String unquote(@Nullable final String field) {
		if (field == null) {
			return null;
		}
		return escapeQuoteRegex.matcher(StringUtils.unwrap(field, quote))
						.replaceAll(Character.toString(quote));
	}

	@Nullable
	@Deprecated
	protected String mayTrim(@Nullable final String field) {
		return trim ? StringUtils.trim(field) : field;
	}
}
