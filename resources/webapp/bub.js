
  // Settings
  var min_bubble_count = 40, // Minimum number of bubbles
      max_bubble_count = 80, // Maximum number of bubbles
      min_bubble_size = 3, // Smallest possible bubble diameter (px)
      max_bubble_size = 12; // Maximum bubble blur amount (px)
  
  // Calculate a random number of bubbles based on our min/max
 var bubbleCount = min_bubble_count + Math.floor(Math.random() * (max_bubble_count + 1));

function bubbles() {

  // Create the bubbles
  for (var i = 0; i < bubbleCount; i++) {
    document.getElementById("bub_back").innerHTML += '<div class="bubble-container"><div class="bubble"></div></div>';
  }
  
  // Now randomise the various bubble elements
  var bubs = document.getElementsByClassName("bubble-container");
  
  rand_bubble(bubs);
}

function rand_bubble(bubbles_array){
	if(bubbles_array.length > 0){
	for(b in bubbles_array){
		if(!isNaN(b)){
    
    // Randomise the bubble positions (0 - 100%)
    var pos_rand = Math.floor(Math.random() * 99);
    var pos_rand2 = Math.floor(Math.random() * 99);
    
    // Randomise their size
    var size_rand = min_bubble_size + Math.floor(Math.random() * (max_bubble_size + 1));
    
    // Random blur
    var blur_rand = Math.floor(Math.random() * 4)+2;
    
    // Apply the new styles
    bubbles_array[b].style["left"] = pos_rand + '%';
    bubbles_array[b].style["top"] = pos_rand2 + '%';
    bubbles_array[b].style["-webkit-filter"] = 'blur(' + blur_rand  + 'px)';
    bubbles_array[b].style["-moz-filter"] = 'blur(' + blur_rand  + 'px)';
    bubbles_array[b].style["-ms-filter"] = 'blur(' + blur_rand  + 'px)';
    bubbles_array[b].style["filter"] = 'blur(' + blur_rand  + 'px)';
    
    bubbles_array[b].children[0].style["width"] = size_rand + 'px';
    bubbles_array[b].children[0].style["height"] = size_rand + 'px';
    
	}
	}
}
  }