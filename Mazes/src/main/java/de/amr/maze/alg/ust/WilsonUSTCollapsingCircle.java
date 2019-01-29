package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static java.lang.Math.max;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.shapes.Circle;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingCircle extends WilsonUST {

	public WilsonUSTCollapsingCircle(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		int center = grid.cell(CENTER);
		grid.set(center, COMPLETED);
		for (int r = max(grid.numRows(), grid.numCols()) - 1; r > 0; r--) {
			new Circle(grid, center, r).forEach(this::loopErasedRandomWalk);
		}
		return grid;
	}
}