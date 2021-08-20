package com.bobocode.basics;

import java.util.Arrays;

public class InsertionSort {
    public static void main(String[] args) {
//        int[] arr = {6,5,12,10,9,1};
//        int[] arr = {5,6,12,1,9,10};
//        int[] arr = {12,11,10,9,8,7,6,7,3,12,11,8,7};
//        int[] arr = {0,0,0,0,0,0,0,0,0,0,0};
//        int[] arr = {6,5,12};
//        int[] arr = {6};
//        int[] arr = {};
//        int[] arr = {7,6};
        int[] arr = {5,2,4,6,1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int current = arr[i];
            int j = i;
            while (j - 1 >= 0 && current < arr[j - 1] ) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = current;
        }
    }
}
