package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.shimomoto.yakety.csv.api.ColumnDefinition;
import org.shimomoto.yakety.csv.api.CsvParser;
import org.shimomoto.yakety.csv.api.ParserConfiguration;

import java.util.List;
import java.util.stream.Stream;

public class CsvParserFactory {

	enum Options {
		TRIM_HEADERS_AND_FIELDS,
		NO_EMPTY_LINE_AT_END;
	}

	static CsvParser<Stream<List<String>>> simple() {
		return new ValueCsvParser();
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	@RequiredArgsConstructor
	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	class ColumnDefinitionConfiguration<C extends ColumnDefinition> implements ParserConfiguration<C> {
		List<C> cols;

		@Override
		public List<C> getCols() {
			return cols;
		}
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	@RequiredArgsConstructor
	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	class ColumnStringConfiguration implements ParserConfiguration<String> {
		List<String> cols;

		@Override
		public List<String> getCols() {
			return cols;
		}
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	@RequiredArgsConstructor
	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	class ColumnIndexConfiguration implements ParserConfiguration<Integer> {
		List<Integer> cols;

		@Override
		public List<Integer> getCols() {
			return cols;
		}
	}
}
