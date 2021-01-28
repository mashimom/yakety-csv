import java.util.stream.Collectors
import java.util.stream.Stream

import org.shimomoto.yakety.csv.CsvParserFactory
import org.shimomoto.yakety.csv.api.CsvParser
import spock.lang.Ignore
import spock.lang.Specification


class MarvelIT extends Specification {

	@Ignore
	def "Create simplets parser"() {
		given:
		def headers = ['Title', ' Release date', ' Phase', ' Film/TV', ' In-universe year']
		def lastLine = ['Untitled Hawkeye series', '', '', ' TV (Disney+)', '']
		when:
		CsvParser<Stream<List<String>>> parser = CsvParserFactory.simple()

		then:
		parser != null

		when:
		def result = parser.parse(this.class.getResourceAsStream('./mcu.csv'))
						.collect(Collectors.toList())

		then:
		result.size() == 69
		result.first() == headers
		result.last().size() == 1
		result[-2] == lastLine
	}
}
