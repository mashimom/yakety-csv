package org.shimomoto.yakety.csv


import java.util.stream.Collectors

import org.shimomoto.yakety.csv.config.FileFormatConfiguration
import spock.lang.Specification
import spock.lang.Subject

class CsvToRowIndexedTextMapParserSpec extends Specification {

	@SuppressWarnings('GroovyAccessibility')
	def "BASICS - Create new parser from default configuration"() {
		given:
		def cols = ['lala', 'lele', 'lili', 'lolo']
		def config = FileFormatConfiguration.builder().indexColumn('i').columns(cols).build()


		when:
		def parser = CsvToRowIndexedTextMapParser.from(config)

		then:
		parser.fieldRegex.toString() == ',(?=([^"]*"[^"]*")*[^"]*$)'
		parser.quote == '"' as char
		!parser.trim
		parser.escapeQuoteRegex != null
		parser.lineSplitter != null
		parser.columnNames == ['i', *cols]
	}

	@SuppressWarnings('GroovyAccessibility')
	def "BASICS - Create new parser from custom configuration"() {
		given:
		def cols = ['lala', 'lele', 'lili', 'lolo']
		def config = FileFormatConfiguration.builder()
						.parserLocale(Locale.forLanguageTag('pt-BR'))
						.lineBreak('\n' as char)
						.separator(';' as char)
						.quote('|' as char)
						.trim(true)
						.indexColumn('index')
						.columns(cols)
						.build()

		when:
		def parser = CsvToRowIndexedTextMapParser.from(config)

		then:
		parser.quote == '|' as char
		parser.trim
		parser.escapeQuoteRegex != null
		parser.lineSplitter != null
		parser.columnNames == ['index', *cols]
	}

	def "BASICS - equals and hashcode as expected"() {
		given:
		def cols = ['lala', 'lele', 'lili', 'lolo']
		def conf1 = FileFormatConfiguration.builder()
						.trim(true)
						.indexColumn('index')
						.columns(cols)
						.build()
		def conf2 = FileFormatConfiguration.builder()
						.parserLocale(Locale.forLanguageTag('pt-BR'))
						.lineBreak('\n' as char)
						.separator(';' as char)
						.quote('|' as char)
						.trim(true)
						.indexColumn('index')
						.columns(cols)
						.build()
		def conf3 = conf2.toBuilder()
						.columns(['lulu', *cols])
						.build()
		and: 'subjects'
		def parser = CsvToRowIndexedTextMapParser.from(conf1)
		def same = CsvToRowIndexedTextMapParser.from(conf1)
		def other = CsvToRowIndexedTextMapParser.from(conf2)
		def another = CsvToRowIndexedTextMapParser.from(conf3)

		expect: 'parsers from same config have same hash code'
		parser.hashCode() == same.hashCode()
		and: 'parsers from different config have different hash code'
		parser.hashCode() != other.hashCode()
		parser.hashCode() != another.hashCode()

		and: 'parsers from same config are equal'
		parser == same
		and: 'parsers from different config are not equal'
		parser != other
		parser != another
	}

	def "BASICS - Repeated columns will fail"() {
		given:
		def config = FileFormatConfiguration.builder()
						.indexColumn('index')
						.columns(['lala', 'lala', 'lili'])
						.build()
		when:
		CsvToRowIndexedTextMapParser.from(config)

		then:
		thrown IllegalArgumentException
	}

	def "BASICS - null column will fail"() {
		given:
		def config = FileFormatConfiguration.builder()
						.indexColumn('index')
						.columns(['lala', null, 'lili'])
						.build()

		when:
		CsvToRowIndexedTextMapParser.from(config)

		then:
		thrown IllegalArgumentException
	}

	def "BASICS - null index will fail"() {
		given:
		def config = FileFormatConfiguration.builder()
						.indexColumn(null)
						.columns(['lala', 'lele', 'lili'])
						.build()

		when:
		CsvToRowIndexedTextMapParser.from(config)

		then:
		thrown IllegalArgumentException
	}

	def "Parse a simple sample with defaults"() {
		given: 'expected column names'
		def cols = ['Title', 'Release date', 'Phase', 'Film/TV', 'In-universe year']
		and: 'the content to match (including empty line at the end)'
		def contentLines = [
						'Title,                              Release date, Phase, Film/TV, In-universe year',
						'Iron Man,                           2008-05-02,   1,     Film,    2008',
						'The Incredible Hulk,                2008-06-13,   1,     Film,    2009',
						'Iron Man 2,                         2010-04-30,   1,     Film,    2009',
						'Thor,                               2011-04-27,   1,     Film,    2009',
						'Captain America: The First Avenger, 2011-07-29,   1,     Film,    1942-1945',
						'The Avengers,                       2012-04-26,   1,     Film,    2010',
						'']
		def content = contentLines.join('\n')
		and: 'the parser'
		def config = FileFormatConfiguration.builder()
						.trim(true)
						.indexColumn('#')
						.columns(cols)
						.build()
		@Subject
		def parser = CsvToRowIndexedTextMapParser.from(config)

		when:
		List<Map<String, String>> result = parser.parse(content).collect(Collectors.toList())

		then: 'result size is the content lines minus header and empty line at the end'
		result.size() == (contentLines.size() - 2)
		and: 'rows parsed'
		result[0] == ['#': '1', 'Title': 'Iron Man', 'Release date': '2008-05-02', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '2008']
		result[1] == ['#': '2', 'Title': 'The Incredible Hulk', 'Release date': '2008-06-13', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '2009']
		result[2] == ['#': '3', 'Title': 'Iron Man 2', 'Release date': '2010-04-30', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '2009']
		result[3] == ['#': '4', 'Title': 'Thor', 'Release date': '2011-04-27', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '2009']
		result[4] == ['#': '5', 'Title': 'Captain America: The First Avenger', 'Release date': '2011-07-29', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '1942-1945']
		result[5] == ['#': '6', 'Title': 'The Avengers', 'Release date': '2012-04-26', 'Phase': '1', 'Film/TV': 'Film', 'In-universe year': '2010']
	}

	def "Parse a complex sample"() {
		given:
		def contentLines = [
						'Make,Model,Description,Price',
						'Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00',
						'Dell,"","Alienware 38 Curved ""Gaming Monitor""",  6699.00',
						'Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00',
						'Samsung,,"  Promotion! Special Price\n49"" Dual QHD, QLED, HDR1000  ",4999.00',
						''
		]
		def content = contentLines.join('\n')
		and: 'parser'
		def cols = ['Make', 'Model', 'Description', 'Price']
		def config = FileFormatConfiguration.builder()
						.indexColumn('ndx')
						.columns(cols)
						.build()
		@Subject
		def parser = CsvToRowIndexedTextMapParser.from(config)

		when:
		List<Map<String, String>> result = parser.parse(content).collect(Collectors.toList())

		then: 'result size is the content lines minus header and empty line at the end'
		result.size() == (contentLines.size() - 2)
		and: 'line #0'
		result[0]['ndx'] == '1'
		result[0]['Make'] == 'Dell'
		result[0]['Model'] == 'P3421W'
		result[0]['Description'] == 'Dell 34, Curved, USB-C Monitor'
		result[0]['Price'] == '2499.00'
		and: 'line #1'
		result[1]['ndx'] == '2'
		result[1]['Make'] == 'Dell'
		result[1]['Model'] == ''
		result[1]['Description'] == 'Alienware 38 Curved "Gaming Monitor"'
		result[1]['Price'] == '  6699.00'
		and: 'line #2'
		result[2]['ndx'] == '3'
		result[2]['Make'] == 'Samsung'
		result[2]['Model'] == ''
		result[2]['Description'] == '49" Dual QHD, QLED, HDR1000'
		result[2]['Price'] == '6199.00'
		and: 'line #3'
		result[3]['ndx'] == '4'
		result[3]['Make'] == 'Samsung'
		result[3]['Model'] == ''
		result[3]['Description'] == '  Promotion! Special Price\n49" Dual QHD, QLED, HDR1000  '
		result[3]['Price'] == '4999.00'
	}
}
