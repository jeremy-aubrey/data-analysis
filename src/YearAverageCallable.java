//********************************************************************
//
//  Author:        Jeremy Aubrey
//
//  Program #:     8
//
//  File Name:     YearAverageCallable.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/20/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   A callable class that calculates the average price
//                 of gas for each given year in the data set.
//
//********************************************************************

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class YearAverageCallable implements Callable<String> {

	private List<GasDataPoint> dataList;
	
	// constructor
	public YearAverageCallable(List<GasDataPoint> data) {
		dataList = data;
	}
	
    //***************************************************************
    //
    //  Method:       mapPricesToYear (Non Static)
    // 
    //  Description:  Maps a List of GasDataPoint objects to a LinkedHashMap
    //                where each key is the year and each value is an ArrayList of 
    //                associated gas prices prices for that year.
    //
    //  Parameters:   None
    //
    //  Returns:      Map<Integer(year), ArrayList<Double>(prices)>
    //
    //**************************************************************
	private Map<Integer, ArrayList<Double>> mapPricesToYear() {
		
		Map<Integer, ArrayList<Double>> mappedPrices = new LinkedHashMap<Integer, ArrayList<Double>>();
		
		for(GasDataPoint dp : dataList) {
			
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
	
	}// end mapPricesToYear method 
	
    //***************************************************************
    //
    //  Method:       generateResults (Non Static)
    // 
    //  Description:  Generates a String containing the average price for 
    //                a given year in the mapped data.
    //
    //  Parameters:   Map<Integer, ArrayList<Double>> (year, prices list)
    //
    //  Returns:      String (aggregated results)
    //
    //**************************************************************
	private String generateResults(Map<Integer, ArrayList<Double>> mappedPrices) {
		
		StringBuilder results = new StringBuilder();
		
		//header and column titles
		results.append("AVERAGES BY YEAR\n");                             
		results.append(String.format("%-7s%s%n","YEAR","PRICE (AVG)"));
		
		//for each year, get the average gas price and add to results
		for(Integer year : mappedPrices.keySet()) {
			String result = getAveragePrice(year, mappedPrices);
			results.append(result);
		}
		
		return results.toString();
		
	}// end generateReults method
	
    //***************************************************************
    //
    //  Method:       getAveragePrice (Non Static)
    // 
    //  Description:  A helper method that uses streams to calculate the 
    //                average price from an ArrayList of Doubles (prices)
    //                for a single year.
    //
    //  Parameters:   Integer (key), Map<Integer, ArrayList<Double>> (year, prices list)
    //
    //  Returns:      String (avg price for a single year)
    //
    //**************************************************************
	private String getAveragePrice(Integer year, Map<Integer, ArrayList<Double>> mappedPrices) {
		
		//use key to get associated prices List, then get average
		OptionalDouble avg = mappedPrices.get(year)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.average();
		
		//format result 
		DecimalFormat formatter = new DecimalFormat("0.000");
		String formattedAvg = "----"; // default (missing data)
		if(avg.isPresent()) {
			formattedAvg = formatter.format(avg.getAsDouble());
		}
		String line = String.format("%-5s:%6s%n",year,formattedAvg);
		
		return line;
		
	}// end getAveragePrice

    //***************************************************************
    //
    //  Method:       call (Non Static)
    // 
    //  Description:  Calls class methods to calculate the average price 
    //                of gas for each year and returns the aggregated results.
    //
    //  Parameters:   None
    //
    //  Returns:      String (average price per year)
    //
    //**************************************************************
	@Override
	public String call() throws Exception {
		
		//map original data to new (year, prices list) pairs and process
		String results = " [ NO DATA FOUND ]"; // default
		Map<Integer, ArrayList<Double>> mappedPrices = mapPricesToYear();
		if(!mappedPrices.isEmpty()) {
			results = generateResults(mappedPrices);
		}
		
		return results;
		
	}// end call method

}//end YearAverageCallable method