package Edges;

public class UEdge extends Edge{
    public UEdge(Integer r1, Integer r2){
        this.r1 = Math.min(r1, r2);
        this.r2 = Math.max(r1, r2);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UEdge && super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("{%d,%d}", r1, r2);
    }
}
