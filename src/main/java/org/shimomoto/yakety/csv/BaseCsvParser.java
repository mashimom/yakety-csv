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

import java.util.Locale;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter(AccessLevel.PROTECTED)
@Slf4j
abstract class BaseCsvParser<S> implements CsvParser<S> {
	char quote;
	char lineBreak;
	boolean trim;
	QuotedLinesSplitter lineSplitter;
	@EqualsAndHashCode.Exclude
	Pattern fieldRegex;
	@EqualsAndHashCode.Exclude
	Pattern escapeQuoteRegex;

	protected BaseCsvParser(@NotNull final FileFormatConfiguration configuration) {
		this.quote = configuration.getQuote();
		this.lineBreak = configuration.getLineBreak();
		this.trim = configuration.isTrim();
		this.lineSplitter = new QuotedLinesSplitter(lineBreak, quote);
		this.fieldRegex = getPattern(configuration.getParserLocale(),
						Character.toString(configuration.getSeparator()),
						configuration.getQuote());
		this.escapeQuoteRegex = Pattern.compile(String.format("%c{2}", quote));
	}

	protected static Pattern getPattern(final Locale parserLocale, final String separator, final char quote) {
		final String separatorString = StringEscapeUtils.escapeJava(separator);

		return Pattern.compile(String.format(
						parserLocale,
						"%s(?=([^%1c]*%1c[^%1c]*%1c)*[^%1c]*$)",
						separatorString,
						quote, quote, quote, quote, quote));
	}

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
