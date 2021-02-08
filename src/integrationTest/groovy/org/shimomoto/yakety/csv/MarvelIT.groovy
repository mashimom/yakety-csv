package org.shimomoto.yakety.csv

import java.time.LocalDate
import java.util.stream.Collectors

import static configuration.marvel.csv.MarvelUniverseColumns.*
import static configuration.marvel.csv.MarvelUniverseIndexColumn.INDEX
import configuration.marvel.api.IMarvelUniverseColumn
import configuration.marvel.csv.MarvelUniverseBeanAssembly
import configuration.marvel.csv.MarvelUniverseMedia
import configuration.marvel.domain.MCUPhases
import configuration.marvel.domain.MediaVehicle
import org.apache.commons.lang3.Range
import org.shimomoto.yakety.csv.config.ExtendedFileFormatConfiguration
import org.shimomoto.yakety.csv.config.FileFormatConfiguration
import spock.lang.Specification

class MarvelIT extends Specification {

	MarvelUniverseMedia ironMan = MarvelUniverseMedia.builder()
					.id(1L)
					.title('Iron Man')
					.releaseDate(LocalDate.of(2008, 5, 2))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.is(2008))
					.build()
	MarvelUniverseMedia hulk = MarvelUniverseMedia.builder()
					.id(2L)
					.title('The Incredible Hulk')
					.releaseDate(LocalDate.of(2008, 6, 13))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.is(2009))
					.build()
	MarvelUniverseMedia ironMan2 = MarvelUniverseMedia.builder()
					.id(3L)
					.title('Iron Man 2')
					.releaseDate(LocalDate.of(2010, 4, 30))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.is(2009))
					.build()
	MarvelUniverseMedia thor = MarvelUniverseMedia.builder()
					.id(4L)
					.title('Thor')
					.releaseDate(LocalDate.of(2011, 4, 27))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.is(2009))
					.build()
	MarvelUniverseMedia capitainAmerica = MarvelUniverseMedia.builder()
					.id(5L)
					.title('Captain America: The First Avenger')
					.releaseDate(LocalDate.of(2011, 7, 29))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.between(1942, 1945))
					.build()
	MarvelUniverseMedia avengers = MarvelUniverseMedia.builder()
					.id(6L)
					.title('The Avengers')
					.releaseDate(LocalDate.of(2012, 04, 26))
					.phase(MCUPhases.PHASE_1)
					.filmTv(MediaVehicle.FILM)
					.inUniverseYear(Range.is(2010))
					.build()

	List<String> contentLines = [
					'Title,                              Release date, Phase, Film/TV, In-universe year',
					'Iron Man,                           2008-05-02,   1,     Film,    2008',
					'The Incredible Hulk,                2008-06-13,   1,     Film,    2009',
					'Iron Man 2,                         2010-04-30,   1,     Film,    2009',
					'Thor,                               2011-04-27,   1,     Film,    2009',
					'Captain America: The First Avenger, 2011-07-29,   1,     Film,    1942-1945',
					'The Avengers,                       2012-04-26,   1,     Film,    2010'
	]

	String content = contentLines.join('\n')

	def "Parser to beans works on small set of lines"() {
		given:
		def config = ExtendedFileFormatConfiguration.builder()
						.trim(true)
						.indexColumn(INDEX)
						.columns(values().toList())
						.build()
		def assembly = new MarvelUniverseBeanAssembly(config.parserLocale)
		and: 'a parser'
		def parser = CsvParserFactory.toBeans(config, assembly)

		when:
		def result = parser.parse(content).collect(Collectors.toList())

		then:
		result[0] == ironMan
		result[1] == hulk
		result[2] == ironMan2
		result[3] == thor
		result[4] == capitainAmerica
		result[5] == avengers
	}

	def "Parser to beans works without exceptions to full number of lines, even when data is malformed for the type"() {
		given:
		def config = ExtendedFileFormatConfiguration.builder()
						.trim(true)
						.indexColumn(INDEX)
						.columns(values().toList())
						.build()
		def assembly = new MarvelUniverseBeanAssembly(config.parserLocale)
		and: 'a parser'
		def parser = CsvParserFactory.toBeans(config, assembly)
		and: 'data'
		def bis = new BufferedInputStream(getClass().getResourceAsStream('mcu.csv'))

		expect:
		parser.parse(bis).collect(Collectors.toList()).size() == 64
	}
	def "Parse a small sample using code defined columns"() {
		given:
		IMarvelUniverseColumn index = INDEX
		IMarvelUniverseColumn[] cols = values() as IMarvelUniverseColumn[]
		def config = FileFormatConfiguration.builder().trim(true).build()
		and: 'a parser'
		def parser = CsvParserFactory.toRowIndexedTextMap(config, index, cols.toList())

		when:
		List<Map<IMarvelUniverseColumn, String>> result = parser.parse(content).collect(Collectors.toList())

		then:
		result.size() == 6
		result[0] == [(INDEX): '1', (TITLE): 'Iron Man', (RELEASE_DATE): '2008-05-02', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '2008']
		result[1] == [(INDEX): '2', (TITLE): 'The Incredible Hulk', (RELEASE_DATE): '2008-06-13', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '2009']
		result[2] == [(INDEX): '3', (TITLE): 'Iron Man 2', (RELEASE_DATE): '2010-04-30', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '2009']
		result[3] == [(INDEX): '4', (TITLE): 'Thor', (RELEASE_DATE): '2011-04-27', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '2009']
		result[4] == [(INDEX): '5', (TITLE): 'Captain America: The First Avenger', (RELEASE_DATE): '2011-07-29', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '1942-1945']
		result[5] == [(INDEX): '6', (TITLE): 'The Avengers', (RELEASE_DATE): '2012-04-26', (PHASE): '1', (FILM_TV): 'Film', (IN_UNIVERSE_YEAR): '2010']
	}

	def "Parse inputstream code defined columns"() {
		given:
		IMarvelUniverseColumn index = INDEX
		IMarvelUniverseColumn[] cols = values() as IMarvelUniverseColumn[]
		def config = FileFormatConfiguration.builder().trim(true).build()
		and: 'a parser'
		def parser = CsvParserFactory.toRowIndexedTextMap(config, index, cols.toList())
		and: 'content as inputstream'
		BufferedInputStream content = new BufferedInputStream(getClass().getResourceAsStream('mcu.csv'))

		when:
		List<Map<IMarvelUniverseColumn, String>> result = parser.parse(content).collect(Collectors.toList())

		then:
		result.size() == 64
		result.collect { it[INDEX] }.toSet().size() == 64
	}

	def "Parse inputstream with textual defined columns"() {
		given:
		def cols = ['Title', 'Release date', 'Phase', 'Film/TV', 'In-universe year']
		def config = FileFormatConfiguration.builder().trim(true).build()
		and: 'a parser'
		def parser = CsvParserFactory.toRowIndexedTextMap(config, '#', cols)
		and: 'content as inputstream'
		BufferedInputStream content = new BufferedInputStream(getClass().getResourceAsStream('mcu.csv'))

		when:
		List<Map<String, String>> result = parser.parse(content).collect(Collectors.toList())

		then: 'count rows as header is discarded'
		result.size() == 64
		and: 'all indexes must be distinct'
		result.collect { it['#'] }.toSet().size() == 64
		and: 'last record is ok'
		result.last()['#'] == '64'
		result.last()['Title'] == 'Untitled Hawkeye series'
		result.last()['Release date'] == ''
		result.last()['Phase'] == ''
		result.last()['Film/TV'] == 'TV (Disney+)'
		result.last()['In-universe year'] == ''
	}

	def "Parse inputstream without defining columns"() {
		given:
		def config = FileFormatConfiguration.builder().trim(true).build()
		and: 'a parser'
		def parser = CsvParserFactory.toText(config)
		and: 'content as inputstream'
		BufferedInputStream content = new BufferedInputStream(getClass().getResourceAsStream('mcu.csv'))

		when:
		List<List<String>> result = parser.parse(content).collect(Collectors.toList())

		then: 'count of header and rows is correct'
		result.size() == 65
		and: 'header is the first "record"'
		result.first() == ['Title', 'Release date', 'Phase', 'Film/TV', 'In-universe year']
		and: 'last record is ok'
		result.last()[0] == 'Untitled Hawkeye series'
		result.last()[1] == ''
		result.last()[2] == ''
		result.last()[3] == 'TV (Disney+)'
		result.last()[4] == ''
	}
}
