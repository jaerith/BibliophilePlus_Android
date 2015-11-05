package com.firstdomino.BibliophilePlusDeluxe;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * This class generates a pie chart that displays the percentage of influence that
 * certain books or book subjects have on another subfield of media (a song type, a game type, etc.).
 * 
 * For example, one pie chart may show that our database's breakdown for classic rock has 
 * 25% influenced by classic fiction, 20% influenced by fantasy, 15% influenced by science fiction, etc.  
 * 
 * This class is used solely by the ChartTabListActivity and ChartTopXTabListActivity classes (which are
 * employed under the F4Activity class).
 */
public class ChartStatsActivity extends ChartActivityBase {
	
    public String getName() {
		
	    return "Statistics";
	}

    public String getDesc() {
		
	    return "Statistics in regard to book types/song types";
	}

    /**
     * Builds an empty pie chart as a control test.
     * 
     * @return The Andriod Intent dispatched that will trigger the actual chart Activity.
     */	
    public Intent execute(Context context) 
	{
		return execute(context, "TEST", null, null);
	}

    /**
     * This method will generate the correct Android Intent class for pie chart generation, which
     * which will then be used by the caller to call "startActivity()" in order to actually create
     * the pie chart (via the listening third-party library).
     * 
     * @param context     The context of the current application
     * @param screenTitle The title of the pie chart
     * @param statTitles  The respective name of each pie chart section 
     * @param statValues  The respective percentage of each pie chart section
     * @return The Intent that's used to actually create the pie chart.
     */
    public Intent execute(Context context, String screenTitle, String[] statTitles, double[] statValues) {
		
		String[] titles = null;
	    double[] values = null;
	    int[]    colors = null;
	    
	    if (statValues != null) {
	        values = statValues;
	        titles = statTitles;
	    }
	    else {
	    	values = new double[] { 12, 14, 11, 10, 19 };
	    	titles = new String[] { "Alpha", "Beta", "Charlie", "Delta", "Epsilon" };
	    }
	    
	    if (values.length == 1)
	    	colors = new int[] { Color.BLUE };
	    else if (values.length == 2)
	        colors = new int[] { Color.BLUE, Color.GREEN };
	    else if (values.length == 3)
	        colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA };
	    else if (values.length == 4)
	        colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, };
	    else if (values.length == 5)
	        colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
	    else if (values.length == 6)
	    	colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN
	    		                 , Color.LTGRAY };
	    else if (values.length == 7)
	    	colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN
                                 , Color.LTGRAY, Color.RED };
	    else if (values.length == 8)
	    	colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN
                                 , Color.LTGRAY, Color.RED, Color.GRAY };
	    else if (values.length == 9)
	    	colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN
                                 , Color.LTGRAY, Color.RED, Color.GRAY, Color.DKGRAY };
	    else
	    	colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN
                                 , Color.LTGRAY, Color.RED, Color.GRAY, Color.DKGRAY, Color.rgb(128, 255, 0) };
	    
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    
	    renderer.setZoomButtonsVisible(false);
	    renderer.setZoomEnabled(true);
	    
	    renderer.setBackgroundColor( Color.BLACK );	    
	    renderer.setApplyBackgroundColor(true);
	    
	    renderer.setChartTitle(screenTitle);
	    renderer.setChartTitleTextSize(22);
	    	    
	    return ChartFactory.getPieChartIntent(context, 
	    		                              buildCategoryDataset("Statistics Breakdown", values, titles),
	                                          renderer, 
	                                          screenTitle);
	}
}