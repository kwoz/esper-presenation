package org.kwoz;

import com.espertech.esper.client.*;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.ExampleEvent;
import org.kwoz.events.TradeEvent;

import static java.lang.Thread.sleep;

/**
 * @author karol.wozniak@nsn.com
 */
public class Trades1 {
    public static void main(String[] params) throws InterruptedException {
        EPServiceProvider serviceProvider = new EsperPowerServiceProvider().getServiceProvider();


        EPStatement statement = serviceProvider.getEPAdministrator().createEPL("SELECT name " +
                "FROM Trades " +
                "WHERE price > 200");

        statement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for (EventBean e : newEvents) {
                    System.out.println(e.get("name"));
                }
            }
        });

        final EPRuntime epRuntime = serviceProvider.getEPRuntime();

        epRuntime.sendEvent(new TradeEvent("złoto", 300));
        epRuntime.sendEvent(new TradeEvent("pszenica", 20));
        epRuntime.sendEvent(new TradeEvent("uran", 30000));

        sleep(3000);

    }

}
