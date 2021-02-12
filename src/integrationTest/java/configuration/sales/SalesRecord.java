package configuration.sales;

import configuration.sales.domain.Regions;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Data
public class SalesRecord {
	Long rowNum;
	Regions region;
	String country;
	String itemType;
	String salesChannel;
	Character orderPriority;
	LocalDate orderDate;
	Long orderId;
	LocalDate shipDate;
	Integer unitsSold;
	BigDecimal unitPrice;
	BigDecimal unitCost;
	BigDecimal totalRevenue;
	BigDecimal totalCost;
	BigDecimal totalProfit;
}
