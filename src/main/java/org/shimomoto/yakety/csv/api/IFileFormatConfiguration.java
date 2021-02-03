package org.shimomoto.yakety.csv.api;

public interface IFileFormatConfiguration {
	java.util.Locale getParserLocale();

	char getLineBreak();

	char getSeparator();

	char getQuote();

	boolean isTrim();
}
