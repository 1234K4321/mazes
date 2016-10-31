package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.iterators.Sequence;

/**
 * A grid traversal where the left and right grid "walls" are "collapsing".
 * 
 * @author Armin Reichert
 *
 */
public class CollapsingWalls implements Sequence<Integer> {

	private final NakedGrid2D<?> grid;

	public CollapsingWalls(NakedGrid2D<?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {

			private Integer nextLeft, nextRight;
			private boolean left;
			private int visited;

			{
				left = true;
				nextLeft = grid.cell(0, 0);
				nextRight = grid.cell(grid.numCols() - 1, grid.numRows() - 1);
				visited = 0;

			}

			@Override
			public boolean hasNext() {
				return visited < grid.vertexCount();
			}

			@Override
			public Integer next() {
				if (left) {
					Integer cell = nextLeft;
					int x = grid.col(nextLeft), y = grid.row(nextLeft);
					if (y < grid.numRows() - 1) {
						nextLeft = grid.cell(x, y + 1);
					} else {
						nextLeft = grid.cell(x + 1, 0);
					}
					left = false;
					++visited;
					return cell;
				} else {
					Integer cell = nextRight;
					int x = grid.col(nextRight), y = grid.row(nextRight);
					if (y > 0) {
						nextRight = grid.cell(x, y - 1);
					} else {
						nextRight = grid.cell(x - 1, grid.numRows() - 1);
					}
					left = true;
					++visited;
					return cell;
				}
			}
		};
	}
}
