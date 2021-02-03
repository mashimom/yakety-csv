package org.shimomoto.yakety.csv.api;

public interface ColumnDefinition extends HasDisplayName{

	int getOrder();

	String getDisplayName();

	boolean isNullable();
}
