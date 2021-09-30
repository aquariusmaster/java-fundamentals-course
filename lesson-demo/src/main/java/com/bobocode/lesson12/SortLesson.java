package com.bobocode.lesson12;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SortLesson {
    public static void main(String[] args) {
        List<Integer> list = ThreadLocalRandom.current().ints(1000).boxed().collect(Collectors.toList());
        insertionSort(list);
    }

    private static void insertionSort(List<Integer> list) {
        Objects.requireNonNull(list);
        if (list.size() < 2) return;
        for (int i = 1; i < list.size(); i++) {
            int j = 0;
            while (j < i && true) {
                Integer temp = list.get(i);
                Integer tem = list.get(j);
                if (temp > tem);
            }

        }
    }
}
