package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@Getter(AccessLevel.PROTECTED)
@Slf4j
class QuotedLinesSplitter {
	//TODO: only works with single char linebreak (no windows format yet) and single char quote
	int lineBreak;
	int quote;

	QuotedLinesSplitter(final char lineBreak, final char quote) {
		this.lineBreak = (int) lineBreak;
		this.quote = (int) quote;
	}

	public Stream<@NotNull String> parse(@NotNull final String content) {
		return parse(new BufferedReader(new StringReader(content)));
	}

	public Stream<@NotNull String> parse(@NotNull final File file) {
		try {
			return parse(new FileInputStream(file));
		} catch (final FileNotFoundException e) {
			log.error("Unable to open file", e);
			throw new RuntimeException(e);
		}
	}

	public Stream<@NotNull String> parse(@NotNull final InputStream content) {
		return parse(new BufferedReader(new InputStreamReader(content)));
	}

	private Stream<@NotNull String> parse(final BufferedReader reader) {
		final Supplier<@Nullable String> lineSupplier = () -> {
			try {
				int quoteCount = 0;
				final StringBuilder sb = new StringBuilder();
				int ch;
				while ((ch = reader.read()) != -1) {
					if (ch == lineBreak && (quoteCount % 2 == 0)) break;
					if (ch == quote) quoteCount++;
					sb.append((char) ch);
				}
				return (ch == -1 && sb.length() == 0) ? null : sb.toString();
			} catch (final IOException e) {
				log.error("Unable to read from source", e);
			}
			return null;
		};

		final Runnable readerClose = () -> {
			try {
				reader.close();
			} catch (final IOException e) {
				log.error("Unable to close reader source", e);
			}
		};

		return Stream.generate(lineSupplier)
						.takeWhile(Objects::nonNull)
						.onClose(readerClose);
	}
}
