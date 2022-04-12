package com.kola.otdr.util;

/**
 * Tuple 元组
 * @param <A>
 * @param <B>
 */
public class Tuple<A, B> {

    public final A first;

    public final B second;

    public Tuple(A a, B b){
        first = a;
        second = b;
    }

    public String toString(){
        return "(" + first + ", " + second + ")";
    }

}