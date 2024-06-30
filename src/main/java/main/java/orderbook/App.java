package main.java.orderbook;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.logging.Level;

public class App 
{
	private static String krakenWebSocket = "wss://ws.kraken.com/v2";
	
	private static JavaWebSocketClient client;
	
	private static Logger logger = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args ) throws InterruptedException
    {
    	
    	//Application runtime in milliseconds. Default is 15 minutes 
    	//but feel free to change to any value that suits you
    	//alternatively you can change the "while condition" below to an endless loop, 
    	//e.g. 'while (true)' and terminate execution manually
    	long end_time = System.currentTimeMillis() + 900000;
    	
    	beginSubscription();
    	
    	while(System.currentTimeMillis() < end_time) {
    		Thread.sleep(60000);
    		createCandle();
    	}
        
        endSubScription();
    }
    
    //function, takes currently available data and creates a candle from it
    private static void createCandle() {
    	if(!Orderbook.midPriceList.isEmpty()&&!Orderbook.bidList.isEmpty()
				&&!Orderbook.askList.isEmpty()) {
    		synchronized(Orderbook.askList) {
    			synchronized(Orderbook.bidList) {
    				synchronized(Orderbook.tickCounter) {
    					synchronized(Orderbook.midPriceList) {
    						Thread candleParseThread = new Thread(new CandleParser(
    								new ArrayList<Double>(Orderbook.midPriceList)));
        					
    						candleParseThread.start();
    						Orderbook.clearMidPrices();				
    					}
    				}
    			}
    		}
    	}else {
    		logMessage(Level.INFO,"No new candle created. Reason: No data available");
    	}
    }
    
    //subscribes to Kraken Websocket
    private static void beginSubscription() {
    	try {
        	client = new JavaWebSocketClient(new URI(krakenWebSocket));    
        	client.connectBlocking();     
        }catch (Exception e) {
        	e.printStackTrace();
        }
        
        client.send("{\"method\": \"subscribe\",\"params\": {\"channel\": \"book\",\"symbol\": [\"ALGO/USD\",\"MATIC/USD\"]}}");
    }
    
    //ends subscription to Kraken websocket
    private static void endSubScription() {
    	client.send("{\"method\": \"unsubscribe\",\"params\": {\"channel\": \"book\",\"symbol\": [\"ALGO/USD\",\"MATIC/USD\"],\"snapshot\":\"true\"}}");
        
        client.close();
        
        kafkaConsumption();
    }
    
    //displays all candles send to Kafka
    private static void kafkaConsumption() {
    	System.out.println(Orderbook.candleList);
    	KafkaHandler.getInstance().getConsumer().subscribe(Arrays.asList("BauerAndreasGSR"));
	    
	    ConsumerRecords<String, String> records = 
	    		KafkaHandler.getInstance().getConsumer().poll(Duration.ofMillis(100));
	    for (ConsumerRecord<String, String> record : records) {
	    	logMessage(Level.INFO,"Candle from Kafka retrieved. Value:"+record.value());
	    }
    }
    
    //globally available function used for logging
    public static void logMessage(Level lvl,String message) {
    	try {
			logger.log(Level.INFO,message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
