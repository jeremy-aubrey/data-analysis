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
//                 of gas for each given month in the data set.
//
//********************************************************************

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;

public class MonthAverageCallable implements Callable<String> {

	private List<GasDataPoint> dataList;
	
	// constructor
	public MonthAverageCallable(List<GasDataPoint> data) {
		dataList = data;
	}
	
    //***************************************************************
    //
    //  Method:       mapPricesToMonth (Non Static)
    // 
    //  Description:  Maps a List of GasDataPoint objects to a LinkedHashMap
    //                where each key is the month/year and each value is an ArrayList of 
    //                associated gas prices prices for that date.
    //
    //  Parameters:   None
    //
    //  Returns:      Map<String(month/year), ArrayList<Double>(prices)>
    //
    //**************************************************************
	private Map<String, ArrayList<Double>> mapPricesToMonth() {
		
		Map<String, ArrayList<Double>> mappedPrices = new LinkedHashMap<String, ArrayList<Double>>();
		
		for(GasDataPoint dp : dataList) {
			
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
		
		return mappedPrices;
		
	}// end mapPricesToMonth method
	
    //***************************************************************
    //
    //  Method:       generateResults (Non Static)
    // 
    //  Description:  Generates a String containing the average price for 
    //                a given year in the mapped data.
    //
    //  Parameters:   Map<String, ArrayList<Double>> (date, prices list)
    //
    //  Returns:      String
    //
    //**************************************************************
	private String generateResults(Map<String, ArrayList<Double>> mappedPrices) {
		
		StringBuilder results = new StringBuilder();
		
		//header and column titles
		results.append("AVERAGES BY MONTH\n");
		results.append(String.format("%-10s%s%n","MO/YEAR","PRICE (AVG)"));
		
		//for each date, get the average gas price and add to results
		for(String date : mappedPrices.keySet()) {
			String result = getAveragePrice(date, mappedPrices);
			results.append(result);
		}
		
		return results.toString();
		
	}// end generateResults method
	
    //***************************************************************
    //
    //  Method:       getAveragePrice (Non Static)
    // 
    //  Description:  A helper method that uses streams to calculate the 
    //                average price from an ArrayList of Doubles (prices)
    //                for a single date.
    //
    //  Parameters:   String (key), Map<String, ArrayList<Double>> (date, prices list)
    //
    //  Returns:      String
    //
    //**************************************************************
	private String getAveragePrice(String date, Map<String, ArrayList<Double>> mappedPrices) {
		
		//use key to get associated prices List, then get average
		OptionalDouble avg = mappedPrices.get(date)
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.average();
		
		//format result
		DecimalFormat formatter = new DecimalFormat("0.000");
		String formattedAvg = "----"; // default (missing data)
		if(avg.isPresent()) {
			formattedAvg = formatter.format(avg.getAsDouble());
		}
		String line = String.format("%-8s:%6s%n",date,formattedAvg);
		
		return line;
		
	}// end getAvergePrices method
	
    //***************************************************************
    //
    //  Method:       call (Non Static)
    // 
    //  Description:  Calls class methods to calculate the average price 
    //                of gas for each month and returns the aggregated results.
    //
    //  Parameters:   None
    //
    //  Returns:      String (average price per month)
    //
    //**************************************************************
	@Override
	public String call() throws Exception {
		
		//map original data to new (month/year, prices list) pairs and process
		String results = " [ NO DATA FOUND ]"; // default
		Map<String, ArrayList<Double>> mappedPrices = mapPricesToMonth();
		if(!mappedPrices.isEmpty()) {
			results = generateResults(mappedPrices);
		}
		
		return results;
		
	}// end call method

}// end MonthAverageCallable class