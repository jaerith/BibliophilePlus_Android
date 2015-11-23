package com.firstdomino.BibliophilePlusDeluxe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class represents the Test Your Knowledge screen, which allows the user 
 * to take a multiple-choice quiz in order to test their own knowledge of the
 * influence of books and to learn more about the book trivia in our data cache.
 * On top of keeping a score, this screen will also provide a timer so that the
 * user can try to beat a certain time.
 */
public class F5Activity extends DashboardActivity 
{
	public static final String CONST_ANSWER_TRUE  = "TRUE";
	public static final String CONST_ANSWER_FALSE = "FALSE";
	
	public static final int CONST_QUESTION_TYPE_BOOK   = 0;
	public static final int CONST_QUESTION_TYPE_BOOK_2 = 1;
	public static final int CONST_QUESTION_TYPE_AUTHOR = 2;
	public static final int CONST_QUESTION_TYPE_SONG   = 3;
	public static final int CONST_QUESTION_TYPE_LOC    = 4;
	public static final int CONST_QUESTION_TYPE_MAX    = 5;
	
	private static ArrayList<QuizQuestion> oQuizQuestions     = new ArrayList<QuizQuestion>();
	private static int                     nCurrQuestionIndex = -1;
	
	private static int  nAnsweredRight = 0;
	private static int  nAnsweredWrong = 0;
	
	private static long mStartTime = 0L;
	
	private TextView  textQuestion;
	
	private Button    buttonFirstAnswer;
	private ImageView iconFirstAnswer;
	
	private Button    buttonSecondAnswer;
	private ImageView iconSecondAnswer;
	
	private Button    buttonThirdAnswer;
	private ImageView iconThirdAnswer;
	
	private Button    buttonPreviousQuestion;
	private Button    buttonNextQuestion;
	
	private TextView  textTimer;
	private TextView  textScoreboardRight;
    private TextView  textScoreboardPct;
    private TextView  textScoreboardWrong;
	
	private Button    buttonMoreAboutAnswer;
	private Button    buttonReset;
	private Button    buttonResetTitleBar;
	
	private Random    generator;	
	private Handler   mHandler;	
	private Runnable  mUpdateTimeTask;

    /**
     * Standard Android method called to invoke the Activity class.  It will prepare
     * the various controls that the user will use to play the trivia game.
     * 
     * @param savedInstanceState Data that represents the state of the app
     * @return Nothing
     */
	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_f5);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    textQuestion = (TextView) findViewById(R.id.quiz_question_text);
	    
	    buttonFirstAnswer = (Button) findViewById(R.id.quiz_answer_one);
	    iconFirstAnswer   = (ImageView) findViewById(R.id.quiz_answer_one_icon);
        
	    buttonSecondAnswer = (Button) findViewById(R.id.quiz_answer_two);
	    iconSecondAnswer   = (ImageView) findViewById(R.id.quiz_answer_two_icon);
	    
	    buttonThirdAnswer = (Button) findViewById(R.id.quiz_answer_three);
	    iconThirdAnswer   = (ImageView) findViewById(R.id.quiz_answer_three_icon);

	    buttonPreviousQuestion = (Button) findViewById(R.id.backward_button);
	    buttonNextQuestion     = (Button) findViewById(R.id.forward_button);
	    
		textTimer           = (TextView) findViewById(R.id.timer_text);
		textScoreboardRight = (TextView) findViewById(R.id.score_right_text);
        textScoreboardPct   = (TextView) findViewById(R.id.score_pct_text);
        textScoreboardWrong = (TextView) findViewById(R.id.score_wrong_text);

		buttonMoreAboutAnswer = (Button) findViewById(R.id.more_about_answer_button);
		buttonReset           = (Button) findViewById(R.id.reset_button);
		buttonResetTitleBar   = (Button) findViewById(R.id.title_bar_reset_button);
				
		generator = new Random();
		mHandler  = new Handler();
		
		if (nCurrQuestionIndex < 0)
		{
			resetIcons();
			
			resetButtons();
			
			++nCurrQuestionIndex;
			
		    nextQuestion();
		}
		
		mUpdateTimeTask = new Runnable() {
            public void run() {
            	
                long millis  = System.currentTimeMillis() - mStartTime;
                int  seconds = (int) (millis / 1000);
                int  minutes = seconds / 60;

                if (minutes < 10000) {
	                seconds = seconds % 60;
	
			        if (seconds < 10) {
			            textTimer.setText("" + minutes + ":0" + seconds);
			        } else {
			            textTimer.setText("" + minutes + ":" + seconds);            
			        }			        
                }
				
				mHandler.postDelayed(mUpdateTimeTask, 1000);
            }
        };
        
        textTimer.setOnClickListener(new OnClickListener(){
        	
            public void onClick(View v) {
				if (mStartTime == 0L) {
				    mStartTime = System.currentTimeMillis();
				    mHandler.removeCallbacks(mUpdateTimeTask);
				    mHandler.postDelayed(mUpdateTimeTask, 1000);
				}
				else {
					mHandler.removeCallbacks(mUpdateTimeTask);
					mStartTime = 0L;
				}            	
            }
        });
        
		resetTimer();
		
		updateScoreboard();
		
		displayQuestion(nCurrQuestionIndex);
		
		if (HomeActivity.bOnBNDevice)
			buttonResetTitleBar.setVisibility(View.INVISIBLE);
		else 
			buttonReset.setVisibility(View.INVISIBLE);
	}
	
    /**
     * This handler will submit the user's answer to a question, which will
     * then be evaluated.  It will show if the user's choice is correct
     * and then update the scoreboard.
     * 
     * @param v The button that called this handler
     * @return Nothing
     */
	public void onClickAnswer (View v)
	{
		int    nAnswerIdx;
		String sMessage;
		
	    int id = v.getId ();
	    switch (id) {
	      case R.id.quiz_answer_one :
	    	   nAnswerIdx = 0;
	           // sMessage = "Answer One = (" + v.toString() + ")";
	           break;
	      case R.id.quiz_answer_two :
	    	   nAnswerIdx = 1;
	    	   // sMessage = "Answer Two = (" + v.toString() + ")";
	           break;
	      case R.id.quiz_answer_three :
	    	   nAnswerIdx = 2;
	    	   // sMessage = "Answer Three = (" + v.toString() + ")";
	           break;
	      default: 
	    	   nAnswerIdx = -1;
	    	   break;
	    }
	    
	    QuizQuestion currQuestion = oQuizQuestions.get(nCurrQuestionIndex);
	    if (currQuestion.isAnswered)
	    	return;
	    else
	    	currQuestion.isAnswered = true;
	    
	    String sSelectedAnswer = currQuestion.answerKeys.get(nAnswerIdx);
	    
	    if (currQuestion.answers.get(sSelectedAnswer) == CONST_ANSWER_TRUE) {
	    	++nAnsweredRight;
	    }
	    else {
	        ++nAnsweredWrong;
	    }
	    
        setAnswerIcons(currQuestion);

	    buttonMoreAboutAnswer.setBackgroundResource(R.drawable.quiz_maa_button);
	    buttonMoreAboutAnswer.setEnabled(true);
	    
	    updateScoreboard();	    
	}

    /**
     * If the user has already answered at least one trivia question, this handler
     * will display the previous question that was answered by the user (along
     * with the results of the user's choice).
     * 
     * @param v The button that called this handler
     * @return Nothing
     */
	public void onClickPreviousQuestion(View v) 
	{
		if (nCurrQuestionIndex > 0)
		{
            --nCurrQuestionIndex;
            
            displayQuestion(nCurrQuestionIndex);
		}		
	}

    /**
     * This handler will generate a new trivia question (and possible answers)
     * for the user.
     * 
     * @param v The button that called this handler
     * @return Nothing
     */	
	public void onClickNextQuestion(View v) 
	{
		QuizQuestion currQuestion = oQuizQuestions.get(nCurrQuestionIndex);
		if (!currQuestion.isAnswered)
			return;
		
	    ++nCurrQuestionIndex;
	    	    
	    if (nCurrQuestionIndex < oQuizQuestions.size())
	    {
	        QuizQuestion nextCurrQuestion = oQuizQuestions.get(nCurrQuestionIndex);
	        
	        if (!nextCurrQuestion.isAnswered)
	        {
	    	    resetIcons();
	    	    
                resetButtons();
	        }
	    }
	    else if (nCurrQuestionIndex >= oQuizQuestions.size())
	    {
  		    updateScoreboard();
		    resetIcons();
		
	        nextQuestion();
	    }

	    displayQuestion(nCurrQuestionIndex);	    
	}
	
    /**
     * Once the user has answered the trivia question, this handler will allow the user 
     * to retrieve all of the trivia data related to the book in the question. 
     * 
     * @param v The button that called this handler
     * @return Nothing
     */		
	public void onClickMoreAboutAnswer(View v) 
	{
	    QuizQuestion currQuestion = oQuizQuestions.get(nCurrQuestionIndex);
		
	    if (currQuestion.bookData.length() > 0) {
	    	
			if (BibliophileBase.bookOverviewItems.containsKey(currQuestion.bookData)) {																			
			
				BookOverviewItem oTmpOverviewItem = 
						BibliophileBase.bookOverviewItems.get(currQuestion.bookData);
														
				BookSummaryActivity.tmpBookOverviewItem = oTmpOverviewItem;
				
			    startActivity (new Intent(F5Activity.this.getApplicationContext(), BookSummaryActivity.class));
			}	    	
	    }
	}

    /**
     * This handler will reset the trivia quiz, meaning that it will:
     * 
     * 1.) erase any questions and answers that had been presented to the user.
     * 2.) reset the scoreboard
     * 3.) reset the timer 
     * 
     * @param v The button that called this handler
     * @return Nothing
     */			
	public void onClickQuizReset(View v) 
	{
		new AlertDialog.Builder(this)
		.setTitle("WARNING")
		.setMessage("You have indicated that the quiz should be reset.  Are you sure that you wish to reset the quiz?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dlg, int s) {
		    	
		        oQuizQuestions     = new ArrayList<QuizQuestion>();
                nCurrQuestionIndex = -1;
	
	            nAnsweredRight = nAnsweredWrong = 0;
	            mStartTime = 0L;
		
		        resetIcons();
		
		        resetButtons();
		
		        ++nCurrQuestionIndex;
		
	            nextQuestion();
	    
		        resetTimer();
		
		        updateScoreboard();
		
		        displayQuestion(nCurrQuestionIndex);
		    }
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dlg, int s) {}
		})
		.show();		
	}

    /**
     * Under construction
     * 
     * @param v The button that called this handler
     * @return Nothing
     */				
	public void onClickSubmitScore(View v) 
	{
		new AlertDialog.Builder(this)
		.setTitle("DEBUG")
		.setMessage("F5Activity::onClickSubmitScore()")
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dlg, int s) {}
		})
		.show();
	}

    /**
     * This handler will start/stop the timer that can be used to 
     * add a new competitive dimension to the trivia quiz.
     * 
     * @param v The button that called this handler
     * @return Nothing
     */			
	public void onClickTimer(View v)
	{
       if (mStartTime == 0L) {
            mStartTime = System.currentTimeMillis();
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 100);
       }
       else {
    	   mHandler.removeCallbacks(mUpdateTimeTask);
    	   mStartTime = 0L;
	   }
	}

    /**
     * This method will display the question (and any associated results)
     * contained in the current trivia quiz.
     * 
     * @param nQuestionIndex The index of the question within the current quiz cache
     * @return Nothing
     */
	private void displayQuestion(int nQuestionIndex)
	{
        if ((nQuestionIndex < 0) || (nQuestionIndex >= oQuizQuestions.size()))
        	return;
        
        QuizQuestion tmpQuestion = oQuizQuestions.get(nQuestionIndex);
                		
	    textQuestion.setText(tmpQuestion.textQuestion);
	    
	    buttonFirstAnswer.setText(tmpQuestion.answerKeys.get(0));
	    buttonSecondAnswer.setText(tmpQuestion.answerKeys.get(1));
	    buttonThirdAnswer.setText(tmpQuestion.answerKeys.get(2));
	    
	    if (tmpQuestion.isAnswered) 
	    {
            setAnswerIcons(tmpQuestion);
            
    	    buttonMoreAboutAnswer.setBackgroundResource(R.drawable.quiz_maa_button);
		    buttonMoreAboutAnswer.setEnabled(true);
	    }
	    else 
	    {
	    	resetIcons();

	    	resetButtons();
	    }
	}

    /**
     * This method will populate the provided QuizQuestion with a question (along
     * with potential answers) about a random author.  
     * 
     * @param poGenerator The random number generator used to randomly select an author
     * @param poQuizQuestion The class that we will populate with the question and answers 
     * @return Nothing
     */	
	private void generateAuthorQuestion(Random poGenerator, QuizQuestion poQuizQuestion)
	{
		boolean          bKeepLooking        = true;
		BookOverviewItem tmpBookOverviewItem = null;
		
		int    nBookIdx  = -1;
		int    nSongIdx  = -1;
		int    nGameIdx  = -1;
		int    nParanIdx = -1;
		
		String sBookData = "";
		String sAuthor   = "";
		
		while (bKeepLooking) {
			
			nBookIdx = Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
			
			sBookData = BibliophileBase.bookCatalog.get(nBookIdx);
						
			tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sBookData);

			if ((tmpBookOverviewItem.sibbItems.size() > 0) && 
				!tmpBookOverviewItem.sAuthor.equalsIgnoreCase("Various")) {
				
				bKeepLooking = false;
		    }
		}
		
		bKeepLooking = true;
		
		nSongIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
		
		poQuizQuestion.textQuestion = "What author influenced the song " +
				                      tmpBookOverviewItem.sibbItems.get(nSongIdx).sOtherData +
				                      "?";

		generateAuthorQuestionAnswers(poGenerator, poQuizQuestion, nBookIdx);

		if ((nParanIdx = tmpBookOverviewItem.sAuthor.indexOf("(")) > 0) {
			sAuthor = tmpBookOverviewItem.sAuthor.substring(0, nParanIdx);
	    }
		else {
			sAuthor = tmpBookOverviewItem.sAuthor;
	    }

	    sAuthor = sAuthor.replace("'", " ").replace("&", " and ").replace(" ", "_");

		poQuizQuestion.bookData = 
			tmpBookOverviewItem.sibbItems.get(nSongIdx).sBookData;
	    
		poQuizQuestion.textMoreAboutAnswerLink = 
				BibliophileBase.CONST_WIKI_SITE + sAuthor;
	}

    /**
     * This method will populate the provided QuizQuestion with potential answers 
     * about a random author.  Of course, one answer will be correct, but we will need
     * to provide other answers that are wrong.
     * 
     * @param poGenerator The random number generator used to randomly generate wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @param nBookIdx The index of the book data with the correct answer 
     * @return Nothing
     */		
	private void generateAuthorQuestionAnswers(Random poGenerator, 
			                             QuizQuestion poQuizQuestion, 
			                                      int nBookIdx)
	{
		boolean bKeepLooking       = false;
		int     nParanIdx          = -1;
        int     nAltBookAnswerIdx1 = nBookIdx;
        int     nAltBookAnswerIdx2 = nBookIdx;
        String  sAltBookData1      = "";
        String  sAltBookData2      = "";
        String  sAuthor            = "";
        String  sAltAuthor1        = "";
        String  sAltAuthor2        = "";
        String  sBookData          = BibliophileBase.bookCatalog.get(nBookIdx);
        
        BookOverviewItem tmpBookOverviewItem = 
        		BibliophileBase.bookOverviewItems.get(sBookData);
        
		if ((nParanIdx = tmpBookOverviewItem.sAuthor.indexOf("(")) > 0) {
			sAuthor = tmpBookOverviewItem.sAuthor.substring(0, nParanIdx);
	    }
		else {
			sAuthor = tmpBookOverviewItem.sAuthor;
	    }
		
        bKeepLooking = true;
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx1 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
        	sAltBookData1 = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx1);        	
            sAltAuthor1   = BibliophileBase.bookOverviewItems.get(sAltBookData1).sAuthor;
        	
        	if ((nAltBookAnswerIdx1 >= 0)              && 
        	    !sAltAuthor1.equalsIgnoreCase(sAuthor) && 
                !sAltAuthor1.equalsIgnoreCase("Various")) {
                	
        		break;
            }
	    }
	
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx2 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
        	sAltBookData2 = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx2);
            sAltAuthor2   = BibliophileBase.bookOverviewItems.get(sAltBookData2).sAuthor;
        	
        	if ((nAltBookAnswerIdx2 >= 0)                  && 
        		!sAltAuthor2.equalsIgnoreCase(sAuthor)     && 
        		!sAltAuthor2.equalsIgnoreCase(sAltAuthor1) && 
        	    !sAltAuthor2.equalsIgnoreCase("Various")) {
        		
        		break;
            }
	    }
                                
		poQuizQuestion.answers.put(sAuthor,     CONST_ANSWER_TRUE);
		poQuizQuestion.answers.put(sAltAuthor1, CONST_ANSWER_FALSE);
		poQuizQuestion.answers.put(sAltAuthor2, CONST_ANSWER_FALSE);
        
        if ((nBookIdx % 3) == 0) {
        	
        	poQuizQuestion.answerKeys.add(sAuthor);
        	
        	if ((nAltBookAnswerIdx1 % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sAltAuthor1);
        		poQuizQuestion.answerKeys.add(sAltAuthor2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltAuthor2);
        		poQuizQuestion.answerKeys.add(sAltAuthor1);
            }
	    }
        else {
        	
        	poQuizQuestion.answerKeys.add(sAltAuthor1);
        	
        	if ((nBookIdx % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sAuthor);
        		poQuizQuestion.answerKeys.add(sAltAuthor2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltAuthor2);
        		poQuizQuestion.answerKeys.add(sAuthor);
            }        	        	
	    }
	}

    /**
     * This method will populate the provided QuizQuestion with potential answers 
     * about a random book.  Of course, one answer will be correct, but we will need
     * to provide other answers that are wrong.
     * 
     * @param poGenerator The random number generator used to randomly generate wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @param nBookIdx The index of the book data with the correct answer 
     * @return Nothing
     */		
	private void generateBookQuestionAnswers(Random poGenerator, 
			                           QuizQuestion poQuizQuestion, 
			                                    int nBookIdx)
	{
		boolean bKeepLooking       = false;
		int     nParanIdx          = -1;
        int     nAltBookAnswerIdx1 = nBookIdx;
        int     nAltBookAnswerIdx2 = nBookIdx;
        String  sAltBookData1      = "";
        String  sAltBookData2      = "";
        String  sAltBook1          = "";
        String  sAltBook2          = "";
        String  sBook              = "";
        String  sBookData          = BibliophileBase.bookCatalog.get(nBookIdx);
        
        BookOverviewItem tmpBookOverviewItem = 
        		BibliophileBase.bookOverviewItems.get(sBookData);
        
	    if ((nParanIdx = tmpBookOverviewItem.sBook.indexOf("(")) > 0) {
	    	sBook = tmpBookOverviewItem.sBook.substring(0, nParanIdx).trim();
	    }
	    else {
	    	sBook = tmpBookOverviewItem.sBook.trim();
	    }
		
        bKeepLooking = true;
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx1 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
        	sAltBookData1 = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx1);        
            sAltBook1     = BibliophileBase.bookOverviewItems.get(sAltBookData1).sBook;
            
    	    if ((nParanIdx = sAltBook1.indexOf("(")) > 0) {
		    	sAltBook1 = sAltBook1.substring(0, nParanIdx).trim();
		    }
		    else {
		    	sAltBook1 = sAltBook1.trim();
		    }
        	
        	if ((nAltBookAnswerIdx1 >= 0) && 
        		!sAltBook1.equalsIgnoreCase(sBook)) {
        		break;
            }
	    }
	
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx2 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
	        sAltBookData2 = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx2);
	        sAltBook2     = BibliophileBase.bookOverviewItems.get(sAltBookData2).sBook;
	        			    
		    if ((nParanIdx = sAltBook2.indexOf("(")) > 0) {
		    	sAltBook2 = sAltBook2.substring(0, nParanIdx).trim();
		    }
		    else {
		    	sAltBook2 = sAltBook2.trim();
		    }
        	
        	if ((nAltBookAnswerIdx2 >= 0)          && 
        		!sAltBook2.equalsIgnoreCase(sBook) && 
        		!sAltBook2.equalsIgnoreCase(sAltBook1)) {
        		
        		break;
            }
	    }

		poQuizQuestion.answers.put(sBook,     CONST_ANSWER_TRUE);
		poQuizQuestion.answers.put(sAltBook1, CONST_ANSWER_FALSE);
		poQuizQuestion.answers.put(sAltBook2, CONST_ANSWER_FALSE);
        
        if ((nBookIdx % 3) == 0) {
        	
        	poQuizQuestion.answerKeys.add(sBook);
        	
        	if ((nAltBookAnswerIdx1 % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sAltBook1);
        		poQuizQuestion.answerKeys.add(sAltBook2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltBook2);
        		poQuizQuestion.answerKeys.add(sAltBook1);
            }
	    }
        else {
        	
        	poQuizQuestion.answerKeys.add(sAltBook1);
        	
        	if ((nBookIdx % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sBook);
        		poQuizQuestion.answerKeys.add(sAltBook2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltBook2);
        		poQuizQuestion.answerKeys.add(sBook);
            }        	        	
	    }
	}

    /**
     * This method will generate a type of trivia question that asks about a certain book 
     * to have influenced both a game and a song.
     * 
     * @param poGenerator The random number generator used to randomly generate the question and wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @return None
     */	
	private void generateBookQuestion1(Random poGenerator, QuizQuestion poQuizQuestion)
	{
		boolean          bKeepLooking        = true;
		BookOverviewItem tmpBookOverviewItem = null;
		
		int    nBookIdx      = -1;
		int    nSongIdx      = -1;
		int    nGameIdx      = -1;
		int    nGameDelimIdx = -1;
		
		String sBookData = "";
		String sGame     = "";
		
		while (bKeepLooking) {
			
			nBookIdx = Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
			
			sBookData = BibliophileBase.bookCatalog.get(nBookIdx);
									
			tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sBookData);

			if ((tmpBookOverviewItem.sibbItems.size() > 0) && (tmpBookOverviewItem.gibbItems.size() > 0)) {
				bKeepLooking = false;
		    }
		}
		
		bKeepLooking = true;
		
		nSongIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
		nGameIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.gibbItems.size());		
		sGame    = tmpBookOverviewItem.gibbItems.get(nGameIdx).sOtherData;
		
		if ((nGameDelimIdx = sGame.indexOf(BibliophileBase.OTHER_INFO_DELIM)) > 0)			
			sGame = sGame.substring(0, nGameDelimIdx).trim();
		
		poQuizQuestion.textQuestion = "What story/work influenced both the game " +
				                      sGame +
				                      " and the song " +
				                      tmpBookOverviewItem.sibbItems.get(nSongIdx).sOtherData +
				                      "?";

		generateBookQuestionAnswers(poGenerator, poQuizQuestion, nBookIdx);

		poQuizQuestion.bookData =
                tmpBookOverviewItem.sibbItems.get(nSongIdx).sBookData;		
				
		poQuizQuestion.textMoreAboutAnswerLink = 
				tmpBookOverviewItem.sibbItems.get(nSongIdx).getBL();
	}

    /**
     * This method will generate a type of trivia question that asks about a certain book 
     * to have influenced a certain song.
     * 
     * @param poGenerator The random number generator used to randomly generate the question and wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @return None
     */		
	private void generateBookQuestion2(Random poGenerator, QuizQuestion poQuizQuestion)
	{
		boolean          bKeepLooking        = true;
		BookOverviewItem tmpBookOverviewItem = null;
		
		int    nBookIdx  = -1;
		int    nSongIdx  = -1;
		int    nGameIdx  = -1;
		
		String sBookData = "";
		
		while (bKeepLooking) {
			
			nBookIdx = Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
			
			sBookData = BibliophileBase.bookCatalog.get(nBookIdx);
									
			tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sBookData);

			if (tmpBookOverviewItem.sibbItems.size() > 0) {
				bKeepLooking = false;
		    }
		}
		
		bKeepLooking = true;
		
		nSongIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
		
		poQuizQuestion.textQuestion = "What story/work influenced the song " +
				                      tmpBookOverviewItem.sibbItems.get(nSongIdx).sOtherData +
				                      "?";

		generateBookQuestionAnswers(poGenerator, poQuizQuestion, nBookIdx);

		poQuizQuestion.bookData =
                tmpBookOverviewItem.sibbItems.get(nSongIdx).sBookData;		
		
		poQuizQuestion.textMoreAboutAnswerLink = 
				tmpBookOverviewItem.sibbItems.get(nSongIdx).getBL();		
	}

    /**
     * This method will generate a type of trivia question that asks about the setting
     * of a specific book.
     * 
     * @param poGenerator The random number generator used to randomly generate the question and wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @return None
     */		
	private void generateLocationQuestion(Random poGenerator, QuizQuestion poQuizQuestion)
	{
		boolean          bKeepLooking        = true;
		BookOverviewItem tmpBookOverviewItem = null;
		
		int    nBookIdx  = -1;
		int    nLocIdx   = -1;
		
		String sBookData = "";
		String sLocation = "";
		
		while (bKeepLooking) {
			
			nBookIdx = Math.abs(poGenerator.nextInt() % BibliophileBase.libItems.size());
			
			sBookData = (String) BibliophileBase.libItems.get(nBookIdx);

			tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sBookData);

			if (tmpBookOverviewItem.locItems.size() > 0) {				
				bKeepLooking = false;
		    }
		}

		String sBookDataCopy = new String(sBookData);
		
		int nIdx = -1;
		if ((nIdx = sBookDataCopy.lastIndexOf(BibliophileBase.CONST_WRITTEN_BY_VAR)) > 0)
			sBookDataCopy = sBookDataCopy.substring(0, nIdx);
		
		poQuizQuestion.textQuestion = "What location was a setting for the story/work " +
				                      sBookDataCopy +
				                      "?";
				
		bKeepLooking = true;
		
		nLocIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.locItems.size());
				
		generateLocQuestionAnswers(poGenerator, poQuizQuestion, nBookIdx, nLocIdx);

		poQuizQuestion.bookData =
                tmpBookOverviewItem.locItems.get(nLocIdx).sBookData;		
		
		poQuizQuestion.textMoreAboutAnswerLink = 
				tmpBookOverviewItem.locItems.get(nLocIdx).getBL();		
	}

    /**
     * This method will populate the provided QuizQuestion with potential answers 
     * in regard to the location of a specific book.  Of course, one answer will be correct, 
     * but we will need to provide other answers that are wrong.
     * 
     * @param poGenerator The random number generator used to randomly generate wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @param nBookIdx The index of the book data with the correct answer
     * @param nLocIdx The index of the location data with the correct answer
     * @return Nothing
     */		
	private void generateLocQuestionAnswers(Random poGenerator, 
			                          QuizQuestion poQuizQuestion, 
			                                   int nBookIdx,
			                                   int nLocIdx)
	{
		boolean bKeepLooking       = false;
		int     nAltLoc1Idx        = -1;
		int     nAltLoc2Idx        = -1;
        int     nAltBookAnswerIdx1 = nBookIdx;
        int     nAltBookAnswerIdx2 = nBookIdx;
        String  sAltBookData1      = "";
        String  sAltBookData2      = "";
        String  sLocation          = "";
        String  sAltLocation1      = "";
        String  sAltLocation2      = "";
        String  sBookData          = BibliophileBase.libItems.get(nBookIdx);
        
        BookOverviewItem tmpBookOverviewItem = 
        		BibliophileBase.bookOverviewItems.get(sBookData);
        
		sLocation = tmpBookOverviewItem.locItems.get(nLocIdx).sOtherData;
		
        bKeepLooking = true;
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx1 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.libItems.size());
        	
        	sAltBookData1 = BibliophileBase.libItems.get(nAltBookAnswerIdx1);
        	
        	tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sAltBookData1);
        	
        	if (tmpBookOverviewItem.locItems.size() > 0) {
        		
        		nAltLoc1Idx =
        				Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.locItems.size());
        		
	        	sAltLocation1  = 
	        			tmpBookOverviewItem.locItems.get(nAltLoc1Idx).sOtherData;
	        		        	
	        	if (!sAltLocation1.equalsIgnoreCase(sLocation)) {	                	
	        		break;
	            }
        	}
	    }
	
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx2 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.libItems.size());
        	
        	sAltBookData2 = BibliophileBase.libItems.get(nAltBookAnswerIdx2);
        	
        	tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sAltBookData2);
        	
        	if (tmpBookOverviewItem.locItems.size() > 0) { 
        		
        		nAltLoc2Idx =
        				Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.locItems.size());
        		
	        	sAltLocation2  = 
	        			tmpBookOverviewItem.locItems.get(nAltLoc2Idx).sOtherData;
	        		        	
	        	if (!sAltLocation2.equalsIgnoreCase(sLocation) && !sAltLocation2.equalsIgnoreCase(sAltLocation1)) {	                	
	        		break;
	            }
        	}
	    }
                                
		poQuizQuestion.answers.put(sLocation,     CONST_ANSWER_TRUE);
		poQuizQuestion.answers.put(sAltLocation1, CONST_ANSWER_FALSE);
		poQuizQuestion.answers.put(sAltLocation2, CONST_ANSWER_FALSE);
        
        if ((nBookIdx % 3) == 0) {
        	
        	poQuizQuestion.answerKeys.add(sLocation);
        	
        	if ((nAltBookAnswerIdx1 % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sAltLocation1);
        		poQuizQuestion.answerKeys.add(sAltLocation2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltLocation2);
        		poQuizQuestion.answerKeys.add(sAltLocation1);
            }
	    }
        else {
        	
        	poQuizQuestion.answerKeys.add(sAltLocation1);
        	
        	if ((nBookIdx % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sLocation);
        		poQuizQuestion.answerKeys.add(sAltLocation2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltLocation2);
        		poQuizQuestion.answerKeys.add(sLocation);
            }        	        	
	    }
	}

    /**
     * This method will populate the provided QuizQuestion with a question (along
     * with potential answers) about a random song that was influenced by a book.  
     * 
     * @param poGenerator The random number generator used to randomly select a song and wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers 
     * @return Nothing
     */	
	private void generateSongQuestion(Random poGenerator, QuizQuestion poQuizQuestion)
	{
		boolean          bKeepLooking        = true;
		BookOverviewItem tmpBookOverviewItem = null;
		
		int    nBookIdx  = -1;
		int    nSongIdx  = -1;
		int    nParanIdx = -1;
		
		String sBookData = "";
		String sSong     = "";
		
		while (bKeepLooking) {
			
			nBookIdx = Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
			
			sBookData = BibliophileBase.bookCatalog.get(nBookIdx);
						
			tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sBookData);

			if (tmpBookOverviewItem.sibbItems.size() > 0) {
				
				bKeepLooking = false;
		    }
		}
		
		String sBookDataCopy = new String(sBookData);
		
		int nIdx = -1;
		if ((nIdx = sBookDataCopy.lastIndexOf(BibliophileBase.CONST_WRITTEN_BY_VAR)) > 0)
			sBookDataCopy = sBookDataCopy.substring(0, nIdx);
		
		poQuizQuestion.textQuestion = "What song received influence from the story/work " +
				                      sBookDataCopy +
				                      "?";
		
		bKeepLooking = true;
		
		nSongIdx = Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
				
		generateSongQuestionAnswers(poGenerator, poQuizQuestion, nBookIdx, nSongIdx);

		poQuizQuestion.bookData = 
				tmpBookOverviewItem.sibbItems.get(nSongIdx).sBookData;
		
		poQuizQuestion.textMoreAboutAnswerLink = 
				tmpBookOverviewItem.sibbItems.get(nSongIdx).getOL();
	}

    /**
     * This method will populate the provided QuizQuestion with potential answers 
     * in regard to a specific song.  Of course, one answer will be correct, but we will 
     * need to provide other answers that are wrong.
     * 
     * @param poGenerator The random number generator used to randomly generate wrong answers
     * @param poQuizQuestion The class that we will populate with the question and answers
     * @param nBookIdx The index of the book data with the correct answer
     * @param nSongIdx The index of the song data with the correct answer
     * @return Nothing
     */		
	private void generateSongQuestionAnswers(Random poGenerator, 
			                           QuizQuestion poQuizQuestion, 
			                                    int nBookIdx,
			                                    int nSongIdx)
	{
		boolean bKeepLooking       = false;
		int     nParanIdx          = -1;
		int     nAltSong1Idx       = -1;
		int     nAltSong2Idx       = -1;
        int     nAltBookAnswerIdx1 = nBookIdx;
        int     nAltBookAnswerIdx2 = nBookIdx;
        String  sAltBookData1      = "";
        String  sAltBookData2      = "";
        String  sSong              = "";
        String  sAltSong1          = "";
        String  sAltSong2          = "";
        String  sBookData          = BibliophileBase.bookCatalog.get(nBookIdx);
        String  sDelim             = BibliophileBase.SONG_INFO_DELIM;
        
        BookOverviewItem tmpBookOverviewItem = 
        		BibliophileBase.bookOverviewItems.get(sBookData);
        
		sSong = 
				tmpBookOverviewItem.sibbItems.get(nSongIdx).sOtherData.split(sDelim)[0];
		
        bKeepLooking = true;
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx1 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
        	sAltBookData1       = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx1);
        	tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sAltBookData1);
        	
        	if (tmpBookOverviewItem.sibbItems.size() > 0) {
        		
        		nAltSong1Idx =
        				Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
        		
	        	sAltSong1  = 
	        			tmpBookOverviewItem.sibbItems.get(nAltSong1Idx).sOtherData.split(sDelim)[0];
	        		        	
	        	if (!sAltSong1.equalsIgnoreCase(sSong)) {	                	
	        		break;
	            }
        	}
	    }
	
        while (bKeepLooking) {
        	
        	nAltBookAnswerIdx2 = 
        			Math.abs(poGenerator.nextInt() % BibliophileBase.bookCatalog.size());
        	
        	sAltBookData2       = BibliophileBase.bookCatalog.get(nAltBookAnswerIdx2);
        	tmpBookOverviewItem = BibliophileBase.bookOverviewItems.get(sAltBookData2);
        	
        	if (tmpBookOverviewItem.sibbItems.size() > 0) { 
        		
        		nAltSong2Idx =
        				Math.abs(poGenerator.nextInt() % tmpBookOverviewItem.sibbItems.size());
        		
	        	sAltSong2  = 
	        			tmpBookOverviewItem.sibbItems.get(nAltSong2Idx).sOtherData.split(sDelim)[0];
	        		        	
	        	if (!sAltSong2.equalsIgnoreCase(sSong) && !sAltSong2.equalsIgnoreCase(sAltSong1)) {	                	
	        		break;
	            }
        	}
	    }
                                
		poQuizQuestion.answers.put(sSong,     CONST_ANSWER_TRUE);
		poQuizQuestion.answers.put(sAltSong1, CONST_ANSWER_FALSE);
		poQuizQuestion.answers.put(sAltSong2, CONST_ANSWER_FALSE);
        
        if ((nBookIdx % 3) == 0) {
        	
        	poQuizQuestion.answerKeys.add(sSong);
        	
        	if ((nAltBookAnswerIdx1 % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sAltSong1);
        		poQuizQuestion.answerKeys.add(sAltSong2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltSong2);
        		poQuizQuestion.answerKeys.add(sAltSong1);
            }
	    }
        else {
        	
        	poQuizQuestion.answerKeys.add(sAltSong1);
        	
        	if ((nBookIdx % 3) == 1) {
        		poQuizQuestion.answerKeys.add(sSong);
        		poQuizQuestion.answerKeys.add(sAltSong2);
        	}
        	else {
        		poQuizQuestion.answerKeys.add(sAltSong2);
        		poQuizQuestion.answerKeys.add(sSong);
            }        	        	
	    }
	}

    /**
     * This method will do the 'heavy work' to create the next question in the trivia quiz, 
     * randomly selecting the type of question (about a song, about an author, 
     * about a location, etc.) and the accompanying set of answers.
     * 
     * @return None
     */		
	private void generateNewQuestion()
	{
		QuizQuestion newQuestion = new QuizQuestion();
		
    	int nQuestionType = Math.abs(generator.nextInt() % CONST_QUESTION_TYPE_MAX);
    	
    	if (nQuestionType == CONST_QUESTION_TYPE_BOOK) {
    		generateBookQuestion1(generator, newQuestion);
	    }
    	else if (nQuestionType == CONST_QUESTION_TYPE_BOOK_2) {
    		generateBookQuestion2(generator, newQuestion);
    	}
    	else if (nQuestionType == CONST_QUESTION_TYPE_AUTHOR) {
    		generateAuthorQuestion(generator, newQuestion);
    	}
    	else if (nQuestionType == CONST_QUESTION_TYPE_SONG) {
    		generateSongQuestion(generator, newQuestion);
    	}
    	else if (nQuestionType == CONST_QUESTION_TYPE_LOC) {
    		
    		try {
    		    generateLocationQuestion(generator, newQuestion);
    		}
            catch (Throwable e) {
            	
            	String sMsg = e.toString();
            	
    			new AlertDialog.Builder(F5Activity.this)
    			.setTitle("Warning")
    			.setMessage("Request failed: "+sMsg)
    			.setNeutralButton("Done", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dlg, int s) {}
    			})
    			.show();
            	        	
            	// System.out.println(sMsg);        	        	
            }
	    }
    	else { 
    		
			newQuestion.textQuestion = "What author influenced both the videogame Bioshock and the song Anthem by the band Rush?";
			
			newQuestion.answerKeys.add("Noam Chomsky");
			newQuestion.answerKeys.add("Ayn Rand");
			newQuestion.answerKeys.add("Gore Vidal");
			
			newQuestion.answers.put("Noam Chomsky", CONST_ANSWER_FALSE);
			newQuestion.answers.put("Ayn Rand",     CONST_ANSWER_TRUE);
			newQuestion.answers.put("Gore Vidal",   CONST_ANSWER_FALSE);
			
			newQuestion.textMoreAboutAnswerLink = "http://en.wikipedia.org/wiki/Ayn_Rand";
    	}
		
		oQuizQuestions.add(newQuestion);		
	}

    /**
     * This method will move the trivia quiz onto the next question, right after the 
     * screen controls are reset in preparation for it.
     * 
     * @return None
     */		
	private void nextQuestion() 
	{		
		// if (nCurrQuestionIndex >= (oQuizQuestions.size()-1))
		generateNewQuestion();
		
		resetButtons();		
	}

    /**
     * This method will reset (i.e., disable) the trivia quiz's control buttons, 
     * notably the button that will invoke a details screen about a question's answer. 
     * 
     * @return None
     */	
	private void resetButtons() 
	{
		buttonMoreAboutAnswer.setBackgroundResource(R.drawable.quiz_maa_inv_button);
		buttonMoreAboutAnswer.setEnabled(false);
	}

    /**
     * This method will reset (i.e., set to invisible) the trivia quiz's icons,
     * notably those that indicate whether the answers are right or wrong. 
     * 
     * @return None
     */		
	private void resetIcons()
	{
		iconFirstAnswer.setVisibility(View.INVISIBLE);
		iconSecondAnswer.setVisibility(View.INVISIBLE);
		iconThirdAnswer.setVisibility(View.INVISIBLE);
	}

    /**
     * This method will reset the timer.
     * 
     * @return None
     */	
	private void resetTimer()
	{
        mStartTime = 0L;

		textTimer.setText("00:00");
	}

    /**
     * Once a user has answered a trivia question, this method will then populate the
     * answers with icons that indicate which answer is correct and which answers are wrong.  
     * 
     * @param currQuestion The class that will provide us with the needed information about the answers
     * @return None
     */		
	private void setAnswerIcons(QuizQuestion currQuestion)
	{
	    String sFirstAnswer  = currQuestion.answerKeys.get(0);
	    String sSecondAnswer = currQuestion.answerKeys.get(1);
	    String sThirdAnswer  = currQuestion.answerKeys.get(2);
	    
	    if (currQuestion.answers.get(sFirstAnswer) == CONST_ANSWER_TRUE) {
	    	iconFirstAnswer.setBackgroundResource(R.drawable.quiz_icon_right);	    	
	    }
	    else {
	    	iconFirstAnswer.setBackgroundResource(R.drawable.quiz_icon_wrong);
	    }
	    iconFirstAnswer.setVisibility(View.VISIBLE);

	    if (currQuestion.answers.get(sSecondAnswer) == CONST_ANSWER_TRUE) {
	    	iconSecondAnswer.setBackgroundResource(R.drawable.quiz_icon_right);	    	
	    }
	    else {
	    	iconSecondAnswer.setBackgroundResource(R.drawable.quiz_icon_wrong);
	    }
	    iconSecondAnswer.setVisibility(View.VISIBLE);

	    if (currQuestion.answers.get(sThirdAnswer) == CONST_ANSWER_TRUE) {
	    	iconThirdAnswer.setBackgroundResource(R.drawable.quiz_icon_right);	    	
	    }
	    else {
	    	iconThirdAnswer.setBackgroundResource(R.drawable.quiz_icon_wrong);
	    }
	    iconThirdAnswer.setVisibility(View.VISIBLE);
	}

    /**
     * Once a user has answered a trivia question, this method will then take
     * the current score and then appropriately update the score displayed on
     * the screen.  
     * 
     * @return None
     */
	private void updateScoreboard()
	{
		int nTmpPctRight = 0;
		
		String sTmpRight = "";
		String sTmpPct   = "";
		String sTmpWrong = "";
		
		double dPctMult = 100;
		
		if (nAnsweredRight > 99) {
			sTmpRight = "99+";
		}
		else {
			sTmpRight = Integer.toString(nAnsweredRight);
	    }
		
		if (nAnsweredWrong > 99) {
			sTmpWrong = "99+";			
		}
		else {
			sTmpWrong = Integer.toString(nAnsweredWrong);
	    }
		
		if ((nAnsweredRight > 0) || (nAnsweredWrong > 0)) {
			
			double dDenom      = (double) (nAnsweredRight + nAnsweredWrong);
			double dPercentage = nAnsweredRight / dDenom;
			
			nTmpPctRight = (int) (dPercentage * dPctMult);
			
			if (nTmpPctRight > 0) {
			    sTmpPct = Integer.toString(nTmpPctRight) + "%";
			}
			else {
				sTmpPct = "";
		    }
		}		
		
		textScoreboardRight.setText(sTmpRight);
		textScoreboardPct.setText(sTmpPct);
		textScoreboardWrong.setText(sTmpWrong);		
	}

    /**
     * This subclass serves as the data structure that represents one question
     * within the trivia quiz, containing:
     * 
     * 1.) the question
     * 2.) the right answer and a few wrong ones
     * 3.) a link with more data about the correct answer
     */
    class QuizQuestion
    {
    	public String                 bookData;
    	public String                 textQuestion;
    	public ArrayList<String>      answerKeys;
    	public HashMap<String,String> answers;
    	public String                 textMoreAboutAnswerLink;
    	public boolean                isAnswered;
    	
    	public QuizQuestion()
    	{
    		bookData     = "";
    		textQuestion = "";
    		
    		answerKeys = new ArrayList<String>();
    		answers    = new HashMap<String,String>();
    		
    		textMoreAboutAnswerLink = "";
    		
    		isAnswered = false;
    	}
    }
        
} // end class
