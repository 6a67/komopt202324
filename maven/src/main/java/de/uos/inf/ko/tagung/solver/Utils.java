package de.uos.inf.ko.tagung.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

/**
 * Helper methods for CSP Solving.
 *
 * @author Sven Boge
 */
public class Utils {

	/**
	 * Builds the constraint |x1 - x2| > c.
	 *
	 * @param model the CSP model class
	 * @param x1 first varaible of CSP
	 * @param x2 second varaible of CSP
	 * @return constraint |x1 - x2| > c
	 */
	public static Constraint abs(Model model, IntVar x1, IntVar x2, int c) {
		// |x1 - x2| > 0, d.h. x1 - x2 > c
		final Constraint absPos = model.arithm(x1, "-", x2, ">", c);
		final Constraint x1First = model.arithm(x2, "<", x1);
		final Constraint and1 = model.and(absPos, x1First);
		// |x1 - x2| < 0, d.h. -(x1 - x2) > c oder x2 - x1 > c
		final Constraint absNeg = model.arithm(x2, "-", x1, ">", c);
		final Constraint x2First = model.arithm(x1, "<", x2);
		final Constraint and2 = model.and(absNeg, x2First);
		return model.or(and1, and2);
	}

	/**
	 * Container class that transports CSP solution and variables.
	 */
	public static class TagungSolutions {
		public IntVar[] variables;
		public List<Solution> solutions;

		public TagungSolutions(IntVar[] variables, List<Solution> solutions) {
			this.variables = variables;
			this.solutions = solutions;
		}
	}
}
