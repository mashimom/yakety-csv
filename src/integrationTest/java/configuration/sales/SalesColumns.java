package configuration.sales;


import configuration.sales.api.ISalesColumn;
import org.jetbrains.annotations.NotNull;

public enum SalesColumns implements ISalesColumn {
	REGION("Region"),
	COUNTRY("Country"),
	ITEM_TYPE("Item Type"),
	SALES_CHANNEL("Sales Channel"),
	ORDER_PRIORITY("Order Priority"),
	ORDER_DATE("Order Date"),
	ORDER_ID("Order ID"),
	SHIP_DATE("Ship Date"),
	UNITS_SOLD("Units Sold"),
	UNIT_PRICE("Unit Price"),
	UNIT_COST("Unit Cost"),
	TOTAL_REVENUE("Total Revenue"),
	TOTAL_COST("Total Cost"),
	TOTAL_PROFIT("Total Profit");

	public final @NotNull String displayName;
	public final boolean nullable;

	SalesColumns(@NotNull final String displayName, final boolean nullable) {
		this.displayName = displayName;
		this.nullable = nullable;
	}

	SalesColumns(@NotNull final String displayName) {
		this(displayName,true);
	}

	@Override
	public int getOrder() {
		return this.ordinal();
	}

	@Override
	public @NotNull String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}
}
