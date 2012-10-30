(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            UiManager: UiManager,
            //Copied from Slick.Grid
            Event: Event,
            EventData: EventData,
            EventHandler: EventHandler
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
            var myLayout = $('body').layout({north__minSize: 70, west__minSize: 200});
            myLayout.allowOverflow("north");
            //Create the menu
            menuManager = new Tuml.MenuManager();

            //Create the context manager
            contextManager = new Tuml.ContextManager();
            contextManager.onClickContextMenu.subscribe(function(e, args) {
                console.log('TumlUiManager onClickContextMenu fired');
                refresh(args.uri);
                changeMyUrl(args.name, args.uri);
            });

            //Create the context manager
            leftMenuManager = new Tuml.LeftMenuManager();
            leftMenuManager.onMenuClick.subscribe(function(e, args) {
                //Do something like refresh the page
                console.log('looks like its happening');
                refresh(args.uri);
                changeMyUrl(args.name, args.uri);
            });

            //Create main view manager
            mainViewManager = new Tuml.MainViewManager(leftMenuManager);
            mainViewManager.onPutSuccess.subscribe(function(e, args) {
                console.log('TumlUiManager onPutSuccess fired');
                self.onPutSuccess.notify(args, e, self);
                //TODO activity log mayhap
            });
            mainViewManager.onPutFailure.subscribe(function(e, args) {
                console.log('TumlUiManager onPutFailure fired');
                self.onPutFailure.notify(args, e, self);
            });
            mainViewManager.onPostSuccess.subscribe(function(e, args) {
                console.log('TumlUiManager onPostSuccess fired');
                self.onPostSuccess.notify(args, e, self);
            });
            mainViewManager.onPostFailure.subscribe(function(e, args) {
                console.log('TumlUiManager onPostFailure fired');
                self.onPostFailure.notify(args, e, self);
            });
            mainViewManager.onDeleteSuccess.subscribe(function(e, args) {
                console.log('TumlUiManager onDeleteSuccess fired');
                self.onDeleteSuccess.notify(args, e, self);
            });
            mainViewManager.onDeleteFailure.subscribe(function(e, args) {
                console.log('TumlUiManager onDeleteFailure fired');
                self.onDeleteFailure.notify(args, e, self);
            });
            mainViewManager.onCancel.subscribe(function(e, args) {
                console.log('TumlUiManager onCancel fired');
                self.onCancel.notify(args, e, self);
            });
            mainViewManager.onContextMenuClickLink.subscribe(function(e, args) {
                console.log('TumlUiManager onContextMenuClickLink fired');
                self.onContextMenuClickLink.notify(args, e, self);
                refresh(args.tumlUri);
                changeMyUrl(args.name, args.tumlUri);
            });
            mainViewManager.onContextMenuClickDelete.subscribe(function(e, args) {
                console.log('TumlUiManager onContextMenuClickDelete fired');
                self.onContextMenuClickDelete.notify(args, e, self);
            });
            mainViewManager.onPutOneSuccess.subscribe(function(e, args) {
                console.log('TumlUiManager onPutOneSuccess fired');
                self.onPutOneSuccess.notify(args, e, self);
                refresh(args.uri);
                changeMyUrl(args.name, args.uri);
            });
            mainViewManager.onPutOneFailure.subscribe(function(e, args) {
                console.log('TumlUiManager onPutOneFailure fired');
                self.onPutOneFailure.notify(args, e, self);
            });

            window.onpopstate = function(event) {
                if (document.location.hash === "") {
                    var pathname = document.location.pathname.replace("/ui2", "");
                    refresh(pathname);
                }
            };

            refresh(tumlUri);

        }

        function refresh(tumlUri) {
            //Call the server for the tumlUri
            var contextVertexId = tumlUri.match(/\d+/);
            $.ajax({
                url: tumlUri,
                type: "GET",
                dataType: "json",
                contentType: "json",
                success: function(result, textStatus, jqXHR) {
                    var contextMetaData = getContextMetaData(result);
                    contextManager.refresh(contextMetaData, contextVertexId);
                    mainViewManager.refresh(tumlUri, result);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert('error getting ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                }
            });
        }

        function changeMyUrl(title, url) {
            var urlToPush = '/restAndJson/ui2' + url.substring('restAndJson/'.length);
            history.pushState({}, title, urlToPush);
        }

        function getContextMetaData(result) {
            //only using the context metatData here so no need to be in the look
            if (result instanceof Array && result[0].meta.length === 3) {   
                return result[0].meta[1];
            } else {
                //When navigating a property the result is an array, when accessing a entity by vertex its not, TODO refactor
                if (result instanceof Array) {
                    response = result[0];
                } else {
                    response = result;
                }
                return response.meta[1];
            }

        }

        //Public api
        $.extend(this, {
            "TumlUiManagerVersion": "1.0.0",
            //These events are propogated from the grid
            "onPutSuccess": new Tuml.Event(),
            "onPutFailure": new Tuml.Event(),
            "onPostSuccess": new Tuml.Event(),
            "onPostFailure": new Tuml.Event(),
            "onDeleteSuccess": new Tuml.Event(),
            "onDeleteFailure": new Tuml.Event(),
            "onCancel": new Tuml.Event(),
            "onContextMenuClickLink": new Tuml.Event(),
            "onContextMenuClickDelete": new Tuml.Event(),
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event()
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
