package de.uos.inf.ko.tagung.solver.student;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import de.uos.inf.ko.tagung.solver.Utils;

import java.util.List;

/**
 * Aufgabe P2 (c)
 *
 * @author
 */
public class Tagung {
	public static Utils.TagungSolutions runModel() {
		// 1. create model
		Model model = new Model("Tagung");

		// 2. create variables
		IntVar xA = model.intVar("xA", 1,5);
		IntVar xE; //...

		IntVar yG; //...

		IntVar[] all = new IntVar[]
			{
				xA //,....
		  };
		IntVar[] allX = new IntVar[]
			{
				xA//,...
			};
		IntVar[] allY = new IntVar[]
			{
				//...
			};

		// 3. add constraints
		// Constraint (0)

		// Constraint (1)

		// Constraint (2)

		// Constraint (3)

		// Constraint (4)

		// Constraint (5)

		// Constraint (6)

		// Constraint (7)

		// 4. get solver and solve model
		Solver solver = model.getSolver();
		final List<Solution> allSolutions = solver.findAllSolutions();
		Utils.TagungSolutions result = new Utils.TagungSolutions(all, allSolutions);
		return result;
	}
}
