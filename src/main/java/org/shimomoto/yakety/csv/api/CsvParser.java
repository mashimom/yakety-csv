package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public interface CsvParser<S> {
	@NotNull S parse(@NotNull String content);

	@NotNull S parse(@NotNull File file);

	@NotNull S parse(@NotNull InputStream input);
}
