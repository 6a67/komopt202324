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
		IntVar xB = model.intVar("xB", 1,5);
		IntVar xC = model.intVar("xC", 1,5);
		IntVar xD = model.intVar("xD", 1,5);
		IntVar xE = model.intVar("xE", 1,5);

		IntVar xZ = model.intVar("xZ", 1,5);
		IntVar xY = model.intVar("xY", 1,5);
		IntVar xX = model.intVar("xX", 1,5);
		IntVar xW = model.intVar("xW", 1,5);
		IntVar xV = model.intVar("xV", 1,5);

		IntVar[] all = new IntVar[]
			{
				xA,
				xB,
				xC,
				xD,
				xE,
				xZ,
				xY,
				xX,
				xW,
				xV
		  };
		IntVar[] allX = new IntVar[]
			{
				xA,
				xB,
				xC,
				xD,
				xE
			};
		IntVar[] allY = new IntVar[]
			{
				xZ,
				xY,
				xX,
				xW,
				xV
			};

		// 3. add constraints
		model.arithm(xZ, "!=", 2).post();
		model.arithm(xZ, "!=", 4).post();

		model.arithm(xA.sub(xB).abs().intVar(), ">=", 2).post();

		model.arithm(xC, "=", xA.add(1).intVar()).post();

		model.arithm(xB, "=", xY.add(1).intVar()).post();

		model.arithm(xB, "<", xX).post();
		model.arithm(xC, "<", xX).post();

		model.arithm(xW, "<", xV).post();

		model.arithm(xC, ">", xD).post();

		model.allDifferent(allX).post();
		model.allDifferent(allY).post();
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
