package de.uos.inf.ko.skyscrapers.solver.student;

import de.uos.inf.ko.skyscrapers.Instance;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;

import java.util.List;

/**
 * A skyscrapers solver.
 *
 * @author
 */
public class CSPSolver {
    public static Instance solve(Instance instance) {
        // 1. create model
        Model model = new Model("Skyscraper Instance");

        // 2. create variables
        int n = instance.getGamefieldSize();
        IntVar x[][] = model.intVarMatrix("x", n, n, 1, n);

        BoolVar N[][] = model.boolVarMatrix("VisN", n, n);
        BoolVar O[][] = model.boolVarMatrix("VisO", n, n);
        BoolVar S[][] = model.boolVarMatrix("VisS", n, n);
        BoolVar W[][] = model.boolVarMatrix("VisW", n, n);

        // 3. add constraints
        // make sure skyscrapers in each row are of different height
        IntVar[] horizontal = new IntVar[n];
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                horizontal[column] = x[row][column];
            }
            model.allDifferent(horizontal).post();
        }

        // make sure skyscrapers in each column are of different height
        IntVar[] vertical = new IntVar[n];
        for (int column = 0; column < n; column++) {
            for (int row = 0; row < n; row++) {
                vertical[row] = x[row][column];
            }
            model.allDifferent(vertical).post();
        }

        // make sure preset values are set
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                int presetValue = instance.getGamefield()[row][column];
                if (presetValue != 0) {
                    RealVar preset = model.realVar(presetValue);
                    model.eq(preset, x[row][column]).post();
                }
            }
        }


        // N
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) {
                    // if it is the first skyscraper, it is always visible
                    model.addClauseTrue(N[i][j]);
                } else {
                    // check if the current field's value is bigger than the maximum of the field's values above
                    IntVar[] previous = new IntVar[i];
                    for (int k = 0; k < i; k++) {
                        previous[k] = x[k][j];
                    }
                    model.reifyXgtY(x[i][j], model.max("NPrevious_Column_[" + i + "][" +  j + "]", previous), N[i][j]);
                }
            }
        }

        // S
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1) {
                    // if it is the first skyscraper, it is always visible
                    model.addClauseTrue(S[i][j]);
                } else {
                    // check if the current field's value is bigger than the maximum of the field's values below
                    IntVar[] previous = new IntVar[n - i - 1];
                    for (int k = i + 1; k < n; k++) {
                        previous[k - i - 1] = x[k][j];
                    }
                    model.reifyXgtY(x[i][j], model.max("SPrevious_Column_[" + i + "][" +  j + "]", previous), S[i][j]);
                }
            }
        }

        // W
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    // if it is the first skyscraper, it is always visible
                    model.addClauseTrue(W[i][j]);
                } else {
                    // check if the current field's value is bigger than the maximum of the field's values to the left
                    IntVar[] previous = new IntVar[j];
                    for (int k = 0; k < j; k++) {
                        previous[k] = x[i][k];
                    }
                    model.reifyXgtY(x[i][j], model.max("WPrevious_Column_[" + i + "][" +  j + "]", previous), W[i][j]);
                }
            }
        }

        // O
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == n - 1) {
                    // if it is the first skyscraper, it is always visible
                    model.addClauseTrue(O[i][j]);
                } else {
                    // check if the current field's value is bigger than the maximum of the field's values to the right
                    IntVar[] previous = new IntVar[n - j - 1];
                    for (int k = j + 1; k < n; k++) {
                        previous[k - j - 1] = x[i][k];
                    }
                    model.reifyXgtY(x[i][j], model.max("OPrevious_Column_[" + i + "][" +  j + "]", previous), O[i][j]);
                }
            }
        }

        // check if North values match
        for (int j = 0; j < n; j++) {
            int presetN = instance.getNorth()[j];

            // skip if no preset is assigned
            if (presetN == 0) {
                continue;
            }

            // calculate sum for column j
            int sum = 0;
            IntVar[] column = new IntVar[n];
            for (int i = 0; i < n; i++) {
                column[i] = N[i][j];
            }
            model.eq(model.realVar(presetN), model.sum("NSum_", column)).post();
        }

        // check if south values match
        for (int j = 0; j < n; j++) {
            int presetS = instance.getSouth()[j];

            // skip if no preset is assigned
            if (presetS == 0) {
                continue;
            }

            // calculate sum for column j
            int sum = 0;
            IntVar[] column = new IntVar[n];
            for (int i = 0; i < n; i++) {
                column[i] = S[i][j];
            }
            model.eq(model.realVar(presetS), model.sum("SSum_", column)).post();
        }

        // check if west values match
        for (int i = 0; i < n; i++) {
            int presetW = instance.getWest()[i];

            // skip if no preset is assigned
            if (presetW == 0) {
                continue;
            }

            // calculate sum for row i
            IntVar[] row = new IntVar[n];
            for (int j = 0; j < n; j++) {
                row[j] = W[i][j];
            }
            model.eq(model.realVar(presetW), model.sum("WSum_", row)).post();
        }

        // check if east values match
        for (int i = 0; i < n; i++) {
            int presetE = instance.getEast()[i];

            // skip if no preset is assigned
            if (presetE == 0) {
                continue;
            }

            // calculate sum for row i
            IntVar[] row = new IntVar[n];
            for (int j = 0; j < n; j++) {
                row[j] = O[i][j];
            }
            model.eq(model.realVar(presetE), model.sum("OSum_", row)).post();
        }

        // 4. get solver and solve model
        List<Solution> solutions = model.getSolver().findAllSolutions();

        // 5. print solutions
        int size = instance.getGamefieldSize();
        System.out.println("Number of solutions: " + solutions.size());
        int cnt = 1;
        for (Solution solution : solutions) {
            int[][] solutionArray = new int[size][size];
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    solutionArray[i][j] = solution.getIntVal(x[i][j]);
                }
            }
            if (cnt == 1) {
                instance.setSolution(solutionArray);
            }
            System.out.println("------- solution number " + cnt + "-------");
            instance.printSolution();

            ++cnt;
        }

        return instance;
    }
}
