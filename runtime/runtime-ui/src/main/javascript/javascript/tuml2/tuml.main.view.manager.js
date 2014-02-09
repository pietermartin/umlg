(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlMainViewManager: TumlMainViewManager,
            TumlFakeIndex: 0,
            GlobalOneToOneIndex: GlobalOneToOneIndex
        }
    });

    function GlobalOneToOneIndex() {

        var index = {};

        this.addToOneToOneIndex = function (fakeId, item) {
            index[fakeId] = item;
        }

        this.getIndex = function (fakeId) {

            if (index[fakeId] !== undefined && index[fakeId] !== null) {
                return index[fakeId];
            }
            return null;
        }

    }

    function TumlMainViewManager(uiManager, leftMenuManager) {

        this.TumlMainViewManager = "1.0.0";
        var self = this;
        this.uiManager = uiManager;
        var oclExecuteUri;
        var instanceQueryTumlUri;
        var classQueryTumlUri;
        var rootQueryTumlUri;
        this.contextVertexId = -1;
        var contextChanged = true;
        this.globalOneToOneIndex = new GlobalOneToOneIndex();
        this.qualifiedName = null;
        this.tumlActiveTabViewManager = null;

        this.setFocus = function (focusTo) {
            if (focusTo == Tuml.FocusEnum.LEFT_MENU) {
                //currentFocus == 0
                leftMenuManager.setFocus();
            } else if (focusTo == Tuml.FocusEnum.TOP_CONTEXT) {
                //currentFocus == 1
                this.tumlTabViewManagers[0].setFocus(focusTo);
            } else {
                this.tumlTabViewManagers[0].setFocus(focusTo);
            }
        }

        this.refreshQueryMenuCss = function (queryToHighlightId, accordionIndex) {
            leftMenuManager.refreshQueryMenuCss(queryToHighlightId, accordionIndex);
        }

        this.refreshDiagramCss = function (diagramNameHighlightId, accordionIndex) {
            leftMenuManager.refreshDiagramCss(diagramNameHighlightId, accordionIndex);
        }

        this.refresh = function (tumlUri, result) {
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            //propertyNavigatingTo is null when viewing a one
            this.propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(result[0].meta.qualifiedName, metaDataNavigatingFrom));

            if (this.propertyNavigatingTo != null && (this.propertyNavigatingTo.oneToMany || this.propertyNavigatingTo.manyToMany)) {
                //Property is a many
                this.qualifiedName = result[0].meta.qualifiedName;
                this.tumlUri = tumlUri;
                this.tabContainerProperty = this.propertyNavigatingTo;

                //This also takes care of the leftMenuManager
                this.handleMany(metaDataNavigatingFrom, metaDataNavigatingTo, result);

            } else {
                //Property is a one
                //If there are no data then it is for creation
                var hasData = false;
                for (var i = 0; i < result.length; i++) {
                    if (result[i].data !== null) {
                        hasData = true;
                        break;
                    }
                }
                var isForCreation = hasData === false;
                if (!isForCreation) {
                    this.qualifiedName = result[0].meta.qualifiedName;
                    this.tumlUri = tumlUri;
                    this.tabContainerProperty = this.propertyNavigatingTo;

                    metaDataNavigatingTo = this.handleOneNotForCreation(result);

                } else {
                    //Check if property navigating frmo is the composite owner.
                    //Creation is only possible from the composite owner
                    if (this.propertyNavigatingTo.composite) {
                        this.qualifiedName = result[0].meta.qualifiedName;
                        this.tumlUri = tumlUri;
                        this.tabContainerProperty = this.propertyNavigatingTo;

                        this.handleOneForCreation(metaDataNavigatingFrom, metaDataNavigatingTo, result);

                    } else {
                        alert('Property ' + this.propertyNavigatingTo.name + ' has no data and can not be created as it is not composite!');
                        return false;
                    }

                }
            }
            if (this.contextVertexId == null) {
                oclExecuteUri = "/" + tumlModelName + "/oclExecuteQuery";
            } else {
                oclExecuteUri = "/" + tumlModelName + "/" + this.contextVertexId + "/oclExecuteQuery";
            }
            if (hasInstanceQuery(metaDataNavigatingTo, metaDataNavigatingFrom)) {
                instanceQueryTumlUri = "/" + tumlModelName + "/baseumlgwithquerys/" + this.contextVertexId + "/instanceQuery";
            } else {
                instanceQueryTumlUri = '';
            }
            classQueryTumlUri = "/" + tumlModelName + "/classquery/" + this.contextVertexId + "/query";
            rootQueryTumlUri = "/" + tumlModelName + "/rootquery";

            if (this.contextVertexId != undefined && this.contextVertexId !== -1 && contextChanged) {
                //This is the default query tab, always open
                this.addDefaultQueryTab();
            } else {
                this.addRootQueryTab();
            }

            if (!this.tumlTabViewManagers[0].activated) {
                this.tumlTabViewManagers[0].activated = true;
                this.tumlTabViewManagers[0].activeOpenTabsGrid();
                this.tumlTabViewManagers[0].addOrRemoveSelectButton();
            }
            this.tumlTabViewManagers[0].open = true;

            this.tabContainer.find('a:first').tab('show');

            this.updateNavigationHeader(this.qualifiedName);
            $('body').layout().resizeAll();
            return true;
        }

        this.handleMany = function (metaDataNavigatingFrom, metaDataNavigatingTo, result) {
            var newContextVertexId = retrieveVertexId(this.tumlUri);
            //Check if we coming from a one
//            var wasOne = false;
//            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
//                var tumlTabViewManager = this.tumlTabViewManagers[j];
//                if (tumlTabViewManager instanceof  Tuml.TumlTabOneViewManager) {
//                    wasOne = true;
//                }
//            }
            var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);
//            if (!contextChanged && wasOne) {
//                this.addButtons();
//            }
            leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, this.contextVertexId, this.propertyNavigatingTo);
            refreshInternal(this.tumlUri, result, false);
            //reorder tabs, make sure new tabs are first
            reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);

            //register the tab listeners
            this.tabContainer.find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                //Show the footer
                $('#tabs-layoutpanelPanelDefault').children('.umlg-panel-footer.panel-footer').show();
            })
        }

        this.handleOneNotForCreation = function (result) {
            var self = this;
            //Only one element of the array contains data, i.e. for the return concrete type
            var metaDataNavigatingTo;
            for (var i = 0; i < result.length; i++) {
                if (result[i].data !== null) {
                    metaDataNavigatingTo = result[i].meta.to;
                    this.qualifiedName = result[i].meta.qualifiedName;
                    var newContextVertexId = result[i].data.id;
                    var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                    //If property is a one then there is no navigating from
                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, this.contextVertexId);
                    //Do not call refreshInternal as it creates all tabs for the meta data
                    var tumlTabViewManager = this.createTabContainer(tuml.tab.Enum.Properties, result[i], this.tumlUri, {forLookup: false, forManyComponent: false, isOne: true, forCreation: false}, this.propertyNavigatingTo);
                    this.addToTumlTabViewManagers(tumlTabViewManager);
                    tumlTabViewManager.createTab(result[i], /*isForCreation*/false);

                    //move buttons to form-div
                    //this.addButtons();

                    //reorder tabs, make sure new tabs are first
                    reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);
                    break;
                }
            }
            return metaDataNavigatingTo;
        }

        this.handleTabActivate = function (e) {
            var activatedTab = e.target.offsetParent // activated tab
            var activatedTabContentDiv = this.tabContainer.find('#' + activatedTab.id.substring(2));
            if (self instanceof Tuml.TumlMainViewManager) {
                var tabEnum = $.data(activatedTabContentDiv[0], 'tabEnum');
                if (tabEnum != tuml.tab.Enum.Diagrams) {
                    var queryId = $.data(activatedTabContentDiv[0], 'queryId');
                    if (queryId === undefined || queryId === null) {
                        queryId = -1;
                    }
                    self.refreshQueryMenuCss(queryId, tabEnum);
                } else {
                    var name = $.data(activatedTabContentDiv[0], 'diagramName');
                    self.refreshDiagramCss(name, tabEnum);
                }
            }

            //first deactivate all grids
            self.deactivateGrids();

            //On deletions there is no previous tab
            if (e.relatedTarget !== undefined) {

                var previousTab = e.relatedTarget.offsetParent // previous tab
                var previousTabContentDiv = this.tabContainer.find('#' + previousTab.id.substring(2));

                //always set the old tab to being closed
                for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = self.tumlTabViewManagers[i];
                    if (previousTabContentDiv[0] !== undefined && previousTabContentDiv[0].id == tumlTabViewManager.getTabId()) {
                        tumlTabViewManager.isOpen = false;
                        if (tumlTabViewManager.tumlTabGridManager !== undefined || tumlTabViewManager.tumlTabOneManager !== undefined) {
                            tumlTabViewManager.open = false;
                            break;
                        }
                    }
                }
            }

            var tumlTabViewManagerClickedOn = null;
            //if a leaf node then set it to open
            for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = self.tumlTabViewManagers[i];
                if (activatedTabContentDiv[0].id == tumlTabViewManager.getTabId()) {

                    tumlTabViewManagerClickedOn = tumlTabViewManager;

                    if (tumlTabViewManager.tumlTabViewManagers.length === 0) {
                        tumlTabViewManager.open = true;
                    }
                    ;
                    break;
                }
                ;
            }
            ;
            tumlTabViewManagerClickedOn.isOpen = true;
            if (!tumlTabViewManagerClickedOn.activated) {
                tumlTabViewManagerClickedOn.activeOpenTabsGrid();
                tumlTabViewManagerClickedOn.addOrRemoveSelectButton();
            }
            tumlTabViewManagerClickedOn.activated = false;
            //Set the current open tab to not open
            if (this.tumlActiveTabViewManager !== null) {
                this.tumlActiveTabViewManager.isOpen = false;
            }
            this.tumlActiveTabViewManager = tumlTabViewManagerClickedOn;
            this.tumlActiveTabViewManager.isOpen = true;

        };

        this.handleOneForCreation = function (metaDataNavigatingFrom, metaDataNavigatingTo, result) {
            //This is for creation of the one
            var newContextVertexId = retrieveVertexId(this.tumlUri);
            var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

            leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, this.contextVertexId);
            refreshInternal(this.tumlUri, result, true, true);

            //reorder tabs, make sure new tabs are first
            reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);
        }

        /**
         * This executes when the user presses cntrl shift save
         * Find the open tab with a save button and click it
         */
        this.saveViaKeyPress = function () {
            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = this.tumlTabViewManagers[i];
                if (tumlTabViewManager.saveViaKeyPress()) {
                    return;
                }
            }
        }

        this.doSave = function (commit) {
            var startTime = new Date().getTime();
            var tumlTabViewManagers = this.getTumlTabManyOrOneViewManagers(commit);

            var validationResults = [];
            var overloadedPostData = {insert: [], update: [], delete: []};
            var many = true;
            for (var i = 0; i < tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = tumlTabViewManagers[i];
                if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {

                    var multiplicityValidationResult = this.validateMultiplicity(tumlTabViewManagers);
                    if (multiplicityValidationResult !== null) {
                        validationResults.push(multiplicityValidationResult);
                    }

                    var dataView = tumlTabViewManager.tumlTabGridManager.dataView;

                    if (commit) {
                        validationResults.push.apply(validationResults, tumlTabViewManager.validateInsert())
                        validationResults.push.apply(validationResults, tumlTabViewManager.validateUpdate())
                    }

                    overloadedPostData.insert.push.apply(overloadedPostData.insert, dataView.getNewItems());
                    overloadedPostData.update.push.apply(overloadedPostData.update, dataView.getUpdatedItems());
                    overloadedPostData.delete.push.apply(overloadedPostData.delete, dataView.getDeletedItems());
                } else {
                    many = false;
                    if (tumlTabViewManager.oneManyOrQuery.forCreation) {
                        //in case of inheritence their might be multiple tabs, check for the active one
                        if (tumlTabViewManager.open) {
                            overloadedPostData.insert.push(tumlTabViewManager.tumlTabOneManager.data);
                            if (commit) {
                                validationResults.push.apply(validationResults, tumlTabViewManager.validateInsert());
                            }
                            break;
                        }
                    } else {
                        if (this.propertyNavigatingTo == null) {
                            overloadedPostData = tumlTabViewManager.tumlTabOneManager.data;
                            if (commit) {
                                validationResults.push.apply(validationResults, tumlTabViewManager.validateUpdate());
                            }
                        } else {
                            overloadedPostData.update.push(tumlTabViewManager.tumlTabOneManager.data);
                            if (commit) {
                                validationResults.push.apply(validationResults, tumlTabViewManager.validateUpdate());
                            }
                        }
                        break;
                    }
                }
            }

            if (commit && validationResults.length > 0) {
                var message = "";
                for (var i = 0; i < validationResults.length; i++) {
                    var validationResult = validationResults[i];
                    message += validationResult.toString();
                }
                alert(message);
                return;
            }

            var postUri;
            if (!commit) {
                postUri = self.tumlUri + "?rollback=true";
            } else {
                postUri = self.tumlUri;
            }

            var AJAX_TYPE;
            if (this.propertyNavigatingTo == null) {
                AJAX_TYPE = "PUT";
            } else {
                AJAX_TYPE = "POST";
            }

            $.ajax({
                url: postUri,
                type: AJAX_TYPE,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (result) {
                    if (commit) {
                        //post returns the meta data with, unlike get which does not
                        self.updateTabsForResultAfterCommit(result);
                    } else {
//                        var endTimeBeforeUpdateGrids = new Date().getTime();
//                        console.log("Time taken in millis for server call before update grids = " + (endTimeBeforeUpdateGrids - startTime));

                        //post returns the meta data with, unlike get which does not
                        self.updateTabsForResultAfterRollback(result);

//                        var endTimeAfterUpdateGrids = new Date().getTime();
//                        console.log("Time taken in millis to update grids = " + (endTimeAfterUpdateGrids - endTimeBeforeUpdateGrids));
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.updateTabsForResultAfterCommit = function (result) {
            for (var i = 0; i < result.length; i++) {
                var resultForTab = result[i];
                var metaForData = resultForTab.meta.to;
                var indexesToRemove = [];
                for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[j];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                        tumlTabViewManager.beginUpdate();
                        //TOTO use qualified name somehow
                        if (tumlTabViewManager.tabId == metaForData.name) {
                            for (var k = 0; k < resultForTab.data.length; k++) {
                                this.clearComponentAndAssociationClassTmpId(resultForTab.data[k], metaForData, true);
                                tumlTabViewManager.updateGridAfterCommit(resultForTab.data[k]);
                            }
                        }
                        tumlTabViewManager.endUpdate(false);
                    } else if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {

                        if (result.length > 1) {
                            throw 'result array from server can not be bigger than one for a one!';
                        }

                        if (tumlTabViewManager.tabId == metaForData.name) {
                            tumlTabViewManager.beginUpdate();
                            this.clearComponentAndAssociationClassTmpId(resultForTab.data, metaForData, true);
                            tumlTabViewManager.updateGridAfterCommit(resultForTab.data);
                            tumlTabViewManager.endUpdate(true);
                        } else {
                            //Close any other one tabs that may be open after a creation with multiple concrete tabs
                            indexesToRemove.push(j);
                        }
                    }
                }
                for (var j = 0; j < indexesToRemove.length > 0; j++) {
                    var index = indexesToRemove[j];
                    this.tumlTabViewManagers[index].closeTab();
                }
            }
            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                var tumlTabViewManager = this.tumlTabViewManagers[j];
                if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                    //This will clear the update, insert arrays in slick grid's data view
                    tumlTabViewManager.clearArraysAfterCommit();
                }
            }
        }

        this.updateTabsForResultAfterRollback = function (result) {
            for (var i = 0; i < result.length; i++) {
                var resultForTab = result[i];
                var metaForDataTo = resultForTab.meta.to;
                for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[j];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                        tumlTabViewManager.beginUpdate();
                        //TODO use qualified name somehow
                        //Line up he result with the correct tab
                        if (tumlTabViewManager.tabId == metaForDataTo.name) {
                            for (var k = 0; k < resultForTab.data.length; k++) {
                                //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
                                //Go through all the properties, for each composite property set the id = tmpId
                                this.setComponentIdToTmpId(resultForTab.data[k], metaForDataTo);
                                tumlTabViewManager.updateGridAfterRollback(resultForTab.data[k]);
                            }
                        }
                        tumlTabViewManager.endUpdate(true);
                    } else {
                        if (tumlTabViewManager.tabId == metaForDataTo.name) {
                            tumlTabViewManager.beginUpdate();
                            this.setComponentIdToTmpId(resultForTab.data, metaForDataTo);
                            tumlTabViewManager.updateGridAfterRollback(resultForTab.data);
                            tumlTabViewManager.endUpdate(true);
                        }
                    }
                }
            }
        }

        this.validateMultiplicity = function (tumlTabViewManagers) {
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
                var validationResult = new Tuml.ValidationResult(-1, this.tabContainerProperty.name);
                validationResult.property = this.tabContainerProperty;
                validationResult.qualifiedName = this.tabContainerProperty.qualifiedName;
                validationResult.message = 'multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + (this.tabContainerProperty.upper !== -1 ? this.tabContainerProperty.upper : '*') + ']';
                return validationResult;
            } else {
                return null;
            }
        }

        this.clearTabsOnAddOneOrMany = function (newContextVertexId) {
            if (newContextVertexId === undefined && this.contextVertexId === -1) {
                contextChanged = true;
            } else if (this.contextVertexId === -1 && newContextVertexId === null) {
                contextChanged = true;
            } else if (this.contextVertexId === -1 && newContextVertexId === undefined) {
                contextChanged = true;
            } else {
                contextChanged = newContextVertexId !== this.contextVertexId;
            }
            this.contextVertexId = newContextVertexId;
            if (contextChanged) {
                this.destroyTabContainer();
                this.maybeCreateTabContainer();
            } else {
                this.removeMultiplicityWarningHeader();
                var tumlTabViewManagersToClose = [];
                //Remove property tabs only, query tabs remain for the context
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof  Tuml.TumlTabOneViewManager || tumlTabViewManager instanceof  Tuml.TumlTabManyViewManager) {
                        tumlTabViewManagersToClose.push(tumlTabViewManager);
                    }
                }
                for (var i = 0; i < tumlTabViewManagersToClose.length; i++) {
                    tumlTabViewManagersToClose[i].closeTab();
                    //Clear the buttons
                    this.tabLayoutTabFooterDiv.empty();
                }
            }
            this.addButtons();

            //Save current tabs to help with reordering 2 lines down
            var savedTumlTabViewManagers = [];
            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = this.tumlTabViewManagers[i];
                savedTumlTabViewManagers.push(tumlTabViewManager);
            }
            return savedTumlTabViewManagers;
        };

        this.addQueryTab = function (post, query) {
            //Check is there is already a tab open for this query
            var tumlTabViewManagerQuery;
            var tabIndex = 0;
            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                if (this.tumlTabViewManagers[j].tabId == query.getDivName()) {
                    tumlTabViewManagerQuery = this.tumlTabViewManagers[j];
                    tabIndex = j;
                    break;
                }
            }
            if (tumlTabViewManagerQuery === undefined) {
                if (query.queryType === undefined || query.queryType === null) {
                    //This is for the new query tab
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.Properties, this.tabContainer, instanceQueryTumlUri, classQueryTumlUri, query.getDivName(), query.name, query.id);
                } else if (query.queryType === 'instanceQuery') {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.InstanceQueries, this.tabContainer, instanceQueryTumlUri, '', query.getDivName(), query.name, query.id);
                } else if (query.queryType === 'classQuery') {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.ClassQueries, this.tabContainer, '', classQueryTumlUri, query.getDivName(), query.name, query.id);
                } else {
                    //Root queries
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.RootQueries, this.tabContainer, '', rootQueryTumlUri, query.getDivName(), query.name, query.id);
                }
                tumlTabViewManagerQuery.createTab(post);
                tumlTabViewManagerQuery.parentTabContainerManager = this;

                if (query.id === -1) {
                    this.tumlTabViewManagers.push(tumlTabViewManagerQuery);
                    reorderTabs();
                } else {
                    this.tumlTabViewManagers.splice(this.tumlTabViewManagers.length - 1, 0, tumlTabViewManagerQuery);
                    reorderTabs();
                }

                tumlTabViewManagerQuery.createQuery(oclExecuteUri, query, post);
                tumlTabViewManagerQuery.showInlineForm();

                if (this.tumlActiveTabViewManager !== null) {
                    this.tumlActiveTabViewManager.isOpen = false;
                }
                this.tumlActiveTabViewManager = tumlTabViewManagerQuery;
                this.tumlActiveTabViewManager.isOpen = true;

            } else {
                //Just make the tab active
                this.tabContainer.children('ul').find('li:eq(' + self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery) + ') a').tab('show')
            }
            return tumlTabViewManagerQuery;

        };

        this.addDiagramTab = function (treeNode) {
            //Check is there is already a tab open for this query
            var tumlTabDiagramViewManager;
            var tabIndex = 0;
            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                if (this.tumlTabViewManagers[j].tabId == treeNode.name) {
                    tumlTabDiagramViewManager = this.tumlTabViewManagers[j];
                    tabIndex = j;
                    break;
                }
            }
            if (tumlTabDiagramViewManager === undefined) {
                tumlTabDiagramViewManager = new Tuml.TumlTabDiagramViewManager(tuml.tab.Enum.Diagrams, this.tabContainer, treeNode.name, treeNode.name);
                tumlTabDiagramViewManager.createTab();
                tumlTabDiagramViewManager.parentTabContainerManager = this;

                this.tumlTabViewManagers.splice(this.tumlTabViewManagers.length - 1, 0, tumlTabDiagramViewManager);
                reorderTabs();

            } else {
                //Just make the tab active
                this.tabContainer.children('ul').find('li:eq(' + self.tumlTabViewManagers.indexOf(tumlTabDiagramViewManager) + ') a').tab('show')
            }

            tumlTabDiagramViewManager.createDiagram(treeNode.path, treeNode.type);

            return tumlTabDiagramViewManager;

        }

        this.afterSaveInstance = function (args, previousIndex) {
            addDefaultQueryTab(false);
            var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            //place it back at the previousIndex
            var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
            this.tumlTabViewManagers.splice(currentIndex, 1);
            this.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
            leftMenuManager.refreshInstanceQuery(args.query.id);
        }

        this.afterUpdateInstance = function (args) {
            this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            leftMenuManager.refreshInstanceQuery(args.query.id);
        }

        this.afterDeleteInstance = function (args) {
            leftMenuManager.deleteInstanceQuery(args.query.id);
        }

        this.afterSaveClassQuery = function (args, previousIndex) {
            this.addDefaultQueryTab(false);
            var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            //place it back at the previousIndex
            var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
            this.tumlTabViewManagers.splice(currentIndex, 1);
            this.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
            leftMenuManager.refreshClassQuery(args.query.id);
        }

        this.afterUpdateClassQuery = function (args) {
            this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            leftMenuManager.refreshClassQuery(args.query.id);
        }

        this.afterDeleteClassQuery = function (args) {
            leftMenuManager.deleteClassQuery(args.query.id);
        }

        function reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers) {
            for (var i = 0; i < savedTumlTabViewManagers.length; i++) {
                var tumlTabViewManager = savedTumlTabViewManagers[i];
                var index = self.tumlTabViewManagers.indexOf(tumlTabViewManager)
                self.tumlTabViewManagers.splice(index, 1);
            }
            for (var i = 0; i < savedTumlTabViewManagers.length; i++) {
                self.tumlTabViewManagers.push(savedTumlTabViewManagers[i]);
            }
            reorderTabs();
        }

        function hasInstanceQuery(metaDataNavigatingTo, metaDataNavigatingFrom) {
            var properties;
            if (metaDataNavigatingFrom !== undefined) {
                properties = metaDataNavigatingFrom.properties;
            } else {
                properties = metaDataNavigatingTo.properties;
            }
            for (var i = 0; i < properties.length; i++) {
                var property = properties[i];
                if (property.name !== 'instanceQuery') {
                    return true;
                }
            }
            return false;
        }

        this.addDefaultQueryTab = function () {
            this.addQueryTab(true, new Tuml.Query(-1, 'New Query', 'New Query Description', 'self.name', 'ocl', null, null));
        }

        this.addRootQueryTab = function () {
            this.addQueryTab(true, new Tuml.Query(-1, 'New Query', 'New Query Description', 'self.name', 'ocl', null, 'rootQuery'));
        }

        function refreshInternal(tumlUri, result, isOne, forCreation) {
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            var previousTumlTabViewManager = null;
            for (var i = 0; i < result.length; i++) {
                if (previousTumlTabViewManager !== null) {
                    previousTumlTabViewManager.open = false;
                }
                var tumlTabViewManager = self.createTabContainer(tuml.tab.Enum.Properties, result[i], tumlUri, {forLookup: false, forManyComponent: false, isOne: isOne, forCreation: forCreation}, self.propertyNavigatingTo);
                self.addToTumlTabViewManagers(tumlTabViewManager);

                if (forCreation) {
                    var data = {id: 'fake::' + Tuml.TumlFakeIndex++};
                    data.tmpId = data.id;
                    result[i].data = data;
                }

                tumlTabViewManager.createTab(result[i], forCreation);
                if (forCreation) {
                    self.saveNewRow();
                }

                previousTumlTabViewManager = tumlTabViewManager;
            }
        }

        function reorderTabs() {
            var tabsNav = self.tabContainer.find(".nav.nav-tabs");
            var first = true;
            for (var j = 0; j < self.tumlTabViewManagers.length; j++) {
                var li = $('#li' + self.tumlTabViewManagers[j].tabId);
                if (first) {
                    tabsNav.append(li);
                } else {
                    li.insertAfter(tabsNav);
                }
                tabsNav = li;
                first = false;
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

        Tuml.TumlTabContainerManager.call(this, this.tabContainer);

    }

    TumlMainViewManager.prototype = new Tuml.TumlTabContainerManager;

    TumlMainViewManager.prototype.nestedCount = function (count) {
        return count;
    }

    TumlMainViewManager.prototype.refreshContext = function (tumlUri) {
        this.uiManager.refresh(tumlUri);
    }

    TumlMainViewManager.prototype.addToOneToOneIndex = function (fakeId, item) {
        this.globalOneToOneIndex.addToOneToOneIndex(fakeId, item);
    }

    TumlMainViewManager.prototype.updateDataModelForOneToOne = function (fakeId, fieldName, one) {
        var indexForFakeId = this.globalOneToOneIndex.getIndex(fakeId);
        if (indexForFakeId !== null) {
            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                var tumlTabViewManager = this.tumlTabViewManagers[j];
                if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                    tumlTabViewManager.updateOne(fakeId, fieldName, one, indexForFakeId);
                }
            }
        }
    }

    TumlMainViewManager.prototype.updateDataModelForOneToOneForUpdatedItem = function (qualifiedName, id, displayName, fieldName, one) {
        for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
            var tumlTabViewManager = this.tumlTabViewManagers[j];
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                if (tumlTabViewManager.tumlTabGridManager.localMetaForData.qualifiedName !== qualifiedName) {
                    tumlTabViewManager.updateOneForUpdate(id, displayName, fieldName, one);
                }
            }
        }
    }

    TumlMainViewManager.prototype.doCancel = function () {
        this.enableButtons();
        Slick.GlobalEditorLock.cancelCurrentEdit();
        this.uiManager.refresh(this.tumlUri);
    }

    TumlMainViewManager.prototype.saveTabs = function () {
        this.doSave(true);
    }

    TumlMainViewManager.prototype.getTabId = function () {
        return 'tabs-layout';
    }

    TumlMainViewManager.prototype.saveNewRow = function () {
        this.doSave(false);
    }

    TumlMainViewManager.prototype.handleLookup = function (lookupUri, qualifiedName, loadDataCallback) {
        var startTime = new Date().getTime();
        var tumlTabViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var overloadedPostData = {insert: [], update: [], delete: []};

        var AJAX_TYPE;

        for (var i = 0; i < tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = tumlTabViewManagers[i];
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && !tumlTabViewManager.oneManyOrQuery.forManyComponent) {
                AJAX_TYPE = "POST";
                if (!tumlTabViewManager.oneManyOrQuery.forManyComponent) {
                    var dataView = tumlTabViewManager.tumlTabGridManager.dataView;
                    overloadedPostData.insert.push.apply(overloadedPostData.insert, dataView.getNewItems());
                    overloadedPostData.update.push.apply(overloadedPostData.update, dataView.getUpdatedItems());
                    overloadedPostData.delete.push.apply(overloadedPostData.delete, dataView.getDeletedItems());
                }
            } else {
                if (this.propertyNavigatingTo == null) {
                    AJAX_TYPE = "PUT";
                } else {
                    AJAX_TYPE = "POST";
                }
                overloadedPostData = tumlTabViewManager.tumlTabOneManager.data;
                break;
            }
        }
        var postUri;
        if (AJAX_TYPE === "POST") {
            postUri = this.tumlUri + "_forwardToLookup?lookupUri=" + lookupUri + '&qualifiedName=' + qualifiedName;
        } else {
            postUri = this.tumlUri + "/forwardToLookup?lookupUri=" + lookupUri + '&qualifiedName=' + qualifiedName;
        }

        $.ajax({
            url: postUri,
            type: AJAX_TYPE,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(overloadedPostData),
            success: function (result) {
//                var endTimeBeforeUpdateGrids = new Date().getTime();
//                console.log("Time taken in millis for server call before update drop down = " + (endTimeBeforeUpdateGrids - startTime));

                //For one lookup
                if (Array.isArray(result)) {
                    for (var j = 0; j < result.length; j++) {
                        var r = result[j];
                        for (var i = 0; i < r.data.length; i++) {
                            var item = r.data[i];
                            if (item.tmpId !== undefined && item.tmpId !== null) {
                                item.id = item.tmpId;
                            }
                        }
                    }
                    loadDataCallback(result);
                } else {
                    //Make sure id's are replaced with tmpId where needed
                    for (var i = 0; i < result.data.length; i++) {
                        var item = result.data[i];
                        if (item.tmpId !== undefined && item.tmpId !== null) {
                            item.id = item.tmpId;
                        }
                    }
                    loadDataCallback(result.data);
                }
//                endTimeBeforeUpdateGrids = new Date().getTime();
//                console.log("Time taken in millis for server call after  update drop down = " + (endTimeBeforeUpdateGrids - startTime));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
            }
        });
    }

})
    (jQuery);
