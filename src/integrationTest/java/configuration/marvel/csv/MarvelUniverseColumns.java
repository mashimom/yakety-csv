package configuration.marvel.csv;

import configuration.marvel.api.IMarvelUniverseColumn;
import org.jetbrains.annotations.NotNull;

public enum MarvelUniverseColumns implements IMarvelUniverseColumn {
	TITLE("Title"),
	RELEASE_DATE("Release date", true),
	PHASE("Phase", true),
	FILM_TV("Film/TV"),
	IN_UNIVERSE_YEAR("In-universe year", true);

	public final @NotNull String name;
	public final boolean nullable;

	MarvelUniverseColumns(@NotNull final String name) {
		this(name, true);
	}

	MarvelUniverseColumns(@NotNull final String name, final boolean nullable) {
		this.name = name;
		this.nullable = nullable;
	}

	@Override
	public @NotNull String getDisplayName() {
		return name;
	}

	@Override
	public boolean isNullable() { return nullable; }

	@Override
	public int getOrder() {
		return this.ordinal();
	}
}
