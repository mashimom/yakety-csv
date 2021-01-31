package org.shimomoto.yakety.csv;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CsvParserFactory {
	public static CsvParser<Stream<List<String>>> toText(@Nullable final FileFormatConfiguration ffc) {
		return Optional.ofNullable(ffc)
						.map(CsvToTextParser::from)
						.orElseGet(() -> CsvToTextParser.from(FileFormatConfiguration.builder().build()));
	}

	public static <C> CsvParser<Stream<Map<C, String>>> toTextMap(@Nullable final FileFormatConfiguration ffc,
	                                                              @NotNull final List<C> columnNames) {
		final Set<C> columnsAsSet = new HashSet<>(columnNames);
		if (columnsAsSet.size() != columnNames.size()) {
			throw new IllegalArgumentException("At least one of the columns are duplicated, which is not allowed");
		}
		return Optional.ofNullable(ffc)
						.map(c -> CsvToTextMapParser.from(c, columnNames))
						.orElseGet(() -> CsvToTextMapParser.from(FileFormatConfiguration.builder().build(), columnNames));
	}

	public static <C> CsvParser<Stream<Map<C, String>>> toRowIndexedTextMap(@Nullable final FileFormatConfiguration ffc,
	                                                                        @NotNull final C indexColName,
	                                                                        @NotNull final List<C> columnNames) {
		//TODO: check if there is no repeated column, add all to a set and check sizes
		final Set<C> columnsAsSet = Stream.concat(Stream.of(indexColName), columnNames.stream())
						.collect(Collectors.toSet());
		if (columnsAsSet.size() != columnNames.size() + 1) {
			throw new IllegalArgumentException("At least one of the columns are duplicated, which is not allowed");
		}
		return Optional.ofNullable(ffc)
						.map(c -> CsvToRowIndexedTextMapParser.from(c, indexColName, columnNames))
						.orElseGet(() -> CsvToRowIndexedTextMapParser.from(FileFormatConfiguration.builder().build(), indexColName, columnNames));
	}

	@SuppressWarnings("unused")
	public static final class Standard {
		private Standard() {
		}

		public static final FileFormatConfiguration excel = FileFormatConfiguration.builder().separator(';').build();
		public static final FileFormatConfiguration tsv = FileFormatConfiguration.builder().separator('\t').build();
	}
}
