package com.example.mangareader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Detail detail;
    //mod in order to send data to another fragment in view pager smh -1
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Detail detail) {
        super(fragmentActivity);
        //mod in order to send data to another fragment in view pager smh -2
        this.detail = detail;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            //mod in order to send data to another fragment in view pager smh -3
            case 0: return Fragment_Info.newInstance(detail);
            case 1: return new Fragment_Chapter();
            default: return new Fragment_Info();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
