package main.java.orderbook;

//Class to store "Bids" as objects, extends Tip
public class Bid extends Tip{
	
	final TipSideType tipSide = TipSideType.BID;

	@Override
	public String toString() {
		return "Bid [tipSide=" + tipSide + ", Price=" + getPrice() + ", Quantity=" + getQuantity() + "]";
	}
	
}
