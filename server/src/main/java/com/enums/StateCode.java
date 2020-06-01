package com.enums;

public enum StateCode {
    NO_TOKEN(4001),TOKEN_ERR(4002),TOKEN_SUCCESS(200);
    private int value;

    StateCode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
