package com.firstdomino.BibliophilePlusDeluxe;

import android.accounts.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.*;

/**
 * This class represents the Send Us Your Ideas screen, which allows the user 
 * to submit book trivia not found yet in our catalog and to submit ideas 
 * that could be used to enhance the app.
 */
public class F6Activity extends DashboardActivity 
{
	private EditText  edit_email_address;
	private Spinner   spinner_submission_type;
	private EditText  edit_email_body;
	private CheckBox  chkbx_response;
	private Button    button_send_feedback;
	
	public static final Pattern EMAIL_ADDRESS
        = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        );

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the various controls needed to submit the user's suggestion.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_f6);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    edit_email_address      = (EditText) findViewById(R.id.AddressEditView);
	    spinner_submission_type = (Spinner)  findViewById(R.id.FeedbackTypeSpinner);
	    edit_email_body         = (EditText) findViewById(R.id.FeedbackBodyEditText);
	    chkbx_response          = (CheckBox) findViewById(R.id.FeedbackResponseCheckBox);
	    button_send_feedback    = (Button)   findViewById(R.id.SendFeedbackButton);
	    
	    // NOTE: Decided that this section is unnecessary (for now)
	    edit_email_address.setVisibility(View.GONE);	    
	}

    /**
     * This handler will package the contents of the various controls on this page
     * in order to submit the user's suggestion in the form of an email.
     * 
     * @param v The button that called this handler
     * @return Nothing
     */
	public void onClickSendFeedback(View view)
	{
		String sEmailMessage = new String();
		
		sEmailMessage += edit_email_body.getText().toString();
		
		if (chkbx_response.isChecked())
		{
			sEmailMessage += "\n\nPlease respond to my preferred email address of (" +
					         edit_email_address.getText().toString() + 
					         ") or to the email address from which this message was sent.";
		}
				
		Intent i = new Intent(Intent.ACTION_SEND);
		
		i.setType("text/plain");
		
		i.putExtra(Intent.EXTRA_EMAIL  , 
				   new String[]{"submissions@bibliophileonline.com"});
		
		i.putExtra(Intent.EXTRA_SUBJECT, 
				   spinner_submission_type.getSelectedItem().toString() );
		
		i.putExtra(Intent.EXTRA_TEXT, sEmailMessage);
		
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} 
		catch (android.content.ActivityNotFoundException ex) {
			
			new AlertDialog.Builder(F6Activity.this)
			.setTitle("ERROR!")
			.setMessage("Email failed to be sent due to activity not being found:\n(" + ex + ")")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
		}
		catch (Throwable ex){
			
			new AlertDialog.Builder(F6Activity.this)
			.setTitle("ERROR!")
			.setMessage("Email failed to be sent:\n(" + ex + ")")
			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dlg, int s) {}
			})
			.show();
		}				
	}
    
} // end class
