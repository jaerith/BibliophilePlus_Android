package com.firstdomino.BibliophilePlusDeluxe;

/**
 * This class provides the generic data structure for a Bibliophile record, where:
 *     'sBookData' holds raw data about the general information about the book
 *     'sOtherData' holds raw data about trivia info of the book (relating to music, games, etc.)
 *     'sMiscData' is currently unused
 *     
 * for each book.
 */
public class BiblioItem
{
	public String sBookData;
	public String sOtherData;
	public String sMiscData;
	
	public BiblioItem(String psBookData, String psOtherData) 
	{
	    sBookData  = psBookData;
	    sOtherData = psOtherData;
	    sMiscData  = null;
	}
	
	public BiblioItem(String psBookData, String psOtherData, String psMiscData)
	{
	    sBookData  = psBookData;
	    sOtherData = psOtherData;
	    sMiscData  = psMiscData;
	}
	
	/**
	 * Generates a Wikipedia hyperlink about the record's book. 
	 *
	 * @return The hyperlink to an informative page about the actual book.
	 */
	public String getBL()
	{
		String sTmpBook  = "";
		String sBookLink = "";
		
		try {
			
			if (sBookData.contains(BibliophileBase.BOOK_INFO_DELIM)) {
				
				sTmpBook = sBookData.split(BibliophileBase.BOOK_INFO_DELIM)[0];
				
				sTmpBook = sTmpBook.replaceAll("The Story of ", "");
				sTmpBook = sTmpBook.replaceAll("The Legend of ", "");
				sTmpBook = sTmpBook.replaceAll("the ", "The ");
				sTmpBook = sTmpBook.replaceAll(" ", "_");
				sTmpBook = sTmpBook.replaceAll("'", "%27");
				sTmpBook = sTmpBook.replaceAll("(Series)", "(series)");
				sTmpBook = sTmpBook.trim();
			}
		}
		catch (Throwable t)
		{}
		
		sBookLink = BibliophileBase.CONST_WIKI_SITE + sTmpBook;
		
	    return sBookLink; 	    		
	}

	/**
	 * Generates a hyperlink about trivia related to the book. 
	 *
	 * @return The hyperlink to an informative page about the trivia.
	 */	
	public String getOL()
	{
	    String sOtherLink = "";
	    
	    sOtherLink = sOtherData;
	    
	    try {
	    	
		    if (sOtherData.contains(BibliophileBase.SONG_INFO_DELIM)) {
		    
		    	String[] asOtherData = sOtherData.split(BibliophileBase.SONG_INFO_DELIM);
		    	
		    	if (asOtherData.length > 1) {
		    		
		        	String sAltSong      = asOtherData[0].replace(' ', '+');
		        	String sAltPerformer = asOtherData[1].replace(' ', '+');
		        	
		        	sOtherLink = BibliophileBase.CONST_LYRICS_SITE + sAltSong + "+" + sAltPerformer;
		    	}
		    }
		    else if (sOtherData.contains(BibliophileBase.OTHER_INFO_DELIM)) {
		    	
		        String[] asOtherData = sOtherData.split(BibliophileBase.OTHER_INFO_DELIM);
		        
		    	if (asOtherData.length > 1) {
		    	
		    		String sOtherSubject = asOtherData[0];
		    		
				    sOtherSubject = sOtherSubject.replaceAll("the ", "The ");
				    sOtherSubject = sOtherSubject.replaceAll(" ", "_");
				    sOtherSubject = sOtherSubject.replaceAll("'", "%27");
				    sOtherSubject = sOtherSubject.replaceAll("(Series)", "(series)");
					
				    sOtherLink = BibliophileBase.CONST_WIKI_SITE + sOtherSubject;
		    	}
		    }
	    }
	    catch (Throwable t)
	    {}
	    
	    return sOtherLink;
	}
}