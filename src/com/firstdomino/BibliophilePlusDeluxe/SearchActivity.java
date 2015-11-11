package com.firstdomino.BibliophilePlusDeluxe;

import android.os.Bundle;

/**
 * When complete, this class will provide an extensive set of options that will allow
 * the user to extensively search the data cache.
 * 
 * This class was still under construction at the time of the application's release.
 */
public class SearchActivity extends DashboardActivity 
{
    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the various tabs that are to be presented to the user.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */		
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_search);
	    setTitleFromActivityLabel (R.id.title_text);
	}
    
} // end class