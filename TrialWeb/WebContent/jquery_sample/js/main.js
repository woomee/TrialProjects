
$( function() {
    // タブの設定
    $( "#tabs" ).tabs();

    // 検索入力の設定
    $( "#input-keyword" ).autocomplete({
        source: "data/search-result.json",
        minLength: 2,
        select: function( event, ui ) {
          log( "Selected: " + ui.item.value + " aka " + ui.item.id );
        }
    });

    // 検索結果リストの設定
    // $( "#selectable" ).selectable({
    //     stop: function() {
    //       var index = $( "#selectable li" ).index( this );
    //     }
    // });
    
    // JSONから追加
    $.getJSON(
      'data/search-result.json',
      function(data) {
        var newsList = data["news-list"];
        for(var i=0; i<newsList.length; i++) {
          $("#selectable").append("<li class=\"ui-widget-content\">" + newsList[i] + "</li>");
        }

        $( "#selectable" ).selectable();
      }
    );

});