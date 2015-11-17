package com.firstdomino.BibliophilePlusDeluxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.util.regex.Pattern;

/**
 * This class represents the Discover Influences screen, allowing the user to focus on
 * a particular genre of books in our catalog and observe its overall influence on media.
 * 
 * For example, the user can select Classic Fiction and then observe a list of all media
 * (song, games, etc.) that have been influenced by Classic Fiction. 
 */
public class F2Activity extends DashboardActivity 
{
	private static ArrayList<RowModel> bookTypeModelList = new ArrayList<RowModel>();
	
	private ListView bookList_book_types;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the list of book genres that should be displayed to the user.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_f2);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    if (bookTypeModelList.size() == 0)
	    {
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
	    
	    bookList_book_types = (ListView) findViewById(R.id.booklist_book_types);
	    
	    bookList_book_types.setAdapter(new ABSelectTypeAdapter(bookTypeModelList) );
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
		
		EditText          searchTextView     = null;
		HashSet<String>   foundMatchSet      = new HashSet<String>();
		HashSet<String>   foundMatchSetLower = new HashSet<String>(); 
		ArrayList<String> foundMatches       = new ArrayList<String>();
		
		if ((searchTextView = (EditText) findViewById(R.id.search_text)) != null) {
			
			String matchLower = "";
			String searchText = searchTextView.getText().toString();
					
            // searchText = searchText.trim().toLowerCase();
			
			if (searchText.length() > 0) {
				
				Toast
				.makeText(getApplicationContext(), 
						  "Searching...", 
						  Toast.LENGTH_LONG)
			    .show ();
				
				Set<String> allKeys = BibliophileBase.bookOverviewItems.keySet();
				
				for(String tmpKey : allKeys) {
					
                    BookOverviewItem oTmpOverviewItem = BibliophileBase.bookOverviewItems.get(tmpKey);                                        	
                	
                	boolean bFoundMatch = 
                	    Pattern
                	    .compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE)
                	    .matcher(oTmpOverviewItem.sBookData)
                	    .find();
                	
                	if (bFoundMatch)                		
                		foundMatchSet.add(oTmpOverviewItem.sBookData);
			    }
				
				foundMatches.addAll(foundMatchSet);
				
				ListBooksActivity.tmpContentsDataList = foundMatches;
				
	            startActivity (new Intent(getApplicationContext(), ListBooksActivity.class));
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
	 * This subclass serves as the logical view for each displayed list within a tab of this Activity.
	 * A list will allow the user to interact with its contained rows.  For instance, if the user
	 * selects Classic Fiction, it will find all book trivia data associated with that genre and then
	 * pass that list of books (and associated data) to an instance of ListBooksActivity class, which 
	 * in turn will display this list to the user.
	 */	
    class ABSelectTypeAdapter extends ArrayAdapter<RowModel> {
    	    	
    	ABSelectTypeAdapter(ArrayList<RowModel> list) {
    		    		
    		super(F2Activity.this, R.layout.select_type_row, list);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                   nImageId;
    		SelectTypeViewWrapper wrapper;
    		ImageView             typeImg;
    		TextView              typeText;
    		    		
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
																
								TmpType =
										ABSelectTypeAdapter.this.getItem(myPosition).toString();
								
								// NOTE: Use type to get all relevant books
								ArrayList<BiblioItem> oSibbBiblioItems = 
										BibliophileBase.sibbItemsBookType.get(TmpType);
								
								ArrayList<BiblioItem> oGibbBiblioItems = 
										BibliophileBase.gibbItemsBookType.get(TmpType);
								
								HashSet<String> setAllBooksOfType = new HashSet<String>(); 
								
								for (BiblioItem tmpBiblioItem : oSibbBiblioItems)
									setAllBooksOfType.add(tmpBiblioItem.sBookData);
								
								for (BiblioItem tmpBiblioItem : oGibbBiblioItems)
									setAllBooksOfType.add(tmpBiblioItem.sBookData);
								
								ArrayList<String> oAllBooksOfType = new ArrayList(setAllBooksOfType);
								
								ListBooksActivity.tmpContentsDataList = oAllBooksOfType;
								
								startActivity (new Intent(F2Activity.this.getApplicationContext(), ListBooksActivity.class));																		
							}
					};
							
					// wrapper.base.setOnClickListener(typeClick);
					typeImg.setOnClickListener(typeClick);
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(F2Activity.this)
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

			nImageId = getBookTypeImageIcon(model.toString());
    		
            wrapper.getTypeImage().setBackgroundResource( nImageId );           

    		return (row);
        }
    }    
    
} // end class
