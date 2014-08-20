(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            ContextManager: ContextManager
        }
    });

    function ContextManager() {

        var self = this;
        var previousUri;

        this.goBackOne = function () {
            if (previousUri !== undefined) {
                previousUri.click();
            }
        }

        this.setFocus = function () {
            var contextRootlink = $('#contextRootUl li');
            $(contextRootlink[0]).addClass('ui-state-focus');
            contextRootlink.find('a')[0].focus();
        }

        this.refresh = function (name, uri, contextVertexId) {
            //build context path to root
            if (name === tumlModelName) {
                createContextPath([
                    {name: name, uri: uri}
                ]);
            } else {
                var replacedUri = uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(contextVertexId));
                var pathToCompositeRootUri = replacedUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(contextVertexId)) + '/compositePathToRoot';
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
            /*
             <ol class="breadcrumb">
             <li><a href="#">Home</a></li>
             <li><a href="#">Library</a></li>
             <li class="active">Data</li>
             </ol>
             */
            $('#contextRoot').remove();
            var contextRoot = $('<ol />', {id: 'contextRoot', class: 'breadcrumb uml-breadcrumb'}).appendTo('#umlgnavbar .collapse.navbar-collapse');
            //Insert keyDown code here start
            //
            //Insert keyDown code here end
            data.reverse();

            for (var i = 0; i < data.length; i++) {
                var property = data[i];
                var li = $('<li data=' + property.uri + '/>').appendTo(contextRoot);
                if (i == 0) {
                    $('<span />', {class: 'glyphicon glyphicon-home'}).appendTo(li);
                }
                var adjustedUri = addUiToUrl(property.uri);
                var a = $('<a />', {href: adjustedUri, text: (i == 0 ? ' ' : '') + property.name, title: property.name, tabindex: i,
                    click: function (e) {
                        var url = $.data(e.target).data;
                        self.onClickContextMenu.notify({uri: removeUiFromUrl(url), name: "unused"}, null, self);
                        e.preventDefault();
                        return false;
                    }
                });
                a.appendTo(li);
                a.data('data', property.uri);
                if (i == data.length - 2) {
                    previousUri = a;
                }
            }

        }

        //Public api
        $.extend(this, {
            "TumlContextManagerVersion": "1.0.0",
            "onClickContextMenu": new Tuml.Event()
        });

    }

})(jQuery);
