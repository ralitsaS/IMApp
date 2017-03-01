package com.katakiari.contest2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 3;
	// Tab Titles
	private String tabtitles[] = new String[] { "CONTACTS", "ROOMS","GROUPS" };
	Context context;
 
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	@Override
	public Fragment getItem(int position) {
		switch (position) {
 
			// Open FragmentTab1.java
		case 0:
			FriendsList fragmenttab1 = new FriendsList();
			return fragmenttab1;
 
			// Open FragmentTab2.java
		case 1:
			ChatRooms fragmenttab2 = new ChatRooms();
			return fragmenttab2;
			
			// Open FragmentTab2.java
					case 2:
						ChatGroups fragmenttab3 = new ChatGroups();
						return fragmenttab3;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}