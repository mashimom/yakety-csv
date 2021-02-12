package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;

public interface HasDisplayValue<T> {

	@NotNull T getDisplayValue();
}
