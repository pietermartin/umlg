(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabContainerManager: TumlTabContainerManager
        }
    });

    function TumlTabContainerManager(tabContainer, propertyNavigatingTo) {

        //This will be null for the root object only
        this.propertyNavigatingTo = propertyNavigatingTo;
        this.tumlTabViewManagers = [];
        this.tabContainer = null;
        this.parentTabContainer = tabContainer;
        this.parentTabContainerManager = null;

        this.getParentTabContainerManager = function () {
            return this.parentTabContainerManager;
        }

        this.setParentTabContainerManager = function (parentTabContainerManager) {
            this.parentTabContainerManager = parentTabContainerManager;
        }

        this.removeTumlTabViewManager = function (tumlTabViewManager) {
            var index = this.tumlTabViewManagers.indexOf(tumlTabViewManager);
            this.tumlTabViewManagers.splice(index, 1);
        }

        this.destroyTabContainer = function() {
            $('#' + this.getTabId() + 'ui-layout-center-heading').remove();
            this.clearAllTabs();
            if (this.tabContainer !== null) {
                this.tabContainer.remove();
            }
        }

        this.maybeCreateTabContainer = function () {
            var self = this;
            var tabLayoutDiv = $('#' + this.getTabId());
            var tabContainer = $('#' + this.getTabId() + 'Tabs');
            if (tabContainer.length == 0) {

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
                    }
                });
            }
        }

        this.createPropertyDescriptionHeading = function() {
            var multiplicity;
            if (this.propertyNavigatingTo.upper == -1) {
                multiplicity = 'multiplicity: [' + this.propertyNavigatingTo.lower + '..*]';
            } else {
                multiplicity = 'multiplicity: [' + this.propertyNavigatingTo.lower + '..' + this.propertyNavigatingTo.upper + ']';
            }
            var unique = 'unique: ' + this.propertyNavigatingTo.unique;
            var ordered = 'ordered: ' + this.propertyNavigatingTo.ordered;
            //TODO
//            var derived = 'derived: ' + propertyNavigatingTo.derived;
            var association = 'association: ' + (this.propertyNavigatingTo.composite ? 'composite' : 'non composite');
            return multiplicity + ', ' + unique + ', ' + ordered + ', ' + association;
        }

    }

    TumlTabContainerManager.prototype.getTabId = function() {
        alert('this must be overriden!');
    }


    TumlTabContainerManager.prototype.updateValidationWarningHeader = function () {
        $('#' + this.getTabId() + 'validation-warning').children().remove();
        var tumlTabManyViewManagers = this.getTumlTabManyViewManagers(false);
        var rowCount = 0;
        for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
            var dataView = tumlTabManyViewManagers[i].tumlTabGridManager.dataView;
            rowCount += dataView.getItems().length;
        }
        if (rowCount < this.propertyNavigatingTo.lower || (this.propertyNavigatingTo.upper !== -1 && rowCount > this.propertyNavigatingTo.upper)) {
            $('#' + this.getTabId() + 'validation-warning').append($('<span />').text(
                'multiplicity falls outside the valid range [' + this.propertyNavigatingTo.lower + '..' + this.propertyNavigatingTo.upper + ']'));
        }
    }

    TumlTabContainerManager.prototype.getTumlTabManyViewManagers = function (commitCurrentEdit) {
        var tumlTabManyViewManagers = [];
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            //Get all the many tab views
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                if (commitCurrentEdit) {
                    tumlTabViewManager.tumlTabGridManager.grid.getEditorLock().commitCurrentEdit();
                }
                tumlTabManyViewManagers.push(tumlTabViewManager);
            }
        }
        return tumlTabManyViewManagers;
    }


    TumlTabContainerManager.prototype.refreshContext = function (tumlUri) {
        this.getParentTabContainerManager().refreshContext(tumlUri);
    }

    TumlTabContainerManager.prototype.clearAllTabs = function () {
        //Do this in reverse as closeTab removes the tab from the array
        var tabLength = this.tumlTabViewManagers.length;
        while (tabLength--) {
            this.tumlTabViewManagers[tabLength].closeTab();
        }
        this.tumlTabViewManagers = null;
    }

    TumlTabContainerManager.prototype.addToTumlTabViewManagers = function (tumlChildTabViewManager) {
        this.tumlTabViewManagers.push(tumlChildTabViewManager);
    }

    TumlTabContainerManager.prototype.addNewRow = function () {
        alert("TumlTabContainerManager.prototype.addNewRow must be overriden");
    }

    TumlTabContainerManager.prototype.postTabCreate = function (tumlTabViewManager, result, isOne, metaForData, forCreation, activeIndex) {
        tumlTabViewManager.createTab();
        this.tabContainer.tabs("option", "active", activeIndex);
        //Create the grid
        if (!isOne) {
            tumlTabViewManager.createGrid(result);
        } else {
            tumlTabViewManager.createOne(result.data[0], metaForData, forCreation);
        }
    }

    TumlTabContainerManager.prototype.handleDeleteRow = function() {
        this.getParentTabContainerManager().updateValidationWarningHeader();
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
                    forOneComponent: options.forOneComponent
                },
                tumlUri,
                result,
                propertyNavigatingTo
            );
            tumlTabViewManager.setParentTabContainerManager(this);

            tumlTabViewManager.onOneComponentSaveButtonSuccess.subscribe(function (e, args) {
                alert('commented out');
//                    tumlTabViewManager.getParentTumlTabViewManager().setValue(args.value);
//                    closeTab(tumlTabViewManager);
//                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
//                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onOneComponentCloseButtonSuccess.subscribe(function (e, args) {
                alert('commented out');
//                    closeTab(tumlTabViewManager);
//                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
//                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
            });
            tumlTabViewManager.onPutOneSuccess.subscribe(function (e, args) {
                self.onPutOneSuccess.notify(args, e, self);
            });
            tumlTabViewManager.onPostOneSuccess.subscribe(function (e, args) {
                Tuml.TumlTabContainerManager.prototype.clearAllTabs.call(this);
                self.onPostOneSuccess.notify(args, e, self);
            });
            tumlTabViewManager.onDeleteOneSuccess.subscribe(function (e, args) {
                Tuml.TumlTabContainerManager.prototype.clearAllTabs.call(this);
                self.onDeleteOneSuccess.notify(args, e, self);
            });
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
                        self.postTabCreate(tumlOneComponentTabViewManager, metaDataResponse[0], true, metaDataResponse[0].meta.to, false, self.tumlTabViewManagers.length - 1);
                        tumlTabViewManager.setProperty(args.property);
                        tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting ' + args.property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                    }
                });
            });
            tumlTabViewManager.onClickManyComponent.subscribe(function (e, args) {
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
                        var tumlOneComponentTabViewManager = self.addTab(
                            tuml.tab.Enum.Properties,
                            metaDataResponse[0],
                            args.tumlUri,
                            args.property,
                            {forLookup: false, forManyComponent: true, isOne: false, forCreation: true}
                        );
                        self.postTabCreate(tumlOneComponentTabViewManager, metaDataResponse[0], true, metaDataResponse[0].meta.to, false, self.tumlTabViewManagers.length - 1);
                        //TODO fix below can not use tumlTabViewManager as a variable as it represent just the last varriable
//                            tumlTabViewManager.setProperty(args.property);
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
                tumlTabViewManager.setParentTabContainerManager(this);
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
                tumlTabViewManager.setParentTabContainerManager(this);
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
                self.postTabCreate(tumlLookupTabViewManager, args.data, true, args.data.meta.to, false, self.tumlTabViewManagers.length - 1);
                tumlLookupTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
            });
            tumlTabViewManager.onClickManyComponentCell.subscribe(function (e, args) {

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
        tumlTabViewManager.onAddRowSuccess.subscribe(function (e, args) {
            self.updateValidationWarningHeader();
        });
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
