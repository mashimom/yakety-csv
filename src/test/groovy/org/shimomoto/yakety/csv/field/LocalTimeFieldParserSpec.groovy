package org.shimomoto.yakety.csv.field

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import spock.lang.Specification

class LocalTimeFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.UK)
		def same = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.UK)
		def other = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.JAPAN)

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
		def parser = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.US)

		expect:
		parser.parse('23:10:25') == LocalTime.of(23,10,25)
	}

	def "parser fails on format mismatch"() {
		given:
		def parser = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.US)

		expect:
		parser.parse('23h10m25s') == null
	}

	def "parser fails on null input"() {
		given:
		def parser = new LocalTimeFieldParser(DateTimeFormatter.ISO_TIME, Locale.US)

		expect:
		parser.parse(null) == null
	}
}
