import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class YearAverageCallable implements Callable<String> {

	private Map<Integer, ArrayList<Double>> mappedPrices;    // year -> prices list (mapping)
	private List<GasDataPoint> dataSet;                      // original data set
	private StringBuilder results;                           // results to return
	
	public YearAverageCallable(List<GasDataPoint> data) {
		dataSet = data;
		mappedPrices = new LinkedHashMap<Integer, ArrayList<Double>>(); // keys maintain insertion order
		results = new StringBuilder();
	}
	
	private void mapPricesToYear() {
		for(GasDataPoint dp : dataSet) {
			
			int year = (Integer)dp.getYear();
			double price = (Double)dp.getPrice();
			
			//if year key not found, add new key value pair entry
			if(!mappedPrices.containsKey(year)) {
				mappedPrices.put(year, new ArrayList<Double>()); // add a new key value pair
				mappedPrices.get(year).add(price);               // add price
			} else {
				//else add price to existing year
				mappedPrices.get(year).add(price);
			}
		}
	}
	
	private void averagePrices() {
		
		results.append("AVERAGES BY YEAR\n");
		results.append(String.format("%-7s%s%n","YEAR","PRICE (AVG)"));
		for(Integer year : mappedPrices.keySet()) {
			OptionalDouble avg = mappedPrices.get(year)
					.stream()
					.mapToDouble(d -> d.doubleValue())
					.average();
			
			DecimalFormat formatter = new DecimalFormat("0.000");
			String formattedAve = "----"; // default (missing data)
			if(avg.isPresent()) {
				formattedAve = formatter.format(avg.getAsDouble());
			}
			
			String line = String.format("%-5s:%6s%n",year,formattedAve);
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
