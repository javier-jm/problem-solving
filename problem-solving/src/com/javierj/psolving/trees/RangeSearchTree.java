package com.javierj.psolving.trees;

import java.util.HashSet;

/**
 * This class contains algorithms to find a range of nodes give a balanced
 * binary tree. The implementation follows the descriptions found in:
 * 
 * https://en.wikipedia.org/wiki/Range_tree
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
public class RangeSearchTree {
	/**
	 * Basic TreeNode implementation
	 *
	 */
	public class TreeNode {
		TreeNode left;
		TreeNode right;
		int data;
		
		public TreeNode(int data)
		{
			this.data = data;
		}
	}
	
	/**
	 * Range trees can be used to find the set of points that lay inside a given
	 * interval. To report the points that lie in the interval [x1, x2], we
	 * start by searching for x1 and x2.
	 * 
	 * The following method will use a regular balanced binary search tree for
	 * this purpose. More info in https://en.wikipedia.org/wiki/Range_tree
	 * 
	 * @param x1
	 *            Low (start) range position
	 * @param x2
	 *            High (end) range position
	 * @param root
	 *            A balanced binary search tree root
	 * @return
	 */
	public HashSet<Integer> getNodesInRange(int x1, int x2, TreeNode root) {
		if (x1 >= x2) {
			throw new IllegalArgumentException("Range error: x1 must be less than x2");
		}

		// At some vertex in the tree, the search paths to x1 and x2 will
		// diverge. Let vsplit be the last vertex that these two search paths
		// have in common.
		TreeNode vsplit = root;
		while (vsplit != null) {
			if (x1 < vsplit.data && x2 < vsplit.data) {
				vsplit = vsplit.left;
			} else if (x1 > vsplit.data && x2 > vsplit.data) {
				vsplit = vsplit.right;
			} else {
				break;
			}
		}

		// Report the value stored at vsplit if it is inside the query interval.
		HashSet<Integer> nodesInRange = new HashSet<>();
		if (vsplit == null) {
			return nodesInRange;
		} else if (inRange(vsplit.data, x1, x2)) {
			nodesInRange.add(vsplit.data);
		}

		// Continue searching for x1 in the range tree (vSplit to x1).
		getLeftNodes(x1, x2, nodesInRange, vsplit.left);
		// Continue searching for x2 in the range tree (vSplit to x2).
		getRightNodes(x1, x2, nodesInRange, vsplit.right);

		return nodesInRange;
	}

	/**
	 * Binary search for x1 within the provided tree
	 * 
	 * @param x1
	 * @param x2
	 * @param nodesInRange
	 * @param root
	 */
	private void getLeftNodes(int x1, int x2, HashSet<Integer> nodesInRange, TreeNode root) {
		if (root == null) {
			return;
		}

		// If v is a leaf, report the value stored at v if it is inside the
		// query interval.
		if (inRange(root.data, x1, x2)) {
			nodesInRange.add(root.data);
		}

		if (root.data >= x1) {
			// For every vertex v in the search path from vsplit to x1, if the
			// value stored at v is greater than x1, report every point in the
			// right-subtree of v.
			grabAll(root.right, nodesInRange);
			getLeftNodes(x1, x2, nodesInRange, root.left);
		} else {
			getLeftNodes(x1, x2, nodesInRange, root.right);
		}
	}

	/**
	 * Binary search for x2 within the provided tree
	 * 
	 * @param x1
	 * @param x2
	 * @param nodesInRange
	 * @param root
	 */
	private void getRightNodes(int x1, int x2, HashSet<Integer> nodesInRange, TreeNode root) {
		if (root == null) {
			return;
		}

		// Report the leaf of this path if it lies within the query interval.
		if (inRange(root.data, x1, x2)) {
			nodesInRange.add(root.data);
		}

		if (root.data <= x2) {
			// Report all of the points stored in the left-subtrees of the
			// vertices with values less than x2 along the search path from
			// vsplit to x2,
			grabAll(root.left, nodesInRange);
			getRightNodes(x1, x2, nodesInRange, root.right);
		} else {
			getRightNodes(x1, x2, nodesInRange, root.left);
		}
	}

	/**
	 * Determine if the current value is within the range represented by x1 and
	 * x2, this means: x1 <= value <= x2
	 * 
	 * @param value
	 * @param x1
	 * @param x2
	 * @return
	 */
	private boolean inRange(int value, int x1, int x2) {
		return x1 <= value && value <= x2;
	}

	/**
	 * DFS all nodes of this tree and add them to the current range
	 */
	private void grabAll(TreeNode root, HashSet<Integer> nodesInRange) {
		if (root == null) {
			return;
		}

		nodesInRange.add(root.data);
		grabAll(root.left, nodesInRange);
		grabAll(root.right, nodesInRange);
	}
}
