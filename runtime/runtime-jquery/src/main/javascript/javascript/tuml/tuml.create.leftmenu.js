function createLeftMenu(menuArray, classNameLowerCased, contextVertexId) {
    $('.ui-left-menu-link').children().remove();
    $.each(menuArray, function(index, value) {
        var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        // var adjustedUri = menuArray[index].tumlUri.replace('{' + classNameLowerCased + 'Id}', contextVertexId);
        $('.ui-left-menu-link').append('<li>').append(
            $('<a>', {
            text: menuArray[index].name,
            title: menuArray[index].name,
            href: adjustedUri,
            click: function() {
                change_my_url(menuArray[index].name, adjustedUri);
                refreshPageTo(adjustedUri);
                return false;
            }
        })).append('</li>');
    });
}
