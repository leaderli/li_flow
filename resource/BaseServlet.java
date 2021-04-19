package com.leaderli.li.flow.base;

import java.util.Map;

public interface BaseServlet {


    void service(Session session);


    Map<String, String> getBranches();

}
