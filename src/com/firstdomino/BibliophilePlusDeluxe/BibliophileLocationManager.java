package com.firstdomino.BibliophilePlusDeluxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.*;
import android.os.Bundle;
import android.webkit.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * This "singleton" class had been intended for use within the F3Activity, before the switch to using the
 * WebView class with the Google Map inside of it.  There was the intention to eventually include it as a
 * supplement to the F3Activity.  However, it was never actually completed.
 */
public class BibliophileLocationManager {
	
	public static final String CONST_BOOK_SETTING = "Book Which is Set ";
	
	// Acquire a reference to the system Location Manager
	private static HomeActivity    homeActivity    = null;
	private static LocationManager locationManager = null;
	
	private static String tmpBookLink = ""; 
	
	public static double initLatitude  = 40.6985;
	public static double initLongitude = -73.9514;
	
	public static HashMap<String, String> veryNearItems = new HashMap<String, String>();
	public static HashMap<String, String> closeItems    = new HashMap<String, String>();
	public static HashMap<String, String> distantItems  = new HashMap<String, String>();
	
	/**
	 * Initializes data about books whose setting was within a close proximity of the user's current location. 
	 *
	 * @param  poHomeActivity    The main Activity launched when the Android application starts up.
	 * @param  poLocationManager The Android class that would help us to determine the user's current location.
	 * @return Nothing. 
	 */						
	public static void initialize(HomeActivity poHomeActivity, LocationManager poLocationManager) {
		
		homeActivity    = poHomeActivity;
		locationManager = poLocationManager;
		
		/*
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			
		    public void onLocationChanged(Location location) {
		    	
		        // Called when a new location is found by the network location provider.
		     
		    	if (initLatitude == 0.00)
                   initLatitude = location.getLatitude();
		    	
		    	if (initLongitude == 0.00)
		    		initLongitude = location.getLongitude();
		    	
		    	
		    }
	
		    public void onStatusChanged(String provider, int status, Bundle extras) {}
	
		    public void onProviderEnabled(String provider) {}
	
		    public void onProviderDisabled(String provider) {}
		};
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000, locationListener);
		*/		  
	}

	/*
	 * 
	public static void generateRandomClosebyBook()
	{
		generateRandomLocationBook("Closeby", closeItems);
	}
	
	public static void generateRandomFartherBook()
	{
		generateRandomLocationBook("Farther Away", distantItems);
	}
	
	public static void generateRandomNearbyBook()
	{
		generateRandomLocationBook("Very Nearby", veryNearItems);
	}
	
	private static void generateRandomLocationBook(String psRelativeTitleLoc, HashMap<String, String> poLocations)  
	{
		int    nLoopCount = 0;
    	int    nRandomIdx = 0;
    	String sTmpKey    = null;
    	
		Random poGenerator = new Random();
    	
    	nRandomIdx = (poGenerator.nextInt() % poLocations.size());
    	
    	while ((nRandomIdx = (poGenerator.nextInt() % poLocations.size())) < 0)
    	{    		
    		if (nLoopCount > BibliophileBase.MAX_LIST_SIZE)
    			break;
    		else    		
    		    ++nLoopCount;
        }
    	
		if (nRandomIdx >= 0) 
		{    			
			sTmpKey = (String) poLocations.keySet().toArray()[nRandomIdx];				    		
    	
			if (sTmpKey != null)
			{
				int      nParanIdx      = -1;
				String   sDialogTitle   = CONST_BOOK_SETTING + psRelativeTitleLoc;
				String   sTmpBook       = "";
				String   sTmpAuthor     = "";
				String   sLocation      = poLocations.get(sTmpKey);
				String   sItem          = sTmpKey + " (Set in " + sLocation + ")";
				String[] aBookDataSplit = sTmpKey.split(BibliophileBase.BOOK_INFO_DELIM);
	
				sTmpBook   = aBookDataSplit[0];
				sTmpAuthor = aBookDataSplit[1]; 
				
				sTmpBook = sTmpBook.replaceAll("The Story of ", "");
				sTmpBook = sTmpBook.replaceAll("The Legend of ", "");
				sTmpBook = sTmpBook.replaceAll("the ", "The ");
				sTmpBook = sTmpBook.replaceAll(" ", "_");
				sTmpBook = sTmpBook.replaceAll("'", "%27");
				sTmpBook = sTmpBook.replaceAll("(Series)", "(series)");
				
				sTmpAuthor = sTmpAuthor.replaceAll(" ", "_");
				sTmpAuthor = sTmpAuthor.replaceAll("'", "%27");
				
				if ((nParanIdx = sTmpBook.indexOf("(")) > 0) {
					sTmpBook = sTmpBook.substring(0, nParanIdx).trim();
			    }
				
				tmpBookLink = BibliophileBase.CONST_WIKI_SITE + sTmpBook + "_" + sTmpAuthor;
						
				new AlertDialog.Builder(homeActivity)
				.setTitle(sDialogTitle)
				.setMessage(sItem + "\n\nWould you like to find out more about this book or author?")
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int s) 
					{}
				})
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int s) {
						
						bibliophileBase.loadBrowserUrl(tmpBookLink);
						
						bibliophileBase.tabs.setCurrentTab(4);
						
						tmpBookLink = "";
					}
				})
				.show();
			}
		}
    }
	
	public static void parseLocationData(int nLocationDataHandle)
	{
		int nRowCount = 0;
        int nRowMax   = 100000;
        
        double dTmpLat  = 0.0;
        double dTmpLong = 0.0;
		
        try{
            InputStream in = homeActivity.getResources().openRawResource(nLocationDataHandle);
            
            String          sTmpLine   = null;
            String          sTmpValue  = null;
            String          sBook      = null;
            String          sAuthor    = null;
            String          sLocation  = null;
            String          sLatitude  = null;
            String          sLongitude = null;
            String          sBookLink  = null;
            String          sItem      = null;
            BufferedReader  reader     = null;
            StringTokenizer tokenizer  = null;
            
            reader = new BufferedReader(new InputStreamReader(in));
                        
            nRowCount = 0;
            for (nRowCount = 0; 
                 (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
                 ++nRowCount) {
            	
            	tokenizer = new StringTokenizer(sTmpLine, "|");
            	
            	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
            		
                	sTmpValue = tokenizer.nextToken();
            		
            		if (i == 0){
            			sBook = sTmpValue;
            		}
            		else if (i == 1) {
            			sAuthor = sTmpValue;
              	    }
            		else if (i == 2) {
            			sLocation = sTmpValue;            			
            	    }
            		else if (i == 3) {
            			sLatitude = sTmpValue;
            	    }
            		else if (i == 4) {
            			sLongitude = sTmpValue;
            	    }
                }
            	
            	dTmpLat  = Double.parseDouble(sLatitude);
            	dTmpLong = Double.parseDouble(sLongitude);
            	
            	sItem = sBook + " written by " + sAuthor;
            	                       	
            	if ( (dTmpLat >= (initLatitude-0.5)) && (dTmpLat <= (initLatitude+0.5)) &&
            	     (dTmpLong >= (initLongitude-0.5)) && (dTmpLong <= (initLongitude+0.5)) ) {
            		
            		veryNearItems.put(sItem, sLocation);
                }
            	else if ( (dTmpLat >= (initLatitude-5.0)) && (dTmpLat <= (initLatitude+5.0)) &&
               	          (dTmpLong >= (initLongitude-5.0)) && (dTmpLong <= (initLongitude+5.0)) ) {
            		
            		closeItems.put(sItem, sLocation);
            	}
            	else {
            		
            		distantItems.put(sItem, sLocation);
                } 
            }            
        }
        catch (Throwable e){}
	}
    */
}
