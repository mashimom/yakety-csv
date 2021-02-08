package org.shimomoto.yakety.csv.field

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import spock.lang.Specification

class LocalDateFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.UK)
		def same = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.UK)
		def other = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.JAPAN)

		expect: 'hash and equals match'
		parser == same
		parser.hashCode() == parser.hashCode()
		parser.toString() == same.toString()
		and: 'hash and equals do not match'
		parser != null
		parser != new Object()
		parser != other
		parser.hashCode() != other.hashCode()
	}

	def "parser works"() {
		given:
		def parser = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.US)

		expect:
		parser.parse('2021-02-08') == LocalDate.of(2021, 2, 8)
	}

	def "parser fails on format mismatch"() {
		given:
		def parser = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.US)

		expect:
		parser.parse('08-02-2021') == null
	}

	def "parser fails on null input"() {
		given:
		def parser = new LocalDateFieldParser(DateTimeFormatter.ISO_DATE, Locale.US)

		expect:
		parser.parse(null) == null
	}
}
