package com.javierj.psolving.math;

/**
 * This class contains useful methods regarding the Pascal's triangle
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
public class PascalTriangle {
	/**
	 * This method will print the first # levels of the Pascal's triangle. It
	 * uses the method described in:
	 * 
	 * https://en.wikipedia.org/wiki/Pascal%27s_triangle#Calculating_a_row_or_diagonal_by_itself
	 * 
	 * It basically computes the Combinations of the current row and col
	 * multiplied by the previous one (which will always be 1 at the beginning
	 * of each pascal triangle row). It will print each tree element to the output
	 * stream, aligning the numbers with spaces to form a perfect triangle.
	 * 
	 * @param num
	 *            # of levels to print
	 */
	public static void printPascalTriangle(int num) {
		// Create a pad (# of spaces) to display between numbers to keep things
		// in order. This should be bigger than the # of digits of the highest
		// expected number and it should be an odd number (to have the same
		// number of spaces to the left and to the right between numbers)
		int pad = 7;

		// Calculate the # of spaces to the left of each number plus itself
		// (this is the width of the steps of the triangle)
		int stepsWidth = pad / 2 + 1;

		// Now calculate the maximum # of spaces from the left side of the
		// screen to the first triangle's level (we will have num-1 steps in the
		// triangle)
		int spaces = (num - 1) * stepsWidth;

		for (int n = 0; n < num; n++) {
			// Print the left spaces of the current level, deduct the size of a
			// number in each row
			if (spaces > 0) {
				System.out.printf("%" + spaces + "s", "");
				spaces -= stepsWidth;
			}
			// This will represent the previous combination C(n k-1)
			int prevCombination = 1;
			for (int k = 1; k <= n + 1; k++) {
				System.out.print(prevCombination);

				// Calculate how many digits this number has and deduct that to
				// the pad between numbers to keep everything aligned
				int digits = (int) Math.log10(prevCombination);
				if (digits < pad) {
					System.out.printf("%" + (pad - digits) + "s", "");
				}

				// Formula from Wikipedia (we can remove that "+1" if we start
				// the row loop at n=1)
				prevCombination = prevCombination * (n + 1 - k) / k;

			}
			// Row separator
			System.out.println();
		}
	}
}
