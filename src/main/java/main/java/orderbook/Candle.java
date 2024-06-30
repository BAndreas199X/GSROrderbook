package main.java.orderbook;

import java.text.SimpleDateFormat;

//Class to store "Candles" as objects
public class Candle {
	
	final String Timestamp =  new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
	private String openMidPrice;
	private String highMidPrice;
	private String lowMidPrice;
	private String closeMidPrice;
	private int total_ticks;
	private int candle_ticks;
	
	public String getOpenMidPrice() {
		return openMidPrice;
	}
	public void setOpenMidPrice(String openMidPrice) {
		this.openMidPrice = openMidPrice;
	}
	public String getHighMidPrice() {
		return highMidPrice;
	}
	public void setHighMidPrice(String highMidPrice) {
		this.highMidPrice = highMidPrice;
	}
	public String getLowMidPrice() {
		return lowMidPrice;
	}
	public void setLowMidPrice(String lowMidPrice) {
		this.lowMidPrice = lowMidPrice;
	}
	public String getCloseMidPrice() {
		return closeMidPrice;
	}
	public void setCloseMidPrice(String closeMidPrice) {
		this.closeMidPrice = closeMidPrice;
	}
	public int getTotal_ticks() {
		return total_ticks;
	}
	public void setTotal_ticks(int total_ticks) {
		this.total_ticks = total_ticks;
	}
	public int getCandle_ticks() {
		return candle_ticks;
	}
	public void setCandle_ticks(int candle_ticks) {
		this.candle_ticks = candle_ticks;
	}
	
	@Override
	public String toString() {
		return "Candle [Timestamp=" + Timestamp + ", Open Mid Price=" + openMidPrice + ", High Mid Price=" + highMidPrice
				+ ", Low Mid Price=" + lowMidPrice + ", Close Mid Price=" + closeMidPrice + ", Total Ticks=" + total_ticks
				+ ", Candle Ticks=" + candle_ticks + "]";
	}
}
