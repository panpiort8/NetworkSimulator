package Routers;

public class DEdgeStatus {
    private final long time;
    private final boolean enabled;

    public DEdgeStatus(boolean b, long time) {
        enabled = b;
        this.time = time;
    }

    static DEdgeStatus getNewer(DEdgeStatus es1, DEdgeStatus es2){
        return es1.time > es2.time ? es1 : es2;
    }

    public boolean newerThan(DEdgeStatus other){
        return time > other.time;
    }

    public long getTime() {
        return time;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
