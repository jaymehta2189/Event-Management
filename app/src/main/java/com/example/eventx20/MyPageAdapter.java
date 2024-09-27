package com.example.eventx20;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPageAdapter extends FragmentStateAdapter {

    public MyPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment for the corresponding page
        switch (position) {
            case 0:
                return new Qrfargment();
            case 1:
                return new foodfragment();
            default:
                return new Qrfargment(); // Default to the first fragment
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of fragments (pages)
    }
}
