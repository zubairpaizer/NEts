package com.fyp.faaiz.ets.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fyp.faaiz.ets.tabs.employee.EmployeesTabsFragment;
import com.fyp.faaiz.ets.tabs.home.HomeTabsFragment;
import com.fyp.faaiz.ets.tabs.location.LocationsTabsFragment;

/**
 * Created by zubair on 4/3/17.
 */

public class TabsFragment extends FragmentStatePagerAdapter {

    public TabsFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeTabsFragment();
            case 1:
                return new LocationsTabsFragment();
            case 2:
                return new EmployeesTabsFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Locations";
            case 2:
                return "Employees";
            default:
                break;
        }
        return super.getPageTitle(position);
    }
}
