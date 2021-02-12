package org.shimomoto.yakety.csv.field.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class DomainUtils {

	private DomainUtils() {
	}

	@NotNull
	public static <E> Optional<E> find(@NotNull final Predicate<E> matchCondition, @NotNull final Stream<E> domainValues) {
		return domainValues
						.filter(matchCondition)
						.findFirst();
	}

	@Nullable
	public static <E> E findOrNull(@NotNull final Predicate<E> matchCondition, @NotNull final Stream<E> domainValues) {
		return find(matchCondition, domainValues)
						.orElse(null);
	}
}
