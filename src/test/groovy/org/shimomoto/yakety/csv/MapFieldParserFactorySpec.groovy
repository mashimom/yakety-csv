package org.shimomoto.yakety.csv

import java.time.format.DateTimeFormatter

import org.shimomoto.yakety.csv.field.*
import spock.lang.Specification
import spock.lang.Subject

class MapFieldParserFactorySpec extends Specification {
	Locale loc = Locale.JAPAN
	@Subject
	MapFieldParserFactory factory = new MapFieldParserFactory(loc)

	String someKey = "someKey"

	def "BASICS - own fields, equals and hashcode"() {
		expect:
		factory.locale == loc
		and: 'equals and hashcode works when they are they same'
		factory == new MapFieldParserFactory(loc)
		factory.hashCode() == new MapFieldParserFactory(loc).hashCode()
		and: 'equals and hashcode fails works when they are not they same'
		factory != null
		factory != new Object()
		factory != new MapFieldParserFactory(Locale.CHINA)
		factory.hashCode() != new MapFieldParserFactory(Locale.ITALY).hashCode()
	}

	def "forBigDecimal works"() {
		when:
		def result = factory.forBigDecimal(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof BigDecimalFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forBigInteger works"() {
		when:
		def result = factory.forBigInteger(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof BigIntegerFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forDouble works"() {
		when:
		def result = factory.forDouble(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof DoubleFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forFloat works"() {
		when:
		def result = factory.forFloat(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof FloatFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forInteger works"() {
		when:
		def result = factory.forInteger(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof IntegerFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forLocalDate works"() {
		given:
		java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ISO_DATE

		when:
		def result = factory.forLocalDate(someKey, fmt)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof LocalDateFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forLocalDateTime works"() {
		given:
		java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ISO_DATE_TIME

		when:
		def result = factory.forLocalDateTime(someKey, fmt)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof LocalDateTimeFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forLocalTime works"() {
		given:
		DateTimeFormatter fmt = DateTimeFormatter.ISO_TIME

		when:
		def result = factory.forLocalTime(someKey, fmt)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof LocalTimeFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}

	def "forLong works"() {
		when:
		def result = factory.forLong(someKey)

		then:
		//noinspection GroovyAccessibility
		result.delegate instanceof LongFieldParser
		result.delegate.locale == loc
		result.key == someKey
	}
}
