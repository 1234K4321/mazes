package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.Eller;

public class EllerApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new EllerApp());
	}

	public EllerApp() {
		super(128);
		setAppName("Eller's Algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new Eller(grid).run(null);
			new SwingBFSAnimation(canvas, grid).runFrom(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}