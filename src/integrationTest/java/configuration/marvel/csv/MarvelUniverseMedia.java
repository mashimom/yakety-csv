package configuration.marvel.csv;

import configuration.marvel.domain.MCUPhases;
import configuration.marvel.domain.MediaVehicle;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class MarvelUniverseMedia {
	@Nullable Long id;
	@Nullable String title;
	@Nullable LocalDate releaseDate;
	@Nullable MCUPhases phase;
	@Nullable MediaVehicle filmTv;
	@Nullable Range<Integer> inUniverseYear;
}
