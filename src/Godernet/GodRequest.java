package Godernet;

import Routers.Router;

public class GodRequest{
    private final RequestType type;
    private final Router router;
    public GodRequest(RequestType type, Router router){
        this.type = type;
        this.router = router;
    }

    public Router getRouter() {
        return router;
    }

    public RequestType getType() {
        return type;
    }
}
