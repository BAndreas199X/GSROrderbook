# GSROrderbook
GSR Markets’ Data Engineer Test

Assumption 1: In production the program would run potentially indefinitely
In production, I assume my program would run 24/7, as it’s possible that Kraken might publish snapshot data at any daytime, at night, on weekends, holidays, etc. However, since this is only a “simulation”, I wrote the program to terminate after 15 minutes. Should the program be expected to run indefinitely, the “while” loop in “App.class”, line 33  can be changed to an endless loop, e.g. “while(true)”
Assumption 2: Database storage is not part of the assignment
In production, I’d definitely store candles in a database after creating them. However, database storage is not mentioned in the assignment. To the contrary, there is mention of “in memory” and Kafka in the document, which I take as database storage being explicitly excluded from the assignment. For that reason, no consideration of data storage in my program.
Assumption 3: Flexibility and maintainability are preferred
When extracting the Bid and Ask data from the received JSON string in TipParser.class (lines 30 - 34) I could have used a JSON parser. However, this would have made the definition of a “Tick” object necessary. Since the JSON string we receive is quite convoluted, I decided to use “StringUtils.substringBetween” instead. Outside of being easier to implement: Should the JSON format used by Kraken ever change, adjustment would be easier. The result is higher flexibility and maintainability.
Assumption 4: Only snapshot data must be considered
When subscribing to Kraken’s “book” websocket, you receive either “snapshots” or “updates”. However, the instructions only mention “Snapshots”. Furthermore, updates only contain either ask or bid data, and since both are necessary to calculate the mid price, I have only considered snapshots. Should my assumption be erroneous, the if-clause I use to filter the messages in JavaWebSocketClient.class, line 39, may be adjusted.
Assumption 5: The storage of Tip data must never be interrupted
The reception of tick data must never be interrupted, since lacking data might falsify the results. E.g. if a tick is missing, the mid price levels might be calculated as too low or too high. Therefore, when creating a candle, or converting the received JSON string to object, the program must not be interrupted.
For that reason, I have used multithreads when creating Tips or Candles, which run parallel to the main program. That way, the reception of ticks and tip data will never be interrupted.
Assumption 6: Open and Close Mid Price must consider ALL ticks
When calculating open and close mid price, based on the phrasing “at the beginning/end of the minute”, as opposed to “during the minute”, I assume that the ask and bid values of all ticks received must be evaluated. Not just the data received during the minutes. This assumption is also based on the fact that, when calculating the Open Mid Price, there would be no data available when only considering data of the minute.
An alternative interpretation might be using the first and last mid price calculated during the minute, respectively.
Assumption 7: High and Low Mid Price must only consider the ticks observed during the respective minute.
When calculating high and low mid price, based on the phrasing “during the minute”, I assume that only mid prices calculated from ticks received during the respective minute must be considered.
Remark to assumptions 6 and 7:
As I was not 100% sure whether my interpretation, which I outlined in assumptions 6 and 7, is correct, I have created the infrastructure for alternative interpretations.These regard the four mid price variables in the candle: Open Mid Price, High Mid Price, Low Mid Price, Close Mid Price

In case the tip data for all ticks received should be considered for all variables, the command “Orderbook.clearMidPrices()” in App.class, line 53, can be removed. I have also created an alternative Constructor in CandleParser.class, which does not have any input variables .

In case only the tip data for ticks received during the last minute should be considered for all variables, then the command “Orderbook.clearMidPrices()” in App.class, line 53, should be replaced by the function “Orderbook.clearData()”, which I have created in “Orderbook.class”. The alternative constructor  in CandleParser.class, which has three times List<T> as parameters, should be used instead..


