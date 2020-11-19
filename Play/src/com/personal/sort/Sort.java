package com.personal.sort;

public interface Sort<T extends Comparable> {

  public T[] sort(T[] input);
}
