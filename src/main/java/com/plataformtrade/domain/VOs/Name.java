package com.plataformtrade.domain.VOs;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
