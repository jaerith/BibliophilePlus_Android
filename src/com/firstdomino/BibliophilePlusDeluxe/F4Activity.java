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

import com.firstdomino.BibliophilePlusDeluxe.DashboardActivity.RowModel;

/**
 * This class represents the Analyze The Charts screen, which allows the user 
 * to view various pie charts that represent different statistics about the book-related
 * data in our catalog.
 */
public class F4Activity extends DashboardActivity {

	public static final String CONST_STAT_CHOICE_CHARTS    = "Breakdown of How Types of Books Have Influenced Songs";
	public static final String CONST_STAT_CHOICE_TOP_5     = "Top Books to Influence Other Media";
	public static final String CONST_STAT_CHOICE_FAVORITES = "Readers' Favorites";
	
	private static ArrayList<RowModel> contentsModelList = new ArrayList<RowModel>();
	private static ArrayList<String>   contentsDataList  = new ArrayList<String>();
		
	private TextView  title_view; 
	private ListView  stat_choice_list;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the main list of options that will allow the user to drill down to another 
     * list, which will have more specific selections. 
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{	
		try {
			
			int i = 0;
		 
		    super.onCreate(savedInstanceState);
		    setContentView (R.layout.activity_f4);	    
		    setTitleFromActivityLabel (R.id.title_text);
		   
		    title_view        = (TextView) findViewById(R.id.title_text);
		    stat_choice_list  = (ListView) findViewById(R.id.stat_choice_list);
		    		    
		    if (contentsDataList.size() <= 0) {
		        contentsDataList.add(CONST_STAT_CHOICE_CHARTS);
		        contentsDataList.add(CONST_STAT_CHOICE_TOP_5);
		        // contentsDataList.add(CONST_STAT_CHOICE_FAVORITES);
		        
				for (i = 0; i < contentsDataList.size(); ++i) { 

					String sTmpStatChoice = contentsDataList.get(i);				
					contentsModelList.add(new RowModel(sTmpStatChoice));
				}
		    }
		    
			stat_choice_list.setAdapter(new StatChoiceListAdapter(contentsModelList));
		}
		catch (Throwable t) {
			
			Toast
			.makeText(this, "Request failed: "+t.toString(), 4000)
			.show();
	    }
	}

    /**
     * Under construction 
     * 
     * @param v The button that called this handler
     * @return Nothing
     */
	public void onClickSearch (View v)
	{
	    // startActivity (new Intent(getApplicationContext(), SearchActivity.class));
	}

	/**
	 * This subclass serves as the logical view for a row within the displayed list.
	 */
    class StatChoiceViewWrapper {
    	
    	View      base;
    	
    	TextView  label = null;
    	
    	StatChoiceViewWrapper(View base) {
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
	 * selects the "Top Books to Influence Other Media" row, it will then invoke the 
	 * ChartTopXTabListActivity class in order to present that screen.
	 */
    class StatChoiceListAdapter extends ArrayAdapter<RowModel> {    	    	
    	
    	StatChoiceListAdapter(ArrayList<RowModel> list) {
    		    		
    		super(F4Activity.this, R.layout.row, list);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                   nMaxRow;
    		int                   nImageId;
    		StatChoiceViewWrapper wrapper;
    		TextView              typeText;
    		
			nMaxRow = F4Activity.contentsDataList.size();
    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.results_row, parent, false);
	    			
	    			wrapper = new StatChoiceViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			typeText = wrapper.getLabel();
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
								// TODO Auto-generated method stub							
								Integer myPosition   = (Integer) v.getTag();							
								String  TmpStatChoice = "";

								TmpStatChoice =
										StatChoiceListAdapter.this.getItem(myPosition).toString();
																	
								if (TmpStatChoice == F4Activity.CONST_STAT_CHOICE_CHARTS) {									
								    startActivity (new Intent(F4Activity.this.getApplicationContext(), ChartTabListActivity.class));
								}
								else if (TmpStatChoice == F4Activity.CONST_STAT_CHOICE_TOP_5) {
								    startActivity (new Intent(F4Activity.this.getApplicationContext(), ChartTopXTabListActivity.class));
								}
								else if (TmpStatChoice == F4Activity.CONST_STAT_CHOICE_FAVORITES) {

									// NOTE: Under construction
								    // startActivity (new Intent(F4Activity.this.getApplicationContext(), ChartFavoritesActivity.class));
								}									
							}
					};
												
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(F4Activity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new StatChoiceViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (StatChoiceViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());

    		// wrapper.base.setTag(position);
    		wrapper.getLabel().setTag(position);
    		    		    		    		
    		return (row);
        }
    }
    
} // end class

