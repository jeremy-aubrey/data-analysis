import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class MonthAverageCallable implements Callable<String> {

	private List<GasDataPoint> dataSet;
	
	public MonthAverageCallable(List<GasDataPoint> data) {
		dataSet = data;
	}
	
	private Map<String, ArrayList<Double>> mapPricesToYear() {
		Map<String, ArrayList<Double>> mappedPrices = new LinkedHashMap<String, ArrayList<Double>>();
		for(GasDataPoint dp : dataSet) {
			
			String monthYear = String.valueOf(dp.getMonth()) + "/" + String.valueOf(dp.getYear());
			double price = (Double)dp.getPrice();
			
			//if year key not found, add new key value pair entry
			if(!mappedPrices.containsKey(monthYear)) {
				mappedPrices.put(monthYear, new ArrayList<Double>()); // add a new key value pair
				mappedPrices.get(monthYear).add(price);               // add price
			} else {
				//else add price to existing year
				mappedPrices.get(monthYear).add(price);
			}
		}
		return mappedPrices;
	}
	
	private String generateResults(Map<String, ArrayList<Double>> mappedPrices) {
		StringBuilder results = new StringBuilder();
		results.append("AVERAGES BY MONTH\n");
		results.append(String.format("%-10s%s%n","MO/YEAR","PRICE (AVG)"));
		for(String date : mappedPrices.keySet()) {
			
			String result = getAveragePrice(date, mappedPrices);
			
			results.append(result);
		}
		return results.toString();
	}
	
	private String getAveragePrice(String date, Map<String, ArrayList<Double>> mappedPrices) {
		
		OptionalDouble avg = mappedPrices.get(date)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.average();
		
		DecimalFormat formatter = new DecimalFormat("0.000");
		String formattedAvg = "----"; // default (missing data)
		if(avg.isPresent()) {
			formattedAvg = formatter.format(avg.getAsDouble());
		}
		
		String line = String.format("%-8s:%6s%n",date,formattedAvg);
		return line;
	}
	
	@Override
	public String call() throws Exception {
		String results = " [ NO DATA FOUND ]"; // default
		Map<String, ArrayList<Double>> mappedPrices = mapPricesToYear();
		if(!mappedPrices.isEmpty()) {
			results = generateResults(mappedPrices);
		}
		return results;
	}

}
