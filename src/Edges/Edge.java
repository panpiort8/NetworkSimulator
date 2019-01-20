package Edges;

import java.util.Objects;

public abstract class Edge<T>{
    protected T r1;
    protected T r2;

    public T getR1() {
        return r1;
    }

    public T getR2() {
        return r2;
    }

    @Override
    public int hashCode() {
        return r1.hashCode()*20089 + r2.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Edge))
            return false;
        Edge e = (Edge) o;
        return Objects.equals(r1, e.r1) && Objects.equals(r2, e.r2);
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", r1, r2);
    }
}
