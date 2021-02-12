package org.shimomoto.yakety.csv.config;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.IFileFormatConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum ConfigRules implements Predicate<IFileFormatConfiguration<?>> {
	HEADER_LINES(c -> c.getLinesBeforeHeader() >= 0),
	INDEX_COLUMN_FILLED(c -> c.getIndexColumn() != null),
	EMPTY_INDEX_COLUMN(INDEX_COLUMN_FILLED.negate()),
	HAS_COLUMNS(c -> !c.getColumns().isEmpty()),
	EMPTY_COLUMNS(HAS_COLUMNS.negate()),
	NON_NULL_COLUMNS(c -> c.getColumns().stream().noneMatch(Objects::isNull)),
	ALL_UNIQUE_COLUMNS(ConfigRules::validColumns);

	private final Predicate<IFileFormatConfiguration<?>> predicate;

	ConfigRules(final Predicate<IFileFormatConfiguration<?>> predicate) {
		this.predicate = predicate;
	}

	private static boolean validColumns(@NotNull final IFileFormatConfiguration<?> config) {
		final List<?> allColumns = Stream.concat(
						Stream.of(config.getIndexColumn()),
						config.getColumns().stream())
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
		final HashSet<?> uniqueColumns = new HashSet<>(allColumns);

		return allColumns.size() == uniqueColumns.size();
	}

	@Override
	public boolean test(final IFileFormatConfiguration<?> config) {
		return this.predicate.test(config);
	}
}
