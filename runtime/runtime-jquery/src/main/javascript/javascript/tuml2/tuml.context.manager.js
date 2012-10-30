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

        function refresh(contextMetaData, contextVertexId) {
            //build context path to root
            if (contextMetaData.name === 'Root') {
                createContextPath([{name: 'Root', uri: contextMetaData.uri}]);
            } else {
                var pathToCompositeRootUri = contextMetaData.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId) + '/compositePathToRoot';
                $.ajax({
                    url: pathToCompositeRootUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
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
                    b = $('<b class="contextPath" data=' + property.uri + '/>').text(property.name).appendTo("#contextRoot");
                } else {
                    b = $('<b class="contextPath" data=' + property.uri + '/>').text(' | ' + property.name).appendTo("#contextRoot");
                }
                b.click(function(e) {
                    var url = $(e.target).attr("data");
                    self.onClickContextMenu.notify({uri: url, name: "unused"}, null, self);
                    //change_my_url("unused", url);
                    //refreshPageTo(url);
                });
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
