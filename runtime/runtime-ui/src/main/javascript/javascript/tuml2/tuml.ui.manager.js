(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            UiManager:UiManager,
            //Copied from Slick.Grid
            Event:Event,
            EventData:EventData,
            EventHandler:EventHandler
        }
    });

    function UiManager(tumlUri) {

        var self = this;
        var menuManager;
        var contextManager;
        var leftMenuManager;
        var mainViewManager;

        this.init = function() {
            //Create layout
            var myLayout = $('body').layout({livePaneResizing:true, north__minSize:40, east:{initClosed:true}, south:{initClosed:true}, west:{minSize:300}});
            myLayout.allowOverflow("north");

            //Create the menu
            menuManager = new Tuml.MenuManager();

            //Create the context manager
            contextManager = new Tuml.ContextManager();
            contextManager.onClickContextMenu.subscribe(function (e, args) {
                self.refresh(args.uri);
                changeMyUrl(args.uri);
            });

            //Create the context manager
            leftMenuManager = new Tuml.LeftMenuManager();
            leftMenuManager.onMenuClick.subscribe(function (e, args) {
                //Do something like refresh the page
                self.refresh(args.uri);
                changeMyUrl(args.uri);
            });
            leftMenuManager.onQueryClick.subscribe(function (e, args) {
//                var queryTabDivName = args.name.replace(/\s/g, '');
                mainViewManager.addQueryTab(false, new Tuml.Query(args.id, args.name, args.name, args.queryString, args.queryEnum, null, args.queryType));
            });

            //Create main view manager
            mainViewManager = new Tuml.TumlMainViewManager(this, leftMenuManager);

            window.onpopstate = function (event) {
                if (event.state !== null && document.location.hash === "") {
                    var pathname = document.location.pathname.replace("/ui2", "");
                    self.refresh(pathname);
                }
            };

            self.refresh(tumlUri);

        }

        this.refresh = function(tumlUri) {
            //Change the browsers url
            changeMyUrl(tumlUri);
            //Call the server for the tumlUri
            var contextVertexId = retrieveVertexId(tumlUri);
            $.ajax({
                url:tumlUri,
                type:"GET",
                dataType:"json",
                contentType:"json",
                success:function (result, textStatus, jqXHR) {
                    //put the meta data in the cache
                    //this needs refactoring to use http OPTION to get the meta data only
                    var metaDataArray = [];
                    for (var i = 0; i < result.length; i++) {
                        var metaData = {data: []};
                        metaData.meta = result[i].meta;
                        metaDataArray.push(metaData);
                    }
                    Tuml.Metadata.Cache.add(metaData.meta.qualifiedName, metaDataArray);

                    mainViewManager.refresh(tumlUri, result);

                    var contextMetaData = getContextMetaData(result, contextVertexId);
                    contextManager.refresh(contextMetaData.name, contextMetaData.uri, contextMetaData.contextVertexId);
                },
                error:function (jqXHR, textStatus, errorThrown) {
                    alert('error getting ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                }
            });
        }

        function getContextMetaData(result, urlId) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            var propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many
                return {name:metaDataNavigatingFrom.name, uri:metaDataNavigatingFrom.uri, contextVertexId:urlId};
            } else {
                //Property is a one
                //This is to check if there is data, if not it is a creation and the context remains that of the parent
                for (var i = 0; i < result.length; i++) {
                    var response = result[i];
                    //There are many data(s) to cater for the multiple concrete types, as its a one take the first one
                    if (response.data === undefined && response.data === null) {
                        alert('UIManager.getContextMetaData response.data !== undefined && response.data !== null');
                    }

                    if (!isNaN(response.data.id) || metaDataNavigatingTo.name === 'Root') {
                        metaDataNavigatingTo = result[i].meta.to;
                        return {name:metaDataNavigatingTo.name, uri:metaDataNavigatingTo.uri, contextVertexId:response.data.id};
                    }
                }
                return {name:metaDataNavigatingFrom.name, uri:metaDataNavigatingFrom.uri, contextVertexId:urlId};
            }
        }

        function findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom) {
            if (metaDataNavigatingFrom == undefined) {
                return null;
            } else {
                //The property one is navigating from is in the metaDataNavigatingFrom,
                //Find the property with the qualifiedName for the metaDataNavigatingTo.qualifiedName
                for (var i = 0; i < metaDataNavigatingFrom.properties.length; i++) {
                    var property = metaDataNavigatingFrom.properties[i];
                    if (property.qualifiedName == qualifiedName) {
                        return property;
                    }
                }
                alert('Property navigatingTo not found!!!');
                return null;
            }
        }

        function changeMyUrl(url) {
            var indexOfSecondBackSlash = url.indexOf('/', 1);
            var firstPart = url.substring(0, indexOfSecondBackSlash);
            var secondPart = url.substring(indexOfSecondBackSlash, url.length);
            var urlToPush;
            if (firstPart !== undefined && firstPart != '') {
                urlToPush = firstPart + '/ui2' + secondPart;
            } else {
                urlToPush = secondPart + '/ui2';
            }
            history.pushState({}, "firefox ignores this", urlToPush);
        }

        //Public api
        $.extend(this, {
            "TumlUiManagerVersion":"1.0.0"
        });

        this.init();
    }

    /***
     * An event object for passing data to event handlers and letting them control propagation.
     * <p>This is pretty much identical to how W3C and jQuery implement events.</p>
     * @class EventData
     * @constructor
     */
    function EventData() {
        var isPropagationStopped = false;
        var isImmediatePropagationStopped = false;

        /***
         * Stops event from propagating up the DOM tree.
         * @method stopPropagation
         */
        this.stopPropagation = function () {
            isPropagationStopped = true;
        };

        /***
         * Returns whether stopPropagation was called on this event object.
         * @method isPropagationStopped
         * @return {Boolean}
         */
        this.isPropagationStopped = function () {
            return isPropagationStopped;
        };

        /***
         * Prevents the rest of the handlers from being executed.
         * @method stopImmediatePropagation
         */
        this.stopImmediatePropagation = function () {
            isImmediatePropagationStopped = true;
        };

        /***
         * Returns whether stopImmediatePropagation was called on this event object.\
         * @method isImmediatePropagationStopped
         * @return {Boolean}
         */
        this.isImmediatePropagationStopped = function () {
            return isImmediatePropagationStopped;
        }
    }

    /***
     * A simple publisher-subscriber implementation.
     * @class Event
     * @constructor
     */
    function Event() {
        var handlers = [];

        this.getHandlers = function() {
            return handlers;
        }

        /***
         * Adds an event handler to be called when the event is fired.
         * <p>Event handler will receive two arguments - an <code>EventData</code> and the <code>data</code>
         * object the event was fired with.<p>
         * @method subscribe
         * @param fn {Function} Event handler.
         */
        this.subscribe = function (fn) {
            handlers.push(fn);
        };

        /***
         * Removes an event handler added with <code>subscribe(fn)</code>.
         * @method unsubscribe
         * @param fn {Function} Event handler to be removed.
         */
        this.unsubscribe = function (fn) {
            for (var i = handlers.length - 1; i >= 0; i--) {
                if (handlers[i] === fn) {
                    handlers.splice(i, 1);
                }
            }
        };

        /***
         * Fires an event notifying all subscribers.
         * @method notify
         * @param args {Object} Additional data object to be passed to all handlers.
         * @param e {EventData}
         *      Optional.
         *      An <code>EventData</code> object to be passed to all handlers.
         *      For DOM events, an existing W3C/jQuery event object can be passed in.
         * @param scope {Object}
         *      Optional.
         *      The scope ("this") within which the handler will be executed.
         *      If not specified, the scope will be set to the <code>Event</code> instance.
         */
        this.notify = function (args, e, scope) {
            e = e || new EventData();
            scope = scope || this;

            var returnValue;
            for (var i = 0; i < handlers.length && !(e.isPropagationStopped() || e.isImmediatePropagationStopped()); i++) {
                returnValue = handlers[i].call(scope, e, args);
            }

            return returnValue;
        };
    }

    function EventHandler() {
        var handlers = [];

        this.subscribe = function (event, handler) {
            handlers.push({
                event:event,
                handler:handler
            });
            event.subscribe(handler);

            return this;  // allow chaining
        };

        this.unsubscribe = function (event, handler) {
            var i = handlers.length;
            while (i--) {
                if (handlers[i].event === event &&
                    handlers[i].handler === handler) {
                    handlers.splice(i, 1);
                    event.unsubscribe(handler);
                    return;
                }
            }

            return this;  // allow chaining
        };

        this.unsubscribeAll = function () {
            var i = handlers.length;
            while (i--) {
                handlers[i].event.unsubscribe(handlers[i].handler);
            }
            handlers = [];

            return this;  // allow chaining
        }
    }

    //Public api
    $.extend(this, {
        "TumlUIVersion":"1.0.0",
        // Events
        "refreshUri":new Tuml.Event()
    });

})(jQuery);
//