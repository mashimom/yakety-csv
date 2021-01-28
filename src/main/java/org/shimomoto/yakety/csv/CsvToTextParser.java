package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CsvToTextParser implements CsvParser<Stream<List<String>>> {
	Pattern lineBreakRegex;
	Pattern fieldRegex;
	char quote; //TODO: move to column by column option
	boolean trim; //TODO: move to column by column option

	@NotNull
	private static Pattern getPattern(final @NotNull Locale parserLocale, final String separator, final char quote) {
		final String separatorString = StringEscapeUtils.escapeJava(separator);

		return Pattern.compile(String.format(
						parserLocale,
						"%s(?=([^%1c]*%1c[^%1c]*%1c)*[^%1c]*$)",
						separatorString,
						quote, quote, quote, quote, quote));
	}

	public static CsvToTextParser from(final FileFormatConfiguration config) {
		return new CsvToTextParser(
						getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote()),
						getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote()),
						config.getQuote(),
						config.isTrim());
	}

	public @NotNull Stream<List<String>> parse(final String content) {
		return parseLines(new Scanner(content));
	}

	public @NotNull Stream<List<String>> parse(final File file) {
		try {
			return parseLines(new Scanner(file));
		} catch (final FileNotFoundException e) {
			log.error("Unable to read file: {}", file);
		}
		return Stream.empty();
	}

	public @NotNull Stream<List<String>> parse(final InputStream input) {
		return parseLines(new Scanner(input));
	}

	private Stream<List<String>> parseLines(final Scanner scanner) {
		return scanner.useDelimiter(lineBreakRegex)
						.tokens()
						.map(this::getFieldsFromLine);
	}

	@NotNull
	private List<String> getFieldsFromLine(final String line) {
		try (final Scanner sc = new Scanner(line)) {
			return sc.useDelimiter(fieldRegex)
							.tokens()
							.map(this::mayTrim)
							.map(this::unquote)
							.collect(Collectors.toList());
		}
	}

	private String unquote(@Nullable final String field) {
		if (field == null) {
			return null;
		}
		final Pattern escapeQuoteRegex = Pattern.compile(String.format("%c{2}", quote));
		return escapeQuoteRegex.matcher(StringUtils.unwrap(field, quote))
						.replaceAll(Character.toString(quote));
	}

	private String mayTrim(final String field) {
		return trim ? field.trim() : field;
	}
}
