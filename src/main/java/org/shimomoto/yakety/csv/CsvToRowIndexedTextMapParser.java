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
import org.shimomoto.yakety.csv.config.ConfigChecker;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
class CsvToRowIndexedTextMapParser<C> extends CsvToTextMapParser<C> implements CsvParser<Stream<Map<C, String>>> {

	public CsvToRowIndexedTextMapParser(@NotNull final FileFormatConfiguration<C> config) {
		super(config);
	}

	public static <C> CsvParser<Stream<Map<C, String>>> from(@NotNull final FileFormatConfiguration<C> config) {
		if (!ConfigChecker.isValid(config)) {
			throw new IllegalArgumentException("Invalid configuration");
		}
		return new CsvToRowIndexedTextMapParser<>(config);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(@NotNull final String content) {
		return StreamUtils.zipWithIndex(
						getLineSplitter().parse(content))
						.skip(1)
						.map(this::getMapFromIndexedLine);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(@NotNull final InputStream input) {
		return StreamUtils.zipWithIndex(
						getLineSplitter().parse(input))
						.skip(1)
						.map(this::getMapFromIndexedLine);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(@NotNull final File file) {
		return StreamUtils.zipWithIndex(
						getLineSplitter().parse(file))
						.skip(1)
						.map(this::getMapFromIndexedLine);
	}

	private Map<C, String> getMapFromIndexedLine(final Indexed<String> indexedLine) {
		final String[] splitFields = super.getFieldRegex()
						.split(indexedLine.getValue(), super.getColumnNames().size() - 1);

		final Stream<String> fields = Stream.concat(
						Stream.of(Long.toString(indexedLine.getIndex())),
						Arrays.stream(splitFields)
										.map(super::mayTrim)
										.map(super::unquote));

		return StreamUtils.zip(super.getColumnNames().stream(), fields, Pair::of)
						.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
}
