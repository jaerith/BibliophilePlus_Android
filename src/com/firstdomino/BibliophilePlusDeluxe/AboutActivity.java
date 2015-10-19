package com.firstdomino.BibliophilePlusDeluxe;

import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class provides the textual content for the About box and describes the general purpose
 * of this application to the user.
 */
public class AboutActivity extends DashboardActivity 
{
    private TextView about_text;
    
    protected void onCreate(Bundle savedInstanceState) 
    {
    	StringBuilder stbAboutBody = new StringBuilder();
    	
        super.onCreate(savedInstanceState);

        setContentView (R.layout.activity_about);
        setTitleFromActivityLabel (R.id.title_text);
        
        about_text = (TextView) findViewById(R.id.about_text);
        
        stbAboutBody.append("Discover new dimensions of literature and history ");
        stbAboutBody.append("by using the following features which are described below: ");
                
        stbAboutBody.append("\n\n");
        
        stbAboutBody.append("BROWSE BOOKS\n\n");
        stbAboutBody.append("Browse through our catalog of books and immediately observe ");
        stbAboutBody.append("some interesting facts about books.\n");
        stbAboutBody.append("(When entries on a book's screen are selected, it will attempt to retrieve a Web page using an Internet connection.)\n");
        stbAboutBody.append("(When possible, this catalog is updated on an infrequent basis using an Internet connection).\n");
        stbAboutBody.append("\n");
        
        stbAboutBody.append("DISCOVER INFLUENCES\n\n");
        stbAboutBody.append("Browse through our song data in order to find out what books had ");
        stbAboutBody.append("an influence in their creation.\n");
        stbAboutBody.append("(When possible, this data is updated on an infrequent basis using an Internet connection).\n");
        stbAboutBody.append("\n");
        
        stbAboutBody.append("EXPLORE YOUR WORLD\n\n");
        stbAboutBody.append("Use the interactive map and select a spot on the globe in order to ");
        stbAboutBody.append("discover if any books were set in that location.\n");
        stbAboutBody.append("(An Internet connection is required to use this feature.)\n");        
        stbAboutBody.append("(For best results, do not expand the map using pinch motions.  Instead, ");
        stbAboutBody.append("use the (+/-) control on the map to zoom in/zoom out.)\n");
        stbAboutBody.append("\n");

        stbAboutBody.append("ANALYZE THE CHARTS\n\n");
        stbAboutBody.append("Peruse the generated charts in order to get a visual sense of how ");
        stbAboutBody.append("books have influenced songs and games, both by category and ");
        stbAboutBody.append("by individual work.\n");        
        stbAboutBody.append("\n");

        stbAboutBody.append("TEST YOUR KNOWLEDGE\n\n");
        stbAboutBody.append("Test your knowledge of the real-world dimensions of literature and history ");
        stbAboutBody.append("by taking a quiz.  Escalate the difficulty by turning on the timer and ");
        stbAboutBody.append("getting 75% correct in 75 seconds!\n");
        stbAboutBody.append("\n");

	    stbAboutBody.append("SEND US YOUR IDEAS\n\n");
        stbAboutBody.append("If you know something that should be in our database or know of a new feature ");
        stbAboutBody.append("that would be cool in this app, be sure to send us an email and let us know!\n");
        stbAboutBody.append("\n");
        
        about_text.setText(stbAboutBody.toString());
    }
    
} // end class
