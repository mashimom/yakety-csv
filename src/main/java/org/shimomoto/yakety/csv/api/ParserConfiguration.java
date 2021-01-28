package org.shimomoto.yakety.csv.api;

import java.util.List;

public interface ParserConfiguration<T> {
	List<T> getCols();
}
