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

package parser.flatzinc.ast.constraints.global;

import gnu.trove.map.hash.THashMap;
import parser.flatzinc.ast.constraints.IBuilder;
import parser.flatzinc.ast.expression.EAnnotation;
import parser.flatzinc.ast.expression.Expression;
import solver.Solver;
import solver.constraints.Constraint;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

import java.util.List;

/**
 * <br/>
 *
 * @author Jean-Guillaume Fages
 * @since 26/07/12
 */
public class GlobalCardinalityLowUpBuilder implements IBuilder {

    @Override
    public Constraint[] build(Solver solver, String name, List<Expression> exps, List<EAnnotation> annotations, THashMap<String, Object> map) {
        IntVar[] vars = exps.get(0).toIntVarArray(solver);
        int[] values = exps.get(1).toIntArray();
        int[] low = exps.get(2).toIntArray();
        int[] up = exps.get(3).toIntArray();
        boolean closed = exps.get(4).boolValue();
        IntVar[] cards = new IntVar[low.length];
        for (int i = 0; i < low.length; i++) {
            cards[i] = VariableFactory.bounded("card of val " + values[i], low[i], up[i], solver);
        }
        return new Constraint[]{IntConstraintFactory.global_cardinality(vars, values, cards, closed)};
    }
}
