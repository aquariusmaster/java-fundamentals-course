package com.bobocode;

import java.util.Arrays;

public class Sorting {
    public static void main(String[] args) {
        int[] arr = {4,2,6,8,4,56,11};
        mergeSort(arr);
        System.out.println("Arr end:" + Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr) {
        if (arr.length < 2) return;
        int n = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, n);
        int[] right = Arrays.copyOfRange(arr, n, arr.length);

        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0;
        while (i < left.length && j < right.length) {
            if (left[i] > right[j]) {
                arr[i+j] = right[j++];
            } else {
                arr[i+j] = left[i++];
            }
        }
        System.arraycopy(left, i, arr, i+j, left.length - i);
        System.arraycopy(right, j, arr, i+j, right.length - j);
    }

}
