package main.java.orderbook;

import java.net.URI;
import java.util.logging.Level;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

class JavaWebSocketClient extends WebSocketClient {

	 public JavaWebSocketClient(URI serverUri) {
	    super(serverUri);        
	 }
	 
	 @Override
	 public void onOpen(ServerHandshake handshakedata) {
	    App.logMessage(Level.INFO,"Connection to Kraken Websocket successful!");  
	 }

	 @Override
	 public void onMessage(String message) {
		 
		 onMessageAction(message);
	 }

	 @Override
	 public void onClose(int code, String reason, boolean remote) {
		 App.logMessage(Level.INFO,"Disconnected from Kraken Websocket successful!"); 
	 }

	 @Override
	 public void onError(Exception ex) {
	    ex.printStackTrace();
	 }
	 
	 //initiates "Tip"-creation once a Snapshot is received.
	 public void onMessageAction(String message) {
		 //Filtering out non-Snapshot messages
		 if(message.contains("snapshot")&&message.contains("bid")&&message.contains("ask")) {
			 Orderbook.tickIncrease();
			 Thread tipParseThread = new Thread(new TipParser(message));
			 tipParseThread.start();
		 }
	 }
}
