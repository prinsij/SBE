package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class Health extends Component {
    public int getHp() {
        return hp;
    }

    private int hp = 0;

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
}
