package com.fyp.faaiz.ets.drawer;

/**
 * Created by zubair on 4/1/17.
 */

import java.util.ArrayList;

public class NavigationDrawerItem {

    public static ArrayList<String> getData() {

        ArrayList list = new ArrayList();

        for (int i = 0; i < 10; i++) {
            list.add("Faaiz Hussain" + i);
        }

        return list;
    }
}
