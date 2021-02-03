package org.shimomoto.yakety.csv

import java.util.stream.Collectors

import spock.lang.Specification
import spock.lang.Subject

class QuotedLinesSplitterSpec extends Specification {

	def "BASICS"() {
		given:
		def splitter = new QuotedLinesSplitter('\n' as char, '"' as char)
		def same = new QuotedLinesSplitter('\n' as char, '"' as char)
		def other = new QuotedLinesSplitter('|' as char, ',' as char)

		expect:
		splitter == same
		splitter.hashCode() == same.hashCode()
		splitter != other
		splitter.hashCode() != other.hashCode()
		splitter != null
		splitter != new Object()
	}

	def "Read small content with empty line at the end"() {
		given: 'content of 6 lines (including header) plus empty line at the end'
		def contentLines =
						['Make,Model,Description,Price',
						 'Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00',
						 'Dell,"","Alienware 38 Curved ""Gaming Monitor""",6699.00',
						 'Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00',
						 'Samsung,,"Promotion! Special Price\n49"" Dual QHD, QLED, HDR1000",4999.00',
						 'ASUS TUF,VG27AQ,"ASUS \'TUF\' VG27AQ - QHD IPS ""Gaming Monitor"" - 27 inch - 144-165hz",',
						 '']
		def content = contentLines.join('\n')
		and: 'the splitter configured with new line and double quotes'
		@Subject
		def lineSplitter = new QuotedLinesSplitter('\n' as char, '"' as char)

		when:
		def result = lineSplitter.parse(content).collect(Collectors.toList())

		then:
		result.size() == contentLines.size() - 1
		result[0] == contentLines[0]
		result[1] == contentLines[1]
		result[2] == contentLines[2]
		result[3] == contentLines[3]
		result[4] == contentLines[4]
		result[5] == contentLines[5]
	}

	def "Read small content without empty line at the end"() {
		given: 'content of 6 lines (including header) plus empty line at the end'
		def contentLines = [
						'Make,Model,Description,Price',
						'Dell,P3421W,"Dell 34, Curved, USB-C Monitor",2499.00',
						'Dell,"","Alienware 38 Curved ""Gaming Monitor""",6699.00',
						'Samsung,,"49"" Dual QHD, QLED, HDR1000",6199.00',
						'Samsung,,"Promotion! Special Price\n49"" Dual QHD, QLED, HDR1000",4999.00',
						'ASUS TUF,VG27AQ,"ASUS \'TUF\' VG27AQ - \n""QHD IPS Gaming Monitor - \n27 inch - \n144-165hz""",'
		]
		def content = contentLines.join('\n')
		and: 'the splitter configured with new line and double quotes'
		@Subject
		def lineSplitter = new QuotedLinesSplitter('\n' as char, '"' as char)

		when:
		def result = lineSplitter.parse(content).collect(Collectors.toList())

		then:
		result.size() == contentLines.size()
		result[0] == contentLines[0]
		result[1] == contentLines[1]
		result[2] == contentLines[2]
		result[3] == contentLines[3]
		result[4] == contentLines[4]
		result[5] == contentLines[5]
	}
}
