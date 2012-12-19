(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            TumlBaseTabViewManager:TumlBaseTabViewManager,
            TumlTabOneViewManager:TumlTabOneViewManager,
            TumlTabManyViewManager:TumlTabManyViewManager,
            TumlTabQueryViewManager:TumlTabQueryViewManager
        }
    });

    function TumlTabQueryViewManager(tumlUri, tabDivName, tabTitleName) {

        //Public api
        $.extend(this, {
            "TumlTabQueryViewManager":"1.0.0",
            "onPostQuerySuccess":new Tuml.Event(),
            "onPutQuerySuccess":new Tuml.Event(),
            "onDeleteQuerySuccess":new Tuml.Event()
        });

        var self = this;
        this.tabDivName = tabDivName;
        this.tabTitleName = tabTitleName;
        this.tumlTabQueryManager = new Tuml.TumlTabQueryManager(tumlUri);
        this.tumlTabQueryManager.onPutQuerySuccess.subscribe(function (e, args) {
            self.closeTab();
            self.tabTitleName = args.queryName;
            self.tabDivName = args.queryName.replace(/\s/g, '');
            self.createTab();
            self.createQuery(false, args.oclExecuteUri, args.queryName, args.queryEnum, args.queryString);
            self.onPutQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onPostQuerySuccess.subscribe(function (e, args) {
            self.closeTab();
            self.tabTitleName = args.queryName;
            self.tabDivName = args.queryName.replace(/\s/g, '');
            self.createTab();
            self.createQuery(false, args.oclExecuteUri, args.queryName, args.queryEnum, args.queryString);
            self.onPostQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onDeleteQuerySuccess.subscribe(function (e, args) {
            self.closeTab();
//            self.onPostQuerySuccess.notify(args, e, self);
        });
        //Public api
        $.extend(this, {
            "TumlTabOneViewManager":"1.0.0",
            "onClickOneComponent":new Tuml.Event(),
            "onClickManyComponent":new Tuml.Event()
        });

        this.createQuery = function (post, oclExecuteUri, queryName, queryEnum, queryString) {
            this.tumlTabQueryManager.createQuery(post, self.tabDivName, oclExecuteUri, queryName, queryEnum, queryString);
        }

    }

    TumlTabQueryViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabQueryViewManager.prototype.createTab = function () {
        this.tabId = this.tabDivName;
        this.tabTitle = this.tabTitleName;
        TumlBaseTabViewManager.prototype.createTab.call(this);
    }

    function TumlTabOneViewManager(oneManyOrQuery, tumlUri, result) {
        var self = this;
        //Public api
        $.extend(this, {
            "TumlTabOneViewManager":"1.0.0",
            "onClickOneComponent":new Tuml.Event(),
            "onClickManyComponent":new Tuml.Event()
        });
        TumlBaseTabViewManager.call(this, oneManyOrQuery, tumlUri, result);
    }

    TumlTabOneViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabOneViewManager.prototype.createTab = function () {
        if (this.oneManyOrQuery.forOneComponent) {
            this.tabId = this.result.meta.to.name + "OneComponent";
            this.tabTitle = this.result.meta.to.name + " One Add";
        } else {
            this.tabId = this.result.meta.to.name;
            this.tabTitle = this.result.meta.to.name;
        }
        TumlBaseTabViewManager.prototype.createTab.call(this);
    }

    TumlTabOneViewManager.prototype.clear = function () {
        this.tumlTabOneManager = null;
        TumlBaseTabViewManager.prototype.clear.call(this);
    }

    TumlTabOneViewManager.prototype.setProperty = function (property) {
        this.property = property;
    }

    TumlTabOneViewManager.prototype.init = function (tumlUri, result) {
        var self = this;
        TumlBaseTabViewManager.prototype.init.call(this, tumlUri, result);
        if (this.oneManyOrQuery.forOneComponent) {
            this.tumlTabOneManager = new Tuml.TumlTabComponentOneManager(tumlUri);
            this.tumlTabOneManager.onOneComponentSaveButtonSuccess.subscribe(function (e, args) {
                self.getLinkedTumlTabViewManager().setValue(args.value);
                //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                $('#tab-container').tabs('close', args.tabName + " One Add");
                $('#' + args.tabName + "OneComponent").remove();
                self.getLinkedTumlTabViewManager().enableTab();
            });
            this.tumlTabOneManager.onOneComponentCloseButtonSuccess.subscribe(function (e, args) {
                //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                $('#tab-container').tabs('close', args.tabName + " One Add");
                $('#' + args.tabName + "OneComponent").remove();
                self.getLinkedTumlTabViewManager().enableTab();
            });
        } else {
            this.tumlTabOneManager = new Tuml.TumlTabOneManager(tumlUri);
        }
        this.tumlTabOneManager.onClickOneComponent.subscribe(function (e, args) {
            self.onClickOneComponent.notify(args, e, self);
        });
        this.tumlTabOneManager.onClickManyComponent.subscribe(function (e, args) {
            self.onClickManyComponent.notify(args, e, self);
        });
        this.tumlTabOneManager.onPutOneSuccess.subscribe(function (e, args) {
            self.onPutOneSuccess.notify(args, e, self);
        });
        this.tumlTabOneManager.onPutOneFailure.subscribe(function (e, args) {
            self.onPutOneFailure.notify(args, e, self);
        });
        this.tumlTabOneManager.onPostOneSuccess.subscribe(function (e, args) {
            self.onPostOneSuccess.notify(args, e, self);
        });
        this.tumlTabOneManager.onPostOneFailure.subscribe(function (e, args) {
            self.onPostOneFailure.notify(args, e, self);
        });
    }

    TumlTabOneViewManager.prototype.setValue = function (value) {
        this.tumlTabOneManager.setValue(this.property, value);
    }

    //Must be created after tabs have been created, else things look pretty bad like...
    TumlTabOneViewManager.prototype.createOne = function (result, metaForData, isForCreation) {
        this.tumlTabOneManager.refresh(result, metaForData, this.qualifiedName, isForCreation);
    }

    function TumlTabManyViewManager(oneManyOrQuery, tumlUri, result) {
        var self = this;
        //Public api
        $.extend(this, {
            "TumlTabManyViewManager":"1.0.0"
        });
        this.tumlTabGridManager;
        TumlBaseTabViewManager.call(this, oneManyOrQuery, tumlUri, result);
    }

    TumlTabManyViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabManyViewManager.prototype.createTab = function () {
        if (this.oneManyOrQuery.forLookup) {
            this.tabId = this.result.meta.to.name + "Lookup";
            this.tabTitle = this.result.meta.to.name + " Select";
        } else if (this.oneManyOrQuery.forManyComponent) {
            this.tabId = this.result.meta.to.name + "ManyComponent";
            this.tabTitle = this.result.meta.to.name + " Many Add";
        } else {
            this.tabId = this.result.meta.to.name;
            this.tabTitle = this.result.meta.to.name;
        }
        TumlBaseTabViewManager.prototype.createTab.call(this);
    }

    TumlTabManyViewManager.prototype.setCell = function (cell) {
        this.cell = cell;
    }
    TumlTabManyViewManager.prototype.clear = function () {
        this.tumlTabGridManager = null;
        TumlBaseTabViewManager.prototype.clear.call(this);
    }
    TumlTabManyViewManager.prototype.init = function (tumlUri, tabDivName) {
        var self = this;
        //Do not pass the div in, it causes issues with refreshing
        TumlBaseTabViewManager.prototype.init.call(this, tumlUri, tabDivName);
        if (this.oneManyOrQuery.forLookup) {
            this.tumlTabGridManager = new Tuml.TumlForManyLookupGridManager(tumlUri, this.oneManyOrQuery.propertyNavigatingTo);
            this.tumlTabGridManager.onSelectButtonSuccess.subscribe(function (e, args) {
                self.onSelectButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                self.onSelectCancelButtonSuccess.notify(args, e, self);
            });
        } else if (this.oneManyOrQuery.forManyComponent) {
            this.tumlTabGridManager = new Tuml.TumlManyComponentGridManager(tumlUri, this.oneManyOrQuery.propertyNavigatingTo);
            this.tumlTabGridManager.onManyComponentSaveButtonSuccess.subscribe(function (e, args) {
                self.getLinkedTumlTabViewManager().setValue(args.value);
                //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                $('#tab-container').tabs('close', args.tabName + " Many Add");
                $('#' + args.tabName + "ManyComponent").remove();
                self.getLinkedTumlTabViewManager().enableTab();
            });
            this.tumlTabGridManager.onManyComponentCancelButtonSuccess.subscribe(function (e, args) {
                //This is needed else the cell has a pointer to the wrong array
                self.getLinkedTumlTabViewManager().setValue(args.value);
            });
            this.tumlTabGridManager.onManyComponentCloseButtonSuccess.subscribe(function (e, args) {
                //This is needed else the cell has a pointer to the wrong array
                self.getLinkedTumlTabViewManager().setValue(args.value);
                $('#tab-container').tabs('close', args.tabName + " Many Add");
                $('#' + args.tabName + "ManyComponent").remove();
                self.getLinkedTumlTabViewManager().enableTab();
            });
            this.tumlTabGridManager.onClickManyComponentCell.subscribe(function (e, args) {
                self.onClickManyComponentCell.notify(args, e, self);
            });
            this.tumlTabGridManager.onClickOneComponentCell.subscribe(function (e, args) {
                self.onClickOneComponentCell.notify(args, e, self);
            });
        } else {
            this.tumlTabGridManager = new Tuml.TumlTabGridManager(tumlUri, this.oneManyOrQuery.propertyNavigatingTo);
            this.tumlTabGridManager.onAddButtonSuccess.subscribe(function (e, args) {
                self.onAddButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onClickManyComponentCell.subscribe(function (e, args) {
                self.onClickManyComponentCell.notify(args, e, self);
            });
            this.tumlTabGridManager.onClickOneComponentCell.subscribe(function (e, args) {
                self.onClickOneComponentCell.notify(args, e, self);
            });
        }
        this.tumlTabGridManager.onPutSuccess.subscribe(function (e, args) {
            self.onPutSuccess.notify(args, e, self);
            self.createGridForResult(args.data, args.tabId);
        });
        this.tumlTabGridManager.onPutFailure.subscribe(function (e, args) {
            self.onPutFailure.notify(args, e, self);
        });
        this.tumlTabGridManager.onPostSuccess.subscribe(function (e, args) {
            self.onPostSuccess.notify(args, e, self);
            self.createGridForResult(args.data, args.tabId);
        });
        this.tumlTabGridManager.onPostFailure.subscribe(function (e, args) {
            self.onPostFailure.notify(args, e, self);
        });
        this.tumlTabGridManager.onDeleteSuccess.subscribe(function (e, args) {
            self.onDeleteSuccess.notify(args, e, self);
            self.createGridForResult(args.data, args.tabId);
        });
        this.tumlTabGridManager.onDeleteFailure.subscribe(function (e, args) {
            self.onDeleteFailure.notify(args, e, self);
        });
        this.tumlTabGridManager.onCancel.subscribe(function (e, args) {
            self.onCancel.notify(args, e, self);
            self.createGridForResult(args.data, args.tabId);
        });
        this.tumlTabGridManager.onSelfCellClick.subscribe(function (e, args) {
            self.onSelfCellClick.notify(args, e, self);
        });
        this.tumlTabGridManager.onContextMenuClickLink.subscribe(function (e, args) {
            self.onContextMenuClickLink.notify(args, e, self);
        });
        this.tumlTabGridManager.onContextMenuClickDelete.subscribe(function (e, args) {
            self.onContextMenuClickDelete.notify(args, e, self);
        });
    }
    TumlTabManyViewManager.prototype.addItems = function (items) {
        this.tumlTabGridManager.addItems(items);
    }
    TumlTabManyViewManager.prototype.setValue = function (value) {
        this.tumlTabGridManager.setCellValue(this.cell, value);
    }
    TumlTabManyViewManager.prototype.createGridForResult = function (result, tabId) {
        for (i = 0; i < result.length; i++) {
            var metaForData = result[i].meta.to;
            if (metaForData.name === tabId) {
                $('#' + this.tabDivName).children().remove();
                this.createGrid(result[i]);
                return;
            }
        }
    }
    TumlTabManyViewManager.prototype.createGrid = function (result) {
        this.tumlTabGridManager.refresh(result);
    }

    function TumlBaseTabViewManager(oneManyOrQuery, tumlUri, result) {

        var self = this;
        this.linkedTumlTabViewManager;
        var tabTitle;
        var tabId;
        this.oneManyOrQuery = oneManyOrQuery;
        this.result = result;

        function selectTab() {
            $('#tab-container').tabs('select', this.tabTitle);
        }

        function enableTab() {
            $('#tab-container').tabs('enableTab', this.tabTitle);
        }

        function getTab(title) {
            return $('#tab-container').tabs('getTab', title);
        }

        function disableTab() {
            $('#tab-container').tabs('disableTab', this.tabTitle);
        }

        function closeTab() {
            $('#tab-container').tabs('close', this.tabTitle);
        }

        function setLinkedTumlTabViewManager(tumlTabViewManager) {
            this.linkedTumlTabViewManager = tumlTabViewManager;
        }

        function getLinkedTumlTabViewManager() {
            return this.linkedTumlTabViewManager;
        }

        if (tumlUri !== undefined) {
            this.init(tumlUri, result);
        }

        //Public api
        $.extend(this, {
            "TumlTabViewManagerVersion":"1.0.0",
            //These events are propogated from the grid
            "onAddButtonSuccess":new Tuml.Event(),
            "onSelectButtonSuccess":new Tuml.Event(),
            "onSelectCancelButtonSuccess":new Tuml.Event(),
            "onClickManyComponentCell":new Tuml.Event(),
            "onClickOneComponentCell":new Tuml.Event(),
            "onManyComponentCloseButtonSuccess":new Tuml.Event(),
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
            "setLinkedTumlTabViewManager":setLinkedTumlTabViewManager,
            "getLinkedTumlTabViewManager":getLinkedTumlTabViewManager,
            "enableTab":enableTab,
            "disableTab":disableTab,
            "closeTab":closeTab,
            "tabId":tabId,
            "tabTitle":tabTitle,
            "selectTab":selectTab
        });

    }

    TumlBaseTabViewManager.prototype.clear = function () {
        tumlTabQueryManager = null;
    }

    TumlBaseTabViewManager.prototype.init = function (tumlUri, result) {
        this.qualifiedName = result.meta.qualifiedName;
        this.tabDivName = result.meta.to.name;
    }

    TumlBaseTabViewManager.prototype.createTab = function () {
        var tabContainer = $('#tab-container');
        $('#tab-container').tabs('add', {title:this.tabTitle, content:'<div id="' + this.tabId + '" />', closable:true});
    }

})(jQuery);
