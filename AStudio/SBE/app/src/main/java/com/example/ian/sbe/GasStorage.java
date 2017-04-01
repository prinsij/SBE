package com.example.ian.sbe;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Ian on 2017-04-01.
 */

public class GasStorage extends Component {
    public enum GAS {
        OXYGEN,
        NITROGEN,
        C02;
    }
    private Map<GAS, Integer> storage = new TreeMap<>();

    public int getAmount(GAS gas) {
        return storage.get(gas);
    }

    public void setAmount(GAS gas, int amount) {
        this.storage.put(gas, amount);
    }
}
