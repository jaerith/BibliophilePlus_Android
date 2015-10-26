package com.firstdomino.BibliophilePlusDeluxe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

/**
 * This class provides a common set of functionality and data (i.e., the main cache) that supports 
 * the various Activity classes of the Android application.  Most of these routines are only called
 * during the initialization of the app.
 * 
 * (The argument could be made that it should have been a singleton class.) 
 */
public class BibliophileBase extends Activity {
		
	public static final boolean CONST_FULL_VERSION_FLAG = true; 
	
	public static final int MAX_LIST_SIZE = 25;
	public static final int MAX_ITEM_LEN  = 128;
	
	public static final int CONST_MORE_IDX = MAX_LIST_SIZE;
	
	public static final int CONST_NOOK_NORM_TXT_SIZE = 25;
	public static final int CONST_NOOK_MORE_TXT_SIZE = 35;
	
	public static final int CONST_OTHER_NORM_TXT_SIZE = 22;
	public static final int CONST_OTHER_MORE_TXT_SIZE = 28;
	
	public static final int normTextSize = 25;
	public static final int moreTextSize = 35;
	
	public static final String CONST_REVOKE_ITEMS_FILE = "Items.Revoke.dat"; 
	public static final String CONST_SUPP_FILE_SIBB    = "Sibb.Supplemental.dat";
	public static final String CONST_SUPP_FILE_GIBB    = "Gibb.Supplemental.dat";
	public static final String CONST_EAN_LIST_FILE     = "Eans.List.dat";	
	
	public static final String CONST_NONE_AVAILABLE = "None Available";
	public static final String CONST_MORE           = "More...";
	public static final String CONST_INFLUENCED_BY  = "Influenced By";
	public static final String CONST_BY_VAR         = " by Various";
	public static final String CONST_WRITTEN_BY_VAR = " written by Various";
	
	public static final String BOOK_INFO_DELIM      = " written by ";
	public static final String SONG_INFO_DELIM      = " performed by ";
	public static final String OTHER_INFO_DELIM     = " developed by ";
	
	public static final String CONST_WIKI_SITE      = "http://en.wikipedia.org/wiki/";
	public static final String CONST_LYRICS_SITE    = "http://www.lyricstime.com/search/?q=";
	
	public static final char   CONST_ACTIVITY_TYPE_SIBB          = 'S';
	public static final String CONST_ACTIVITY_SUBTYPE_SIBB_BY_BT = "SBT";
	public static final String CONST_ACTIVITY_SUBTYPE_SIBB_BY_ST = "SST";
	
	public static final char   CONST_ACTIVITY_TYPE_GIBB          = 'G';
	public static final String CONST_ACTIVITY_SUBTYPE_GIBB_BY_BT = "GBT";
	public static final String CONST_ACTIVITY_SUBTYPE_GIBB_BY_ST = "GST";
	
	public static final char   CONST_ACTIVITY_TYPE_AB            = 'A';
	public static final String CONST_ACTIVITY_SUBTYPE_AB_BY_BT   = "ABT";
	
	public static final char CONST_ACTIVITY_TYPE_QUOTES = 'Q';
	public static final char CONST_ACTIVITY_TYPE_LOC    = 'L';
	
	public static boolean defaultEnabled    = false;
	public static boolean altDefaultEnabled = false;
			
	public static HashMap<String,String> songTypeMap = new HashMap<String,String>();
	public static HashMap<String,String> gameTypeMap = new HashMap<String,String>();
	public static HashMap<String,String> bookTypeMap = new HashMap<String,String>();
		
	public static char   currResultsType       = '?';
	public static String currResultsSubType    = "?";
	public static String currResultsSubTypeVal = "?";
	
	public static ArrayList<String>                      bookCatalog       = new ArrayList<String>();           
	public static HashMap<String, BookOverviewItem>      bookOverviewItems = new HashMap<String, BookOverviewItem>();
	
	public static ArrayList<String>                      sibbItems         = new ArrayList<String>();
	public static HashMap<String, ArrayList<BiblioItem>> sibbItemsSongType = new HashMap<String, ArrayList<BiblioItem>>();
	public static HashMap<String, ArrayList<BiblioItem>> sibbItemsBookType = new HashMap<String, ArrayList<BiblioItem>>();
	
	public static ArrayList<String> sibbBookData   = new ArrayList<String>();
	public static ArrayList<String> sibbSongTypes  = new ArrayList<String>();
	public static ArrayList<String> sibbBookTypes  = new ArrayList<String>();
		
	public static ArrayList<String>                      gibbItems         = new ArrayList<String>();
	public static HashMap<String, ArrayList<BiblioItem>> gibbItemsGameType = new HashMap<String, ArrayList<BiblioItem>>();
	public static HashMap<String, ArrayList<BiblioItem>> gibbItemsBookType = new HashMap<String, ArrayList<BiblioItem>>();
	
	public static ArrayList<String> gibbBookData   = new ArrayList<String>();
	 
	public static ArrayList<String> foundItems = new ArrayList<String>();
	public static ArrayList<String> libItems   = new ArrayList<String>();
	
	public static ArrayList<String> quoteBookData  = new ArrayList<String>();
	
	public static ArrayList<String> revokedSibbItems = new ArrayList<String>();
	public static ArrayList<String> revokedGibbItems = new ArrayList<String>();
	
	public static HashMap<String, String> eanList  = new HashMap<String, String>();

	/**
	 * The standard initialization method used by an Activity. 
	 *
	 * @return Nothing.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		
		try {}
		catch (Throwable t) {
			Toast
			.makeText(this, "Request failed: "+t.toString(), 4000)
			.show();
		}		
	}
	
	/**
	 * This method will initialize the caches used by the main menus of several Activity classes.  
	 *
	 * @return Nothing.
	 */	
	public static void initialize() {
		
		if (songTypeMap.size() == 0) {
			songTypeMap.put("1", F1Activity.CONST_SIBB_STYPE_CLASSIC_ROCK);
			songTypeMap.put("2", F1Activity.CONST_SIBB_STYPE_ROCK);
			songTypeMap.put("3", F1Activity.CONST_SIBB_STYPE_POP);
			songTypeMap.put("4", F1Activity.CONST_SIBB_STYPE_HIP_HOP);
			songTypeMap.put("5", F1Activity.CONST_SIBB_STYPE_METAL);
			songTypeMap.put("6", F1Activity.CONST_SIBB_STYPE_GOSPEL);
			songTypeMap.put("7", F1Activity.CONST_SIBB_STYPE_ALT);
			songTypeMap.put("8", F1Activity.CONST_SIBB_STYPE_COMPOSITIONS);
			songTypeMap.put("9", F1Activity.CONST_SIBB_STYPE_COUNTRY);
			
			gameTypeMap.put("1", F1Activity.CONST_GIBB_GTYPE_CLASSIC);
			gameTypeMap.put("2", F1Activity.CONST_GIBB_GTYPE_FPS);
			gameTypeMap.put("3", F1Activity.CONST_GIBB_GTYPE_RTS);
			gameTypeMap.put("4", F1Activity.CONST_GIBB_GTYPE_RPG);
			gameTypeMap.put("5", F1Activity.CONST_GIBB_GTYPE_PUZZLE);
			gameTypeMap.put("6", F1Activity.CONST_GIBB_GTYPE_AA);
			gameTypeMap.put("7", F1Activity.CONST_GIBB_GTYPE_SIM);
	
			bookTypeMap.put("1", DashboardActivity.CONST_BOOK_TYPE_SCIFI);
			bookTypeMap.put("2", DashboardActivity.CONST_BOOK_TYPE_FICTION);
			bookTypeMap.put("3", DashboardActivity.CONST_BOOK_TYPE_HISTORY);
			bookTypeMap.put("4", DashboardActivity.CONST_BOOK_TYPE_POETRY);
			bookTypeMap.put("5", DashboardActivity.CONST_BOOK_TYPE_CLASSIC_LIT);
			bookTypeMap.put("6", DashboardActivity.CONST_BOOK_TYPE_ANCIENT_TXT);
			bookTypeMap.put("7", DashboardActivity.CONST_BOOK_TYPE_BIOGRAPHY);
			bookTypeMap.put("8", DashboardActivity.CONST_BOOK_TYPE_HUMOR);
			bookTypeMap.put("9", DashboardActivity.CONST_BOOK_TYPE_CHILD_LIT);
			bookTypeMap.put("10", DashboardActivity.CONST_BOOK_TYPE_NON_FICTION);
		}
    }
	
	/**
	 * Returns an array of totals for a given book type, in which each respective total is 
	 * the number of a song type influenced by a book of that type. (Used mainly by the F4Activity class.) 
	 *
	 * @param  psBookType       The type of books (classic fiction, science fiction, etc.)
	 * @param  poSongCategories The song categories for which we want the totals.
	 * @return The totals for the song categories. 
	 */			
	public static double[] getBookTypeStats(String psBookType, String[] poSongCategories)
	{
		int      nSongCatIdx = 0;
		double[] aStatValues = new double[poSongCategories.length];
		
		Set<String> oAllBooks = bookOverviewItems.keySet();
		
		HashMap<String, Integer> oSongCatStats = new HashMap<String, Integer>();
		
		for (String sTmpBook : oAllBooks) {
			
			BookOverviewItem oTmpOverviewItem = bookOverviewItems.get(sTmpBook);

			if (oTmpOverviewItem.sBookType == psBookType) {
				
		        for (String sTmpSongCategory : poSongCategories) {
		        	
		        	if (oTmpOverviewItem.sibbMembershipTypes.containsKey(sTmpSongCategory)) {
	
	                    if (!oSongCatStats.containsKey(sTmpSongCategory))
	                    	oSongCatStats.put(sTmpSongCategory, new Integer(0));
	                    
	                    Integer nTmpSongCatCount = oTmpOverviewItem.sibbMembershipTypes.get(sTmpSongCategory);
	                    
	                    Integer nTmpSongCatTotal = oSongCatStats.get(sTmpSongCategory);
	                    
	                    nTmpSongCatTotal += nTmpSongCatCount;
	                    
	                    oSongCatStats.put(sTmpSongCategory, nTmpSongCatTotal);
		        	}
		        }
			}
		}
		
		nSongCatIdx = 0;

        for (String sTmpSongCategory : poSongCategories) {

        	if (oSongCatStats.keySet().contains(sTmpSongCategory)) {
        		
        	    Integer nTmpSongCatTotal = oSongCatStats.get(sTmpSongCategory);
        	
        	    aStatValues[nSongCatIdx] = nTmpSongCatTotal;
        	}
        	else
        		aStatValues[nSongCatIdx] = 0;

        	++nSongCatIdx;
        }
		
        return aStatValues;
	}

	/**
	 * Returns an array of totals for a given game type, in which each respective total is 
	 * the number of a game type influenced by a book of that type. (Used mainly by the F4Activity class.) 
	 *
	 * @param  psGameType       The type of games (RPG, FPS, etc.)
	 * @param  poBookCategories The book categories for which we want the totals.
	 * @return The totals for the book categories. 
	 */				
	public static double[] getGameTypeStats(String psGameType, String[] poBookCategories)
	{
		int      nBookCatIdx = 0;
		double[] aStatValues = new double[poBookCategories.length];
		
		Set<String>       oAllBooks  = bookOverviewItems.keySet();
		ArrayList<String> alBookCats = new ArrayList<String>();
		
		for (nBookCatIdx = 0; nBookCatIdx < poBookCategories.length; ++nBookCatIdx) {
		    alBookCats.add( poBookCategories[nBookCatIdx] );
		}
		
		HashMap<String, Integer> oBookCatStats = new HashMap<String, Integer>();
		
		for (String sTmpBook : oAllBooks) {
			
			BookOverviewItem oTmpOverviewItem = bookOverviewItems.get(sTmpBook);

			if (oTmpOverviewItem.gibbMembershipTypes.keySet().contains(psGameType)) {
				
				if (alBookCats.contains(oTmpOverviewItem.sBookType)) {
					
                    if (!oBookCatStats.containsKey(oTmpOverviewItem.sBookType))
                    	oBookCatStats.put(oTmpOverviewItem.sBookType, new Integer(0));

                    Integer nTmpBookCatTotal = oBookCatStats.get(oTmpOverviewItem.sBookType);

                    nTmpBookCatTotal += 1;
 
                    oBookCatStats.put(oTmpOverviewItem.sBookType, nTmpBookCatTotal);					
			    }				
			}
		}
		
		nBookCatIdx = 0;

        for (String sTmpBookCategory : poBookCategories) {

        	if (oBookCatStats.keySet().contains(sTmpBookCategory)) {
        	    Integer nTmpBookCatTotal = oBookCatStats.get(sTmpBookCategory);
        	
        	    aStatValues[nBookCatIdx] = nTmpBookCatTotal;
        	}
        	else
        		aStatValues[nBookCatIdx] = 0;

        	++nBookCatIdx;
        }
		
        return aStatValues;
	}

	/**
	 * Returns the top 5 books (and the associated number of games) that have influenced games of a certain type. 
	 * (Used mainly by the F4Activity class.) 
	 *
	 * @param  psGameType The type of game for which we want the top 5 influential books.
	 * @return The top 5 books and their totals of games influenced. 
	 */					
	public static HashMap<String,Integer> getGameTypeTop5(String psGameType)
	{
		HashMap<String,Integer>       oAllResults    = new HashMap<String,Integer>();
		HashMap<String,Integer>       oTop5Results   = new HashMap<String,Integer>();
		LinkedHashMap<String,Integer> oSortedResults = new LinkedHashMap<String,Integer>();
				
		Set<String> oAllBooks = bookOverviewItems.keySet();
		
		for (String sTmpBook : oAllBooks) {
			
			BookOverviewItem oTmpOverviewItem = bookOverviewItems.get(sTmpBook);

			if (oTmpOverviewItem.gibbMembershipTypes.keySet().contains(psGameType)) {
				
				Integer nTmpGameCatCount = 
						oTmpOverviewItem.gibbMembershipTypes.get(psGameType);
				
                if (!oAllResults.containsKey(sTmpBook))
                    oAllResults.put(sTmpBook, new Integer(0));

                Integer nTmpBookTotal = oAllResults.get(sTmpBook);

                nTmpBookTotal += nTmpGameCatCount;
 
                oAllResults.put(sTmpBook, nTmpBookTotal);
			}
		}
		
		oSortedResults = sortByValue(oAllResults);

		int nKeyIdx     = -1;
		int nKeysLength = oSortedResults.keySet().size();
		
        for (String key : oSortedResults.keySet()) {
        	
        	++nKeyIdx;
        	if (nKeyIdx < (nKeysLength-5))
        	     continue;
        	
            oTop5Results.put(key, oSortedResults.get(key));
            
            if (oTop5Results.size() >= 5) {
            	break;
            }            
        }
        
        return oTop5Results;
	}

	/**
	 * Returns an array of totals for a given song type, in which each respective total is 
	 * the number of a song type influenced by a book of that type. (Used mainly by the F4Activity class.) 
	 *
	 * @param  psSongType       The type of song (rock, rap, etc.)
	 * @param  poBookCategories The book categories for which we want the totals.
	 * @return The totals for the book categories. 
	 */				
	public static double[] getSongTypeStats(String psSongType, String[] poBookCategories)
	{
		int      nBookCatIdx = 0;
		double[] aStatValues = new double[poBookCategories.length];
		
		Set<String>       oAllBooks  = bookOverviewItems.keySet();
		ArrayList<String> alBookCats = new ArrayList<String>();
		
		for (nBookCatIdx = 0; nBookCatIdx < poBookCategories.length; ++nBookCatIdx) {
		    alBookCats.add( poBookCategories[nBookCatIdx] );
		}
		
		HashMap<String, Integer> oBookCatStats = new HashMap<String, Integer>();
		
		for (String sTmpBook : oAllBooks) {
			
			BookOverviewItem oTmpOverviewItem = bookOverviewItems.get(sTmpBook);

			if (oTmpOverviewItem.sibbMembershipTypes.keySet().contains(psSongType)) {
				
				if (alBookCats.contains(oTmpOverviewItem.sBookType)) {
					
                    if (!oBookCatStats.containsKey(oTmpOverviewItem.sBookType))
                    	oBookCatStats.put(oTmpOverviewItem.sBookType, new Integer(0));

                    Integer nTmpBookCatTotal = oBookCatStats.get(oTmpOverviewItem.sBookType);

                    nTmpBookCatTotal += 1;
 
                    oBookCatStats.put(oTmpOverviewItem.sBookType, nTmpBookCatTotal);					
			    }				
			}
		}
		
		nBookCatIdx = 0;

        for (String sTmpBookCategory : poBookCategories) {

        	if (oBookCatStats.keySet().contains(sTmpBookCategory)) {
        	    Integer nTmpBookCatTotal = oBookCatStats.get(sTmpBookCategory);
        	
        	    aStatValues[nBookCatIdx] = nTmpBookCatTotal;
        	}
        	else
        		aStatValues[nBookCatIdx] = 0;

        	++nBookCatIdx;
        }
		
        return aStatValues;
	}	

	/**
	 * Returns the top 5 books (and the associated number of songs) that have influenced songs of a certain type. 
	 * (Used mainly by the F4Activity class.) 
	 *
	 * @param  psSongType The type of song for which we want the top 5 influential books.
	 * @return The top 5 books and their totals of songs influenced. 
	 */						
	public static HashMap<String,Integer> getSongTypeTop5(String psSongType)
	{
		HashMap<String,Integer>       oAllResults    = new HashMap<String,Integer>();
		HashMap<String,Integer>       oTop5Results   = new HashMap<String,Integer>();
		LinkedHashMap<String,Integer> oSortedResults = new LinkedHashMap<String,Integer>();
				
		Set<String> oAllBooks = bookOverviewItems.keySet();
		
		for (String sTmpBook : oAllBooks) {
			
			BookOverviewItem oTmpOverviewItem = bookOverviewItems.get(sTmpBook);

			if (oTmpOverviewItem.sibbMembershipTypes.keySet().contains(psSongType)) {
				
				Integer nTmpSongCatCount = 
						oTmpOverviewItem.sibbMembershipTypes.get(psSongType);
				
                if (!oAllResults.containsKey(sTmpBook))
                    oAllResults.put(sTmpBook, new Integer(0));

                Integer nTmpBookTotal = oAllResults.get(sTmpBook);

                nTmpBookTotal += nTmpSongCatCount;
 
                oAllResults.put(sTmpBook, nTmpBookTotal);
			}
		}
		
		oSortedResults = sortByValue(oAllResults);

		int nKeyIdx     = -1;
		int nKeysLength = oSortedResults.keySet().size();
		
        for (String key : oSortedResults.keySet()) {
        	
        	++nKeyIdx;
        	if (nKeyIdx < (nKeysLength-5))
        	     continue;
        	
            oTop5Results.put(key, oSortedResults.get(key));
            
            if (oTop5Results.size() >= 5) {
            	break;
            }            
        }
        
        return oTop5Results;
	}

	/**
	 * Initializes the main cache of data that drives the application, 
	 * loading it from the files in the '/res/raw' folder. 
	 *
	 * @param  oHome       The main Activity launched when the Android application starts up.
	 * @param  bLimitedRun The indicator for whether we should limit the load of all quote and location data.
	 * @return Nothing. 
	 */
	public static void parseBiblioData(HomeActivity oHome, Boolean bLimitedRun) {
				
		int nRowCount = 0;
		int nRowMax   = 0;
		
		Random generator = new Random();
		
		if (bLimitedRun) {
			nRowMax = 25;
	    }
		else {
			nRowMax = 5000;
		}
						
		initialize();
		
        try{
            InputStream inRevokedItemsData = oHome.openFileInput(CONST_REVOKE_ITEMS_FILE);
            
            parseRevokedItemsData(oHome, inRevokedItemsData);
        }
        catch (Throwable e){}
		
        try{
        	// Parse data loaded with the application
            InputStream inCoreSibbData = 
            		oHome.getResources().openRawResource(R.raw.songsinspiredbybooks);

            parseSibbData(oHome, inCoreSibbData, false);
            
            // Parse new data downloaded from the server
            InputStream inSuppSibbData = oHome.openFileInput(CONST_SUPP_FILE_SIBB);
            
            parseSibbData(oHome, inSuppSibbData, true);
        }
        catch (Throwable e){}
        
        try{
        	// Parse data loaded with the application
            InputStream inCoreGibbData = 
            		oHome.getResources().openRawResource(R.raw.gamesinspiredbybooks);                        
            
            parseGibbData(oHome, inCoreGibbData);

            // Parse new data downloaded from the server
            InputStream inSuppGibbData = oHome.openFileInput(CONST_SUPP_FILE_GIBB);
            
            parseGibbData(oHome, inSuppGibbData);
        }        
        catch (Throwable e){}
        
        try{
        	
            InputStream in = 
            		oHome.getResources().openRawResource(R.raw.quotes);
            
            String          sTmpLine   = null;
            String          sTmpValue  = null;
            String          sBook      = null;
            String          sAuthor    = null;
            String          sTmpQuote  = null;
            String          sBookData  = null;
            String          sBookLink  = null;
            BufferedReader  reader     = null;
            StringTokenizer tokenizer  = null;
            
            reader = new BufferedReader(new InputStreamReader(in));
                        
            nRowCount = 0;
            for (nRowCount = 0; 
                 (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
                 ++nRowCount) {
            	
            	sBook = sAuthor = sBookData = sBookLink = sTmpQuote = "";
            	            	            	
            	tokenizer = new StringTokenizer(sTmpLine, "|");
            	
            	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
            		
                	sTmpValue = tokenizer.nextToken();
            		
            		if (i == 1) {
            			sBook = sTmpValue;
            			
            			sBookLink = sBook;
            			
            			sBookLink = sBookLink.replaceAll("the ", "The ");
            			sBookLink = sBookLink.replaceAll(" ", "_");
            			sBookLink = sBookLink.replaceAll("'", "%27");
            			sBookLink = sBookLink.replaceAll("(Series)", "(series)");
            			
            			sBookLink = CONST_WIKI_SITE + sBookLink;            			
            	    }
            		else if (i == 2) {
            			sAuthor = sTmpValue;
            	    }
            		else if (i == 4) {
            			sTmpQuote = sTmpValue;
            			
            			if (sTmpQuote == "NULL") {
            				sTmpQuote = "";
            		    }
            	    }
                }
            	
        		if (sTmpQuote.length() > 0) {
        			        			
        			sBookData = sBook + BOOK_INFO_DELIM + sAuthor;        			        		
        			
	            	quoteBookData.add("\"" + sTmpQuote + "\"\n\n" + sBookData + "\n");
	            	
            	    BiblioItem oNewQuoteItem = 
            				new BiblioItem(sBookData, sTmpQuote);
	            	
	            	if (!bookOverviewItems.containsKey(sBookData))
	            		bookOverviewItems.put(sBookData, new BookOverviewItem(sBookData));
	            		
	            	BookOverviewItem oBookOverviewFoundItem = bookOverviewItems.get(sBookData);
	            	
	            	oBookOverviewFoundItem.quoteItems.add(oNewQuoteItem);
        		}        		
            }
        }
        catch (Throwable e) {
        	
        	String sMsg = e.toString();
        	        	        	
        	System.out.println(sMsg);        	        	
        }
        
        try{
            InputStream in = 
            		oHome.getResources().openRawResource(R.raw.locationsinbooks);
            
            int             nParanIdx  = -1;
            String          sTmpLine   = null;
            String          sTmpValue  = null;
            String          sBook      = null;
            String          sTmpBook   = null;
            String          sAuthor    = null;
            String          sLocation  = null;
            String          sBookData  = null;
            String          sBookLink  = null;
            String          sOtherLink = null;
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
            		
            		if (i == 1) {
            			sBook = sTmpValue;

            			sTmpBook = sBook;
            			
            			sTmpBook = sTmpBook.replaceAll("The Story of ", "");
            			sTmpBook = sTmpBook.replaceAll("The Legend of ", "");
            			sTmpBook = sTmpBook.replaceAll("the ", "The ");
            			sTmpBook = sTmpBook.replaceAll(" ", "_");
            			sTmpBook = sTmpBook.replaceAll("'", "%27");
            			sTmpBook = sTmpBook.replaceAll("(Series)", "(series)");
            			sTmpBook = sTmpBook.trim();
            			
            			sBookLink = CONST_WIKI_SITE + sTmpBook;
            	    }
            		else if (i == 2) {
            			sAuthor = sTmpValue;
            	    }
            		else if (i == 3) {
            			sLocation = sTmpValue;
            			
    					if ((nParanIdx = sLocation.indexOf("(")) > 0) {
							sLocation = sLocation.substring(0, nParanIdx).trim();
					    }
						else {
							sLocation = sLocation.trim();
					    }

    					sOtherLink = sLocation;
            			sOtherLink = sOtherLink.replaceAll(" ", "_");
            			sOtherLink = sOtherLink.replaceAll("'", "%27");
    					
    					sOtherLink = CONST_WIKI_SITE + sLocation;
            	    }
                }
            	
            	sBookData = sBook + BOOK_INFO_DELIM + sAuthor;
            	
            	if (!libItems.contains(sBookData))
            	    libItems.add(sBookData);
            	
        	    BiblioItem oNewLibItem = 
        				new BiblioItem(sBookData, sLocation);
            	            	
            	if (!bookOverviewItems.containsKey(sBookData))
            		bookOverviewItems.put(sBookData, new BookOverviewItem(sBookData));
            		
            	BookOverviewItem oBookOverviewFoundItem = bookOverviewItems.get(sBookData);
            	
            	oBookOverviewFoundItem.locItems.add(oNewLibItem);
            }
            
        }
        catch (Throwable e){}
        
        try {
        	
        	foundItems.add(BibliophileBase.CONST_NONE_AVAILABLE);
	    }
        catch (Throwable e){}
        
        try{
            InputStream inEanListData = oHome.openFileInput(CONST_EAN_LIST_FILE);
            
            parseEanListData(oHome, inEanListData);
        }
        catch (Throwable e){}
    }

	/**
	 * Parses and loads a file that contains a mapping between books and Barnes & Noble EAN identifiers.
	 * This data had been intended to be used by the F1Activity and F2Activity, allowing users the chance
	 * to buy a mentioned book from a launched B&N app.
	 *
	 * @param  oHome                The main Activity launched when the Android application starts up.
	 * @param  poEanListInputStream The handle to the file with the Book->EAN mapping.
	 * @return Nothing. 
	 */
	public static void parseEanListData(HomeActivity oHome, InputStream poEanListInputStream)
	{
		int             nRowCount     = 0;
		int             nRowMax       = 100000;
		String          sTitle        = "";
		String          sEan          = "";
        String          sTmpLine      = null;
        String          sTmpValue     = null;
        BufferedReader  reader        = null;
        StringTokenizer tokenizer     = null;
        
        eanList.clear();
        
        try {
	        reader = new BufferedReader(new InputStreamReader(poEanListInputStream));
	                    
	        nRowCount = 0;
	        for (nRowCount = 0; 
	             (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
	             ++nRowCount) {
	        	
	        	tokenizer = new StringTokenizer(sTmpLine, "|");
	        	
	        	sTitle = sEan = "";
	        	
	        	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
	        		
	            	sTmpValue = tokenizer.nextToken();
	        		
	        		if (i == 0){
	        			sTitle = sTmpValue;
	        		}
	        		else if (i == 1) {
	        			sEan = sTmpValue;
	          	    }
	        	}
	        	
	        	if ((sTitle.length() > 0) && (sEan.length() > 0)) {
	        		eanList.put(sTitle, sEan);
	            }
	        }
        }
        catch (IOException e) {
        	
			new AlertDialog.Builder(oHome)
			.setTitle("DEBUG")
			.setMessage("ERROR!  Failed to read EAN List file:\n(" + e + ")...")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
        }
	}

	/**
	 * Parses and loads the raw data file that contains trivia information about games that have
	 * been influenced by books.  This raw data file is found in the '/res/raw' folder.
	 *
	 * @param  oHome             The main Activity launched when the Android application starts up.
	 * @param  poGibbInputStream The handle to the file with trivia about games influenced by books.
	 * @return Nothing. 
	 */	
	public static void parseGibbData(HomeActivity oHome, InputStream poGibbInputStream)
	{
		int             nRowCount  = 0;
		int             nRowMax    = 100000;
        String          sTmpLine   = null;
        String          sTmpValue  = null;
        String          sGame      = null;
        String          sStudio    = null;
        String          sBook      = null;
        String          sTmpBook   = null;
        String          sAuthor    = null;
        String          sBookData  = null;
        String          sBookLink  = null;
        String          sBookType  = null;
        String          sGameType  = null;
        String          sGameLink  = null;
        String          sItem      = null;
        BufferedReader  reader     = null;
        StringTokenizer tokenizer  = null;
        
        try { 
        
	        reader = new BufferedReader(new InputStreamReader(poGibbInputStream));
	                    
	        nRowCount = 0;
	        for (nRowCount = 0; 
	             (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
	             ++nRowCount) {
	        	            	            	
	        	tokenizer = new StringTokenizer(sTmpLine, "|");
	        	
	        	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
	        		
	            	sTmpValue = tokenizer.nextToken();
	        		
	        		if (i == 1){
	        			sGame = sTmpValue;
	        			
	        			sGameLink = sGame; 
	        					
	        			sGameLink = sGameLink.replaceAll("the ", "The ");
	        			sGameLink = sGameLink.replaceAll(" ", "_");
	        			sGameLink = sGameLink.replaceAll("'", "%27");
	        			sGameLink = sGameLink.replaceAll("(Series)", "(series)");
	        			
	        			sGameLink = CONST_WIKI_SITE + sGameLink;
	
	        		}
	        		else if (i == 2) {
	        			sStudio = sTmpValue;
	          	    }
	        		else if (i == 4) {
	        			sBook = sTmpValue;
	
	        			sTmpBook = sBook;
	        			
	        			sTmpBook = sTmpBook.replaceAll("The Story of ", "");
	        			sTmpBook = sTmpBook.replaceAll("The Legend of ", "");
	        			sTmpBook = sTmpBook.replaceAll("the ", "The ");
	        			sTmpBook = sTmpBook.replaceAll(" ", "_");
	        			sTmpBook = sTmpBook.replaceAll("'", "%27");
	        			sTmpBook = sTmpBook.replaceAll("(Series)", "(series)");
	        			
	        			sBookLink = CONST_WIKI_SITE + sTmpBook;
	        	    }
	        		else if (i == 5) {
	        			sAuthor = sTmpValue;
	        	    }
	        		else if (i == 8) {
	        			if ( (sTmpValue != null) && (sTmpValue != "NULL") ) {
	        			    sBookType = bookTypeMap.get(sTmpValue);
	        			}
	        			else {
	        				sBookType = DashboardActivity.CONST_BOOK_TYPE_FICTION;
	        		    }
	        	    }
	        		else if (i == 9) {
	        			if ( (sTmpValue != null) && (sTmpValue != "NULL") ) {
	        			    sGameType = gameTypeMap.get(sTmpValue);
	        			    
	        			    if ( (sGameType == null) || (sGameType == "NULL") ) {
	        			    	sGameType = F1Activity.CONST_GIBB_GTYPE_RPG;
	        			    }
	        			}
	        			else {
	        				sGameType = F1Activity.CONST_GIBB_GTYPE_RPG;
	        		    }
	        	    }
	            }
	        	
	        	sBookData = sBook + BOOK_INFO_DELIM + sAuthor;	        	
	        	sItem     = sGame + OTHER_INFO_DELIM + sStudio;
	        	
	        	if (!revokedGibbItems.contains(sItem)) 
	        	{		        	
		        	gibbBookData.add(sBook + BOOK_INFO_DELIM + sAuthor + "\n");
		        	
		        	gibbItems.add(sItem);
		        	
		    	    BiblioItem oNewGibbItem = 
		    				new BiblioItem(sBookData, sItem);
		        	
		        	if (sGameType != "") {
		
		        		if ( !gibbItemsGameType.containsKey(sGameType) ) {
		        			gibbItemsGameType.put(sGameType, new ArrayList<BiblioItem>());
		        	    }
		
		        		ArrayList<BiblioItem> oTmpGameTypeList = gibbItemsGameType.get(sGameType);
		        		            		
		        		oTmpGameTypeList.add(oNewGibbItem);
		        	}
		        	
		        	if (sBookType != "") {
		
		        		if ( !gibbItemsBookType.containsKey(sBookType) ) {
		        			gibbItemsBookType.put(sBookType, new ArrayList<BiblioItem>());
		        	    }
		        		
		        		ArrayList<BiblioItem> oTmpBookTypeList = gibbItemsBookType.get(sBookType);
		        		
		        		oTmpBookTypeList.add(oNewGibbItem);
		        	}
		        	
		        	if (!bookOverviewItems.containsKey(sBookData))
		        		bookOverviewItems.put(sBookData, new BookOverviewItem(sBookData));
		        		
		        	BookOverviewItem oBookOverviewFoundItem = bookOverviewItems.get(sBookData);
		        	
		        	oBookOverviewFoundItem.gibbItems.add(oNewGibbItem);
		        	
		        	if (!oBookOverviewFoundItem.gibbMembershipTypes.containsKey(sGameType))
		        		oBookOverviewFoundItem.gibbMembershipTypes.put(sGameType, new Integer(1));
		        	else
		        	{
		                Integer nGibbMembershipCount = 
		                		oBookOverviewFoundItem.gibbMembershipTypes.get(sGameType);
		                
		                nGibbMembershipCount += 1;
		                
		                oBookOverviewFoundItem.gibbMembershipTypes.put(sGameType, nGibbMembershipCount);
		        	}
	        	}
	        }
        }
        catch (IOException e) {
        	
			new AlertDialog.Builder(oHome)
			.setTitle("DEBUG")
			.setMessage("ERROR!  Failed to read supplemental GIBB file:\n(" + e + ")...")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
        }
	}

	/**
	 * Parses and loads a data file that flags certain data in the '/res/raw' as outdated or incorrect,  
	 * which in turn removes it from the cache.
	 * 
	 * This data file is downloaded from a server whenever the app starts.
	 *
	 * @param  oHome             The main Activity launched when the Android application starts up.
	 * @param  poGibbInputStream The handle to the file with mentions of the revoked trivia items.
	 * @return Nothing. 
	 */	
	public static void parseRevokedItemsData(HomeActivity oHome, InputStream poRevokedInputStream)
	{
		int             nRowCount     = 0;
		int             nRowMax       = 100000;
		String          sSibbType     = "";
		String          sGibbType     = "";
        String          sTmpLine      = null;
        String          sTmpValue     = null;
        String          sTmpType      = null;
        String          sTmpTypeValue = null;
        String          sItem         = null;
        BufferedReader  reader        = null;
        StringTokenizer tokenizer     = null;
        
        sSibbType += CONST_ACTIVITY_TYPE_SIBB;
        sGibbType += CONST_ACTIVITY_TYPE_GIBB;
        
        revokedSibbItems.clear();

        try {
	        reader = new BufferedReader(new InputStreamReader(poRevokedInputStream));
	                    
	        nRowCount = 0;
	        for (nRowCount = 0; 
	             (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
	             ++nRowCount) {
	        	
	        	tokenizer = new StringTokenizer(sTmpLine, "|");
	        	
	        	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
	        		
	            	sTmpValue = tokenizer.nextToken();
	        		
	        		if (i == 0){
	        			sTmpType = sTmpValue;
	        		}
	        		else if (i == 1) {
	        			sTmpTypeValue = sTmpValue;
	          	    }
	        	}
	        	
        		if ( sTmpType.equalsIgnoreCase(sSibbType) ) {
        			
        			if (!revokedSibbItems.contains(sTmpTypeValue)) {
        				revokedSibbItems.add(sTmpTypeValue);
        		    }
        	    }
        		else if ( sTmpType.equalsIgnoreCase(sGibbType) ) {
        			
        			if (!revokedGibbItems.contains(sTmpTypeValue)) {
        				revokedGibbItems.add(sTmpTypeValue);
        		    }
        	    }	
	        }
        }
        catch (IOException e) {
        	
			new AlertDialog.Builder(oHome)
			.setTitle("DEBUG")
			.setMessage("ERROR!  Failed to read Revoked Items file:\n(" + e + ")...")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
        }		
	}

	/**
	 * Parses and loads the raw data file that contains trivia information about songs that have
	 * been influenced by books.  This raw data file is found in the '/res/raw' folder.
	 *
	 * @param  oHome              The main Activity launched when the Android application starts up.
	 * @param  poSibbInputStream  The handle to the file with trivia about songs influenced by books.
	 * @param  bStoreToMostRecent Indicates whether qualified data should be stored in a 'Most Recent' list within the F1Activity.
	 * @return Nothing. 
	 */		
	public static void parseSibbData(HomeActivity oHome, InputStream poSibbInputStream, boolean bStoreToMostRecent)
	{
		int             nRowCount  = 0;
		int             nRowMax    = 100000;
        String          sTmpLine   = null;
        String          sTmpValue  = null;
        String          sSong      = null;
        String          sPerformer = null;
        String          sBook      = null;
        String          sAuthor    = null;
        String          sSongType  = null;
        String          sBookType  = null;
        String          sTmpBook   = null;
        String          sBookData  = null;
        String          sBookLink  = null;
        String          sMusicLink = null;
        String          sItem      = null;
        BufferedReader  reader     = null;
        StringTokenizer tokenizer  = null;
        
        LinkedList<BiblioItem> tmpStorage = new LinkedList<BiblioItem>();

        try {
	        reader = new BufferedReader(new InputStreamReader(poSibbInputStream));
	                    
	        nRowCount = 0;
	        for (nRowCount = 0; 
	             (nRowCount < nRowMax) && ((sTmpLine = reader.readLine()) != null);
	             ++nRowCount) {
	        		        	
	        	tokenizer = new StringTokenizer(sTmpLine, "|");
	        	
	        	for (int i = 0; tokenizer.hasMoreTokens(); ++i) {
	        		
	            	sTmpValue = tokenizer.nextToken();
	        		
	        		if (i == 2){
	        			sSong = sTmpValue;
	        		}
	        		else if (i == 3) {
	        			sPerformer = sTmpValue;
	          	    }
	        		else if (i == 5) {
	        			sBook = sTmpValue;
	        			
	        			sTmpBook = sBook;
	        			
	        			sTmpBook = sTmpBook.replaceAll("The Story of ", "");
	        			sTmpBook = sTmpBook.replaceAll("The Legend of ", "");
	        			sTmpBook = sTmpBook.replaceAll("the ", "The ");
	        			sTmpBook = sTmpBook.replaceAll(" ", "_");
	        			sTmpBook = sTmpBook.replaceAll("'", "%27");
	        			sTmpBook = sTmpBook.replaceAll("(Series)", "(series)");
	        			
	        			sBookLink = CONST_WIKI_SITE + sTmpBook;
	        	    }
	        		else if (i == 6) {
	        			sAuthor = sTmpValue;
	        	    }
	        		else if (i == 9) {
	        			if ( (sTmpValue != null) && (sTmpValue != "NULL") ) {
	        			    sBookType = bookTypeMap.get(sTmpValue);
	        			}
	        			else {
	        				sBookType = DashboardActivity.CONST_BOOK_TYPE_FICTION;
	        		    }
	        	    }
	        		else if (i == 10) {
	        			if ( (sTmpValue != null) && (sTmpValue != "NULL") ) {
	        			    sSongType = songTypeMap.get(sTmpValue);
	        			    
	        			    if ( (sSongType == null) || (sSongType == "NULL") ) {
	        			    	sSongType = F1Activity.CONST_SIBB_STYPE_ROCK;
	        			    }
	        			}
	        			else {
	        				sSongType = F1Activity.CONST_SIBB_STYPE_ROCK;
	        		    }
	        	    }
	            }
	        	            	
	        	sItem = sSong + SONG_INFO_DELIM + sPerformer;
	        	
	        	if (!revokedSibbItems.contains(sItem)) 
	        	{	        	
		        	sibbItems.add(sItem);
		        	            	
		        	sibbSongTypes.add(sSongType);
		        	sibbBookTypes.add(sBookType);
		        	
		        	sBookData = sBook + BOOK_INFO_DELIM + sAuthor;
		        	sibbBookData.add(sBookData);
		        	
		    	    BiblioItem oNewSibbItem = 
		    				new BiblioItem(sBookData, sItem);
		        	
		        	if (sSongType != "") {
		
		        		if ( !sibbItemsSongType.containsKey(sSongType) ) {
		        			sibbItemsSongType.put(sSongType, new ArrayList<BiblioItem>());
		        	    }
		
		        		ArrayList<BiblioItem> oTmpSongTypeList = sibbItemsSongType.get(sSongType);
		        		            		
		        		oTmpSongTypeList.add(oNewSibbItem);
		        	}
		        	
		        	if (sBookType != "") {
		
		        		if ( !sibbItemsBookType.containsKey(sBookType) ) {
		        			sibbItemsBookType.put(sBookType, new ArrayList<BiblioItem>());
		        	    }
		        		
		        		ArrayList<BiblioItem> oTmpBookTypeList = sibbItemsBookType.get(sBookType);
		        		
		        		oTmpBookTypeList.add(oNewSibbItem);
		        	}
		        	
		        	if (bStoreToMostRecent) {
		        		tmpStorage.add(oNewSibbItem);
	        	    }
		        	
		        	if (!bookOverviewItems.containsKey(sBookData)) {		        		
		        		bookOverviewItems.put(sBookData, new BookOverviewItem(sBookData));
		        	}
		        	
		        	if (!bookCatalog.contains(bookCatalog)) {
		        		bookCatalog.add(sBookData);
		        	}
		        		
		        	BookOverviewItem oBookOverviewFoundItem = bookOverviewItems.get(sBookData);
		        	
		        	oBookOverviewFoundItem.sBook   = sBook;
		        	oBookOverviewFoundItem.sAuthor = sAuthor; 
		        	
		        	if ((oBookOverviewFoundItem.sBookType == null) || (oBookOverviewFoundItem.sBookType.length() == 0))
		        	    oBookOverviewFoundItem.sBookType = sBookType;
		
		        	oBookOverviewFoundItem.sibbItems.add(oNewSibbItem);
		        	
		        	if (!oBookOverviewFoundItem.sibbMembershipTypes.containsKey(sSongType))
		        		oBookOverviewFoundItem.sibbMembershipTypes.put(sSongType, new Integer(1));
		        	else
		        	{
		                Integer nSibbMembershipCount = 
		                		oBookOverviewFoundItem.sibbMembershipTypes.get(sSongType);
		                
		                nSibbMembershipCount += 1;
		                
		                oBookOverviewFoundItem.sibbMembershipTypes.put(sSongType, nSibbMembershipCount);
		        	}
	        	}
	        }

        	if (bStoreToMostRecent) {
        		
        		ArrayList<BiblioItem> oMostRecentSibbList = 
        				new ArrayList<BiblioItem>();
        		
        		while (tmpStorage.size() > 50) {
        			tmpStorage.removeFirst();
        	    }
        		
        		oMostRecentSibbList.addAll(tmpStorage);
        		
    			sibbItemsSongType.put(F1Activity.CONST_SIBB_STYPE_RECENT, 
    					              oMostRecentSibbList);
        	}

        }
        catch (IOException e) {
        	
			new AlertDialog.Builder(oHome)
			.setTitle("DEBUG")
			.setMessage("ERROR!  Failed to read supplemental SIBB file:\n(" + e + ")...")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
        }
	}

	/**
	 * General purpose function that will sort a Map's contents so that they're ordered as a LinkedHashMap.
	 *
	 * @param  map     The map that should be sorted and then repackaged into a LinkedHashMap.
	 * @return The sorted Map repackaged as a LinkedHashMap. 
	 */	
	static LinkedHashMap sortByValue(Map map) {
		
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	              .compareTo(((Map.Entry) (o2)).getValue());
	          }
	     });

	    LinkedHashMap result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}

	/**
	 * Updates the cache by removing those trivia items that have been marked as 'Revoked'.
	 *
	 * @param  oHome    The main Activity launched when the Android application starts up.
	 * @return Nothing. 
	 */	
    public static void updateData(HomeActivity oHome) {
    	
        try{
            InputStream inRevokedItemsData = oHome.openFileInput(CONST_REVOKE_ITEMS_FILE);
            
            parseRevokedItemsData(oHome, inRevokedItemsData);
        }
        catch (Throwable e){}		
    }
}