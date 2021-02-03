package configuration.marvel.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.HasDisplayValue;

import java.util.Arrays;

public enum MCUPhases implements HasDisplayValue<Integer> {
	PHASE_1(1),
	PHASE_2(2),
	PHASE_3(3),
	PHASE_4(4);

	public final int displayValue;

	MCUPhases(final int displayValue) {
		this.displayValue = displayValue;
	}

	public static @Nullable MCUPhases findOrNull(@Nullable final Integer value) {
		if (value == null) {
			return null;
		}

		return org.shimomoto.yakety.csv.field.util.DomainUtils.findOrNull(
						(MCUPhases e) -> e.getDisplayValue().equals(value),
						Arrays.stream(MCUPhases.values()));
	}

	@Override
	public @NotNull Integer getDisplayValue() {
		return displayValue;
	}

}
