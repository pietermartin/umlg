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
            if (name === 'Root') {
                createContextPath([
                    {name: 'Root', uri: uri}
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
            var contextRoot = $('<ol />', {id: 'contextRoot', class: 'breadcrumb uml-breadcrumb'}).appendTo('.collapse.navbar-collapse');
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

//            $('#umlgBrandRoot').remove();
//            var brandUl = $('<ol />', {id: 'umlgBrandRoot', class: 'nav navbar-nav'}).appendTo('.collapse.navbar-collapse');
//            var brandLi = $('<li />').appendTo(brandUl);
//            $('<a />', {class: 'navbar-brand', href: 'http://umlg.org', target: '_blank'}).text('Umlg').appendTo(brandLi);

            /*
             $('#contextRoot').remove();
             var contextRoot = $('<div />', {id: 'contextRoot', class: 'ui-widget ui-state-default'}).appendTo('.ui-layout-north');

             contextRoot.keydown(function(e) {
             if (e.which == 39) {
             //right arrow
             var current = contextRoot.find('li.ui-state-focus');
             var next = current.next('li');
             current.removeClass('ui-state-focus');
             next.addClass('ui-state-focus');
             next.find('a')[0].focus();
             } else if (e.which == 37) {
             //left arrow
             var current = contextRoot.find('li.ui-state-focus');
             var previous = current.prev('li');
             current.removeClass('ui-state-focus');
             previous.addClass('ui-state-focus');
             previous.find('a')[0].focus();
             }
             });

             var contextRootUl = $('<ul />', {id: 'contextRootUl', class: 'ui-widget ui-widget-content ui-corner-all'}).appendTo(contextRoot);
             data.reverse();

             for (var i = 0; i < data.length; i++) {
             var property = data[i];
             var li = $('<li data=' + property.uri + ' class=ui-corner-all' + '/>').appendTo(contextRootUl);
             li.on('focusout', function() {$(this).removeClass('ui-state-focus')});
             var a = $('<a />', {href: property.uri, text: property.name, title: property.name, tabindex: i,
             click: function (e) {
             var url = $.data(e.target).data;
             self.onClickContextMenu.notify({uri: url, name: "unused"}, null, self);
             e.preventDefault();
             return false;
             }
             });
             a.appendTo(li);
             li.on('mouseover focus', function() {$(this).addClass('ui-state-hover')});
             li.on('mouseleave blur', function() {$(this).removeClass('ui-state-hover ui-state-focus')});
             a.data('data', property.uri);
             if (i == 0) {
             $('<span />', {class: 'ui-icon ui-icon-home'}).appendTo(a);
             } else {
             $('<span />', {class: 'ui-icon ui-icon-carat-1-e'}).appendTo(a);
             }
             if (i == data.length - 2) {
             previousUri = a;
             }
             };
             */

//            $('.embossed').remove();
//            var p = $('<div />', {class: 'embossed'});
//            $('<span />').text("UmlG").appendTo(p);
//            p.appendTo(contextRoot);
        }

        //Public api
        $.extend(this, {
            "TumlContextManagerVersion": "1.0.0",
            "onClickContextMenu": new Tuml.Event()
        });

    }

})(jQuery);
