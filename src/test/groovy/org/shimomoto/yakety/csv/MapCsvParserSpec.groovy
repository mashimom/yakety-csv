package org.shimomoto.yakety.csv

import java.util.stream.Collectors

import org.shimomoto.yakety.csv.api.CsvParser
import spock.lang.Specification
import spock.lang.Subject

class MapCsvParserSpec extends Specification {

	def delegated = Mock(CsvParser)
	def cols = ['id', 'name', 'tags', 'price', 'external_id']
	def vals = ['42', '"The Clojure Workshop"', '"clojure,programming,training,it"', '60.23', '"123456789"']

	@Subject
	MapCsvParser<String> parser = new MapCsvParser<>(delegated, cols)

	def "parse works for columns to map keys"() {
		when:
		def result = parser.parse("some content").collect(Collectors.toList())

		then:
		result != null
		and: "header"
		result[0].keySet() == cols.toSet()
		result[0].values().toSet() == cols.toSet()
		and: "body"
		result[1].keySet() == cols.toSet()
		result[1].values().toSet() == vals.toSet()
		and: 'interactions'
		delegated.parse("some content") >> [cols.stream(), vals.stream()].stream()
	}
}
