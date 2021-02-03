package org.shimomoto.yakety.csv.field;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.field.api.FieldParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class FieldParsers {

	private FieldParsers() {
	}

	public static FieldParser<BigDecimal> forBigDecimal(@NotNull final Locale locale) {
		return new BigDecimalFieldParser(locale);
	}

	public static FieldParser<BigInteger> forBigInteger(@NotNull final Locale locale) {
		return new BigIntegerFieldParser(locale);
	}

	public static FieldParser<Double> forDouble(@NotNull final Locale locale) {
		return new DoubleFieldParser(locale);
	}

	public static FieldParser<Float> forFloat(@NotNull final Locale locale) {
		return new FloatFieldParser(locale);
	}

	public static FieldParser<Integer> forInteger(@NotNull final Locale locale) {
		return new IntegerFieldParser(locale);
	}

	public static FieldParser<LocalDate> forLocalDate(@NotNull final DateTimeFormatter formatter, @NotNull final Locale locale) {
		return new LocalDateFieldParser(formatter, locale);
	}

	public static FieldParser<LocalDateTime> forLocalDateTime(@NotNull final DateTimeFormatter formatter, @NotNull final Locale locale) {
		return new LocalDateTimeFieldParser(formatter, locale);
	}

	public static FieldParser<LocalTime> forLocalTime(@NotNull final DateTimeFormatter formatter, @NotNull final Locale locale) {
		return new LocalTimeFieldParser(formatter, locale);
	}

	public static FieldParser<Long> forLong(@NotNull final Locale locale) {
		return new LongFieldParser(locale);
	}
}
