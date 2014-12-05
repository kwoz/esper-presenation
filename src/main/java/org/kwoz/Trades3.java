package org.kwoz;

import com.espertech.esper.client.*;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.TimerControlEvent;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.TradeEvent;

import static java.lang.Thread.sleep;

/**
 * @author karol.wozniak@nsn.com
 */
public class Trades3 {

    private static final int TEN_MINS = 1000 * 60 * 10;

    public static void main(String[] params) throws InterruptedException {
        EPServiceProvider serviceProvider = new EsperPowerServiceProvider().getServiceProvider();


        EPStatement statement = serviceProvider.getEPAdministrator().createEPL("SELECT avg(price)\n" +
                "FROM Trades.win:time(5 minutes)\n" +
                "WHERE name = \"złoto\"\n" +
                "OUTPUT SNAPSHOT EVERY 1 second");

        statement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for (EventBean e : newEvents) {
                    //System.out.format("%s %f %f %f \n", e.get("name"), e.get("avg(price)"), e.get("max(price)"), e.get("min(price)"));
                    System.out.println(e.get("name"));
                }
            }
        });

        final EPRuntime epRuntime = serviceProvider.getEPRuntime();
        epRuntime.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));

        final long t0 = System.currentTimeMillis();

        epRuntime.sendEvent(new TradeEvent("złoto", 300));
        epRuntime.sendEvent(new TradeEvent("pszenica", 20));
        epRuntime.sendEvent(new TradeEvent("uran", 30000));
        epRuntime.sendEvent(new TradeEvent("złoto", 310));
        epRuntime.sendEvent(new TradeEvent("pszenica", 22));
        epRuntime.sendEvent(new TradeEvent("uran", 29000));

        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 1001));

        epRuntime.sendEvent(new TradeEvent("złoto", 301));
        epRuntime.sendEvent(new TradeEvent("pszenica", 21));

        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 2001));

        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 10001));

        sleep(3000);

    }

}
