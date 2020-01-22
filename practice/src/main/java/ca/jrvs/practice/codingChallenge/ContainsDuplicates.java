package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ticket: https://www.notion.so/Contains-Duplicate-09c77ece160e4209a087d991e7c114a2
 */
public class ContainsDuplicates {

  /**
   * Big-O: O(n)
   * Justification: Need to go through entire array in worst case, with each hashtable
   * operation being constant time
   */
  public boolean containsDuplicatesSet(int[] nums) {
    Set<Integer> set = new HashSet<>(nums.length);
    for (int x: nums) {
      if (set.contains(x)) return true;
      set.add(x);
    }
    return false;
  }

  /**
   * Big-O: O(n log n)
   * Justification: Sorting is usually O(nlogn) if using heapsort
   */
  public boolean containsDuplicatesSort(int[] nums){
    Arrays.sort(nums);
    for (int i = 0;i<nums.length-1;i++){
      if (nums[i]==nums[i+1]) return true;
    }
    return false;
  }

}