package de.amr.easy.grid.impl;

import java.util.HashSet;
import java.util.Set;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.event.EdgeAddedEvent;
import de.amr.easy.graph.event.EdgeChangeEvent;
import de.amr.easy.graph.event.EdgeRemovedEvent;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.graph.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * A grid which can be observed by graph listeners.
 * 
 * @author Armin Reichert
 */
public class ObservableGrid extends Grid implements ObservableGrid2D, ObservableGraph<Integer, WeightedEdge<Integer>> {

	private final Set<GraphListener<Integer, WeightedEdge<Integer>>> listeners = new HashSet<>();
	private boolean eventsEnabled;

	public ObservableGrid(int numCols, int numRows) {
		super(numCols, numRows);
		eventsEnabled = true;
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		super.addEdge(p, q);
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.edgeAdded(new EdgeAddedEvent<>(this, edge(p, q).get()));
			}
		}
	}

	@Override
	public void removeEdge(Integer p, Integer q) {
		edge(p, q).ifPresent(edge -> {
			super.removeEdge(p, q);
			if (eventsEnabled) {
				for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
					listener.edgeRemoved(new EdgeRemovedEvent<>(this, edge));
				}
			}
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.graphChanged(this);
			}
		}
	}

	/* {@link ObservableGraph} interface */

	@Override
	public void addGraphListener(GraphListener<Integer, WeightedEdge<Integer>> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphListener(GraphListener<Integer, WeightedEdge<Integer>> listener) {
		listeners.remove(listener);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		eventsEnabled = enabled;
	}

	@Override
	public void fireVertexChange(Integer vertex, Object oldValue, Object newValue) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.vertexChanged(new VertexChangeEvent<>(this, vertex, oldValue, newValue));
			}
		}
	}

	@Override
	public void fireEdgeChange(WeightedEdge<Integer> edge, Object oldValue, Object newValue) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.edgeChanged(new EdgeChangeEvent<>(this, edge, oldValue, newValue));
			}
		}
	}

	@Override
	public void fireGraphChange(ObservableGraph<Integer, WeightedEdge<Integer>> graph) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.graphChanged(graph);
			}
		}
	}
}
