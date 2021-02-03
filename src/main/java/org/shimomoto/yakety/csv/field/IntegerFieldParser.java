package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.IntegerValidator;

import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Value
class IntegerFieldParser extends BaseFieldParser<Integer> {

	Locale locale;

	public IntegerFieldParser(final Locale locale) {
		super(IntegerValidator.getInstance()::validate);
		this.locale = locale;
	}
}
