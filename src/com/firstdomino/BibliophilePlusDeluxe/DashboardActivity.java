package com.firstdomino.BibliophilePlusDeluxe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class serves as the base class for any screen presented to the user within this app.
 */
public abstract class DashboardActivity extends Activity 
{		
	public static final String CONST_BOOK_TYPE_SCIFI        = "Science Fiction/Fantasy";
	public static final String CONST_BOOK_TYPE_FICTION      = "Fiction";
	public static final String CONST_BOOK_TYPE_HISTORY      = "History/Mythology";
	public static final String CONST_BOOK_TYPE_POETRY       = "Poetry";
	public static final String CONST_BOOK_TYPE_CLASSIC_LIT  = "Classic Literature";
	public static final String CONST_BOOK_TYPE_ANCIENT_TXT  = "Ancient Text";
	public static final String CONST_BOOK_TYPE_BIOGRAPHY    = "Biography";
	public static final String CONST_BOOK_TYPE_HUMOR        = "Humor";
	public static final String CONST_BOOK_TYPE_CHILD_LIT    = "Children's Literature";
	public static final String CONST_BOOK_TYPE_NON_FICTION  = "Non-Fiction";
	
	public static final String CONST_SIBB_STYPE_CLASSIC_ROCK = "Classic Rock";
	public static final String CONST_SIBB_STYPE_ROCK         = "Rock";
	public static final String CONST_SIBB_STYPE_POP          = "Pop";
	public static final String CONST_SIBB_STYPE_HIP_HOP      = "Hip-Hop";
	public static final String CONST_SIBB_STYPE_METAL        = "Metal";
	public static final String CONST_SIBB_STYPE_GOSPEL       = "Gospel";
	public static final String CONST_SIBB_STYPE_ALT          = "Alternative/Indie";
	public static final String CONST_SIBB_STYPE_COMPOSITIONS = "Singer/Songwriter";
	public static final String CONST_SIBB_STYPE_COUNTRY      = "Country";
	public static final String CONST_SIBB_STYPE_RECENT       = "Recently Added";
	
	public static final String CONST_GIBB_GTYPE_CLASSIC      = "Classic";
    public static final String CONST_GIBB_GTYPE_FPS          = "First-Person Shooter";
    public static final String CONST_GIBB_GTYPE_RTS          = "Real-Time Strategy";
    public static final String CONST_GIBB_GTYPE_RPG          = "Role-Playing Game";
    public static final String CONST_GIBB_GTYPE_PUZZLE       = "Puzzle Solver";
    public static final String CONST_GIBB_GTYPE_AA           = "Action-Adventure";
    public static final String CONST_GIBB_GTYPE_SIM          = "Simulator";

    /**
     * Standard Android method called to invoke the Activity class.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */	
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.activity_default);
	}

    /**
     * Standard Android method that serves as the class destructor.
     * 
     * @return Nothing
     */	
	protected void onDestroy ()
	{
	   super.onDestroy ();
	}

    /**
     * Standard Android method that serves to pause the Activity's execution.
     * 
     * @return Nothing
     */		
	protected void onPause ()
	{
	   super.onPause ();
	}

    /**
     * Standard Android method that serves to restart the Activity's execution.
     * 
     * @return Nothing
     */			
	protected void onRestart ()
	{
	   super.onRestart ();
	}

    /**
     * Standard Android method that serves to resume the Activity's execution.
     * 
     * @return Nothing
     */				
	protected void onResume ()
	{
	   super.onResume ();
	}

    /**
     * Standard Android method that serves to start the Activity's execution.
     * 
     * @return Nothing
     */					
	protected void onStart ()
	{
	   super.onStart ();
	}

    /**
     * Standard Android method that serves to stop the Activity's execution.
     * 
     * @return Nothing
     */						
	protected void onStop ()
	{
	   super.onStop ();
	}

    /**
     * This handler (to be overriden by an inherited class) assumes that each screen 
     * will likely display a button that can bring the user back to the app's home screen. 
     * 
     * @param v The button that invoked this handler
     * @return Nothing
     */							
	public void onClickHome (View v)
	{
	    goHome (this);
	}

    /**
     * This handler (to be overriden by an inherited class) assumes that each screen 
     * will likely display a button that can invoke the SearchActivity class (which
     * allows the user to search the data cache with specific text).
     * 
     * @param v The button that invoked this handler
     * @return Nothing
     */								
	public void onClickSearch (View v)
	{
	    startActivity (new Intent(getApplicationContext(), SearchActivity.class));
	}
	
    /**
     * This handler (to be overriden by an inherited class) assumes that each screen 
     * will likely display a button that can invoke the AboutActivity class (which
     * allows the user to read a short description about the application).
     * 
     * @param v The button that invoked this handler
     * @return Nothing
     */								
	public void onClickAbout (View v)
	{
	    startActivity (new Intent(getApplicationContext(), AboutActivity.class));
	}

    /**
     * This handler is mainly used by the HomeActivity screen for any general 
     * user interaction.  If the user hits certain buttons and has only downloaded
     * the trial version of the application, then this handler will inform the user
     * that such functionality is only available in the paid version; otherwise, 
     * the app will invoke the Activity that corresponds to the button (i.e., the
     * F*Activity classes).
     * 
     * @param v The button that invoked this handler
     * @return Nothing
     */									
	public void onClickFeature (View v)
	{
		boolean bCompleteExecution = true;
		
	    int id = v.getId ();

	    switch (id) {	    
	      case R.id.home_btn_feature1 :
	      case R.id.home_btn_feature2 :
	      case R.id.home_btn_feature3 :
	      case R.id.home_btn_feature5 :	    	  
	    	   if (!BibliophileBase.CONST_FULL_VERSION_FLAG) {
	    		   
					new AlertDialog.Builder(DashboardActivity.this)
					.setTitle("UPGRADE REQUIRED")
					.setMessage("This feature is only available with the paid version.")
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dlg, int s) {}
					})
					.show();	    		   
	    		   
	    		   bCompleteExecution = false;
	    	   }	    	   
	           break;
	      default: 
	    	   break;
	    }	    

	    if (bCompleteExecution) {
		    switch (id) {
		      case R.id.home_btn_feature1 :
		           startActivity (new Intent(getApplicationContext(), F1Activity.class));
		           break;
		      case R.id.home_btn_feature2 :
		           startActivity (new Intent(getApplicationContext(), F2Activity.class));
		           break;
		      case R.id.home_btn_feature3 :
		           startActivity (new Intent(getApplicationContext(), F3Activity.class));
		           break;
		      case R.id.home_btn_feature4 :
		           startActivity (new Intent(getApplicationContext(), F4Activity.class));
		           break;
		      case R.id.home_btn_feature5 :
		           startActivity (new Intent(getApplicationContext(), F5Activity.class));
		           break;
		      case R.id.home_btn_feature6 :
		           startActivity (new Intent(getApplicationContext(), F6Activity.class));
		           break;
		      default: 
		    	   break;
		    }
	    }
	}

    /**
     * This function serves to bring the user from the current Activity back to the app's
     * home screen (i.e., the HomeActivity).
     * 
     * @param context The current context of the application
     * @return Nothing
     */				
	public void goHome(Context context) 
	{
	    final Intent intent = new Intent(context, HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    context.startActivity (intent);
	}

    /**
     * This function sets a text box with the current title of the Activity.
     * 
     * @param textViewId The identifier of the targeted text box
     * @return Nothing
     */					
	public void setTitleFromActivityLabel (int textViewId)
	{
	    TextView tv = (TextView) findViewById (textViewId);
	    if (tv != null) tv.setText (getTitle ());
	} // end setTitleText

    /**
     * This general helper allows you to present a small dialog box.
     * 
     * @param msg The message to display in the dialog box
     * @return Nothing
     */						
	public void toast (String msg)
	{
	    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} // end toast

    /**
     * This general helper allows you to log a message and present it within 
     * a small dialog box.
     * 
     * @param msg The message to display in the dialog box
     * @return Nothing
     */							
	public void trace (String msg) 
	{
	    Log.d("Demo", msg);
	    toast (msg);
	}
	
	
	//
	// STATIC METHODS
    //

    /**
     * This method will return the image ID for a specific genre of books.  This image
     * will be placed in its respective row within a screen's list
     * 
     * @param bookType The target name for the specific genre of books
     * @return The ID of the image that represents that book genre
     */
	public static int getBookTypeImageIcon(String bookType) {
		
		if ( bookType == null) {
			return R.drawable.fiction;
	    }
				
		if ( bookType == CONST_BOOK_TYPE_SCIFI ) {
			return R.drawable.scifi;
	    }
		else if ( bookType == CONST_BOOK_TYPE_FICTION ) {
			return R.drawable.fiction;
	    }
		else if ( bookType == CONST_BOOK_TYPE_HISTORY ) {
			return R.drawable.history;
	    }
		else if ( bookType == CONST_BOOK_TYPE_POETRY ) {
			return R.drawable.poetry;
	    }
		else if ( bookType == CONST_BOOK_TYPE_CLASSIC_LIT ) {
			return R.drawable.classic;
	    }
		else if ( bookType == CONST_BOOK_TYPE_ANCIENT_TXT ) {
			return R.drawable.ancient;
	    }
		else if ( bookType == CONST_BOOK_TYPE_BIOGRAPHY ) {
			return R.drawable.biography;
	    }
		else if ( bookType == CONST_BOOK_TYPE_HUMOR ) {
			return R.drawable.humor;
	    }
		else if ( bookType == CONST_BOOK_TYPE_CHILD_LIT ) {
			return R.drawable.children;
	    }
		else if ( bookType == CONST_BOOK_TYPE_NON_FICTION ) {
			return R.drawable.non_fiction;
	    }
		else {
			return R.drawable.fiction;
	    }    			
    }

    /**
     * This general helper function tests to see if the screen currently
     * has access to an available network.
     * 
     * @return Indicator of whether or not a network is available
     */						
	public boolean isNetworkAvailable() {
		
		boolean bIsAvailable = false;
		
		try {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    
		    if ((activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting())
		    	bIsAvailable = true;
		}
		catch (Throwable e) {
			
			// NOTE: What 
	    }
		
		return bIsAvailable;
	}
	
	// HELPER CLASSES

	/**
	 * This general subclass serves as the logical view for any text-only rows in a presented list.
	 * 
	 * Since it supplies only basic functionality, it will likely not be used by any classes
	 * that inherit this one.
	 */    
    class RowModel { 
    	
    	String label;
    	
    	RowModel(String label) {
    		this.label = label;
        }
    	
    	public String toString() {    		    		
    		return (label);
    	}
    }

	/**
	 * This general subclass serves as the logical view for any rows with text and images 
	 * in a presented list.
	 * 
	 * Since it supplies only basic functionality, it will likely not be used by any classes
	 * that inherit this one.
	 */        
    class SelectTypeViewWrapper {
    	
    	View      base;
    	
    	ImageView   typeImg        = null;
    	TextView    label          = null;
    	
    	SelectTypeViewWrapper(View base) {
    		this.base = base;
        }
    	
    	ImageView getTypeImage() {
    		
    		if (typeImg==null) {
    			typeImg = (ImageView) base.findViewById(R.id.selectTypeImage);    			
    	    }
    		
    		return typeImg;
    	}
    	    	
    	TextView getLabel() {
    		
    		if (label==null) {
    			label=(TextView) base.findViewById(R.id.selectTypeLabel);
    	    }
    		
    		return (label);
        }    	    	
    }

	/**
	 * This general subclass serves as the logical view for any long-text rows in a presented list.
	 * 
	 * Since it supplies only basic functionality, it will likely not be used by any classes
	 * that inherit this one.
	 */    
    class BasicViewWrapper {
    	
    	View      base;
    	
    	TextView  label = null;
    	
    	BasicViewWrapper(View base) {
    		this.base = base;
        }
    	    	    	
    	TextView getLabel() {
    		
    		if (label==null) {
    			label=(TextView) base.findViewById(R.id.basicLabel);
    	    }
    		
    		return (label);
        }    	    	
    }

	/**
	 * This general subclass serves as the logical view for any displayed lists within a screen.
	 * A list will allow the user to interact with its contained rows.
	 * 
	 * Since it supplies only basic functionality, it will likely not be used by any classes
	 * that inherit this one.
	 *  
	 */			
    class BasicAdapter extends ArrayAdapter<RowModel> {    	    	
    	
    	BasicAdapter(ArrayList<RowModel> list) {
    		    		
    		super(DashboardActivity.this, R.layout.basic_row, list);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		BasicViewWrapper wrapper;
    		TextView         typeText;
    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.basic_row, parent, false);
	    			
	    			wrapper = new BasicViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			typeText = wrapper.getLabel();
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
									// TODO Auto-generated method stub							
									Integer myPosition = (Integer) v.getTag();							
									String  TmpData    = "";

									TmpData =
											BasicAdapter.this.getItem(myPosition).toString();									
									
									new AlertDialog.Builder(DashboardActivity.this)
									.setTitle("EXPANDED")
									.setMessage(TmpData)
									.setNeutralButton("Done", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dlg, int s) {}
									})
									.show();
																		
							}
					};
							
					// wrapper.base.setOnClickListener(typeClick);
					typeText.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(DashboardActivity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new BasicViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (BasicViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());

    		wrapper.getLabel().setTag(position);
    		    		    		    		
    		return (row);
        }
    }


} // end class
