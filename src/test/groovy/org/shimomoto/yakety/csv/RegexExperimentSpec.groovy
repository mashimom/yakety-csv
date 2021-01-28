package org.shimomoto.yakety.csv


import java.util.stream.Collectors

import org.apache.commons.text.StringEscapeUtils
import spock.lang.Specification
import spock.lang.Unroll

class RegexExperimentSpec extends Specification {

	def "parses sample content"() {
		given:
		def content = '''\
						Make,Model,Description,Price
						Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00
						Dell,"","Alienware 38 Curved ""Gaming Monitor""",6699.00
						Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00
						Samsung,,"Promotion! Special Price
						49"" Dual QHD, QLED, HDR1000",4999.00
						'''.stripIndent()

		when:
		def s = new Scanner(content)
		s.useDelimiter(~/\n(?=([^"]*"[^"]*")*[^"]*$)/)
		def result = s.tokens().collect(Collectors.toList())


		then:
		result.size() == 5
		result.first() == 'Make,Model,Description,Price'
		result.last() == '''\
						Samsung,,"Promotion! Special Price
						49"" Dual QHD, QLED, HDR1000",4999.00'''.stripIndent()
	}

	@Unroll
	def "sanity check,pattern escape of [#original]"() {
		expect:
		StringEscapeUtils.escapeJava(original) == quoted

		where:
		_ | original | quoted
		0 | 'abc'    | 'abc'
		1 | '\\'     | '\\\\'
		2 | '\n'     | '\\n'
		3 | '\r\n'   | '\\r\\n'
		4 | ','      | ','
//		5 | '"'      | '"' //does not work, why?
	}
}
