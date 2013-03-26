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

        function init() {
            //Create layout
            var myLayout = $('body').layout({livePaneResizing:true, north__minSize:40, east:{initClosed:true}, south:{initClosed:true}, west:{minSize:300}});
            myLayout.allowOverflow("north");
            //Create the menu
            menuManager = new Tuml.MenuManager();

            //Create the context manager
            contextManager = new Tuml.ContextManager();
            contextManager.onClickContextMenu.subscribe(function (e, args) {
                refresh(args.uri);
                changeMyUrl(args.name, args.uri);
            });

            //Create the context manager
            leftMenuManager = new Tuml.LeftMenuManager();
            leftMenuManager.onMenuClick.subscribe(function (e, args) {
                //Do something like refresh the page
                refresh(args.uri);
                changeMyUrl(args.name, args.uri);
            });
            leftMenuManager.onQueryClick.subscribe(function (e, args) {
                var queryTabDivName = args.name.replace(/\s/g, '');
                mainViewManager.addQueryTab(false, new Tuml.Query(args.id, args.name, args.name, args.queryString, args.queryEnum, null, args.queryType));
            });

            //Create main view manager
            mainViewManager = new Tuml.TumlMainViewManager(leftMenuManager);
            mainViewManager.onPutSuccess.subscribe(function (e, args) {
                self.onPutSuccess.notify(args, e, self);
            });
            mainViewManager.onPutFailure.subscribe(function (e, args) {
                self.onPutFailure.notify(args, e, self);
            });
            mainViewManager.onPostSuccess.subscribe(function (e, args) {
                self.onPostSuccess.notify(args, e, self);
            });
            mainViewManager.onPostFailure.subscribe(function (e, args) {
                self.onPostFailure.notify(args, e, self);
            });
            mainViewManager.onDeleteSuccess.subscribe(function (e, args) {
                self.onDeleteSuccess.notify(args, e, self);
            });
            mainViewManager.onDeleteFailure.subscribe(function (e, args) {
                self.onDeleteFailure.notify(args, e, self);
            });
            mainViewManager.onCancel.subscribe(function (e, args) {
                self.onCancel.notify(args, e, self);
            });

            mainViewManager.onPostInstanceQuerySuccess.subscribe(function (e, args) {
                leftMenuManager.refreshInstanceQuery();
            });
            mainViewManager.onPostClassQuerySuccess.subscribe(function (e, args) {
                leftMenuManager.refreshClassQuery();
            });

            mainViewManager.onSelfCellClick.subscribe(function (e, args) {
                self.onSelfCellClick.notify(args, e, self);
                refresh(args.tumlUri);
                changeMyUrl(args.name, args.tumlUri);
            });
            mainViewManager.onContextMenuClickLink.subscribe(function (e, args) {
                self.onContextMenuClickLink.notify(args, e, self);
                refresh(args.tumlUri);
                changeMyUrl(args.name, args.tumlUri);
            });
            mainViewManager.onContextMenuClickDelete.subscribe(function (e, args) {
                self.onContextMenuClickDelete.notify(args, e, self);
            });
            mainViewManager.onPutOneSuccess.subscribe(function (e, args) {
                self.onPutOneSuccess.notify(args, e, self);
                var contextMetaData = getContextMetaData(args.data, args.tumlUri);
                contextManager.refresh(contextMetaData.name, contextMetaData.uri, contextMetaData.contextVertexId);
                var adjustedUri = contextMetaData.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextMetaData.contextVertexId);
                changeMyUrl(contextMetaData.name, adjustedUri);
            });
            mainViewManager.onPutOneFailure.subscribe(function (e, args) {
                self.onPutOneFailure.notify(args, e, self);
            });
            mainViewManager.onPostOneSuccess.subscribe(function (e, args) {
                self.onPostOneSuccess.notify(args, e, self);
                var contextMetaData = getContextMetaData(args.data);
                contextManager.refresh(contextMetaData.name, contextMetaData.uri, contextMetaData.contextVertexId);
                var adjustedUri = contextMetaData.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextMetaData.contextVertexId);
                changeMyUrl(contextMetaData.name, adjustedUri);
                //This is like calling refresh only we already have the data
                var contextVertexId = retrieveVertexId(adjustedUri);
                mainViewManager.refresh(adjustedUri, args.data);
            });
            mainViewManager.onPostOneFailure.subscribe(function (e, args) {
                self.onPostOneFailure.notify(args, e, self);
            });
            mainViewManager.onDeleteOneSuccess.subscribe(function (e, args) {
                var contextMetaData = getContextMetaData(args.data, args.tumlUri);
                contextManager.refresh(contextMetaData.name, contextMetaData.uri, contextMetaData.contextVertexId);
                var adjustedUri = contextMetaData.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextMetaData.contextVertexId);
                changeMyUrl(contextMetaData.name, adjustedUri);
                //This is like calling refresh only we already have the data
                var contextVertexId = retrieveVertexId(adjustedUri);
                mainViewManager.refresh(adjustedUri, args.data);
            });

            window.onpopstate = function (event) {
                if (document.location.hash === "") {
                    var pathname = document.location.pathname.replace("/ui2", "");
                    refresh(pathname);
                }
            };

            refresh(tumlUri);

        }

        function refresh(tumlUri) {
            //Call the server for the tumlUri
            var contextVertexId = retrieveVertexId(tumlUri);
            $.ajax({
                url:tumlUri,
                type:"GET",
                dataType:"json",
                contentType:"json",
                success:function (result, textStatus, jqXHR) {
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
                    if (response.data.length > 0) {
                        return {name:metaDataNavigatingTo.name, uri:metaDataNavigatingTo.uri, contextVertexId:response.data[0].id};
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

        function changeMyUrl(title, url) {
            var indexOfSecondBackSlash = url.indexOf('/', 1);
            var firstPart = url.substring(0, indexOfSecondBackSlash);
            var secondPart = url.substring(indexOfSecondBackSlash, url.length);
            var urlToPush;
            if (firstPart !== undefined && firstPart != '') {
                urlToPush = firstPart + '/ui2' + secondPart;
            } else {
                urlToPush = secondPart + '/ui2';
            }
            history.pushState({}, title, urlToPush);
        }

        //Public api
        $.extend(this, {
            "TumlUiManagerVersion":"1.0.0",
            //These events are propogated from the grid
            "onPutSuccess":new Tuml.Event(),
            "onPutFailure":new Tuml.Event(),
            "onPostSuccess":new Tuml.Event(),
            "onPostFailure":new Tuml.Event(),
            "onDeleteSuccess":new Tuml.Event(),
            "onDeleteFailure":new Tuml.Event(),
            "onCancel":new Tuml.Event(),
            "onSelfCellClick":new Tuml.Event(),
            "onContextMenuClickLink":new Tuml.Event(),
            "onContextMenuClickDelete":new Tuml.Event(),
            "onPutOneSuccess":new Tuml.Event(),
            "onPutOneFailure":new Tuml.Event(),
            "onPostOneSuccess":new Tuml.Event(),
            "onPostOneFailure":new Tuml.Event()
        });
        init();
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