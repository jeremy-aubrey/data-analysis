import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class DescendingSortCallable implements Callable<String> {
	
	private List<GasDataPoint> dataSet;     
	
	public DescendingSortCallable(List<GasDataPoint> data) {
		dataSet = data;
	}
	
	private List<GasDataPoint> sortDataDescending() {

		List<GasDataPoint> sorted = dataSet.stream()
				.sorted(Comparator.comparingDouble(GasDataPoint::getPrice).reversed())
				.collect(Collectors.toList());

		return sorted;
	}
	
	private String generateResults(List<GasDataPoint> list) {
		
		StringBuilder results = new StringBuilder();
		
		results.append("PRICES SORTED HIGHEST TO LOWEST\n");
		results.append(String.format("%-10s%s%n","MO/YEAR","PRICE"));
		
		for(GasDataPoint dp : list) {
			String monthYear = dp.getMonth()+"/"+dp.getYear();
			DecimalFormat formatter = new DecimalFormat("0.000");
			String formattedPrice = formatter.format(dp.getPrice());
			String line = String.format("%-8s:%6s%n",monthYear,formattedPrice);
			results.append(line);
		}
		
		return results.toString();
	}
	
	@Override
	public String call() throws Exception {
		List<GasDataPoint> sortedList = sortDataDescending();
		return generateResults(sortedList);
	}
}
