package org.jainy.personal.sort;

import java.util.Arrays;

public class TestSort {

  public static void main(String[] args) {
    int[] input = {1,2,3,4,5,6,67,99,65,34,67,84,11,21,33,6,43,55,98,3324,1,4,7};
    System.out.println(Arrays.toString(input));

    int[] javaOutput = new int[input.length];
    System.arraycopy(input,0, javaOutput,0,input.length);
    Arrays.sort(javaOutput);
    System.out.println(Arrays.toString(javaOutput));

    Sort<Integer> sorter = new InsertionSort<>();

    Integer[] input1 = Arrays.stream(input).boxed().toArray(Integer[]:: new);
    Integer[] result1 = sorter.sort(input1);

    System.out.println(Arrays.toString(result1));


    String value = "stop";
    char[] valueArr = value.toCharArray();
    Arrays.sort(valueArr);
    System.out.println(String.valueOf(valueArr));
  }
}
