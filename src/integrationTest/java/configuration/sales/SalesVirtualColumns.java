package configuration.sales;

import configuration.sales.api.ISalesColumn;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class SalesVirtualColumns {
	public static final ISalesColumn INDEX = new ISalesColumn() {
		@Override
		public int getOrder() {
			return -1;
		}

		@Override
		public @NotNull String getDisplayName() {
			return "#";
		}

		@Override
		public boolean isNullable() {
			return false;
		}
	};
}
