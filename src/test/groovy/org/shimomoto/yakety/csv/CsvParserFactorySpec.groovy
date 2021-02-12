package org.shimomoto.yakety.csv

import org.shimomoto.yakety.csv.api.BeanAssembly
import org.shimomoto.yakety.csv.api.ColumnDefinition
import org.shimomoto.yakety.csv.config.FileFormatConfiguration
import spock.lang.Specification

class CsvParserFactorySpec extends Specification {
	class MyBean {
		MyBean(Long id, String title) {
			this.id = id
			this.title = title
		}
		Long id
		String title
	}

	interface MyColumn extends ColumnDefinition {}
	MyColumn title
	MyColumn index

	FileFormatConfiguration config
	BeanAssembly<MyColumn, MyBean> beanAssembly = Mock(BeanAssembly)

	void setup() {
		title = new MyColumn() {
			final int order = 0
			final String displayName = 'title'
			final boolean nullable = false
		}
		index = new MyColumn() {
			final int order = -1
			final String displayName = '#'
			final boolean nullable = false
		}
		config = FileFormatConfiguration.builder()
						.parserLocale(Locale.JAPAN)
						.lineBreak('|' as char)
						.separator(';' as char)
						.quote('~' as char)
						.trim(true)
						.indexColumn(index)
						.columns([title])
						.linesBeforeHeader(1)
						.headerless(true)
						.build()
	}

	def "toText - parser to stream of line fields works"() {
		when:
		def result = CsvParserFactory.toText(config.toBuilder().indexColumn(null).columns([]).build())

		then:
		result != null
		result instanceof CsvToTextParser
		result.quote == config.quote
		result.lineBreak == config.lineBreak
		result.trim == config.trim
		result.lineSplitter.lineBreak == (int) config.lineBreak
		result.lineSplitter.quote == (int) config.quote
		result.fieldRegex != null
		result.escapeQuoteRegex != null
	}

	def "toText - parser to stream of line fields works with defaults"() {
		when:
		CsvParserFactory.toText(null)

		then:
		thrown NullPointerException
	}

	def "toTextMap, string columns - parser to stream of fields by columns works"() {
		given:
		def newConfig = config.toBuilder().indexColumn(null).columns(['title']).build()
		when:
		def result = CsvParserFactory.toTextMap(newConfig)

		then:
		result != null
		result instanceof CsvToTextMapParser
		result.quote == config.quote
		result.lineBreak == config.lineBreak
		result.trim == config.trim
		result.lineSplitter.lineBreak == (int) config.lineBreak
		result.lineSplitter.quote == (int) config.quote
		result.fieldRegex != null
		result.escapeQuoteRegex != null
		result.columnNames == ['title']
	}

	def "toTextMap, string columns - fails due to columns duplication"() {
		when:
		CsvParserFactory.toTextMap(config.toBuilder().columns([title,title]).build())

		then:
		thrown IllegalArgumentException
	}

	def "toTextMap, non-string columns - parser to stream of fields by columns works"() {
		given:
		def newConfig = config.toBuilder().indexColumn(null).build()
		when:
		def result = CsvParserFactory.toTextMap(newConfig)

		then:
		result != null
		result instanceof CsvToTextMapParser
		result.quote == config.quote
		result.lineBreak == config.lineBreak
		result.trim == config.trim
		result.lineSplitter.lineBreak == (int) config.lineBreak
		result.lineSplitter.quote == (int) config.quote
		result.fieldRegex != null
		result.escapeQuoteRegex != null
		result.columnNames == [title]
	}

	def "toTextMap, non-string columns - fails on duplicate columns"() {
		when:
		CsvParserFactory.toTextMap(config.toBuilder().columns([title, title]).build())

		then:
		thrown IllegalArgumentException
	}

	def "toRowIndexedTextMap - parser to stream of fields by columns with derived index works"() {
		when:
		def result = CsvParserFactory.toRowIndexedTextMap(config)

		then:
		result != null
		result instanceof CsvToRowIndexedTextMapParser
		result.quote == config.quote
		result.lineBreak == config.lineBreak
		result.trim == config.trim
		result.lineSplitter.lineBreak == (int) config.lineBreak
		result.lineSplitter.quote == (int) config.quote
		result.fieldRegex != null
		result.escapeQuoteRegex != null
		result.columnNames == [index, title]
	}

	def "toRowIndexedTextMap - fails on duplicate columns"() {
		when:
		CsvParserFactory.toRowIndexedTextMap(config.toBuilder().columns([index,title]).build())

		then:
		thrown IllegalArgumentException
	}

	def "toBeans - parser to stream of fields by columns with derived index works"() {
		given:
		FileFormatConfiguration exConfig = FileFormatConfiguration.builder()
						.parserLocale(Locale.JAPAN)
						.lineBreak('|' as char)
						.separator(';' as char)
						.quote('~' as char)
						.trim(true)
						.indexColumn(index)
						.columns([title])
						.build()
		when:
		def result = CsvParserFactory.toBeans(exConfig, beanAssembly)

		then:
		result != null
		result instanceof CsvToBeanParser
		result.delegate instanceof CsvToRowIndexedTextMapParser
		result.beanAssembly == beanAssembly
		result.delegate.quote == config.quote
		result.delegate.lineBreak == config.lineBreak
		result.delegate.trim == config.trim
		result.delegate.lineSplitter.lineBreak == (int) config.lineBreak
		result.delegate.lineSplitter.quote == (int) config.quote
		result.delegate.fieldRegex != null
		result.delegate.escapeQuoteRegex != null
		result.delegate.columnNames == [index, title]
	}

	def "toBeans - fails on duplicate columns"() {
		given:
		FileFormatConfiguration exConfig = FileFormatConfiguration.builder()
						.parserLocale(Locale.JAPAN)
						.lineBreak('|' as char)
						.separator(';' as char)
						.quote('~' as char)
						.trim(true)
						.indexColumn(index)
						.columns([title, index])
						.build()
		when:
		CsvParserFactory.toBeans(exConfig, beanAssembly)

		then:
		thrown IllegalArgumentException
	}
}
