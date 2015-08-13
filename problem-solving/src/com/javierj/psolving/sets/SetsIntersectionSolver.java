package com.javierj.psolving.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains useful methods to find the intersection of multiple sets.
 * An Example of the problem being solved here can be found in
 * http://www.geeksforgeeks.org/intersection-of-n-sets/
 * 
 * Copyright (C) 2015 Javier Jimenez Martinez
 * 
 * This class is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */
public class SetsIntersectionSolver {

	/**
	 * GeeksForGeeks problem description:
	 * -------------------------------------------------------------------------
	 * Intersection of n sets. Given n sets of integers of different sizes. Each
	 * set may contain duplicates also. How to find the intersection of all the
	 * sets. If an element is present multiple times in all the sets, it should
	 * be added that many times in the result.
	 * 
	 * For example, consider there are three sets {1,2,2,3,4} {2,2,3,5,6}
	 * {1,3,2,2,6}. The intersection of the given sets should be {2,2,3}
	 * -------------------------------------------------------------------------
	 * 
	 * This method will try to solve this problem in O(m*n) time complexity with
	 * O(m) extra space. We simply create a runner map of any set and use it as
	 * a base to compare all other sets while "decreasing" this runner map on
	 * each comparison. In the end we will only have the intersection of ALL
	 * sets.
	 * 
	 * @param sets
	 *            The sets
	 * @return A list containing the intersection of all sets
	 */
	public static List<Integer> getIntersection(int[]... sets) {

		// Get the frequency of the first set in O(m) where m = length of the
		// biggest set. We will use this frequency to find intersections in all
		// the other sets
		HashMap<Integer, Integer> freqSet = new HashMap<>();
		for (int a : sets[0]) {
			int qty = 1;
			if (freqSet.containsKey(a)) {
				qty += freqSet.get(a);
			}
			freqSet.put(a, qty);
		}

		// Create a copy of the original set so we can modify it without losing
		// the original set values. This two sets will be used to count how many
		// items are intersecting on each set pass (counting found items
		// comparing the freqSet vs runnerFreqSet frequencies).
		HashMap<Integer, Integer> runnerFreqSet = new HashMap<>(freqSet);

		// For each remaining sets deduct frequency from the runner set to
		// continuously remove intersections. This will take be O(n * m)
		for (int[] set : sets) { // O(n)
			if (set == sets[0]) {
				// Do not process the first set since that's our original
				// freqSet
				continue;
			}

			// If the current set in the loop has a match with the runner set
			// make sure we deduct it for future analysis. This and the next for
			// loops will be the O(m) part of the overall O(m*n) complexity
			for (int a : set) {
				if (runnerFreqSet.containsKey(a)) {
					int qty = runnerFreqSet.get(a);
					if (qty > 0) {
						// The set in the loop shares an integer with the runner
						// set, deduct it from the runner set
						runnerFreqSet.put(a, qty - 1);
					}
				}
			}

			// Now analyze the runner frequency set and determine which integers
			// intersected and by how much (using the original freqSet). This
			// will take O(m)
			for (int a : freqSet.keySet()) {
				// First check if we had an intersection of "a" in the loop set
				// and the current set)
				if (runnerFreqSet.get(a) < freqSet.get(a)) {
					int qtyFound = 0;
					if (runnerFreqSet.get(a) < 0) {
						// If we found more "a"s in the loop set than in the
						// runner set just "count" the "a"s from the runner set
						qtyFound = freqSet.get(a);
					} else {
						// We found the same number of "a"s (or less) in the
						// loop set and
						// in the runner set. Count how many exactly
						qtyFound = freqSet.get(a) - runnerFreqSet.get(a);
					}

					// Update the qty found in the runner set since this is the
					// new qty that we will expect from other sets
					runnerFreqSet.put(a, qtyFound);
				} else {
					// We didn't find an "a" in this loop set so remove it from
					// the runner
					// set (since it's not an intersection in all either..)
					runnerFreqSet.remove(a);
				}
			}
			// Setup the original frequency set with the new runner set for the
			// next loop set comparison
			freqSet = new HashMap<>(runnerFreqSet);
		}

		// Convert the resulting frequency map to a list. This will take O(m) at
		// most..
		return explodeToArray(runnerFreqSet);
	}

	private static List<Integer> explodeToArray(HashMap<Integer, Integer> intersectionSet) {
		ArrayList<Integer> intersectionList = new ArrayList<>();
		// Take the frequency of each intersected int and explode it by that
		// frequency. This will take O(m) at most since we have non duplicated
		// map keys with its frequency
		for (int a : intersectionSet.keySet()) {
			int qtyFound = intersectionSet.get(a);
			for (int i = 0; i < qtyFound; i++) {
				intersectionList.add(a);
			}
		}
		return intersectionList;
	}

	public static void main(String[] args) {
		int[] set1 = new int[] { 1, 2, 2, 2, 2, 3, 2, 4 };
		int[] set2 = new int[] { 2, 2, 2, 3, 5, 6 };
		int[] set3 = new int[] { 1, 3, 2, 2, 2, 6, 1, 1, 1, 1 };

		System.out.println(getIntersection(set1, set2, set3));
	}
}
