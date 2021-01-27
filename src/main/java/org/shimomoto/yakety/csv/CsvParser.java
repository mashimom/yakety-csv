package org.shimomoto.yakety.csv;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class CsvParser {

	static final Pattern lineRegex =
					Pattern.compile("(?:(?:(?<=^|,)(?<!\")\"(.*?)(?<!\")\"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+");

	public @NotNull Stream<Stream<String>> parse(String content) {
		final BufferedReader br = new BufferedReader(new StringReader(content));
		final Runnable safeClose = () -> {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		return br.lines()
						.map(l -> lineSplit(lineRegex, l))
						.onClose(safeClose);
	}

	private static Stream<String> lineSplit(@SuppressWarnings("SameParameterValue")
	                                        @NotNull final Pattern pattern,
	                                        @NotNull final String line) {
		return new Scanner(line)
						.findAll(pattern)
						.map(MatchResult::group);

//		final Matcher matcher = pattern.matcher(line);
//		final Stream.Builder<String> stb = Stream.builder();
//		while (matcher.find()) {
//			stb.add(matcher.group());
//		}
//		return stb.build();
	}
}
