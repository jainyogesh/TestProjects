package org.jainy.personal.sort;

public class InsertionSort<T extends Comparable> implements Sort<T> {

  public T[] sort(T[] input) {
    for(int i=1; i < input.length; i++) {
      T currentValue = input[i];
      int j = i-1;
      while (j >=0 && currentValue.compareTo(input[j]) == -1) {
        input[j+1] = input[j];
        j--;
      }
      input[j+1] = currentValue;
    }
    return  input;
  }
}
