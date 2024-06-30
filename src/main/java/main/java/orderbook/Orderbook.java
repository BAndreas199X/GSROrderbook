package main.java.orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//Orderbook, stores all the candles created, as well as the data used to create it
//ploys singleton design pattern
public class Orderbook {

	//collections used to store the data neccesary for creating candles
	static List<Bid> bidList = new ArrayList<Bid>();
	static List<Ask> askList = new ArrayList<Ask>();
	static Integer tickCounter = 0;
	static List<Double> midPriceList = new ArrayList<Double>();
	
	//collection of all created candles
	static List<Candle> candleList = new ArrayList<Candle>();
	
    private static volatile Orderbook Orderbook_instance = null;

    private Orderbook() {
    	App.logMessage(Level.INFO,"Orderbook successfully created!");
    }

    public static Orderbook getInstance() {
        if (Orderbook_instance == null) {
            synchronized(Orderbook.class) {
                if (Orderbook_instance == null) {
                	Orderbook_instance = new Orderbook();
                }
            }
        }
        return Orderbook_instance;
    }
    
    //functions used to add to the collections
    public static void appendToBidList(List<Bid> i_BidsList) {
    	synchronized (bidList) {
    		bidList.addAll(i_BidsList);
    	}
    }
    
    public static void appendToAskList(List<Ask> i_AsksList) {
    	synchronized (askList) {
    		askList.addAll(i_AsksList);
    	}
    }
	
	public static void tickIncrease() {
			tickCounter++;
	}
	
	public static void addMidPrice(double i_MidPrice) {
		 midPriceList.add(i_MidPrice);
	}
	
	public static void addCandle(Candle i_Candle) {
		candleList.add(i_Candle);
	}
	
	public static void clearMidPrices() {
		midPriceList.clear();
	}
	
	public static void clearData() {
		midPriceList.clear();
		bidList.clear();
		askList.clear();
	}
    
	//function used to calculate mid price
	public static double midPriceCalculator(double i_bid,double i_ask) {
		return (i_bid + i_ask)/2;
	}
}
