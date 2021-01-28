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
public class FileFormatConfiguration {
	@Builder.Default
	Locale parserLocale = Locale.US;
	@Builder.Default
	String lineBreak = "\n";
	@Builder.Default
	char separator = ',';
	@Builder.Default
	char quote = '"';
	@Builder.Default
	boolean trim = false; //TODO: move to column by column option
//	@Builder.Default
//	int skipFromTop = 0; //TODO: move to content configuration
//	@Builder.Default
//	boolean headerless = false; //TODO: move to content configuration
//	@Builder.Default
//	boolean endsWithEmptyLine = true; //TODO: move to content configuration, maybe?
}
