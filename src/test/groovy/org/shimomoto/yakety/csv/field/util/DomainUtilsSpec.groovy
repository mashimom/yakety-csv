package org.shimomoto.yakety.csv.field.util

import java.util.function.Predicate

import spock.lang.Specification

class DomainUtilsSpec extends Specification {

	def "find has a result"() {
		given:
		Predicate<String> predicate = { e -> 'second' == e }
		List<String> domain = ['first', 'second', 'third']

		expect:
		DomainUtils.find(predicate, domain.stream()) == Optional.of('second')
	}

	def "find has no result"() {
		given:
		Predicate<String> predicate = { e -> 'fourth' == e }
		List<String> domain = ['first', 'second', 'third']

		expect:
		DomainUtils.find(predicate, domain.stream()) == Optional.empty()
	}

	def "findOrNull has a result"() {
		given:
		Predicate<String> predicate = { e -> 'second' == e }
		List<String> domain = ['first', 'second', 'third']

		expect:
		DomainUtils.findOrNull(predicate, domain.stream()) == 'second'
	}

	def "findOrNull has no result"() {
		given:
		Predicate<String> predicate = { e -> 'fourth' == e }
		List<String> domain = ['first', 'second', 'third']

		expect:
		DomainUtils.findOrNull(predicate, domain.stream()) == null
	}
}
