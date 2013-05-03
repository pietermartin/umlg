(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabContainerManager: TumlTabContainerManager
        }
    });

    function TumlTabContainerManager(tabContainer, propertyNavigatingTo) {

        this.TumlTabContainerManager = "1.0.0";
        var self = this;

        //This will be null for the root object only
        this.propertyNavigatingTo = propertyNavigatingTo;
        this.tabContainerProperty = null;
        this.tumlTabViewManagers = [];
        this.tabContainer = null;
        this.parentTabContainer = tabContainer;
        this.parentTabContainerManager = null;

        this.destroyTabContainer = function () {
            $('#' + this.getTabId() + 'ui-layout-center-heading').remove();
            this.clearAllTabs();
            if (this.tabContainer !== null) {
                this.tabContainer.remove();
            }
        }

        this.maybeCreateTabContainer = function () {
            var tabLayoutDiv = $('#' + this.getTabId());
            var tabContainer = $('#' + this.getTabId() + 'Tabs');
            if (tabContainer.length == 0) {

                //disable the parents save button
                if (!(this instanceof Tuml.TumlMainViewManager)) {
                    this.parentTabContainerManager.disableButtons();
                }

                this.tumlTabViewManagers = [];

                //add in the div where the property info validation warning goes
                var uiLayoutCenterHeading = $('<div />', {id: this.getTabId() + 'ui-layout-center-heading', class: 'ui-layout-center-heading'}).appendTo(tabLayoutDiv);
                $('<div />', {id: this.getTabId() + 'navigation-qualified-name', class: 'navigation-qualified-name'}).appendTo(uiLayoutCenterHeading);
                $('<div />', {id: this.getTabId() + 'validation-warning', class: 'validation-warning'}).appendTo(uiLayoutCenterHeading);

                this.tabContainer = $('<div />', {id: this.getTabId() + 'Tabs'}).appendTo(tabLayoutDiv);
                this.tabContainer.append('<ul />');
                this.tabContainer.tabs();
                this.tabContainer.find(".ui-tabs-nav").sortable({
                    axis: "x",
                    stop: function () {
                        self.tabContainer.tabs("refresh");
                    }
                });

                this.tabContainer.tabs({
                    activate: function (event, ui) {
                        var queryId = $.data(ui.newPanel[0], 'queryId');
//                        var tabEnum = $.data(ui.newPanel[0], 'tabEnum');

                        //first deactivate all grids
                        self.deactivateGrids();

                        //always set the old tab to being closed
                        for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                            var tumlTabViewManager = self.tumlTabViewManagers[i];
                            if (ui.oldPanel['0'] !== undefined && ui.oldPanel['0'].id == tumlTabViewManager.getTabId()) {
                                if (tumlTabViewManager.tumlTabGridManager !== undefined) {
                                    tumlTabViewManager.open = false;
                                    break;
                                }
                            }
                        }

                        var tumlTabViewManagerClickedOn = null;
                        //if a leaf node then set it to open
                        for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                            var tumlTabViewManager = self.tumlTabViewManagers[i];
                            if (ui.newPanel['0'].id == tumlTabViewManager.getTabId()) {

                                tumlTabViewManagerClickedOn = tumlTabViewManager;

                                if (tumlTabViewManager.tumlTabViewManagers.length === 0) {
                                    tumlTabViewManager.open = true;
                                }
                                break;
                            }
                        }

                        tumlTabViewManagerClickedOn.activeOpenTabsGrid();

                    }
                });

                this.tabContainer.tabs({
                    beforeActivate: function (event, ui) {
                        if (!Slick.GlobalEditorLock.commitCurrentEdit()) {
                            return false;
                        } else {
                            return true;
                        }

                    }
                });

                //add in button
                this.addButtons();
            }
        }

        this.disableButtons = function () {
            var tabsNav = this.tabContainer.find('.ui-tabs-nav');
            tabsNav.find('#' + this.getTabId() + 'save').attr('disabled', 'disabled');
        }

        this.enableButtons = function () {
            var tabsNav = this.tabContainer.find('.ui-tabs-nav');
            tabsNav.find('#' + this.getTabId() + 'save').removeAttr('disabled');
        }

        this.addButtons = function () {
            var tabsNav = this.tabContainer.find('.ui-tabs-nav');
            var tabsButtonDiv = $('<div />', {id: 'tabcontainer-button', class: 'tabs-button'}).appendTo(tabsNav);
            var saveButton = $('<button />', {id: this.getTabId() + 'save'}).text('Save').appendTo(tabsButtonDiv);
            saveButton.button().click(function (event) {
                if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                    self.saveTabs();
                }
                event.preventDefault();
            });
            var cancelButton = $('<button />', {id: this.getTabId() + 'save'}).text('Cancel').appendTo(tabsButtonDiv);
            cancelButton.button().click(function (event) {
                self.doCancel();
                event.preventDefault();
            });

        }

        this.createPropertyDescriptionHeading = function () {
            var multiplicity;
            if (this.tabContainerProperty.upper == -1) {
                multiplicity = 'multiplicity: [' + this.tabContainerProperty.lower + '..*]';
            } else {
                multiplicity = 'multiplicity: [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']';
            }
            var unique = 'unique: ' + this.tabContainerProperty.unique;
            var ordered = 'ordered: ' + this.tabContainerProperty.ordered;
            //TODO
//            var derived = 'derived: ' + propertyNavigatingTo.derived;
            var association = 'association: ' + (this.tabContainerProperty.composite ? 'composite' : 'non composite');
            return multiplicity + ', ' + unique + ', ' + ordered + ', ' + association;
        }

    }

    TumlTabContainerManager.prototype.updateDataModel = function(fakeId, fieldName, one) {
        this.parentTabContainerManager.updateDataModel(fakeId, fieldName, one);
    }

    TumlTabContainerManager.prototype.doCancel = function () {
        //Save the child's backedup data into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
            if (this.tabContainerProperty.upper == -1 || this.tabContainerProperty.upper > 1) {
                var data = [];
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && tumlTabViewManager.tumlTabGridManager.dataView.getItems().length > 0) {
                        data.push.apply(data, tumlTabViewManager.backupData);
                    }
                }
                this.setCellValue(data);
            } else {
                if (this.tabContainerProperty.upper !== 1) {
                    alert("upper suppose to be a one");
                }
                var firstTumlTabViewManager = null;
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {
                        if (firstTumlTabViewManager !== null) {
                            alert("tabs gone wrong!");
                        }
                        firstTumlTabViewManager = tumlTabViewManager;
                    }
                }
                this.setCellValue(tumlTabViewManager.backupData);
            }
        }
        this.destroyTabContainer();
        this.tumlTabGridManager.active = true;
        $('#slickGrid' + this.tabId).show();

        //enable the save button
        this.parentTabContainerManager.enableButtons();
    }

    //This only gets called by component sub tabs
    TumlTabContainerManager.prototype.saveTabs = function () {
        if (this.validateMultiplicity()) {
            //Save the child grids into the component's cell
            if (this.tumlTabViewManagers.length > 0) {
                if (this.tabContainerProperty.upper == -1 || this.tabContainerProperty.upper > 1) {
                    var data = [];
                    for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                        var tumlTabViewManager = this.tumlTabViewManagers[i];
                        if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && tumlTabViewManager.tumlTabGridManager.dataView.getItems().length > 0) {
                            data.push.apply(data, tumlTabViewManager.getTabData());
                            tumlTabViewManager.tumlTabGridManager.updateDataModel();
                        }
                    }
                    this.setCellValue(data);
                } else {
                    if (this.tabContainerProperty.upper !== 1) {
                        alert("upper suppose to be a one");
                    }
                    var firstTumlTabViewManager = null;
                    for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                        var tumlTabViewManager = this.tumlTabViewManagers[i];
                        if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {
                            if (firstTumlTabViewManager !== null) {
                                alert("tabs gone wrong!");
                            }
                            firstTumlTabViewManager = tumlTabViewManager;
                        }
                    }
                    var data = tumlTabViewManager.tumlTabOneManager.fieldsToObject();
                    this.setCellValue(data);
                    tumlTabViewManager.tumlTabOneManager.updateDataModel(data);

                }
            }
            this.destroyTabContainer();
            this.tumlTabGridManager.active = true;
            $('#slickGrid' + this.tabId).show();

            //enable the save button
            this.parentTabContainerManager.enableButtons();
        }
    }

    TumlTabContainerManager.prototype.setComponentIdToTmpId = function (item) {
        //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
        //Go through all the properties, for each composite property set the id = tmpId
        if (item.tmpId !== undefined) {
            item.id = item.tmpId;
        }
        for (var p in item) {
            if (item[p] !== undefined && item[p] !== null) {
                if (Array.isArray(item[p])) {

                    for (var i = 0; i < item[p].length; i++) {
                        this.setComponentIdToTmpId(item[p][i]);
                    }

                } else if (typeof item[p] === 'object') {

                    this.setComponentIdToTmpId(item[p]);

                }
            }
        }
    }

    TumlTabContainerManager.prototype.deactivateGrids = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            this.tumlTabViewManagers[i].deactivateGrids();
        }
    }

    TumlTabContainerManager.prototype.activeOpenTabsGrid = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            tumlTabViewManager.activeOpenTabsGrid();
        }
    }

    TumlTabContainerManager.prototype.getTabId = function () {
        alert('this must be overriden!');
    }

    TumlTabContainerManager.prototype.removeValidationWarningHeader = function () {
        $('#' + this.getTabId() + 'validation-warning').children().remove();
    }

    TumlTabContainerManager.prototype.updateNavigationHeader = function (qualifiedName) {
        $('#' + this.getTabId() + 'navigation-qualified-name').children().remove();
        var propertyDescription = qualifiedName;
        if (this.tabContainerProperty !== undefined && this.tabContainerProperty !== null) {
            propertyDescription += '  -  ' + this.createPropertyDescriptionHeading();
        }
        $('#' + this.getTabId() + 'navigation-qualified-name').append($('<span />').text(propertyDescription));
    }

    TumlTabContainerManager.prototype.updateValidationWarningHeader = function () {
        $('#' + this.getTabId() + 'validation-warning').children().remove();
        var tumlTabManyViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var rowCount = 0;
        for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
            var dataView = tumlTabManyViewManagers[i].tumlTabGridManager.dataView;
            rowCount += dataView.getItems().length;
        }
        if (rowCount < this.tabContainerProperty.lower || (this.tabContainerProperty.upper !== -1 && rowCount > this.tabContainerProperty.upper)) {
            $('#' + this.getTabId() + 'validation-warning').append($('<span />').text(
                'multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']'));
        }
    }

    TumlTabContainerManager.prototype.validateMultiplicity = function () {
        var tumlTabViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var rowCount = 0;
        for (var i = 0; i < tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = tumlTabViewManagers[i];
            if (tumlTabViewManager instanceof  Tuml.TumlTabManyViewManager) {
                var dataView = tumlTabViewManager.tumlTabGridManager.dataView;
                rowCount += dataView.getItems().length;
            } else {
                rowCount = 1;
                break;
            }
        }
        if (rowCount < this.tabContainerProperty.lower || (this.tabContainerProperty.upper !== -1 && rowCount > this.tabContainerProperty.upper)) {
            alert('multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']');
            return false;
        } else {
            return true;
        }
    }

    TumlTabContainerManager.prototype.getTumlTabManyOrOneViewManagers = function (commitCurrentEdit) {
        var tumlTabManyViewManagers = [];
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            //Get all the many tab views
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager || tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {
                if (commitCurrentEdit) {
                    Slick.GlobalEditorLock.commitCurrentEdit();
                }
                tumlTabManyViewManagers.push(tumlTabViewManager);
            }
        }
        return tumlTabManyViewManagers;
    }


    TumlTabContainerManager.prototype.refreshContext = function (tumlUri) {
        this.parentTabContainerManager.refreshContext(tumlUri);
    }

    TumlTabContainerManager.prototype.saveAndClearAllTabs = function () {
        //Do this in reverse as saveAndCloseTab removes the tab from the array
        var tabLength = this.tumlTabViewManagers.length;
        while (tabLength--) {
            this.tumlTabViewManagers[tabLength].saveAndCloseTab();
        }
        this.tumlTabViewManagers = null;
    }

    TumlTabContainerManager.prototype.clearAllTabs = function () {
        //Do this in reverse as closeTab removes the tab from the array
        var tabLength = this.tumlTabViewManagers.length;
        while (tabLength--) {
            this.tumlTabViewManagers[tabLength].closeTab();
        }
        this.tumlTabViewManagers = [];
    }

    TumlTabContainerManager.prototype.addToTumlTabViewManagers = function (tumlChildTabViewManager) {
        this.tumlTabViewManagers.push(tumlChildTabViewManager);
    }

    TumlTabContainerManager.prototype.addNewRow = function () {
        alert("TumlTabContainerManager.prototype.addNewRow must be overriden");
    }

    TumlTabContainerManager.prototype.handleDeleteRow = function () {
    }

    TumlTabContainerManager.prototype.addTab = function (tabEnum, result, tumlUri, options, propertyNavigatingTo) {
        var metaForData = result.meta.to;

        var tumlTabViewManager;
        if (options.isOne) {
            tumlTabViewManager = new Tuml.TumlTabOneViewManager(
                tabEnum,
                this.tabContainer,
                {
                    many: !options.isOne,
                    one: options.isOne,
                    query: false,
                    forLookup: options.forLookup,
                    forManyComponent: options.forManyComponent,
                    forOneComponent: options.forOneComponent,
                    forCreation: options.forCreation
                },
                tumlUri,
                result,
                propertyNavigatingTo
            );
            tumlTabViewManager.parentTabContainerManager = this;

            tumlTabViewManager.onClickOneComponent.subscribe(function (e, args) {
                //Get the meta data
                $.ajax({
                    url: args.property.tumlMetaDataUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (metaDataResponse, textStatus, jqXHR) {
                        $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitleName);
                        if (args.data !== null) {
                            metaDataResponse[0].data = args.data;
                        }
//                        tabEnum, result, tumlUri, options, propertyNavigatingTo
                        var tumlOneComponentTabViewManager = self.addTab(
                            tuml.tab.Enum.Properties,
                            metaDataResponse[0],
                            args.tumlUri,
                            {forLookup: false, forManyComponent: false, forOneComponent: true, isOne: true, forCreation: true},
                            args.property
                        );
                        tumlOneComponentTabViewManager.createTab(metaDataResponse[0], false);
                        tumlTabViewManager.setProperty(args.property);
                        tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting ' + args.property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                    }
                });
            });
        } else {
            if (options.forManyComponent) {
                tumlTabViewManager = new Tuml.TumlTabManyComponentViewManager(
                    tabEnum,
                    this.tabContainer,
                    {
                        many: !options.isOne,
                        one: options.isOne,
                        query: false,
                        forLookup: options.forLookup,
                        forManyComponent: options.forManyComponent
                    },
                    tumlUri,
                    result,
                    propertyNavigatingTo

                );
                tumlTabViewManager.parentTabContainerManager = this;
            } else {
                tumlTabViewManager = new Tuml.TumlTabManyViewManager(
                    tabEnum,
                    this.tabContainer,
                    {
                        many: !options.isOne,
                        one: options.isOne,
                        query: false,
                        forLookup: options.forLookup,
                        forManyComponent: options.forManyComponent
                    },
                    tumlUri,
                    result,
                    propertyNavigatingTo
                );
                tumlTabViewManager.parentTabContainerManager = this;
            }
            tumlTabViewManager.onSelectButtonSuccess.subscribe(function (e, args) {
                tumlTabViewManager.getParentTumlTabViewManager().addItems(args.items);
                //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                $('#tab-container').tabs('close', args.tabName + " Select");
                $('#' + args.tabName + "Lookup").remove();
                self.tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
                self.tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onManyComponentSaveButtonSuccess.subscribe(function (e, args) {
                alert('commented out');
//                    tumlTabViewManager.getParentTumlTabViewManager().setValue(args.value);
//                    closeTab(tumlTabViewManager);
//                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
//                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onManyComponentCloseButtonSuccess.subscribe(function (e, args) {
                alert('commented out');
//                    closeTab(tumlTabViewManager);
//                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
//                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                $('#tab-container').tabs('close', args.tabName + " Select");
                $('#' + args.tabName + "Lookup").remove();
                self.tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
                self.tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onAddButtonSuccess.subscribe(function (e, args) {
                $('#tab-container').tabs('disableTab', metaForData.name);
                var tumlLookupTabViewManager = addTab(
                    tuml.tab.Enum.Properties,
                    args.data,
                    args.tumlUri,
                    args.propertyNavigatingTo,
                    {forLookup: true, forManyComponent: false}
                );
                tumlLookupTabViewManager.createTab(args.data, false);
                tumlLookupTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
            });

            tumlTabViewManager.onClickOneComponentCell.subscribe(function (e, args) {
                console.log('TumlMainViewManager onClickOneComponentCell fired');
                //Get the meta data
                $.ajax({
                    url: args.property.tumlMetaDataUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (result, textStatus, jqXHR) {
                        if (args.data.length !== 0) {
                            result[0].data = args.data;
                        }
                        var tumlOneComponentTabViewManager = self.addTab(
                            tuml.tab.Enum.Properties,
                            result[0],
                            args.tumlUri,
                            {forLookup: false, forManyComponent: false, forOneComponent: true, isOne: true, forCreation: true},
                            args.property
                        );
                        tumlTabViewManager.setCell(args.cell);
                        tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                        self.tabContainer.tabs("disable", self.tumlTabViewManagers.indexOf(tumlTabViewManager));
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting ' + args.property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                    }
                });
            });

            tumlTabViewManager.onSelfCellClick.subscribe(function (e, args) {
                self.onSelfCellClick.notify(args, e, self);
            });
            tumlTabViewManager.onContextMenuClickDelete.subscribe(function (e, args) {
                self.onContextMenuClickDelete.notify(args, e, self);
            });
        }
        tumlTabViewManager.onPutSuccess.subscribe(function (e, args) {
            self.onPutSuccess.notify(args, e, self);
            if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                alert('update the tree!');
            }
        });
        tumlTabViewManager.onPutFailure.subscribe(function (e, args) {
            self.onPutFailure.notify(args, e, self);
        });
        tumlTabViewManager.onPostSuccess.subscribe(function (e, args) {
            self.onPostSuccess.notify(args, e, self);
            alert('update the tree!');
            if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                var metaDataNavigatingTo = args.data[0].meta.to;
                var metaDataNavigatingFrom = args.data[0].meta.from;
                var contextVertexId = retrieveVertexId(args.tumlUri);
                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
            }
        });
        tumlTabViewManager.onPostFailure.subscribe(function (e, args) {
            self.onPostFailure.notify(args, e, self);
        });
        tumlTabViewManager.onDeleteSuccess.subscribe(function (e, args) {
            self.onDeleteSuccess.notify(args, e, self);
        });
        tumlTabViewManager.onDeleteFailure.subscribe(function (e, args) {
            self.onDeleteFailure.notify(args, e, self);
        });
        tumlTabViewManager.onCancel.subscribe(function (e, args) {
            self.onCancel.notify(args, e, self);
        });

        tumlTabViewManager.onCloseTab.subscribe(function (e, panelId) {
//                for (var j = 0; j < self.tumlTabViewManagers.length; j++) {
//                    if (panelId === self.tumlTabViewManagers[j].tabDivName) {
//                        self.tumlTabViewManagers[j].closeTab();
//                    }
//                }
//                tabContainer.tabs("refresh");
        });

        return tumlTabViewManager;

    }


})(jQuery);
