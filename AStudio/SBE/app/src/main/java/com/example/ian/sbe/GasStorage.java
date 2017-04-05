package com.example.ian.sbe;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Ian on 2017-04-01.
 */

public class GasStorage extends Component {
    public boolean isActive() {
        return active;
    }

    public enum GAS {
        OXYGEN,
        NITROGEN,
        C02;
    }
    private Map<GAS, Integer> storage = new TreeMap<>();

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active = true;

    public GasStorage(boolean active) {
        this.active = active;
    }

    public void airFill() {
        this.storage.clear();
        this.storage.put(GAS.OXYGEN, 21);
        this.storage.put(GAS.NITROGEN, 73);
        this.storage.put(GAS.C02, 6);
    }

    public int getPressure() {
        int total = 0;
        for (GAS gas : GAS.values()) {
            total += getAmount(gas);
        }
        return total;
    }

    public void clear() {
        this.storage.clear();
    }

    public GasStorage() {}

    public GasStorage(GAS defaultFill) {
        this.storage.put(defaultFill, 100);
    }

    public int getAmount(GAS gas) {
        try {
            return storage.get(gas);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setAmount(GAS gas, int amount) {
        this.storage.put(gas, amount);
    }
}
