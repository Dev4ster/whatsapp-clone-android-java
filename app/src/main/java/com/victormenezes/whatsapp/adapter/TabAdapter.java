package com.victormenezes.whatsapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.victormenezes.whatsapp.fragment.ContactsFragment;
import com.victormenezes.whatsapp.fragment.ConversationsFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] titleTabs = {"CONTATOS", "CONVERSAS"};


    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ContactsFragment();
                break;
            case 1:
                fragment = new ConversationsFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titleTabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleTabs[position];
    }
}
