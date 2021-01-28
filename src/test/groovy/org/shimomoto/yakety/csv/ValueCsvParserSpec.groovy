package org.shimomoto.yakety.csv

import java.util.stream.Collectors
import java.util.stream.Stream

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class ValueCsvParserSpec extends Specification {

	@Subject
	ValueCsvParser parser = new ValueCsvParser()

	@Unroll
	def "scannerSplit of #values #lbr, works"() {
		given:
		def content = values.join(lbr == 'LF' ? '\n' : '\r\n')

		when:
		//noinspection GroovyAccessibility
		def result = ValueCsvParser.scannerSplit(ValueCsvParser.lineRegex, new Scanner(content)).collect(Collectors.toList())

		then:
		result.size() == values.size()
		result == values

		where:
		_ | values                    | lbr
		0 | ['']                      | 'LF'
		0 | ['']                      | 'CRLF'
		1 | ['a', '']                 | 'LF'
		1 | ['a', '']                 | 'CRLF'
		2 | ['', 'b']                 | 'LF'
		2 | ['', 'b']                 | 'CRLF'
		3 | ['a', 'b']                | 'LF'
		3 | ['a', 'b']                | 'CRLF'
		4 | ['a\t', 'b,c,d', 'e,f,g'] | 'LF'
		4 | ['a\t', 'b,c,d', 'e,f,g'] | 'CRLF'
	}

	@Unroll
	def "scannerSplit of #values fails"() {
		given:
		def content = values.join(lbr == 'LF' ? '\n' : '\r\n')

		when:
		//noinspection GroovyAccessibility
		def result = ValueCsvParser.scannerSplit(ValueCsvParser.lineRegex, new Scanner(content)).collect(Collectors.toList())

		then:
		result.size() != values.size()
		result != values

		where:
		_ | values                        | lbr
		0 | ['n\t', '"b,\nc,d"', 'e,f,g'] | 'LF' //quoted line breaks
	}

	@Unroll
	def "fieldSplit of #values works"() {
		given:
		def content = values.join(',')
//		def pattern = ~'(?:(?<=^|,)(.*?)(?=,|$)))+' //no quotes
//		def pattern = ~'(?:(?:(?<=^|,)"(.*?)"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+' //no double quotes
//		def pattern = ~'(?:(?:(?<=^|,)(?<!")"(.*?)(?<!")"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+' //complete

		when:
		//noinspection GroovyAccessibility
		def result = ValueCsvParser.fieldSplit(ValueCsvParser.fieldRegex, content)

		then:
		result.size() == values.size()
		result == values

		where:
		_ | values
		0 | ['']
		1 | ['42 ', '"xyz"', ' 60.23', '"1234"']
		2 | ['42', '"xyz"', '60.23', '"1234"']
		3 | ['"a,b,c"']
		4 | ['"a, b , c"']
		5 | ['"a,b,c"', '"c,d,e"']
		6 | ['42', '"a,b,c"']
		7 | ['42', '"a,""b,c"']
		8 | ['42', '"a,b"",c"']
	}

	def "Parser reads content and returns stream"() {
		given:
		def lines = [['id', 'name', 'tags', 'price', 'external_id'],
		             ['42', '"The Clojure Workshop"', '"clojure,programming,training,it"', '60.23', '"123456789"']]
		def content = lines.collect { it.join(',') }.join('\n')

		when:
		List<Stream<String>> result = parser.parseFromScanner(new Scanner(content))
						.collect(Collectors.toList())

		then:
		result.size() == 2
		result[0] == lines[0]
		result[1] == lines[1]
	}
}
