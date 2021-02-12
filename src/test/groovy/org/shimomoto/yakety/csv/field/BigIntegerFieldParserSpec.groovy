package org.shimomoto.yakety.csv.field

import spock.lang.Specification
import spock.lang.Unroll

class BigIntegerFieldParserSpec extends Specification {

	def "BASICS - bean hash and equals"() {
		given:
		def parser = new BigIntegerFieldParser(Locale.UK)
		def same = new BigIntegerFieldParser(Locale.UK)
		def other = new BigIntegerFieldParser(Locale.JAPAN)

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
		BigIntegerFieldParser parser = new BigIntegerFieldParser(l)

		expect:
		parser.parse(s) == r

		where:
		_  | s                            | l                      | r
		0  | '4,294,967,295'              | Locale.UK              | 4294967295G
		1  | '4,294,967,295'              | Locale.US              | 4294967295G
		2  | '4,294,967,295'              | Locale.JAPAN           | 4294967295G
		3  | '4.294.967.295'              | Locale.ITALY           | 4294967295G
		4  | '4.294.967.295'              | new Locale('es', 'ES') | 4294967295G
		5  | '4.294.967.295'              | new Locale('pt', 'BR') | 4294967295G
		6  | '4.294.967.295'              | new Locale('nl', 'NL') | 4294967295G
		7  | '-9,223,372,036,854,775,808' | Locale.UK              | -9223372036854775808G //TODO: parser is not going over long boundaries
		8  | '-9,223,372,036,854,775,808' | Locale.US              | -9223372036854775808G
		9  | '-9,223,372,036,854,775,808' | Locale.JAPAN           | -9223372036854775808G
		10 | '-9.223.372.036.854.775.808' | Locale.ITALY           | -9223372036854775808G
		11 | '-9.223.372.036.854.775.808' | new Locale('es', 'ES') | -9223372036854775808G
		12 | '-9.223.372.036.854.775.808' | new Locale('pt', 'BR') | -9223372036854775808G
		13 | '-9.223.372.036.854.775.808' | new Locale('nl', 'NL') | -9223372036854775808G
	}

	@Unroll
	def "Parsing #s with #l locale fails without exception"() {
		given:
		BigIntegerFieldParser parser = new BigIntegerFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_  | s                  | l
		0  | '4.294.967.295'    | Locale.UK
		1  | '4.294.967.295'    | Locale.US
		2  | '4.294.967.295'    | Locale.JAPAN
		3  | '4,294,967,295'    | Locale.ITALY
		4  | '4,294,967,295'    | new Locale('es', 'ES')
		5  | '4,294,967,295'    | new Locale('pt', 'BR')
		6  | '4,294,967,295'    | new Locale('nl', 'NL')
		7  | '-999.999.999.999' | Locale.UK
		8  | '-999.999.999.999' | Locale.US
		9  | '-999.999.999.999' | Locale.JAPAN
		10 | '-999,999,999,999' | Locale.ITALY
		11 | '-999,999,999,999' | new Locale('es', 'ES')
		12 | '-999,999,999,999' | new Locale('pt', 'BR')
		13 | '-999,999,999,999' | new Locale('nl', 'NL')
	}

	def "Parsing invalid strings fails without exception"() {
		given:
		BigIntegerFieldParser parser = new BigIntegerFieldParser(l)

		expect:
		parser.parse(s) == null

		where:
		_ | s                  | l
		0 | '+/-9223372036854' | Locale.UK
		1 | '--9223372036854'  | Locale.US
		2 | 'null'             | Locale.ITALY
		3 | '-'                | Locale.JAPAN
		4 | '#234'             | new Locale('es', 'ES')
		5 | '(4.294.967.295)'  | new Locale('pt', 'BR')
		6 | '$4.294.967.295'   | new Locale('nl', 'NL')
	}
}
