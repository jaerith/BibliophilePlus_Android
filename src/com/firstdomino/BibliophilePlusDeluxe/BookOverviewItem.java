package com.firstdomino.BibliophilePlusDeluxe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides the data structure for a master Bibliophile record, where:
 * 
 *     'sBookData' holds raw data about the general information about the book
 *     'sBookType' denotes the book's type     
 *     'sBook' is the book's title
 *     'sAuthor' is the book's author
 *     
 *     'sibbMembershipTypes' holds metadata about the book's influence on song types
 *     'gibbMembershipTypes' holds metadata about the book's influence on game types
 *     
 *     'sibbItems' holds data about songs influenced by the book
 *     'gibbItems' holds data about games influenced by the book
 *     'quoteItems' holds data about quotes attributed to the book
 *     'locItems' holds data about physical real-world locations found in the book
 */
public class BookOverviewItem
{
	public String sBookData;
	public String sBookType;
	
	public String sBook;
	public String sAuthor;
	
	public HashMap<String,Integer> sibbMembershipTypes;
	public HashMap<String,Integer> gibbMembershipTypes;
	
	public ArrayList<BiblioItem> sibbItems;
	public ArrayList<BiblioItem> gibbItems;
	public ArrayList<BiblioItem> quoteItems;
	public ArrayList<BiblioItem> locItems;
	
	public BookOverviewItem(String psBookData) 
	{
	    sBookData  = psBookData;	    
	    sBookType  = "";
	    
	    sibbMembershipTypes = new HashMap<String, Integer>();
	    gibbMembershipTypes = new HashMap<String, Integer>();
	 
	    sibbItems  = new ArrayList<BiblioItem>();
	    gibbItems  = new ArrayList<BiblioItem>();
	    quoteItems = new ArrayList<BiblioItem>();
	    locItems   = new ArrayList<BiblioItem>();
	}	
}
