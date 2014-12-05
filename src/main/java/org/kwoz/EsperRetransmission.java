package org.kwoz;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.kwoz.esper.EsperPowerServiceProvider;
import org.kwoz.events.TransmissionEvent;

import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * @author karol.wozniak@nsn.com
 */
public class EsperRetransmission {

    private static final String FLOATING_WINDOW_QUERY = "select failedBytes , okBytes, failedBytes / okBytes as errorRate " +
            "from TransmissionEvent " +
            "match_recognize ( " +
            "measures sum(transfer_failed.size) as failedBytes, sum(transfer_ok.size) as okBytes " +
            "pattern ( ( (transfer_failed fail)* | (transfer_ok OK)* )* over ) " +
            "define " +
            "transfer_failed as transfer_failed.msgType ='send'," +
            "transfer_ok as transfer_ok.msgType ='send'," +
            "fail as fail.msgType = 'failure'," +
            "OK as OK.msgType = 'OK'," +
            "over as over.msgType = 'over' " +
            ")";

    public static void main(String args[]) throws InterruptedException {
        EPServiceProvider esperService = new EsperPowerServiceProvider().getServiceProvider();

        EPStatement simpleStatement = esperService.getEPAdministrator().createEPL(FLOATING_WINDOW_QUERY);

        simpleStatement.addListener(new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for (EventBean e : newEvents) {
                    System.out.println("[" + new Date().toString() + "] " + "failedBytes: " + e.get("failedBytes") +
                        " okBytes: " + e.get("okBytes") +
                        " errorRate: " + e.get("errorRate")
                    );

                }

            }
        });

        esperService.getEPRuntime().sendEvent(new TransmissionEvent("send", 23));
        esperService.getEPRuntime().sendEvent(new TransmissionEvent("failure"));

        esperService.getEPRuntime().sendEvent(new TransmissionEvent("send", 17));
        esperService.getEPRuntime().sendEvent(new TransmissionEvent("OK"));

        esperService.getEPRuntime().sendEvent(new TransmissionEvent("send", 27));
        esperService.getEPRuntime().sendEvent(new TransmissionEvent("failure"));

        esperService.getEPRuntime().sendEvent(new TransmissionEvent("over"));

        System.out.println(FLOATING_WINDOW_QUERY);
    }


}



