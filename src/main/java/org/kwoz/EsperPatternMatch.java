package org.kwoz;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.CallEvent;

import java.util.Date;

import static java.lang.Thread.sleep;

public class EsperPatternMatch {
    private static final String FLOATING_WINDOW_QUERY = "select * from CallEvent " +
            "match_recognize ( " +
            "partition by phoneId " +
            "measures Init.phoneId as phoneId, Init.msgTime as CallStart, Failure.msgTime as CallEnd " +
            "pattern (Init Failure) " +
            "define " +
            "Init as Init.msgType ='StartCall'," +
            "Failure as Failure.msgType='CallFailure' "+
            ")" ;

    public static void main(String args[]) throws InterruptedException {
        EPServiceProvider esperService = new EsperPowerServiceProvider().getServiceProvider();

        EPStatement simpleStatement = esperService.getEPAdministrator().createEPL(FLOATING_WINDOW_QUERY);

        simpleStatement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for(EventBean e : newEvents){
                    System.out.println("[" + new Date().toString() + "] " + "phoneId: " + e.get("phoneId")
                            + " start: " + e.get("CallStart")
                            + " end: " + e.get("CallEnd") );
                }

            }
        });

        esperService.getEPRuntime().sendEvent(new CallEvent("123", "StartCall", new Date()));
        esperService.getEPRuntime().sendEvent(new CallEvent("456", "StartCall", new Date()));
        sleep(1000);
        esperService.getEPRuntime().sendEvent(new CallEvent("123", "EndCall", new Date()));
        esperService.getEPRuntime().sendEvent(new CallEvent("456", "CallFailure", new Date()));


        System.out.println(FLOATING_WINDOW_QUERY);
    }


}
