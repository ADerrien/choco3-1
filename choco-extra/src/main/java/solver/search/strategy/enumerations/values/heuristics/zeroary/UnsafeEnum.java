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

package solver.search.strategy.enumerations.values.heuristics.zeroary;

import gnu.trove.map.hash.THashMap;
import solver.search.strategy.enumerations.values.heuristics.Action;
import solver.search.strategy.enumerations.values.heuristics.HeuristicVal;

public class UnsafeEnum extends HeuristicVal {
    int from;
    int delta;
    int to;
    int idx;

    private UnsafeEnum(Action action) {
        super(action);
    }

    public UnsafeEnum(int from, int delta, int to) {
        super();
        this.from = from;
        this.idx = from;
        this.delta = delta;
        this.to = to;
    }

    public UnsafeEnum(int from, int delta, int to, Action action) {
        super(action);
        this.from = from;
        this.idx = from;
        this.delta = delta;
        this.to = to;
    }

    public boolean hasNext() {
        return delta > 0 ? idx <= to : idx >= to;
    }

    public int next() {
        int _from = idx;
        idx += delta;
        return _from;
    }

    public void remove() {
        throw new UnsupportedOperationException("UnsafeEnum.remove not implemented");
    }

    @Override
    protected void doUpdate(Action action) {
        idx = from;
    }

    @Override
    public HeuristicVal duplicate(THashMap<HeuristicVal, HeuristicVal> map) {
        if (map.containsKey(this)) {
            return map.get(this);
        } else {
            UnsafeEnum duplicata = new UnsafeEnum(this.action);
            duplicata.from = this.from;
            duplicata.delta = this.delta;
            duplicata.to = this.to;
            duplicata.idx = this.idx;
            map.put(this, duplicata);
            return duplicata;
        }
    }
}
