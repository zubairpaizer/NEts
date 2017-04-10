package com.fyp.faaiz.ets;

/**
 * Created by zubair on 4/1/17.
 */

import java.util.ArrayList;

public class NavigationDrawerItem {
    public static ArrayList<String> getData() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < 10; i++) {
            list.add("Zubair Ibrahim" + i);
        }

        return list;
    }
}
