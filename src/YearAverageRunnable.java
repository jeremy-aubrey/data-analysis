import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class YearAverageRunnable implements Callable<String> {

	private Map<Integer, ArrayList<Double>> mappedPrices;    // year -> prices list (mapping)
	private List<GasDataPoint> dataSet;                      // original data set
	private StringBuilder results;                           // results to return
	
	public YearAverageRunnable(List<GasDataPoint> data) {
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
		for(Integer year : mappedPrices.keySet()) {
			OptionalDouble ave = mappedPrices.get(year)
					.stream()
					.mapToDouble(d -> d.doubleValue())
					.average();
			
			DecimalFormat formatter = new DecimalFormat("0.00");
			String formattedAve = "[ NO DATA ]"; // default
			if(ave.isPresent()) {
				formattedAve = formatter.format(ave.getAsDouble());
			}
			
			String line = year+" : "+formattedAve+"\n"; // individual result (year, average)
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
