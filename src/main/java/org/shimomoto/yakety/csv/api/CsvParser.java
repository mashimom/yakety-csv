package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.stream.Stream;

public interface CsvParser<T, L extends Stream<T>> {
	@NotNull Stream<L> parse(String content);

	@NotNull Stream<L> parse(File file);

	@NotNull Stream<L> parse(InputStream input);
}
