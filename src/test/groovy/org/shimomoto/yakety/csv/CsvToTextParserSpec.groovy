package org.shimomoto.yakety.csv


import java.util.stream.Collectors

import spock.lang.Specification
import spock.lang.Subject

class CsvToTextParserSpec extends Specification {

	FileFormatConfiguration config
	@Subject
	CsvToTextParser parser

	@SuppressWarnings('GroovyAccessibility')
	def "Create new parser from default configuration"() {
		given:
		config = FileFormatConfiguration.builder().build()

		when:
		parser = CsvToTextParser.from(config)

		then:
		parser.lineBreakRegex.toString() == '\\n(?=([^"]*"[^"]*")*[^"]*$)'
		parser.fieldRegex.toString() == ',(?=([^"]*"[^"]*")*[^"]*$)'
		parser.quote == '"' as char
		!parser.trim
	}

	@SuppressWarnings('GroovyAccessibility')
	def "Create new parser from custom configuration"() {
		given:
		config = FileFormatConfiguration.builder()
						.parserLocale(Locale.forLanguageTag('pt-BR'))
						.lineBreak('\r\n')
						.separator(';' as char)
						.quote('|' as char)
						.trim(true)
						.build()

		when:
		parser = CsvToTextParser.from(config)

		then:
		parser.lineBreakRegex.toString() == '\\r\\n(?=([^|]*|[^|]*|)*[^|]*$)'
		parser.fieldRegex.toString() == ';(?=([^|]*|[^|]*|)*[^|]*$)'
		parser.quote == '|' as char
		parser.trim
	}

	def "Parse a simple sample with defaults"() {
		given:
		parser = CsvToTextParser.from(FileFormatConfiguration.builder().trim(true).build())
		def content = '''\
								Title,                              Release date, Phase, Film/TV, In-universe year
								Iron Man,                           2008-05-02,   1,     Film,    2008
								The Incredible Hulk,                2008-06-13,   1,     Film,    2009
								Iron Man 2,                         2010-04-30,   1,     Film,    2009
								Thor,                               2011-04-27,   1,     Film,    2009
								Captain America: The First Avenger, 2011-07-29,   1,     Film,    1942-1945
								The Avengers,                       2012-04-26,   1,     Film,    2010
								'''.stripIndent()
		when:
		def result = parser.parse(content).collect(Collectors.toList())

		then:
		result.size() == 7
		result[0] == ['Title', 'Release date', 'Phase', 'Film/TV', 'In-universe year']
		result[1] == ['Iron Man', '2008-05-02', '1', 'Film', '2008']
		result[2] == ['The Incredible Hulk', '2008-06-13', '1', 'Film', '2009']
		result[3] == ['Iron Man 2', '2010-04-30', '1', 'Film', '2009']
		result[4] == ['Thor', '2011-04-27', '1', 'Film', '2009']
		result[5] == ['Captain America: The First Avenger', '2011-07-29', '1', 'Film', '1942-1945']
		result[6] == ['The Avengers', '2012-04-26', '1', 'Film', '2010']
	}

	def "Parse a complex sample"() {
		given:
		parser = CsvToTextParser.from(FileFormatConfiguration.builder().build())
		def content = '''\
						Make,Model,Description,Price
						Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00
						Dell,"","Alienware 38 Curved ""Gaming Monitor""",  6699.00
						Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00
						Samsung,,"  Promotion! Special Price
						49"" Dual QHD, QLED, HDR1000  ",4999.00
						'''.stripIndent()

		when:
		def result = parser.parse(content).collect(Collectors.toList())

		then:
		result.size() == 5
		and: 'header line as expected'
		def firstLine = result[0]
		firstLine[0] == 'Make'
		firstLine[1] == 'Model'
		firstLine[2] == 'Description'
		firstLine[3] == 'Price'
		and: 'second line as expected'
		def secondLine = result[1]
		secondLine[0] == 'Dell'
		secondLine[1] == 'P3421W'
		secondLine[2] == 'Dell 34, Curved, USB-C Monitor'
		secondLine[3] == '2499.00'
		def thirdLine = result[2]
		thirdLine[0] == 'Dell'
		thirdLine[1] == ''
		thirdLine[2] == 'Alienware 38 Curved "Gaming Monitor"'
		thirdLine[3] == '  6699.00'
		def fourthLine = result[3]
		fourthLine[0] == 'Samsung'
		fourthLine[1] == ''
		fourthLine[2] == '49" Dual QHD, QLED, HDR1000'
		fourthLine[3] == '6199.00'
		def fifthLine = result.last()
		fifthLine[0] == 'Samsung'
		fifthLine[1] == ''
		fifthLine[2] == '  Promotion! Special Price\n49" Dual QHD, QLED, HDR1000  '
		fifthLine[3] == '4999.00'
	}
}
