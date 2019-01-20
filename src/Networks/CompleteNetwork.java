package Networks;

import Godernet.Link;
import Routers.Router;

import java.util.Objects;

public class CompleteNetwork extends Network{
    CompleteNetwork(){}
    public CompleteNetwork(int n){
        super(n);
        for (Router r1 : routers.values())
            for(Router r2 : routers.values())
                if(!Objects.equals(r1, r2))
                    links.add(new Link(r1, r2));

    }
}
