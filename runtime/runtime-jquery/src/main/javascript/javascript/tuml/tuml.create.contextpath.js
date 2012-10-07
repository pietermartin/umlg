function createContextPath(data) {
    $('.ui-layout-north').children().remove();
    var menu = data.reverse();
    $.each(data, function(index, property) {
        var b = {};
        if (index === 0) {
            b = $('<b class="contextPath" data=' + property.uri + '/>').text(property.name).appendTo(".ui-layout-north");
        } else {
            b = $('<b class="contextPath" data=' + property.uri + '/>').text(' | ' + property.name).appendTo(".ui-layout-north");
        }
        b.click(function(e) {
            var url = $(e.target).attr("data");
            change_my_url("unused", url);
            refreshPageTo(url);
        });
    });
}
