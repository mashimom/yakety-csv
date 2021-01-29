package org.shimomoto.yakety.csv;

import com.codepoetics.protonpack.StreamUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
class CsvToTextMapParser<C> extends BaseCsvParser<Stream<Map<C, String>>> implements CsvParser<Stream<Map<C, String>>> {
	@Getter(AccessLevel.PROTECTED)
	private final List<C> columnNames;

	protected CsvToTextMapParser(final Pattern lineBreakRegex, final Pattern fieldRegex, final char quote, final boolean trim, final List<C> columnNames) {
		super(lineBreakRegex, fieldRegex, quote, trim);
		this.columnNames = columnNames;
	}

	public static <C> CsvParser<Stream<Map<C, String>>> from(final FileFormatConfiguration config, final List<C> columnNames) {
		return new CsvToTextMapParser<>(BaseCsvParser.getPattern(config.getParserLocale(), config.getLineBreak(), config.getQuote()),
						BaseCsvParser.getPattern(config.getParserLocale(), Character.toString(config.getSeparator()), config.getQuote()),
						config.getQuote(),
						config.isTrim(),
						columnNames);
	}

	@Override
	protected Stream<Map<C, String>> empty() {
		return Stream.empty();
	}

	@Override
	protected Stream<Map<C, String>> parseLines(final @NotNull Scanner scanner) {
		return scanner.useDelimiter(super.getLineBreakRegex())
						.tokens()
						.skip(1) //skip header
						.map(this::getMapFromLine);

	}

	private Map<C, String> getMapFromLine(final String line) {
		final Stream<String> fields = Arrays.stream(super.getFieldRegex().split(line, this.columnNames.size()))
						.map(super::mayTrim)
						.map(super::unquote);
		return StreamUtils.zip(this.columnNames.stream(), fields, Pair::of)
						.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
}
