
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
    $( "#selectable" ).selectable({
        stop: function() {
          var result = $( "#select-result" ).empty();
          $( ".ui-selected", this ).each(function() {
            var index = $( "#selectable li" ).index( this );
            result.append( " #" + ( index + 1 ) );
          });
        }
    });
    
    

});