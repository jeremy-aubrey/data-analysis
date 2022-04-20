//********************************************************************
//
//  Author:        Jeremy Aubrey
//
//  Program #:     8
//
//  File Name:     AscendingSortCallable.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/20/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   A callable class that creates a sorted list of prices
//                 from the data. The prices are sorted in ascending order.
//
//********************************************************************

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AscendingSortCallable implements Callable<String> {

	private List<GasDataPoint> dataList;     
	
	// constructor
	public AscendingSortCallable(List<GasDataPoint> data) {
		dataList = data;
	}
	
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
	private List<GasDataPoint> sortDataAscending(List<GasDataPoint> dataList) {

		List<GasDataPoint> sorted = dataList.stream()
				.sorted(Comparator.comparingDouble(GasDataPoint::getPrice))
				.collect(Collectors.toList());

		return sorted;
		
	}// end sortDataAscending method
	
    //***************************************************************
    //
    //  Method:       generateResults (Non Static)
    // 
    //  Description:  Generates a String containing the aggregated 
    //                sorted data (ascending).
    //
    //  Parameters:   List<GasDataPoint> (sorted list)
    //
    //  Returns:      String (aggregated results)
    //
    //**************************************************************
	private String generateResults(List<GasDataPoint> sortedList) {
		
		StringBuilder results = new StringBuilder();
		
		//header and column titles
		results.append("PRICES SORTED HIGHEST TO LOWEST\n");
		results.append(String.format("%-13s%s%n","DATE","PRICE"));
		
		//for each GasDataPoint object, get the date and price
		for(GasDataPoint dp : sortedList) {
			String date = dp.getDay()+"/"+dp.getMonth()+"/"+dp.getYear();
			DecimalFormat formatter = new DecimalFormat("0.000");
			String formattedPrice = formatter.format(dp.getPrice());
			String line = String.format("%-11s:%6s%n",date,formattedPrice);
			results.append(line);
		}
		
		return results.toString();
		
	}// end generateResults method
	
    //***************************************************************
    //
    //  Method:       call (Non Static)
    // 
    //  Description:  Calls class methods to sort the data by price 
    //                in ascending order and returns the aggregated results.
    //
    //  Parameters:   None
    //
    //  Returns:      String (prices sorted in ascending order)
    //
    //**************************************************************
	@Override
	public String call() throws Exception {
		
		//sort data and pass it to be processed
		String results = " [ NO DATA FOUND ]"; // default
		List<GasDataPoint> sortedList = sortDataAscending(dataList);
		if(!sortedList.isEmpty()) {
			results = generateResults(sortedList);
		}
		
		return results;
		
	}// end call method
	
}// end AscendingSortCallable method