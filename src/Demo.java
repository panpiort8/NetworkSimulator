import Godernet.Godernet;
import Networks.CycleNetwork;
import Networks.PathNetwork;

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
        System.out.println("level set: " + targetLevel.getName());
    }

    public static void main(String[]args) throws InterruptedException {
        setLevel(Level.INFO);
        Godernet godernet = new Godernet(new CycleNetwork(100));
        godernet.prepare();
//        Thread.sleep(1000);
        godernet.simulate(2000L);
        godernet.waitForRouters();
        System.out.println(godernet.getStatistics());
    }
}
