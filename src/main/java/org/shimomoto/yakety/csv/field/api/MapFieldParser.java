package org.shimomoto.yakety.csv.field.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface MapFieldParser<C, F> {
	@Nullable F parse(@NotNull Map<C,String> map);
}
