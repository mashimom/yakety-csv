package org.shimomoto.yakety.csv;

import com.codepoetics.protonpack.StreamUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.config.ConfigChecker;
import org.shimomoto.yakety.csv.config.FileFormatConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
class CsvToTextMapParser<C> extends BaseCsvParser<Stream<Map<C, String>>> implements CsvParser<Stream<Map<C, String>>> {
	@Getter(AccessLevel.PROTECTED)
	private final List<@NotNull C> columnNames;

	public CsvToTextMapParser(@NotNull final FileFormatConfiguration<C> config) {
		super(config);
		this.columnNames = Stream.concat(
						Stream.of(config.getIndexColumn()),
						config.getColumns().stream())
						.filter(Objects::nonNull)
						.collect(Collectors.toUnmodifiableList());
	}

	public static <C> CsvParser<Stream<Map<C, String>>> from(@NotNull final FileFormatConfiguration<C> config) {
		if (!ConfigChecker.isValidWithoutIndex(config)) {
			throw new IllegalArgumentException("Configuration is invalid");
		}
		return new CsvToTextMapParser<>(config);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(final @NotNull String content) {
		return getLineSplitter().parse(content)
						.skip(1)
						.map(this::getMapFromLine);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(final @NotNull InputStream input) {
		return getLineSplitter().parse(input)
						.skip(1)
						.map(this::getMapFromLine);
	}

	@Override
	public @NotNull Stream<Map<C, String>> parse(final @NotNull File file) {
		return getLineSplitter().parse(file)
						.skip(1)
						.map(this::getMapFromLine);
	}

	private Map<C, String> getMapFromLine(final String line) {
		final Stream<String> fields = Arrays.stream(super.getFieldRegex().split(line, this.columnNames.size()))
						.map(super::mayTrim)
						.map(super::unquote);
		return StreamUtils.zip(this.columnNames.stream(), fields, Pair::of)
						.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}
}
