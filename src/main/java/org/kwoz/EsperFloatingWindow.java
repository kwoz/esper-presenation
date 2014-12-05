package org.kwoz;

import com.espertech.esper.client.*;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.PowerEvent;

import java.util.Date;

import static java.lang.Thread.sleep;

public class EsperFloatingWindow {

    private static final String FLOATING_WINDOW_QUERY = "select eNodebId, max(signalPower) " +
            "from PowerEvent.win:time( 3 sec ) " +
            "group by eNodebId " +
            "output snapshot every 1 sec";

    public static void main(String args[]) throws InterruptedException {
        EPServiceProvider esperService = new EsperPowerServiceProvider().getServiceProvider();

        EPStatement simpleStatement = esperService.getEPAdministrator().createEPL(FLOATING_WINDOW_QUERY);

        simpleStatement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                //System.out.println("Emit! new " + newEvents.length);
                //System.out.println(" old: " + oldEvents.length );
                for(EventBean e : newEvents){
                    System.out.println("[" + new Date().toString() + "] " + "eNodebId: " + e.get("eNodebId")+ " value: " + e.get("max(signalPower)"));
                }

            }
        });

        for(int second =0 ; second < 10; second++){
            esperService.getEPRuntime().sendEvent(new PowerEvent("a", 100 - 10 * second ));
            System.out.println("[" + new Date().toString() + "] Sent: " + (100 - 10 * second) );
            sleep(900);
        }

        sleep(5000);
    }
}
