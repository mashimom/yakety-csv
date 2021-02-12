package org.shimomoto.yakety.csv.field

import spock.lang.Specification
import spock.lang.Unroll

class DoubleFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new DoubleFieldParser(Locale.UK)
		def same = new DoubleFieldParser(Locale.UK)
		def other = new DoubleFieldParser(Locale.JAPAN)

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
		DoubleFieldParser parser = new DoubleFieldParser(l)

		expect:
		parser.parse(s) == r

		where:
		_  | s                   | l                      | r
		0  | '4,294,967,295.00'  | Locale.UK              | 4294967295.00D
		1  | '4,294,967,295.00'  | Locale.US              | 4294967295.00D
		2  | '4,294,967,295.00'  | Locale.JAPAN           | 4294967295.00D
		3  | '4.294.967.295,000' | Locale.ITALY           | 4294967295.00D
		4  | '4.294.967.295,000' | new Locale('es', 'ES') | 4294967295.00D
		5  | '4.294.967.295,00'  | new Locale('pt', 'BR') | 4294967295.00D
		6  | '4.294.967.295,00'  | new Locale('nl', 'NL') | 4294967295.00D
		7  | '4.9E-324'          | Locale.UK              | Double.MIN_VALUE
		8  | '4.9E-324'          | Locale.US              | Double.MIN_VALUE
		9  | '4.9E-324'          | Locale.JAPAN           | Double.MIN_VALUE
		10 | '4,9E-324'          | Locale.ITALY           | Double.MIN_VALUE
		11 | '4,9E-324'          | new Locale('es', 'ES') | Double.MIN_VALUE
		12 | '4,9E-324'          | new Locale('pt', 'BR') | Double.MIN_VALUE
		13 | '4,9E-324'          | new Locale('nl', 'NL') | Double.MIN_VALUE
	}

	@Unroll
	def "Parsing #s with #l locale fails without exception"() {
		given:
		DoubleFieldParser parser = new DoubleFieldParser(l)

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
		DoubleFieldParser parser = new DoubleFieldParser(l)

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
