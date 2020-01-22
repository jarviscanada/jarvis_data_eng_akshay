package ca.jrvs.practice.dataStructure.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayAPIs {

  public static void main(String[] args) {
    //create string of ints
        int[] intArray = new int[10];
        intArray[0]=100;
        intArray[1]=200;
        intArray[2]=300;

        //shortcut syntax for creating an array

    int[] inLineArray ={100,200,300};

    //2d array
    String[][] names={{"a","b","c"},{"q","r"}};
    char[] source={'a','r'};
    char[] dest=new char[2];

    System.arraycopy(source,1,dest,0,1);
    System.out.println(new String(dest));

    //convert an array to a List
    List<String> fruits = Arrays.asList("ze","orange");
    fruits=Arrays.asList(new String[]{"Aplle","oerage"});

    // copy
    String[] fruitArray = new String[]{"mango","banana"};
    String[] anotherFruitArray=Arrays.copyOfRange(fruitArray,0,2);
    System.out.printf(Arrays.toString(anotherFruitArray));

    //sort
    Arrays.sort(fruitArray);
    System.out.println(Arrays.toString(fruitArray));

    //binary search
    int exact=Arrays.binarySearch(fruitArray,"banana");
    System.out.println(exact);

  }

}
