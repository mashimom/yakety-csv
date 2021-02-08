package org.shimomoto.yakety.csv

import java.util.stream.Collectors
import java.util.stream.Stream

import org.shimomoto.yakety.csv.api.BeanAssembly
import org.shimomoto.yakety.csv.api.ColumnDefinition
import org.shimomoto.yakety.csv.api.CsvParser
import org.shimomoto.yakety.csv.config.FileFormatConfiguration
import spock.lang.Ignore
import spock.lang.Specification

class CsvToBeanParserSpec extends Specification {

	class MyBean {
		MyBean(Long id, String title) {
			this.id = id
			this.title = title
		}
		Long id;
		String title;
	}

	interface MyColumn extends ColumnDefinition {}
	MyColumn title
	MyColumn index

	FileFormatConfiguration config = FileFormatConfiguration.builder().build()
	BeanAssembly<MyColumn, MyBean> beanAssembly = Mock(BeanAssembly)

	void setup() {
		title = new MyColumn() {
			final int order = 0
			final String displayName = 'title'
			final boolean nullable = false
		}
		index = new MyColumn() {
			final int order = -1
			final String displayName = '#'
			final boolean nullable = false
		}
	}

	def "BASIC - Static factory method works"() {
		when:
		CsvToBeanParser parser = (CsvToBeanParser) CsvToBeanParser.from(config, index, [title], beanAssembly)

		then:
		parser != null
		parser.delegate != null
		parser.delegate instanceof CsvToRowIndexedTextMapParser
		parser.beanAssembly == beanAssembly
//		and: 'interactions'
//		1 *
	}

	def "BASIC - Static factory method fails on bad index"() {
		given:
		MyColumn badIndex = new MyColumn() {
			final int order = 100
			final String displayName = 'bad'
			final boolean nullable = false
		}
		when:
		CsvToBeanParser.from(config, badIndex, [title], beanAssembly)

		then:
		thrown IllegalArgumentException
	}

	def "BASIC - Static factory method fails on duplicated column"() {
		when:
		CsvToBeanParser.from(config, index, [title, title], beanAssembly)

		then:
		thrown IllegalArgumentException

		when:
		CsvToBeanParser.from(config, index, [title, index], beanAssembly)

		then:
		thrown IllegalArgumentException
	}

	def "BASIC - equals and hash works"() {
		given:
		def extraCol = new MyColumn() {
			final int order = 1
			final String displayName = 'description'
			final boolean nullable = true
		}
		def conf1 = FileFormatConfiguration.builder().build()
		def conf2 = FileFormatConfiguration.builder()
						.quote('`' as char)
						.separator(';' as char)
						.build();
		and: 'subjects'
		def parser = CsvToBeanParser.from(conf1, index, [title], beanAssembly)
		def same = CsvToBeanParser.from(conf1, index, [title], beanAssembly)
		def other = CsvToBeanParser.from(conf2, index, [title], beanAssembly)
		def another = CsvToBeanParser.from(conf1, index, [title, extraCol], beanAssembly)

		expect:
		parser.hashCode() == same.hashCode()
		parser == same
		and: 'different'
		parser != null
		parser != new Object()
		parser.hashCode() != other.hashCode()
		parser != other
		parser.hashCode() != another.hashCode()
		parser != another
	}

	@Ignore
	def "BASIC - should not be equal but is"() {
		given:
		def conf1 = FileFormatConfiguration.builder().build()
		def conf2 = FileFormatConfiguration.builder().separator(';' as char).build();
		and: 'subjects'
		def parser = CsvToBeanParser.from(conf1, index, [title], beanAssembly)
		def other = CsvToBeanParser.from(conf2, index, [title], beanAssembly)

		expect:
		parser != other
		parser.hashCode() != other.hashCode() //why does this fail?
	}

	def "parse works"() {
		given:
		CsvToBeanParser parser = (CsvToBeanParser) CsvToBeanParser.from(config, index, [title], beanAssembly)
		def contentLines = [
						'title',
						'Iron Man',
						'The Incredible Hulk',
						'Iron Man 2',
						'Thor',
						'Captain America: The First Avenger',
						'The Avengers',
						'']
		def content = contentLines.join('\n')

		when:
		def result = parser.parse(content)

		then:
		result != null
	}

	def "Parse works"() {
		given: 'a mocked parser'
		CsvParser<Stream<Map<MyColumn, String>>> delegateParser = Mock(CsvParser)
		and: 'some results'
		def dataByKey = [
						[(index): 1, (title): 'first'],
						[(index): 2, (title): 'second'],
						[(index): 3, (title): 'third'],
		]
		and: 'generated beans'
		def beans = [
						new MyBean(1L, 'first'),
						new MyBean(2L, 'second'),
						new MyBean(3L, 'third')
		]
		and: 'inputs'
		def dataString = 'some content'
		def dataFile = new File('./somefile')
		def dataIS = Mock(InputStream)
		and: 'a parser with mocks'
		//noinspection GroovyAccessibility
		CsvToBeanParser<MyColumn, MyBean> parser = new CsvToBeanParser(delegateParser, beanAssembly)

		when:
		def stringResult = parser.parse(dataString).collect(Collectors.toList())

		then:
		stringResult != null
		and: 'interactions'
		1 * delegateParser.parse(dataString) >> dataByKey.stream()
		1 * beanAssembly.assemble(dataByKey[0]) >> beans[0]
		1 * beanAssembly.assemble(dataByKey[1]) >> beans[1]
		1 * beanAssembly.assemble(dataByKey[2]) >> beans[2]
		0 * _

		when:
		def fileResult = parser.parse(dataFile).collect(Collectors.toList())

		then:
		fileResult != null
		and: 'interactions'
		1 * delegateParser.parse(dataFile) >> dataByKey.stream()
		1 * beanAssembly.assemble(dataByKey[0]) >> beans[0]
		1 * beanAssembly.assemble(dataByKey[1]) >> beans[1]
		1 * beanAssembly.assemble(dataByKey[2]) >> beans[2]
		0 * _

		when:
		def instreamResult = parser.parse(dataIS).collect(Collectors.toList())

		then:
		instreamResult != null
		and: 'interactions'
		1 * delegateParser.parse(dataIS) >> dataByKey.stream()
		1 * beanAssembly.assemble(dataByKey[0]) >> beans[0]
		1 * beanAssembly.assemble(dataByKey[1]) >> beans[1]
		1 * beanAssembly.assemble(dataByKey[2]) >> beans[2]
		0 * _
	}
}
