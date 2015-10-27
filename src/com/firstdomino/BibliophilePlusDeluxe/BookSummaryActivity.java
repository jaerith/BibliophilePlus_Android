package com.firstdomino.BibliophilePlusDeluxe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class represents the screen that will be displayed when the user selects a book 
 * within one of the main activities (especially F1Activity and F2Activity).  The screen
 * will display all of the facts and interesting trivia associated with this book.
 * There will be four lists displayed by this screen:
 * 
 * The chosen book (which is a list of one)
 * The song trivia list
 * The game trivia list
 * The quote list
 * 
 * You can find its layout within the file '/layout/activity_book_summary.xml'.
 */
public class BookSummaryActivity extends DashboardActivity 
{
	public static BookOverviewItem tmpBookOverviewItem = null;

	private String                book;
	private String                bookData;
	private ArrayList<BiblioItem> sibbItems;
	private ArrayList<BiblioItem> gibbItems;
	private ArrayList<BiblioItem> quoteItems;
	private ArrayList<BiblioItem> locSettings;
	
	private ImageButton  purchaseButton;

	private LinearLayout sibbLayer;
	private LinearLayout gibbLayer;
	private LinearLayout quotesLayer; 
	
	private ListView selected_book_list;
	private ListView influenced_songs_list;
	private ListView influenced_games_list;
	private ListView available_quotes_list;
	private ListView location_settings_list;

	/**
	 * The standard Android routine to initialize an activity (i.e., a screen).
	 * 
	 * @param  savedInstanceState The class used to save state information about an activity.
	 * @return Nothing. 
	 */			
	protected void onCreate(Bundle savedInstanceState) 
	{
		int  i = 0;
		
		boolean bHideBuyButton = true;
		
		try {
		 
		    super.onCreate(savedInstanceState);
		    setContentView (R.layout.activity_book_summary);	    
		    setTitleFromActivityLabel (R.id.title_text);
		    
		    bookData    = tmpBookOverviewItem.sBookData;
		    sibbItems   = (ArrayList<BiblioItem>) tmpBookOverviewItem.sibbItems.clone();
		    gibbItems   = (ArrayList<BiblioItem>) tmpBookOverviewItem.gibbItems.clone();
		    quoteItems  = (ArrayList<BiblioItem>) tmpBookOverviewItem.quoteItems.clone();
		    locSettings = (ArrayList<BiblioItem>) tmpBookOverviewItem.locItems.clone();
		    
		    purchaseButton = (ImageButton) findViewById(R.id.purchase_button);
		    
		    selected_book_list     = (ListView) findViewById(R.id.sel_book_list);
			influenced_songs_list  = (ListView) findViewById(R.id.infl_songs_list);
			influenced_games_list  = (ListView) findViewById(R.id.infl_games_list);
			available_quotes_list  = (ListView) findViewById(R.id.avail_quotes_list);
			// location_settings_list = (ListView) findViewById(R.id.loc_sets_list);
			
		    sibbLayer   = (LinearLayout) findViewById(R.id.sibb_layer);
	        gibbLayer   = (LinearLayout) findViewById(R.id.gibb_layer);
	        quotesLayer = (LinearLayout) findViewById(R.id.quotes_layer);
			
			influenced_songs_list.setScrollbarFadingEnabled(false);
			influenced_games_list.setScrollbarFadingEnabled(false);
			available_quotes_list.setScrollbarFadingEnabled(false);
			
			int    nIdx      = -1;
			String sBookData = tmpBookOverviewItem.sBookData;
			if ((nIdx = sBookData.lastIndexOf(BibliophileBase.CONST_WRITTEN_BY_VAR)) > 0)
				sBookData = sBookData.substring(0, nIdx);
			
			if ((nIdx = sBookData.lastIndexOf(BibliophileBase.BOOK_INFO_DELIM)) > 0)
				book = sBookData.substring(0, nIdx).trim();
			else 
				book = sBookData;

    		if (HomeActivity.bOnBNDevice == false)
    			bHideBuyButton = true;
    		else if (BibliophileBase.eanList.containsKey(book))
    			bHideBuyButton = false;
						
			if (!isNetworkAvailable()) { 
				purchaseButton.setVisibility(View.GONE);
			}
			// NOTE: Temporary measure
			else if (bHideBuyButton) {
				purchaseButton.setVisibility(View.GONE);
		    }
			
			ArrayList<RowModel> oBookEntryModelList = new ArrayList<RowModel>();
			
			oBookEntryModelList.add(new RowModel(sBookData));
			selected_book_list.setAdapter(new BookSummaryAdapter(oBookEntryModelList,
					                                             BookSummaryAdapter.CONST_BK_SUMM_SECTION_BOOKS));

			if (sibbItems.size() > 0) {
				
				ArrayList<RowModel> oSongModelList = new ArrayList<RowModel>();
				
				for (i = 0; i < sibbItems.size(); ++i) 
				{
					BiblioItem oTmpBibItem = sibbItems.get(i);
					
					oSongModelList.add(new RowModel(oTmpBibItem.sOtherData));
				}
				
				influenced_songs_list.setAdapter(new BookSummaryAdapter(oSongModelList, 
						                                                BookSummaryAdapter.CONST_BK_SUMM_SECTION_SONGS));
			}
			else {
				
				sibbLayer.setVisibility(View.GONE);
		    }
			
			if (gibbItems.size() > 0) {
				
				ArrayList<RowModel> oGameModelList = new ArrayList<RowModel>();
				
				for (i = 0; i < gibbItems.size(); ++i) 
				{
					BiblioItem oTmpBibItem = gibbItems.get(i);
					
					oGameModelList.add(new RowModel(oTmpBibItem.sOtherData));
				}
				
				influenced_games_list.setAdapter(new BookSummaryAdapter(oGameModelList, 
						                                                BookSummaryAdapter.CONST_BK_SUMM_SECTION_GAMES));
			}
			else {
				
				gibbLayer.setVisibility(View.GONE);
		    }

			if (quoteItems.size() > 0) {
				
				ArrayList<RowModel> oQuoteModelDataList = new ArrayList<RowModel>();
				
				for (i = 0; i < quoteItems.size(); ++i) 
				{
					BiblioItem oTmpBibItem = quoteItems.get(i);
					
					oQuoteModelDataList.add(new RowModel(oTmpBibItem.sOtherData));
				}
				
				available_quotes_list.setAdapter(new BasicAdapter(oQuoteModelDataList));
			}
			else {
				
				quotesLayer.setVisibility(View.GONE);
		    }				
		}
		catch (Throwable t) {
			
			Toast
			.makeText(this, "Request failed: "+t.toString(), 4000)
			.show();
	    }
	}

	/**
	 * Builds a message with various interesting facts about a book and with the intent of being
	 * the body of an email.  
	 *
	 * @return A message containing interesting facts about the book displayed in this screen. 
	 */			
	private String buildMessage() {
		
        int    nIdx          = 0;
		String sEmailMessage = new String();		
		String sBookData     = new String();
		
		if (sibbItems.size() > 0) {
			sBookData = sibbItems.get(0).sBookData;
	    }
		else {
			sBookData = gibbItems.get(0).sBookData;
	    }
		
		if ((nIdx = sBookData.lastIndexOf(BibliophileBase.CONST_WRITTEN_BY_VAR)) > 0)
			sBookData = sBookData.substring(0, nIdx);
		
		sEmailMessage += "  You've heard about or read " + sBookData +
				         ", right?\n\n";
				
		if (sibbItems.size() > 0) {
			
			sEmailMessage += "  Did you know that it inspired the following song(s):\n";
			
			if (sibbItems.size() > 1)
			    sEmailMessage += "\n---\n\n";
			
			for (BiblioItem oTmpSongItem : sibbItems) {
				
				sEmailMessage += "\n" + oTmpSongItem.sOtherData + "\n";				
		    }
						
			sEmailMessage += "\n";
			
			if (sibbItems.size() > 1)
			    sEmailMessage += "---\n\n";
		}
		
		if (gibbItems.size() > 0) {
						
			sEmailMessage += "  Did you know that it inspired the following game(s):\n";
			
			if (sibbItems.size() > 1)
			    sEmailMessage += "\n---\n\n";
			
			for (BiblioItem oTmpSongItem : gibbItems) {
				
				sEmailMessage += "\n" + oTmpSongItem.sOtherData + "\n";				
		    }
			
			sEmailMessage += "\n";
			
			if (sibbItems.size() > 1)
			    sEmailMessage += "---\n\n";
		}
		
		sEmailMessage += "  Isn't that cool?";
		
		sEmailMessage += "\n\n";
		sEmailMessage += "\t\t\t\t\t\t\t\tSent by Bibliophile+ on the Nook\n";
		sEmailMessage += "\t\t\t\t\t\t\t\thttp://www.facebook.com/BibliophilePlus";
		
		return sEmailMessage;
    } 

	/**
	 * This handler will be invoked when the user clicks on the Email button on this screen.
	 * It will prepare an email body with interesting facts about the book displayed by this screen,
	 * and then it will invoke an Email activity so that the user can send that email body to a friend.   
	 *
	 * @param  v The view (i.e., the email button) associated this handler. 
	 * @return Nothing. 
	 */			
	public void onClickEmail (View v)
	{
		String sEmailMessage = buildMessage();
		Intent i             = new Intent(Intent.ACTION_SEND);
		
		i.setType("text/plain");
		
		i.putExtra(Intent.EXTRA_EMAIL, 
				   new String[]{"my_friend@theiremailaccount.com"});
		
		i.putExtra(Intent.EXTRA_SUBJECT, 
				   "Hey, Check This Out" );
		
		i.putExtra(Intent.EXTRA_TEXT, sEmailMessage);
		
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} 
		catch (android.content.ActivityNotFoundException ex) {
			
			new AlertDialog.Builder(BookSummaryActivity.this)
			.setTitle("ERROR!")
			.setMessage("Email failed to be sent due to activity not being found:\n(" + ex + ")")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
		}
		catch (Throwable ex){
			
			new AlertDialog.Builder(BookSummaryActivity.this)
			.setTitle("ERROR!")
			.setMessage("Email failed to be sent:\n(" + ex + ")")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
		}
	}

	/**
	 * This handler will be invoked when the user clicks on the Purchase button on this screen.
	 * It attempts to load the Barnes & Noble app with an EAN associated with the book displayed
	 * on this screen, so that the user may buy the book.
	 * 
	 * Unfortunately, this particular functionality was never brought to fruition, and it has yet
	 * to work properly.
	 *
	 * @param  v The view (i.e., the Purchase button) associated with this handler.
	 * @return Nothing. 
	 */			
    public void onClickPurchase (View v)
	{
    	if (isNetworkAvailable()) {
    	
    		if (BibliophileBase.eanList.containsKey(book)) {
    		
    			String sEan = (String) BibliophileBase.eanList.get(book);
    			    			
	    		try {
			        Intent i = new Intent();
			
			        i.setAction( "com.bn.sdk.shop.details" );
			
			        i.putExtra( "product_details_ean" , sEan );
			
			        startActivity( i );
	    		}
	    		catch (Throwable t)
	    		{
	    			int x = 0;
	    		}
    		}
    	}
	}

	/**
	 * Under construction. 
	 *
	 * @param  v The view (i.e., the Purchase button) associated with this handler.
	 * @return Nothing. 
	 */			
	public void onClickSearch (View v)
	{
		// NOTE: Figure out what to do here
	    // startActivity (new Intent(getApplicationContext(), SearchActivity.class));
	}
	
	/**
	 * This subclass serves as the logical view (i.e., list row) for each trivia item. 
	 */			
    class BookSummaryViewWrapper {
    	
    	View      base;
    	
    	TextView  label = null;
    	ImageView image = null;
    	
    	BookSummaryViewWrapper(View base) {
    		this.base = base;
        }
    	    	    	
    	TextView getLabel() {
    		
    		if (label==null) {
    			label=(TextView) base.findViewById(R.id.bookOverviewLabel);
    	    }
    		
    		return (label);
        }
    	
    	ImageView getImage() {
    		
    		if (image==null) {
    			image=(ImageView) base.findViewById(R.id.bookOverviewImage);
    	    }
    		
    		return (image);
        }    	    	

    }

	/**
	 * This subclass serves as the logical view for each displayed list within this Activity.
	 * A list will allow the user to interact with its contained rows.  For instance, if the user
	 * select a song trivia row item, it will attempt to call a URL that shows the lyrics of that
	 * particular song.  If the user selects a game trivia item, it will attempt to call a Wikipedia URL
	 * that describes the mentioned game.
	 */			
    class BookSummaryAdapter extends ArrayAdapter<RowModel> {
    	
    	public static final String CONST_BK_SUMM_SECTION_BOOKS = "B";
    	public static final String CONST_BK_SUMM_SECTION_SONGS = "S";
    	public static final String CONST_BK_SUMM_SECTION_GAMES = "G";
    	
    	private String bookSummarySection = "?";
    	
    	BookSummaryAdapter(ArrayList<RowModel> list, String psBookSummarySection) {
    		    		
    		super(BookSummaryActivity.this, R.layout.book_overview_row, list);
    		
    		bookSummarySection = psBookSummarySection;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		
    		int                    nImageId;
    		BookSummaryViewWrapper wrapper;
    		TextView               targetText;
    		ImageView              arrowButton;
    		    		
    		if (row == null) {
    			
    			try {
    				
	    			LayoutInflater inflater = getLayoutInflater();
	    			
	    			row = inflater.inflate(R.layout.book_overview_row, parent, false);
	    			
	    			wrapper = new BookSummaryViewWrapper(row);
	    			
	    			row.setTag(wrapper);
	    			
	    			targetText  = wrapper.getLabel();
	    			arrowButton = wrapper.getImage();
	    			
                    if (!BibliophileBase.altDefaultEnabled)
	    			    arrowButton.setVisibility(View.INVISIBLE);
	    			
	    			View.OnClickListener typeClick =	    					
		    			new View.OnClickListener() {
								
							public void onClick(View v) {
									
									// TODO Auto-generated method stub							
									Integer myPosition = (Integer) v.getTag();							
									String  TmpType    = "";
									String  targetUrl  = "";
									
									try {

										TmpType =
											BookSummaryAdapter.this.getItem(myPosition).toString();
										
										if (BookSummaryAdapter.this.bookSummarySection == BookSummaryAdapter.CONST_BK_SUMM_SECTION_BOOKS) {
											
											if (BookSummaryActivity.this.sibbItems.size() > 0)
											    targetUrl = BookSummaryActivity.this.sibbItems.get(myPosition).getBL();											
											else if (BookSummaryActivity.this.gibbItems.size() > 0)
												targetUrl = BookSummaryActivity.this.gibbItems.get(myPosition).getBL();
											else {
											    if (BookSummaryActivity.this.bookData.length() > 0) {
											    	
											        BiblioItem tmpItem = 
											        		new BiblioItem(BookSummaryActivity.this.bookData, "");
											        
												    targetUrl = tmpItem.getBL();
											    }
											}
										}
										else if (BookSummaryAdapter.this.bookSummarySection == BookSummaryAdapter.CONST_BK_SUMM_SECTION_SONGS)
										    targetUrl = BookSummaryActivity.this.sibbItems.get(myPosition).getOL();
										else
											targetUrl = BookSummaryActivity.this.gibbItems.get(myPosition).getOL();
									}
									catch (Throwable e) {
										
										new AlertDialog.Builder(BookSummaryActivity.this)
										.setTitle("DEBUG")
										.setMessage("ERROR!  Unable to find the relevant URL.")
										.setNeutralButton("Done", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dlg, int s) {}
										})
										.show();
								    }
									
									if (targetUrl.length() > 0) {
										
										if (BibliophileBase.defaultEnabled) {
											
											WebViewActivity.tmpLoadUrl = targetUrl;
											
											try {
											     startActivity (new Intent(BookSummaryActivity.this.getApplicationContext(), WebViewActivity.class));
											}
											catch (Throwable e) {
												new AlertDialog.Builder(BookSummaryActivity.this)
												.setTitle("DEBUG")
												.setMessage("ERROR!  Could not load URL(" + targetUrl + ")\n" + e.toString())
												.setNeutralButton("Done", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dlg, int s) {}
												})
												.show();
										    }
										}
									}									
							}
					};
							
					targetText.setOnClickListener(typeClick);
					arrowButton.setOnClickListener(typeClick);
			    }
    			catch (Throwable t) {
    				
    				new AlertDialog.Builder(BookSummaryActivity.this)
    				.setTitle("Warning")
    				.setMessage("Request failed: "+t)
    				.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dlg, int s) {}
    				})
    				.show();
    				
	    			wrapper = new BookSummaryViewWrapper(row);
    		    }
    		}
    		else {
    			
    			wrapper = (BookSummaryViewWrapper) row.getTag();
    		}
    		
    		RowModel model = getItem(position);
    		wrapper.getLabel().setText(model.toString());
			
    		wrapper.getLabel().setTag(position);
    		wrapper.getImage().setTag(position);
    		    		    		    		
    		return (row);
        }
    }
    
} // end class

