import Godernet.Godernet;
import Networks.CycleNetwork;
import Networks.DynamicGeoNetwork;
import Networks.GeoNetwork;
import Networks.PathNetwork;
import javafx.util.Pair;
import org.junit.Test;

import javax.xml.ws.Action;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Demo {
    private static void setLevel(Level targetLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
        System.out.println("LOGGER LEVEL: " + targetLevel.getName());
    }

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s %5$s%6$s%n");
        setLevel(Level.OFF);
    }

    private static void printStats(Godernet godernet){
        for(Pair<String, Object> stat : godernet.getStatistics())
            System.out.println(String.format("%s: %s", stat.getKey(), stat.getValue()));
    }


    @Test
    public void pathNetworkRun() throws InterruptedException {
        Godernet godernet = new Godernet(new PathNetwork(40));
        godernet.prepare();
        godernet.simulate(2000L);
        printStats(godernet);
    }

    @Test
    public void circleNetworkRun() throws InterruptedException {
        Godernet godernet = new Godernet(new CycleNetwork(100));
        godernet.prepare();
        godernet.simulate(2000L);
        printStats(godernet);
    }

    @Test
    public void staticGeoNetwork() throws InterruptedException {
        Godernet godernet = new Godernet(new GeoNetwork(100));
        godernet.prepare();
        godernet.simulate(5000L);
        printStats(godernet);
    }

    @Test
    public void dynamicGeoNetwork() throws InterruptedException {
        Godernet godernet = new Godernet(new DynamicGeoNetwork(100));
        godernet.prepare();
        godernet.simulate(5000L);
        printStats(godernet);
    }

    @Test
    public void lowDenseDynamicGeoNetwork() throws InterruptedException {
        Godernet godernet = new Godernet(new DynamicGeoNetwork(100, (n) -> 5D));
        godernet.prepare();
        godernet.simulate(5000L);
        printStats(godernet);
    }

    public static void main(String[]args) throws InterruptedException {

    }
}
