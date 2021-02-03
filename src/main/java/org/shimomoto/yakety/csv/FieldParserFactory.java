package org.shimomoto.yakety.csv;

import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.field.FieldParsers;
import org.shimomoto.yakety.csv.field.api.FieldParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Value
public class FieldParserFactory {
	Locale locale;

	public FieldParserFactory(final Locale locale) {
		this.locale = locale;
	}

	public FieldParser<BigDecimal> forBigDecimal() {
		return FieldParsers.forBigDecimal(this.locale);
	}

	public FieldParser<BigInteger> forBigInteger() {
		return FieldParsers.forBigInteger(this.locale);
	}

	public FieldParser<Double> forDouble() {
		return FieldParsers.forDouble(this.locale);
	}

	public FieldParser<Float> forFloat() {
		return FieldParsers.forFloat(this.locale);
	}

	public FieldParser<Integer> forInteger() {
		return FieldParsers.forInteger(this.locale);
	}

	public FieldParser<LocalDate> forLocalDate(@NotNull final DateTimeFormatter formatter) {
		return FieldParsers.forLocalDate(formatter, this.locale);
	}

	public FieldParser<LocalDateTime> forLocalDateTime(@NotNull final DateTimeFormatter formatter) {
		return FieldParsers.forLocalDateTime(formatter, this.locale);
	}

	public FieldParser<LocalTime> forLocalTime(@NotNull final DateTimeFormatter formatter) {
		return FieldParsers.forLocalTime(formatter, this.locale);
	}

	public FieldParser<Long> forLong() {
		return FieldParsers.forLong(this.locale);
	}
}
