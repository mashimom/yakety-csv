package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.FloatValidator;

import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Value
class FloatFieldParser extends BaseFieldParser<Float> {

	Locale locale;

	public FloatFieldParser(final Locale locale) {
		super(FloatValidator.getInstance()::validate);
		this.locale = locale;
	}
}
