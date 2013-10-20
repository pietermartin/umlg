(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            UiManager: UiManager,
            //Copied from Slick.Grid
            Event: Event,
            EventData: EventData,
            EventHandler: EventHandler,
            FocusEnum: {LEFT_MENU: 0, TOP_CONTEXT : 1, CENTER_TAB: 2, CENTER_GRID: 3}
        }
    });

    function UiManager(tumlUri) {

        var self = this;
        var menuManager;
        var contextManager;
        var leftMenuManager;
        var mainViewManager;
        var currentFocus;

        this.init = function () {
            //Create layout
            var myLayout = $('body').layout(
                {
                    enableCursorHotkey: false,
                    livePaneResizing: true,
                    //This is the navbar height,
                    north__minSize: 51,
                    east: {initClosed: true},
                    south: {minSize: 30, initClosed: false},
                    west: {minSize: 300}
                }
            );
            myLayout.allowOverflow("north");

            //Create the context manager
            contextManager = new Tuml.ContextManager();
            contextManager.onClickContextMenu.subscribe(function (e, args) {
                self.refresh(args.uri);
            });

            //Create the menu
            menuManager = new Tuml.MenuManager();

            //Create the context manager
            leftMenuManager = new Tuml.LeftMenuManager();
            leftMenuManager.onMenuClick.subscribe(function (e, args) {
                //Do something like refresh the page
                self.refresh(args.uri);
            });
            leftMenuManager.onQueryClick.subscribe(function (e, args) {
                mainViewManager.addQueryTab(false, new Tuml.Query(args.id, args.name, args.name, args.queryString, args.queryEnum, null, args.queryType));
            });

            currentFocus = Tuml.FocusEnum.LEFT_MENU;

            //Create main view manager
            mainViewManager = new Tuml.TumlMainViewManager(this, leftMenuManager);

            window.onpopstate = function (event) {
                if (event.state !== null && document.location.hash === "") {
                    var pathname = removeUiFromUrl(document.location.pathname);
                    self.refresh(pathname, false);
                }
            };

            $(document).keydown(function (event) {
                if (!(event.which == 17 || event.which == 117 || event.which == 27 || event.which == 8 || event.which == 83 || event.which == 39)) {
                    return true;
                }
                if (event.ctrlKey && event.which == 83) {
                    //83 = s
                    self.saveViaKeyPress();
                    event.preventDefault();
                    event.stopImmediatePropagation();
                    return false;
                } else  if (event.which == 27) {
                    //27 = esc
                    self.cancelViaKeyPress();
                    event.preventDefault();
                    event.stopImmediatePropagation();
                    return false;
                } else  if (event.ctrlKey && event.which == 8) {
                    //8 = <- back
                    self.goBackOne();
                    event.preventDefault();
                    event.stopImmediatePropagation();
                } else  if (event.which == 117) {
                    //F6
                    self.moveFocus();
                    event.preventDefault();
                    event.stopImmediatePropagation();
                } else  if (event.which == 39) {
                    //right arrow
//                    alert('right arrow');
                } else {
                    return true;
                }
            });

            $(document).keydown(function (event) {
                if (!(event.which == 8)) {
                    return true;
                }
                if (event.ctrlKey && event.which == 8) {
                    alert('go back');
                } else {
                    return true;
                }
            });

            self.refresh(tumlUri);
        }

        this.goBackOne = function() {
            contextManager.goBackOne();
        }

        this.moveFocus = function () {
            if (currentFocus == Tuml.FocusEnum.LEFT_MENU) {
                currentFocus = Tuml.FocusEnum.CENTER_GRID;
                mainViewManager.setFocus(currentFocus);
            } else if (currentFocus == Tuml.FocusEnum.CENTER_GRID) {
                currentFocus = Tuml.FocusEnum.TOP_CONTEXT;
                contextManager.setFocus();
            } else if (currentFocus == Tuml.FocusEnum.TOP_CONTEXT) {
                currentFocus = Tuml.FocusEnum.LEFT_MENU;
                mainViewManager.setFocus(currentFocus);
            } else {
                currentFocus = Tuml.FocusEnum.LEFT_MENU;
                mainViewManager.setFocus(currentFocus);
            }
        }

        this.saveViaKeyPress = function () {
            mainViewManager.saveViaKeyPress();
        }

        this.cancelViaKeyPress = function () {
            mainViewManager.cancelViaKeyPress();
        }

        this.refresh = function (tumlUri, pushUrl) {
            //Change the browsers url
            if (pushUrl === undefined || pushUrl) {
                pushUrlToBrowser(tumlUri);
            }
            var contextVertexId = retrieveVertexId(tumlUri);
            if (contextVertexId !== null) {
                contextVertexId = decodeURIComponent(contextVertexId);
            }
            $.ajax({
                url: tumlUri,
                type: "GET",
                dataType: "json",
                contentType: "json",
                success: function (result) {
                    //This will first look in the cache for the meta data else it will get it from the server
                    //The meta data will be added to the result
                    retrieveMetaDataIfNotInCache(tumlUri, contextVertexId, result, continueRefresh);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('error getting ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                }
            });
        }

        function continueRefresh(tumlUri, result, contextVertexId) {

            //Need to validate here if we can continue.
            //Only valid request can continue else the ui gets awfully confused.

            //Non composite ones with no data can not be navigated to
            if (mainViewManager.refresh(tumlUri, result)) {
                var contextMetaData = getContextMetaData(result, contextVertexId);
                contextManager.refresh(contextMetaData.name, contextMetaData.uri, contextMetaData.contextVertexId);
            }
        }

        function getContextMetaData(result, urlId) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            var propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many
                return {name: metaDataNavigatingFrom.name, uri: metaDataNavigatingFrom.uri, contextVertexId: urlId};
            } else {
                //Property is a one
                //This is to check if there is data, if not it is a creation and the context remains that of the parent
                for (var i = 0; i < result.length; i++) {
                    var response = result[i];
                    //There are many data(s) to cater for the multiple concrete types, as its a one take the first one
                    if (response.data === undefined && response.data === null) {
                        alert('UIManager.getContextMetaData response.data !== undefined && response.data !== null');
                    }

                    if ((response.data.id !== undefined && response.data.id.indexOf('fake') === -1) || metaDataNavigatingTo.name === 'Root') {
                        metaDataNavigatingTo = result[i].meta.to;
                        return {name: metaDataNavigatingTo.name, uri: metaDataNavigatingTo.uri, contextVertexId: response.data.id};
                    }
                }
                return {name: metaDataNavigatingFrom.name, uri: metaDataNavigatingFrom.uri, contextVertexId: urlId};
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

        function pushUrlToBrowser(url) {
            history.pushState({}, "firefox ignores this", addUiToUrl(url));
        }

        //Public api
        $.extend(this, {
            "TumlUiManagerVersion": "1.0.0"
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

        this.getHandlers = function () {
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
                event: event,
                handler: handler
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
        "TumlUIVersion": "1.0.0",
        // Events
        "refreshUri": new Tuml.Event()
    });

})(jQuery);
//