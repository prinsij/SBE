package com.example.ian.sbe;

import static java.lang.StrictMath.abs;

/**
 * Created by Ian on 2017-04-06.
 */

public class RegulatorTransformer extends GasTransformer {
    @Override
    public void transform(GasStorage gasStorage) {
        // adjust local atmosphere to desired levels, percolation will eventually fix the
        // whole station if we keep this up
        int changeAmount = Settings.getSingleton().getRegulatorChange();
        if (gasStorage.getAmount(GasStorage.GAS.C02) > Settings.getSingleton().getIdealc02()) {
            gasStorage.subtractGas(GasStorage.GAS.C02, changeAmount);
        }
        if (gasStorage.getAmount(GasStorage.GAS.OXYGEN) < Settings.getSingleton().getIdealo2()) {
            gasStorage.addGas(GasStorage.GAS.OXYGEN, changeAmount);
        }
        if (gasStorage.getAmount(GasStorage.GAS.TOXIN) > Settings.getSingleton().getIdealToxin()) {
            gasStorage.subtractGas(GasStorage.GAS.TOXIN, changeAmount);
        }
        int pressureDiff = gasStorage.getPressure() - Settings.getSingleton().getIdealPressure();
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
