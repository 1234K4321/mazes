package de.amr.demos.maze.swing;

import static de.amr.demos.maze.swing.QuickMazeDemoApp.launch;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.maze.alg.BinaryTreeRandom;

public class BinaryTreeRandomApp {

	public static void main(String[] args) {
		launch(BinaryTreeRandom.class, SimpleEdge::new);
	}
}