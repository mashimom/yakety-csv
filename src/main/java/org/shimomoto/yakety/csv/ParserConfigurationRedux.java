package org.shimomoto.yakety.csv;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder(toBuilder = true)
@Value
@Slf4j
public class ParserConfigurationRedux {
	@Builder.Default
	Locale parserLocale = Locale.US;
	@Builder.Default
	String lineBreak = "\n";
	@Builder.Default
	char separator = ',';
	@Builder.Default
	char quote = '"';
	@Builder.Default
	boolean trim = false;
//	@Builder.Default
//	int skipFromTop = 0;
//	@Builder.Default
//	boolean headerless = false;
//	@Builder.Default
//	boolean endsWithEmptyLine = true;
}
