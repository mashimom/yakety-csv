package configuration.marvel.csv;

import configuration.marvel.api.IMarvelUniverseColumn;
import configuration.marvel.domain.MCUPhases;
import configuration.marvel.domain.MediaVehicle;
import lombok.Value;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.field.FieldParsers;
import org.shimomoto.yakety.csv.MapFieldParserFactory;
import org.shimomoto.yakety.csv.field.api.FieldParser;
import org.shimomoto.yakety.csv.field.api.MapFieldParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
public class MarvelUniverseBeanAssembly implements BeanAssembly<IMarvelUniverseColumn, MarvelUniverseMedia> {

	Locale locale;
	MapFieldParser<IMarvelUniverseColumn, Long> idParser;
	MapFieldParser<IMarvelUniverseColumn, LocalDate> releaseDateParser;
	MapFieldParser<IMarvelUniverseColumn, Integer> phaseParser;
	MapFieldParser<IMarvelUniverseColumn, Range<Integer>> inUniverseYearParser;

	public MarvelUniverseBeanAssembly(final Locale locale) {
		this.locale = locale;
		final MapFieldParserFactory fpf = new MapFieldParserFactory(locale);
		this.idParser = fpf.forLong(MarvelUniverseIndexColumn.INDEX);
		this.releaseDateParser = fpf.forLocalDate(MarvelUniverseColumns.RELEASE_DATE, DateTimeFormatter.ISO_LOCAL_DATE);
		this.phaseParser = fpf.forInteger(MarvelUniverseColumns.PHASE);
		this.inUniverseYearParser = new InUniverseYearParser();
	}

	@NotNull
	@Override
	public MarvelUniverseMedia assemble(@NotNull final Map<IMarvelUniverseColumn, String> map) {
		return MarvelUniverseMedia.builder()
						.id(idParser.parse(map))
						.title(map.get(MarvelUniverseColumns.TITLE))
						.releaseDate(releaseDateParser.parse(map))
						.phase(MCUPhases.findOrNull(phaseParser.parse(map)))
						.filmTv(MediaVehicle.findOrNull(map.get(MarvelUniverseColumns.FILM_TV)))
						.inUniverseYear(inUniverseYearParser.parse(map))
						.build();
	}

	private static class InUniverseYearParser implements MapFieldParser<IMarvelUniverseColumn, Range<Integer>> {
		static FieldParser<Integer> yearParser = FieldParsers.forInteger(Locale.getDefault());

		public @Nullable Range<Integer> parse(final @NotNull Map<IMarvelUniverseColumn, String> map) {
			final String value = map.get(MarvelUniverseColumns.IN_UNIVERSE_YEAR);
			if (StringUtils.isBlank(value)) {
				return null;
			}
			final List<Integer> years = Arrays.stream(value.split("[-/]", 2))
							.map(yearParser::parse)//any illegal char will result in null value
							.filter(Objects::nonNull)
							.collect(Collectors.toList());

			return switch (years.size()) {
				case 1 -> Range.is(years.get(0));
				case 2 -> Range.between(years.get(0), years.get(1));
				default -> null;
			};
		}
	}
}
