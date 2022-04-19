import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		List<GasData> data = new ArrayList<GasData>();
		
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
					data.add(new GasData(month, day, year, price));
				} catch (NumberFormatException | NullPointerException e) {
					
				}
				line = reader.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		for(GasData gd: data) {
			System.out.println(gd);
		}
		System.out.println(data.size() + " data objects found");
		
	}// end main method
	
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
