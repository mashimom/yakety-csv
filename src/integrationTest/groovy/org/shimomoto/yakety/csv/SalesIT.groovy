package org.shimomoto.yakety.csv

import java.util.stream.Collectors
import java.util.zip.ZipInputStream

import configuration.sales.SalesAssembly
import configuration.sales.SalesColumns
import configuration.sales.SalesVirtualColumns
import org.shimomoto.yakety.csv.config.FileFormatConfiguration
import spock.lang.Specification


class SalesIT extends Specification {
	def "Parse all data and check count"() {
		given:
		def getZippedResourceInputStream = { zfr ->
			def zipped = new ZipInputStream(getClass().getResourceAsStream(zfr))
			zipped.nextEntry
			zipped
		}
		def config = FileFormatConfiguration.builder()
						.indexColumn(SalesVirtualColumns.INDEX)
						.columns(SalesColumns.values().toList())
						.build()
		def assembly = new SalesAssembly(config.parserLocale)
		and: 'a parser'
		def parser = CsvParserFactory.toBeans(config, assembly)
		and: 'data'
		def bis = new BufferedInputStream(getZippedResourceInputStream('sales_records_1m.zip'))

		expect:
		parser.parse(bis).count() == 1000000
	}
}
