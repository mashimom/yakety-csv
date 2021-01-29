package configuration.marvel;

import configuration.marvel.api.IMarvelUniverseColumn;
import org.jetbrains.annotations.NotNull;

public enum MarvelUniverseColumns implements IMarvelUniverseColumn {
	TITLE("Title"),
	RELEASE_DATE("Release date"),
	PHASE("Phase"),
	FILM_TV("Film/TV"),
	IN_UNIVERSE_YEAR("In-universe year");

	public final @NotNull String name;

	MarvelUniverseColumns(@NotNull final String name) {
		this.name = name;
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	@Override
	public int getOrder() {
		return this.ordinal();
	}
}
