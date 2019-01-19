package Routers;

import Edges.Edge;

public class DEdge extends Edge<Integer> {

    DEdge(Integer rid1, Integer rid2){
        r1 = rid1;
        r2 = rid2;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DEdge && super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", r1, r2);
    }
}
