package org.shimomoto.yakety.csv

import java.util.zip.ZipInputStream

import spock.lang.Specification
import spock.lang.Timeout

class QuotedLineSplitterIT extends Specification {

	@Timeout(3)
	def "Read 17Mb+ file without keeping entire file in memory"() {
		given:
		def url = new URL('https://raw.githubusercontent.com/samirarman/brazil-death-data/master/merged_data/by_city_monthly.csv')

		def lines = new QuotedLineSplitter('\n' as char, '"' as char)

		expect:
		lines.parse(url.newReader()).count() == 279492
	}

	@Timeout(10)
	def "Read 200Mb+ file without keeping entire file in memory"() {
		given:
		def getZippedResourceInputStream = { zfr ->
			def zipped = new ZipInputStream(getClass().getResourceAsStream(zfr))
			zipped.nextEntry
			zipped
		}
		def lines = new QuotedLineSplitter('\n' as char, '"' as char)

		expect:
		lines.parse(getZippedResourceInputStream('./dados-estatisticos.zip')).count() == 891742
	}
}
