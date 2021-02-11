package configuration.sales.domain;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.HasDisplayName;

public enum Regions implements HasDisplayName {
	ASIA("Asia"),
	AUSTRALIA_AND_OCEANIA("Australia and Oceania"),
	CENTRAL_AMERICA_AND_THE_CARIBBEAN("Central America and the Caribbean"),
	EUROPE("Europe"),
	MIDDLE_EAST_AND_NORTH_AFRICA("Middle East and North Africa"),
	NORTH_AMERICA("North America"),
	SOUTH_AMERICA("South America"),
	SUB_SAHARAN_AFRICA("Sub-Saharan Africa");

	public final String displayName;

	Regions(final String displayName) {
		this.displayName = displayName;
	}

	@Override
	public @NotNull String getDisplayName() {
		return displayName;
	}
}
