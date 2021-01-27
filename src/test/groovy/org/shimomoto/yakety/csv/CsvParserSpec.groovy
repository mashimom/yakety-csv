package org.shimomoto.yakety.csv

import java.util.stream.Collectors
import java.util.stream.Stream

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CsvParserSpec extends Specification {

	@Subject
	CsvParser parser = new CsvParser()

	@Unroll
	def "lineSplit of #values"() {
		given:
		def content = values.join(',')
//		def pattern = ~'(?:(?<=^|,)(.*?)(?=,|$)))+' //no quotes
//		def pattern = ~'(?:(?:(?<=^|,)"(.*?)"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+' //no double quotes
		def pattern = ~'(?:(?:(?<=^|,)(?<!")"(.*?)(?<!")"(?=,|$))|(?:(?<=^|,)(.*?)(?=,|$)))+' //complete

		when:
		def result = CsvParser.lineSplit(pattern, content).collect(Collectors.toList())

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
		List<Stream<String>> result = parser.parse(content)
						.map({ s -> s.collect(Collectors.toList()) })
						.collect(Collectors.toList())

		then:
		result.size() == 2
		result[0] == lines[0]
		result[1] == lines[1]
	}
}
