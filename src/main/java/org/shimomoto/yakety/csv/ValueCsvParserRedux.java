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
public class ValueCsvParserRedux {
	Pattern lineBreakRegex;
	//	Pattern lineBreakRegex = Pattern.compile("\\n(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	Pattern fieldRegex;
	//	Pattern fieldRegex = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	char quote;
	boolean trim;

	//TODO: I kinda know we should use string instead of char, bear with me for now
	@NotNull
	private static Pattern getPattern(final @NotNull Locale parserLocale, final String separator, final char quote) {
		final String separatorString = StringEscapeUtils.escapeJava(separator);

		return Pattern.compile(String.format(
						parserLocale,
						"%s(?=([^%1c]*%1c[^%1c]*%1c)*[^%1c]*$)",
						separatorString,
						quote, quote, quote, quote, quote));
	}

	public static ValueCsvParserRedux from(final ParserConfigurationRedux config) {
		return new ValueCsvParserRedux(
						getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote()),
						getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote()),
						config.getQuote(),
						config.isTrim());
	}

	public Stream<List<String>> parse(final String content) {
		return parseInternal(new Scanner(content));
	}

	public Stream<List<String>> parse(final File file) {
		try {
			return parseInternal(new Scanner(file));
		} catch (final FileNotFoundException e) {
			log.error("Unable to read file: {}", file);
		}
		return Stream.empty();
	}

	public Stream<List<String>> parse(final InputStream input) {
		return parseInternal(new Scanner(input));
	}

	private Stream<List<String>> parseInternal(final Scanner scanner) {
		return scanner.useDelimiter(lineBreakRegex)
						.tokens()
						.map(this::getFieldsFromLine);
	}

	@NotNull
	private List<String> getFieldsFromLine(final String line) {
		return new Scanner(line)
						.useDelimiter(fieldRegex)
						.tokens()
						.map(this::mayTrim)
						.map(this::unquote)
						.collect(Collectors.toList());
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
