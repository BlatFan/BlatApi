package ru.blatfan.blatapi.utils;

public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}