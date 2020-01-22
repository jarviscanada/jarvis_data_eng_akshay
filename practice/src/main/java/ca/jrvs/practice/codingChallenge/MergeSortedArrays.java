package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;

class MergeSortedArrays {
  public static int[] merge(int[] nums1, int[] nums2) {
    int length=nums1.length+nums2.length;
    int[] result= new int[length];
    int counter1=0,counter2=0;
    for(int i=0;i<length;i++){
      if(counter2>=nums2.length){
        result[i]=nums1[counter1++];
      }
      else if(counter1>=nums1.length){
        result[i]=nums2[counter2++];
      }
      else if(nums1[counter1]<nums2[counter2]){
        result[i]=nums1[counter1++];
      }
      else {
        result[i]=nums2[counter2++];
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int[] array1={1,2,3};
    int[] array2={1,3,4,6};
    int[] array3=merge(array1,array2);
    System.out.println(Arrays.toString(array3));
  }
}

//for(int i=0;i<result.length;i++){
//    nums1[i]=result[i];
//    }