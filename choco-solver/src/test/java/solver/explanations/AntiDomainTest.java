/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.explanations;

import junit.framework.Assert;
import org.testng.annotations.Test;
import solver.Solver;
import solver.explanations.antidom.AntiDomBipartiteSet;
import solver.explanations.antidom.AntiDomBitset;
import solver.explanations.antidom.AntiDomain;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 09/01/13
 */
public class AntiDomainTest {

    private void check(AntiDomain ad, Solver solver) {
        Assert.assertFalse(ad.get(1));
        Assert.assertFalse(ad.get(2));
        Assert.assertFalse(ad.get(3));
        Assert.assertFalse(ad.get(4));
        Assert.assertFalse(ad.get(5));
        solver.getEnvironment().worldPush();
        ad.set(1);
        ad.set(5);
        Assert.assertTrue(ad.get(1));
        Assert.assertFalse(ad.get(2));
        Assert.assertFalse(ad.get(3));
        Assert.assertFalse(ad.get(4));
        Assert.assertTrue(ad.get(5));
        solver.getEnvironment().worldPush();
        ad.set(3);
        Assert.assertTrue(ad.get(1));
        Assert.assertFalse(ad.get(2));
        Assert.assertTrue(ad.get(3));
        Assert.assertFalse(ad.get(4));
        Assert.assertTrue(ad.get(5));
        solver.getEnvironment().worldPop();
        Assert.assertTrue(ad.get(1));
        Assert.assertFalse(ad.get(2));
        Assert.assertFalse(ad.get(3));
        Assert.assertFalse(ad.get(4));
        Assert.assertTrue(ad.get(5));
        solver.getEnvironment().worldPop();
        Assert.assertFalse(ad.get(1));
        Assert.assertFalse(ad.get(2));
        Assert.assertFalse(ad.get(3));
        Assert.assertFalse(ad.get(4));
        Assert.assertFalse(ad.get(5));
    }

    @Test(groups = "1s")
    public void test01() {
        Solver solver = new Solver();
        IntVar v = VariableFactory.enumerated("A", 1, 5, solver);
        AntiDomain ad = new AntiDomBitset(v);
        check(ad, solver);
    }

    @Test(groups = "1s")
    public void test02() {
        Solver solver = new Solver();
        IntVar v = VariableFactory.enumerated("A", 1, 5, solver);
        AntiDomain ad = new AntiDomBipartiteSet(v);
        check(ad, solver);
    }

    @Test(groups = "1s")
    public void test03() {
        Solver solver = new Solver();
        IntVar v = VariableFactory.enumerated("A", 2, 6, solver);
        AntiDomain ad = new AntiDomBipartiteSet(v);
        ad.set(6);
        ad.set(5);
        ad.set(2);

    }
}
