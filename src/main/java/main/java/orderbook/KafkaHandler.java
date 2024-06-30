package main.java.orderbook;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

//Kafka Handler for publishing created Candles to Kafka, and for receiving them in the end
//employs Singleton pattern
public class KafkaHandler {
	
	final Properties propsProducer = new Properties();
	private Producer<String, String> producer;
	
	final Properties propsConsumer = new Properties();
	private Consumer<String, String> consumer;
	
	private static volatile KafkaHandler instance = null;
 
	public KafkaHandler(){
	  
		//Configure Producer
		propsProducer.put("bootstrap.servers","localhost:9092"); 
		propsProducer.put("acks", "all");
		propsProducer.put("retries", 0); 
		propsProducer.put("batch.size", 16384);
		propsProducer.put("linger.ms", 1); 
		propsProducer.put("buffer.memory",33554432); 
		propsProducer.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		propsProducer.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
	  
		this.producer = new KafkaProducer<>(propsProducer);
	  
		//Configure Consumer
		propsConsumer.setProperty("bootstrap.servers", "localhost:9092");
		propsConsumer.setProperty("group.id", "test");
		propsConsumer.setProperty("enable.auto.commit", "true");
		propsConsumer.setProperty("auto.commit.interval.ms", "1000");
		propsConsumer.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		propsConsumer.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
	  
		this.consumer = new KafkaConsumer<>(propsConsumer);
	  
	}
	 
	//get KafkaHandler
	public static KafkaHandler getInstance() {
		if (instance == null) {
			synchronized(KafkaHandler.class) { 
				if (instance == null) { 
					instance = new KafkaHandler(); 
				} 
			}
		} 
		return instance; 
	}
	
	public Producer<String, String> getProducer() { 
		return producer; 
	}
	  
	public Consumer<String, String> getConsumer() { 
		return consumer; 
	}
}