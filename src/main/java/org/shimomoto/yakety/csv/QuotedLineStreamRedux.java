package org.shimomoto.yakety.csv;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Value
@Slf4j
public class QuotedLineStreamRedux {
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

	@Nullable
	private Stream<@NotNull String> parse(final BufferedReader reader) {
		final Supplier<@Nullable String> lineSupplier = () -> {
			try {
				int quoteCount = 0;
				final StringBuilder sb = new StringBuilder();
				int ch;
				while ((ch = reader.read()) != -1) {
					if (ch == lineBreak && (quoteCount % 2 == 0)) {
						break;
					}
					if (ch == quote) {
						quoteCount++;
					}
					sb.append((char) ch);
				}

				return (ch == -1) ? null : sb.toString();
			} catch (final IOException e) {
				log.error("Unable to read", e);
			}
			return null;
		};

		return Stream.generate(lineSupplier)
						.takeWhile(Objects::nonNull);
	}
}
