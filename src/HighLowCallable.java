import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class HighLowCallable implements Callable<String> {
	
	private Map<Integer, ArrayList<Double>> mappedPrices;   // month/year -> prices (mapping)
	private List<GasDataPoint> dataSet;                     // original data set
	StringBuilder results;                                  // results to return
	
	public HighLowCallable(List<GasDataPoint> data) {
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
	
	private void getMinAndMax() {
		
		results.append("LOWEST AND HIGHEST PRICES BY YEAR\n");
		results.append(String.format("%-6s%4s%7s%n","YEAR","LOW","HIGH"));
		for(Integer year : mappedPrices.keySet()) {
			
			double min = getMinPrice(year);
			double max = getMaxPrice(year);
			
			DecimalFormat formatter = new DecimalFormat("0.000");
			String formattedMin = "----"; // default (missing data)
			String formattedMax = "----"; // default (missing data)
			
			if(min != -1) {
				formattedMin = formatter.format(min);
			}
			
			if(max != -1) {
				formattedMax = formatter.format(max);
			}
			
			String line = String.format("%-5s:%6s%6s%n",year,formattedMin,formattedMax);
			results.append(line);
		}
	}
	
	private double getMinPrice(Integer year) {
		OptionalDouble min = mappedPrices.get(year)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.min();
		
		double minResult = -1;
		
		if(min.isPresent()) {
			minResult = min.getAsDouble();
		}
		
		return minResult;
	}
	
	private double getMaxPrice(Integer year) {
		OptionalDouble min = mappedPrices.get(year)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.max();
		
		double maxResult = -1;
		
		if(min.isPresent()) {
			maxResult = min.getAsDouble();
		}
		
		return maxResult;
	}
	
	@Override
	public String call() throws Exception {
		mapPricesToYear();
		getMinAndMax();
		
		return results.toString();
	}

}
