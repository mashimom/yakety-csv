package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.api.ColumnDefinition;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Slf4j
class CsvToBeanParser<C extends ColumnDefinition, T> implements CsvParser<Stream<T>> {
	CsvParser<Stream<Map<C, String>>> delegate;
	BeanAssembly<C, T> beanAssembly;

	public CsvToBeanParser(@NotNull final ExtendedFileFormatConfiguration<C> config,
	                       @NotNull final BeanAssembly<C, T> beanAssembly) {
		final FileFormatConfiguration basicConfig = FileFormatConfiguration.builder()
						.parserLocale(config.getParserLocale())
						.lineBreak(config.getLineBreak())
						.separator(config.getSeparator())
						.quote(config.getQuote())
						.trim(config.isTrim())
						.build();

		this.delegate = CsvParserFactory.toRowIndexedTextMap(
						basicConfig,
						config.getIndexColumn(),
						config.getColumns());
		this.beanAssembly = beanAssembly;
	}

	public static <C extends ColumnDefinition, T> CsvParser<Stream<T>> from(@NotNull final FileFormatConfiguration config,
	                                                                        @NotNull final C indexColName,
	                                                                        @NotNull final List<C> columnNames,
	                                                                        @NotNull final BeanAssembly<C, T> beanAssembly) {
		if (indexColName.getOrder() > 0) {
			throw new IllegalArgumentException("Index column is a virtual column and should have negative order");
		}
		final HashSet<C> columnsSet = Stream.concat(Stream.of(indexColName),
						columnNames.stream())
						.filter(Objects::nonNull)
						.collect(Collectors.toCollection(HashSet::new));
		if (columnsSet.size() != (columnNames.size() + 1)) {
			throw new IllegalArgumentException("Column names must be unique and non-null");
		}

		final ExtendedFileFormatConfiguration<C> newConfig = ExtendedFileFormatConfiguration.<C>builder()
						.parserLocale(config.getParserLocale())
						.lineBreak(config.getLineBreak())
						.separator(config.getSeparator())
						.quote(config.getQuote())
						.trim(config.isTrim())
						.indexColumn(indexColName)
						.columns(columnNames)
						.build();
		return new CsvToBeanParser<>(newConfig, beanAssembly);
	}

	@Override
	public @NotNull Stream<T> parse(@NotNull final String content) {
		return delegate.parse(content)
						.map(beanAssembly::assemble);
	}

	@Override
	public @NotNull Stream<T> parse(@NotNull final File file) {
		return delegate.parse(file)
						.map(beanAssembly::assemble);
	}

	@Override
	public @NotNull Stream<T> parse(@NotNull final InputStream input) {
		return delegate.parse(input)
						.map(beanAssembly::assemble);
	}
}
