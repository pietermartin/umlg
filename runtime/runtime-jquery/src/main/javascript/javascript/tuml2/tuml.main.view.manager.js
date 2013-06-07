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
        this.contextVertexId = -1;
        var contextChanged = true;
        this.globalOneToOneIndex = new GlobalOneToOneIndex();
        this.qualifiedName = null;

        this.refreshQueryMenuCss = function(queryId, accordionIndex) {
            leftMenuManager.refreshQueryMenuCss(queryId, accordionIndex);
        }

        this.refresh = function (tumlUri, result) {
            this.qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            this.tumlUri = tumlUri;
            //propertyNavigatingTo is null when viewing a one
            this.propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(this.qualifiedName, metaDataNavigatingFrom));
            this.tabContainerProperty = this.propertyNavigatingTo;
            if (this.propertyNavigatingTo != null && (this.propertyNavigatingTo.oneToMany || this.propertyNavigatingTo.manyToMany)) {
                //Property is a many

                var newContextVertexId = retrieveVertexId(tumlUri);

                //Check if we coming from a one
                var wasOne = false;
                for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[j];
                    if (tumlTabViewManager instanceof  Tuml.TumlTabOneViewManager) {
                        wasOne = true;
                    }
                }

                var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                //If the context changed then a new container would have been created with buttons
                if (!contextChanged && wasOne) {
                    this.addButtons();
                }

                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, this.contextVertexId);
                refreshInternal(tumlUri, result, false);

                //reorder tabs, make sure new tabs are first
                reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);
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
                    //Only element of the array contains data, i.e. for the return concrete type
                    for (var i = 0; i < result.length; i++) {
                        if (result[i].data !== null) {
                            metaDataNavigatingTo = result[i].meta.to;
                            this.qualifiedName = result[i].meta.qualifiedName;
                            var newContextVertexId = result[i].data.id;
                            var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                            //If property is a one then there is n navigating from
                            leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, this.contextVertexId);
                            //Do not call refreshInternal as it creates all tabs for the meta data
                            var tumlTabViewManager = this.createTabContainer(tuml.tab.Enum.Properties, result[i], tumlUri, {forLookup: false, forManyComponent: false, isOne: true, forCreation: false}, this.propertyNavigatingTo);
                            this.addToTumlTabViewManagers(tumlTabViewManager);
                            tumlTabViewManager.createTab(result[i], isForCreation);
                            //reorder tabs, make sure new tabs are first
                            reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);
                        }
                    }
                } else {
                    //This is for creation of the one
                    this.qualifiedName = result[0].meta.qualifiedName;
                    var newContextVertexId = retrieveVertexId(tumlUri);
                    var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, this.contextVertexId);
                    refreshInternal(tumlUri, result, true, true);

                    //reorder tabs, make sure new tabs are first
                    reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);

                }
            }
            if (this.contextVertexId == null) {
                oclExecuteUri = "/" + tumlModelName + "/oclExecuteQuery";
            } else {
                oclExecuteUri = "/" + tumlModelName + "/" + this.contextVertexId + "/oclExecuteQuery";
            }
            if (hasInstanceQuery(metaDataNavigatingTo, metaDataNavigatingFrom)) {
                instanceQueryTumlUri = "/" + tumlModelName + "/basetumlwithquerys/" + this.contextVertexId + "/instanceQuery";
            } else {
                instanceQueryTumlUri = '';
            }
            classQueryTumlUri = "/" + tumlModelName + "/classquery/" + this.contextVertexId + "/query";


            if (this.contextVertexId !== -1 && contextChanged) {
                //This is the default query tab, always open
                addDefaultQueryTab();
            }

            this.tumlTabViewManagers[0].open = true;
            this.tabContainer.tabs("option", "active", 0);

            this.updateNavigationHeader(this.qualifiedName);
            $('body').layout().resizeAll();
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
                success: function (result, textStatus, jqXHR) {
                    if (commit) {
                        self.updateTabsForResultAfterCommit(result);
                    } else {
                        var endTimeBeforeUpdateGrids = new Date().getTime();
                        console.log("Time taken in millis for server call before update grids = " + (endTimeBeforeUpdateGrids - startTime));
                        self.updateTabsForResultAfterRollback(result);
                        endTimeBeforeUpdateGrids = new Date().getTime();
                        console.log("Time taken in millis for server call after  update grids = " + (endTimeBeforeUpdateGrids - startTime));
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
                    tumlTabViewManager.clearArraysAfterCommit();
                }
            }
        }

        this.updateTabsForResultAfterRollback = function (result) {
            for (var i = 0; i < result.length; i++) {
                var resultForTab = result[i];
                var metaForData = resultForTab.meta.to;
                for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[j];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                        tumlTabViewManager.beginUpdate();
                        //TODO use qualified name somehow
                        //Line up he result with the correct tab
                        if (tumlTabViewManager.tabId == metaForData.name) {
                            for (var k = 0; k < resultForTab.data.length; k++) {
                                //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
                                //Go through all the properties, for each composite property set the id = tmpId
                                this.setComponentIdToTmpId(resultForTab.data[k]);
                                tumlTabViewManager.updateGridAfterRollback(resultForTab.data[k]);
                            }
                        }
                        tumlTabViewManager.endUpdate(true);
                    } else {
                        if (tumlTabViewManager.tabId == metaForData.name) {
                            tumlTabViewManager.beginUpdate();
                            this.setComponentIdToTmpId(resultForTab.data);
                            tumlTabViewManager.updateGridAfterRollback(resultForTab.data);
                            tumlTabViewManager.endUpdate(true);
                        }
                    }
                }
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
                }
            }

            //Save current tabs to help with reordering 2 lines down
            var savedTumlTabViewManagers = [];
            for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = this.tumlTabViewManagers[i];
                savedTumlTabViewManagers.push(tumlTabViewManager);
            }
            return savedTumlTabViewManagers;
        }

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
                if (query.queryType === undefined) {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.Properties, this.tabContainer, instanceQueryTumlUri, classQueryTumlUri, query.getDivName(), query.name, query.id);
                } else if (query.queryType === 'instanceQuery') {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.InstanceQueries, this.tabContainer, instanceQueryTumlUri, '', query.getDivName(), query.name, query.id);
                } else {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.ClassQueries, this.tabContainer, '', classQueryTumlUri, query.getDivName(), query.name, query.id);
                }
                tumlTabViewManagerQuery.createTab(post);
                tumlTabViewManagerQuery.parentTabContainerManager = this;

                if (query.id === -1) {
                    this.tumlTabViewManagers.push(tumlTabViewManagerQuery);
                    reorderTabs();
                    this.tabContainer.tabs("option", "active", this.tumlTabViewManagers.length - 1);
                } else {
                    this.tumlTabViewManagers.splice(this.tumlTabViewManagers.length - 1, 0, tumlTabViewManagerQuery);
                    reorderTabs();
                    this.tabContainer.tabs("option", "active", this.tumlTabViewManagers.length - 2);
                }

                tumlTabViewManagerQuery.createQuery(oclExecuteUri, query, post);
//                tumlTabViewManagerQuery.onPutInstanceQuerySuccess.subscribe(function (e, args) {
//                    tumlTabViewManagerQuery.closeTab();
//                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
//                    leftMenuManager.refreshInstanceQuery(args.query.id);
//                });
//                tumlTabViewManagerQuery.onPostInstanceQuerySuccess.subscribe(function (e, args) {
//                    var previousIndex = self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
//                    tumlTabViewManagerQuery.closeTab();
//                    addDefaultQueryTab(false);
//                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
//
//                    //place it back at the previousIndex
//                    var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
//                    this.tumlTabViewManagers.splice(currentIndex, 1);
//                    this.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
//                    this.tabContainer.tabs("option", "active", previousIndex);
//
//                    leftMenuManager.refreshInstanceQuery(args.query.id);
//                });
//                tumlTabViewManagerQuery.onPutClassQuerySuccess.subscribe(function (e, args) {
//                    tumlTabViewManagerQuery.closeTab();
//                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
//                    leftMenuManager.refreshClassQuery(args.query.id);
//                });
//                tumlTabViewManagerQuery.onPostClassQuerySuccess.subscribe(function (e, args) {
//                    var previousIndex = self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
//                    closeTab(tumlTabViewManagerQuery);
//                    addDefaultQueryTab(false);
//                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
//
//                    //place it back at the previousIndex
//                    var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
//                    self.tumlTabViewManagers.splice(currentIndex, 1);
//                    self.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
//                    tabContainer.tabs("option", "active", previousIndex);
//
//                    leftMenuManager.refreshClassQuery(args.query.id);
//                });
//                tumlTabViewManagerQuery.onDeleteQuerySuccess.subscribe(function (e, args) {
//                    closeTab(tumlTabViewManagerQuery);
//                    leftMenuManager.refreshQuery();
//                });
//                tumlTabViewManagerQuery.onSelfCellClick.subscribe(function (e, args) {
//                    self.onSelfCellClick.notify(args, e, self);
//                });

            } else {
                //Just make the tab active
                this.tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery));
            }
            return tumlTabViewManagerQuery;

        }

        this.afterSaveInstance = function(args, previousIndex) {
            addDefaultQueryTab(false);
            var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            //place it back at the previousIndex
            var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
            this.tumlTabViewManagers.splice(currentIndex, 1);
            this.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
            this.tabContainer.tabs("option", "active", previousIndex);
            leftMenuManager.refreshInstanceQuery(args.query.id);
        }

        this.afterUpdateInstance = function(args) {
            this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
            leftMenuManager.refreshInstanceQuery(args.query.id);
        }

        this.afterDeleteInstance = function(args) {
            leftMenuManager.deleteInstanceQuery(args.query.id);
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

        function addDefaultQueryTab() {
            self.addQueryTab(true, new Tuml.Query(-1, 'New Query', 'New Query Description', 'self.name', 'ocl'));
        }

        function validateMultiplicity(tumlTabManyViewManagers) {
            var rowCount = 0;
            for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
                var tumlTabManyViewManager = tumlTabManyViewManagers[i];
                //Need to travel down components and validate their multiplicities
                if (tumlTabManyViewManager instanceof Tuml.TumlTabManyViewManager && !tumlTabManyViewManager.oneManyOrQuery.forManyComponent) {

                    var dataView = tumlTabManyViewManager.tumlTabGridManager.dataView;

                    rowCount += dataView.getItems().length;
                } else {
                    return true;
                }
            }
            if (rowCount < self.propertyNavigatingTo.lower || (self.propertyNavigatingTo.upper !== -1 && rowCount > self.propertyNavigatingTo.upper)) {
                alert('multiplicity falls outside the valid range [' + self.propertyNavigatingTo.lower + '..' + self.propertyNavigatingTo.upper + ']');
                return false;
            } else {
                return true;
            }
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
//            self.addButtons();
        }

        function reorderTabs() {
            var tabsNav = self.tabContainer.find(".ui-tabs-nav");
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
            self.tabContainer.tabs("refresh");
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
                if (tumlTabViewManager.tumlTabGridManager.metaForData.qualifiedName !== qualifiedName) {
                    tumlTabViewManager.updateOneForUpdate(id, displayName, fieldName, one);
                }
            }
        }
    }

    TumlMainViewManager.prototype.doCancel = function () {
        this.enableButtons();
        var self = this;
        Slick.GlobalEditorLock.cancelCurrentEdit();
        $.ajax({
            url: this.tumlUri,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            success: function (result, textStatus, jqXHR) {
                self.refresh(self.tumlUri, result);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
            }
        });
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
            success: function (result, textStatus, jqXHR) {
                var endTimeBeforeUpdateGrids = new Date().getTime();
                console.log("Time taken in millis for server call before update drop down = " + (endTimeBeforeUpdateGrids - startTime));

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
                endTimeBeforeUpdateGrids = new Date().getTime();
                console.log("Time taken in millis for server call after  update drop down = " + (endTimeBeforeUpdateGrids - startTime));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
            }
        });
    }

})
    (jQuery);
