package org.shimomoto.yakety.csv.field

import spock.lang.Specification
import spock.lang.Unroll

class BigDecimalFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new BigDecimalFieldParser(Locale.UK)
		def same = new BigDecimalFieldParser(Locale.UK)
		def other = new BigDecimalFieldParser(Locale.JAPAN)

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
		BigDecimalFieldParser parser = new BigDecimalFieldParser(l)

		expect:
		parser.parse(s) == r

		where:
		_  | s                        | l                      | r
		0  | '4,294,967,295.00'       | Locale.UK              | 4294967295.00
		1  | '4,294,967,295.00'       | Locale.US              | 4294967295.00
		2  | '4,294,967,295.00'       | Locale.JAPAN           | 4294967295.00
		3  | '4.294.967.295,000'      | Locale.ITALY           | 4294967295.00
		4  | '4.294.967.295,000'      | new Locale('es', 'ES') | 4294967295.00
		5  | '4.294.967.295,00'       | new Locale('pt', 'BR') | 4294967295.00
		6  | '4.294.967.295,00'       | new Locale('nl', 'NL') | 4294967295.00
		7  | '-9,223,372,036,854.123' | Locale.UK              | -9223372036854.123
		8  | '-9,223,372,036,854.123' | Locale.US              | -9223372036854.123
		9  | '-9,223,372,036,854.123' | Locale.JAPAN           | -9223372036854.123
		10 | '-9.223.372.036.854,123' | Locale.ITALY           | -9223372036854.123
		11 | '-9.223.372.036.854,123' | new Locale('es', 'ES') | -9223372036854.123
		12 | '-9.223.372.036.854,123' | new Locale('pt', 'BR') | -9223372036854.123
		13 | '-9.223.372.036.854,123' | new Locale('nl', 'NL') | -9223372036854.123
	}

	@Unroll
	def "Parsing #s with #l locale fails without exception"() {
		given:
		BigDecimalFieldParser parser = new BigDecimalFieldParser(l)

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
		BigDecimalFieldParser parser = new BigDecimalFieldParser(l)

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
