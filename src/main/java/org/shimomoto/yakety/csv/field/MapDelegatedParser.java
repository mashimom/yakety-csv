package org.shimomoto.yakety.csv.field;

import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.field.api.FieldParser;
import org.shimomoto.yakety.csv.field.api.MapFieldParser;

import java.util.Map;

@Value
public class MapDelegatedParser<F,C> implements MapFieldParser<C, F> {
	@NotNull FieldParser<F> delegate;
	@NotNull C key;

	@Override
	public @Nullable F parse(@NotNull final Map<C, String> map) {
		return delegate.parse(map.get(key));
	}
}
