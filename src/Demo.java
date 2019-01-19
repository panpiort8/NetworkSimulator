import Godernet.Godernet;
import Networks.CompleteNetwork;

public class Demo {
    public static void main(String[]args) throws InterruptedException {
        Godernet godernet = new Godernet(new CompleteNetwork(10));
        godernet.start();
        Thread.sleep(1000);
        godernet.forceStop();
    }
}
