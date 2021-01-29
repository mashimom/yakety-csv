package org.shimomoto.yakety.csv;

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
class CsvToRowIndexedTextMapParser<C> extends CsvToTextMapParser<C> implements CsvParser<Stream<Map<C, String>>> {
	protected CsvToRowIndexedTextMapParser(final Pattern lineBreakRegex,
	                                       final Pattern fieldRegex,
	                                       final char quote,
	                                       final boolean trim,
	                                       final C indexedColumnName,
	                                       final List<C> columnNames) {
		super(lineBreakRegex,
						fieldRegex,
						quote,
						trim,
						Stream.concat(Stream.of(indexedColumnName), columnNames.stream())
										.collect(Collectors.toList()));
	}

	public static <C> CsvParser<Stream<Map<C, String>>> from(final FileFormatConfiguration config, final C indexColName, final List<C> columnNames) {
		final Pattern lineBreakRegex =
						BaseCsvParser.getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote());
		final Pattern fieldRegex =
						BaseCsvParser.getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote());

		return new CsvToRowIndexedTextMapParser<>(
						lineBreakRegex,
						fieldRegex,
						config.getQuote(),
						config.isTrim(),
						indexColName,
						columnNames);
	}

	@Override
	protected Stream<Map<C, String>> empty() {
		return Stream.empty();
	}

	@Override
	protected Stream<Map<C, String>> parseLines(@NotNull final Scanner scanner) {
		final Stream<String> lines = scanner.useDelimiter(super.getLineBreakRegex())
						.tokens();

		return StreamUtils.zipWithIndex(lines)
						.map(this::getMapFromIndexedLine)
						.skip(1);
	}

	private Map<C, String> getMapFromIndexedLine(final Indexed<String> indexedLine) {
		final String[] splitFields = super.getFieldRegex().split(indexedLine.getValue(), super.getColumnNames().size());
		final Stream<String> fields = Stream.concat(
						Stream.of(Long.toString(indexedLine.getIndex())),
						Arrays.stream(splitFields)
										.map(super::mayTrim)
										.map(super::unquote));

		return StreamUtils.zip(super.getColumnNames().stream(), fields, Pair::of)
						.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
}
