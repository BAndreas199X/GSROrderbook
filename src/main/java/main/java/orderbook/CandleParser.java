package main.java.orderbook;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//Class to create a Candle out of the available Tip data. employs Multithread
public class CandleParser implements Runnable{
	
	private int tickKnockOff;
	
	private Candle new_Candle;
	private Candle prev_Candle;
	
	private List<Bid> bidList;
	private List<Ask> askList;
	private List<Double> midPrices;
	
	public CandleParser() {	
		this.new_Candle = new Candle();
		
		this.tickKnockOff = Orderbook.tickCounter;
		this.bidList = Orderbook.bidList;
		this.askList = Orderbook.askList;
		
		this.midPrices = Orderbook.midPriceList;
	}
	
	public CandleParser(List<Double> midPriceList) {	
		this.new_Candle = new Candle();
		
		this.tickKnockOff = Orderbook.tickCounter;
		this.bidList = Orderbook.bidList;
		this.askList = Orderbook.askList;
		
		this.midPrices = midPriceList;
	}
	
	public CandleParser(List<Double> midPriceList,List<Bid> bidList,List<Ask> askList) {	
		this.new_Candle = new Candle();
		
		this.tickKnockOff = Orderbook.tickCounter;
		
		this.bidList = bidList;
		this.askList = askList;
		this.midPrices = midPriceList;
	}

	@Override
	public void run() {
		
		//Set tick counter and Open Mid Price
		//differs based on whether we're setting the first candle or not
		if(!(Orderbook.candleList.isEmpty())) {
			this.prev_Candle = Orderbook.candleList.get(Orderbook.candleList.size()-1);
			
			this.new_Candle.setCandle_ticks(tickKnockOff-this.prev_Candle.getTotal_ticks());
			
			this.new_Candle.setOpenMidPrice(this.prev_Candle.getCloseMidPrice());
			
		}else {
			this.new_Candle.setCandle_ticks(tickKnockOff);
			
			this.new_Candle.setOpenMidPrice(null);	
		}
		
		//Sets how many ticks have been observed overall
		this.new_Candle.setTotal_ticks(this.tickKnockOff);
		
		//Calculates Close Mid Price out of averafe from Bids and Asks
		Double averageBid = bidList.stream().mapToDouble(c -> Double.parseDouble(c.getPrice())).average().orElse(Double.NaN);
		Double averageAsk = askList.stream().mapToDouble(c -> Double.parseDouble(c.getPrice())).average().orElse(Double.NaN);
		
		if(averageBid<averageAsk) {
			this.new_Candle.setCloseMidPrice(Double.toString(Orderbook.midPriceCalculator(
					averageBid,averageAsk)));
		}
		//streams mid price array to find maximum und minimum mid prices
		Double maxMidPrice = Collections.max(this.midPrices);
		Double minMidPrice = Collections.min(this.midPrices);
		
		//assign result to value
		if(!maxMidPrice.isNaN()) {
			this.new_Candle.setHighMidPrice(Double.toString(maxMidPrice));
		}
		
		if(!minMidPrice.isNaN()) {
			this.new_Candle.setLowMidPrice(Double.toString(minMidPrice));
		}
		
		//store created candle
		Orderbook.addCandle(this.new_Candle);
		App.logMessage(Level.INFO,"New Candle created and registered."
				+ "\nValues:"+this.new_Candle.toString());
		
		
		//send created candle to Kafka
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		try {
			String jsonStr = objectMapper.writeValueAsString(new_Candle);

			String timestamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());
			
			KafkaHandler.getInstance().getProducer().send(new ProducerRecord<String,String>(
							"BauerAndreasGSR", "Key-timestamp "+timestamp, jsonStr));
			
			App.logMessage(Level.INFO,"New Candle successfully submitted to Kafka."
					+ "\nTopic: "+"BauerAndreasGSR");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
