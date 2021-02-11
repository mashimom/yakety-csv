package org.shimomoto.yakety.csv.field;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.HasDisplayName;
import org.shimomoto.yakety.csv.field.api.MapFieldParser;

import java.util.EnumSet;
import java.util.Map;

public class BaseDomainMapParser<C, D extends Enum<D> & HasDisplayName>
				implements MapFieldParser<C, D> {
	private final C col;
	private final Class<D> enumType;

	public BaseDomainMapParser(final C col, final Class<D> enumType) {
		this.col = col;
		this.enumType = enumType;
	}

	@Override
	public @Nullable D parse(@NotNull final Map<C, String> map) {
		final @Nullable String value = map.get(col);
		return EnumSet.allOf(enumType).stream()
						.filter(e -> e.getDisplayName().equals(value))
						.findFirst()
						.orElse(null);
	}
}
