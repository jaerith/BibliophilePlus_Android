package com.firstdomino.BibliophilePlusDeluxe;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
import android.net.*;
import android.view.KeyEvent;
import android.view.View;

import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * 
 * This class serves as the home screen of the application.  It will display six large buttons
 * on its face, where each button will then correspond to the invocation of one F*Activity class.
 * The buttons will appear the following layout in the home screen:
 * 
 *            F1Activity Button (Browse Books)                        F2Activity Button (Discover Influences)
 *            
 *            F3Activity Button (Explore Your World)                  F4Activity Button (Analyze the Charts)
 *            
 *            F5Activity Button (Test Your Knowledge)                 F6Activity Button (Send Us Your Ideas)
 *
 * F1Activity -> Allows the user to generally browse our catalog's books
 * F2Activity -> Allows the user to dig deeper into the catalog, specifically the trivia data (songs influenced by books, etc.)
 * F3Activity -> Allows the user to discover books' settings using a Google Map
 * F4Activity -> Allows the user to look at charts showing statistics about the trivia data
 * F5Activity -> Allows the user to play a game using the trivia data
 * F6Activity -> Provides a form for the user to submit their own trivia facts and/or about new ideas for this app
 *  
 */

public class HomeActivity extends DashboardActivity 
{
	public static boolean bFirstInstance = true;
	public static boolean bOnBNDevice    = true;
	
	public static final String sRetrieveUrl  = "http://api.bibliophileonline.com/bibliofile_retrieve.php";
	
	public static final String sDetectDefaultModeUrl    = sRetrieveUrl + "?method=getDefaultMode";
	public static final String sDetectDefaultSubModeUrl = sRetrieveUrl + "?method=getDefaultSubMode";
	
	public static final String sRevokeItemsUrl = sRetrieveUrl + "?method=getRevokeItems";	
	public static final String sSuppSibbUrl    = sRetrieveUrl + "?method=getMostRecentSongFacts";
	public static final String sSuppGibbUrl    = sRetrieveUrl + "?method=getMostRecentGameFacts";
	public static final String sEanListUrl     = sRetrieveUrl + "?method=getEanList";
	public static final String sSuppLibUrl     = "";
	public static final String sSuppQuoteUrl   = "";

    /**
     * Standard Android method called to invoke the Activity class.  This method will
     * be the starting point for the entire application, initializing the data cache
     * and making other necessary preparations.  
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
		
	    if (bFirstInstance)
	    {
			String sProduct      = android.os.Build.PRODUCT;
			String sManufacturer = android.os.Build.MANUFACTURER;
		    
			if (sManufacturer.equals("BarnesAndNoble"))
				bOnBNDevice = true;
			else
				bOnBNDevice = false;
	    	
	    	saveSupplementalData();
	    	
			try {
				BibliophileBase.parseBiblioData(this, false);
		    }
			catch (Throwable e) {
				
				new AlertDialog.Builder(this)
				.setTitle("DEBUG")
				.setMessage("HomeActivity::onCreate() -> Failed to parse biblio data :\n(" + e + ")")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }
		    
			try {
				LocationManager locationManager = 
						(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
				
				BibliophileLocationManager.initialize(this, locationManager);
		    }
			catch (Throwable e) {
				
				new AlertDialog.Builder(this)
				.setTitle("DEBUG")
				.setMessage("HomeActivity::onCreate() -> Failed to instantiate LocationManager :\n(" + e + ")")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }
						
			bFirstInstance = false;
	    }
	}
    
    /**
     * Standard Android method that serves as the class destructor.
     * 
     * @return Nothing
     */	
	protected void onDestroy ()
	{
	   super.onDestroy ();
	}

    /**
     * Standard Android method that serves to pause the Activity's execution.
     * 
     * @return Nothing
     */		
	protected void onPause ()
	{
	   super.onPause ();
	}

    /**
     * Standard Android method that serves to restart the Activity's execution.
     * 
     * @return Nothing
     */				
	protected void onRestart ()
	{
	   super.onRestart ();
	}

    /**
     * Standard Android method that serves to resume the Activity's execution.
     * 
     * @return Nothing
     */				
	protected void onResume ()
	{
	   super.onResume ();
	}

    /**
     * Standard Android method that serves to start the Activity's execution.
     * 
     * NOTE: We will test the network availability here in order to:
     * 1.) determine if we can download more recent data from the web site 
     * 2.) determine if certain functionality should be available to the user 
     * 
     * @return Nothing
     */					
	protected void onStart ()
	{
	   super.onStart ();
	   
	   if (!BibliophileBase.defaultEnabled) {
		   
		   if (this.isNetworkAvailable()) {
			   
			   if (this.isDefaultMode())
				   BibliophileBase.defaultEnabled = true;
			   
			   if (this.isDefaultSubMode())
				   BibliophileBase.altDefaultEnabled = true;
	       }
	   }
	}

    /**
     * Standard Android method that serves to stop the Activity's execution.
     * 
     * @return Nothing
     */							
	protected void onStop ()
	{
	   super.onStop ();
	}

    /**
     * This handler will ask for user confirmation before manually downloading the most
     * recent data available from the web site.
     * 
     * NOTE: This handler is currently not used.
     * 
     * @param v The button that called this handler
     * @return Nothing
     */							
	public void onClickRefresh (View v)
	{
    	if (HomeActivity.this.isNetworkAvailable()) {
    		    		
			new AlertDialog.Builder(this)
			.setTitle("Confirmation")
			.setMessage("Are you sure that you want to update the info in this app?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
			    public void onClick(DialogInterface dlg, int s) {
				    
			    	if (HomeActivity.this.isNetworkAvailable()) {
			    		
			    		HomeActivity.this.saveSupplementalData();
			    		
			    		BibliophileBase.updateData(HomeActivity.this);
			        }
			    }
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {		
			    public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
    	}
    	else {
    		
			new AlertDialog.Builder(this)
			.setTitle("Warning")
			.setMessage("Sorry!  Since there is no internet connection available, the info in this app cannot be updated.")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
    	}
	}

    /**
     * This override handler ensures that this Activity will go back to the Task stack.
     * 
     * @param keyCode The value of the key that was hit by the user
     * @param event The type of event generated by the user
     * @return Indicates whether the handler was able to respond successfully
     */							
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
		
	    return super.onKeyDown(keyCode, event);
	}
	
	//
	// NOTE: Support Methods
	//
	
    /**
     * This method makes a call to the web site and determines if user should be 
     * able to retrieve web pages by selecting displayed trivia data.
     * 
     * For example, if an user selects a game trivia item and if DefaultMode is
     * enabled, then the application will use the link to retrieve a Wikipedia
     * page about the game.
     * 
     * @return Indicates whether or not the web site has determined that DefaultMode 
     *         should be enabled
     */	
	private boolean isDefaultMode() {
		
		boolean bDefaultMode = false;
		String  sDefaultInd  = "";
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpClient = new DefaultHttpClient();
	
	    try {
	    	
	        // Execute HTTP Get Request
	        HttpResponse response = httpClient.execute(new HttpGet(sDetectDefaultModeUrl));
	        	        
	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();

	        if (entity != null) {
	
	            InputStream instream = entity.getContent();
	            
                BufferedReader reader     = new BufferedReader(new InputStreamReader(instream));
                StringBuilder  sbSuppData = new StringBuilder();
                
                String line = null;
                while((line = reader.readLine()) != null){                	
                    sbSuppData.append(line);
                }
                instream.close();
                
                sDefaultInd = sbSuppData.toString();

                if (sDefaultInd.equalsIgnoreCase("yes")) {
                	bDefaultMode = true;
	            }
	        }	        
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
		return bDefaultMode;
    }

    /**
     * This method makes a call to the web site and determines if certain controls
     * should be visible (i.e., enabled) to the user.
     * 
     * @return Indicates whether or not the web site has determined that DefaultSubMode 
     *         should be enabled
     */	
	private boolean isDefaultSubMode() {
		
		boolean bDefaultMode = false;
		String  sDefaultInd  = "";
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpClient = new DefaultHttpClient();
	
	    try {
	    	
	        // Execute HTTP Get Request
	        HttpResponse response = httpClient.execute(new HttpGet(sDetectDefaultSubModeUrl));
	        	        
	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();

	        if (entity != null) {
	
	            InputStream instream = entity.getContent();
	            
                BufferedReader reader     = new BufferedReader(new InputStreamReader(instream));
                StringBuilder  sbSuppData = new StringBuilder();
                
                String line = null;
                while((line = reader.readLine()) != null){                	
                    sbSuppData.append(line);
                }
                instream.close();
                
                sDefaultInd = sbSuppData.toString();

                if (sDefaultInd.equalsIgnoreCase("yes")) {
                	bDefaultMode = true;
	            }
	        }	        
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
		return bDefaultMode;
    }

    /**
     * This method downloads the data (trivia, metadata, etc.) from the provided URL.
     * Usually, the URL refers to a specific type of data (songs influenced by books,
     * games influenced by books, etc.). 
     *
     * @param sSuppUrl The HTTP URL used to download the trivia data
     * @return Data retrieved through the HTTP Get
     */	
	private String downloadSupplementalData(String sSuppUrl) {
		
		String sDownloadedSuppData = "";
		
	    // Create a new HttpClient and Post Header
	    HttpClient  httpClient  = new DefaultHttpClient(); 
	
	    try {
	    	
	        // Execute HTTP Get Request
	        HttpResponse response = httpClient.execute(new HttpGet(sSuppUrl));
	        	        
	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();

	        if (entity != null) {
	
	            InputStream instream = entity.getContent();
	            
                BufferedReader reader     = new BufferedReader(new InputStreamReader(instream));
                StringBuilder  sbSuppData = new StringBuilder();
                
                String line = null;
                while((line = reader.readLine()) != null){
                	
                	line = line.replace("<br/>", "\n");
                			
                    sbSuppData.append(line);
                }
                instream.close();
                
                sDownloadedSuppData = sbSuppData.toString();
	            
	            // now you have the string representation of the HTML request
	            instream.close();
	        }	        
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    
	    return sDownloadedSuppData;
	} 

    /**
     * This method will download and save locally to files all of the supplemental data 
     * (trivia, metadata, etc.) in which we have an interest.
     *
     * @return None.
     */		
	private void saveSupplementalData() {
		
    	if (isNetworkAvailable())
    	{
    		String sRevokeItemRecords = downloadSupplementalData(sRevokeItemsUrl);
    		
			String sSibbSuppRecords = downloadSupplementalData(sSuppSibbUrl); 

			String sGibbSuppRecords = downloadSupplementalData(sSuppGibbUrl);
			
			String sEanListRecords = downloadSupplementalData(sEanListUrl);

			/*
			 * NOTE: Where the real work begins
			 */
			try {
				FileOutputStream fosRevokeItems = 
						openFileOutput(BibliophileBase.CONST_REVOKE_ITEMS_FILE, Context.MODE_PRIVATE);
				
				fosRevokeItems.write(sRevokeItemRecords.getBytes());
				fosRevokeItems.close();
			}
			catch (Throwable e) {
				
				new AlertDialog.Builder(HomeActivity.this)
				.setTitle("DEBUG")
				.setMessage("ERROR!  Failed to write to revoked items file:\n(" + e + ")...")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }
			
			try {
				FileOutputStream fosSibb = 
						openFileOutput(BibliophileBase.CONST_SUPP_FILE_SIBB, Context.MODE_PRIVATE);
				
				fosSibb.write(sSibbSuppRecords.getBytes());
				fosSibb.close();
			}
			catch (Throwable e) {
				
				new AlertDialog.Builder(HomeActivity.this)
				.setTitle("DEBUG")
				.setMessage("ERROR!  Failed to write to supplemental SIBB file:\n(" + e + ")...")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }

			try {
				FileOutputStream fosGibb = 
						openFileOutput(BibliophileBase.CONST_SUPP_FILE_GIBB, Context.MODE_PRIVATE);
				
				fosGibb.write(sGibbSuppRecords.getBytes());
				fosGibb.close();
			}
			catch (Throwable e) {
				
				new AlertDialog.Builder(HomeActivity.this)
				.setTitle("DEBUG")
				.setMessage("ERROR!  Failed to write to supplemental GIBB file:\n(" + e + ")...")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }
			
			try {
				FileOutputStream fosEans = 
						openFileOutput(BibliophileBase.CONST_EAN_LIST_FILE, Context.MODE_PRIVATE);
				
				fosEans.write(sEanListRecords.getBytes());
				fosEans.close();
			}
			catch (Throwable e) {
				
				new AlertDialog.Builder(HomeActivity.this)
				.setTitle("DEBUG")
				.setMessage("ERROR!  Failed to write to EAN List file:\n(" + e + ")...")
				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
				})
				.show();
		    }
    	}
	} 

} // end class
