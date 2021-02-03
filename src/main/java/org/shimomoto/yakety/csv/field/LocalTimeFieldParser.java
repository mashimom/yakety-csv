package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Value
class LocalTimeFieldParser extends BaseFieldParser<LocalTime> {

	Locale locale;

	public LocalTimeFieldParser(final DateTimeFormatter format, final Locale locale) {
		super((s, l) -> LocalTimeFieldParser.safeParse(s, l, format));
		this.locale = locale;
	}

	public static @Nullable LocalTime safeParse(final String text,
	                                            @NotNull final Locale locale,
	                                            @NotNull final DateTimeFormatter format) {
		if (text == null) {
			return null;
		}
		try {
			return LocalTime.parse(text, format);
		} catch (final DateTimeParseException e) {
			log.error(java.text.MessageFormat.format("Unable to parse LocalDate {0} with {1}", text, format), e);
		}
		return null;
	}
}
