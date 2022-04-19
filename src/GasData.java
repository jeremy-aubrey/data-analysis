
public class GasData {
	
	private int month;
	private int day;
	private int year;
	private double price;
	
	public GasData(int month, int day, int year, double price) {
		
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
	}

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
		return "GasData [" + month + "," + day + "," + year + "," + price + "]";
	}

}
