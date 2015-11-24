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
import android.widget.AdapterView.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import com.firstdomino.BibliophilePlusDeluxe.DashboardActivity.RowModel;

/**
 * This class represents a screen that will display a list of books to the user;
 * this list is usually the result of a request that searches the data cache or
 * grabs a certain subcategory of books.  This screen is mainly used by the 
 * F2Activity class and the ListResultsActivity class.  
 */
public class ListBooksActivity extends DashboardActivity 
{
	public static final String CONST_SORT_BY_WORK = "Sort By Work";
	
	public static ArrayList<String> tmpContentsDataList  = new ArrayList<String>();
	
	private ArrayList<RowModel> contentsModelList = new ArrayList<RowModel>();
	private ArrayList<String>   contentsDataList  = new ArrayList<String>();
		
	private TextView  title_view; 
	private ListView  book_list;
	private Spinner   sort_by_list;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the list for the book results and then sort the books in alphabetical order by
     * title or author.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{	
		try {
			
		    super.onCreate(savedInstanceState);
		    setContentView (R.layout.activity_book_list);	    
		    setTitleFromActivityLabel (R.id.title_text);
		   
		    title_view   = (TextView) findViewById(R.id.title_text);
		    book_list    = (ListView) findViewById(R.id.book_list);
		    sort_by_list = (Spinner)  findViewById(R.id.sort_by_spinner);
		    		    
	        // title_view.setText( title_type + title_sub_type + title_sub_type_value );
		    
		    contentsDataList = (ArrayList<String>) tmpContentsDataList.clone();
		    tmpContentsDataList.clear();

		    propagateList();
			
		    if (contentsDataList.size() > 1) {
		    
				sort_by_list.setOnItemSelectedListener(new OnItemSelectedListener() {
					
				    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
				    	
				    	ListBooksActivity.this.sortList(parentView.getItemAtPosition(pos).toString());			    	
				    }
	
				    public void onNothingSelected(AdapterView<?> parentView) {
				        // Do nothing
				    }
	
	            });
		    }
		    else { 
		    
		    	sort_by_list.setVisibility(View.GONE);
		    }
		}
		catch (Throwable t) {
			
			Toast
			.makeText(this, "Request failed: "+t.toString(), 4000)
			.show();
	    }
	}

    /**
     * Under Construction
     * 
     * @return Nothing
     */	
	public void onClickSearch (View v)
	{
		// NOTE: Figure out what to do here
	    // startActivity (new Intent(getApplicationContext(), SearchActivity.class));
	}

    /**
     * This method populates the list control with the provided book results, 
     * after they have been sorted.
     * 
     * @return Nothing
     */	
	public void propagateList() 
	{
		int i = 0;
		
		contentsModelList.clear();
		
		for (i = 0; i < contentsDataList.size(); ++i) 
		{
			String sTmpBookData = contentsDataList.get(i);
			
			contentsModelList.add(new RowModel(sTmpBookData));
		}
        			
		book_list.setAdapter(new BookListAdapter(contentsModelList));		
	}

    /**
     * This method will first sort the provided book results (by title or author) and then
     * populate the list control with those results.
     * 
     * @param sSortType Value that indicates the type of sort to conduct on the provided book results
     * @return Nothing
     */
	public void sortList(String sSortType) {
		
		int nBeforeSortingCount = 
				contentsDataList.size();
		
		LinkedHashMap<String, ArrayList<String>> oSortingMap = 
				new LinkedHashMap<String, ArrayList<String>>();
		        
        if (sSortType.equalsIgnoreCase(CONST_SORT_BY_WORK)) {
        	
        	for (String sTmpItem : contentsDataList) {
        		
        		String sWork = sTmpItem.split(" by ")[0];
        		
        		if (sWork.startsWith("The "))
        			sWork = sWork.substring(4);
        		else if (sWork.startsWith("A "))
        			sWork = sWork.substring(2);
        		else if (sWork.startsWith("An "))
        			sWork = sWork.substring(3);
        		
        		if (!oSortingMap.keySet().contains(sWork))
        			oSortingMap.put(sWork, new ArrayList<String>());
        		
        		ArrayList<String> oValueList = oSortingMap.get(sWork);
        		
        		oValueList.add(sTmpItem);
            }
        }
        else {
       
        	for (String sTmpItem : contentsDataList) {
        		
        		String sCreator = sTmpItem.split(" by ")[1];
        		
        		if (!oSortingMap.keySet().contains(sCreator))
        			oSortingMap.put(sCreator, new ArrayList<String>());
        		
        		ArrayList<String> oValueList = oSortingMap.get(sCreator);
        		
        		oValueList.add(sTmpItem);
            }
	    }
        
        contentsDataList.clear();
        
        TreeSet<String> keys = new TreeSet<String>(oSortingMap.keySet());
        for (String key : keys) {
        	
        	ArrayList<String> oValueList = oSortingMap.get(key);
    		
    		for (String sTmpItem : oValueList) {
    			contentsDataList.add(sTmpItem);
            }
        }
        
		propagateList();		
    }

	/**
	 * This subclass serves as the logical view for each displayed row within our list.
	 */			
    class BookListViewWrapper {
    	
    	View      base;
    	
    	TextView  label = null;
    	
    	BookListViewWrapper(View base) {
    		this.base = base;
        }
    	    	    	
    	TextView getLabel() {
    		
    		if (label==null) {
    			label=(TextView) base.findViewById(R.id.resultTypeLabel);
    	    }
    		
    		return (label);
        }    	    	
    }

	/**
	 * This subclass serves as the logical view for the displayed list within this Activity.
	 * A list will allow the user to interact with its contained rows.  For instance, if the user
	 * selects Dune in this list, it will present all of that data about that book by invoking an
	 * instance of the BookSummaryActivity class, which will then display everything in the 
	 * trivia data cache about the book Dune.
	 */			
    class BookListAdapter extends ArrayAdapter<RowModel> {    	    	
    	
    	BookListAdapter(ArrayList<RowModel> list) {
    		    		
    		super(ListBooksActivity.this, R.layout.row, list);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                   nMaxRow;
    		int                   nImageId;
    		BookListViewWrapper   wrapper;
    		TextView              typeText;
    		
			nMaxRow = ListBooksActivity.this.contentsDataList.size();
    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.results_row, parent, false);
	    			
	    			wrapper = new BookListViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			typeText = wrapper.getLabel();
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
									// TODO Auto-generated method stub							
									Integer myPosition   = (Integer) v.getTag();							
									String  TmpBookEntry = "";

									TmpBookEntry =
											BookListAdapter.this.getItem(myPosition).toString();
																		
									if (BibliophileBase.bookOverviewItems.containsKey(TmpBookEntry)) {																			
									
										BookOverviewItem oTmpOverviewItem = 
												BibliophileBase.bookOverviewItems.get(TmpBookEntry);
																				
										BookSummaryActivity.tmpBookOverviewItem = oTmpOverviewItem;
										
									    startActivity (new Intent(ListBooksActivity.this.getApplicationContext(), BookSummaryActivity.class));
									}
							}
					};
												
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(ListBooksActivity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new BookListViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (BookListViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());
			
    		// wrapper.base.setTag(position);
    		wrapper.getLabel().setTag(position);
    		    		    		    		
    		return (row);
        }
    }
    
} // end class

