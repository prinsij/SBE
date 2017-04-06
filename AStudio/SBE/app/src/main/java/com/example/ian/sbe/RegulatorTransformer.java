package com.example.ian.sbe;

import static java.lang.StrictMath.abs;

/**
 * Created by Ian on 2017-04-06.
 */

public class RegulatorTransformer extends GasTransformer {
    private static int idealPressure = 100, idealo2 = 21, idealc02 = 6, idealToxin = 0;
    private static int changeAmount = 3;
    @Override
    public void transform(GasStorage gasStorage) {
        if (gasStorage.getAmount(GasStorage.GAS.C02) > idealc02) {
            gasStorage.subtractGas(GasStorage.GAS.C02, changeAmount);
        }
        if (gasStorage.getAmount(GasStorage.GAS.OXYGEN) < idealo2) {
            gasStorage.addGas(GasStorage.GAS.OXYGEN, changeAmount);
        }
        if (gasStorage.getAmount(GasStorage.GAS.TOXIN) > idealToxin) {
            gasStorage.subtractGas(GasStorage.GAS.TOXIN, changeAmount);
        }
        int pressureDiff = gasStorage.getPressure() - idealPressure;
        if (pressureDiff == 0) {
            return;
        }
        if (abs(pressureDiff) > changeAmount) {
            pressureDiff = changeAmount * (pressureDiff / abs(pressureDiff));
        }
        if (pressureDiff < 0) {
            gasStorage.addGas(GasStorage.GAS.NITROGEN, pressureDiff);
        } else {
            gasStorage.subtractGas(GasStorage.GAS.NITROGEN, pressureDiff);
        }
    }
}
