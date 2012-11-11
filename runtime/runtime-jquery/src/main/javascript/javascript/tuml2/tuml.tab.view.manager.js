(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabViewManager: TumlTabViewManager
        }
    });

    function TumlTabViewManager(oneManyOrQuery, tumlUri, qualifiedName, tabDivName) {

        var self = this;
        var tumlTabGridManager;
        var tumlTabOneManager;
        var tumlTabQueryManager;

        function clear() {
            tumlTabGridManager = null;
            tumlTabOneManager = null;
            tumlTabQueryManager = null;
        }

        function init() {
            if ($('#' + tabDivName) == undefined) {
                alert('The tab must be created before calling the TumlTabViewManager.init()');
            }
            if (oneManyOrQuery.many) {
                //Do not pass the div in, it causes issues with refreshing
                tumlTabGridManager = new Tuml.TumlTabGridManager(tumlUri, oneManyOrQuery.propertyNavigatingTo);
                tumlTabGridManager.onPutSuccess.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPutSuccess fired');
                    self.onPutSuccess.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onPutFailure.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPutFailure fired: ' + args);
                    self.onPutFailure.notify(args, e, self);
                });
                tumlTabGridManager.onPostSuccess.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPostSuccess fired');
                    self.onPostSuccess.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onPostFailure.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPostFailure fired');
                    self.onPostFailure.notify(args, e, self);
                });
                tumlTabGridManager.onDeleteSuccess.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onDeleteSuccess fired');
                    self.onDeleteSuccess.notify(args, e, self);
                });
                tumlTabGridManager.onDeleteFailure.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onDeleteFailure fired');
                    self.onDeleteFailure.notify(args, e, self);
                });
                tumlTabGridManager.onCancel.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onCancel fired');
                    self.onCancel.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onSelfCellClick.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onSelfCellClick fired');
                    self.onSelfCellClick.notify(args, e, self);
                });
                tumlTabGridManager.onContextMenuClickLink.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onContextMenuClickLink fired');
                    self.onContextMenuClickLink.notify(args, e, self);
                });
                tumlTabGridManager.onContextMenuClickDelete.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onContextMenuClickDelete fired');
                    self.onContextMenuClickDelete.notify(args, e, self);
                });
            } else if (oneManyOrQuery.one) {
                tumlTabOneManager = new Tuml.TumlTabOneManager(tumlUri);
                tumlTabOneManager.onPutOneSuccess.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPutOneSuccess fired');
                    self.onPutOneSuccess.notify(args, e, self);
                });
                tumlTabOneManager.onPutOneFailure.subscribe(function(e, args) {
                    console.log('TumlTabViewManager onPutOneFailure fired: ' + args.textStatus + ' ' + args.errorThrown);
                    self.onPutOneFailure.notify(args, e, self);
                });
            } else if (oneManyOrQuery.query) {
                tumlTabQueryManager = new Tuml.TumlTabQueryManager(tumlUri, tabDivName);
            } else {
                alert('TumlTabViewManager.constructor, this should not happen!');
            }
        }

        function createGridForResult(result, tabId) {
            for (i = 0; i < result.length; i++) {
                var metaForData = result[i].meta.to;
                if (metaForData.name === tabId) {
                    $('#' + tabDivName).children().remove();
                    createGrid(result[i], false);
                    return;
                }
            }
        }

        //Grids must be created after tabs have been created, else things look pretty bad like...
        function createGrid(result) {
            tumlTabGridManager.refresh(result);
        }

        //Must be created after tabs have been created, else things look pretty bad like...
        function createOne(result, metaForData) {
            tumlTabOneManager.refresh(result, metaForData, qualifiedName);
        }

        //Must be created after tabs have been created, else things look pretty bad like...
        function createQuery(oclExecuteUri, queryEnum, queryString) {
            tumlTabQueryManager.createQuery(oclExecuteUri, queryEnum, queryString);
        }

        //Public api
        $.extend(this, {
            "TumlTabViewManagerVersion": "1.0.0",
            //These events are propogated from the grid
            "onPutSuccess": new Tuml.Event(),
            "onPutFailure": new Tuml.Event(),
            "onPostSuccess": new Tuml.Event(),
            "onPostFailure": new Tuml.Event(),
            "onDeleteSuccess": new Tuml.Event(),
            "onDeleteFailure": new Tuml.Event(),
            "onCancel": new Tuml.Event(),
            "onSelfCellClick": new Tuml.Event(),
            "onContextMenuClickLink": new Tuml.Event(),
            "onContextMenuClickDelete": new Tuml.Event(),
            //Events for one
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            //Other events
            "createOne": createOne,
            "createGrid": createGrid,
            "createQuery": createQuery,
            "tabDivName": tabDivName,
            "oneManyOrQuery": oneManyOrQuery,
            "clear": clear
        });

        init();
    }

})(jQuery);
