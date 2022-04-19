import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
//  Description:   TODO 
//
//********************************************************************

public class Program8 {

	public static void main(String[] args) {
		
		Program8 test = new Program8();
		test.developerInfo();
		
		String filePath = "GasPrices.txt";
		List<GasDataPoint> data = new ArrayList<GasDataPoint>();
		test.populateList(data, filePath);
		
		int coreCount = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(coreCount);
		
		System.out.println("GAS PRICE STATISTICS\n");
		
		// TESTS //
		Future<String> yearAverages = pool.submit(new YearAverageRunnable(data));
		Future<String> monthAverages = pool.submit(new MonthAverageRunnable(data));
		
		try {
			System.out.println(yearAverages.get());
			System.out.println(monthAverages.get());
		} catch (CancellationException | ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		
		
		// shutdown
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
	}// end main method
	
	public void populateList(List<GasDataPoint> dataList, String filePath) {
		try (BufferedReader reader = new BufferedReader(
				new FileReader(new File(filePath)))) {
			
			String line = reader.readLine();
			while(line != null) {
				
				String[] arr = line.split("[-:]");
				try {
					int month = Integer.parseInt(arr[0]);
					int day = Integer.parseInt(arr[1]);
					int year = Integer.parseInt(arr[2]);
					double price = Double.parseDouble(arr[3]);
					dataList.add(new GasDataPoint(month, day, year, price)); // format is correct, add new GasData object
				} catch (NumberFormatException | NullPointerException e) {
					e.printStackTrace(); 
				}
				line = reader.readLine(); // read the next line
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
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
}
