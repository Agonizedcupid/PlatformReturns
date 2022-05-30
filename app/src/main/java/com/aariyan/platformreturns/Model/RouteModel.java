package com.aariyan.platformreturns.Model;

public class RouteModel {
    private int Routeid;
    private String Route;

    public RouteModel() {
    }

    public RouteModel(int routeid, String route) {
        Routeid = routeid;
        Route = route;
    }

    public int getRouteid() {
        return Routeid;
    }

    public void setRouteid(int routeid) {
        Routeid = routeid;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    @Override
    public String toString() {
        return getRoute();
    }
}
