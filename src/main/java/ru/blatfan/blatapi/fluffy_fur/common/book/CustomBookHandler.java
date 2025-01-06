package ru.blatfan.blatapi.fluffy_fur.common.book;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CustomBookHandler {
    @Getter
    public static List<CustomBook> books = new ArrayList<>();

    public static void register(CustomBook book) {
        books.add(book);
    }
}
