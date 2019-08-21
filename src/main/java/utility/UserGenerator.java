package utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class UserGenerator {
	public static String generatePassword(String username) {
		StringBuffer stringBuffer = new StringBuffer(username);
		stringBuffer.setCharAt(0,username.toUpperCase().charAt(0));
		return new String(stringBuffer)+"@123";
	}
	public static String generateID(String username) {
		String result ="";
		result +=  username.charAt(0);
		result =result.toUpperCase();
		Random rand = new Random(); 
		Integer value = rand.nextInt(10000000)+1;
		result += value.toString();
		return result;
		
	}
	public static ArrayList<LocalDate> getDatesBetween(LocalDate startDate,LocalDate endDate){
		ArrayList<LocalDate> result = new ArrayList<LocalDate>();
		
		LocalDate date = startDate;
		while(!date.isEqual(endDate)) {
			result.add(date);
			date = date.plusDays(1);
		}
		result.add(endDate);
		
		return result;
	}
	public static ArrayList<LocalDate> getNextMondays(Integer noOfMondays,LocalDate startDate){
		ArrayList<LocalDate> mondays = new ArrayList<LocalDate>();
		Integer mondaysCount = 1;
		LocalDate date = startDate;
		while(mondaysCount<=noOfMondays) {
			
			if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
				mondays.add(date);
				mondaysCount += 1;
			}
			date = date.plusDays(1);
			
		}
		return mondays;
	}
	
}
