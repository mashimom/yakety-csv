package org.shimomoto.yakety.csv;

import com.codepoetics.protonpack.StreamUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapCsvParser<K> implements CsvParser<Stream<Map<K, String>>> {
	CsvParser<Stream<Stream<String>>> delegate;
	List<K> columns;

	public MapCsvParser(final CsvParser<Stream<Stream<String>>> delegate, final List<K> columns) {
		this.delegate = delegate;
		this.columns = columns;
	}

	@Override
	public @NotNull Stream<Map<K, String>> parse(final String content) {
		return delegate.parse(content)
						.map(this::toMap);
	}

	@Override
	public @NotNull Stream<Map<K, String>> parse(final File file) {
		return delegate.parse(file)
						.map(this::toMap);
	}

	@Override
	public @NotNull Stream<Map<K, String>> parse(final InputStream input) {
		return delegate.parse(input)
						.map(this::toMap);
	}

	private Map<K, String> toMap(final Stream<String> s) {
		return StreamUtils.zip(columns.stream(), s, Map::entry)
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
