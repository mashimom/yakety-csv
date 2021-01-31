package org.shimomoto.yakety.csv.api;

public interface ColumnDefinition {

	int getOrder();

	String getName();

	default boolean mustTrim() {
		return false;
	}

	default boolean quoted() {
		return false;
	}
}
