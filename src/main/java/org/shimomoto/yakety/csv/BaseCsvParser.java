package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter(AccessLevel.PROTECTED)
@Slf4j
abstract class BaseCsvParser<S> implements CsvParser<S> {
	Pattern lineBreakRegex;
	Pattern fieldRegex;
	char quote; //TODO: move to column by column option
	boolean trim; //TODO: move to column by column option
	Pattern escapeQuoteRegex;

	protected BaseCsvParser(final Pattern lineBreakRegex, final Pattern fieldRegex, final char quote, final boolean trim) {
		this.lineBreakRegex = lineBreakRegex;
		this.fieldRegex = fieldRegex;
		this.quote = quote;
		this.trim = trim;
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

	public S parse(final String content) {
		return parseLines(new Scanner(content));
	}

	public S parse(final File file) {
		try {
			return parseLines(new Scanner(file));
		} catch (final FileNotFoundException e) {
			log.error("Unable to read file: {}", file);
		}
		return empty();
	}

	protected abstract S empty();

	public S parse(final InputStream input) {
		return parseLines(new Scanner(input));
	}

	protected abstract S parseLines(final Scanner scanner);

	protected String unquote(final String field) {
		if (field == null) {
			return null;
		}
		return escapeQuoteRegex.matcher(StringUtils.unwrap(field, quote))
						.replaceAll(Character.toString(quote));
	}

	protected String mayTrim(final String field) {
		return trim ? field.trim() : field;
	}
}
