package com.example.androidproject.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidproject.Fragments.ChatsFragment;
import com.example.androidproject.Fragments.UsersFragment;


public class VPAdapter extends FragmentStateAdapter {

    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1 :
                return  new UsersFragment();
            default:
                return  new ChatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
