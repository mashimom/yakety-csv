package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.BigIntegerValidator;

import java.math.BigInteger;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Value
class BigIntegerFieldParser extends BaseFieldParser<BigInteger> {

	Locale locale;

	public BigIntegerFieldParser(final Locale locale) {
		super(BigIntegerValidator.getInstance()::validate);
		this.locale = locale;
	}
}
