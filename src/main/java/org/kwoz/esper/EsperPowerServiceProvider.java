package org.kwoz.esper;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import org.kwoz.events.*;

public class EsperPowerServiceProvider {

    private final EPServiceProvider serviceProvider;

    public EsperPowerServiceProvider(){
        Configuration esperConfig = new Configuration();
        esperConfig.addEventType("Example", ExampleEvent.class);
        esperConfig.addEventType("PowerEvent", PowerEvent.class);
        esperConfig.addEventType("Trades", TradeEvent.class);
        //esperConfig.addEventType("CallEvent", CallEvent.class);
        //esperConfig.addEventType("TransmissionEvent", TransmissionEvent.class);

        serviceProvider = EPServiceProviderManager.getDefaultProvider(esperConfig);
    }

    public EPServiceProvider getServiceProvider(){
        return serviceProvider;
    }
}
