/*
 * Copyright (c) 1999-2012, Ecole des Mines de Nantes
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Ecole des Mines de Nantes nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package solver.constraints.gary;

import org.testng.annotations.Test;
import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.propagators.gary.arborescences.PropArborescence;
import solver.constraints.propagators.gary.degree.PropNodeDegree_AtLeast;
import solver.constraints.propagators.gary.degree.PropNodeDegree_AtMost;
import solver.constraints.propagators.gary.path.PropPathNoCycle;
import solver.constraints.propagators.gary.path.PropReducedPath;
import solver.search.loop.monitors.SearchMonitorFactory;
import solver.search.strategy.GraphStrategyFactory;
import solver.search.strategy.strategy.AbstractStrategy;
import solver.variables.graph.DirectedGraphVar;
import util.objects.graphs.Orientation;
import util.objects.setDataStructures.SetType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PathTest {

    private static SetType graphTypeEnv = SetType.BOOL_ARRAY;
    private static SetType graphTypeKer = SetType.BOOL_ARRAY;

    public static Solver model(int n, int seed, boolean path, boolean arbo, boolean RG, long nbMaxSols) {
        Solver s = new Solver();
        DirectedGraphVar g = new DirectedGraphVar("G", s, n, graphTypeEnv, graphTypeKer, true);
        for (int i = 0; i < n - 1; i++) {
            for (int j = 1; j < n; j++) {
                g.getEnvelopGraph().addArc(i, j);
            }
        }
        Constraint gc = new Constraint(s);
        int[] succs = new int[n];
        int[] preds = new int[n];
        for (int i = 0; i < n; i++) {
            succs[i] = preds[i] = 1;
        }
        succs[n - 1] = preds[0] = 0;
        gc.addPropagators(new PropNodeDegree_AtLeast(g, Orientation.SUCCESSORS, succs));
        gc.addPropagators(new PropNodeDegree_AtMost(g, Orientation.SUCCESSORS, succs));
        gc.addPropagators(new PropNodeDegree_AtLeast(g, Orientation.PREDECESSORS, preds));
        gc.addPropagators(new PropNodeDegree_AtMost(g, Orientation.PREDECESSORS, preds));
        if (path) {
            gc.addPropagators(new PropPathNoCycle(g, 0, n - 1));
        }
        if (arbo) {
            gc.addPropagators(new PropArborescence(g, 0, true));
        }
        if (RG) {
            gc.addPropagators(new PropReducedPath(g));
        }
        AbstractStrategy strategy = GraphStrategyFactory.graphLexico(g);
        s.post(gc);
        s.set(strategy);
        if (nbMaxSols > 0) {
            SearchMonitorFactory.limitSolution(s, nbMaxSols);
        }
        s.findAllSolutions();
        return s;
    }

    public static void test(int s, int n, int nbMax) {
        System.out.println("Test n=" + n + ", with seed=" + s);
        Solver path = model(n, s, true, false, false, nbMax);
        Solver pathArbo = model(n, s, true, true, false, nbMax);
        Solver pathArboRG = model(n, s, true, true, true, nbMax);
        Solver arbo = model(n, s, false, true, false, nbMax);
        Solver arboRG = model(n, s, false, true, true, nbMax);
        // NbSolutions
        System.out.println("nbSols : " + path.getMeasures().getSolutionCount());
        assertEquals(path.getMeasures().getSolutionCount(), arbo.getMeasures().getSolutionCount());
        assertEquals(path.getMeasures().getSolutionCount(), pathArbo.getMeasures().getSolutionCount());
        assertEquals(path.getMeasures().getSolutionCount(), arboRG.getMeasures().getSolutionCount());
        assertEquals(path.getMeasures().getSolutionCount(), pathArboRG.getMeasures().getSolutionCount());
        // NbFails
        if (graphTypeEnv == SetType.BOOL_ARRAY) {
            assertTrue(path.getMeasures().getFailCount() >= arbo.getMeasures().getFailCount());
            assertTrue(arbo.getMeasures().getFailCount() >= arboRG.getMeasures().getFailCount());
//			not true anymore because path has been upgraded to reinforce filtering
//			assertEquals(pathArbo.getMeasures().getFailCount(), arbo.getMeasures().getFailCount());
//			assertEquals(arboRG.getMeasures().getFailCount(), pathArboRG.getMeasures().getFailCount());
        }
    }

    @Test(groups = "1s")
    public static void smallTrees() {
        for (int s = 0; s < 3; s++) {
            for (int n = 3; n < 8; n++) {
                test(s, n, -1);
            }
        }
    }

    @Test(groups = "10s")
    public static void bigTrees() {
        for (int s = 0; s < 3; s++) {
            for (int n = 100; n < 400; n += 100) {
                test(s, n, 1);
            }
        }
    }

    @Test(groups = "1s")
    public static void testAllDataStructure() {
        for (SetType ge : SetType.values()) {
            graphTypeEnv = ge;
            graphTypeKer = ge;
            smallTrees();
        }
    }
}
