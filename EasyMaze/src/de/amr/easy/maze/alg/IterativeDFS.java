package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Deque;
import java.util.LinkedList;
import java.util.OptionalInt;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Generates a maze by iterative random depth-first-traversal of a grid.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS extends MazeAlgorithm {

	private final Deque<Integer> stack = new LinkedList<>();
	private OptionalInt unvisitedNeighbor;

	public IterativeDFS(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(Integer cell) {
		stack.push(cell);
		grid.set(cell, VISITED);
		while (!stack.isEmpty()) {
			unvisitedNeighbor = grid.neighborsPermuted(cell).filter(this::isCellUnvisited).findAny();
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				if (grid.randomNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				cell = neighbor;
			} else {
				grid.set(cell, COMPLETED);
				if (!stack.isEmpty()) {
					cell = stack.pop();
				}
				if (grid.get(cell) != COMPLETED) {
					stack.push(cell);
				}
			}
		}
	}
}