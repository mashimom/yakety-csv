package org.shimomoto.yakety.csv.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.IFileFormatConfiguration;

import java.util.stream.Stream;

import static org.shimomoto.yakety.csv.config.ConfigRules.*;

@UtilityClass
public class ConfigChecker {

	public static <C> boolean isValidForSimple(@NotNull final IFileFormatConfiguration<C> config) {
		return Stream.of(HEADER_LINES, EMPTY_INDEX_COLUMN, EMPTY_COLUMNS)
						.allMatch(p -> p.test(config));
	}

	public static <C> boolean isValidWithoutIndex(@NotNull final IFileFormatConfiguration<C> config) {
		return Stream.of(HEADER_LINES, EMPTY_INDEX_COLUMN, HAS_COLUMNS, NON_NULL_COLUMNS, ALL_UNIQUE_COLUMNS)
						.allMatch(p -> p.test(config));
	}

	public static <C> boolean isValid(@NotNull final IFileFormatConfiguration<C> config) {
		return Stream.of(HEADER_LINES, INDEX_COLUMN_FILLED, HAS_COLUMNS, NON_NULL_COLUMNS, ALL_UNIQUE_COLUMNS)
						.allMatch(p -> p.test(config));
	}
}

