package com.firstdomino.BibliophilePlusDeluxe;

import android.content.Context;
import android.content.Intent;

/**
 * This interface assists with defining the expected functionality of any
 * class that creates a chart's Intent.
 * 
 * This class is mainly used the ChartActivityBase class.
 */
public interface IChartActivity {
	
  /** A constant for the name field in a list activity. */
  String NAME = "name";
  
  /** A constant for the description field in a list activity. */
  String DESC = "desc";

  /**
   * Returns the chart name.
   * 
   * @return The name of the chart
   */
  String getName();

  /**
   * Returns the chart description.
   * 
   * @return The description of the chart
   */
  String getDesc();

  /**
   * Executes the chart demo.
   * 
   * @param context The context of the current app
   * @return The Intent that can be used to invoke the creation of a chart
   */
  Intent execute(Context context);

}
