package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.LongValidator;

import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Value
class LongFieldParser extends BaseFieldParser<Long> {

	Locale locale;

	public LongFieldParser(final Locale locale) {
		super(LongValidator.getInstance()::validate);
		this.locale = locale;
	}
}
