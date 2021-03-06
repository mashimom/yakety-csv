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
						ASUS TUF,VG27AQ,"ASUS TUF VG27AQ - QHD IPS Gaming Monitor - 27 inch - 144-165hz",
						'''.stripIndent()

		when:
		def s = new Scanner(content)
		s.useDelimiter(~/\n(?=([^"]*"[^"]*")*[^"]*$)/)
		def result = s.tokens().collect(Collectors.toList())


		then:
		result.size() == 6
		result.first() == 'Make,Model,Description,Price'
		result[4] == '''\
						Samsung,,"Promotion! Special Price
						49"" Dual QHD, QLED, HDR1000",4999.00'''.stripIndent()
	}

	def "parses sample line"() {
		given:
		def content = '''ASUS TUF,VG27AQ,,"ASUS TUF VG27AQ - QHD IPS Gaming Monitor, 27 inch, 144-165hz",'''.stripIndent()

		when:
		def result = (~/,(?=([^"]*"[^"]*")*[^"]*$)/).split(content, 5).toList()

		then:
//		result.size() == 4
		result[0] == 'ASUS TUF'
		result[1] == 'VG27AQ'
		result[2] == ''
		result[3] == '"ASUS TUF VG27AQ - QHD IPS Gaming Monitor, 27 inch, 144-165hz"'
		result[4] == ''
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
