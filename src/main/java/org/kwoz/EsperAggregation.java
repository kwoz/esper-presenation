package org.kwoz;

import com.espertech.esper.client.*;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.PowerEvent;

import java.util.Date;

import static java.lang.Thread.sleep;

public class EsperAggregation {

    private static final String AGGREGATION_QUERY = "select eNodebId, max(signalPower) " +
            "from PowerEvent.win:time_batch( 2 sec ) " +
            "group by eNodebId";

    public static void main(String args[]) throws InterruptedException {
        EPServiceProvider esperService = new EsperPowerServiceProvider().getEsperService();

        EPStatement simpleStatement = esperService.getEPAdministrator().createEPL(AGGREGATION_QUERY);

        simpleStatement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for(EventBean e : newEvents){
                    System.out.println("[" + new Date().toString() + "] " + "eNodebId: " + e.get("eNodebId")+ " value: " + e.get("max(signalPower)"));
                }
            }
        });

        esperService.getEPRuntime().sendEvent(new PowerEvent("a", 10));
        esperService.getEPRuntime().sendEvent(new PowerEvent("a", 10));
        esperService.getEPRuntime().sendEvent(new PowerEvent("b", 30));
        esperService.getEPRuntime().sendEvent(new PowerEvent("b", 30));

        sleep(3000);

        esperService.getEPRuntime().sendEvent(new PowerEvent("a", 40));
        esperService.getEPRuntime().sendEvent(new PowerEvent("a", 40));
        esperService.getEPRuntime().sendEvent(new PowerEvent("b", 50));
        esperService.getEPRuntime().sendEvent(new PowerEvent("b", 50));

        sleep(2000);

    }
}
