package org.shimomoto.yakety.csv.field

import spock.lang.Specification
import spock.lang.Unroll

class FloatFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new FloatFieldParser(Locale.UK)
		def same = new FloatFieldParser(Locale.UK)
		def other = new FloatFieldParser(Locale.JAPAN)

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

	@Unroll
	def "Parsing #s with #l locale works"() {
		given:
		FloatFieldParser parser = new FloatFieldParser(l)

		expect:
		parser.parse(s) == r

		where:
		_  | s                | l                      | r
		0  | '1,234,567.90'   | Locale.UK              | 1234567.9F
		1  | '1,234,567.90'   | Locale.US              | 1234567.9F
		2  | '1,234,567.90'   | Locale.JAPAN           | 1234567.9F
		3  | '1.234.567,900'  | Locale.ITALY           | 1234567.9F
		4  | '1.234.567,900'  | new Locale('es', 'ES') | 1234567.9F
		5  | '1.234.567,90'   | new Locale('pt', 'BR') | 1234567.9F
		6  | '1.234.567,90'   | new Locale('nl', 'NL') | 1234567.9F
		7  | '-1,876,543.210' | Locale.UK              | -1876543.21F
		8  | '-1,876,543.210' | Locale.US              | -1876543.21F
		9  | '-1,876,543.210' | Locale.JAPAN           | -1876543.21F
		10 | '-1.876.543,210' | Locale.ITALY           | -1876543.21F
		11 | '-1.876.543,210' | new Locale('es', 'ES') | -1876543.21F
		12 | '-1.876.543,210' | new Locale('pt', 'BR') | -1876543.21F
		13 | '-1.876.543,210' | new Locale('nl', 'NL') | -1876543.21F
	}

	@Unroll
	def "Parsing #s with #l locale fails without exception"() {
		given:
		FloatFieldParser parser = new FloatFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_  | s                            | l
		0  | '4.294.967.295,00'           | Locale.UK
		1  | '4.294.967.295,00'           | Locale.US
		2  | '4.294.967.295,00'           | Locale.JAPAN
		3  | '4,294,967,295.000'          | Locale.ITALY
		4  | '4,294,967,295.000'          | new Locale('es', 'ES')
		5  | '4,294,967,295.00'           | new Locale('pt', 'BR')
		6  | '4,294,967,295.00'           | new Locale('nl', 'NL')
		7  | '-999.999.999.999,999999999' | Locale.UK
		8  | '-999.999.999.999,999999999' | Locale.US
		9  | '-999.999.999.999,999999999' | Locale.JAPAN
		10 | '-999,999,999,999.999999999' | Locale.ITALY
		11 | '-999,999,999,999.999999999' | new Locale('es', 'ES')
		12 | '-999,999,999,999.999999999' | new Locale('pt', 'BR')
		13 | '-999,999,999,999.999999999' | new Locale('nl', 'NL')
	}

	def "Parsing invalid strings fails without exception"() {
		given:
		FloatFieldParser parser = new FloatFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_ | s                    | l
		0 | '+/-9223372036.854'  | Locale.UK
		1 | '--9223372036.854'   | Locale.US
		2 | 'null'               | Locale.ITALY
		3 | '-'                  | Locale.JAPAN
		4 | '#23.4'              | new Locale('es', 'ES')
		5 | '(4.294.967.295,00)' | new Locale('pt', 'BR')
		6 | '$4.294.967.295,00'  | new Locale('nl', 'NL')
	}
}
