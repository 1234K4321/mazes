package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.Color;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;

public class PearlsRendererTestApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new PearlsRendererTestApp());
	}

	static final int GRID_SIZE = 20;
	static final int CANVAS_SIZE = 800;
	static final int GRID_CELL_SIZE = CANVAS_SIZE / GRID_SIZE;

	public PearlsRendererTestApp() {
		super(CANVAS_SIZE, CANVAS_SIZE, GRID_CELL_SIZE, Top4.get(), SimpleEdge::new);
		setAppName("Pearls Renderer Test");
		setRenderingStyle(Style.PEARLS);
		renderer.fnGridBgColor = () -> Color.DARK_GRAY;
	}

	@Override
	public void run() {
		clear();
		canvasAnimation.setEnabled(false);
		grid.setDefaultContent(COMPLETED);
		grid.fill();
		canvas.drawGrid();

		sleep(2000);
		clear();
		canvasAnimation.setEnabled(true);
		canvasAnimation.fnDelay = () -> 1;
		new WilsonUSTRecursiveCrosses(grid).run(0);

		sleep(2000);
		canvasAnimation.fnDelay = () -> 10;
		new DepthFirstTraversalAnimation(grid).run(canvas, new DepthFirstTraversal2<>(grid), 0, grid.cell(BOTTOM_RIGHT));

		sleep(2000);
		canvas.clear();
		floodFill(canvas, grid, 0);
	}

	private void clear() {
		grid.removeEdges();
		grid.clearContent();
		grid.setDefaultContent(UNVISITED);
		canvas.clear();
	}
}