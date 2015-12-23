// Bibliophile API
// Created by Aaron S. Kendall
// Copyright  2009-2010

var baseUrl = "http://api.bibliophileonline.com";

function detectBrowser() {

    var browser=navigator.userAgent.toLowerCase();

	if (browser.indexOf("chrome") != -1)   return 'Chrome';
    if (browser.indexOf("msie") != -1)     return 'Internet Explorer';
	if (browser.indexOf("netscape") != -1) return 'Netscape';
    if (browser.indexOf("firefox") != -1)  return 'Firefox';
    if (browser.indexOf("safari") != -1)   return 'Safari';
	
    if (browser.indexOf("mozilla/5.0") != -1) return 'Mozilla';
	else 
		return navigator.userAgent;
}

function getSongFact( song, performer, author, book, bookCategory, songCategory, displayBoxFlag, randomFlag ) {

    var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderSongFact";
			
	if (song != "") {
		dataString += "&song=" + song;
	}
	
	if (performer != "") {
		dataString += "&performer=" + performer;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (songCategory != "") {
		dataString += "&song_category=" + songCategory;
	}
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
		
	if (randomFlag != "") {
		cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=R";
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false
						}).responseText;
	
	return resultsData;
}

function renderSongFact( song, performer, author, book, bookCategory, songCategory, displayBoxFlag, randomFlag) {

    var cacheFlag  = true;
	var dataString = "method=renderSongFact";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
			
	if (song != "") {
		dataString += "&song=" + song;
	}
	
	if (performer != "") {
		dataString += "&performer=" + performer;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (songCategory != "") {
		dataString += "&song_category=" + songCategory;
	}
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
		
	if (randomFlag != "") {
        cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=R";
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false
						}).responseText;
	
	document.write(resultsData);
}

function getGameFact( game, author, book, bookCategory, gameCategory, displayBoxFlag, randomFlag ) {

    var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderGameFact";
			
	if (game != "") {
		dataString += "&game=" + game;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	if (randomFlag != "") {
	    cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}

	dataString += "&links=B";
			
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	return resultsData;
}

function renderGameFact( game, author, book, bookCategory, gameCategory, displayBoxFlag, randomFlag ) {

    var cacheFlag  = true;
	var dataString = "method=renderGameFact";
			
	if (game != "") {
		dataString += "&game=" + game;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	if (randomFlag != "") {
	    cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=B";

	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	document.write(resultsData);
}

function getLocationFact( location, latLongPair, author, book, displayBoxFlag, randomFlag ) {

    var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderLocationFact";
		
	if (location != "") {
		dataString += "&location=" + location;
	}

	if (latLongPair != "") {
		dataString += "&lat_long_pair=" + latLongPair;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}	
	
	if (book != "") {
		dataString += "&book=" + book;
	}
		
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	if (randomFlag != "") {
		cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=B";
	
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	return resultsData;
}

function renderLocationFact( location, latLongPair, author, book, displayBoxFlag, randomFlag ) {

    var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderLocationFact";
		
	if (location != "") {
		dataString += "&location=" + location;
	}

	if (latLongPair != "") {
		dataString += "&lat_long_pair=" + latLongPair;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}	
	
	if (book != "") {
		dataString += "&book=" + book;
	}
		
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	if (randomFlag != "") {
		cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=B";
	
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	document.write(resultsData);
}

function renderQuoteFact( author, book, keyword, displayBoxFlag, randomFlag ) {

	var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderQuoteFact";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (keyword != "") {
		dataString += "&keyword=" + keyword;
	}
		
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
		
	if (randomFlag != "") {
		cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=B";
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	document.write(resultsData);
}

function getQuoteFact( author, book, keyword, displayBoxFlag, randomFlag ) {

	var cacheFlag  = true;
	var dataString = "app_id=123456789&method=renderQuoteFact";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (keyword != "") {
		dataString += "&keyword=" + keyword;
	}
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	if (randomFlag != "") {
		cacheFlag   = false;
		dataString += "&random=" + randomFlag;
	}
	
	dataString += "&links=B";
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: cacheFlag,
						async: false					
						}).responseText;
	
	return resultsData;
}

function renderTriviaFact( displayBoxFlag ) {

	var dataString = "app_id=123456789&method=renderTriviaFact";
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	dataString += "&links=Y";
			
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: false,
						async: false					
						}).responseText;
	
	document.write(resultsData);
}

function getTriviaFact( displayBoxFlag ) {

	var dataString = "app_id=123456789&method=renderTriviaFact";
	
	if (displayBoxFlag != "") {
		dataString += "&display_box=" + displayBoxFlag;
	}
	
	dataString += "&links=Y";
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						cache: false,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getWorkList( book, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getWorkList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getAuthorList( author, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getAuthorList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getQuotesWorkList( book, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getQuotesWorkList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getQuotesAuthorList( author, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getQuotesAuthorList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getGamesWorkList( book, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getGamesWorkList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (book != "") {
		dataString += "&book=" + book;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getGamesAuthorList( author, bookCategory, gameCategory ) {

	var dataString = "app_id=123456789&method=getGamesAuthorList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
	
	if (author != "") {
		dataString += "&author=" + author;
	}
	
	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (gameCategory != "") {
		dataString += "&game_category=" + gameCategory;
	}	
	
	// document.write("dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_retrieve.php",
						data: dataString,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getAllCharacterTraits() {

	var dataString = "app_id=123456789&method=getAllTraitsList";
	
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_char_retrieve.php",
						data: dataString,
						cache: false,
						async: false					
						}).responseText;
	
	return resultsData;
}

function getCharacterSearchResults( bookCategory, characterType, characterGender, trait1, trait2, trait3 ) {

	var dataString = "app_id=123456789&method=getCharacterSearchResults";

	if (bookCategory != "") {
		dataString += "&book_category=" + bookCategory;
	}
	
	if (characterType != "") {
		dataString += "&character_type=" + characterType;
	}
	
	if (characterGender != "") {
		dataString += "&character_gender=" + characterGender;
	}
	
	if (trait1 != "") {
		dataString += "&Trait1=" + trait1;
	}
	
	if (trait2 != "") {
		dataString += "&Trait2=" + trait2;
	}
	
	if (trait3 != "") {
		dataString += "&Trait3=" + trait3;
	}
			
	// document.write("STEP 1 -> dataString is (" + dataString + ")<br/>");
		
	var resultsData = $.ajax({
						type: "GET",
						url: baseUrl + "/bibliofile_char_retrieve.php",
						data: dataString,
						cache: false,
						async: false					
						}).responseText;
	
	return resultsData;
}