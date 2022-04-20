import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AscendingSortCallable implements Callable<String> {

	private List<GasDataPoint> dataSet;     
	
	public AscendingSortCallable(List<GasDataPoint> data) {
		dataSet = data;
	}
	
	private List<GasDataPoint> sortDataAscending() {

		List<GasDataPoint> sorted = dataSet.stream()
				.sorted(Comparator.comparingDouble(GasDataPoint::getPrice))
				.collect(Collectors.toList());

		return sorted;
	}
	
	private String generateResults(List<GasDataPoint> list) {
		
		StringBuilder results = new StringBuilder();
		
		results.append("PRICES SORTED LOWEST TO HIGHEST\n");
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
		List<GasDataPoint> sortedList = sortDataAscending();
		return generateResults(sortedList);
	}
	

}
