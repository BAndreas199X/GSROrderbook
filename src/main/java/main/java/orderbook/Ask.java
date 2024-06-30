package main.java.orderbook;

//Class to store "Asks" as objects, extends Tip
public class Ask extends Tip{

	final TipSideType tipSide = TipSideType.ASK;

	@Override
	public String toString() {
		return "Ask [tipSide=" + tipSide + ", Price=" + getPrice() + ", Quantity=" + getQuantity() + "]";
	}
	
}
