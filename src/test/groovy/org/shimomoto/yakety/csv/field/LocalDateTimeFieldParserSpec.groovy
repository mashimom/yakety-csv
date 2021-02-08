package org.shimomoto.yakety.csv.field


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import spock.lang.Specification

class LocalDateTimeFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.UK)
		def same = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.UK)
		def other = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.JAPAN)

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
		def parser = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.US)

		expect:
		parser.parse('2021-02-08T23:10:25') == LocalDateTime.of(2021, 2, 8, 23, 10, 25)
	}

	def "parser fails on format mismatch"() {
		given:
		def parser = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.US)

		expect:
		parser.parse('08-02-2021 23:10:25') == null
	}

	def "parser fails on null input"() {
		given:
		def parser = new LocalDateTimeFieldParser(DateTimeFormatter.ISO_DATE_TIME, Locale.US)

		expect:
		parser.parse(null) == null
	}
}
