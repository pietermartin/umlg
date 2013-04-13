(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            ContextManager: ContextManager
        }
    });

    function ContextManager() {

        var self = this;

        this.refresh = function(name, uri, contextVertexId) {
            //build context path to root
            if (name === 'Root') {
                createContextPath([{name: 'Root', uri: uri}]);
            } else {
                var replacedUri = uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var pathToCompositeRootUri = replacedUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId) + '/compositePathToRoot';
                $.ajax({
                    url: pathToCompositeRootUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    success: function(response, textStatus, jqXHR) {
                        createContextPath(response.data);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert('Error creating context path. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

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
            "onClickContextMenu": new Tuml.Event()
        });

    }

})(jQuery);
