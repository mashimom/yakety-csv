package configuration;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.ColumnDefinition;

public enum MarvelUniverseColumns implements ColumnDefinition {
	TITLE("Title"),
	RELEASE_DATE("Release date"),
	PHASE("Phase"),
	FILM_TV("Film/TV"),
	IN_UNIVERSE_YEAR("In-universe year");

	public final @NotNull String name;

	MarvelUniverseColumns(@NotNull String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getOrder() {
		return this.ordinal();
	}
}
