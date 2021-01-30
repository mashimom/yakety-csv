package org.shimomoto.yakety.csv


import spock.lang.IgnoreRest
import spock.lang.Specification

class QuotedLineStreamIT extends Specification {

	def "Read large file without taking much memory"() {
		given:
		def url = new URL('https://raw.githubusercontent.com/samirarman/brazil-death-data/master/merged_data/by_city_monthly.csv')

		def lines = new QuotedLineStream(('\n' as char) as int, ('"' as char) as int)

		expect:
		lines.parse(url.newReader()).count() == 279492
	}

	@IgnoreRest
	def "Read gigantic file without taking much memory"() {
		given:
//		def getFileStream = {
//			ZipInputStream zipped = new ZipInputStream(getClass().getResourceAsStream('./500000_sales_records.zip'))
//			zipped.nextEntry
//			zipped
//			}
		def lines = new QuotedLineStream(('\n' as char) as int, ('"' as char) as int)

		expect:
		lines.parse(getClass().getResourceAsStream('./INFLUD-25-01-2021.csv')).count() == 1163444
	}
}
