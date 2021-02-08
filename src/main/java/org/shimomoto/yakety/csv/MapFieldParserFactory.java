package org.shimomoto.yakety.csv;

import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.field.MapDelegatedParser;
import org.shimomoto.yakety.csv.field.FieldParsers;
import org.shimomoto.yakety.csv.field.api.MapFieldParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Value
public class MapFieldParserFactory {
	Locale locale;

	public MapFieldParserFactory(final Locale locale) {
		this.locale = locale;
	}

	public <C> MapFieldParser<C, BigDecimal> forBigDecimal(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forBigDecimal(this.locale), key);
	}

	public <C> MapFieldParser<C, BigInteger> forBigInteger(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forBigInteger(this.locale), key);
	}

	public <C> MapFieldParser<C, Double> forDouble(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forDouble(this.locale), key);
	}

	public <C> MapFieldParser<C, Float> forFloat(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forFloat(this.locale), key);
	}

	public <C> MapFieldParser<C, Integer> forInteger(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forInteger(this.locale), key);
	}

	public <C> MapFieldParser<C, LocalDate> forLocalDate(final @NotNull C key, @NotNull final DateTimeFormatter formatter) {
		return new MapDelegatedParser<>(FieldParsers.forLocalDate(formatter, this.locale), key);
	}

	public <C> MapFieldParser<C, LocalDateTime> forLocalDateTime(final @NotNull C key, @NotNull final DateTimeFormatter formatter) {
		return new MapDelegatedParser<>(FieldParsers.forLocalDateTime(formatter, this.locale), key);
	}

	public <C> MapFieldParser<C, LocalTime> forLocalTime(final @NotNull C key, @NotNull final DateTimeFormatter formatter) {
		return new MapDelegatedParser<>(FieldParsers.forLocalTime(formatter, this.locale), key);
	}

	public <C> MapFieldParser<C, Long> forLong(final @NotNull C key) {
		return new MapDelegatedParser<>(FieldParsers.forLong(this.locale), key);
	}
}
