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

    function TumlBaseTabViewManager(tabEnum, tabContainer, tumlUri, result, propertyNavigatingTo) {

        this.tabEnum = tabEnum;

        function getTab(title) {
            return $('#tab-container').tabs('getTab', title);
        }

        this.disableTab = function () {
            $('#tab-container').tabs('disableTab', this.tabTitleName);
        }

        //Public api
        $.extend(this, {
            "TumlTabViewManagerVersion": "1.0.0",
            //These events are propogated from the grid
            "onAddButtonSuccess": new Tuml.Event(),
            "onSelectButtonSuccess": new Tuml.Event(),
            "onSelectCancelButtonSuccess": new Tuml.Event(),
            "onClickOneComponentCell": new Tuml.Event(),
            "onManyComponentCloseButtonSuccess": new Tuml.Event(),
            "onOneComponentSaveButtonSuccess": new Tuml.Event(),
            "onOneComponentCloseButtonSuccess": new Tuml.Event(),
            "onManyComponentSaveButtonSuccess": new Tuml.Event(),
            "onManyComponentCloseButtonSuccess": new Tuml.Event(),
            "onPutSuccess": new Tuml.Event(),
            "onPutFailure": new Tuml.Event(),
            "onPostSuccess": new Tuml.Event(),
            "onPostFailure": new Tuml.Event(),
            "onDeleteSuccess": new Tuml.Event(),
            "onDeleteFailure": new Tuml.Event(),
            "onCancel": new Tuml.Event(),
            "onSelfCellClick": new Tuml.Event(),
            "onContextMenuClickDelete": new Tuml.Event(),
            //Events for one
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "onPostOneSuccess": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "onDeleteOneSuccess": new Tuml.Event()

        });

        Tuml.TumlTabContainerManager.call(this, tabContainer, propertyNavigatingTo);

        if (this.result !== undefined) {
            this.init(tumlUri, result);
        }

    }

    TumlBaseTabViewManager.prototype = new Tuml.TumlTabContainerManager;

    TumlBaseTabViewManager.prototype.getTabId = function () {
        return this.tabId;
    }

    TumlBaseTabViewManager.prototype.closeTab = function () {
        this.clearAllTabs();
        $("#" + this.tabId).remove();
        this.li.remove();
        var indexOftab = this.parentTabContainerManager.tumlTabViewManagers.indexOf(this);
        this.parentTabContainerManager.tumlTabViewManagers.splice(indexOftab, 1);
    }

    TumlBaseTabViewManager.prototype.saveAndCloseTab = function () {
        this.saveAndClearAllTabs();
        this.parentTabContainerManager.addToCellValue(this.tumlTabGridManager.dataView.getItems());
        $("#" + this.tabId).remove();
        this.li.remove();
        var indexOftab = this.parentTabContainerManager.tumlTabViewManagers.indexOf(this);
        this.parentTabContainerManager.tumlTabViewManagers.splice(indexOftab, 1);
    }

    TumlBaseTabViewManager.prototype.init = function (tumlUri, result) {
        this.metaForData = result.meta;
        TumlBaseTabViewManager.prototype.onCloseTab = new Tuml.Event();
    }

    TumlBaseTabViewManager.prototype.createTab = function (result, forCreation) {
        var self = this;
        var tabTemplate;

        if (this.parentTabContainerManager instanceof Tuml.TumlMainViewManager) {
            tabTemplate = "<li id='li" + this.tabId + "'><a href='#{href}'>#{label}</a>";
        } else {
            tabTemplate = "<li id='li" + this.tabId + "'><a href='#{href}'>#{label}</a>" +
                "<span class='ui-icon ui-icon-cancel'>Cancel Tab</span>" +
                "<span class='ui-icon ui-icon-plus'>Save Tab</span>" +
                "<span class='ui-icon ui-icon-close'>Close Tab</span></li>";
        }
        var label = this.tabTitleName;
        var id = this.tabId;
        this.li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));

        // cancel icon: refreshing the data
        this.li.find("span.ui-icon-cancel").click(function () {
            if (Slick.GlobalEditorLock.cancelCurrentEdit()) {
                self.closeTab();
            }
        });
        // save icon: save and close the tab on click
        this.li.find("span.ui-icon-plus").click(function () {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                self.saveAndCloseTab();
            }
        });
        // close icon: removing the tab on click
        this.li.find("span.ui-icon-close").click(function () {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                self.closeTab();
            }
        });
        this.parentTabContainer.find(".ui-tabs-nav").append(this.li);
        var divPanel = $('<div />', {id: this.tabId});
        this.parentTabContainer.append(divPanel);
        $.data(divPanel[0], 'tabEnum', this.tabEnum);
        this.parentTabContainer.tabs("refresh");
        return divPanel;
    }

    TumlBaseTabViewManager.prototype.openManyComponent = function (data, cell, tumlUri, property) {

        var self = this;

        //Get the meta data.
        $.ajax({
            url: property.tumlMetaDataUri,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            success: function (result, textStatus, jqXHR) {

                var firstTumlManyComponentTabViewManager = null;
                for (var i = 0; i < result.length; i++) {
                    self.maybeCreateTabContainer();
                    result[i].data = data;
                    //This is wrong as it could be a one
                    var tumlManyComponentTabViewManager = self.addTab(
                        tuml.tab.Enum.Properties,
                        result[i],
                        tumlUri,
                        {forLookup: false, forManyComponent: true, forOneComponent: false, isOne: false, forCreation: true},
                        property
                    );
                    self.setCell(cell);
                    self.addToTumlTabViewManagers(tumlManyComponentTabViewManager);
                    self.tumlTabGridManager.active = false;
                    tumlManyComponentTabViewManager.parentTabContainerManager = self;
                    tumlManyComponentTabViewManager.createTab(result[i], false);
                    if (i === 0) {
                        firstTumlManyComponentTabViewManager = tumlManyComponentTabViewManager;
                    }
                }

                //Set only the first tab to active
                if (firstTumlManyComponentTabViewManager !== null) {
                    self.tabContainer.tabs("option", "active", 0);
                    if (tumlManyComponentTabViewManager instanceof Tuml.TumlTabManyComponentViewManager) {
                        tumlManyComponentTabViewManager.tumlTabGridManager.active = true;
                    }
                }


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
            }
        });
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
        if (queryId !== undefined) {
            this.queryId = queryId;
        }

        this.tabId = tabDivName;
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
        this.tumlTabQueryManager.onSelfCellClick.subscribe(function (e, args) {
            self.onSelfCellClick.notify(args, e, self);
        });
        //Public api
        $.extend(this, {
            "TumlTabOneViewManager": "1.0.0",
            "onClickOneComponent": new Tuml.Event()
        });

        this.createQuery = function (oclExecuteUri, query, post) {
            this.tumlTabQueryManager.createQuery(this.tabId, oclExecuteUri, query, post);
        }

        TumlBaseTabViewManager.call(this, tabEnum, tabContainer);

    }

    TumlTabQueryViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabQueryViewManager.prototype.createTab = function (result, forCreation) {
        var divPanel = TumlBaseTabViewManager.prototype.createTab.call(this, result, forCreation);
        if (this.queryId !== undefined) {
            $.data(divPanel[0], 'queryId', this.queryId);
        }
//        this.parentTabContainer.tabs("option", "active", this.parentTabContainerManager.tumlTabViewManagers.length - 1);
    }

    function TumlTabOneViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result, propertyNavigatingTo) {
        this.oneManyOrQuery = oneManyOrQuery;
        this.result = result;

        //Public api
        $.extend(this, {
            "TumlTabOneViewManager": "1.0.0",
            "onClickOneComponent": new Tuml.Event()
        });
        TumlBaseTabViewManager.call(this, tabEnum, tabContainer, tumlUri, result, propertyNavigatingTo);
    }

    TumlTabOneViewManager.prototype = new Tuml.TumlBaseTabViewManager;

    TumlTabOneViewManager.prototype.createTab = function (result, forCreation) {
        if (this.oneManyOrQuery.forOneComponent) {
            this.tabId = this.result.meta.to.name + "OneComponent";
            this.tabTitleName = this.result.meta.to.name + " One Add";
        } else {
            this.tabId = this.result.meta.to.name;
            this.tabTitleName = this.result.meta.to.name;
        }
        TumlBaseTabViewManager.prototype.createTab.call(this, result, forCreation);
        this.parentTabContainer.tabs("option", "active", this.parentTabContainerManager.tumlTabViewManagers.length - 1);
        this.createOne(result.data[0]);
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

    TumlTabOneViewManager.prototype.addToCellValue = function (value) {
        this.tumlTabOneManager.addToCellValue(this.property, value);
    }

    //Must be created after tabs have been created, else things look pretty bad like...
    TumlTabOneViewManager.prototype.createOne = function (result) {
        this.tumlTabOneManager.refresh(result, this.metaForData, this.metaForData.qualifiedName, false);
    }

    function TumlTabManyViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result, propertyNavigatingTo) {
        this.oneManyOrQuery = oneManyOrQuery;
        this.result = result;
        this.tumlTabGridManager = null;
        TumlBaseTabViewManager.call(this, tabEnum, tabContainer, tumlUri, result, propertyNavigatingTo);
    }

    TumlTabManyViewManager.prototype = new Tuml.TumlBaseTabViewManager();

    TumlTabManyViewManager.prototype.updateGridAfterCommit = function (data) {
        this.tumlTabGridManager.updateGridAfterCommit(data);

        //Check if component tab is open
        if (this.tumlTabViewManagers.length > 0) {

            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {

                var tumlTabViewManager = this.tumlTabViewManagers[i];
                var rowClickedOnData = this.tumlTabGridManager.dataView.getItems()[this.componentCell.row];
                var componentData = rowClickedOnData[tumlTabViewManager.propertyNavigatingTo.name];
                tumlTabViewManager.updateGridAfterCommit(componentData);

            }

        }

    }

    TumlTabManyViewManager.prototype.deactivateGrids = function() {
        this.tumlTabGridManager.active = false;
        Tuml.TumlTabContainerManager.prototype.deactivateGrids.call(this);
    }

    TumlTabManyViewManager.prototype.updateGridAfterRollback = function (item) {

        this.tumlTabGridManager.updateGridAfterRollback(item);

        //Check if component tab is open
        if (this.tumlTabViewManagers.length > 0) {

            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {

                var tumlTabViewManager = this.tumlTabViewManagers[i];
                var rowClickedOnData = this.tumlTabGridManager.dataView.getItems()[this.componentCell.row];
                var componentData = rowClickedOnData[tumlTabViewManager.propertyNavigatingTo.name];

                for (var j = 0; j < componentData.length; j++) {
                    var gridRow = componentData[j];
                    if (tumlTabViewManager.metaForData.to.qualifiedName === gridRow.qualifiedName) {
                        tumlTabViewManager.updateGridAfterRollback(gridRow);
                    }
                }

            }

        }

    }

    TumlTabManyViewManager.prototype.addNewRow = function (event) {
        this.parentTabContainerManager.updateValidationWarningHeader();
        //Save the child grids into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
            var data = [];
            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = this.tumlTabViewManagers[i];
                if (tumlTabViewManager.tumlTabGridManager.dataView.getItems().length > 0) {
                    data.push.apply(data, tumlTabViewManager.tumlTabGridManager.dataView.getItems());
                }
            }
            this.setCellValue(data);
        }
        this.parentTabContainerManager.addNewRow(event);
    }

    TumlTabManyViewManager.prototype.createTab = function (result, forCreation) {
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
        TumlBaseTabViewManager.prototype.createTab.call(this, result, forCreation);
        this.parentTabContainer.tabs("option", "active", this.parentTabContainerManager.tumlTabViewManagers.length - 1);
        this.createGrid(result);
    }

    TumlTabManyViewManager.prototype.setCell = function (cell) {
        this.componentCell = cell;
    }
    TumlTabManyViewManager.prototype.init = function (tumlUri, result) {
        var self = this;
        TumlBaseTabViewManager.prototype.init.call(this, tumlUri, result);
        if (this.oneManyOrQuery.forLookup) {
            this.tumlTabGridManager = new Tuml.TumlForManyLookupGridManager(this, tumlUri, this.propertyNavigatingTo);
            this.tumlTabGridManager.onSelectButtonSuccess.subscribe(function (e, args) {
                self.onSelectButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                self.onSelectCancelButtonSuccess.notify(args, e, self);
            });
        } else if (this.oneManyOrQuery.forManyComponent) {
            this.tumlTabGridManager = new Tuml.TumlManyComponentGridManager(this, tumlUri, this.propertyNavigatingTo);
            this.tumlTabGridManager.onManyComponentSaveButtonSuccess.subscribe(function (e, args) {
                self.onManyComponentSaveButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onManyComponentCloseButtonSuccess.subscribe(function (e, args) {
                self.onManyComponentCloseButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onManyComponentCancelButtonSuccess.subscribe(function (e, args) {
                //This is needed else the cell has a pointer to the wrong array
                self.getParentTumlTabViewManager().addToCellValue(args.value);
            });
            this.tumlTabGridManager.onClickOneComponentCell.subscribe(function (e, args) {
                self.onClickOneComponentCell.notify(args, e, self);
            });
        } else {
            this.tumlTabGridManager = new Tuml.TumlTabGridManager(this, tumlUri, this.propertyNavigatingTo);
            this.tumlTabGridManager.onAddButtonSuccess.subscribe(function (e, args) {
                self.onAddButtonSuccess.notify(args, e, self);
            });
            this.tumlTabGridManager.onClickOneComponentCell.subscribe(function (e, args) {
                self.onClickOneComponentCell.notify(args, e, self);
            });
        }
        this.tumlTabGridManager.onSelfCellClick.subscribe(function (e, args) {
            self.onSelfCellClick.notify(args, e, self);
        });
        this.tumlTabGridManager.onContextMenuClickDelete.subscribe(function (e, args) {
            self.onContextMenuClickDelete.notify(args, e, self);
        });
    }

    TumlTabManyViewManager.prototype.addItems = function (items) {
        this.tumlTabGridManager.addItems(items);
    }

    TumlTabManyViewManager.prototype.setCellValue = function (value) {
        this.tumlTabGridManager.setCellValue(this.componentCell, value);
    }

    TumlTabManyViewManager.prototype.createGrid = function (result) {
        //Create an extra div, not displaying nice directly in the tab panel
        var gridDiv = $('<div />', {id: 'slickGrid' + this.tabId, class: 'slickGridOuter'});
        $('#' + this.tabId).append(gridDiv);
        this.tumlTabGridManager.refresh(result, gridDiv);
    }

    function TumlTabManyComponentViewManager(tabEnum, tabContainer, oneManyOrQuery, tumlUri, result, propertyNavigatingTo) {
        TumlTabManyViewManager.call(this, tabEnum, tabContainer, oneManyOrQuery, tumlUri, result, propertyNavigatingTo);
    }

    TumlTabManyComponentViewManager.prototype = new Tuml.TumlTabManyViewManager();

    TumlTabManyComponentViewManager.prototype.createTab = function (result, forCreation) {
        if (this.oneManyOrQuery.forManyComponent) {
            this.tabId = this.result.meta.to.name + "ManyComponent";
            this.tabTitleName = this.result.meta.to.name + " Many Add";
        } else {
            alert('this should not happen!');
        }
        $('#slickGrid' + this.parentTabContainerManager.tabId).hide();
        TumlBaseTabViewManager.prototype.createTab.call(this, result, forCreation);
        this.parentTabContainer.tabs("option", "active", this.parentTabContainerManager.tumlTabViewManagers.length - 1);
        this.createGrid(result);
    }

    TumlTabManyComponentViewManager.prototype.closeTab = function () {
        //Save the many component's data into the parent tabs row's cell
        Slick.GlobalEditorLock.cancelCurrentEdit();
        TumlBaseTabViewManager.prototype.closeTab.call(this);
    }

})
    (jQuery);
