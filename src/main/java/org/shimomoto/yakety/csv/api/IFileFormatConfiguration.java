package org.shimomoto.yakety.csv.api;

import java.util.List;
import java.util.Locale;

public interface IFileFormatConfiguration<C> {
	Locale getParserLocale();

	char getLineBreak();

	char getSeparator();

	char getQuote();

	boolean isTrim();

	C getIndexColumn();

	List<C> getColumns();

	int getLinesBeforeHeader();

	boolean isHeaderless();
}
