// Bibliophile API
// Created by Aaron S. Kendall
// Copyright  2009-2012

var map;
var marker;
var geocoder;

var search_results = "";

var bfMarkerImage = 
    new google.maps.MarkerImage("http://api.bibliophileonline.com/images/75.png", null, null, null, new google.maps.Size(35, 35));

var newyork = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
var initialLocation = newyork;
var browserSupportFlag =  new Boolean();

var crck_data,rck_data,pop_data,rap_data,metal_data,gspl_data,alt_data,cmp_data,cntry_data;
var clssc_data,fps_data,strat_data,rpg_data,pzzl_data,aa_data;

var barChartCrck,barChartRck,barChartPop,barChartRap,barChartMetal,barChartGospel,barChartAlt,barChartCmp,barChartCntry;
var barChartClssic,barChartFps,barChartStrat,barChartRpg,barChartPzzl,barChartAa;

function roundNumber(num, dec) {
	var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
	return result;
}
  
function initialize_gmap() {

    var latlng = newyork;
    var myOptions = {
      zoom: 4,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
	
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	geocoder = new google.maps.Geocoder();
		
    // Try W3C Geolocation (Preferred)
    if(navigator.geolocation) {
      browserSupportFlag = true;
      navigator.geolocation.getCurrentPosition(function(position) {
        initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
        map.setCenter(initialLocation);

        window.BibliophileJSInterface.testEcho("TESTING...1...2...3");
		
		if (window.BibliophileJSInterface) {
		    window.BibliophileJSInterface.setCoordinates(position.coords.latitude.toString(), position.coords.longitude.toString());
		}
		
      }, function() {
        handleNoGeolocation(browserSupportFlag);
      });
    // Try Google Gears Geolocation
    } else if (google.gears) {
      browserSupportFlag = true;
      var geo = google.gears.factory.create('beta.geolocation');
      geo.getCurrentPosition(function(position) {
        initialLocation = new google.maps.LatLng(position.latitude,position.longitude);
        map.setCenter(initialLocation);
		
		window.BibliophileJSInterface.testEcho("TESTING...1...2...3");
		
		if (window.BibliophileJSInterface) {
		    window.BibliophileJSInterface.setCoordinates(position.coords.latitude.toString(), position.coords.longitude.toString());
		}
		
      }, function() {
        handleNoGeoLocation(browserSupportFlag);
      });
    // Browser doesn't support Geolocation
    } else {
      browserSupportFlag = false;
      handleNoGeolocation(browserSupportFlag);
    }

	marker = new google.maps.Marker({
			position: initialLocation, 
			map: map,
			draggable: true,
		    title:"Current Location",
			zIndex:100
		});
		
	placeNotableMarkers();
			
	google.maps.event.addListener(map, 'click', function(event) {
      handleMapClick(event.latLng);
	});

	/*
	google.maps.event.addListener(marker, "dragstart", function() {
		map.closeInfoWindow();
	});
	*/

	google.maps.event.addListener(marker, "dragend", function(event) {
		handleMarkerMove(event.latLng);
	});
	
	handleMarkerMove(initialLocation);
	
	var resultsTable = "";
	resultsTable = getLatLocResults();
	
	/*
	if (window.BibliophileJSInterface) {
	    window.BibliophileJSInterface.setResults(resultsTable);
	}
	*/
	
	/*
	$('#BiblioLocSearchResults').html("<div id='BiblioLocSearchResults'></div>")
	.hide()
	.fadeIn(1500, function() { 
	  $('#BiblioLocSearchResults').append( resultsTable ); 
	});	
	*/
}

function handleNoGeolocation(errorFlag) {
  if (errorFlag == true) {
	alert("Geolocation service failed.");
	initialLocation = newyork;
  } else {
	// alert("Your browser doesn't support geolocation. We've placed you in New York.");
	initialLocation = newyork;
  }
  map.setCenter(initialLocation);
}

function getPrimaryLocation(locationName) {

  var primaryLocation = "";
  var commaIndex      = locationName.indexOf(",", 0);
  
  if (commaIndex > 0) {
    primaryLocation = locationName.substring(0, commaIndex);
  }
  else {
	primaryLocation = locationName;
  }

  return primaryLocation;
}

function placeNotableMarkers() {

	var marker2 = new google.maps.Marker({
			position: new google.maps.LatLng(40.69847032728747, -73.9514422416687), 
			map: map,
			draggable: false,
		    title:"New York accounts for: 4.69%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker2, 'click', function() {
		handleMapClick(marker2.getPosition());
	});
		
	var marker3 = new google.maps.Marker({
			position: new google.maps.LatLng(51.4953, -0.1339), 
			map: map,
			draggable: false,
		    title:"London accounts for: 4.17%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker3, 'click', function() {
		handleMapClick(marker3.getPosition());
	});
		
	var marker4 = new google.maps.Marker({
			position: new google.maps.LatLng(34.053, -118.2699), 
			map: map,
			draggable: false,
		    title:"Los Angeles accounts for: 2.17%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker4, 'click', function() {
		handleMapClick(marker4.getPosition());
	});
		
	var marker5 = new google.maps.Marker({
			position: new google.maps.LatLng(48.8505, 2.338), 
			map: map,
			draggable: false,
		    title:"Paris accounts for: 1.49%",
			icon: bfMarkerImage,
			zIndex:1
		});

	google.maps.event.addListener(marker5, 'click', function() {
		handleMapClick(marker5.getPosition());
	});
		
	var marker6 = new google.maps.Marker({
			position: new google.maps.LatLng(37.7797, -122.4337), 
			map: map,
			draggable: false,
		    title:"San Francisco accounts for: 1.37%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker6, 'click', function() {
		handleMapClick(marker6.getPosition());
	});
		
	var marker7 = new google.maps.Marker({
			position: new google.maps.LatLng(41.8289, -87.7169), 
			map: map,
			draggable: false,
		    title:"Chicago accounts for: 1.36%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker7, 'click', function() {
		handleMapClick(marker7.getPosition());
	});

	var marker8 = new google.maps.Marker({
			position: new google.maps.LatLng(38.893, -77.0039), 
			map: map,
			draggable: false,
		    title:"Washington D.C. accounts for: 0.78%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker8, 'click', function() {
		handleMapClick(marker8.getPosition());
	});
		
	var marker9 = new google.maps.Marker({
			position: new google.maps.LatLng(29.9553, -90.0762), 
			map: map,
			draggable: false,
		    title:"New Orleans accounts for: 0.78%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker9, 'click', function() {
		handleMapClick(marker9.getPosition());
	});

	var marker10 = new google.maps.Marker({
			position: new google.maps.LatLng(47.593, -122.3335), 
			map: map,
			draggable: false,
		    title:"Seattle accounts for: 0.55%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker10, 'click', function() {
		handleMapClick(marker10.getPosition());
	});
		
	var marker11 = new google.maps.Marker({
			position: new google.maps.LatLng(53.293, -6.2739), 
			map: map,
			draggable: false,
		    title:"Dublin accounts for: 0.55%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker11, 'click', function() {
		handleMapClick(marker11.getPosition());
	});
		
	var marker12 = new google.maps.Marker({
			position: new google.maps.LatLng(41.9041, 12.4688), 
			map: map,
			draggable: false,
		    title:"Rome accounts for: 0.61%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker12, 'click', function() {
		handleMapClick(marker12.getPosition());
	});
		
	var marker13 = new google.maps.Marker({
			position: new google.maps.LatLng(45.3693, 12.293), 
			map: map,
			draggable: false,
		    title:"Venice accounts for: 0.32%",
			icon: bfMarkerImage,
			zIndex:1
		});

	google.maps.event.addListener(marker13, 'click', function() {
		handleMapClick(marker13.getPosition());
	});
		
	var marker14 = new google.maps.Marker({
			position: new google.maps.LatLng(55.7625, 37.611), 
			map: map,
			draggable: false,
		    title:"Moscow accounts for: 0.28%",
			icon: bfMarkerImage,
			zIndex:1
		});

	google.maps.event.addListener(marker14, 'click', function() {
		handleMapClick(marker14.getPosition());
	});		

	var marker15 = new google.maps.Marker({
			position: new google.maps.LatLng(43.6259, -79.4154), 
			map: map,
			draggable: false,
		    title:"Toronto accounts for: 0.27%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker15, 'click', function() {
		handleMapClick(marker15.getPosition());
	});
		
	var marker16 = new google.maps.Marker({
			position: new google.maps.LatLng(31.7852, 35.1803), 
			map: map,
			draggable: false,
		    title:"Jerusalem accounts for: 0.13%",
			icon: bfMarkerImage,
			zIndex:1
		});

	google.maps.event.addListener(marker16, 'click', function() {
		handleMapClick(marker16.getPosition());
	});
		
	var marker17 = new google.maps.Marker({
			position: new google.maps.LatLng(19.0008, 72.7975), 
			map: map,
			draggable: false,
		    title:"Bombay accounts for: 0.11%",
			icon: bfMarkerImage,
			zIndex:1
		});

	google.maps.event.addListener(marker17, 'click', function() {
		handleMapClick(marker17.getPosition());
	});

	var marker18 = new google.maps.Marker({
			position: new google.maps.LatLng(-33.8712, 151.2055), 
			map: map,
			draggable: false,
		    title:"Sydney accounts for: 0.11%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker18, 'click', function() {
		handleMapClick(marker18.getPosition());
	});

	var marker19 = new google.maps.Marker({
			position: new google.maps.LatLng(40.9802, 28.9359), 
			map: map,
			draggable: false,
		    title:"Istanbul accounts for: 0.16%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker19, 'click', function() {
		handleMapClick(marker19.getPosition());
	});
		
	var marker20 = new google.maps.Marker({
			position: new google.maps.LatLng(31.2355, 121.4655), 
			map: map,
			draggable: false,
		    title:"Shanghai accounts for: 0.1%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker20, 'click', function() {
		handleMapClick(marker20.getPosition());
	});
		
	var marker21 = new google.maps.Marker({
			position: new google.maps.LatLng(52.5323, 13.3161), 
			map: map,
			draggable: false,
		    title:"Berlin accounts for: 0.23%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker21, 'click', function() {
		handleMapClick(marker21.getPosition());
	});

	var marker22 = new google.maps.Marker({
			position: new google.maps.LatLng(35.6398, 139.6561), 
			map: map,
			draggable: false,
		    title:"Tokyo accounts for: 0.16%",
			icon: bfMarkerImage,
			zIndex:1
		});	
		
	google.maps.event.addListener(marker22, 'click', function() {
		handleMapClick(marker22.getPosition());
	});
		
	var marker23 = new google.maps.Marker({
			position: new google.maps.LatLng(30.0504, 31.2211), 
			map: map,
			draggable: false,
		    title:"Cairo accounts for: 0.17%",
			icon: bfMarkerImage,
			zIndex:1
		});
		
	google.maps.event.addListener(marker23, 'click', function() {
		handleMapClick(marker23.getPosition());
	});
}

function handleMapClick(location) {

    /*
	var tmpLat = roundNumber(location.lat(), 4);
	var tmpLng = roundNumber(location.lng(), 4);		
	// alert ("You clicked the position(" + tmpLat + "," + tmpLng + ")!");
	document.BF_Location_SearchType_Form.bfl_latitude_text.value  = tmpLat;
	document.BF_Location_SearchType_Form.bfl_longitude_text.value = tmpLng;	
	map.setCenter(location);
	*/

    /*	
    marker = new google.maps.Marker({
      position: location, 
      map: map
    });
	*/
	
	marker.setPosition(location);
	
	handleMarkerMove(location);
	
	var resultsTable = "";
	resultsTable = getLatLocResults();
	
	if (window.BibliophileJSInterface) {
	    window.BibliophileJSInterface.setResults(resultsTable);
	}
	
	/*
	$('#BiblioLocSearchResults').html("<div id='BiblioLocSearchResults'></div>")
	.hide()
	.fadeIn(1500, function() { 
	  $('#BiblioLocSearchResults').append( resultsTable ); 
	});	
	*/
}

function handleMarkerMove(location) {

    // alert("End of the marker move!");
	
	var tmpLat = roundNumber(location.lat(), 4);
	var tmpLng = roundNumber(location.lng(), 4);

	document.BF_Location_SearchType_Form.bfl_latitude_text.value  = tmpLat;
	document.BF_Location_SearchType_Form.bfl_longitude_text.value = tmpLng;
	
	map.setCenter(location);
	selectLocSearchRadio(0);
	
    if (geocoder) {
      geocoder.geocode({'latLng': location}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
		  if (results[1]) {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = getPrimaryLocation(results[1].formatted_address);
		  }
		  /*
		  else if (results[0]) {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = "#0 " + results[0].formatted_address;
		  }
		  else if (results[4]) {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = "#6 " + results[4].formatted_address;
		  }
		  */
          else if (results[5]) {
		  
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = getPrimaryLocation(results[5].formatted_address);
		    // alert("R1: Moved marker to (" + results[1].formatted_address + ").");

            // map.setZoom(11);
            // marker = new google.maps.Marker({
            //    position: latlng, 
            //    map: map
            //}); 
            //infowindow.setContent(results[1].formatted_address);
            //infowindow.open(map, marker);
          }
		  else if (results[6]) {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = getPrimaryLocation(results[6].formatted_address);
		  }
		  else if (results[7]) {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = results[7].formatted_address;
		  }
		  else {
			document.BF_Location_SearchType_Form.bfl_loc_search_text.value = "";
		  }
        } else {
	      document.BF_Location_SearchType_Form.bfl_loc_search_text.value = "";
          // alert("Geocoder failed due to: " + status);
        }
      });
	}
}

function getLatLocResults() {

	var tempResults = "";

	var latitude = $("input#bfl_latitude_text").val();
	if ((latitude == undefined) || (latitude == "")) {
		alert("Please provide a correct latitude.");
		return;
	}
	
	var longitude = $("input#bfl_longitude_text").val();
	if ((longitude == undefined) || (longitude == "")) {
		alert("Please provide a correct longitude.");
		return;
	}
	
	var latLongPair = latitude + "," + longitude;

	// alert("The lat. and long. are (" + latLongPair + ").");
	
	tempResults = getLocationFact( "", latLongPair, "", "", "N", "N" );
	
	// alert("Retrieved the location results: " + resultsTable);
	
	search_results = tempResults;
	
	return tempResults;
}

function mapCheckResize() {

    // alert("DEBUG: checkResize() has been called!");

    map.checkResize();
}

/*
function setMarkerAndGetResults(newLocation) {

	handleMarkerMove(newLocation);
	
	var resultsTable = "";
	resultsTable = getLatLocResults();
	
	$('#BiblioLocSearchResults').html("<div id='BiblioLocSearchResults'></div>")
	.hide()
	.fadeIn(1500, function() { 
	  $('#BiblioLocSearchResults').append( resultsTable ); 
	});	
}
*/

function songChartHandler(songType, songChart, songData) {

	var selection = songChart.getSelection();
	
    // alert("Inside Song Chart Handler - " + selection.length + " item(s) selected!");
	
	if (selection.length > 0) {
	
		var item = selection[0];
	
		if (item.row != null) {
		
			var target      = songData.getFormattedValue(item.row, 0);
			var songResults = "";
			
			// alert("Author: " + target);
		  
			if (target != "The Bible" && target != "Yoruba Folklore" && target != "Norse Mythology") {
				songResults = getSongFact( "", "", target, "", "", songType, "Y", "N" );
			}
			else {
				songResults = getSongFact( "", "", "", target, "", songType, "Y", "N" );
			}
		  
			$('#BiblioSongChartResults').html("<div id='BiblioSongChartResults'></div>")
				.hide()
				.fadeIn(1500, function() { 
				  $('#BiblioSongChartResults').append( songResults ); 
				});
		}		
	}
}

function classicRockHandler(e) { songChartHandler("CRK", barChartCrck, crck_data); }
function rockHandler(e) { songChartHandler("RCK", barChartRck, rck_data); }
function popHandler(e) { songChartHandler("POP", barChartPop, pop_data); }
function hipHopHandler(e) { songChartHandler("HHR", barChartRap, rap_data); }
function metalHandler(e) { songChartHandler("MTL", barChartMetal, metal_data); }
function gospelHandler(e) { songChartHandler("GPL", barChartGospel, gspl_data); }
function alternativeHandler(e) { songChartHandler("ALT", barChartAlt, alt_data); }
function sncHandler(e) { songChartHandler("SNC", barChartCmp, cmp_data); }
function countryRockHandler(e) { songChartHandler("CTY", barChartCntry, cntry_data); }

function translateBookType(key) {

    if (key == "Mythology") {
		return "HST";
	}
	else if (key == "Classic Lit") {
		return "CAL";
	}
	else if (key == "SciFi/Fantasy") {
		return "SFF";
	}
	else if (key == "Ancient Txt") {
		return "ATX";
	}
	else if (key == "Fiction") {
		return "FTN";
	}
	else if (key == "Myth/History") {
		return "HST";
	}
	else if (key == "Nonfiction") {
		return "NFN";
	}
	else if (key == "Classic Lit") {
		return "CAL";
	}
	else if (key == "Poetry") {
		return "POE";
	}
	else {
		return "??";
	}
}

function gameChartHandler(gameType, gameChart, gameData) {

	var selection = gameChart.getSelection();
	
    // alert("Inside Game Chart Handler - " + selection.length + " item(s) selected!");
	
	if (selection.length > 0) {
	
		var item = selection[0];
	
		if (item.row != null) {
		
			var target      = gameData.getFormattedValue(item.row, 0);
			var gameResults = "";
			
			target = translateBookType(target);
			
			// alert("Book Type: " + target + ", Game Type: " + gameType);
		  
			gameResults = getGameFact( "", "", "", target, gameType, "Y", "N" );
		  
			$('#BiblioGameChartResults').html("<div id='BiblioGameChartResults'></div>")
				.hide()
				.fadeIn(1500, function() { 
				  $('#BiblioGameChartResults').append( gameResults ); 
				});
		}		
	}
}

function classicHandler(e) { gameChartHandler("CLC", barChartClssic, clssc_data); }
function fpsHandler(e) { gameChartHandler("FPS", barChartFps, fps_data); }
function stratHandler(e) { gameChartHandler("RTS", barChartStrat, strat_data); }
function rpgHandler(e) { gameChartHandler("RPG", barChartRpg, rpg_data); }
function puzzleHandler(e) { gameChartHandler("PZS", barChartPzzl, pzzl_data); }
function actionAdvHandler(e) { gameChartHandler("AAA", barChartAa, aa_data); }

function drawCharts() {
    drawSongCharts();
    drawGameCharts();
}

function drawSongCharts() {

    var songChartWidth    = 400;
	var songChartHeight   = 150;
	var songTitleFontSize = 18;
	// alert("Setting the data for the song charts!");
	
    crck_data = new google.visualization.DataTable();
    crck_data.addColumn('string', 'Author');
    crck_data.addColumn('number', 'Count');
    crck_data.addRows([
    ['Tolkien', {v:8, f:'J.R.R. Tolkien - 8 songs'}],
	['The Bible',{v:6, f:'The Bible - 6 songs'}],
    ['Moorcock',{v:4, f:'Michael Moorcock - 4 songs'}],
    ['Orwell',  {v:4, f:'George Orwell - 4 songs'}],
    ['Steinbeck',{v:3, f:'John Steinbeck - 3 songs'}]
    ]);
	    
	// alert("About to draw the song charts!");
    barChartCrck = new google.visualization.BarChart(document.getElementById('song_chart_classic_rock'));
	google.visualization.events.addListener(barChartCrck, 'select', classicRockHandler);
	barChartCrck.draw(crck_data, {width: songChartWidth, height: songChartHeight, 
	                  title: 'Number of Classic Rock Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
						 // colors:['#3366CC','#DC3912','#FF9900','#8A2BE2','#109618']
					 });
					 			  
	// alert("Drew the first song chart!");
		
    rck_data = new google.visualization.DataTable();
    rck_data.addColumn('string', 'Author');
    rck_data.addColumn('number', 'Count');
    rck_data.addRows([
    ['Shakespeare', {v:11, f:'William Shakespeare - 11 songs'}],
	['The Bible',{v:9, f:'The Bible - 9 songs'}],
    ['Poe',{v:11, f:'Edgar Allen Poe - 11 songs'}],
    ['Orwell',  {v:9, f:'George Orwell - 9 songs'}],
    ['Stevenson',{v:8, f:'Robert Loius Stevenson - 8 songs'}]
    ]);
				  
    barChartRck = new google.visualization.BarChart(document.getElementById('song_chart_rock'));
    google.visualization.events.addListener(barChartRck, 'select', rockHandler);
	barChartRck.draw(rck_data, {width: songChartWidth, height: songChartHeight, 
						 title: 'Number of Rock Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
					 
    pop_data = new google.visualization.DataTable();
    pop_data.addColumn('string', 'Author');
    pop_data.addColumn('number', 'Count');
    pop_data.addRows([
    ['Homer', {v:2, f:'Homer - 2 songs'}],
	['The Bible',{v:4, f:'The Holy Bible - 4 songs'}],
    ['Fitzgerald',{v:2, f:'F. Scott Fitzgerald - 2 songs'}],
    ['Bellow',  {v:2, f:'Saul Bellow - 2 songs'}],
    ['Yeats',{v:2, f:'W.B. Yeats - 2 songs'}]
    ]);
				  
    barChartPop = new google.visualization.BarChart(document.getElementById('song_chart_pop'));
    google.visualization.events.addListener(barChartPop, 'select', popHandler);
	barChartPop.draw(pop_data, {width: songChartWidth, height: songChartHeight, 
	                    title: 'Number of Pop Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
					 
    rap_data = new google.visualization.DataTable();
    rap_data.addColumn('string', 'Author');
    rap_data.addColumn('number', 'Count');
    rap_data.addRows([
    ['Cervantes', {v:2, f:'Miguel de Cervantes - 2 songs'}],
	['Yoruba Folklore',{v:2, f:'Yoruba Folklore - 2 songs'}],
    ['Poe',{v:2, f:'Edgar Allen Poe - 2 songs'}],
    ['Alighieri',  {v:2, f:'Dante Alighieri - 2 songs'}],
    ['Shakespeare',{v:2, f:'William Shakespeare - 2 songs'}]
    ]);

    barChartRap = new google.visualization.BarChart(document.getElementById('song_chart_rap'));
    google.visualization.events.addListener(barChartRap, 'select', hipHopHandler);
	barChartRap.draw(rap_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Rap Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });

    metal_data = new google.visualization.DataTable();
    metal_data.addColumn('string', 'Author');
    metal_data.addColumn('number', 'Count');
    metal_data.addRows([
    ['Norse Mythology', {v:10, f:'Norse Mythology - 10 songs'}],
	['King',{v:10, f:'Stephen King - 10 songs'}],
    ['Poe',{v:6, f:'Edgar Allen Poe - 6 songs'}],
    ['Goethe',  {v:7, f:'Johann Wolfgang von Goethe - 7 songs'}],
	['Alighieri',  {v:9, f:'Dante Alighieri - 9 songs'}],
    ['Card',{v:3, f:'Orson Scott Card - 3 songs'}]
    ]);
	
    barChartMetal = new google.visualization.BarChart(document.getElementById('song_chart_metal'));
    google.visualization.events.addListener(barChartMetal, 'select', metalHandler);
	barChartMetal.draw(metal_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Metal Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
			  	
    gspl_data = new google.visualization.DataTable();
    gspl_data.addColumn('string', 'Author');
    gspl_data.addColumn('number', 'Count');
    gspl_data.addRows([
    ['The Bible', {v:1, f:'The Bible - 1 song'}],
	['Hurnard',{v:1, f:'Hannah Hurnard - 1 song'}],
    ['Bonhoeffer',{v:1, f:'Dietrich Bonhoeffer - 1 song'}],
    ['Stevenson',  {v:3, f:'Robert Louis Stevenson - 3 songs'}]
    ]);
		
    barChartGospel = new google.visualization.BarChart(document.getElementById('song_chart_gospel'));
    google.visualization.events.addListener(barChartGospel, 'select', gospelHandler);
	barChartGospel.draw(gspl_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Gospel Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
	
    alt_data = new google.visualization.DataTable();
    alt_data.addColumn('string', 'Author');
    alt_data.addColumn('number', 'Count');
    alt_data.addRows([
    ['Shakespeare', {v:4, f:'William Shakespeare - 4 songs'}],
	['Danielewski',{v:3, f:'Mark Z. Danielewski - 3 songs'}],
    ['Goethe',{v:3, f:'Johann Wolfgang von Goethe - 3 songs'}],
    ['Eggers',  {v:2, f:'David Eggers - 2 songs'}],
	['Palahniuk',  {v:2, f:'Chuck Palahniuk - 2 songs'}]
    ]);
		
    barChartAlt = new google.visualization.BarChart(document.getElementById('song_chart_alternative'));
    google.visualization.events.addListener(barChartAlt, 'select', alternativeHandler);
	barChartAlt.draw(alt_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Alternative Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
		  	
    cmp_data = new google.visualization.DataTable();
    cmp_data.addColumn('string', 'Author');
    cmp_data.addColumn('number', 'Count');
    cmp_data.addRows([
    ['The Bible', {v:4, f:'The Bible - 4 songs'}],
	['Yeats',{v:8, f:'W.B. Yeats - 8 songs'}],
    ['Cervantes',{v:5, f:'Miguel de Cervantes - 5 songs'}],
    ['Goethe',{v:5, f:'Johann Wolfgang von Goethe - 5 songs'}],
	['Alighieri',  {v:6, f:'Dante Alighieri - 6 songs'}],	
    ['Eliot',  {v:2, f:'T.S. Eliot - 2 songs'}],
	['Woolf',  {v:2, f:'Virginia Woolf - 2 songs'}]
    ]);
		
    barChartCmp = new google.visualization.BarChart(document.getElementById('song_chart_songwriter'));
    google.visualization.events.addListener(barChartCmp, 'select', sncHandler);
	barChartCmp.draw(cmp_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Songwriter Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
		
    cntry_data = new google.visualization.DataTable();
    cntry_data.addColumn('string', 'Author');
    cntry_data.addColumn('number', 'Count');
    cntry_data.addRows([
    ['Bryson', {v:2, f:'Bill Bryson - 2 songs'}],
	['Melville',{v:1, f:'Herman Melville - 1 song'}],
    ['Welty',{v:1, f:'Eudora Welty - 1 song'}],
    ['Burns', {v:1, f:'Robert Burns - 1 song'}],
	['Coleridge',{v:1, f:'Samuel Taylor Coleridge - 1 song'}]
    ]);
	
    barChartCntry = new google.visualization.BarChart(document.getElementById('song_chart_country'));
    google.visualization.events.addListener(barChartCntry, 'select', countryRockHandler);
	barChartCntry.draw(cntry_data, {width: songChartWidth, height: songChartHeight, 
	                     title: 'Number of Country Songs', titleFontSize:songTitleFontSize,
					     vAxis: {title: 'Authors', titleColor: 'red'}
					 });
}

function drawGameCharts() {

    var gameChartWidth    = 400;
	var gameChartHeight   = 150;
	var gameTitleFontSize = 18;
	// alert("Setting the data for the song charts!");
	
    clssc_data = new google.visualization.DataTable();
    clssc_data.addColumn('string', 'BookSubject');
    clssc_data.addColumn('number', 'Count');
    clssc_data.addRows([
    ['Mythology', {v:5, f:'Mythology - 5 games'}],
	['Classic Lit',{v:6, f:'Classic Literature - 4 games'}],
    ['SciFi/Fantasy',{v:4, f:'Science Fiction/Fantasy - 3 games'}],
    ['Ancient Txt',  {v:4, f:'Ancient Text - 1 game'}]
    ]);
	
	// alert("About to draw the song charts!");
    // Create and draw the visualization.
    barChartClssic = new google.visualization.BarChart(document.getElementById('game_chart_classic_games'));
    google.visualization.events.addListener(barChartClssic, 'select', classicHandler);
	barChartClssic.draw(clssc_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of Classic Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });
			  
	// alert("Drew the first song chart!");
	
    fps_data = new google.visualization.DataTable();
    fps_data.addColumn('string', 'BookSubject');
    fps_data.addColumn('number', 'Count');
    fps_data.addRows([
    ['SciFi/Fantasy', {v:8, f:'Science Fiction/Fantasy - 8 games'}],
	['Fiction',{v:7, f:'Fiction - 7 games'}],
    ['Myth/History',{v:6, f:'Mythology/History - 6 songs'}]    ]);
	
    barChartFps = new google.visualization.BarChart(document.getElementById('game_chart_fps'));
    google.visualization.events.addListener(barChartFps, 'select', fpsHandler);
	barChartFps.draw(fps_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of First-Person Shooter Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });

    strat_data = new google.visualization.DataTable();
    strat_data.addColumn('string', 'BookSubject');
    strat_data.addColumn('number', 'Count');
    strat_data.addRows([
    ['Nonfiction', {v:12, f:'Nonfiction - 12 games'}],
	['SciFi/Fantasy',{v:9, f:'Science Fiction/Fantasy - 9 games'}],
    ['Ancient Txt',{v:7, f:'Ancient Text - 7 games'}],
    ['Myth/History',  {v:2, f:'Mythology/History - 2 games'}],
    ['Classic Lit',{v:1, f:'Classic Literature - 1 game'}]
    ]);
				
    barChartStrat = new google.visualization.BarChart(document.getElementById('game_chart_strategy'));
    google.visualization.events.addListener(barChartStrat, 'select', stratHandler);
	barChartStrat.draw(strat_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of Strategy Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });
					 
    rpg_data = new google.visualization.DataTable();
    rpg_data.addColumn('string', 'BookSubject');
    rpg_data.addColumn('number', 'Count');
    rpg_data.addRows([
    ['SciFi/Fantasy', {v:15, f:'Science Fiction/Fantasy - 15 games'}],
	['Myth/History',{v:10, f:'Mythology/History - 10 games'}],
    ['Fiction',{v:7, f:'Fiction - 7 games'}],
    ['Classic Lit',  {v:5, f:'Classic Literature - 5 games'}],
    ['Nonfiction',{v:3, f:'Nonfiction - 3 games'}]
    ]);
			
    barChartRpg = new google.visualization.BarChart(document.getElementById('game_chart_rpg'));
    google.visualization.events.addListener(barChartRpg, 'select', rpgHandler);
	barChartRpg.draw(rpg_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of Role-Playing Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });
					
    pzzl_data = new google.visualization.DataTable();
    pzzl_data.addColumn('string', 'BookSubject');
    pzzl_data.addColumn('number', 'Count');
    pzzl_data.addRows([
    ['Fiction', {v:3, f:'Fiction - 3 games'}],
	['Myth/History',{v:2, f:'Mythology/History - 2 games'}],
    ['Classic Lit',{v:2, f:'Classic Literature - 2 games'}],
    ['Poetry',  {v:2, f:'Poetry - 2 games'}]
    ]);
						  
    barChartPzzl = new google.visualization.BarChart(document.getElementById('game_chart_puzzle'));
    google.visualization.events.addListener(barChartPzzl, 'select', puzzleHandler);
	barChartPzzl.draw(pzzl_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of Puzzle Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });

    aa_data = new google.visualization.DataTable();
    aa_data.addColumn('string', 'BookSubject');
    aa_data.addColumn('number', 'Count');
    aa_data.addRows([
    ['SciFi/Fantasy', {v:41, f:'Science Fiction/Fantasy - 41 games'}],
	['Classic Lit',{v:33, f:'Classic Literature - 33 games'}],
    ['Myth/History',{v:31, f:'Mythology/History - 31 games'}],
    ['Fiction',  {v:29, f:'Fiction - 29 games'}],
	['Ancient Txt',  {v:8, f:'Ancient Text - 8 games'}]
    ]);
	
    barChartAa = new google.visualization.BarChart(document.getElementById('game_chart_aa'));
    google.visualization.events.addListener(barChartAa, 'select', actionAdvHandler);
	barChartAa.draw(aa_data, {width: gameChartWidth, height: gameChartHeight, 
	                     title: 'Number of Action-Adventure Games', titleFontSize:gameTitleFontSize,
					     vAxis: {title: 'Subjects', titleColor: 'red'}
					 });
}

function writeSongCharts() {
    document.write("<div style=\"width:450px;height:300px;overflow:scroll;border:solid white\" >");
	document.write("<table>");
	document.write("<tr><td><div id=\"song_chart_classic_rock\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_rock\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_pop\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_rap\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_metal\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_gospel\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_alternative\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_songwriter\" \></td></tr>");
	document.write("<tr><td><div id=\"song_chart_country\" \></td></tr>");
	document.write("</table>");
	document.write("</div>");
}

function writeGameCharts() {
    document.write("<div style=\"width:450px;height:300px;overflow:scroll;border:solid white\" >");
	document.write("<table>");
	document.write("<tr><td><div id=\"game_chart_classic_games\" \></td></tr>");
	document.write("<tr><td><div id=\"game_chart_fps\" \></td></tr>");
	document.write("<tr><td><div id=\"game_chart_strategy\" \></td></tr>");
	document.write("<tr><td><div id=\"game_chart_rpg\" \></td></tr>");
	document.write("<tr><td><div id=\"game_chart_puzzle\" \></td></tr>");
	document.write("<tr><td><div id=\"game_chart_aa\" \></td></tr>");
	document.write("</table>");
	document.write("</div>");
}

/*
if (searchAuthorText == "Levitt") {

	var keys = ['ISBN:006073132X','ISBN:9780060731328'];
	var cburl = "http://books.google.com/books";
	
	resultsTable += getPreviewButton(cburl, keys);
}
else if (searchAuthorText == "Verne") {

	var keys2 = ['ISBN:038533303X','ISBN:9780385333030'];
	var cburl2 = "http://books.google.com/books";
	
	resultsTable += getPreviewButton(cburl2, keys2);
}

function getPreviewButton(cburl, keys) {

    var previewButtonLink = "";
    
	// Retrieve the info for these bibkeys           
	var api_url = cburl + "?jscmd=viewapi&bibkeys=" + keys.join();
	document.write(unescape("%3Cscript src=" + api_url +
	  " type='text/javascript'%3E%3C/script%3E"));
	  
	var buttonImg =
	  'http://code.google.com/apis/books/images/gbs_preview_button1.gif';
	  
	for (isbn in _GBSBookInfo) {
	  var b = _GBSBookInfo[isbn];
	  if (b && (b.preview == "full" || b.preview == "partial")) {
		previewButtonLink += '<a href="' + b.preview_url + '">';
		previewButtonLink += '<img ' + 'src="' + buttonImg + '" ' + 'style="border:0; margin:3px;" />';
		previewButtonLink += '<\/a>';
		break;
	  }
	}
	
	return previewButtonLink;
}
*/

function listEntries(booksInfo) {

  // Clear any old data to prepare to display the Loading... message.
  var div = document.getElementById("BiblioQuoteSearchPreviewLink");
  if (div.firstChild) div.removeChild(div.firstChild);
 
  var mainDiv = document.createElement("div");
  
  for (i in booksInfo) {

    var book = booksInfo[i];
  
    if (book.preview != "noview"){

	    // Create a DIV for each book
	    var thumbnailDiv = document.createElement("div");
	    thumbnailDiv.className = "thumbnail";
	 
	    // Add a link to each book's informtaion page
	    var a = document.createElement("a");
	    // a.href = book.info_url;
		a.href = book.preview_url;
		a.target = "_blank";
	    // a.innerHTML = book.bib_key;
	 
	    // Display a thumbnail of the book's cover
	    var img = document.createElement("img");
	    img.src = book.thumbnail_url;
	    a.appendChild(img);
	 
	    thumbnailDiv.appendChild(a);
        		
	    // Alert the user that the book is not previewable
	    var p = document.createElement("p");
	    // p.innerHTML = book.preview;
		p.innerHTML = "Google Preview";
		/*
	    if (p.innerHTML == "noview"){
	        p.style.fontWeight = "bold";
	        p.style.color = "#f00";    
	    }
		*/
	 
	    thumbnailDiv.appendChild(p);
	    mainDiv.appendChild(thumbnailDiv);
		
		break;
	}
  }
  
  div.appendChild(mainDiv);
}

function listEntriesAlt(booksInfo) {

  // Clear any old data to prepare to display the Loading... message.
  var div = document.getElementById("BiblioTriviaQuotePreviewLink");
  if (div.firstChild) div.removeChild(div.firstChild);
 
  var mainDiv = document.createElement("div");
  
  for (i in booksInfo) {

    var book = booksInfo[i];
  
    if (book.preview != "noview"){

	    // Create a DIV for each book
	    var thumbnailDiv = document.createElement("div");
	    thumbnailDiv.className = "thumbnail";
	 
	    // Add a link to each book's informtaion page
	    var a = document.createElement("a");
	    // a.href = book.info_url;
		a.href = book.preview_url;
		a.target = "_blank";
	    // a.innerHTML = book.bib_key;
	 
	    // Display a thumbnail of the book's cover
	    var img = document.createElement("img");
	    img.src = book.thumbnail_url;
	    a.appendChild(img);
	 
	    thumbnailDiv.appendChild(a);
        		
	    // Alert the user that the book is not previewable
	    var p = document.createElement("p");
	    // p.innerHTML = book.preview;
		p.innerHTML = "Google Preview";
		/*
	    if (p.innerHTML == "noview"){
	        p.style.fontWeight = "bold";
	        p.style.color = "#f00";    
	    }
		*/
	 
	    thumbnailDiv.appendChild(p);
	    mainDiv.appendChild(thumbnailDiv);
		
		break;
	}
  }
  
  div.appendChild(mainDiv);
}
 
function searchForPreview(target_isbn, type) {

  // Clear any old data to prepare to display the Loading... message.
  var div;
  var callback;
  
  if (type == "QuoteSearch") {
      div = document.getElementById("BiblioQuoteSearchPreviewLink");
	  callback = "listEntries";
  }
  else {
      div = document.getElementById("BiblioTriviaQuotePreviewLink");
	  callback = "listEntriesAlt";
  }
  
  if (div.firstChild) div.removeChild(div.firstChild);
 
  /*
  // Show a "Loading..." indicator.
  var div = document.getElementById('BiblioQuoteSearchPreviewLink');  
  var p = document.createElement('p');
  p.appendChild(document.createTextNode('Loading...'));
  div.appendChild(p);
  */
 
  // Delete any previous Google Booksearch JSON queries.
  var jsonScript = document.getElementById("jsonScript");
  if (jsonScript) {
    jsonScript.parentNode.removeChild(jsonScript);
  }
      
  // Add a script element with the src as the user's Google Booksearch query. 
  // JSON output is specified by including the alt=json-in-script argument
  // and the callback funtion is also specified as a URI argument.
  var scriptElement = document.createElement("script");
  scriptElement.setAttribute("id", "jsonScript");
  scriptElement.setAttribute("src",
      "http://books.google.com/books?bibkeys=" + 
      escape(target_isbn) + "&jscmd=viewapi&callback=" + callback);
  scriptElement.setAttribute("type", "text/javascript");
  
  // make the request to Google booksearch
  document.documentElement.firstChild.appendChild(scriptElement);
}

function processEanCollection(resultsTable, type) {
	
	var eanCollectionStart = resultsTable.indexOf("<!--");
	
	if (eanCollectionStart > 0) {
		var eanCollectionEnd = resultsTable.indexOf("-->");
		
		if ( (eanCollectionEnd > 0) && (eanCollectionEnd > eanCollectionStart) ) {
	
			var eanCollection = resultsTable.substr( (eanCollectionStart+4), (eanCollectionEnd-eanCollectionStart-4) );
			
			if ( (eanCollection != undefined) && (eanCollection != "") ) {
				searchForPreview(eanCollection, type);
				// alert(eanCollection);
			}
		}
	}			
}

var first_results_btn = 0;
var first_results_clk = 0;

var page_changed     = 0;
var bf_max_favorites = 3;
var bf_curr_count    = 1;
var bf_fav_array     = new Array(bf_max_favorites);
var bf_fav_lb_array  = new Array(bf_max_favorites);

function StringBuilder(value) {
    this.strings = new Array("");
    this.append(value);
}

// Appends the given value to the end of this instance.
StringBuilder.prototype.append = function (value) {
    if (value) {
        this.strings.push(value);
    }
}

// Clears the string buffer
StringBuilder.prototype.clear = function () {
    this.strings.length = 1;
}

// Converts this instance to a String.
StringBuilder.prototype.toString = function () {
    return this.strings.join("");
}

function scrollDown() {

	if ( first_results_btn == 0 ) {
	    alert("Scroll down for your results!");
		first_results_btn = 1;
	}

	// self.scrollTo(0, 1000);
	// window.scroll(0, 1000);
	// scrollTo(0,100);
	// scrollBy(0,100);
    // _parent.scrollTo(0,100);
    // document.getElementById('if').contentWindow.scrollTo(10,100);
    window.frames[0].scrollBy(0,1000);
	// window.scrollBy(0,1000); // horizontal and vertical scroll increments
    // scrolldelay = setTimeout('pageScroll()',100); // scrolls every 100 milliseconds
}

function openBibliofile() {

    advertise();
	window.open('http://www.bibliophileonline.com');
}

function selectSearchRadio(radio_button_index) {

    document.BF_SearchType_Form.bibliosearchchoice[radio_button_index].checked = "1";
}

function selectGameSearchRadio(radio_button_index) {

    document.BF_Game_SearchType_Form.gamesearchchoice[radio_button_index].checked = "1";
}

function selectLocSearchRadio(radio_button_index) {

    document.BF_Location_SearchType_Form.locsearchchoice[radio_button_index].checked = "1";
}

function selectQuoteSearchRadio(radio_button_index) {

    document.BF_Quote_SearchType_Form.quotesearchchoice[radio_button_index].checked = "1";
}

function getSearchType() {

    var           i = 0;
	var search_type = "";

	for (i=0; i < document.BF_SearchType_Form.bibliosearchchoice.length;i++) {
	
		if (document.BF_SearchType_Form.bibliosearchchoice[i].checked) {
			search_type = document.BF_SearchType_Form.bibliosearchchoice[i].value;
			break;
		}
    }
	
	return search_type;
}

function getGameSearchType() {

    var           i = 0;
	var search_type = "";

    // alert("Game Options:(" + document.BF_Game_SearchType_Form.gamesearchchoice.length + ").");
	
	for (i=0; i < document.BF_Game_SearchType_Form.gamesearchchoice.length;i++) {
		
		if (document.BF_Game_SearchType_Form.gamesearchchoice[i].checked) {
			search_type = document.BF_Game_SearchType_Form.gamesearchchoice[i].value;
			break;
		}
    }
	
	return search_type;
}

function getLocSearchType() {

    var           i = 0;
	var search_type = "";
	
	// alert("Loc Options:(" + document.BF_Location_SearchType_Form.locsearchchoice.length + ").");
	
	for (i=0; i < document.BF_Location_SearchType_Form.locsearchchoice.length;i++) {
		
		if (document.BF_Location_SearchType_Form.locsearchchoice[i].checked) {
			search_type = document.BF_Location_SearchType_Form.locsearchchoice[i].value;
			break;
		}
    }
	
	return search_type;
}

function getQuoteSearchType() {

    var           i = 0;
	var search_type = "";
	
	// alert("Loc Options:(" + document.BF_Quote_SearchType_Form.quotesearchchoice.length + ").");
	
	for (i=0; i < document.BF_Quote_SearchType_Form.quotesearchchoice.length;i++) {
		
		if (document.BF_Quote_SearchType_Form.quotesearchchoice[i].checked) {
			search_type = document.BF_Quote_SearchType_Form.quotesearchchoice[i].value;
			break;
		}
    }
	
	return search_type;
}

function pullRandomValues() {
	
	var songResults = getSongFact( "", "", "", "", "", "", "Y", "Y" );
	
	$('#BiblioSearchResults').html("<div id='BiblioSearchResults'></div>")
		.hide()
		.fadeIn(1500, function() { 
		  $('#BiblioSearchResults').append( songResults ); 
		});

	var gameResults = getGameFact( "", "", "", "", "", "Y", "Y" );
	
	$('#BiblioGameSearchResults').html("<div id='BiblioGameSearchResults'></div>")
		.hide()
		.fadeIn(1500, function() { 
		  $('#BiblioGameSearchResults').append( gameResults );
		});
		
    // var quoteResults = getQuoteFact( "", "Anthem", "", "Y", "N" );
    var quoteResults = getQuoteFact( "", "Fountainhead", "", "Y", "N" );
	
	$('#BiblioQuoteSearchResults').html("<div id='BiblioQuoteSearchResults'></div>")
		.hide()
		.fadeIn(1500, function() { 
		  $('#BiblioQuoteSearchResults').append( quoteResults ); 
		});
		
	// searchForPreview("1606208667", "QuoteSearch");
		
	var triviaResult = getTriviaFact( "Y" );
					
	$('#BiblioTriviaResults').html("<div id='BiblioTriviaResults'></div>").append( triviaResult );
	
	processEanCollection(triviaResult, "TriviaQuote");
}

$('.error').hide();

$(document).ready(function(){

	$("a").click(function(event){
	
		// alert("Remember to check out our new iPhone app via the Apple Store!");
	});
	
	$(".button").click(function(event) {  
					
		// var rank01      = $("input#bf_rank01").val();
		// var delconf     = $("input#bf_del_confirm").val();
		// var searchtype  = $("input#bibliosearchchoice").val();
		
		var searchtype = getSearchType();
		
		event.preventDefault();
									
        // alert("SearchType is (" + searchtype + ").");

		if ((searchtype != undefined) && (searchtype != "")) {
			
			var resultsTable = "";

			if (searchtype == "bookcatsearch") {
				var searchBookCategoryCd = $("select#bf_bookcatchoice").val();

				if ((searchBookCategoryCd != undefined) && (searchBookCategoryCd != "")) {
				    // alert("DEBUG: Searching book category using id(" + searchBookCategoryCd + ")");
					resultsTable = getSongFact( "", "", "", "", searchBookCategoryCd, "", "Y", "N" );
				}
				else {
					alert("Please select a book category.");
					return;
				}
			}
			else if (searchtype == "songcatsearch") {
				var searchSongCategoryCd = $("select#bf_songcatchoice").val();

				if ((searchSongCategoryCd != undefined) && (searchSongCategoryCd != "")) {
					resultsTable = getSongFact( "", "", "", "", "", searchSongCategoryCd, "Y", "N" );
				}
				else {
					alert("Please select a song category.");
					return;
				}
			}
			else if (searchtype == "bfcatsearch") {
				var searchBFCategoryCd = $("select#bf_bfcatchoice").val();

				if ((searchBFCategoryCd != undefined) && (searchBFCategoryCd != "")) {
					resultsTable = getSongFact( "", "", "", "", searchBFCategoryCd, "", "Y", "N" );
				}
				else {
					alert("Please select a Bibliophile category.");
					return;
				}
			}
			else if (searchtype == "textsearch") {
			
			    var searchTextType = $("select#bf_textsearchchoice").val();
				
				// alert("SearchTextType is ( " + searchTextType + ").");
				// alert("SearchText is (" + $("input#bf_search_text").val() + ").");
			
				if (searchTextType == "booksearch") {

					var searchBookText = $("input#bf_search_text").val();

					if ((searchBookText != undefined) && (searchBookText != "")) {
						resultsTable = getSongFact( "", "", "", searchBookText, "", "", "Y", "N" );
					}
					else {
						alert("Please provide a book name (or text which might match a book name).");
						return;
					}				
				}
				else if (searchTextType == "authorsearch") {
					var searchAuthorText = $("input#bf_search_text").val();
					
					if ((searchAuthorText != undefined) && (searchAuthorText != "")) {
						resultsTable = getSongFact( "", "", searchAuthorText, "", "", "", "Y", "N" );						
					}
					else {
						alert("Please provide an author name (or text which might match an author name).");
						return;
					}
				}
				else if (searchTextType == "songsearch") {
					var searchSongText = $("input#bf_search_text").val();
					
					if ((searchSongText != undefined) && (searchSongText != "")) {
						resultsTable = getSongFact( searchSongText, "", "", "", "", "", "Y", "N" );
					}
					else {
						alert("Please provide a song name (or text which might match a song name).");
						return;
					}
				}
				else if (searchTextType == "performersearch") {
					var searchPerfomerText = $("input#bf_search_text").val();
					
					if ((searchPerfomerText != undefined) && (searchPerfomerText != "")) {
						resultsTable = getSongFact( "", searchPerfomerText, "", "", "", "", "Y", "N" );
					}
					else {
						alert("Please provide a performer name (or text which might match a performer name).");
						return;
					}
				}
			}
			else {
				resultsTable = getSongFact( "", "", "", "", "", "", "Y", "Y" );
			}

			/*
			// alert("A search type(" + searchtype + ") was issued, dataString(" + dataString + ").");
			
			resultsTable = $.ajax({
				type: "GET",
				url: "http://api.bibliophileonline.com/bibliofile_retrieve.php",
				data: dataString,
				async: false					
				}).responseText;
				
			// alert("The results look like :(" + resultsTable + ").");
			*/
						
			$('#BiblioSearchResults').html("<div id='BiblioSearchResults'></div>")
			.hide()
			.fadeIn(1500, function() { 
			  $('#BiblioSearchResults').append( resultsTable ); 
			});
						
			// scrollDown();
		}
		else {
			alert("Please select a search type before attempting to search.");
			// alert("ERROR!  This page isn't even good enough for me to poop on.");
		}		
	});

	$(".game_search_button").click(function(event) {  
								
		var searchtype = getGameSearchType();
					
		event.preventDefault();
		
		// alert("Game SearchType :(" + searchtype + ").");
					
		if ((searchtype != undefined) && (searchtype != "")) {

			// alert("A search type(" + searchtype + ") was issued, dataString(" + dataString + ").");
		
			var resultsTable = "";

			if (searchtype == "bookcatsearch") {
				var searchBookCategoryCd = $("select#bfg_bookcatchoice").val();

				if ((searchBookCategoryCd != undefined) && (searchBookCategoryCd != "")) {
					resultsTable = getGameFact( "", "", "", searchBookCategoryCd, "", "Y", "N" );
				}
				else {
					alert("Please select a book category.");
					return;
				}
			}
			else if (searchtype == "gamecatsearch") {
				var searchGameCategoryCd = $("select#bfg_gamecatchoice").val();

				if ((searchGameCategoryCd != undefined) && (searchGameCategoryCd != "")) {
					resultsTable = getGameFact( "", "", "", "", searchGameCategoryCd, "Y", "N" );
				}
				else {
					alert("Please select a game category.");
					return;
				}
			}
            else if (searchtype == "textsearch") {
				
				var searchTextType = $("select#bfg_textsearchchoice").val();
				
				if (searchTextType == "booksearch") {
					var searchBookText = $("input#bfg_search_text").val();
					
					if ((searchBookText != undefined) && (searchBookText != "")) {
						resultsTable = getGameFact( "", "", searchBookText, "", "", "Y", "N" );
					}
					else {
						alert("Please provide a book name (or text which might match a book name).");
						return;
					}
				}
				else if (searchTextType == "authorsearch") {
					var searchAuthorText = $("input#bfg_search_text").val();
					
					if ((searchAuthorText != undefined) && (searchAuthorText != "")) {
						resultsTable = getGameFact( "", searchAuthorText, "", "", "", "Y", "N" );
					}
					else {
						alert("Please provide an author name (or text which might match an author name).");
						return;
					}
				}
				else if (searchTextType == "gamesearch") {
					var searchGameText = $("input#bfg_search_text").val();
					
					if ((searchGameText != undefined) && (searchGameText != "")) {
						resultsTable = getGameFact( searchGameText, "", "", "", "", "Y", "N" );
					}
					else {
						alert("Please provide a game name (or text which might match a game name).");
						return;
					}
				}
			}
			else {
				// alert("Random game search initiated!");
				resultsTable = getGameFact( "", "", "", "", "", "Y", "Y" );
			}
			
			/*
			resultsTable = $.ajax({
				type: "GET",
				url: "http://api.bibliophileonline.com/bibliofile_retrieve.php",
				data: dataString,
				async: false					
				}).responseText;
				
			// alert("The results look like :(" + resultsTable + ").");
			*/
			
			$('#BiblioGameSearchResults').html("<div id='BiblioGameSearchResults'></div>")
			.hide()
			.fadeIn(1500, function() { 
			  $('#BiblioGameSearchResults').append( resultsTable ); 
			});
						
			// scrollDown();
		}
		else {
			alert("Please select a search type before attempting to search.");
			// alert("ERROR!  This page isn't even good enough for me to poop on.");
		}
	});	
		
	$(".location_search_button").click(function(event) {
	
		var searchtype = getLocSearchType();
					
		event.preventDefault();
		
		// alert("Location SearchType :(" + searchtype + ").");
					
		if ((searchtype != undefined) && (searchtype != "")) {

			// alert("A search type(" + searchtype + ") was issued, dataString(" + dataString + ").");
		
			var resultsTable = "";
			
			if (searchtype == "coordinatesearch") {
			
				resultsTable = getLatLocResults();
				
				if (window.BibliophileJSInterface) {
					window.BibliophileJSInterface.setResults(resultsTable);
				}
				else {
					$('#BiblioLocSearchResults').html("<div id='BiblioLocSearchResults'></div>")
					.hide()
					.fadeIn(1500, function() { 
					  $('#BiblioLocSearchResults').append( resultsTable ); 
					});
				}
	        }
			else if (searchtype == "locationsearch") {
			
				var locationText = $("input#bfl_loc_search_text").val();
			
				geocoder.geocode( { 'address': locationText}, function(results, status) {
				
					  if (status == google.maps.GeocoderStatus.OK) {
					  
						marker.setPosition(results[0].geometry.location);
					  
						handleMarkerMove(results[0].geometry.location);
						
						resultsTable = getLatLocResults();
						
						if (window.BibliophileJSInterface) {
							window.BibliophileJSInterface.setResults(resultsTable);
						}				
						else {
							$('#BiblioLocSearchResults').html("<div id='BiblioLocSearchResults'></div>")
							.hide()
							.fadeIn(1500, function() { 
							  $('#BiblioLocSearchResults').append( resultsTable ); 
							});
						}
	
					  } else {
						alert("Unable to locate that address for the following reason: " + status);
					  }
				});
					
				/*
				if ((locationText != undefined) && (locationText != "")) {
								
					resultsTable = getLocationFact( locationText, "", "", "", "Y", "N" );
					
					if ((resultsTable == undefined) || (resultsTable == "")) {
						return;
					}
				}
				else {
					alert("Please provide a location (or text which might match a location).");
					return;
				}
				*/
			}
			else {
				alert("Please select a search type before attempting to search.");
			}
		}
		else {
			alert("Please select a search type before attempting to search.");
			// alert("ERROR!  This page isn't even good enough for me to poop on.");
		}
	});	
	
	$(".quote_search_button").click(function(event) {  
								
		var searchtype = getQuoteSearchType();
					
		event.preventDefault();
		
		// alert("Game SearchType :(" + searchtype + ").");
					
		if ((searchtype != undefined) && (searchtype != "")) {

			// alert("A search type(" + searchtype + ") was issued, dataString(" + dataString + ").");
		
			var resultsTable = "";
			
			if (searchtype == "booksearch") {
				var searchBookText = $("input#bfq_book_search_text").val();
				
				if ((searchBookText != undefined) && (searchBookText != "")) {
					resultsTable = getQuoteFact( "", searchBookText, "", "Y", "N" );
				}
				else {
					alert("Please provide a book name (or text which might match a book name).");
					return;
				}
			}
			else if (searchtype == "authorsearch") {
				var searchAuthorText = $("input#bfq_author_search_text").val();
				
				if ((searchAuthorText != undefined) && (searchAuthorText != "")) {
					resultsTable = getQuoteFact( searchAuthorText, "", "", "Y", "N" );
				}
				else {
					alert("Please provide an author name (or text which might match an author name).");
					return;
				}
			}
			else if (searchtype == "keywordsearch") {
				var searchText = $("input#bfq_kw_search_text").val();
				
				if ((searchText != undefined) && (searchText != "")) {
					resultsTable = getQuoteFact( "", "", searchText, "Y", "N" );
				}
				else {
					alert("Please provide a keyword (or text which might match a game name).");
					return;
				}
			}
			/*
			 * NOTE: May be supported in the near future
			else {
				resultsTable = getQuoteFact( "", "",  "", "Y", "N" );
			}
			*/
						
			$('#BiblioQuoteSearchResults').html("<div id='BiblioQuoteSearchResults'></div>")
			.hide()
			.fadeIn(1500, function() { 
			  $('#BiblioQuoteSearchResults').append( resultsTable ); 
			});

	
		    if ( resultsTable.indexOf("<!--") >= 0 ) {
			
				processEanCollection(resultsTable, "QuoteSearch");
			}
			else {
			
				$('#BiblioQuoteSearchPreviewLink').html("<div id='BiblioQuoteSearchPreviewLink'></div>")
				.hide()
				.fadeIn(1500, function() { 
				  $('#BiblioQuoteSearchPreviewLink').append( "" ); 
				});
			}
			
			// scrollDown();
		}
		else {
			alert("Please select a search type before attempting to search.");
			// alert("ERROR!  This page isn't even good enough for me to poop on.");
		}
	});	
	
	$(".trivia_button").click(function(event) {  
											
		event.preventDefault();
					
		var trivaResult = "";

		// alert("DataString(" + dataString + ").");
		
		triviaResult = getTriviaFact( "Y" );
			
		// alert("The results look like :(" + triviaResult + ").");
				
		$('#BiblioTriviaResults').html("<div id='BiblioTriviaResults'></div>").append( triviaResult );
		/*
		.hide()
		.fadeIn(1500, function() { 
		  $('#BiblioTriviaResults').append( triviaResult ); 
		});
		*/
				
		if ( triviaResult.indexOf("<!--") >= 0 ) {
		
			processEanCollection(triviaResult, "TriviaQuote");
		}
		else {
				
			$('#BiblioTriviaQuotePreviewLink').html("<div id='BiblioTriviaQuotePreviewLink'></div>")
			.hide()
			.fadeIn(1500, function() { 
			  $('#BiblioTriviaQuotePreviewLink').append( "" ); 
			});
		}
	});
});

function makeWidget() {

var browser_type = detectBrowser();

var height = 500;

if (window.alt_height) {
	height = window.alt_height;
}

document.write("<div style=\"border:1px solid gray; background-color:#aaaaaa; width:98%; margin-bottom: 1em; padding: 5px \">");

document.write("<br/>");
document.write("<a style=\"color:#ff5555\" onclick=\"initialize_gmap();\" >Refresh map</a>");
document.write("<div id=\"map_canvas\" style=\"width:98%; height:" + height + "px\"></div>");
document.write("<form name=\"BF_Location_SearchType_Form\" action=\"\" >");
document.write("<input type=\"radio\" name=\"locsearchchoice\" id=\"locsearchchoice\" value=\"coordinatesearch\" />");
// document.write("<table border=\"0\" ><tr>");
// document.write("<td>");
document.write("Latitude: <input style=\"width:50px\" type=\"text\" name=\"bfl_latitude_text\" id=\"bfl_latitude_text\" onclick=\"selectLocSearchRadio(0);\" />");
// document.write("</td>");
// document.write("<td>");
document.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
document.write("Longitude: <input style=\"width:50px\" type=\"text\" name=\"bfl_longitude_text\" id=\"bfl_longitude_text\" onclick=\"selectLocSearchRadio(0);\" />");
// document.write("</td>");
// document.write("</tr></table>");
document.write("<br />");
document.write("<input type=\"radio\" name=\"locsearchchoice\" id=\"locsearchchoice\" value=\"locationsearch\" />By a location: ");
document.write("<input type=\"text\" name=\"bfl_loc_search_text\" id=\"bfl_loc_search_text\" onclick=\"selectLocSearchRadio(1);\" />");
document.write("<br />");
document.write("<input name=\"location_search_submit\" type=\"submit\" class=\"location_search_button\" id=\"search_bf_loc_btn\" value=\"Search\" />");
document.write("<br />");
document.write("</form>");
// document.write("<div id=\"BiblioLocSearchResults\" style=\"width:375px;height:300px;overflow:scroll;border:solid white\"></div>");
// document.write("</p>");

document.write("</div>");

}