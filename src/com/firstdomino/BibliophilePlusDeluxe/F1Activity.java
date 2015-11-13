package com.firstdomino.BibliophilePlusDeluxe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.util.regex.*;

/**
 * This class represents the Browse Books screen, allowing the user to focus on
 * browsing the catalog of books in our data cache.  In the Books tab, the user
 * can pick a book based on its genre (like Classic Fiction).  In the Songs tab,
 * the user can pick a genre (like Classic Rock) to learn about the books that
 * have influenced that type of music.
 * 
 * For example, the user can look at books within a specific genre, and they
 * can search for a specific book that matches some provided text.
 */
public class F1Activity extends DashboardActivity 
{		
	private static ArrayList<RowModel> songTypeModelList = new ArrayList<RowModel>();
	private static ArrayList<RowModel> bookTypeModelList = new ArrayList<RowModel>();
	
	protected TabHost  tabs;
	private   ListView sibbList_song_types;
	private   ListView sibbList_book_types;

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
	    setContentView (R.layout.activity_f1);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    if (songTypeModelList.size() == 0)
	    {
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_CLASSIC_ROCK));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_ROCK));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_POP));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_HIP_HOP));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_METAL));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_GOSPEL));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_ALT));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_COMPOSITIONS));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_COUNTRY));
	    	songTypeModelList.add(new RowModel(CONST_SIBB_STYPE_RECENT));
	    	
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_SCIFI));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_FICTION));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_HISTORY));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_POETRY));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_CLASSIC_LIT));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_ANCIENT_TXT));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_BIOGRAPHY));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_HUMOR));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_CHILD_LIT));
	    	bookTypeModelList.add(new RowModel(CONST_BOOK_TYPE_NON_FICTION));	    	
	    }
	    
        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();        
	        	        
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.sibblist_song_types);
        spec.setIndicator("Based on Song Types");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.sibblist_book_types);
        spec.setIndicator("Based on Book Types");
        tabs.addTab(spec);

        sibbList_song_types = (ListView) findViewById(R.id.sibblist_song_types);
        sibbList_book_types = (ListView) findViewById(R.id.sibblist_book_types);

        sibbList_song_types.setAdapter(new SIBBSelectTypeAdapter(songTypeModelList, 
        		                                                 SIBBSelectTypeAdapter.CONST_TYPE_LIST_SONGS) );
        
        sibbList_book_types.setAdapter(new SIBBSelectTypeAdapter(bookTypeModelList, 
												                 SIBBSelectTypeAdapter.CONST_TYPE_LIST_BOOKS) );
	}

    /**
     * This handler will use the text from an associated text box and conduct
     * a search within the data cache for any data that matches the text.  When
     * results are found, it will present them via an invoked instance of the
     * ListResultsActivity class. 
     * 
     * @param v The button that called this handler
     * @return Nothing
     */
	public void onClickSearch (View v) {
	
		String                sOtherData     = null;
		EditText              searchTextView = null;
		ArrayList<BiblioItem> foundMatches   = new ArrayList<BiblioItem>();
		
		if ((searchTextView = (EditText) findViewById(R.id.search_text)) != null) {
			
			String searchText = searchTextView.getText().toString();
					
			if (searchText.length() > 0) {
				
				Toast
				.makeText(getApplicationContext(), 
						  "Searching...", 
						  Toast.LENGTH_LONG)
			    .show ();
				
				Set<String> allKeys = BibliophileBase.bookOverviewItems.keySet();
				
				for(String tmpKey : allKeys) {
					
                    BookOverviewItem oTmpOverviewItem = BibliophileBase.bookOverviewItems.get(tmpKey);
                    
                    for (BiblioItem tmpBiblioItem : oTmpOverviewItem.sibbItems) {                    
                    	
                    	sOtherData = tmpBiblioItem.sOtherData;
                    	
                    	boolean bFoundMatch = 
                    	    Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE).matcher(sOtherData).find();
                    	
                    	if (bFoundMatch) {
                    		foundMatches.add(tmpBiblioItem);
                        }
				    }
			    }
				
				ListResultsActivity.tmpContentsDataList = foundMatches;
				
	            startActivity (new Intent(getApplicationContext(), ListResultsActivity.class));
		    }
		}
	}

    /**
     * This handler will empty the contents of the text book (for the convenience of the user).
     * 
     * @return Nothing
     */	
	public void onClickText() {
		
		EditText searchTextView = null;
		
		if ((searchTextView = (EditText) findViewById(R.id.search_text)) != null) { 
            searchTextView.setText("");
		}
	}

    /**
     * This method will return the image ID for a specific genre of songs.  This image
     * will be placed in its respective row within the Songs tab of this screen.
     * 
     * @param songType The target name for the specific genre of songs
     * @return The ID of the image that represents that song genre
     */
	public static int getSongTypeImageIcon(String songType) {
		
		if ( songType == null) {
			return R.drawable.guitar;
	    }
		
		if ( songType == CONST_SIBB_STYPE_CLASSIC_ROCK ) {
			return R.drawable.classicrock;
	    }
		else if ( songType == CONST_SIBB_STYPE_ROCK ) {
			return R.drawable.guitar;
	    }
		else if ( songType == CONST_SIBB_STYPE_POP ) {
			return R.drawable.pop;
	    }
		else if ( songType == CONST_SIBB_STYPE_HIP_HOP ) {
			return R.drawable.hiphop;
	    }
		else if ( songType == CONST_SIBB_STYPE_METAL ) {
			return R.drawable.metal;
	    }
		else if ( songType == CONST_SIBB_STYPE_GOSPEL ) {
			return R.drawable.gospel;
	    }
		else if ( songType == CONST_SIBB_STYPE_ALT ) {
			return R.drawable.alternative;
	    }
		else if ( songType == CONST_SIBB_STYPE_COMPOSITIONS ) {
			return R.drawable.acoustic;
	    }
		else if ( songType == CONST_SIBB_STYPE_COUNTRY ) {
			return R.drawable.country;
	    }
		else if ( songType == CONST_SIBB_STYPE_RECENT ) {
			return R.drawable.recent;
	    }
		else {
			return R.drawable.guitar;
	    }    			
    }

	/**
	 * This subclass serves as the logical view for each displayed list within a tab of this Activity.
	 * A list will allow the user to interact with its contained rows.  For instance, if the user
	 * selects Classic Rock in the Songs tab, it will specify the correct flags and then invoke the
	 * ListResultsActivity class, which in turn will find and display all books influential to 
	 * Classic Rock music.
	 */				
    class SIBBSelectTypeAdapter extends ArrayAdapter<RowModel> {
    	
    	public static final int CONST_TYPE_LIST_SONGS = 1;
    	public static final int CONST_TYPE_LIST_BOOKS = 2;
    	
    	private int listType = 0;
    	
    	SIBBSelectTypeAdapter(ArrayList<RowModel> list, int type) {
    		    		
    		super(F1Activity.this, R.layout.select_type_row, list);
    		
    		listType = type;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                   nMaxRow;
    		int                   nImageId;
    		SelectTypeViewWrapper wrapper;
    		ImageView             typeImg;
    		TextView              typeText;
    		
    		if (listType == CONST_TYPE_LIST_SONGS) 
    			nMaxRow = songTypeModelList.size();
    		else if (listType == CONST_TYPE_LIST_BOOKS)
    			nMaxRow = bookTypeModelList.size();
    		else
    			nMaxRow = 0;
    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.select_type_row, parent, false);
	    			
	    			wrapper = new SelectTypeViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			typeImg  = wrapper.getTypeImage();
	    			typeText = wrapper.getLabel();
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
								// TODO Auto-generated method stub							
								Integer myPosition = (Integer) v.getTag();							
								String  TmpType    = "";
								
								BibliophileBase.currResultsType = 
										BibliophileBase.CONST_ACTIVITY_TYPE_SIBB;																	

								if (SIBBSelectTypeAdapter.this.listType == SIBBSelectTypeAdapter.CONST_TYPE_LIST_SONGS)
									BibliophileBase.currResultsSubType = BibliophileBase.CONST_ACTIVITY_SUBTYPE_SIBB_BY_ST;
								else if (SIBBSelectTypeAdapter.this.listType == SIBBSelectTypeAdapter.CONST_TYPE_LIST_BOOKS)
									BibliophileBase.currResultsSubType = BibliophileBase.CONST_ACTIVITY_SUBTYPE_SIBB_BY_BT;

								TmpType =
								    SIBBSelectTypeAdapter.this.getItem(myPosition).toString();
																
								BibliophileBase.currResultsSubTypeVal = TmpType;
								
								startActivity (new Intent(F1Activity.this.getApplicationContext(), ListResultsActivity.class));																		
							}
					};
							
					// wrapper.base.setOnClickListener(typeClick);
					typeImg.setOnClickListener(typeClick);
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(F1Activity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new SelectTypeViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (SelectTypeViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());

    		// wrapper.base.setTag(position);
    		wrapper.getLabel().setTag(position);
    		wrapper.getTypeImage().setTag(position);

    		if (listType == CONST_TYPE_LIST_SONGS) 
    			nImageId = getSongTypeImageIcon(model.toString());
    		else if (listType == CONST_TYPE_LIST_BOOKS)
    			nImageId = getBookTypeImageIcon(model.toString());
    		else
    			nImageId = getSongTypeImageIcon(model.toString());
    		
            wrapper.getTypeImage().setBackgroundResource( nImageId );           
    		    		    		    		
    		return (row);
        }
    }    
    
} // end class
