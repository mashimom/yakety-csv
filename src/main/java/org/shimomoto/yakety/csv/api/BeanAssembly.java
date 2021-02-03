package org.shimomoto.yakety.csv.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface BeanAssembly<C extends ColumnDefinition, T> {

	@NotNull
	T assemble(@NotNull Map<C,String> map);
}
