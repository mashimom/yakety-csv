package org.shimomoto.yakety.csv;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.api.CsvParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ValueCsvParser implements CsvParser<Stream<List<String>>> {

	private final boolean trim = false;
	private final boolean drop_last = true;

	//TODO: fix for windows line endings
	//TODO: fix for quoted line breaks
	private static final Pattern lineRegex = Pattern.compile("(?<=^|(?:\\r?\\n))(.*?)(?=(?:\\r?\\n)|$)");
	private static final Pattern fieldRegex =
					Pattern.compile("(?:(?:(?<=^|,)(?<!\")\"(.*?)(?<!\")\"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+");

	private static Stream<String> scannerSplit(@NotNull final Pattern pattern, final Scanner scanner) {
		return scanner
						.findAll(pattern)
						.map(MatchResult::group);
	}

	private static List<String> fieldSplit(@SuppressWarnings("SameParameterValue")
	                                       @NotNull final Pattern pattern,
	                                       @NotNull final String line) {
		return scannerSplit(pattern, new Scanner(line))
						.collect(Collectors.toList());
	}

	@Override
	public @NotNull Stream<List<String>> parse(final String content) {
		return parseFromScanner(new Scanner(content));
	}

	@Override
	public @NotNull Stream<List<String>> parse(final File file) {
		try {
			return parseFromScanner(new Scanner(file));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return Stream.empty();
	}

	@Override
	public @NotNull Stream<List<String>> parse(final InputStream input) {
		return parseFromScanner(new Scanner(input));
	}

	@NotNull
	private Stream<List<String>> parseFromScanner(final Scanner scanner) {
		return scannerSplit(lineRegex, scanner)
						.map(l -> fieldSplit(fieldRegex, l));
	}
}
