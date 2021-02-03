package org.shimomoto.yakety.csv

import java.time.format.DateTimeFormatter

import org.shimomoto.yakety.csv.field.BigDecimalFieldParser
import org.shimomoto.yakety.csv.field.BigIntegerFieldParser
import org.shimomoto.yakety.csv.field.DoubleFieldParser
import org.shimomoto.yakety.csv.field.FloatFieldParser
import org.shimomoto.yakety.csv.field.IntegerFieldParser
import org.shimomoto.yakety.csv.field.LocalDateFieldParser
import org.shimomoto.yakety.csv.field.LocalDateTimeFieldParser
import org.shimomoto.yakety.csv.field.LocalTimeFieldParser
import org.shimomoto.yakety.csv.field.LongFieldParser
import spock.lang.Specification
import spock.lang.Subject

class FieldParserFactorySpec extends Specification {
	Locale loc = Locale.JAPAN
	@Subject
	FieldParserFactory factory = new FieldParserFactory(loc)

	String someKey = "someKey"

	def "BASICS - own fields, equals and hashcode"() {
		expect:
		factory.locale == loc
		and: 'equals and hashcode works when they are they same'
		factory == new FieldParserFactory(loc)
		factory.hashCode() == new FieldParserFactory(loc).hashCode()
		and: 'equals and hashcode fails works when they are not they same'
		factory != null
		factory != new Object()
		factory != new FieldParserFactory(Locale.CHINA)
		factory.hashCode() != new FieldParserFactory(Locale.ITALY).hashCode()
	}

	def "forBigDecimal works"() {
		when:
		def result = factory.forBigDecimal()

		then:
		//noinspection GroovyAccessibility
		result instanceof BigDecimalFieldParser
		result.locale == loc
	}

	def "forBigInteger works"() {
		when:
		def result = factory.forBigInteger()

		then:
		//noinspection GroovyAccessibility
		result instanceof BigIntegerFieldParser
		result.locale == loc
	}

	def "forDouble works"() {
		when:
		def result = factory.forDouble()

		then:
		//noinspection GroovyAccessibility
		result instanceof DoubleFieldParser
		result.locale == loc
	}

	def "forFloat works"() {
		when:
		def result = factory.forFloat()

		then:
		//noinspection GroovyAccessibility
		result instanceof FloatFieldParser
		result.locale == loc
	}

	def "forInteger works"() {
		when:
		def result = factory.forInteger()

		then:
		//noinspection GroovyAccessibility
		result instanceof IntegerFieldParser
		result.locale == loc
	}

	def "forLocalDate works"() {
		given:
		DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE

		when:
		def result = factory.forLocalDate(fmt)

		then:
		//noinspection GroovyAccessibility
		result instanceof LocalDateFieldParser
		result.locale == loc
	}

	def "forLocalDateTime works"() {
		given:
		DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME

		when:
		def result = factory.forLocalDateTime(fmt)

		then:
		//noinspection GroovyAccessibility
		result instanceof LocalDateTimeFieldParser
		result.locale == loc
	}

	def "forLocalTime works"() {
		given:
		DateTimeFormatter fmt = DateTimeFormatter.ISO_TIME

		when:
		def result = factory.forLocalTime(fmt)

		then:
		//noinspection GroovyAccessibility
		result instanceof LocalTimeFieldParser
		result.locale == loc
	}

	def "forLong works"() {
		when:
		def result = factory.forLong()

		then:
		//noinspection GroovyAccessibility
		result instanceof LongFieldParser
		result.locale == loc
	}
}
