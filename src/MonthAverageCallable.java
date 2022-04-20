import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class MonthAverageCallable implements Callable<String> {

	private Map<String, ArrayList<Double>> mappedPrices;    // month/year -> prices (mapping)
	private List<GasDataPoint> dataSet;                     // original data set
	StringBuilder results;                                  // results to return
	
	public MonthAverageCallable(List<GasDataPoint> data) {
		dataSet = data;
		mappedPrices = new LinkedHashMap<String, ArrayList<Double>>(); // keys maintain insertion order
		results = new StringBuilder();
	}
	
	private void mapPricesToYear() {
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
	}
	
	private void averagePrices() {
		
		results.append("AVERAGES BY MONTH\n");
		results.append(String.format("%-10s%s%n","MO/YEAR","PRICE (AVG)"));
		for(String monthYear : mappedPrices.keySet()) {
			OptionalDouble avg = mappedPrices.get(monthYear)
					.stream()
					.mapToDouble(d -> d.doubleValue())
					.average();
			
			DecimalFormat formatter = new DecimalFormat("0.000");
			String formattedAve = "----"; // default (missing data)
			if(avg.isPresent()) {
				formattedAve = formatter.format(avg.getAsDouble());
			}
			
			String line = String.format("%-8s:%6s%n",monthYear,formattedAve);
			results.append(line);
		}
	}
	
	@Override
	public String call() throws Exception {
		mapPricesToYear();
		averagePrices();
		
		return results.toString();
	}

}
