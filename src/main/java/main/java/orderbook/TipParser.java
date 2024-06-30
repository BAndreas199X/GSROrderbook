package main.java.orderbook;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//Class to create a Tip out of a received snapshit. employs Multithread
public class TipParser implements Runnable {

	//snapshot received
	private String snapshotString;
	
	final ObjectMapper g_ObjectMapper = new ObjectMapper();

	public TipParser(String i_snapshotString) {
		this.snapshotString = i_snapshotString;
	}

	
	@Override 
	public void run() {
	  
		//retreive ask and bid data from snapshot
		String substringBids = StringUtils.substringBetween(snapshotString, "\"bids\":",
				  ",\"asks");
	  
		String substringAsks = StringUtils.substringBetween(snapshotString, "\"asks\":",
				  ",\"checksum\"");
	  
		TypeReference<List<Bid>> b_TypeReference = new TypeReference<List<Bid>>() {};
		TypeReference<List<Ask>> a_TypeReference = new TypeReference<List<Ask>>() {};
		try { 
			List<Bid> bid_list = g_ObjectMapper.readValue(substringBids,b_TypeReference); 
			
			List<Ask> ask_list = g_ObjectMapper.readValue(substringAsks, a_TypeReference);
	  
			
			//add retreived Tip data to Orderbook lists
			Orderbook.appendToBidList(bid_list);
			Orderbook.appendToAskList(ask_list); 
	  
			//calculate mid price from received tip data, add it to Orderbook data
			Double maxBid = bid_list.stream().mapToDouble(c -> Double.parseDouble(c.getPrice()))
				      .max().orElseThrow(NoSuchElementException::new);
			
			Double minAsk = ask_list.stream().mapToDouble(c -> Double.parseDouble(c.getPrice()))
					.min().orElseThrow(NoSuchElementException::new);
			
			if(maxBid<minAsk) {
				Orderbook.addMidPrice(Orderbook.midPriceCalculator(maxBid,minAsk));
			}
	  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			App.logMessage(Level.INFO,"New Snapshot received. Safeguarded successfully!"
					+ "\nValues:"+this.snapshotString);
		}
	}
}
