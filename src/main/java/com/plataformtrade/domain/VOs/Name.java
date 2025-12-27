package com.plataformtrade.domain.VOs;

@SuppressWarnings("ClassCanBeRecord")
public final class Name {
    private final String value;

    public Name(String value){
        if(!isValid(value)) throw new IllegalArgumentException("Invalid name");
        this.value = value;
    }

    private boolean isValid(String value) {
        return value != null && value.matches("[a-zA-Z]+");
    }

    public String getValue(){
        return this.value;
    }
}
