package org.shimomoto.yakety.csv


import java.util.stream.Collectors

import spock.lang.Specification
import spock.lang.Subject

class ValueCsvParserReduxSpec extends Specification {

	ParserConfigurationRedux config
	@Subject
	ValueCsvParserRedux parser

	@SuppressWarnings('GroovyAccessibility')
	def "Create new parser from default configuration"() {
		given:
		config = ParserConfigurationRedux.builder().build()

		when:
		parser = ValueCsvParserRedux.from(config)

		then:
		parser.lineBreakRegex.toString() == '\\n(?=([^"]*"[^"]*")*[^"]*$)'
		parser.fieldRegex.toString() == ',(?=([^"]*"[^"]*")*[^"]*$)'
		parser.quote == '"' as char
		!parser.trim
	}

	@SuppressWarnings('GroovyAccessibility')
	def "Create new parser from custom configuration"() {
		given:
		config = ParserConfigurationRedux.builder()
						.parserLocale(Locale.forLanguageTag('pt-BR'))
						.lineBreak('\r\n')
						.separator(';' as char)
						.quote('|' as char)
						.trim(true)
						.build()

		when:
		parser = ValueCsvParserRedux.from(config)

		then:
		parser.lineBreakRegex.toString() == '\\r\\n(?=([^|]*|[^|]*|)*[^|]*$)'
		parser.fieldRegex.toString() == ';(?=([^|]*|[^|]*|)*[^|]*$)'
		parser.quote == '|' as char
		parser.trim
	}

	def "Parse a simple sample with defaults"() {
		given:
		parser = ValueCsvParserRedux.from(ParserConfigurationRedux.builder().trim(true).build())
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
		result.first() == ['Title', 'Release date', 'Phase', 'Film/TV', 'In-universe year']
		result.last() == ['The Avengers', '2012-04-26', '1', 'Film', '2010']
	}

	def "Parse a complex sample"() {
		given:
		parser = ValueCsvParserRedux.from(ParserConfigurationRedux.builder().build())
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
		result.first() == ['Make', 'Model', 'Description', 'Price']
		def thirdLine = result[2]
		thirdLine[0] == 'Dell'
		thirdLine[1] == ''
		thirdLine[2] == 'Alienware 38 Curved "Gaming Monitor"'
		thirdLine[3] == '  6699.00'
		def lastLine = result.last()
		lastLine[0] == 'Samsung'
		lastLine[1] == ''
		lastLine[2] == '  Promotion! Special Price\n49" Dual QHD, QLED, HDR1000  '
		lastLine[3] == '4999.00'
	}
}
