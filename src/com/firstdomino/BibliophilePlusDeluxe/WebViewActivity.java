package com.firstdomino.BibliophilePlusDeluxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.Toast;

/**
 * This class is used by the F3Activity to display the Google Map web page that allows users
 * to interact with it.  By interacting with it, users are then able to learn about books
 * that are set in physical locations throughout the world.
 */
public class WebViewActivity extends DashboardActivity 
{
    public static String tmpLoadUrl = "?"; 
	
	private WebView browser;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the various tabs that are to be presented to the user.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */	
	protected void onCreate(Bundle savedInstanceState) 
	{	
		
		try {
		 
		    super.onCreate(savedInstanceState);
		    setContentView (R.layout.activity_web_view);	    
		    setTitleFromActivityLabel (R.id.title_text);
		   
		    browser = (WebView) findViewById(R.id.webkit);
		    
	        browser.getSettings().setJavaScriptEnabled(true);
	        browser.getSettings().setGeolocationEnabled(true);
	        
	        browser.setWebViewClient(new BibliophileCallback());
	        browser.loadUrl(tmpLoadUrl);
	        
			Toast
			.makeText(this, "Loading URL(" + tmpLoadUrl + ")...", 4000)
			.show();
	        
	        tmpLoadUrl = null;
		}
		catch (Throwable t) {
						
			new AlertDialog.Builder(WebViewActivity.this)
			.setTitle("DEBUG")
			.setMessage("ERROR!  Could not load URL(" + tmpLoadUrl + ")\n" + t.toString())
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
	    }
	}

    /**
     * This handler (to be overriden by an inherited class) assumes that each screen 
     * will likely display a button that can invoke the SearchActivity class (which
     * allows the user to search the data cache with specific text).
     * 
     * @param v The button that invoked this handler
     * @return Nothing
     */
	public void onClickSearch (View v)
	{
		// NOTE: Should we even place a Search button on this screen?
	}

	/**
	 * This helper class provides us with the opportunity to create handlers that can
	 * capture exceptions thrown from the WebView. 
	 */	
    private class BibliophileCallback extends WebViewClient {
        
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	try {
        		
        	    view.loadUrl(url);        	    
        	}
        	catch (Throwable t) {        		
				Toast
				.makeText(WebViewActivity.this, 
						  "Issue with loading the specific URL: (" + url + ")", 
						  Toast.LENGTH_LONG)
				.show();
            }
            
            return (true);
        }
    }
    
} // end class
