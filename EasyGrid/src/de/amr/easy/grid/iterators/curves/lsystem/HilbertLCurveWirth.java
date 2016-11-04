package de.amr.easy.grid.iterators.curves.lsystem;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import de.amr.easy.grid.iterators.curves.Curve;

/**
 * Implementation of a Hilbert curve using the follwing L-system (adapted from the book "Algorithmen
 * und Datenstrukturen" by Niklaus Wirth, Teubner 1983):
 * <p>
 * <code>
 * A -> D w A s A e B <br/>
 * B -> C n B e B s A <br/>
 * C -> B e C n C w D <br/>
 * D -> A s D w D n C
 * </code>
 * </p>
 * The non-terminals <code>n,e,s,w</code> are interpreted as walking towards the corresponding
 * direction. Axiom: <code>A</code>.
 * <p>
 * As given, the curve starts at the upper right corner and ends at the lower right corner of the
 * grid.
 * 
 * @author Armin Reichert
 */
public class HilbertLCurveWirth extends Curve {

	public HilbertLCurveWirth(int i) {
		A(i);
	}

	/**
	 * <code> A -> D w A s A e B </code>
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void A(int i) {
		if (i > 0) {
			D(i - 1);
			walk(W);
			A(i - 1);
			walk(S);
			A(i - 1);
			walk(E);
			B(i - 1);
		}
	}

	/**
	 * <code> B -> C n B e B s A</code>
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void B(int i) {
		if (i > 0) {
			C(i - 1);
			walk(N);
			B(i - 1);
			walk(E);
			B(i - 1);
			walk(S);
			A(i - 1);
		}
	}

	/**
	 * <code> C -> B e C n C w D</code>
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void C(int i) {
		if (i > 0) {
			B(i - 1);
			walk(E);
			C(i - 1);
			walk(N);
			C(i - 1);
			walk(W);
			D(i - 1);
		}
	}

	/**
	 * <code> D -> A s D w D n C</code>
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void D(int i) {
		if (i > 0) {
			A(i - 1);
			walk(S);
			D(i - 1);
			walk(W);
			D(i - 1);
			walk(N);
			C(i - 1);
		}
	}
}