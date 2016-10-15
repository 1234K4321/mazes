package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.BitSet;
import java.util.Optional;
import java.util.function.Predicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html">
 *      Hunt-And-Kill algorithm on Jamis Buck's blog</a>
 */
public class HuntAndKill extends MazeAlgorithm {

	protected final Predicate<Integer> isAlive = animal -> grid.get(animal) == UNVISITED;
	protected final Predicate<Integer> isDead = isAlive.negate();
	protected final BitSet targets;

	public HuntAndKill(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
		targets = new BitSet(grid.numCells());
	}

	@Override
	public void accept(Integer animal) {
		do {
			kill(animal);
			Optional<Integer> livingNeighbor = grid.neighbors(animal, Direction.valuesPermuted()).filter(isAlive).findAny();
			if (livingNeighbor.isPresent()) {
				grid.neighbors(animal).filter(isAlive).forEach(targets::set);
				grid.addEdge(animal, livingNeighbor.get());
				animal = livingNeighbor.get();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(animal, grid.neighbors(animal, Direction.valuesPermuted()).filter(isDead).findAny().get());
			}
		} while (!targets.isEmpty());
	}

	protected Integer hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(Integer animal) {
		grid.set(animal, COMPLETED);
		targets.clear(animal);
	}
}