package Edges;

import java.util.Objects;

public abstract class Edge {
    protected Integer r1;
    protected Integer r2;

    public Integer getR1() {
        return r1;
    }

    public Integer getR2() {
        return r2;
    }

    @Override
    public int hashCode() {
        return r1*20089 + r2;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Edge))
            return false;
        Edge e = (Edge) o;
        return Objects.equals(r1, e.r1) && Objects.equals(r2, e.r2);
    }
}
