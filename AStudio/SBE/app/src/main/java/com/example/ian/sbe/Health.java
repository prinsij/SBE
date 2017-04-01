package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class Health extends Component {
    public int getHp() {
        return hp;
    }

    private final int hp;
    public Health(int hp) {
        this.hp = hp;
    }
}
