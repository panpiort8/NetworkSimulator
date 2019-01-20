package Routers;

public class UEdgeStatus {
    private final long time;
    private final boolean enabled;

    public UEdgeStatus(boolean b, long time) {
        enabled = b;
        this.time = time;
    }

    static UEdgeStatus getNewer(UEdgeStatus es1, UEdgeStatus es2){
        return es1.time > es2.time ? es1 : es2;
    }

    public boolean newerThan(UEdgeStatus other){
        return time > other.time;
    }

    public long getTime() {
        return time;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
