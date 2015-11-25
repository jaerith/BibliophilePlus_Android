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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
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
import com.firstdomino.BibliophilePlusDeluxe.F1Activity.SIBBSelectTypeAdapter;
import com.firstdomino.BibliophilePlusDeluxe.ListBooksActivity.BookListAdapter;

/**
 * This class represents a screen that will display a multidimensional result set;
 * these results are usually the result of a request that searches the data cache.
 * This screen is mainly used by the F1Activity class and the F2Activity class.  
 */
public class ListResultsActivity extends DashboardActivity 
{	
	public static final String CONST_SORT_BY_SONG = "Sort By Song";
	
	public static ArrayList<BiblioItem> tmpContentsDataList = new ArrayList<BiblioItem>();  
	
	private ArrayList<RowModel>   contentsModelList = new ArrayList<RowModel>();	
	private ArrayList<BiblioItem> contentsDataList  = new ArrayList<BiblioItem>(); 
	
	private char     type           = '?';
	private String   sub_type       = "?";
	private String   sub_type_value = "?";
	
	private String    title_type;
	private String    title_sub_type;
	private String    title_sub_type_key;
	
	private TextView  title_view; 
	private ListView  results_list;
	private Spinner   sort_by_list;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the list for the search results.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{	
		title_type         = "";
		title_sub_type     = "";
		title_sub_type_key = "";
	
		try {
			
			int i = 0;
		 
		    super.onCreate(savedInstanceState);
		    setContentView (R.layout.activity_results);	    
		    setTitleFromActivityLabel (R.id.title_text);
		   
		    title_view   = (TextView) findViewById(R.id.title_text);
		    results_list = (ListView) findViewById(R.id.results_list);
		    sort_by_list = (Spinner)  findViewById(R.id.sort_by_spinner);
		    
		    if (BibliophileBase.currResultsType == BibliophileBase.CONST_ACTIVITY_TYPE_SIBB)
		    {
		    	type       = BibliophileBase.currResultsType; 
		    	title_type = "Songs by ";
	
		    	sub_type             = BibliophileBase.currResultsSubType;
		    	sub_type_value       = BibliophileBase.currResultsSubTypeVal;
		    	title_sub_type_key = sub_type_value; 
	
		    	if (BibliophileBase.currResultsSubType == BibliophileBase.CONST_ACTIVITY_SUBTYPE_SIBB_BY_ST)
		    	{
		    		title_sub_type   = "Song Type: ";
		    		contentsDataList = BibliophileBase.sibbItemsSongType.get(title_sub_type_key);
		    	}
		    	else if (BibliophileBase.currResultsSubType == BibliophileBase.CONST_ACTIVITY_SUBTYPE_SIBB_BY_BT)
		    	{
		    		title_sub_type   = "Book Type: ";
		    		contentsDataList = BibliophileBase.sibbItemsBookType.get(title_sub_type_key);
		    	}
		    }
		    else if (BibliophileBase.currResultsType == BibliophileBase.CONST_ACTIVITY_TYPE_GIBB) 
		    {
		    	// NOTE: Do stuff here
		    }
		    else if (tmpContentsDataList.size() > 0) 
		    {
		    	contentsDataList = (ArrayList<BiblioItem>) tmpContentsDataList.clone();
		    	tmpContentsDataList.clear();
		    }		    
		    
	        // title_view.setText( title_type + title_sub_type + title_sub_type_key );
	        			
			BibliophileBase.currResultsType    = '?';
			BibliophileBase.currResultsSubType = "?";
			
			if (contentsDataList.size() > 1) {
				
				sort_by_list.setOnItemSelectedListener(new OnItemSelectedListener() {
					
				    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
				    	
				    	ListResultsActivity.this.sortList(parentView.getItemAtPosition(pos).toString());			    	
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
     * This method populates the list control with the provided book results.
     * 
     * @return Nothing
     */		
	public void propagateList() 
	{
		int i = 0;
		
		contentsModelList.clear();
		
		for (i = 0; i < contentsDataList.size(); ++i) 
		{
			BiblioItem oBibItem = contentsDataList.get(i);
			
			// contentsModelList.add(new RowModel(oBibItem.sOtherData + CONST_ITEM_DELIM + oBibItem.sBookData));
			contentsModelList.add(new RowModel(oBibItem.sOtherData));
		}
		
		results_list.setAdapter(new ListResultsAdapter(contentsModelList));
	}
	
	public void sortList(String sSortType) {
		
		int nBeforeSortingCount = 
				contentsDataList.size();
		
		LinkedHashMap<String, ArrayList<BiblioItem>> oSortingMap = 
				new LinkedHashMap<String, ArrayList<BiblioItem>>();
		        
        if (sSortType.equalsIgnoreCase(CONST_SORT_BY_SONG)) {
        	
        	for (BiblioItem sTmpItem : contentsDataList) {
        		
        		String sWork = sTmpItem.sOtherData.split(" by ")[0];
        		
        		if (sWork.startsWith("The "))
        			sWork = sWork.substring(4);
        		else if (sWork.startsWith("A "))
        			sWork = sWork.substring(2);
        		else if (sWork.startsWith("An "))
        			sWork = sWork.substring(3);
        		
        		if (!oSortingMap.keySet().contains(sWork))
        			oSortingMap.put(sWork, new ArrayList<BiblioItem>());
        		
        		ArrayList<BiblioItem> oValueList = oSortingMap.get(sWork);
        		
        		oValueList.add(sTmpItem);
            }
        }
        else {
       
        	for (BiblioItem sTmpItem : contentsDataList) {
        		
        		String sCreator = sTmpItem.sOtherData.split(" by ")[1];
        		
        		if (!oSortingMap.keySet().contains(sCreator))
        			oSortingMap.put(sCreator, new ArrayList<BiblioItem>());
        		
        		ArrayList<BiblioItem> oValueList = oSortingMap.get(sCreator);
        		
        		oValueList.add(sTmpItem);
            }
	    }
        
        contentsDataList.clear();
        
        TreeSet<String> keys = new TreeSet<String>(oSortingMap.keySet());
        for (String key : keys) {
        	
        	ArrayList<BiblioItem> oValueList = oSortingMap.get(key);
    		
    		for (BiblioItem sTmpItem : oValueList) {
    			contentsDataList.add(sTmpItem);
            }
        }
        
		propagateList();
		
		/*
		int nAfterSortingCount = contentsDataList.size();
		
        new AlertDialog.Builder(ListResultsActivity.this)
		.setTitle("DEBUG")
		.setMessage("Before sort, count is: (" + nBeforeSortingCount + 
				    ") -> After sort, count is: (" + nAfterSortingCount + ")")
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int s) {}
		})
		.show();
		*/
    }
	
    class ListResultViewWrapper {
    	
    	View      base;
    	
    	TextView  label = null;
    	
    	ListResultViewWrapper(View base) {
    		this.base = base;
        }
    	    	    	
    	TextView getLabel() {
    		
    		if (label==null) {
    			label=(TextView) base.findViewById(R.id.resultTypeLabel);
    	    }
    		
    		return (label);
        }    	    	
    }
	
    class ListResultsAdapter extends ArrayAdapter<RowModel> {    	    	
    	
    	ListResultsAdapter(ArrayList<RowModel> list) {
    		    		
    		super(ListResultsActivity.this, R.layout.row, list);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                   nMaxRow;
    		int                   nImageId;
    		ListResultViewWrapper wrapper;
    		TextView              typeText;
    		
			nMaxRow = ListResultsActivity.this.contentsDataList.size();
    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.results_row, parent, false);
	    			
	    			wrapper = new ListResultViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			typeText = wrapper.getLabel();
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
									// TODO Auto-generated method stub							
									Integer    myPosition = (Integer) v.getTag();							
									BiblioItem TmpItem    = null;

									TmpItem =
											ListResultsActivity.this.contentsDataList.get(myPosition);
									
									ListBooksActivity.tmpContentsDataList.clear();
									ListBooksActivity.tmpContentsDataList.add(TmpItem.sBookData);									
									
									/*
									new AlertDialog.Builder(ListResultsActivity.this)
									.setTitle("DEBUG")
									.setMessage("You have selected (" + TmpItem.sBookData + ").")
									.setNeutralButton("Done", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dlg, int s) {}
									})
									.show();
									*/
									
									startActivity (new Intent(ListResultsActivity.this.getApplicationContext(), ListBooksActivity.class));
							}
					};
							
					// wrapper.base.setOnClickListener(typeClick);
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(ListResultsActivity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new ListResultViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (ListResultViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());

			// wrapper.getLabel().setTextColor(Color.WHITE);
			// wrapper.getLabel().setTextSize(BibliophileBase.normTextSize);
    		// wrapper.getLabel().setVisibility(View.VISIBLE);
    		// wrapper.getTypeImage().setVisibility(View.VISIBLE);
			
    		// wrapper.base.setTag(position);
    		wrapper.getLabel().setTag(position);
    		    		    		    		
    		return (row);
        }
    }
    
} // end class
