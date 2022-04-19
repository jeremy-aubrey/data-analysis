import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class MonthAverageRunnable implements Callable<String> {

	private Map<String, ArrayList<Double>> mappedPrices;
	private List<GasDataPoint> dataSet;
	StringBuilder results;
	
	public MonthAverageRunnable(List<GasDataPoint> data) {
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
		for(String monthYear : mappedPrices.keySet()) {
			OptionalDouble ave = mappedPrices.get(monthYear)
					.stream()
					.mapToDouble(d -> d.doubleValue())
					.average();
			
			DecimalFormat formatter = new DecimalFormat("0.00");
			String formattedAve = "[ NO DATA ]"; // default 
			if(ave.isPresent()) {
				formattedAve = formatter.format(ave.getAsDouble());
			}
			
			String line = monthYear+" : "+formattedAve+"\n";
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
