package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class CsvToTextParser extends BaseCsvParser<Stream<List<String>>> implements CsvParser<Stream<List<String>>> {
	private CsvToTextParser(final Pattern lineBreakRegex, final Pattern fieldRegex, final char quote, final boolean trim) {
		super(lineBreakRegex, fieldRegex, quote, trim);
	}

	public static CsvToTextParser from(final FileFormatConfiguration config) {
		return new CsvToTextParser(
						BaseCsvParser.getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote()),
						BaseCsvParser.getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote()),
						config.getQuote(),
						config.isTrim());
	}

	@Override
	protected Stream<List<String>> empty() {
		return Stream.empty();
	}

	@Override
	protected Stream<List<String>> parseLines(final @NotNull Scanner scanner) {
		return scanner.useDelimiter(super.getLineBreakRegex())
						.tokens()
						.map(this::getFieldsFromLine);
	}

	@NotNull
	protected List<String> getFieldsFromLine(final String line) {
		try (final Scanner sc = new Scanner(line)) {
			return sc.useDelimiter(super.getFieldRegex())
							.tokens()
							.map(super::mayTrim)
							.map(super::unquote)
							.collect(Collectors.toList());
		}
	}
}
