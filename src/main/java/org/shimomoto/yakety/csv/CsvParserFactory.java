package org.shimomoto.yakety.csv;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class CsvParserFactory {
	public static CsvParser<Stream<List<String>>> toText(@Nullable final FileFormatConfiguration ffc) {
		return Optional.ofNullable(ffc)
						.map(CsvToTextParser::from)
						.orElseGet(() -> CsvToTextParser.from(FileFormatConfiguration.builder().build()));
	}

	public static CsvParser<Stream<Map<String, String>>> toTextMap(@Nullable final FileFormatConfiguration ffc, @NotNull final List<String> columnNames) {
		return Optional.ofNullable(ffc)
						.map(c -> CsvToTextMapParser.from(c, columnNames))
						.orElseGet(() -> CsvToTextMapParser.from(FileFormatConfiguration.builder().build(), columnNames));
	}

	public static CsvParser<Stream<Map<String, String>>> toRowIndexedTextMap(@Nullable final FileFormatConfiguration ffc,
	                                                                         @Nullable final String indexColName,
	                                                                         @NotNull final List<String> columnNames) {
		final String icn = Optional.ofNullable(indexColName)
						.orElse("_");
		if (columnNames.contains(icn)) {
			throw new IllegalArgumentException("Indexed column must not exist on the file");
		}
		return Optional.ofNullable(ffc)
						.map(c -> CsvToRowIndexedTextMapParser.from(c, icn, columnNames))
						.orElseGet(() -> CsvToRowIndexedTextMapParser.from(FileFormatConfiguration.builder().build(), icn, columnNames));
	}

	@SuppressWarnings("unused")
	public static final class Standard {
		private Standard() {
		}

		public static final FileFormatConfiguration excel = FileFormatConfiguration.builder().separator(';').build();
	}
}
