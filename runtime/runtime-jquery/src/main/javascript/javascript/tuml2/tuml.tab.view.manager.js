(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlBaseTabViewManager: TumlBaseTabViewManager,
            TumlTabOneViewManager: TumlTabOneViewManager,
            TumlTabManyComponentViewManager: TumlTabManyComponentViewManager,
            TumlTabManyViewManager: TumlTabManyViewManager,
            TumlTabQueryViewManager: TumlTabQueryViewManager
        }
    });

    function TumlBaseTabViewManager(tabEnum, tabContainer, tumlUri, result) {

        this.tabEnum = tabEnum;
        var tabId;
        var childTumlTabViewManagers = [];
        var tabContainer = tabContainer;

        function getTab(title) {
            return $('#tab-container').tabs('getTab', title);
        }

        function disableTab() {
            $('#tab-container').tabs('disableTab', this.tabTitleName);
        }

        function addToChildTabViewManager(tumlChildTabViewManager) {
            childTumlTabViewManagers.push(tumlChildTabViewManager);
        }

        function getChildTumlTabViewManagers() {
            return childTumlTabViewManagers;
        }

        if (this.result !== undefined) {
            this.init(tumlUri, result);
        }

        function recreateSubTabContainer() {
            if (subTabContainer !== undefined) {
                subTabContainer.remove();
            }
            var tabLayoutDiv = $('#tabs-layout');
            tabContainer = $('<div />', {id: 'tabs'}).appendTo(tabLayoutDiv);
            tabContainer.append('<ul />');
            tabContainer.tabs();
            tabContainer.find(".ui-tabs-nav").sortable({
                axis: "x",
                stop: function () {
                    tabContainer.tabs("refresh");
                }
            });
            tabContainer.tabs({
                activate: function (event, ui) {
                    var queryId = $.data(ui.newPanel[0], 'queryId');
                    var tabEnum = $.data(ui.newPanel[0], 'tabEnum');
                    leftMenuManager.refreshQueryMenuCss(queryId, tabEnum);
                }
            });
        }

        //Public api
        $.extend(this, {
            "TumlTabViewManagerVersion": "1.0.0",
            //These events are propogated from the grid
            "onAddButtonSuccess": new Tuml.Event(),
            "onSelectButtonSuccess": new Tuml.Event(),
            "onSelectCancelButtonSuccess": new Tuml.Event(),
            "onClickManyComponentCell": new Tuml.Event(),
            "onClickOneComponentCell": new Tuml.Event(),
            "onManyComponentCloseButtonSuccess": new Tuml.Event(),
            "onOneComponentSaveButtonSuccess": new Tuml.Event(),
            "onOneComponentCloseButtonSuccess": new Tuml.Event(),
            "onManyComponentSaveButtonSuccess": new Tuml.Event(),
            "onManyComponentCloseButtonSuccess": new Tuml.Event(),
            "onAddNewRow": new Tuml.Event(),
            "onAddRowSuccess": new Tuml.Event(),
            "onRemoveRowSuccess": new Tuml.Event(),
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
            "onPostOneSuccess": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "onDeleteOneSuccess": new Tuml.Event(),

            //Other events
//            "setParentTumlTabViewManager": setParentTumlTabViewManager,
//            "getParentTumlTabViewManager": getParentTumlTabViewManager,
            "addToChildTabViewManager": addToChildTabViewManager,
            "getChildTumlTabViewManagers": getChildTumlTabViewManagers,
            "disableTab": disableTab,
            "tabId": tabId
        });

    }

    TumlBaseTabViewManager.prototype.clear = function () {
        this.closeTab();
    }

    TumlBaseTabViewManager.prototype.closeTab = function () {
        $("#" + this.tabId).remove();
        this.li.remove();
        this.tabContainer.tabs("refresh");
    }

    TumlBaseTabViewManager.prototype.init = function (tumlUri, result) {
        this.qualifiedName = result.meta.qualifiedName;
        this.tabDivName = result.meta.to.name;
        TumlBaseTabViewManager.prototype.onCloseTab = new Tuml.Event();
    }

    TumlBaseTabViewManager.prototype.createTab = function () {
        var self = this;
        var tabTemplate = "<li id='li" + this.tabId + "'><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>";
        var label = this.tabTitleName, id = this.tabId;
        this.li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));

        // close icon: removing the tab on click
        this.li.find("span.ui-icon-close").click(function () {
            self.onCloseTab.notify(self.tabDivName);
        });

        this.tabContainer.find(".ui-tabs-nav").append(this.li);
        var divPanel = $('<div />', {id: this.tabId});
        this.tabContainer.append(divPanel);
        $.data(divPanel[0], 'tabEnum', this.tabEnum);
        this.tabContainer.tabs("refresh");
        return divPanel;
    }

    function TumlTabQueryViewManager(tabEnum, tabContainer, instanceQueryUri, classQueryUri, tabDivName, tabTitleName, queryId) {

        //Public api
        $.extend(this, {
            "TumlTabQueryViewManager": "1.0.0",
            "onPostInstanceQuerySuccess": new Tuml.Event(),
            "onPutInstanceQuerySuccess": new Tuml.Event(),
            "onPostClassQuerySuccess": new Tuml.Event(),
            "onPutClassQuerySuccess": new Tuml.Event(),
            "onDeleteQuerySuccess": new Tuml.Event()
        });

        var self = this;
        this.tabContainer = tabContainer;
        if (queryId !== undefined) {
            this.queryId = queryId;
        }

        this.tabDivName = tabDivName;
        this.tabTitleName = tabTitleName;
        this.tumlTabQueryManager = new Tuml.TumlTabQueryManager(instanceQueryUri, classQueryUri, this.queryId);
        this.tumlTabQueryManager.onPutInstanceQuerySuccess.subscribe(function (e, args) {
            self.onPutInstanceQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onPostInstanceQuerySuccess.subscribe(function (e, args) {
            self.onPostInstanceQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onPutClassQuerySuccess.subscribe(function (e, args) {
            self.onPutClassQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onPostClassQuerySuccess.subscribe(function (e, args) {
            self.onPostClassQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onDeleteQuerySuccess.subscribe(function (e, args) {
            self.onDeleteQuerySuccess.notify(args, e, self);
        });
        this.tumlTabQueryManager.onContextMenuClickLink.subscribe(function (e, args) {
            self.onContextMenuClickLink.notify(args, e, self);
        });
        this.tumlTabQueryManager.onSelfCellClick.subscribe(function (e, args) {
            self.onSelfCellClick.notify(args, e, self);
        });
        //Public api
        $.extend(this, {
            "TumlTabOneViewManager": "1.0.0",
            "onClickOneComponent": new Tuml.Event(),
            "onClickManyComponent": new Tuml.Event()
        });

        this.createQuery = function (oclExecuteUri, query, post) {
            this.tumlTabQueryManager.createQuery(self.tabDivName, oclExecuteUri, query, post);
        }

        TumlBaseTabViewManager.call(this, tabEnum, this.tabContainer);

    }

    TumlTabQueryViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabQueryViewManager.prototype.createTab = function () {
        this.tabId = this.tabDivName;
        var divPanel = TumlBaseTabViewManager.prototype.createTab.call(this);
        if (this.queryId !== undefined) {
            $.data(divPanel[0], 'queryId', this.queryId);
        }
    }

    function TumlTabOneViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result) {
        this.oneManyOrQuery = oneManyOrQuery;
        this.tabContainer = tabContainer;
        this.result = result;

        //Public api
        $.extend(this, {
            "TumlTabOneViewManager": "1.0.0",
            "onClickOneComponent": new Tuml.Event(),
            "onClickManyComponent": new Tuml.Event()
        });
        TumlBaseTabViewManager.call(this, tabEnum, this.tabContainer, tumlUri, result);
    }

    TumlTabOneViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabOneViewManager.prototype.createTab = function () {
        if (this.oneManyOrQuery.forOneComponent) {
            this.tabId = this.result.meta.to.name + "OneComponent";
            this.tabTitleName = this.result.meta.to.name + " One Add";
        } else {
            this.tabId = this.result.meta.to.name;
            this.tabTitleName = this.result.meta.to.name;
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
                self.onOneComponentSaveButtonSuccess.notify(args, e, self);
            });
            this.tumlTabOneManager.onOneComponentCloseButtonSuccess.subscribe(function (e, args) {
                self.onOneComponentCloseButtonSuccess.notify(args, e, self);
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
        this.tumlTabOneManager.onDeleteOneSuccess.subscribe(function (e, args) {
            self.onDeleteOneSuccess.notify(args, e, self);
        });
        this.tumlTabOneManager.onDeleteOneFailure.subscribe(function (e, args) {
            self.onDeleteOneFailure.notify(args, e, self);
        });
    }

    TumlTabOneViewManager.prototype.setValue = function (value) {
        this.tumlTabOneManager.setValue(this.property, value);
    }

    //Must be created after tabs have been created, else things look pretty bad like...
    TumlTabOneViewManager.prototype.createOne = function (result, metaForData, isForCreation) {
        this.tumlTabOneManager.refresh(result, metaForData, this.qualifiedName, isForCreation);
    }

    function TumlTabManyViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result) {
        var self = this;
        this.oneManyOrQuery = oneManyOrQuery;
        this.tabContainer = tabContainer;
        this.result = result;
        this.tumlTabGridManager = null;
        TumlBaseTabViewManager.call(this, tabEnum, this.tabContainer, tumlUri, result);
    }

    TumlTabManyViewManager.prototype = new Tuml.TumlBaseTabViewManager();
    TumlTabManyViewManager.prototype.constructor = Tuml.TumlBaseTabViewManager;

    TumlTabManyViewManager.prototype.createTab = function () {
        if (this.oneManyOrQuery.forLookup) {
            this.tabId = this.result.meta.to.name + "Lookup";
            this.tabTitleName = this.result.meta.to.name + " Select";
        } else if (this.oneManyOrQuery.forManyComponent) {
            this.tabId = this.result.meta.to.name + "ManyComponent";
            this.tabTitleName = this.result.meta.to.name + " Many Add";
        } else {
            this.tabId = this.result.meta.to.name;
            this.tabTitleName = this.result.meta.to.name;
        }
        TumlBaseTabViewManager.prototype.createTab.call(this);
    }

    TumlTabManyViewManager.prototype.setCell = function (cell) {
        this.cell = cell;
    }
    TumlTabManyViewManager.prototype.clear = function () {
        TumlBaseTabViewManager.prototype.clear.call(this);
        this.tumlTabGridManager = null;
    }
    TumlTabManyViewManager.prototype.init = function (tumlUri, tabDivName) {
        var self = this;
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
                self.onManyComponentSaveButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onManyComponentCloseButtonSuccess.subscribe(function (e, args) {
                self.onManyComponentCloseButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onManyComponentCancelButtonSuccess.subscribe(function (e, args) {
                //This is needed else the cell has a pointer to the wrong array
                self.getParentTumlTabViewManager().setValue(args.value);
            });
            this.tumlTabGridManager.onClickManyComponentCell.subscribe(function (e, args) {
                self.onClickManyComponentCell.notify(args, e, self);
            });
            this.tumlTabGridManager.onClickOneComponentCell.subscribe(function (e, args) {
                self.onClickOneComponentCell.notify(args, e, self);
            });
            this.tumlTabGridManager.onAddNewRow.subscribe(function (e, args) {
                //on a component's new row the value must be set on the parents compnent cell as the whole data set
                //will be posted with rollback=true to initialize the new row from the backend.
                self.getParentTumlTabViewManager().setValue(args);
                self.onAddNewRow.notify(args, e, self);
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
            this.tumlTabGridManager.onAddNewRow.subscribe(function (e, args) {
                self.onAddNewRow.notify(args, e, self);
            });
        }
        this.tumlTabGridManager.onAddRowSuccess.subscribe(function (e, args) {
            self.onAddRowSuccess.notify(args, e, self);
        });
        this.tumlTabGridManager.onRemoveRowSuccess.subscribe(function (e, args) {
            self.onRemoveRowSuccess.notify(args, e, self);
        });
        this.tumlTabGridManager.onPutSuccess.subscribe(function (e, args) {
            self.onPutSuccess.notify(args, e, self);
            self.updateGridForResult(args.data, args.tabId);
        });
        this.tumlTabGridManager.onPutFailure.subscribe(function (e, args) {
            self.onPutFailure.notify(args, e, self);
        });
        this.tumlTabGridManager.onPostSuccess.subscribe(function (e, args) {
            self.onPostSuccess.notify(args, e, self);
            self.updateGridForResult(args.data, args.tabId);
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
                this.createGrid(result[i]);
                return;
            }
        }
    }
    TumlTabManyViewManager.prototype.createGrid = function (result) {
        //Create an extra div, not displaying nice directly in the tab panel
        var gridDiv = $('<div />', {id: 'slickGrid' + this.tabId, class: 'slickGridOuter'});
        $('#' + this.tabId).append(gridDiv);
        this.tumlTabGridManager.refresh(result, gridDiv);
    }
    TumlTabManyViewManager.prototype.updateGridForResult = function (result, tabId) {
        for (var i = 0; i < result.length; i++) {
            var metaForData = result[i].meta.to;
            if (metaForData.name === tabId) {
                this.updateGrid(result[i]);
                return;
            }
        }
    }
    TumlTabManyViewManager.prototype.updateGrid = function (result) {
        this.tumlTabGridManager.update(result);
    }

    function TumlTabManyComponentViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result) {
        TumlTabManyViewManager.call(this, tabEnum, tabContainer, oneManyOrQuery, tumlUri, result);

        this.setParentTumlTabViewManager = function(tumlTabViewManager) {
            this.parentTumlTabViewManager = tumlTabViewManager;
        }

        this.getParentTumlTabViewManager = function() {
            return this.parentTumlTabViewManager;
        }

    }

    TumlTabManyComponentViewManager.prototype = new Tuml.TumlTabManyViewManager();
    TumlTabManyComponentViewManager.prototype.constructor = Tuml.TumlTabManyViewManager;

    TumlTabManyComponentViewManager.prototype.closeTab = function () {
        //Save the many component's data into the parent tabs row's cell
        if (this.tumlTabGridManager.grid.getEditorLock().commitCurrentEdit()) {
//            if (this.tumlTabGridManager.validateMultiplicity()) {
            this.parentTumlTabViewManager.setValue(this.tumlTabGridManager.dataView.getItems());

//                self.doSave();
//                        var validationResults = self.validateNewItems(self.dataView.getNewItems());
//                        if (validationResults.length == 0) {
//                            self.onManyComponentSaveButtonSuccess.notify({value:self.dataView.getItems(), tabName:self.metaForData.name}, null, self);
//                        } else {
//                            var errorMsg = '\n';
//                            for (var i = 0; i < validationResults.length; i++) {
//                                errorMsg += validationResults[i].msg + '\n';
//                            }
//                            alert('There are validation errors: ' + errorMsg);
//                        }
//            }
        }


        TumlBaseTabViewManager.prototype.closeTab.call(this);
    }

})(jQuery);
