package Edges;

public class DEdge extends Edge{

    public DEdge(Integer r1, Integer r2){
        this.r1 = r1;
        this.r2 = r2;
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
