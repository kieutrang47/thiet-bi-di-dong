package com.example.nguyenthikieutrang_22642451_module2_bai24;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new LoginFragment() : new HelpFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
