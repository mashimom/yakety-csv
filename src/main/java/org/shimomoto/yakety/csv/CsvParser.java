package org.shimomoto.yakety.csv;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class CsvParser {

	//TODO: fix for windows line endings
	//TODO: fix for quoted line breaks
	private static final Pattern lineRegex = Pattern.compile("(?<=^|\\n)(.*?)(?=\\n|$)");
	private static final Pattern fieldRegex =
					Pattern.compile("(?:(?:(?<=^|,)(?<!\")\"(.*?)(?<!\")\"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+");

	private static Stream<String> lineSplit(Scanner scanner) {
		return scanner
						.findAll(lineRegex)
						.map(MatchResult::group);
	}

	private static Stream<String> fieldSplit(@SuppressWarnings("SameParameterValue")
	                                         @NotNull final Pattern pattern,
	                                         @NotNull final String line) {
		return new Scanner(line)
						.findAll(pattern)
						.map(MatchResult::group);
	}

	public @NotNull Stream<Stream<String>> parse(final String content) {
		return parse(new Scanner(content));
	}

	public @NotNull Stream<Stream<String>> parse(final File file) {
		try {
			return parse(new Scanner(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return Stream.empty();
	}

	public @NotNull Stream<Stream<String>> parse(final InputStream input) {
		return parse(new Scanner(input));
	}

	public @NotNull Stream<Stream<String>> parse(Scanner scanner) {
		return lineSplit(scanner)
						.map(l -> fieldSplit(fieldRegex, l));
	}
}
