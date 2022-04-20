//********************************************************************
//
//  Author:        Jeremy Aubrey
//
//  Program #:     8
//
//  File Name:     Program8.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/20/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   A multi-threaded program that generates statistics based
//                 on historical gas price data read from a file.
//                 
//                 Data displayed :
//                 - Average price per year
//                 - Average price per month
//                 - High and Low prices per year
//                 
//                 Data written to file:
//                 - List of prices (ascending)
//                 - List of prices (descending)
//
//********************************************************************

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Program8 {

	public static void main(String[] args) {
		
		Program8 test = new Program8();
		test.developerInfo();
		
		//populate data List from file data
		String filePath = "GasPrices.txt";
		List<GasDataPoint> data = new ArrayList<GasDataPoint>();
		test.populateList(data, filePath);
		
		//to run calculations in separate threads
		int coreCount = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(coreCount);
		
		//process statistics in separate threads
		Future<String> yearAverages = pool.submit(new YearAverageCallable(data));
		Future<String> monthAverages = pool.submit(new MonthAverageCallable(data));
		Future<String> lowHighByYear = pool.submit(new HighLowCallable(data));
		Future<String> sortedAscending = pool.submit(new AscendingSortCallable(data));
		Future<String> sortedDescending = pool.submit(new DescendingSortCallable(data));
		
		//display results and create text files
		try {
			System.out.println("\nGAS PRICE STATISTICS\n");
			System.out.println(yearAverages.get());
			System.out.println(monthAverages.get());
			System.out.println(lowHighByYear.get());
			
			String ascendingPriceData = sortedAscending.get();
			String descendingPriceData = sortedDescending.get();
			
			test.createFile("Prices_Sorted_Ascending.txt", ascendingPriceData);
			test.createFile("Prices_Sorted_Descending.txt", descendingPriceData);
			
		} catch (CancellationException | ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		
		// shutdown thread pool
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	
	System.out.println("[ COMPLETE ]");
		
	}// end main method
	
    //***************************************************************
    //
    //  Method:       isValidDate (Non Static)
    // 
    //  Description:  A helper method to validate dates.
    //
    //  Parameters:   int (day), int (month), int (year)
    //
    //  Returns:      boolean (true if date is valid) 
    //
    //**************************************************************
	private boolean isValidDate(int day, int month, int year) {
		
		boolean isValid = false;
		if(day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 1900) {
			isValid = true;
		}
		return isValid;
		
	}//end isValidDate method
	
    //***************************************************************
    //
    //  Method:       populateList (Non Static)
    // 
    //  Description:  A helper method to populate the data List with 
    //                GasDataPoint objects from data read from a file.
    //
    //  Parameters:   List, String (filePath)
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	private void populateList(List<GasDataPoint> dataList, String filePath) {
		System.out.println("[ READING FILE... ]");
		try (BufferedReader reader = new BufferedReader(
				new FileReader(new File(filePath)))) {
			
			//for each line in file, create an array of data
			String line = reader.readLine();
			int lineNumber = 0;
			while(line != null) {
				lineNumber++;
				String[] arr = line.split("[-:]"); // split on - and : characters
				
				// validate data, and instantiate new object
				if(arr.length == 4) {
					try {
						int month = Integer.parseInt(arr[0]);
						int day = Integer.parseInt(arr[1]);
						int year = Integer.parseInt(arr[2]);
						double price = Double.parseDouble(arr[3]);
						if(isValidDate(day, month, year)) {
							dataList.add(new GasDataPoint(month, day, year, price)); // date is valid
						}
					} catch (NumberFormatException | NullPointerException e) {
						System.out.println("[ BAD DATA ON LINE "+lineNumber+" - OMMITED ]");
					}
					
				} else {
					System.out.println("[ BAD DATA ON LINE "+lineNumber+" - OMMITED ]");
				}
				
				line = reader.readLine(); // read the next line
			}
			
		} catch (IOException e) {
			System.out.println("[ ERROR - "+e.getLocalizedMessage()+" ]");
		} 
		System.out.println("[ GOT "+dataList.size()+" RECORDS ]");
		
	}//end populateList method
	
    //***************************************************************
    //
    //  Method:       createFile (Non Static)
    // 
    //  Description:  Creates a file and writes data to it.
    //
    //  Parameters:   String (name of file), String (data)
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	private void createFile(String name, String data) {
		
		try {
			FileWriter fw = new FileWriter(name);
			fw.write(data);
			fw.close();
		} catch(IOException e) {
			System.out.println(" [ ERROR OCCURED WRITING FILE ]");
		}
	}//end createFile method
	
    //***************************************************************
    //
    //  Method:       developerInfo (Non Static)
    // 
    //  Description:  The developer information method of the program.
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public void developerInfo()
    {
       System.out.println("Name:    Jeremy Aubrey");
       System.out.println("Course:  COSC 4301 Modern Programming");
       System.out.println("Program: 8\n");

    } // end developerInfo method
    
}//end Program8 class