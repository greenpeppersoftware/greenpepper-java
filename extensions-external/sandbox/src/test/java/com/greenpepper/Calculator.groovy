package com.greenpepper

class Calculator
{
    int x;

    public total() {
        return x;
    }

    public add( int value ) {
        x+= value;
    }

    public multiply( int value ) {
        x *= value;
    }

    public String toString() {
        return "Groovy Fixture!"
    }
}