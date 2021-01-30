package org.shimomoto.yakety.csv;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Value
@Slf4j
public class QuotedLineStream {
	int lineBreak;
	int quote;

	public Stream<@NotNull String> parse(final String content) {
		final BufferedReader reader = new BufferedReader(new StringReader(content));

		return parse(reader);
	}

	public Stream<@NotNull String> parse(final InputStream content) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(content));

		return parse(reader);
	}

	private Stream<@NotNull String> parse(final BufferedReader reader) {
		final Scanner scanner = new Scanner(reader);

		final Supplier<@Nullable String> lineSupplier = () -> {
			if (!scanner.hasNextLine()) {
				return null;
			}
			final StringBuilder line = new StringBuilder(scanner.nextLine());
			while (StringUtils.countMatches(line, (char) quote) % 2 != 0) {
				if (scanner.hasNextLine()) {
					line.append(scanner.nextLine());
				}
			}
			return line.toString();
		};

		return Stream.generate(lineSupplier)
						.takeWhile(Objects::nonNull)
						.onClose(scanner::close);
	}
}
