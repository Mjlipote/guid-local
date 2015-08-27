var client = new ZeroClipboard( document.getElementById("copy-button"), {
  moviePath: "/zclip/js/ZeroClipboard.swf"
} );

client.on( "load", function(client) {
  // alert( "movie is loaded" );

  client.on( "complete", function(client, args) {
    // `this` is the element that was clicked
    
    //alert("已複製到剪貼簿！" + args.text );
    alert("已複製到剪貼簿！");
  } );
} );