package org.shimomoto.yakety.csv.api;

import java.util.List;

@Deprecated
public interface ParserConfiguration<T> {
	List<T> getCols();
}
