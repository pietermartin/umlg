(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlMainViewManager: TumlMainViewManager
        }
    });

    function TumlMainViewManager(uiManager, leftMenuManager) {

        var self = this;
        this.uiManager = uiManager;
        var oclExecuteUri;
        var instanceQueryTumlUri;
        var classQueryTumlUri;
        var contextVertexId;
        var contextChanged = true;
        var propertyNavigatingTo = null;
        var tumlUri = null;

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
                            var tumlTabViewManager = this.addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup: false, forManyComponent: false, isOne: true, forCreation: false}, this.tabContainer);

                            postTabCreate(tumlTabViewManager, this.tabContainer, result[i], true, result[i].meta.to, isForCreation, self.tumlTabViewManagers.length - 1);

                            //reorder tabs, make sure new tabs are first
                            reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers);

                            break;
                        }
                    }
                } else {
                    //This is for creation of the one
                    qualifiedName = result[0].meta.qualifiedName;
                    var newContextVertexId = retrieveVertexId(tumlUri);
                    var savedTumlTabViewManagers = this.clearTabsOnAddOneOrMany(newContextVertexId);

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
                this.clearAllTabs();
                this.recreateTabContainer();
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
                tumlTabViewManagerQuery.onSelfCellClick.subscribe(function (e, args) {
                    self.onSelfCellClick.notify(args, e, self);
                });

            } else {
                //Just make the tab active
                tabContainer.tabs("option", "active", self.tumlTabViewManagers.indexOf(tumlTabViewManagerQuery));
            }
            return tumlTabViewManagerQuery;

        }

        this.recreateTabContainer = function () {
            var tabContainer = this.getTabContainer();
            if (tabContainer !== null) {
                tabContainer.remove();
            }
            var tabLayoutDiv = $('#tabs-layout');
            tabContainer = $('<div />', {id: 'tabs'}).appendTo(tabLayoutDiv);
            this.setTabContainer(tabContainer);
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
//                leftMenuManager.refreshQueryMenuCss(queryId, tabEnum);
                }
            });
        }


        this.onPutSuccess = new Tuml.Event();
        this.onPutFailure = new Tuml.Event();
        this.onPostSuccess = new Tuml.Event();
        this.onPostFailure = new Tuml.Event();
        this.onDeleteSuccess = new Tuml.Event();
        this.onDeleteFailure = new Tuml.Event();
        this.onCancel = new Tuml.Event();
        this.onSelfCellClick = new Tuml.Event();
        this.onContextMenuClickDelete = new Tuml.Event();

        this.onPutOneSuccess = new Tuml.Event();
        this.onPostOneSuccess = new Tuml.Event();
        this.onDeleteOneSuccess = new Tuml.Event();
        this.onPutOneFailure = new Tuml.Event();
        this.onPostOneFailure = new Tuml.Event();
        this.onPostInstanceQuerySuccess = new Tuml.Event();
        this.onPutInstanceQuerySuccess = new Tuml.Event();
        this.onPostClassQuerySuccess = new Tuml.Event();
        this.onPutClassQuerySuccess = new Tuml.Event();

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
            doSave(false);
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

        function doSave(commit) {
            var tumlTabManyViewManagers = getTumlTabManyViewManagers(commit);
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
                if (!commit) {
                    postUri = self.tumlUri + "?rollback=true";
                } else {
                    postUri = self.tumlUri;
                }

                $.ajax({
                    url: postUri,
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
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
                    contentType: "application/json",
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
                doSave(true);
            }).appendTo('#buttons');
            var $cancelButton = $('<button />').text('Cancel').click(function () {
                doCancel();
            }).appendTo('#buttons');
        }

        function refreshInternal(tumlUri, result, propertyNavigatingTo, isOne, forCreation) {
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            for (var i = 0; i < result.length; i++) {
                var tumlTabViewManager = self.addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup: false, forManyComponent: false, isOne: isOne, forCreation: forCreation}, self.tabContainer);
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

        Tuml.TumlTabContainerManager.call(this, this.tabContainer);

    }

    TumlMainViewManager.prototype = new Tuml.TumlTabContainerManager;

    TumlMainViewManager.prototype.refreshContext = function(tumlUri) {
        this.uiManager.refresh(tumlUri);
    }

    TumlMainViewManager.prototype.addNewRow = function (dataViewItems, event) {
        alert('hi there from TumlMainViewManager');
    }


})
    (jQuery);
