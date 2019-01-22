import Godernet.Godernet;
import Networks.CycleNetwork;
import Networks.DynamicGeoNetwork;
import Networks.GeoNetwork;
import Networks.PathNetwork;
import javafx.util.Pair;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Demo {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s %5$s%6$s%n");
    }

    private static void setLevel(Level targetLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
        System.out.println("LOGGER LEVEL: " + targetLevel.getName());
    }

    private static void printStats(Godernet godernet){
        for(Pair<String, Object> stat : godernet.getStatistics())
            System.out.println(String.format("%s: %s", stat.getKey(), stat.getValue()));
    }

    public static void main(String[]args) throws InterruptedException {
        setLevel(Level.INFO);
        Godernet godernet = new Godernet(new DynamicGeoNetwork(10, (n)->5D));
        godernet.prepare();
        godernet.simulate(2000L);
        printStats(godernet);
    }
}
