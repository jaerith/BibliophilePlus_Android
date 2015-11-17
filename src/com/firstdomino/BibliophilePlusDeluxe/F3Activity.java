package com.firstdomino.BibliophilePlusDeluxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class represents the Explore Your World screen, allowing the user to interact
 * with a Google Map control.  When the user selects a specific location on the map,
 * a list of relevant books (with that location as the setting) will then be presented
 * to the user.  
 */
public class F3Activity extends DashboardActivity 
{
	private WebView browser;
	
	private BibliophileJSInterface jsInterface;
	
    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the WebView that will contain the web page and the interactive Google Map.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_f3);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    if (isNetworkAvailable()) {
		    browser     = (WebView) findViewById(R.id.webkit);
		    jsInterface = new BibliophileJSInterface(this, browser);
		    
	        browser.getSettings().setJavaScriptEnabled(true);
	        browser.getSettings().setGeolocationEnabled(true);
	        // browser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	        
	        browser.getSettings().setSupportZoom(false);
	        browser.getSettings().setBuiltInZoomControls(false);
	        
	        browser.addJavascriptInterface(jsInterface, "BibliophileJSInterface");
	        
	        if (HomeActivity.bOnBNDevice)
	            browser.loadUrl("http://api.bibliophileonline.com/BibliophileMapWidgetLS.htm");
	        else
	        	browser.loadUrl("http://api.bibliophileonline.com/BibliophileMapWidgetLSAmzn.htm");
	        
			Toast
			.makeText(this, "Downloading the interactive map...", 4000)
			.show();
	    }
	    else {
	    	
			new AlertDialog.Builder(F3Activity.this)
			.setTitle("SORRY!")
			.setMessage("This feature requires an active Internet connection.")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
			})
			.show();	    	
	    }
	}

    /**
     * When the user selects a location on the Google Map, there is a Javascript function 
     * within the hosting web page that acts as the handler.  The Javascript function 
     * then calls an associated instance of the BibliophileJSInterface class, and in turn,
     * the Interface class calls this method in order to display the results to the user.
     * 
     * @param psResults The list of books who are set in the location specified by the user
     * @return Nothing
     */		
	public void showResults(String psResults) {

		new AlertDialog.Builder(F3Activity.this)
		.setTitle("A Few Books Found near that Location")
		.setMessage(Html.fromHtml(psResults))
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
		})
		.show();
    }
    
} // end class
