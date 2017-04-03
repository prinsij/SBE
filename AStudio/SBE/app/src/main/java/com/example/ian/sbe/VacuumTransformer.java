package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-03.
 */

public class VacuumTransformer extends GasTransformer {
    public void transform(GasStorage gasStorage) {
        gasStorage.clear();
    }
}
