package com.example.ian.sbe;

import static java.lang.Math.abs;

/**
 * Created by Ian on 2017-04-01.
 */

public class Health extends Component {
    public int getHp() {
        return hp;
    }

    private int hp = 100;

    public Health(int hp) {
        this.hp = hp;
    }

    public void subtract(int amount) {
        if (amount <= this.hp) {
            this.hp -= amount;
        } else {
            this.hp = 0;
        }
    }

    public int checkForDamage(GasStorage gas) {
        int dmg = 0;
        if (gas.getAmount(GasStorage.GAS.OXYGEN) < .15) {
            dmg += 5;
        }
        if (gas.getAmount(GasStorage.GAS.C02) > .1) {
            dmg += 5;
        }
        if (abs(gas.getPressure() - 100) > 20) {
            dmg += 5;
        }
        return dmg;
    }
}
