package org.kwoz.events;

public class PowerEvent {
    private String eNodebId;
    private double signalPower;

    public String geteNodebId() {
        return eNodebId;
    }

    public void seteNodebId(String eNodebId) {
        this.eNodebId = eNodebId;
    }

    public double getSignalPower() {
        return signalPower;
    }

    public void setSignalPower(double signalPower) {
        this.signalPower = signalPower;
    }

    public PowerEvent(String eNodebId, double signalPower) {
        this.eNodebId = eNodebId;
        this.signalPower = signalPower;
    }

}
