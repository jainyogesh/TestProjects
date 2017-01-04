package org.jainy.techgig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LIS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// int[] input1 = {10,22,9,33,21,50,41,60,80};

		int[] input1 = {1,2,3,2,34324,4,5,6,7,8,9 };

		int[] input2 = { 0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15 };
		System.out.println(longestSeq(input1));

	}

	public static int longestSeq(int[] input1) {
		List<Node> masterLeafList = new LinkedList<Node>();

		Set<Integer> checkDuplicateSet = new HashSet<Integer>();

		for (int value : input1) {

			if (checkDuplicateSet.contains(value)) {
				continue;
			} else {
				checkDuplicateSet.add(value);
			}

			Node[] parentArray = new Node[masterLeafList.size()];
			for (int i = 0; i < masterLeafList.size(); i++) {
				Node currLeaf = masterLeafList.get(i);
				if (value > currLeaf.value) {
					parentArray[i] = currLeaf;
					continue;
				} else {
					currLeaf = currLeaf.seed;
					while (currLeaf != null && parentArray[i] == null) {
						if (value > currLeaf.value) {
							parentArray[i] = currLeaf;
						}
						currLeaf = currLeaf.seed;
					}
				}
			}

			int highestLength = -1;
			int highestIndex = -1;
			for (int i = 0; i < parentArray.length; i++) {
				Node currLeaf = parentArray[i];
				if (currLeaf != null) {
					if (currLeaf.length > highestLength) {
						highestLength = currLeaf.length;
						highestIndex = i;
					}
				}
			}

			if (highestLength > -1) {
				Node newLeaf = new Node(value, parentArray[highestIndex]);
				masterLeafList.add(highestIndex, newLeaf);
			} else {
				Node newLeaf = new Node(value, null);
				masterLeafList.add(newLeaf);
			}

		}

		int maxLength = 0;

		for (Node leaf : masterLeafList) {
			if (leaf.length > maxLength) {
				maxLength = leaf.length;
			}
		}
		return maxLength + 1;
	}

	private static class Node {
		Node seed;
		int value;
		int length;
		List<Node> leafList = new ArrayList<Node>();

		Node(int value, Node seed) {
			this.value = value;
			if (seed == null) {
				this.length = 0;
			} else {
				this.seed = seed;
				seed.addLeaf(this);
				this.length = this.seed.length + 1;
			}
		}

		void addLeaf(Node leaf) {
			this.leafList.add(leaf);
		}
	}

}
