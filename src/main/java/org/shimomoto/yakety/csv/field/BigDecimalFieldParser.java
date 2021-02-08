package org.shimomoto.yakety.csv.field;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.validator.routines.BigDecimalValidator;

import java.math.BigDecimal;
import java.util.Locale;

@EqualsAndHashCode(callSuper = false)
@Value
class BigDecimalFieldParser extends BaseFieldParser<BigDecimal> {

	Locale locale;

	public BigDecimalFieldParser(final Locale locale) {
		super(BigDecimalValidator.getInstance()::validate);
		this.locale = locale;
	}
}
