//********************************************************************
//
//  Author:        Jeremy Aubrey
//
//  Program #:     8
//
//  File Name:     HighLowCallable.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/20/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   A callable class that calculates the lowest and highest
//                 price of gas for a given year in the data set.
//
//********************************************************************

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class HighLowCallable implements Callable<String> {
	
	private List<GasDataPoint> data;
	
	// constructor
	public HighLowCallable(List<GasDataPoint> data) {
		this.data = data;
	}
	
    //***************************************************************
    //
    //  Method:       mapDataToYear (Non Static)
    // 
    //  Description:  Maps a List of GasDataPoint objects to a LinkedHashMap
    //                where each key is the month/year and each value is an ArrayList of 
    //                associated GasDataPoint objects for that year.
    //
    //  Parameters:   None
    //
    //  Returns:      Map<Integer>(year), ArrayList<GasDataPoint>(data objects)>
    //
    //**************************************************************
	private Map<Integer, ArrayList<GasDataPoint>> mapDataToYear() {
		
		Map<Integer, ArrayList<GasDataPoint>> mappedDataPoints = new LinkedHashMap<Integer, ArrayList<GasDataPoint>>();
		
		for(GasDataPoint dp : data) {
			
			int year = (Integer)dp.getYear();
			
			//if year key not found, add new key value pair entry
			if(!mappedDataPoints.containsKey(year)) {
				mappedDataPoints.put(year, new ArrayList<GasDataPoint>()); // add a new key value pair
				mappedDataPoints.get(year).add(dp);                        // add GasDataObject
			} else {
				//else add GasDataObject to existing year
				mappedDataPoints.get(year).add(dp); 
			}
		}
		
		return mappedDataPoints;
		
	}// end mapDataToYear method
	
    //***************************************************************
    //
    //  Method:       generateResults (Non Static)
    // 
    //  Description:  Generates a String containing the low price, 
    //                high price, and their associated dates.
    //
    //  Parameters:   None
    //
    //  Returns:      String
    //
    //**************************************************************
	private String generateResults(Map<Integer, ArrayList<GasDataPoint>> mappedDataPoints) {
		
		StringBuilder results = new StringBuilder();
		
		//header and column titles
		results.append("LOWEST AND HIGHEST PRICES BY YEAR\n");
		results.append(String.format("%-5s:%6s%12s%7s%12s%n","YEAR","LOW","(DATE)","HIGH","(DATE)"));
		
		//for each year, get the low price, high price, and dates
		for(Integer year : mappedDataPoints.keySet()) {
			String result = getYearMinAndMax(year,mappedDataPoints);
			results.append(result);
		}
		
		return results.toString();
		
	}// end generateResults method
	
    //***************************************************************
    //
    //  Method:       getYearMinAndMax (Non Static)
    // 
    //  Description:  A helper method that extracts the low and high data 
    //                from GasDataPoint objects.The year key is used to get
    //                the associated List of GasDataPoint objects. A helper
    //                is used to ensure the objects containing the lowest and 
    //                highest prices are selected.
    //
    //  Parameters:   Integer(key), Map<Integer, ArrayList<GasDataPoint>> (year, data objects)
    //
    //  Returns:      String
    //
    //**************************************************************
	private String getYearMinAndMax(Integer year, Map<Integer, ArrayList<GasDataPoint>> mappedDataPoints) {
		
		//use key to get a years data objects
		List<GasDataPoint> yearData = mappedDataPoints.get(year);
		
		//use helper method to sort the objects in ascending order
		List<GasDataPoint> sortedDataByPrices = sortDataAscending(yearData);
		
		//last index will contain object with highest price
		int lastIndex = sortedDataByPrices.size() - 1;
		DecimalFormat formatter = new DecimalFormat("0.000");
		
		//extract data from object containing lowest price
		GasDataPoint low = sortedDataByPrices.get(0);
		String lowDate = low.getDay()+"/"+low.getMonth()+"/"+low.getYear();
		String lowPrice = formatter.format(low.getPrice());
		
		//extract data from object containing highest price
		GasDataPoint high = sortedDataByPrices.get(lastIndex);
		String highDate = high.getDay()+"/"+high.getMonth()+"/"+high.getYear();
		String highPrice = formatter.format(high.getPrice());
		
		//format data
		String line = String.format("%-5s:%6s%12s%7s%12s%n",
				year,lowPrice, lowDate, highPrice,highDate);
		
		return line;
		
	}//end getYearMinAndMax method
	
    //***************************************************************
    //
    //  Method:       sortDataAscending (Non Static)
    // 
    //  Description:  A helper method that creates a new sorted list of
    //                GasDataPoint objects in ascending order based on 
    //                the object's "price" instance variable.
    //
    //  Parameters:   List<GasDataPoint> (data objects) unsorted
    //
    //  Returns:      List<GasDataPoint> (data objects) sorted
    //
    //**************************************************************
	private List<GasDataPoint> sortDataAscending(List<GasDataPoint> list) {

		List<GasDataPoint> sorted = list.stream()
				.sorted(Comparator.comparingDouble(GasDataPoint::getPrice))
				.collect(Collectors.toList());

		return sorted;
		
	}// end sortDataAscending method
	
    //***************************************************************
    //
    //  Method:       call (Non Static)
    // 
    //  Description:  Calls class methods to calculate the lowest and
    //                highest prices for each given year in the data set 
    //                and returns the aggregated results.
    //
    //  Parameters:   None
    //
    //  Returns:      String (lowest price, highest price, and dates)
    //
    //**************************************************************
	@Override
	public String call() throws Exception {
		
		//map original data to new (year, data objects) pairs and process
		String results = " [ NO DATA FOUND ]"; // default
		Map<Integer, ArrayList<GasDataPoint>> mappedDataPoints = mapDataToYear();
		if(!mappedDataPoints.isEmpty()) {
			results = generateResults(mappedDataPoints);
		}
		
		return results;
		
	}// end call method

}//end HighLowCallable class