package com.firstdomino.BibliophilePlusDeluxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.WebView;

/**
 * This class provides the callback interface for the Javascript running in the F3Activity's web page , where:
 * 
 *     'mLocActivity' is the Activity that lets users find books by their settings in locations around the globe
 *     'WebView' is the class that holds the web page (of a global map) and its running Javascript 
 */
public class BibliophileJSInterface {

	private F3Activity mLocActivity;
	private WebView    mAppView;
	
	public boolean mLocationsLoaded = false;
	
	public BibliophileJSInterface (F3Activity activity, WebView appView) {
		
		this.mLocActivity = activity;
        this.mAppView     = appView;
	}

	/**
	 * Based on the user's interactivity with the web page, the Javascript will call this method 
	 * in order to show the books found in the specified location. 
	 *
	 * @param  psMessage The message containing those books found in the user's specified location.
	 * @return Nothing. 
	 */						
    public void setResults(String psMessage) {
    	
    	mLocActivity.showResults(psMessage);
    }
    
	/**
	 * Debugging method.  
	 *
	 * @param  psMessage A method to test interactivity with the Javascript running in the F3Activity.
	 * @return Nothing. 
	 */						
    public void testEcho(String psMessage) {

		new AlertDialog.Builder(mLocActivity)
		.setTitle("DEBUG")
		.setMessage("ERROR!  BibliphileJSInterface::testEcho() -> (" + psMessage + ")...")
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dlg, int s) {}
		})
		.show();    	
    }
}