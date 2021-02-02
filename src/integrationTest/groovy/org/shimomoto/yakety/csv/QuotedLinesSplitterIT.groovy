package org.shimomoto.yakety.csv

import java.util.zip.ZipInputStream

import spock.lang.Specification
import spock.lang.Timeout

class QuotedLinesSplitterIT extends Specification {

	@Timeout(3)
	def "Read 17Mb+ file without keeping entire file in memory"() {
		given:
		def getZippedResourceInputStream = { zfr ->
			def zipped = new ZipInputStream(getClass().getResourceAsStream(zfr))
			zipped.nextEntry
			zipped
		}
		def lines = new QuotedLinesSplitter('\n' as char, '"' as char)

		expect:
		lines.parse(getZippedResourceInputStream('./by_city_monthly.zip')).count() == 279838
	}

	@Timeout(10)
	def "Read 200Mb+ file without keeping entire file in memory"() {
		given:
		def getZippedResourceInputStream = { zfr ->
			def zipped = new ZipInputStream(getClass().getResourceAsStream(zfr))
			zipped.nextEntry
			zipped
		}
		def lines = new QuotedLinesSplitter('\n' as char, '"' as char)

		expect:
		lines.parse(getZippedResourceInputStream('./dados-estatisticos.zip')).count() == 891742
	}
}
