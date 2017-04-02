package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class PowerDraw extends Component {
    private boolean isOn;
    private final int amount;

    public int getAmount() {
        return amount;
    }

    public boolean isOn() {
        return isOn;
    }

    public void toggle(boolean on) {
        this.isOn = on;
    }

    public PowerDraw(int amount) {
        this.amount = amount;
        this.isOn = true;
    }


}
