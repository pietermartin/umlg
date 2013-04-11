(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlMainViewManager: TumlMainViewManager
        }
    });

    function TumlMainViewManager(leftMenuManager) {

        var self = this;
        var oclExecuteUri;
        var instanceQueryTumlUri;
        var classQueryTumlUri;
        var contextVertexId;
        var contextChanged = true;
        var propertyNavigatingTo = null;
        var tumlUri = null;

        function init() {
        }

        this.refresh = function (tumlUri, result) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            this.tumlUri = tumlUri;
            propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many

                var newContextVertexId = retrieveVertexId(tumlUri);
                var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                refreshInternal(tumlUri, result, propertyNavigatingTo, false);

                //reorder tabs, make sure new tabs are first
                reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);
            } else {
                //Property is a one
                var isForCreation = true;
                for (var i = 0; i < result.length; i++) {
                    if (result[i].data.length > 0) {
                        isForCreation = false;
                        break;
                    }
                }
                if (!isForCreation) {
                    //Only one element of the array contains data, i.e. for the return concrete type
                    for (var i = 0; i < result.length; i++) {
                        if (result[i].data.length > 0) {
                            metaDataNavigatingTo = result[i].meta.to;
                            qualifiedName = result[i].meta.qualifiedName;
                            var newContextVertexId = result[i].data[0].id;
                            var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

                            //If property is a one then there is n navigating from
                            leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, contextVertexId);
                            //Do not call refreshInternal as it creates all tabs for the meta data
                            var tumlTabViewManager = addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup: false, forManyComponent: false, isOne: true, forCreation: false}, tabContainer);

                            postTabCreate(tumlTabViewManager, tabContainer, result[i], true, result[i].meta.to, isForCreation, self.tumlTabViewManagers.length - 1);

                            //reorder tabs, make sure new tabs are first
                            reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);

                            break;
                        }
                    }
                } else {
                    //This is for creation of the one
                    qualifiedName = result[0].meta.qualifiedName;
                    var newContextVertexId = retrieveVertexId(tumlUri);
                    var savedTumlTabViewManagers = clearTabsOnAddOneOrMany(newContextVertexId);

                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                    refreshInternal(tumlUri, result, propertyNavigatingTo, true, true);

                    //reorder tabs, make sure new tabs are first
                    reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);

                }
            }

            oclExecuteUri = "/" + tumlModelName + "/" + contextVertexId + "/oclExecuteQuery";
            if (hasInstanceQuery(metaDataNavigatingTo, metaDataNavigatingFrom)) {
                instanceQueryTumlUri = "/" + tumlModelName + "/basetumlwithquerys/" + contextVertexId + "/instanceQuery";
            } else {
                instanceQueryTumlUri = '';
            }
            classQueryTumlUri = "/" + tumlModelName + "/classquery/" + contextVertexId + "/query";


            if (contextVertexId !== undefined && contextVertexId !== null && contextChanged) {
                //This is the default query tab, always open
                addDefaultQueryTab();
            }

            this.tabContainer.tabs("option", "active", 0);

            $('#validation-warning').children().remove();
            $('#navigation-qualified-name').children().remove();
            var propertyDescription = qualifiedName;
            if (propertyNavigatingTo !== undefined && propertyNavigatingTo !== null) {
                propertyDescription += '  -  ' + createPropertyDescriptionHeading(propertyNavigatingTo);
            }
            $('#navigation-qualified-name').append($('<span />').text(propertyDescription));
            addButtons();
            $('body').layout().resizeAll();
        }

        this.clearTabsOnAddOneOrMany = function(newContextVertexId) {
            if (newContextVertexId === undefined && contextVertexId === null) {
                contextChanged = true;
            } else if (contextVertexId === undefined && newContextVertexId === null) {
                contextChanged = true;
            } else if (contextVertexId === undefined && newContextVertexId === undefined) {
                contextChanged = true;
            } else {
                contextChanged = newContextVertexId !== contextVertexId;
            }
            contextVertexId = newContextVertexId;
            if (contextChanged) {
                Tuml.TumlTabContainerManager.prototype.clearAllTabs.call(this);
                Tuml.TumlTabContainerManager.prototype.recreateTabContainer.call(this);
            } else {
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
            for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = self.tumlTabViewManagers[i];
                savedTumlTabViewManagers.push(tumlTabViewManager);
            }
            return savedTumlTabViewManagers;
        }

        this.addQueryTab = function(post, query, reorder) {
            if (reorder === undefined) {
                reorder = true;
            }
            //Check is there is already a tab open for this query
            var tumlTabViewManagerQuery;
            var tabIndex = 0;
            for (var j = 0; j < this.tumlTabViewManagers.length; j++) {
                if (this.tumlTabViewManagers[j].tabDivName == query.getDivName()) {
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
                tumlTabViewManagerQuery.createTab();

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
                tumlTabViewManagerQuery.onPutInstanceQuerySuccess.subscribe(function (e, args) {
                    tumlTabViewManagerQuery.closeTab();
                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
                    leftMenuManager.refreshInstanceQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPostInstanceQuerySuccess.subscribe(function (e, args) {
                    var previousIndex = self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
                    tumlTabViewManagerQuery.closeTab();
                    addDefaultQueryTab(false);
                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));

                    //place it back at the previousIndex
                    var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
                    this.tumlTabViewManagers.splice(currentIndex, 1);
                    this.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
                    this.tabContainer.tabs("option", "active", previousIndex);

                    leftMenuManager.refreshInstanceQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPutClassQuerySuccess.subscribe(function (e, args) {
                    tumlTabViewManagerQuery.closeTab();
                    var newTumlTabViewManager = this.addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
                    leftMenuManager.refreshClassQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPostClassQuerySuccess.subscribe(function (e, args) {
                    var previousIndex = self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
                    closeTab(tumlTabViewManagerQuery);
                    addDefaultQueryTab(false);
                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));

                    //place it back at the previousIndex
                    var currentIndex = self.tumlTabViewManagers.indexOf(newTumlTabViewManager);
                    self.tumlTabViewManagers.splice(currentIndex, 1);
                    self.tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
                    tabContainer.tabs("option", "active", previousIndex);

                    leftMenuManager.refreshClassQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onDeleteQuerySuccess.subscribe(function (e, args) {
                    closeTab(tumlTabViewManagerQuery);
                    leftMenuManager.refreshQuery();
                });
                tumlTabViewManagerQuery.onContextMenuClickLink.subscribe(function (e, args) {
                    self.onContextMenuClickLink.notify(args, e, self);
                });
                tumlTabViewManagerQuery.onSelfCellClick.subscribe(function (e, args) {
                    self.onSelfCellClick.notify(args, e, self);
                });

            } else {
                //Just make the tab active
                tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery));
            }
            return tumlTabViewManagerQuery;

        }

        function createPropertyDescriptionHeading(propertyNavigatingTo) {
            var multiplicity;
            if (propertyNavigatingTo.upper == -1) {
                multiplicity = 'multiplicity: [' + propertyNavigatingTo.lower + '..*]';
            } else {
                multiplicity = 'multiplicity: [' + propertyNavigatingTo.lower + '..' + propertyNavigatingTo.upper + ']';
            }
            var unique = 'unique: ' + propertyNavigatingTo.unique;
            var ordered = 'ordered: ' + propertyNavigatingTo.ordered;
            //TODO
//            var derived = 'derived: ' + propertyNavigatingTo.derived;
            var association = 'association: ' + (propertyNavigatingTo.composite ? 'composite' : 'non composite');
            return multiplicity + ', ' + unique + ', ' + ordered + ', ' + association;
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

        function addDefaultQueryTab(reorder) {
            self.addQueryTab(true, new Tuml.Query(-1, 'New Query', 'New Query Description', 'self.name', 'ocl'), reorder);
        }

        function updateValidationWarningHeader() {
            $('#validation-warning').children().remove();
            var tumlTabManyViewManagers = getTumlTabManyViewManagers(false);
            var rowCount = 0;
            for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
                var dataView = tumlTabManyViewManagers[i].tumlTabGridManager.dataView;
                rowCount += dataView.getItems().length;
            }
            if (rowCount < propertyNavigatingTo.lower || (propertyNavigatingTo.upper !== -1 && rowCount > propertyNavigatingTo.upper)) {
                $('#validation-warning').append($('<span />').text(
                    'multiplicity falls outside the valid range [' + propertyNavigatingTo.lower + '..' + propertyNavigatingTo.upper + ']'));
            }
        }

        function validateMultiplicity(tumlTabManyViewManagers) {
            var rowCount = 0;
            for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
                if (!tumlTabManyViewManagers[i].oneManyOrQuery.forManyComponent) {
                    var dataView = tumlTabManyViewManagers[i].tumlTabGridManager.dataView;
                    rowCount += dataView.getItems().length;
                }
            }
            if (rowCount < propertyNavigatingTo.lower || (propertyNavigatingTo.upper !== -1 && rowCount > propertyNavigatingTo.upper)) {
                alert('multiplicity falls outside the valid range [' + propertyNavigatingTo.lower + '..' + propertyNavigatingTo.upper + ']');
                return false;
            } else {
                return true;
            }
        }

        function getTumlTabManyViewManagers(commitCurrentEdit) {
            var tumlTabManyViewManagers = [];
            for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = self.tumlTabViewManagers[i];
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

        function addNewRow() {
//            alert('hi there addNewRow');
            doSave(true);
//            var overloadedPostData = {};
//            overloadedPostData['insert'] = {qualifiedName: self.localMetaForData.qualifiedName};
//            $.ajax({
//                url: tumlUri + "?rollback=true",
//                type: "POST",
//                dataType: "json",
//                contentType: "json",
//                data: JSON.stringify(overloadedPostData),
//                success: function (data, textStatus, jqXHR) {
//                    //Cancel prevent validation from happening
//                    self.grid.getEditController().cancelCurrentEdit();
//                    self.dataView.addItem(data[0].data[0]);
//                    //This ensures the cell is in edit mode, i.e. the cursor is ready for typing
//                    self.grid.editActiveCell();
//                    self.onAddRowSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
//                },
//                error: function (jqXHR, textStatus, errorThrown) {
//                    $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
//                }
//            });
        }

        function doSave(rollback) {
            var tumlTabManyViewManagers = getTumlTabManyViewManagers(!rollback);
            var overloadedPostData = {};
            overloadedPostData['insert'] = [];
            overloadedPostData['update'] = [];
            overloadedPostData['delete'] = [];

            if (validateMultiplicity(tumlTabManyViewManagers)) {
                for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
                    if (!tumlTabManyViewManagers[i].oneManyOrQuery.forManyComponent) {
                        var dataView = tumlTabManyViewManagers[i].tumlTabGridManager.dataView;
                        overloadedPostData['insert'].push.apply(overloadedPostData['insert'], dataView.getNewItems());
                        overloadedPostData['update'].push.apply(overloadedPostData['update'], dataView.getUpdatedItems());
                        overloadedPostData['delete'].push.apply(overloadedPostData['delete'], dataView.getDeletedItems());
                    }
                }
                var postUri;
                if (rollback) {
                    postUri = self.tumlUri + "?rollback=true";
                } else {
                    postUri = self.tumlUri;
                }

                $.ajax({
                    url: postUri,
                    type: "POST",
                    dataType: "json",
                    contentType: "json",
                    data: JSON.stringify(overloadedPostData),
                    success: function (data, textStatus, jqXHR) {
//                        alert('onsavesuucess');
//                        self.onPostSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
//                        self.onPostFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
                    }
                });


            }

//            if (self.grid.getEditorLock().commitCurrentEdit()) {
//                if (self.validateMultiplicity()) {
//                    self.doSave();
//                }
//            } else {
//                alert("Commit failed on current active cell!");
//            }
//
//            var overloadedPostUri = this.propertyNavigatingTo.tumlOverloadedPostUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);
//
//            var overloadedPostData = {};
//            var validationErrors = true;
//            if (this.dataView.getNewItems().length > 0) {
//                var validationResults = this.validateNewItems(this.dataView.getNewItems());
//                if (validationResults.length == 0) {
//                    validationErrors = false;
//                    overloadedPostData['insert'] = this.dataView.getNewItems();
//                } else {
//                    var errorMsg = '\n';
//                    for (var i = 0; i < validationResults.length; i++) {
//                        errorMsg += validationResults[i].msg + '\n';
//                    }
//                    alert('Validation errors: ' + errorMsg);
//                }
//            }
//            if (!validationErrors) {
//                if (this.dataView.getUpdatedItems().length > 0) {
//                    overloadedPostData['update'] = this.dataView.getUpdatedItems();
//                }
//                if (this.dataView.getDeletedItems().length > 0) {
//                    overloadedPostData['delete'] = this.dataView.getDeletedItems();
//                }
//                $.ajax({
//                    url: overloadedPostUri,
//                    type: "POST",
//                    dataType: "json",
//                    contentType: "json",
//                    data: JSON.stringify(overloadedPostData),
//                    success: function (data, textStatus, jqXHR) {
//                        self.onDeleteSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
//                        self.onDeleteFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
//                    }
//                });
//            }
        }

        function doCancel() {
            if (self.grid.getEditorLock().commitCurrentEdit()) {
                $.ajax({
                    url: tumlUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
                    success: function (result, textStatus, jqXHR) {
                        //Only cancel this tab
                        for (var i = 0; i < result.length; i++) {
                            var metaForData = result[i].meta.to;
                            if (metaForData.name === self.localMetaForData.name) {
                                self.cancel(result[i].data);
                                return;
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                    }
                });
            }
        }

        function addButtons() {
            $('#buttons').children().remove();
            //Save button
            $('<button />').text('Save').click(function () {
                doSave(false);
            }).appendTo('#buttons');
            var $cancelButton = $('<button />').text('Cancel').click(function () {
                doCancel();
            }).appendTo('#buttons');
        }

        function refreshInternal(tumlUri, result, propertyNavigatingTo, isOne, forCreation) {
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            for (var i = 0; i < result.length; i++) {
                var tumlTabViewManager = addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup: false, forManyComponent: false, isOne: isOne, forCreation: forCreation}, self.tabContainer);
                postTabCreate(tumlTabViewManager, self.tabContainer, result[i], false, result[i].meta.to, false, self.tumlTabViewManagers.length - 1);
            }
        }

        function postTabCreate(tumlTabViewManager, tabContainer, result, isOne, metaForData, forCreation, activeIndex) {
            tumlTabViewManager.createTab();
            tabContainer.tabs("option", "active", activeIndex);
            //Create the grid
            if (!isOne) {
                tumlTabViewManager.createGrid(result);
            } else {
                tumlTabViewManager.createOne(result.data[0], metaForData, forCreation);
            }
        }

        function addTab(tabEnum, result, tumlUri, propertyNavigatingTo, options, tabContainer) {
            var metaForData = result.meta.to;

            var tumlTabViewManager;
            if (options.isOne) {
                tumlTabViewManager = new Tuml.TumlTabOneViewManager(tabEnum, tabContainer,
                    {propertyNavigatingTo: propertyNavigatingTo,
                        many: !options.isOne,
                        one: options.isOne,
                        query: false,
                        forLookup: options.forLookup,
                        forManyComponent: options.forManyComponent,
                        forOneComponent: options.forOneComponent
                    }, tumlUri, result
                );
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
                        contentType: "json",
                        success: function (metaDataResponse, textStatus, jqXHR) {
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitleName);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                metaDataResponse[0],
                                args.tumlUri,
                                args.property,
                                {forLookup: false, forManyComponent: false, forOneComponent: true, isOne: true, forCreation: true},
                                tumlTabViewManager.createSubTabContainer()
                            );
                            postTabCreate(tumlOneComponentTabViewManager, tabContainer, metaDataResponse[0], true, metaDataResponse[0].meta.to, false, self.tumlTabViewManagers.length - 1);
                            tumlTabViewManager.setProperty(args.property);
                            tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });
                tumlTabViewManager.onClickManyComponent.subscribe(function (e, args) {
                    //Get the meta data
                    $.ajax({
                        url: args.property.tumlMetaDataUri,
                        type: "GET",
                        dataType: "json",
                        contentType: "json",
                        success: function (metaDataResponse, textStatus, jqXHR) {
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitleName);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                metaDataResponse[0],
                                args.tumlUri,
                                args.property,
                                {forLookup: false, forManyComponent: true, isOne: false, forCreation: true}
                            );
                            postTabCreate(tumlOneComponentTabViewManager, tabContainer, metaDataResponse[0], true, metaDataResponse[0].meta.to, false, self.tumlTabViewManagers.length - 1);
                            tumlTabViewManager.setProperty(args.property);
                            tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });
            } else {
                if (options.forManyComponent) {
                    tumlTabViewManager = new Tuml.TumlTabManyComponentViewManager(tabEnum, tabContainer,
                        {propertyNavigatingTo: propertyNavigatingTo,
                            many: !options.isOne,
                            one: options.isOne,
                            query: false,
                            forLookup: options.forLookup,
                            forManyComponent: options.forManyComponent
                        }, tumlUri, result
                    );
                } else {
                    tumlTabViewManager = new Tuml.TumlTabManyViewManager(tabEnum, tabContainer,
                        {propertyNavigatingTo: propertyNavigatingTo,
                            many: !options.isOne,
                            one: options.isOne,
                            query: false,
                            forLookup: options.forLookup,
                            forManyComponent: options.forManyComponent
                        }, tumlUri, result
                    );
                    self.tumlTabViewManagers.push(tumlTabViewManager);
                }
                tumlTabViewManager.onSelectButtonSuccess.subscribe(function (e, args) {
                    tumlTabViewManager.getParentTumlTabViewManager().addItems(args.items);
                    //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                    $('#tab-container').tabs('close', args.tabName + " Select");
                    $('#' + args.tabName + "Lookup").remove();
                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
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
                    tabContainer.tabs("enable", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
                    tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManager.getParentTumlTabViewManager()));
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
                    postTabCreate(tumlLookupTabViewManager, tabContainer, args.data, true, args.data.meta.to, false, self.tumlTabViewManagers.length - 1);
                    tumlLookupTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                });
                tumlTabViewManager.onClickManyComponentCell.subscribe(function (e, args) {
                    //Get the meta data.
                    $.ajax({
                        url: args.property.tumlMetaDataUri,
                        type: "GET",
                        dataType: "json",
                        contentType: "json",
                        success: function (result, textStatus, jqXHR) {

                            for (var i = 0; i < result.length; i++) {
                                var subTabContainer = tumlTabViewManager.createOrReturnSubTabContainer();
                                result[i].data = args.data;
                                var tumlManyComponentTabViewManager = addTab(
                                    tuml.tab.Enum.Properties,
                                    result[i],
                                    args.tumlUri,
                                    args.property,
                                    {forLookup: false, forManyComponent: true, forOneComponent: false, isOne: false, forCreation: true},
                                    subTabContainer
                                );
                                tumlManyComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                                postTabCreate(tumlManyComponentTabViewManager, subTabContainer, result[i], false, result[i].meta.to, false, i);
                                tumlTabViewManager.setCell(args.cell);
                                tumlTabViewManager.addToChildTabViewManager(tumlManyComponentTabViewManager);
                            }
//                            tabContainer.tabs("disable", self.tumlTabViewManagers.indexOf(tumlTabViewManager));
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });

                });

                tumlTabViewManager.onClickOneComponentCell.subscribe(function (e, args) {
                    console.log('TumlMainViewManager onClickOneComponentCell fired');
                    //Get the meta data
                    $.ajax({
                        url: args.property.tumlMetaDataUri,
                        type: "GET",
                        dataType: "json",
                        contentType: "json",
                        success: function (result, textStatus, jqXHR) {
                            if (args.data.length !== 0) {
                                result[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                result[0],
                                args.tumlUri,
                                args.property,
                                {forLookup: false, forManyComponent: false, forOneComponent: true, isOne: true, forCreation: true}
                            );
                            tumlTabViewManager.setCell(args.cell);
                            tumlOneComponentTabViewManager.setParentTumlTabViewManager(tumlTabViewManager);
                            tabContainer.tabs("disable", self.tumlTabViewManagers.indexOf(tumlTabViewManager));
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });

                tumlTabViewManager.onSelfCellClick.subscribe(function (e, args) {
                    self.onSelfCellClick.notify(args, e, self);
                });
                tumlTabViewManager.onContextMenuClickLink.subscribe(function (e, args) {
                    self.onContextMenuClickLink.notify(args, e, self);
                });
                tumlTabViewManager.onContextMenuClickDelete.subscribe(function (e, args) {
                    self.onContextMenuClickDelete.notify(args, e, self);
                });
            }
            tumlTabViewManager.onAddNewRow.subscribe(function (e, args) {
                addNewRow();
            });
            tumlTabViewManager.onAddRowSuccess.subscribe(function (e, args) {
                updateValidationWarningHeader();
            });
            tumlTabViewManager.onRemoveRowSuccess.subscribe(function (e, args) {
                updateValidationWarningHeader();
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
                for (var j = 0; j < self.tumlTabViewManagers.length; j++) {
//                    if (panelId === self.tumlTabViewManagers[j].tabDivName) {
//                        closeTab(tumlTabViewManagers[j]);
//                    }
                }
                tumlTabViewManager.closeTab();

//                closeTab(tumlTabViewManager);
                tabContainer.tabs("refresh");
            });

            return tumlTabViewManager;

        }

        function reorderTabs() {
            var tabsNav = self.tabContainer.find(".ui-tabs-nav");
            var first = true;
            for (var j = 0; j < self.tumlTabViewManagers.length; j++) {
                var li = $('#li' + self.tumlTabViewManagers[j].tabDivName);
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

        TumlMainViewManager.prototype = new Tuml.TumlTabContainerManager;

        //Public api
        $.extend(this, {
            "TumlMainViewManager": "1.0.0",
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

            "onPutOneSuccess": new Tuml.Event(),
            "onPostOneSuccess": new Tuml.Event(),
            "onDeleteOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "onPostInstanceQuerySuccess": new Tuml.Event(),
            "onPutInstanceQuerySuccess": new Tuml.Event(),
            "onPostClassQuerySuccess": new Tuml.Event(),
            "onPutClassQuerySuccess": new Tuml.Event()

//            "refresh": refresh,
//            "addQueryTab": addQueryTab
        });

        init();

        Tuml.TumlTabContainerManager.call(this);

    }


})
    (jQuery);
