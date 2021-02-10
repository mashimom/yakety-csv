package org.shimomoto.yakety.csv;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.api.ColumnDefinition;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@UtilityClass
public class CsvParserFactory {
	public static CsvParser<Stream<List<String>>> toText(@NotNull final FileFormatConfiguration<?> ffc) {
		return CsvToTextParser.from(ffc);
	}

	public static <C> CsvParser<Stream<Map<C, String>>> toTextMap(@NotNull final FileFormatConfiguration<C> ffc) {
		return CsvToTextMapParser.from(ffc);
	}

	public static <C> CsvParser<Stream<Map<C, String>>> toRowIndexedTextMap(@NotNull final FileFormatConfiguration<C> ffc) {
		return CsvToRowIndexedTextMapParser.from(ffc);
	}

	public static <C extends ColumnDefinition, T> CsvParser<Stream<T>> toBeans(
					@NotNull final FileFormatConfiguration<C> config,
					@NotNull final BeanAssembly<C, T> beanAssembly) {
		return CsvToBeanParser.from(config, beanAssembly);
	}
}
