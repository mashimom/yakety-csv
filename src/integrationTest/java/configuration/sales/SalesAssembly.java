package configuration.sales;

import configuration.sales.api.ISalesColumn;
import configuration.sales.domain.Regions;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.yakety.csv.MapFieldParserFactory;
import org.shimomoto.yakety.csv.api.BeanAssembly;
import org.shimomoto.yakety.csv.field.api.MapFieldParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Value
public class SalesAssembly implements BeanAssembly<ISalesColumn, SalesRecord> {
	Locale locale;
	MapFieldParser<ISalesColumn, Long> rowNumParser;
	MapFieldParser<ISalesColumn, Regions> regionParser;
	MapFieldParser<ISalesColumn, String> countryParser;
	MapFieldParser<ISalesColumn, String> itemTypeParser;
	MapFieldParser<ISalesColumn, String> salesChannelParser;
	MapFieldParser<ISalesColumn, Character> orderPriorityParser;
	MapFieldParser<ISalesColumn, LocalDate> orderDateParser;
	MapFieldParser<ISalesColumn, Long> orderIdParser;
	MapFieldParser<ISalesColumn, LocalDate> shipDateParser;
	MapFieldParser<ISalesColumn, Integer> unitsSoldParser;
	MapFieldParser<ISalesColumn, BigDecimal> unitPriceParser;
	MapFieldParser<ISalesColumn, BigDecimal> unitCostParser;
	MapFieldParser<ISalesColumn, BigDecimal> totalRevenueParser;
	MapFieldParser<ISalesColumn, BigDecimal> totalCostParser;
	MapFieldParser<ISalesColumn, BigDecimal> totalProfitParser;

	public SalesAssembly(final Locale locale) {
		this.locale = locale;
		final MapFieldParserFactory fpf = new MapFieldParserFactory(locale);
		final DateTimeFormatter usDate = DateTimeFormatter.ofPattern("M/d/yyyy", locale);

		this.rowNumParser = fpf.forLong(SalesVirtualColumns.INDEX);
		this.regionParser = fpf.forTextualDomain(SalesColumns.REGION, Regions.class);
		this.countryParser = SalesColumns.COUNTRY::get;
		this.itemTypeParser = SalesColumns.ITEM_TYPE::get;
		this.salesChannelParser = SalesColumns.SALES_CHANNEL::get;
		this.orderPriorityParser = (m) -> m.get(SalesColumns.ORDER_PRIORITY).chars()
						.mapToObj(i -> (char) i)
						.findFirst()
						.orElse(null);
		this.orderDateParser = fpf.forLocalDate(SalesColumns.ORDER_DATE, usDate);
		this.orderIdParser = fpf.forLong(SalesColumns.ORDER_ID);
		this.shipDateParser = fpf.forLocalDate(SalesColumns.SHIP_DATE, usDate);
		this.unitsSoldParser = fpf.forInteger(SalesColumns.UNITS_SOLD);
		this.unitPriceParser = fpf.forBigDecimal(SalesColumns.UNIT_PRICE);
		this.unitCostParser = fpf.forBigDecimal(SalesColumns.UNIT_COST);
		this.totalRevenueParser = fpf.forBigDecimal(SalesColumns.TOTAL_REVENUE);
		this.totalCostParser = fpf.forBigDecimal(SalesColumns.TOTAL_COST);
		this.totalProfitParser = fpf.forBigDecimal(SalesColumns.TOTAL_PROFIT);
	}

	@Override
	public @NotNull SalesRecord assemble(@NotNull final Map<ISalesColumn, String> map) {
		return SalesRecord.builder()
						.rowNum(rowNumParser.parse(map))
						.region(regionParser.parse(map))
						.country(countryParser.parse(map))
						.itemType(itemTypeParser.parse(map))
						.salesChannel(salesChannelParser.parse(map))
						.orderPriority(orderPriorityParser.parse(map))
						.orderDate(orderDateParser.parse(map))
						.orderId(orderIdParser.parse(map))
						.shipDate(shipDateParser.parse(map))
						.unitsSold(unitsSoldParser.parse(map))
						.unitPrice(unitPriceParser.parse(map))
						.unitCost(unitCostParser.parse(map))
						.totalRevenue(totalRevenueParser.parse(map))
						.totalCost(totalCostParser.parse(map))
						.totalProfit(totalProfitParser.parse(map))
						.build();
	}
}
