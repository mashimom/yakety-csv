package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.api.ColumnDefinition;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.config.ConfigChecker;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Slf4j
class CsvToBeanParser<C extends ColumnDefinition, T> implements CsvParser<Stream<T>> {
	CsvParser<Stream<Map<C, String>>> delegate;
	BeanAssembly<C, T> beanAssembly;

	@Deprecated
	@SuppressWarnings("unused") //Used in tests for now
	private CsvToBeanParser(final CsvParser<Stream<Map<C, String>>> delegate, final BeanAssembly<C, T> beanAssembly) {
		this.delegate = delegate;
		this.beanAssembly = beanAssembly;
	}

	private CsvToBeanParser(@NotNull final FileFormatConfiguration<C> config,
	                        @NotNull final BeanAssembly<C, T> beanAssembly) {
		this.delegate = CsvParserFactory.toRowIndexedTextMap(config);
		this.beanAssembly = beanAssembly;
	}

	public static <C extends ColumnDefinition, T> CsvParser<Stream<T>> from(
					@NotNull final FileFormatConfiguration<C> config,
					@NotNull final BeanAssembly<C, T> beanAssembly) {
		if (!ConfigChecker.isValid(config)) {
			throw new IllegalArgumentException("Invalid configuration");
		}

		//noinspection ConstantConditions
		if (config.getIndexColumn().getOrder() > 0) {
			throw new IllegalArgumentException("Index column is a virtual column and should have negative order");
		}

		return new CsvToBeanParser<>(config, beanAssembly);
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
