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

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
class CsvToRowIndexedTextMapParser<C> extends CsvToTextMapParser<C> implements CsvParser<Stream<Map<C, String>>> {

	public CsvToRowIndexedTextMapParser(@NotNull final FileFormatConfiguration config,
	                                    @NotNull final C indexedColumnName,
	                                    @NotNull final List<C> columnNames) {
		super(config, Stream.concat(Stream.of(indexedColumnName), columnNames.stream())
						.collect(Collectors.toList()));
	}

	public static <C> CsvParser<Stream<Map<C, String>>> from(@NotNull final FileFormatConfiguration config,
	                                                         @NotNull final C indexColName,
	                                                         @NotNull final List<C> columnNames) {
		final HashSet<C> columnsSet = Stream.concat(Stream.of(indexColName),
						columnNames.stream())
						.filter(Objects::nonNull)
						.collect(Collectors.toCollection(HashSet::new));
		if (columnsSet.size() != (columnNames.size() + 1)) {
			throw new IllegalArgumentException("Column names must be unique and non-null");
		}
		return new CsvToRowIndexedTextMapParser<>(config, indexColName, columnNames);
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
