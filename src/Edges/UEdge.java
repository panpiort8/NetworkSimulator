package Edges;

import Edges.Edge;

public class UEdge extends Edge<Integer> {

    public UEdge(Integer rid1, Integer rid2){
        r1 = Math.min(rid1, rid2);
        r2 = Math.max(rid1, rid2);
    }

}
