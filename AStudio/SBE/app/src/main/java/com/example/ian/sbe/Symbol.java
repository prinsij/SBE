package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class Symbol extends Component {
    private int layer = 0;
    public char getSymbol() {
        return symbol;
    }

    public Symbol(char sym) {
        this.symbol = sym;
    }
    public Symbol(char sym, int layer) {
        this.symbol = sym;
        this.layer = layer;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    private char symbol;


    public int getLayer() {
        return layer;
    }
}
