package com.bobocode.basics;

import java.util.Arrays;

public class Merge {

    public static void main(String[] args) {
//        int[] arr = {6,5,12,10,9,1};
//        int[] arr = {5,6,12,1,9,10};
//        int[] arr = {12,11,10,9,8,7,6,7,3,12,11,8,7};
//        int[] arr = {0,0,0,0,0,0,0,0,0,0,0};
//        int[] arr = {6,5,12};
//        int[] arr = {6};
//        int[] arr = {};
//        int[] arr = {7,6};
        int[] arr = {1, 5, 6, 9, 10, 12};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
        //Merge Sort consumes time of O(N*log(n)), less than the Insertion Sort
        //Merge Sort consumes space of O(N), more than the Insertion Sort
        //The same number of operations will be executed no matter if the array is partially sorted or not.
    }

    public static void mergeSort(int[] arr, int start, int end) {
        int length = end - start + 1;
        if (length <= 1) {
            return;
        }
        if (length == 2) {
            if (arr[start] > arr[end]) {
                int temp = arr[start];
                arr[start] = arr[end];
                arr[end] = temp;
            }
            return;
        }
        int middle = length / 2 + start;
        mergeSort(arr, start, middle - 1);
        mergeSort(arr, middle, end);
        merge(arr, start, middle - 1, middle, end);
    }

    private static void merge(int[] arr, int leftStart, int leftEnd, int rightStart, int rightEnd) {
        int leftIndex = leftStart, rightIndex = rightStart;
        int length = rightEnd - leftStart + 1;
        int[] tempArr = new int[length];
        for (int i = 0; i < length; i++) {
            if (leftIndex > leftEnd && rightIndex <= rightEnd ) {
                tempArr[i] = arr[rightIndex++];
                continue;
            }
            if (rightIndex > rightEnd && leftIndex <= leftEnd ) {
                tempArr[i] = arr[leftIndex++];
                continue;
            }
            if (arr[leftIndex] < arr[rightIndex]) {
                tempArr[i] = arr[leftIndex++];
            } else {
                tempArr[i] = arr[rightIndex++];
            }
        }
        System.arraycopy(tempArr, 0, arr, leftStart, length);
    }
}