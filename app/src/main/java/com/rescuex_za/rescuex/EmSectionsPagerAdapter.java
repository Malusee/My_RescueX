package com.rescuex_za.rescuex;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Asus on 12/29/2017.
 */

public class EmSectionsPagerAdapter extends FragmentPagerAdapter {

    public EmSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                EmergencyFragment emergencyFragment = new EmergencyFragment();
                return emergencyFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 1;
    }
    public CharSequence getPageTitle(int position){

        switch(position){
            case 0:
                return "My Emergencies";

            default:
                return null;
        }

    }
}
