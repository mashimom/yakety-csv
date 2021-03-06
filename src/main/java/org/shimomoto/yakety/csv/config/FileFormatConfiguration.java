package org.shimomoto.yakety.csv.config;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimomoto.yakety.csv.api.IFileFormatConfiguration;

import java.util.List;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder(toBuilder = true)
@Value
@Slf4j
public class FileFormatConfiguration<C> implements IFileFormatConfiguration<C> {
	@Builder.Default
	@NotNull Locale parserLocale = Locale.US;
	@Builder.Default
	char lineBreak = '\n';
	@Builder.Default
	char separator = ',';
	@Builder.Default
	char quote = '"';
	@Builder.Default
	boolean trim = false;
	@Builder.Default
	@Nullable C indexColumn = null;
	@Builder.Default
	@NotNull List<C> columns = List.of();
	@Builder.Default
	int linesBeforeHeader = 0;
	@Builder.Default
	boolean headerless = false;
}
