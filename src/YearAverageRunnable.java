import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;

public class YearAverageRunnable implements Runnable {

	private HashMap<Integer, ArrayList<Double>> yearPrices;
	private List<GasDataPoint> dataSet;
	
	public YearAverageRunnable(List<GasDataPoint> data) {
		dataSet = data;
		yearPrices = new HashMap<Integer, ArrayList<Double>>();
	}
	
	private void mapPricesToYear() {
		for(GasDataPoint dp : dataSet) {
			
			int year = (Integer)dp.getYear();
			double price = (Double)dp.getPrice();
			
			//if year key not found, add new key value pair
			if(!yearPrices.containsKey(year)) {
				yearPrices.put(year, new ArrayList()); // add a new key value pair into hashmap
				yearPrices.get(year).add(price);       // add price
			} else {
				//else add price to year
				yearPrices.get(year).add(price);
			}
		}
	}
	
	private void averagePrices() {
		System.out.println("\nAVERAGES BY YEAR\n");
		DecimalFormat formatter = new DecimalFormat("#.00");
		for(Integer year : yearPrices.keySet()) {
			OptionalDouble ave = yearPrices.get(year)
					.stream()
					.mapToDouble(d -> d.doubleValue())
					.average();
			
			String formattedAve = formatter.format(ave.getAsDouble());
			
			System.out.println(year+" : "+formattedAve);
		}
	}
	
	@Override
	public void run() {
		mapPricesToYear();
		averagePrices();
	}

}
