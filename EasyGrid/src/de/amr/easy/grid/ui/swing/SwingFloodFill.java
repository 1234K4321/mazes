package de.amr.easy.grid.ui.swing;

import static java.lang.String.format;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.function.Function;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Executes a breadth-first traversal on a grid and draws a color map ("flood-fill") visualizing the
 * cell distances from the start cell.
 * 
 * @author Armin Reichert
 */
public class SwingFloodFill {

	private static GridRenderer createRenderer(GridRenderer base, BreadthFirstTraversal bfs, BitSet solution,
			boolean distancesVisible, int maxDistance) {

		Function<Integer, Color> cellBgColor = cell -> {
			if (maxDistance == -1) {
				return base.getCellBgColor(cell);
			} else if (maxDistance == 0) {
				return Color.getHSBColor(0.16f, 0.5f, 1f);
			} else {
				float hue = 0.16f + 0.7f * bfs.getDistance(cell) / maxDistance;
				return Color.getHSBColor(hue, 0.5f, 1f);
			}
		};

		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = base::getCellSize;
		renderer.fnPassageWidth = base::getPassageWidth;
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, renderer.getPassageWidth() / 2);
		renderer.fnTextFont = () -> font;
		renderer.fnText = cell -> distancesVisible ? format("%d", bfs.getDistance(cell)) : "";
		renderer.fnCellBgColor = cell -> cellBgColor.apply(cell);
		renderer.fnPassageColor = (u, v) -> cellBgColor.apply(u);
		return renderer;
	}

	public void run(ObservingGridCanvas canvas, BareGrid2D<?> grid, int source, boolean distancesVisible) {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid);
		bfs.addObserver(new GraphTraversalListener() {

			@Override
			public void edgeTouched(int source, int target) {
				canvas.drawGridPassage(grid.edge(source, target).get(), true);
			}

			@Override
			public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
				canvas.drawGridCell(vertex);
			}
		});

		canvas.stopListening();
		bfs.traverseGraph(source);
		canvas.startListening();

		GridRenderer renderer = createRenderer(canvas.getRenderer().get(), bfs, new BitSet(), distancesVisible,
				bfs.getMaxDistance());
		canvas.pushRenderer(renderer);
		bfs.traverseGraph(source);
		canvas.popRenderer();
	}
}