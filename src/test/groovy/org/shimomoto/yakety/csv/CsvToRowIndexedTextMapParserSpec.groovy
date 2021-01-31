package org.shimomoto.yakety.csv


import java.util.stream.Collectors

import spock.lang.Specification
import spock.lang.Subject

class CsvToRowIndexedTextMapParserSpec extends Specification {

	@SuppressWarnings('GroovyAccessibility')
	def "BASICS - Create new parser from default configuration"() {
		given:
		def config = FileFormatConfiguration.builder().build()
		def cols = ['lala', 'lele', 'lili', 'lolo']

		when:
		def parser = CsvToRowIndexedTextMapParser.from(config, 'i', cols)

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
		def config = FileFormatConfiguration.builder()
						.parserLocale(Locale.forLanguageTag('pt-BR'))
						.lineBreak('\n' as char)
						.separator(';' as char)
						.quote('|' as char)
						.trim(true)
						.build()
		def cols = ['lala', 'lele', 'lili', 'lolo']

		when:
		def parser = CsvToRowIndexedTextMapParser.from(config, 'index', cols)

		then:
		parser.quote == '|' as char
		parser.trim
		parser.escapeQuoteRegex != null
		parser.lineSplitter != null
		parser.columnNames == ['index', *cols]
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
		@Subject
		def parser = CsvToRowIndexedTextMapParser.from(FileFormatConfiguration.builder().trim(true).build(), '#', cols)

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
		def cols = ['Make', 'Model', 'Description', 'Price']
		def parser = CsvToRowIndexedTextMapParser.from(FileFormatConfiguration.builder().build(), 'ndx', cols)
		def contentLines = [
						'Make,Model,Description,Price',
						'Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00',
						'Dell,"","Alienware 38 Curved ""Gaming Monitor""",  6699.00',
						'Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00',
						'Samsung,,"  Promotion! Special Price\n49"" Dual QHD, QLED, HDR1000  ",4999.00',
						''
		]
		def content = contentLines.join('\n')

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
