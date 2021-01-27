package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public interface CsvParser<S> {
	@NotNull S parse(String content);

	@NotNull S parse(File file);

	@NotNull S parse(InputStream input);
}
