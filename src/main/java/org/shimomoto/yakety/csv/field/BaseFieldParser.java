package org.shimomoto.yakety.csv.field;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.field.api.FieldParser;

import java.util.Locale;
import java.util.function.BiFunction;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BaseFieldParser<F> implements FieldParser<F> {
	//SIC: You cannot hash functions
	BiFunction<@Nullable String, @NotNull Locale, @Nullable F> localizedParser;

	protected BaseFieldParser(final BiFunction<@Nullable String, @NotNull Locale, @Nullable F> localizedParser) {
		this.localizedParser = localizedParser;
	}

	@NotNull
	abstract Locale getLocale();

	@Override
	public @Nullable F parse(@Nullable final String value) {
		return localizedParser.apply(value, getLocale());
	}

}
