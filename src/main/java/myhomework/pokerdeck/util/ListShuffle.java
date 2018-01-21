package myhomework.pokerdeck.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class to shuffle a List based on random generator.
 * 
 * This implementation uses java.util.Random as random generator and implements the
 * See: <a href="http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm">Fisher And Yates algorithm</a>
 *
 * @author Bruno Martinez
 */
public class ListShuffle {

	private final Random randomGenerator;

	/**
	 * Creates a new instance initializing the internal random generator with the specified seed number.
	 * 
	 * Two instances built with the same seed value will produce the same shuffling result when calling successfully 
	 * the {@link shuffle} method with the same sequence.
	 * @param randomSeed long number to initialize the internal java.util.Random
	 */
	public ListShuffle(long randomSeed) {
		this.randomGenerator = new Random(randomSeed);
	}

	/**
	 * Creates a new instance with automatic initialization of the internal random generator.
	 */
	public ListShuffle() {
		this.randomGenerator = new Random();
	}

	/**
	 * Shuffles in place the specified list.
	 * @param list the list to shuffle.
	 */
	public <E> void shuffle(List<E> list) {
		/*
		 *  https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
		 *  -- To shuffle an array a of n elements (indices 0..n-1):
		 *  for i from n-1 downto 1 do
     	 *      j <- random integer such that 0 <= j <= i
     	 *      exchange a[j] and a[i]
		 */
		int size = list.size();
		for(int i = size - 1; i >= 1; i--) {
			int j = randomGenerator.nextInt(i + 1);
			Collections.swap(list, i, j);
		}
	}


}
