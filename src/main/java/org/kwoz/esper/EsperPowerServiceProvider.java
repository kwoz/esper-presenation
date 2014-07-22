package org.kwoz.esper;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import org.kwoz.events.CallEvent;
import org.kwoz.events.PowerEvent;
import org.kwoz.events.TransmissionEvent;

public class EsperPowerServiceProvider {

    private final Configuration esperConfig;
    private final EPServiceProvider esperService;

    public EsperPowerServiceProvider(){
        esperConfig = new Configuration();
        esperConfig.addEventType("PowerEvent", PowerEvent.class);
        esperConfig.addEventType("CallEvent", CallEvent.class);
        esperConfig.addEventType("TransmissionEvent", TransmissionEvent.class);

        esperService = EPServiceProviderManager.getDefaultProvider(esperConfig);
    }

    public EPServiceProvider getEsperService(){
        return esperService;
    }

}
