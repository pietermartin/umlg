(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            ContextManager: ContextManager
        }
    });

    function ContextManager() {

        var self = this;

        function init() {
        }

        function refresh(name, uri, contextVertexId) {
            //build context path to root
            if (name === 'Root') {
                createContextPath([{name: 'Root', uri: uri}]);
//                updateContextHeading('Root', 0);
            } else {
                var replacedUri = uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var pathToCompositeRootUri = replacedUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId) + '/compositePathToRoot';
                $.ajax({
                    url: pathToCompositeRootUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
                    success: function(response, textStatus, jqXHR) {
                        createContextPath(response.data);
//                        updateContextHeading(name, contextVertexId, replacedUri);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert('Error creating context path. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

//        function updateContextHeading(name, id, url) {
//            $('#contextHeading').remove();
//            var contextHeadingDiv = $('<div />', {id: "contextHeading"}).appendTo('.ui-layout-north');
//            var b = $('<b data=' + url + ' class="contextHeadingB"/>').appendTo(contextHeadingDiv);
//            var a = $('<a />', {href: url, text: name + '[' + id + ']', title: name + '[' + id + ']', click :
//                function(e) {
//                    var url = $.data(e.target).data;
//                    self.onClickContextMenu.notify({uri: url, name: "unused"}, null, self);
//                    return false;
//                }
//            });
//            a.data('data', url);
//            a.appendTo(b);
//        }

        function createContextPath(data) {
            $('#contextRoot').remove();
            $('<div />', {id: 'contextRoot'}).appendTo('.ui-layout-north');
            var menu = data.reverse();
            $.each(data, function(index, property) {
                var b = {};
                if (index === 0) {
                    b = $('<b class="contextPath" data=' + property.uri + '/>').appendTo("#contextRoot");
                } else {
                    $('#contextRoot').append(' | ');
                    b = $('<b class="contextPath" data=' + property.uri + '/>').appendTo("#contextRoot");
                }
                var a = $('<a />', {href: property.uri, text: property.name, title: property.name, click:
                    function(e) {
                        var url = $.data(e.target).data;
                        self.onClickContextMenu.notify({uri: url, name: "unused"}, null, self);
                        return false;
                    }
                });
                a.data('data', property.uri);
                a.appendTo(b);
            });
        }

        //Public api
        $.extend(this, {
            "TumlContextManagerVersion": "1.0.0",
            "onClickContextMenu": new Tuml.Event(),
            "refresh": refresh
        });

        init();
    }

})(jQuery);
