import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class YearAverageCallable implements Callable<String> {

	private List<GasDataPoint> dataSet;
	
	public YearAverageCallable(List<GasDataPoint> data) {
		dataSet = data;
	}
	
	private Map<Integer, ArrayList<Double>> mapPricesToYear() {
		Map<Integer, ArrayList<Double>> mappedPrices = new LinkedHashMap<Integer, ArrayList<Double>>();
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
		return mappedPrices;
	}
	
	private String generateResults(Map<Integer, ArrayList<Double>> mappedPrices) {
		StringBuilder results = new StringBuilder();
		results.append("AVERAGES BY YEAR\n");                            
		results.append(String.format("%-7s%s%n","YEAR","PRICE (AVG)"));
		for(Integer year : mappedPrices.keySet()) {
			
			String result = getAveragePrice(year, mappedPrices);
			results.append(result);
		}
		return results.toString();
	}
	
	private String getAveragePrice(Integer year, Map<Integer, ArrayList<Double>> mappedPrices) {
		
		OptionalDouble avg = mappedPrices.get(year)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.average();
		
		DecimalFormat formatter = new DecimalFormat("0.000");
		String formattedAvg = "----"; // default (missing data)
		if(avg.isPresent()) {
			formattedAvg = formatter.format(avg.getAsDouble());
		}
		
		String line = String.format("%-5s:%6s%n",year,formattedAvg);
		return line;
	}

	@Override
	public String call() throws Exception {
		String results = " [ NO DATA FOUND ]"; // default
		Map<Integer, ArrayList<Double>> mappedPrices = mapPricesToYear();
		if(!mappedPrices.isEmpty()) {
			results = generateResults(mappedPrices);
		}
		return results;
	}

}
