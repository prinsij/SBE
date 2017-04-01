package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public abstract class GasTransformer extends Component {
    private int failurePenalty = 0;
    public boolean transform(GasStorage gasStorage) {
        return true;
    }

    public int getFailurePenalty() {
        return failurePenalty;
    }
}
