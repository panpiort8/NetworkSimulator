package Godernet;

public class SimpleTimer{
    private long startTime;
    private final long period;
    public SimpleTimer(long period){
        this.period = period;
        restart();
    }
    public boolean isOver(){
        return System.currentTimeMillis()-startTime >= period;
    }
    public void restart(){
        startTime = System.currentTimeMillis();
    }
}