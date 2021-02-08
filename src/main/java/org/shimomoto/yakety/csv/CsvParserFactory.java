package org.shimomoto.yakety.csv;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.api.ColumnDefinition;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.config.ExtendedFileFormatConfiguration;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

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

	public static <C extends ColumnDefinition, T> CsvParser<Stream<T>> toBeans(
					@NotNull final ExtendedFileFormatConfiguration<C> config,
					@NotNull final BeanAssembly<C, T> beanAssembly) {

		final Set<C> columnsAsSet = Stream.concat(Stream.of(config.getIndexColumn()), config.getColumns().stream())
						.collect(Collectors.toSet());
		if (columnsAsSet.size() != config.getColumns().size() + 1) {
			throw new IllegalArgumentException("At least one of the columns are duplicated, which is not allowed");
		}

		final FileFormatConfiguration simpleconfig = FileFormatConfiguration.builder()
						.parserLocale(config.getParserLocale())
						.lineBreak(config.getLineBreak())
						.separator(config.getSeparator())
						.quote(config.getQuote())
						.trim(config.isTrim())
						.build();
		return CsvToBeanParser.from(
						simpleconfig,
						config.getIndexColumn(),
						config.getColumns(),
						beanAssembly);
	}


	@SuppressWarnings("unused")
	public static final class StandardFiles {
		public static final FileFormatConfiguration excel = FileFormatConfiguration.builder().separator(';').build();
		public static final FileFormatConfiguration tsv = FileFormatConfiguration.builder().separator('\t').build();

		private StandardFiles() {
		}
	}
}
