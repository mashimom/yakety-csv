package org.shimomoto.yakety.csv

import java.util.stream.Collectors

import spock.lang.Specification

class QuotedLineStreamSpec extends Specification {

	def "Read small file dynamically"() {
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
		def lines = new QuotedLineStream(('\n' as char) as int, ('"' as char) as int)

		when:
		def result = lines.parse(content).collect(Collectors.toList())

		then:
		result.size() == 6
	}
}
