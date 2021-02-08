package org.shimomoto.yakety.csv.field

import spock.lang.Specification
import spock.lang.Unroll

class IntegerFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new IntegerFieldParser(Locale.UK)
		def same = new IntegerFieldParser(Locale.UK)
		def other = new IntegerFieldParser(Locale.JAPAN)

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
		IntegerFieldParser parser = new IntegerFieldParser(l)

		expect:
		parser.parse(s) == r

		where:
		_  | s                | l                      | r
		0  | '1,294,967,295'  | Locale.UK              | 1294967295i
		1  | '1,294,967,295'  | Locale.US              | 1294967295i
		2  | '1,294,967,295'  | Locale.JAPAN           | 1294967295i
		3  | '1.294.967.295'  | Locale.ITALY           | 1294967295i
		4  | '1.294.967.295'  | new Locale('es', 'ES') | 1294967295i
		5  | '1.294.967.295'  | new Locale('pt', 'BR') | 1294967295i
		6  | '1.294.967.295'  | new Locale('nl', 'NL') | 1294967295i
		7  | '-1,854,775,808' | Locale.UK              | -1854775808i
		8  | '-1,854,775,808' | Locale.US              | -1854775808i
		9  | '-1,854,775,808' | Locale.JAPAN           | -1854775808i
		10 | '-1.854.775.808' | Locale.ITALY           | -1854775808i
		11 | '-1.854.775.808' | new Locale('es', 'ES') | -1854775808i
		12 | '-1.854.775.808' | new Locale('pt', 'BR') | -1854775808i
		13 | '-1.854.775.808' | new Locale('nl', 'NL') | -1854775808i
	}

	@Unroll
	def "Parsing #s with #l locale fails without exception"() {
		given:
		IntegerFieldParser parser = new IntegerFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_  | s               | l
		0  | '1,294,967.295' | Locale.UK
		1  | '1,294,967.295' | Locale.US
		2  | '1,294,967.295' | Locale.JAPAN
		3  | '1,294,967.295' | Locale.ITALY
		4  | '1,294,967.295' | new Locale('es', 'ES')
		5  | '1,294,967.295' | new Locale('pt', 'BR')
		6  | '1,294,967.295' | new Locale('nl', 'NL')
		7  | '-9.999.999'    | Locale.UK
		8  | '-9.999.999'    | Locale.US
		9  | '-9.999.999'    | Locale.JAPAN
		10 | '-9,999,999'    | Locale.ITALY
		11 | '-9,999,999'    | new Locale('es', 'ES')
		12 | '-9,999,999'    | new Locale('pt', 'BR')
		13 | '-9,999,999'    | new Locale('nl', 'NL')
	}

	@Unroll
	def "Parsing string #s fails without exception"() {
		given:
		IntegerFieldParser parser = new IntegerFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_ | s             | l
		0 | '+/-9999999'  | Locale.UK
		1 | '--9999999'   | Locale.US
		2 | 'null'        | Locale.ITALY
		3 | '-'           | Locale.JAPAN
		4 | '#234'        | new Locale('es', 'ES')
		5 | '(1,000,000)' | new Locale('pt', 'BR')
		6 | '$1,000,000'  | new Locale('nl', 'NL')
	}
}
