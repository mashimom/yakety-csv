package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.DoubleValidator;

import java.util.Locale;

@EqualsAndHashCode(callSuper = false)
@Value
class DoubleFieldParser extends BaseFieldParser<Double> {

	Locale locale;

	public DoubleFieldParser(final Locale locale) {
		super(DoubleValidator.getInstance()::validate);
		this.locale = locale;
	}
}
