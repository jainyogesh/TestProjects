package org.jainy.personal;

import java.io.*;

public class CandidateCode5 {

	static int totalPath = 0;

	public static int numberOfPath(int[] input1, int[] input2) {
		if (input1.length != 2) {
			return totalPath;
		}

		int rows = input1[0];
		int columns = input1[1];

		if (rows * columns != input2.length) {
			return totalPath;
		}

		Cell[][] grid = new Cell[rows][columns];
		for (int m = 0, idx = 0; m < rows; m++) {
			Cell[] row = grid[m];
			for (int n = 0; n < columns; n++) {
				row[n] = new Cell(input2[idx], m, n);
				idx++;
				processRule(row[n]);
			}
		}

		Coordinate start = new Coordinate(0, 0);
		Coordinate end = new Coordinate(rows - 1, columns - 1);
		findPaths(grid, start, end);

		return totalPath;
	}

	private static void findPaths(Cell[][] grid, Coordinate start,
			Coordinate end) {
		Cell startCell = grid[start.getRow()][start.getColumn()];
		Cell endCell = grid[end.getRow()][end.getColumn()];
		findPaths(grid, startCell, endCell);
	}

	private static void findPaths(Cell[][] grid, Cell currentCell, Cell endCell) {
		if (currentCell.equals(endCell)) {
			totalPath++;
			return;
		}

		int curRow = currentCell.getCoordinate().getRow();
		int curCol = currentCell.getCoordinate().getColumn();
		List<Movement> movements = currentCell.getMovements();
		for (int i = 0; i < movements.size(); i++) {
			Movement m = movements.get(i);
			switch (m) {
			case No_Movement:
				break;
			case Right:
				if (curCol + 1 < grid[0].length) {
					currentCell = grid[curRow][curCol + 1];
					findPaths(grid, currentCell, endCell);
				}
				break;
			case Lower:
				if (curRow + 1 < grid.length) {
					currentCell = grid[curRow + 1][curCol];
					findPaths(grid, currentCell, endCell);
				}
				break;
			case Diagnol:
				if (curCol + 1 < grid[0].length && curRow + 1 < grid.length) {
					currentCell = grid[curRow + 1][curCol + 1];
					findPaths(grid, currentCell, endCell);
				}
				break;
			}
		}
	}

	private static void processRule(Cell cell) {
		int cellNumber = cell.getCellNumber();
		switch (cellNumber) {
		case 0:
			cell.addMovement(Movement.No_Movement);
			break;
		case 1:
			cell.addMovement(Movement.Right);
			break;
		case 2:
			cell.addMovement(Movement.Lower);
			break;
		case 3:
			cell.addMovement(Movement.Diagnol);
			break;
		case 4:
			cell.addMovement(Movement.Right);
			cell.addMovement(Movement.Lower);
			break;
		case 5:
			cell.addMovement(Movement.Right);
			cell.addMovement(Movement.Diagnol);
			break;
		case 6:
			cell.addMovement(Movement.Lower);
			cell.addMovement(Movement.Diagnol);
			break;
		case 7:
			cell.addMovement(Movement.Right);
			cell.addMovement(Movement.Lower);
			cell.addMovement(Movement.Diagnol);
			break;
		}
	}

	private static class Cell {
		int cellNumber;
		List<Movement> movements;
		Coordinate c = null;

		Cell(int cellNumber, int row, int column) {
			this.cellNumber = cellNumber;
			this.movements = new List<Movement>(3);
			this.c = new Coordinate(row, column);
		}

		int getCellNumber() {
			return this.cellNumber;
		}

		Coordinate getCoordinate() {
			return this.c;
		}

		void addMovement(Movement m) {
			movements.add(m);
		}

		List<Movement> getMovements() {
			return this.movements;
		}

		public String toString() {
			return "[" + this.cellNumber + this.c + ":" + this.movements + "]";
		}

		public boolean equals(Object o) {
			if (!(o instanceof Cell))
				return false;
			Cell temp = (Cell) o;
			if (temp.getCoordinate().getRow() == this.c.getRow()
					&& temp.getCoordinate().getColumn() == this.c.getColumn())
				return true;
			return false;
		}
	}

	private static enum Movement {
		Right, Lower, Diagnol, No_Movement;
	}

	private static class Coordinate {
		int row;
		int column;

		Coordinate(int row, int column) {
			this.row = row;
			this.column = column;
		}

		int getRow() {
			return this.row;
		}

		int getColumn() {
			return this.column;
		}

		public String toString() {
			return "(" + this.row + "," + this.column + ")";
		}
	}

	private static class List<E> {
		Object[] buckets = null;
		int count = 0;

		private List() {
			buckets = new Object[100];
		}

		private List(int size) {
			buckets = new Object[size];
		}

		private List(E[] arr) {
			buckets = arr;
			count = arr.length;
		}

		private void add(E obj) {
			if (count + 1 > buckets.length) {
				Object[] copy = new Object[buckets.length * 2];
				System.arraycopy(buckets, 0, copy, 0, buckets.length);
				buckets = copy;
			}
			buckets[count] = obj;
			count++;
		}

		private E get(int index) {
			return (E) buckets[index];
		}

		private int size() {
			return count;
		}

		public String toString() {
			StringBuilder res = new StringBuilder();
			for (int i = 0; i < count - 1; i++) {
				res.append(buckets[i]).append(',');
			}
			res.append(buckets[count - 1]);
			return res.toString();
		}
	}

}