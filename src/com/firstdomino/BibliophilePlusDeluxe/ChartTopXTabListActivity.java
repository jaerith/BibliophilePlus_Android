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
 * This class represents a screen that is created as one of the user's choices 
 * on the F4Activity screen.  It will display a series of tabs and rows that allow 
 * the user to invoke charts of the top 5 influential books for a specific genre.
 * 
 * For example, one pie chart may show that the database's top 5 influential books for classic rock are 
 * The Bible (10%), Lord of the Rings (5%), etc.
 * 
 * This class is used solely by the F4Activity class.
 */
public class ChartTopXTabListActivity extends DashboardActivity 
{
    private static ArrayList<RowModel> generalModelList  = new ArrayList<RowModel>();
	private static ArrayList<RowModel> songTypeModelList = new ArrayList<RowModel>();
	private static ArrayList<RowModel> bookTypeModelList = new ArrayList<RowModel>();
	private static ArrayList<RowModel> gameTypeModelList = new ArrayList<RowModel>();
	
	public  static ChartStatsActivity  statsActivity = null;
	
	protected TabHost  tabs;
	private   ListView sibbList_general_types;
	private   ListView sibbList_song_types;
	private   ListView sibbList_book_types;
	private   ListView gibbList_game_types;

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
	    setContentView (R.layout.activity_chart_tab_list);
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
	    	
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_CLASSIC));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_FPS));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_RTS));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_RPG));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_PUZZLE));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_AA));
	    	gameTypeModelList.add(new RowModel(CONST_GIBB_GTYPE_SIM));
	    }
	    
	    if (statsActivity == null)
	    {
	    	statsActivity = new ChartStatsActivity();
	    }
	    
        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();        
	        	        
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.sibblist_song_types);
        spec.setIndicator("Song Types");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.gibblist_game_types);
        spec.setIndicator("Game Types");
        tabs.addTab(spec);
        
        sibbList_song_types = (ListView) findViewById(R.id.sibblist_song_types);
        gibbList_game_types = (ListView) findViewById(R.id.gibblist_game_types);

        sibbList_song_types.setAdapter(new SelectTypeAdapter(songTypeModelList, 
        		                                             SelectTypeAdapter.CONST_TYPE_LIST_SONGS) );
                
        gibbList_game_types.setAdapter(new SelectTypeAdapter(gameTypeModelList, 
                                                             SelectTypeAdapter.CONST_TYPE_LIST_GAMES) );        
	}

    /**
     * Under Construction
     */	
	public void onClickSearch (View v) {

		/*
		 * NOTE: What should we do here, if anything?
		 */
	}

    /**
     * This method will return the image ID for a specific genre of games.  This image
     * will be placed in its respective row within the Games tab of this screen.
     * 
     * @param gameType The target name for the specific genre of games
     * @return The ID of the image that represents that game genre
     */	
	public static int getGameTypeImageIcon(String gameType) {
		
		if ( gameType == null) {
			return R.drawable.button_games_small;
	    }
		
		if ( gameType == CONST_GIBB_GTYPE_CLASSIC ) {
			return R.drawable.button_games;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_FPS ) {
			return R.drawable.game_type_fps;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_RTS ) {
			return R.drawable.game_type_rts;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_RPG ) {
			return R.drawable.game_type_rpg;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_PUZZLE ) {
			return R.drawable.game_type_puzzle;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_AA ) {
			return R.drawable.game_type_aa;
	    }
		else if ( gameType == CONST_GIBB_GTYPE_SIM ) {
			return R.drawable.game_type_simulator;
	    }
		else {
			return R.drawable.button_games_small;
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
		else {
			return R.drawable.guitar;
	    }    			
    }

	/**
	 * This subclass serves as the logical view for each displayed list within a tab of this Activity.
	 * A list will allow the user to interact with its contained rows.  For instance, if the user
	 * selects Classic Rock in the Songs tab, it will first collect the relevant data about Classic Rock 
	 * and then invoke a pie chart with that data (via the ChartStatsActivity class).
	 */	
    class SelectTypeAdapter extends ArrayAdapter<RowModel> {
    	
    	public static final int CONST_TYPE_LIST_SONGS = 1;
    	public static final int CONST_TYPE_LIST_BOOKS = 2;
    	public static final int CONST_TYPE_LIST_GAMES = 3;
    	
    	private int listType = 0;
    	
    	SelectTypeAdapter(ArrayList<RowModel> list, int type) {
    		    		
    		super(ChartTopXTabListActivity.this, R.layout.select_type_row, list);
    		
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
    		else if (listType == CONST_TYPE_LIST_BOOKS)
    			nMaxRow = gameTypeModelList.size();
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
								Integer myPosition  = (Integer) v.getTag();							
								String  SubTypeVal  = "";
								String  ScreenTitle = "";
								
								String[]                statTitles = null;
							    double[]                statValues = null;
							    HashMap<String,Integer> oTopValues = null;
								
								SubTypeVal =
								    SelectTypeAdapter.this.getItem(myPosition).toString();
								
								if (SelectTypeAdapter.this.listType == SelectTypeAdapter.CONST_TYPE_LIST_SONGS)
								{									
									oTopValues = 
										   BibliophileBase.getSongTypeTop5(SubTypeVal);
									
									statTitles = new String[oTopValues.size()];
									statValues = new double[oTopValues.size()];
									
									int nStatIdx = 0;
									for (String sTitle : oTopValues.keySet()) {
										
										statTitles[nStatIdx] = sTitle;
										statValues[nStatIdx] = oTopValues.get(sTitle).doubleValue();
										
										++nStatIdx;
								    }
									
									ScreenTitle = "Top 5 Books to Influence " + SubTypeVal + " Songs";											
								}
								else if (SelectTypeAdapter.this.listType == SelectTypeAdapter.CONST_TYPE_LIST_GAMES)
								{
									oTopValues = 
											   BibliophileBase.getGameTypeTop5(SubTypeVal);
									
								    statTitles = new String[oTopValues.size()];
									statValues = new double[oTopValues.size()];
									
									int nStatIdx = 0;
									for (String sTitle : oTopValues.keySet()) {
										
										statTitles[nStatIdx] = sTitle;
										statValues[nStatIdx] = oTopValues.get(sTitle).doubleValue();
										
										++nStatIdx;
								    }
										
									ScreenTitle = "Top 5 Books to Influence " + SubTypeVal + " Games";
								}

								try {
								    startActivity(ChartTopXTabListActivity.statsActivity.execute(ChartTopXTabListActivity.this, ScreenTitle, statTitles, statValues));
								}
								catch (Throwable t) {
									
									new AlertDialog.Builder(ChartTopXTabListActivity.this)
									.setTitle("DEBUG")
									.setMessage("ERROR!  Could not create stats chart for (" + ScreenTitle + ")\n\n" + t.toString())
									.setNeutralButton("Done", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dlg, int s) {}
									})
									.show();
							    }
							}
					};
							
					// wrapper.base.setOnClickListener(typeClick);
					typeImg.setOnClickListener(typeClick);
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(ChartTopXTabListActivity.this)
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

    		wrapper.getLabel().setTag(position);
    		wrapper.getTypeImage().setTag(position);

    		if (listType == CONST_TYPE_LIST_SONGS) 
    			nImageId = getSongTypeImageIcon(model.toString());
    		else if (listType == CONST_TYPE_LIST_BOOKS)
    			nImageId = getBookTypeImageIcon(model.toString());
    		else if (listType == CONST_TYPE_LIST_GAMES)
    		    nImageId = getGameTypeImageIcon(model.toString());
    		else
    			nImageId = getSongTypeImageIcon(model.toString());
    		
            wrapper.getTypeImage().setBackgroundResource( nImageId );           
    		    		    		    		
    		return (row);
        }
    }    
    
} // end class

