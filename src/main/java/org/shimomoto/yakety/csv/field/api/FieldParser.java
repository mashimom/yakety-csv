package org.shimomoto.yakety.csv.field.api;

import org.jetbrains.annotations.Nullable;

public interface FieldParser<F> {
	@Nullable F parse(@Nullable String value);
}


