function createLeftMenu(menuArray, contextVertexId) {
    $('.ui-left-menu-link').children().remove();
    $.each(menuArray, function(index, value) {
        var property = menuArray[index];
        var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        $('.ui-left-menu-link').append('<li class="ui-left-menu-li" />');
        
        $('.ui-left-menu-li').appendTo(
            $('<a>', {
            text: menuArray[index].name,
            title: menuArray[index].name,
            href: adjustedUri,
            click: function() {
                change_my_url(menuArray[index].name, adjustedUri);
                refreshPageTo(adjustedUri);
                return false;
            }
        })));
    });
}
