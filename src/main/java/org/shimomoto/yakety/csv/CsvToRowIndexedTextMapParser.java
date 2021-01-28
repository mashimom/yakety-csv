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

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
final class CsvToRowIndexedTextMapParser extends CsvToTextMapParser implements CsvParser<Stream<Map<String, String>>> {
	protected CsvToRowIndexedTextMapParser(final Pattern lineBreakRegex,
	                                       final Pattern fieldRegex,
	                                       final char quote,
	                                       final boolean trim,
	                                       final String indexedColumnName,
	                                       final List<String> columnNames) {
		super(lineBreakRegex,
						fieldRegex,
						quote,
						trim,
						Stream.concat(Stream.of(indexedColumnName), columnNames.stream())
										.collect(Collectors.toList()));
	}

	public static CsvParser<Stream<Map<String, String>>> from(final FileFormatConfiguration config, final String indexColName, final List<String> columnNames) {
		final Pattern lineBreakRegex =
						BaseCsvParser.getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote());
		final Pattern fieldRegex =
						BaseCsvParser.getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote());

		return new CsvToRowIndexedTextMapParser(
						lineBreakRegex,
						fieldRegex,
						config.getQuote(),
						config.isTrim(),
						indexColName,
						columnNames);
	}

	@Override
	protected Stream<Map<String, String>> empty() {
		return Stream.empty();
	}

	@Override
	protected Stream<Map<String, String>> parseLines(@NotNull final Scanner scanner) {
		final Stream<String> lines = scanner.useDelimiter(super.getLineBreakRegex())
						.tokens();

		return StreamUtils.zipWithIndex(lines)
						.map(this::getMapFromIndexedLine)
						.skip(1);
	}

	private Map<String, String> getMapFromIndexedLine(final Indexed<String> indexedLine) {
		try (final Scanner sc = new Scanner(indexedLine.getValue())) {
			final Stream<String> fields = Stream.concat(
							Stream.of(Long.toString(indexedLine.getIndex())),
							sc.useDelimiter(super.getFieldRegex())
											.tokens()
											.map(super::mayTrim)
											.map(super::unquote));

			return StreamUtils.zip(super.getColumnNames().stream(), fields, Pair::of)
							.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
		}
	}
}
