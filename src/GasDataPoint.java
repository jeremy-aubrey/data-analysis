//********************************************************************
//
//  Author:        Jeremy Aubrey
//
//  Program #:     8
//
//  File Name:     GasDataPoint.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      04/20/2022
//
//  Instructor:    Fred Kumi 
//
//  Description:   A class that maintains data about weekly average prices for a
//                 gallon of gasoline.
//
//********************************************************************

public class GasDataPoint {
	
	private int month;
	private int day;
	private int year;
	private double price;
	
	// constructor
	public GasDataPoint(int month, int day, int year, double price) {
		
		if(month > 0 && month <= 12) {
			this.month = month;
		}
		
		if(day > 0 && day <= 31) {
			this.day = day;
		}
		
		if(year > 0) {
			this.year = year;
		}
		
		if(price > 0.00) {
			this.price = price;
		}
	}//end constructor

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getYear() {
		return year;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "GasDataPoint [" + month + "," + day + "," + year + "," + price + "]";
	}

}//end GasDataPoint class