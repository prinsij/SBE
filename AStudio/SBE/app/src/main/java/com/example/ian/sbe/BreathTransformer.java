package com.example.ian.sbe;

import android.util.Log;

/**
 * Created by Ian on 2017-04-03.
 */

public class BreathTransformer extends GasTransformer {
    private static final int amount = 1;
    public void transform(GasStorage gasStorage) {
        int c02 = gasStorage.getAmount(GasStorage.GAS.C02);
        if (c02 > amount) {
            gasStorage.setAmount(GasStorage.GAS.C02, c02 + amount);
            gasStorage.setAmount(GasStorage.GAS.OXYGEN, gasStorage.getAmount(GasStorage.GAS.OXYGEN) - amount);
        }
    }
}
