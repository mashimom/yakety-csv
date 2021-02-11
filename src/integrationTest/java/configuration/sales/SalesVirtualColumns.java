package configuration.sales;

import configuration.sales.api.ISalesColumn;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SalesVirtualColumns {
	public static final ISalesColumn INDEX = new ISalesColumn() {
		@Override
		public int getOrder() {
			return -1;
		}

		@Override
		public String getDisplayName() {
			return "#";
		}

		@Override
		public boolean isNullable() {
			return false;
		}
	};
}
