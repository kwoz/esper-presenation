package org.kwoz;

import com.espertech.esper.client.*;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.ExampleEvent;
import org.kwoz.events.PowerEvent;

import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * @author karol.wozniak@nsn.com
 */
public class EsperExample {

    public static void main(String[] params) throws InterruptedException {
        EPServiceProvider serviceProvider = new EsperPowerServiceProvider().getServiceProvider();


        EPStatement statement = serviceProvider.getEPAdministrator().createEPL("SELECT * FROM Example");

        statement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
            for (EventBean e : newEvents) {
                System.out.println(e.get("field"));
            }
            }
        });

        final EPRuntime epRuntime = serviceProvider.getEPRuntime();

        ExampleEvent event = new ExampleEvent();
        event.setField("test");
        event.setOtherField(42);

        epRuntime.sendEvent(event);
        epRuntime.sendEvent(event);
        epRuntime.sendEvent(event);

        sleep(3000);

    }

}
