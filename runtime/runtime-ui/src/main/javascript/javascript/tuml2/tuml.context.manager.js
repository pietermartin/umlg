(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            ContextManager: ContextManager
        }
    });

    function ContextManager() {

        var self = this;

        this.goBackOne = function () {

        }

        this.setFocus = function() {
            var contextRootlink = $('#contextRoot a');
            $(contextRootlink[0]).addClass('ui-state-focus');
            contextRootlink[0].focus();
        }

        this.refresh = function (name, uri, contextVertexId) {
            //build context path to root
            if (name === 'Root') {
                createContextPath([
                    {name: 'Root', uri: uri}
                ]);
            } else {
                var replacedUri = uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var pathToCompositeRootUri = replacedUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId) + '/compositePathToRoot';
                $.ajax({
                    url: pathToCompositeRootUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (response) {
                        createContextPath(response.data);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('Error creating context path. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        function createContextPath(data) {
            $('#contextRoot').remove();
            $('<div />', {id: 'contextRoot', class: 'ui-widget ui-widget-default ui-state-default ui-corner-all'}).appendTo('.ui-layout-north');
            data.reverse();
            $.each(data, function (index, property) {
                var b = {};
                if (index === 0) {
                    b = $('<h3 data=' + property.uri + ' class=' + "ui-helper-reset" + '/>').appendTo("#contextRoot");
                } else {
                    $('<span />').text(' | ').appendTo($('#contextRoot'));
                    b = $('<h3 data=' + property.uri + ' class=' + "ui-helper-reset" + '/>').appendTo("#contextRoot");
                }
                var a = $('<a />', {href: property.uri, text: property.name, title: property.name, tabindex: index + 1, class: 'ui-corner-all', click: function (e) {
                    var url = $.data(e.target).data;
                    self.onClickContextMenu.notify({uri: url, name: "unused"}, null, self);
                    return false;
                }
                });
                a.data('data', property.uri);
                a.appendTo(b);
            });

            $('.embossed').remove();
            var p = $('<div />', {class: 'embossed'});
            $('<span />').text("UmlG").appendTo(p);
            p.appendTo('.ui-layout-north');
        }

        //Public api
        $.extend(this, {
            "TumlContextManagerVersion": "1.0.0",
            "onClickContextMenu": new Tuml.Event()
        });

    }

})(jQuery);
