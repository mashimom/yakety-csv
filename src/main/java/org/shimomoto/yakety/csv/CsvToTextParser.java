package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
class CsvToTextParser extends BaseCsvParser<Stream<List<String>>> implements CsvParser<Stream<List<String>>> {
	private CsvToTextParser(@NotNull final FileFormatConfiguration configuration) {
		super(configuration);
	}

	public static CsvToTextParser from(final @NotNull FileFormatConfiguration config) {
		return new CsvToTextParser(config);
	}

	@Override
	public @NotNull Stream<List<String>> parse(final @NotNull String content) {
		return getLineSplitter().parse(content)
						.map(this::getFieldsFromLine);
	}

	@Override
	public @NotNull Stream<List<String>> parse(final @NotNull InputStream input) {
		return getLineSplitter().parse(input)
						.map(this::getFieldsFromLine);
	}

	@Override
	public @NotNull Stream<List<String>> parse(final @NotNull File file) {
		return getLineSplitter().parse(file)
						.map(this::getFieldsFromLine);
	}

	@NotNull
	protected List<String> getFieldsFromLine(final String line) {
		return Arrays.stream(super.getFieldRegex().split(line, -1))
						.map(super::mayTrim)
						.map(super::unquote)
						.collect(Collectors.toList());
	}
}
