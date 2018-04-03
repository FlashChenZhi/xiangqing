package com.asrs;

import com.asrs.domain.Route;

import java.util.HashMap;

/**
 * Author: Zhouyue
 * Date: 2008-10-21
 * Time: 12:11:07
 * Copyright Daifuku Shanghai Ltd.
 */
public class RouteManager {
    private static RouteManager instance = new RouteManager();

    private RouteManager() {
//                  Transaction.begin();
//                  Session session = HibernateUtil.getCurrentSession();
//                  Collection<Route> routes = session.createQuery("from Route").list();
//                  for (Route route : routes)
//                  {
//                        RouteManager.Instance().addRoute(route.getFromStation() + route.getToStation(), route);
//                  }
//                  Transaction.commit();
    }

    public static RouteManager Instance() {
        return instance;
    }

    public HashMap<String, Route> routes = new HashMap<String, Route>();

    public void addRoute(String key, Route route) {
        routes.put(key, route);
    }

    public boolean isRouteOk(String sourceSt, String destSt) throws AsrsException {
        return true;
//            if (routes.containsKey(sourceSt + destSt))
//            {
//                  Route route = routes.get(sourceSt + destSt);
//                  for (AsrsMachine mac : route.getAsrsMachines())
//                  {
//                        String macStr = mac.getMacType() + mac.getMacNo();
//                        if (MacManager.Instance().getStatus(macStr).equals(Message30._Status.Disconnected))
//                        {
//                              return false;
//                        }
//                  }
//                  return true;
//            }
//            else
//            {
//                  throw new AsrsException(String.format(AsrsError.ROUTE_NOT_FOUNE, sourceSt, destSt));
//            }
    }

}
