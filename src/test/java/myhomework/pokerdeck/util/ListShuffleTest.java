package myhomework.pokerdeck.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Unit test class for the List shuffling utility
 * 
 * @author Bruno Martinez
 */
public class ListShuffleTest {

	
	@Test
	public void testShuffleKeepSize() {
		
		Integer [] initialList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};		
		List<Integer> list1 = Arrays.asList(initialList.clone());
		List<Integer> list2 = Arrays.asList(initialList.clone());
		
		ListShuffle shuffle = new ListShuffle();
		shuffle.shuffle(list1);
		
		//System.out.println(list1);
		
		assertEquals(list1.size(), list2.size());
	}
	
	@Test
	public void testPredictibleShuffleWithSameSeed() {

		Integer [] initialList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};		
		List<Integer> list1 = Arrays.asList(initialList.clone());
		List<Integer> list2 = Arrays.asList(initialList.clone());
		final long seed = System.nanoTime();
		
		ListShuffle shuffle1 = new ListShuffle(seed);
		shuffle1.shuffle(list1);
		
		ListShuffle shuffle2 = new ListShuffle(seed);
		shuffle2.shuffle(list2);
		
		assertEquals(list1, list2);
	}
	
	@Test
	public void testShuffleOutput() {
		
		Integer [] initialList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};		
		List<Integer> referenceList = Arrays.asList(initialList.clone());
		List<Integer> list = Arrays.asList(initialList.clone());
		
		ListShuffle shuffle = new ListShuffle(100);
		shuffle.shuffle(list);
		
		// Output must be different as we used different shuffle
		assertNotEquals(referenceList, list);
		
		// We should still have same elements in the List
		Collections.sort(referenceList);
		Collections.sort(list);
		assertEquals(referenceList, list);		
	}

}
