package com.bobocode.basics;

import java.util.Arrays;

public class Sorting {
    private static int[] tempArr;

    public static void main(String[] args) {
        //check 1
        int[] arr = {1, 3, 2, 9, 2};
        insertionSort(arr);
        checkEquals(arr, new int[] {1, 2, 2, 3, 9});
        int[] arr1 = {1, 3, 2, 9, 2};
        mergeSort(arr1);
        checkEquals(arr, new int[] {1, 2, 2, 3, 9});

        //check 2
        arr = new int[] {6, 5, 3, 1, 8, 7, 2, 4};
        insertionSort(arr);
        checkEquals(arr, new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        arr1 = new int[] {6, 5, 3, 1, 8, 7, 2, 4};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {1, 2, 3, 4, 5, 6, 7, 8});

        //check 3
        arr = new int[] {8};
        insertionSort(arr);
        checkEquals(arr, new int[] {8});
        arr1 = new int[] {8};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {8});

        //check 4
        arr = new int[] {};
        insertionSort(arr);
        checkEquals(arr, new int[] {});
        arr1 = new int[] {};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {});

        //check 5
        arr = new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1};
        insertionSort(arr);
        checkEquals(arr, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        arr1 = new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});

        //check 6
        arr = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        insertionSort(arr);
        checkEquals(arr, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8});
        arr1 = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8});

        //check 7
        arr = new int[] {998, 996, 1000};
        insertionSort(arr);
        checkEquals(arr, new int[] {996, 998, 1000});
        arr1 = new int[] {998, 996, 1000};
        mergeSort(arr1);
        checkEquals(arr1, new int[] {996, 998, 1000});


    }

    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int val = arr[i];
            int index = i - 1;
            while (index >= 0 && val < arr[index]) {
                arr[index + 1] = arr[index];
                arr[index] = val;
                index--;
            }
        }
    }

    public static void mergeSort(int[] arr) {
        tempArr = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int start, int end) {
        if (end - start < 1) return;
        int mid = (end - start) / 2 + start;
        mergeSort(arr, start, mid);
        mergeSort(arr, mid + 1, end);
        merge(arr, start, mid, end);
    }

    private static void merge(int[] arr, int start, int mid, int end) {
        int leftIndex = start;
        int rightIndex = mid + 1;
        int i = 0;
        while (leftIndex <= mid && rightIndex <= end) {
            if (arr[leftIndex] > arr[rightIndex]) {
                tempArr[i++] = arr[rightIndex++];
            } else {
                tempArr[i++] = arr[leftIndex++];
            }
        }
        while (rightIndex <= end) {
            tempArr[i++] = arr[rightIndex++];
        }
        while (leftIndex <= mid) {
            tempArr[i++] = arr[leftIndex++];
        }
        System.arraycopy(tempArr, 0, arr, start, i);
    }

    private static void checkEquals(int[] a, int[] a2) {
        if(!Arrays.equals(a, a2)) {
            throw new AssertionError(Arrays.toString(a) + " is not equal to: " + Arrays.toString(a2));
        }

    }
}
