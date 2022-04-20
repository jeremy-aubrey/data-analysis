import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class HighLowCallable implements Callable<String> {
	
	private List<GasDataPoint> rawData;
	
	public HighLowCallable(List<GasDataPoint> data) {
		rawData = data;
	}
	
	private Map<Integer, ArrayList<GasDataPoint>> mapDataToYear() {
		Map<Integer, ArrayList<GasDataPoint>> mappedData = new LinkedHashMap<Integer, ArrayList<GasDataPoint>>();
		for(GasDataPoint dp : rawData) {
			
			int year = (Integer)dp.getYear();
			
			//if year key not found, add new key value pair entry
			if(!mappedData.containsKey(year)) {
				mappedData.put(year, new ArrayList<GasDataPoint>()); // add a new key value pair
				mappedData.get(year).add(dp);               // add price
			} else {
				//else add price to existing year
				mappedData.get(year).add(dp);
			}
		}
		return mappedData;
	}
	
	private String generateResults(Map<Integer, ArrayList<GasDataPoint>> mappedData) {
		StringBuilder results = new StringBuilder();
		results.append("LOWEST AND HIGHEST PRICES BY YEAR\n");
		results.append(String.format("%-5s:%6s%12s%7s%12s%n","YEAR","LOW","(DATE)","HIGH","(DATE)"));
		for(Integer year : mappedData.keySet()) {
			
			String result = getYearMinAndMax(year,mappedData);
			results.append(result);
		}
		return results.toString();
	}
	
	private String getYearMinAndMax(Integer year, Map<Integer, ArrayList<GasDataPoint>> mappedData) {
		
		List<GasDataPoint> yearData = mappedData.get(year);
		List<GasDataPoint> sortedDataByPrices = sortDataAscending(yearData);
		int lastIndex = sortedDataByPrices.size() - 1;
		DecimalFormat formatter = new DecimalFormat("0.000");
		
		GasDataPoint low = sortedDataByPrices.get(0);
		String lowDate = low.getDay()+"/"+low.getMonth()+"/"+low.getYear();
		String lowPrice = formatter.format(low.getPrice());
		
		GasDataPoint high = sortedDataByPrices.get(lastIndex);
		String highDate = high.getDay()+"/"+high.getMonth()+"/"+high.getYear();
		String highPrice = formatter.format(high.getPrice());
		
		String line = String.format("%-5s:%6s%12s%7s%12s%n",
				year,lowPrice, lowDate, highPrice,highDate);
		
		return line;
	}
	
	private List<GasDataPoint> sortDataAscending(List<GasDataPoint> list) {

		List<GasDataPoint> sorted = list.stream()
				.sorted(Comparator.comparingDouble(GasDataPoint::getPrice))
				.collect(Collectors.toList());

		return sorted;
	}
	
	
	@Override
	public String call() throws Exception {
		String results = " [ NO DATA FOUND ]"; // default
		Map<Integer, ArrayList<GasDataPoint>> mappedPrices = mapDataToYear();
		if(!mappedPrices.isEmpty()) {
			results = generateResults(mappedPrices);
		}
		return results;
	}

}
