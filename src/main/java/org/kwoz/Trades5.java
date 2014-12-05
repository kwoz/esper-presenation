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
public class Trades5 {

    public static void main(String[] params) throws InterruptedException {
        EPServiceProvider serviceProvider = new EsperPowerServiceProvider().getServiceProvider();

        EPStatement statement = serviceProvider.getEPAdministrator().createEPL("SELECT * \n" +
                "FROM Trades MATCH_RECOGNIZE (\n" +
                "PARTITION BY name\n" +
                "MEASURES A.name as name, A.price as pocz, count(B.price) as liczba, C.price as koniec\n" +
                "PATTERN (A B+ C)\n" +
                "DEFINE " +
                "A AS A.price <= 200, " +
                "B AS B.price > 200, " +
                "C AS C.price <= 200 )");

        statement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for (EventBean e : newEvents) {
                    //System.out.format("%s %f %f %f \n", e.get("name"), e.get("avg(price)"), e.get("max(price)"), e.get("min(price)"));
                    System.out.println(e.get("name") +  " " +e.get("pocz") + " " + e.get("liczba") + " " + e.get("koniec"));
                }
            }
        });

        final EPRuntime epRuntime = serviceProvider.getEPRuntime();
        epRuntime.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));

        final long t0 = System.currentTimeMillis();

        epRuntime.sendEvent(new TradeEvent("złoto", 199));
        epRuntime.sendEvent(new TradeEvent("złoto", 198));
        epRuntime.sendEvent(new TradeEvent("pszenica", 199));
        epRuntime.sendEvent(new TradeEvent("pszenica", 204));
        epRuntime.sendEvent(new TradeEvent("pszenica", 198));

        epRuntime.sendEvent(new TradeEvent("miedź", 199));
        epRuntime.sendEvent(new TradeEvent("miedź", 203));
        epRuntime.sendEvent(new TradeEvent("miedź", 203));
        epRuntime.sendEvent(new TradeEvent("miedź", 199));


        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 1001));

        epRuntime.sendEvent(new TradeEvent("złoto", 301));
        epRuntime.sendEvent(new TradeEvent("pszenica", 21));

        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 2001));

        epRuntime.sendEvent(new CurrentTimeEvent(t0 + 10001));

        sleep(3000);

    }
}
