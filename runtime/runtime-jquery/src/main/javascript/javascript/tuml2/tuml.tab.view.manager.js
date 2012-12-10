(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            TumlTabViewManager:TumlTabViewManager
        }
    });

    function TumlTabViewManager(oneManyOrQuery, tumlUri, qualifiedName, tabDivName) {

        var self = this;
        var tumlTabGridManager;
        var tumlTabOneManager;
        var tumlTabQueryManager;
        var linkedTumlTabViewManager;
        var tabTitle;
        var tabId;

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
                if (oneManyOrQuery.forLookup) {
                    tumlTabGridManager = new Tuml.TumlForManyLookupGridManager(tumlUri, oneManyOrQuery.propertyNavigatingTo);
                    tumlTabGridManager.onSelectButtonSuccess.subscribe(function (e, args) {
                        self.onSelectButtonSuccess.notify(args, e, self);
                    });
                    tumlTabGridManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                        self.onSelectCancelButtonSuccess.notify(args, e, self);
                    });
                } else if (oneManyOrQuery.forManyComponent) {
                    tumlTabGridManager = new Tuml.TumlManyComponentGridManager(tumlUri, oneManyOrQuery.propertyNavigatingTo);
                    tumlTabGridManager.onManyComponentAddButtonSuccess.subscribe(function (e, args) {
                        self.onManyComponentAddButtonSuccess.notify(args, e, self);
                    });
                    tumlTabGridManager.onManyComponentCancelButtonSuccess.subscribe(function (e, args) {
                        self.onManyComponentCancelButtonSuccess.notify(args, e, self);
                    });
                    tumlTabGridManager.onClickManyComponentCell.subscribe(function (e, args) {
                        self.onClickManyComponentCell.notify(args, e, self);
                    });
                } else {
                    tumlTabGridManager = new Tuml.TumlTabGridManager(tumlUri, oneManyOrQuery.propertyNavigatingTo);
                    tumlTabGridManager.onAddButtonSuccess.subscribe(function (e, args) {
                        self.onAddButtonSuccess.notify(args, e, self);
                    });
                    tumlTabGridManager.onClickManyComponentCell.subscribe(function (e, args) {
                        self.onClickManyComponentCell.notify(args, e, self);
                    });
                }
                tumlTabGridManager.onPutSuccess.subscribe(function (e, args) {
                    self.onPutSuccess.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onPutFailure.subscribe(function (e, args) {
                    self.onPutFailure.notify(args, e, self);
                });
                tumlTabGridManager.onPostSuccess.subscribe(function (e, args) {
                    self.onPostSuccess.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onPostFailure.subscribe(function (e, args) {
                    self.onPostFailure.notify(args, e, self);
                });
                tumlTabGridManager.onDeleteSuccess.subscribe(function (e, args) {
                    self.onDeleteSuccess.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onDeleteFailure.subscribe(function (e, args) {
                    self.onDeleteFailure.notify(args, e, self);
                });
                tumlTabGridManager.onCancel.subscribe(function (e, args) {
                    self.onCancel.notify(args, e, self);
                    createGridForResult(args.data, args.tabId);
                });
                tumlTabGridManager.onSelfCellClick.subscribe(function (e, args) {
                    self.onSelfCellClick.notify(args, e, self);
                });
                tumlTabGridManager.onContextMenuClickLink.subscribe(function (e, args) {
                    self.onContextMenuClickLink.notify(args, e, self);
                });
                tumlTabGridManager.onContextMenuClickDelete.subscribe(function (e, args) {
                    self.onContextMenuClickDelete.notify(args, e, self);
                });
            } else if (oneManyOrQuery.one) {
                tumlTabOneManager = new Tuml.TumlTabOneManager(tumlUri);
                tumlTabOneManager.onPutOneSuccess.subscribe(function (e, args) {
                    self.onPutOneSuccess.notify(args, e, self);
                });
                tumlTabOneManager.onPutOneFailure.subscribe(function (e, args) {
                    self.onPutOneFailure.notify(args, e, self);
                });
                tumlTabOneManager.onPostOneSuccess.subscribe(function (e, args) {
                    self.onPostOneSuccess.notify(args, e, self);
                });
                tumlTabOneManager.onPostOneFailure.subscribe(function (e, args) {
                    self.onPostOneFailure.notify(args, e, self);
                });
            } else if (oneManyOrQuery.query) {
                tumlTabQueryManager = new Tuml.TumlTabQueryManager(tumlUri, tabDivName);
            } else {
                alert('TumlTabViewManager.constructor, this should not happen!');
            }
        }

        function addItems(items) {
            tumlTabGridManager.addItems(items);
        }

        function setCellValue(cell, value) {
            tumlTabGridManager.setCellValue(cell, value);
        }

        function createGridForResult(result, tabId) {
            for (i = 0; i < result.length; i++) {
                var metaForData = result[i].meta.to;
                if (metaForData.name === tabId) {
                    $('#' + tabDivName).children().remove();
                    createGrid(result[i]);
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

        function setLinkedTumlTabViewManager(tumlTabViewManager) {
            linkedTumlTabViewManager = tumlTabViewManager;
        }

        function getLinkedTumlTabViewManager() {
            return linkedTumlTabViewManager;
        }

        //Public api
        $.extend(this, {
            "TumlTabViewManagerVersion":"1.0.0",
            //These events are propogated from the grid
            "onAddButtonSuccess":new Tuml.Event(),
            "onSelectButtonSuccess":new Tuml.Event(),
            "onSelectCancelButtonSuccess":new Tuml.Event(),
            "onClickManyComponentCell":new Tuml.Event(),
            "onManyComponentAddButtonSuccess":new Tuml.Event(),
            "onManyComponentCancelButtonSuccess":new Tuml.Event(),
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
            //Events for one
            "onPutOneSuccess":new Tuml.Event(),
            "onPutOneFailure":new Tuml.Event(),
            "onPostOneSuccess":new Tuml.Event(),
            "onPostOneFailure":new Tuml.Event(),
            //Other events
            "createOne":createOne,
            "createGrid":createGrid,
            "createQuery":createQuery,
            "clear":clear,
            "setLinkedTumlTabViewManager":setLinkedTumlTabViewManager,
            "getLinkedTumlTabViewManager":getLinkedTumlTabViewManager,
            "addItems":addItems,
            "setCellValue": setCellValue,
            "tabId":tabId,
            "tabTitle":tabTitle
        });

        init();
    }

})(jQuery);
