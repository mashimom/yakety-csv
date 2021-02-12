package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ColumnDefinition extends HasDisplayName {

	int getOrder();

	@NotNull String getDisplayName();

	boolean isNullable();

	@Nullable
	default String get(@NotNull final Map<? extends ColumnDefinition, String> map) {
		return map.get(this);
	}
}
