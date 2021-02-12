package configuration.marvel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.HasDisplayName;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum MediaVehicle implements HasDisplayName {
	FILM("Film"),
	SERIES("Series");

	public final String displayName;

	public static @Nullable MediaVehicle findOrNull(@Nullable final String text) {
		if (StringUtils.isBlank(text))
			return null;
		return org.shimomoto.yakety.csv.field.util.DomainUtils.findOrNull(
						(MediaVehicle e) -> e.getDisplayName().equals(text),
						Arrays.stream(MediaVehicle.values()));
	}
}
