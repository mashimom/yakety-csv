package org.shimomoto.yakety.csv.field

import org.shimomoto.yakety.csv.field.api.FieldParser
import spock.lang.Specification
import spock.lang.Subject

class MapDelegatedParserSpec extends Specification {

	FieldParser<Integer> delegate = Mock(FieldParser)
	String key = 'my column'

	@Subject
	MapDelegatedParser<Integer, String> fieldParser = new MapDelegatedParser<Integer, String>(delegate, key)

	def "BASICS - equals and hash"() {
		given:
		FieldParser<Integer> anotherDelegate = new FieldParser<Integer>() {
			@Override
			Integer parse(String value) {
				return -1i
			}
		}
		and: 'field parsers'
		MapDelegatedParser<Integer, String> same = new MapDelegatedParser<Integer, String>(delegate, key)
		MapDelegatedParser<Integer, String> other = new MapDelegatedParser<Integer, String>(delegate, 'other')
		MapDelegatedParser<Integer, String> another = new MapDelegatedParser<Integer, String>(anotherDelegate, key)

		expect: 'matches'
		fieldParser.hashCode() == fieldParser.hashCode()
		fieldParser == fieldParser
		fieldParser.hashCode() == same.hashCode()
		fieldParser == same
		and: 'different'
		fieldParser != null
		fieldParser != new Object()
		fieldParser.hashCode() != other.hashCode()
		fieldParser != other
		fieldParser.hashCode() != another.hashCode()
		fieldParser != another
	}

	def "parse works"() {
		given:
		Map<String, String> someMap = Mock(Map)

		when:
		def result = fieldParser.parse(someMap)

		then:
		result == 1i
		and: 'interactions'
		1 * someMap.get(key) >> 'some value'
		1 * delegate.parse('some value') >> 1i
		0 * _
	}
}
