(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            TumlManyViewManager:TumlManyViewManager
        }
    });

    function TumlManyViewManager(leftMenuManager) {

        var self = this;
        var tumlTabViewManagers = [];
        var tabContainer;
        var contextVertexId;
        var oclExecuteUri;
        var instanceQueryTumlUri;
        var classQueryTumlUri;
        var contextVertexId;
        var contextChanged = true;

        function init() {
        }

        function refresh(tumlUri, result) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            var propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many

                var newContextVertexId = retrieveVertexId(tumlUri);
                var savedTumlTabViewManagers = clearTabsOnAddOneOrMany(newContextVertexId);

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
                            var savedTumlTabViewManagers = clearTabsOnAddOneOrMany(newContextVertexId);

                            //If property is a one then there is n navigating from
                            leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, contextVertexId);
                            //Do not call refreshInternal as it creates all tabs for the meta data
                            addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup:false, forManyComponent:false, isOne:true, forCreation:false});

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
                instanceQueryTumlUri = "/"+tumlModelName+"/basetumlwithquerys/" + contextVertexId + "/instanceQuery";
            } else {
                instanceQueryTumlUri = '';
            }
            classQueryTumlUri = "/"+tumlModelName+"/classquery/" + contextVertexId + "/query";


            if (contextVertexId !== undefined && contextVertexId !== null && contextChanged) {
                //This is the default query tab, always open
                addDefaultQueryTab();
            }

            tabContainer.tabs("option", "active", 0);

            $('#ui-layout-center-heading').children().remove();
            $('#ui-layout-center-heading').append($('<span />').text(qualifiedName));
            $('body').layout().resizeAll();
        }

        function clearTabsOnAddOneOrMany(newContextVertexId) {
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
                self.clear();
                recreateTabContainer();
            } else {
                var tumlTabViewManagersToClose = [];
                //Remove property tabs only, query tabs remain for the context
                for (var i = 0; i < tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof  Tuml.TumlTabOneViewManager || tumlTabViewManager instanceof  Tuml.TumlTabManyViewManager) {
                        tumlTabViewManagersToClose.push(tumlTabViewManager);
                    }
                }
                for (var i = 0; i < tumlTabViewManagersToClose.length; i++) {
                    closeTab(tumlTabViewManagersToClose[i]);
                }
            }
            //Save current tabs to help with reordering 2 lines down
            var savedTumlTabViewManagers = [];
            for (var i = 0; i < tumlTabViewManagers.length; i++) {
                var tumlTabViewManager = tumlTabViewManagers[i];
                savedTumlTabViewManagers.push(tumlTabViewManager);
            }
            return savedTumlTabViewManagers;
        }

        function reorderTabsAfterAddOneOrMany(savedTumlTabViewManagers) {
            for (var i = 0; i < savedTumlTabViewManagers.length; i++) {
                var tumlTabViewManager = savedTumlTabViewManagers[i];
                var index = tumlTabViewManagers.indexOf(tumlTabViewManager)
                tumlTabViewManagers.splice(index, 1);
            }
            for (var i = 0; i < savedTumlTabViewManagers.length; i++) {
                tumlTabViewManagers.push(savedTumlTabViewManagers[i]);
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
            addQueryTab(true, new Tuml.Query(-1, 'New Query', 'New Query Description', 'self.name', 'ocl'), reorder);
        }

        function recreateTabContainer() {
            if (tabContainer !== undefined) {
                tabContainer.remove();
            }
            tabContainer = $('<div />', {id:'tabs'}).appendTo('.ui-layout-center');
            tabContainer.append('<ul />');
            tabContainer.tabs();
            tabContainer.find(".ui-tabs-nav").sortable({
                axis:"x",
                stop:function () {
                    tabContainer.tabs("refresh");
                }
            });
            tabContainer.tabs({
                activate:function (event, ui) {
                    var queryId = $.data(ui.newPanel[0], 'queryId');
                    var tabEnum = $.data(ui.newPanel[0], 'tabEnum');
                    leftMenuManager.refreshQueryMenuCss(queryId, tabEnum);
                }
            });
        }

        function refreshInternal(tumlUri, result, propertyNavigatingTo, isOne, forCreation) {
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            for (var i = 0; i < result.length; i++) {
                addTab(tuml.tab.Enum.Properties, result[i], tumlUri, propertyNavigatingTo, {forLookup:false, forManyComponent:false, isOne:isOne, forCreation:forCreation});
            }
        }

        function addTab(tabEnum, result, tumlUri, propertyNavigatingTo, options) {
            var metaForData = result.meta.to;

            var tumlTabViewManager;
            if (options.isOne) {
                tumlTabViewManager = new Tuml.TumlTabOneViewManager(tabEnum, tabContainer,
                    {propertyNavigatingTo:propertyNavigatingTo,
                        many:!options.isOne,
                        one:options.isOne,
                        query:false,
                        forLookup:options.forLookup,
                        forManyComponent:options.forManyComponent,
                        forOneComponent:options.forOneComponent
                    }, tumlUri, result
                );
                tumlTabViewManager.onOneComponentSaveButtonSuccess.subscribe(function (e, args) {
                    tumlTabViewManager.getLinkedTumlTabViewManager().setValue(args.value);
                    closeTab(tumlTabViewManager);
                    tabContainer.tabs("enable", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                    tabContainer.tabs("option", "active", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                });
                tumlTabViewManager.onOneComponentCloseButtonSuccess.subscribe(function (e, args) {
                    closeTab(tumlTabViewManager);
                    tabContainer.tabs("enable", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                    tabContainer.tabs("option", "active", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                });
                tumlTabViewManager.onPutOneSuccess.subscribe(function (e, args) {
                    self.onPutOneSuccess.notify(args, e, self);
                });
                tumlTabViewManager.onPostOneSuccess.subscribe(function (e, args) {
                    self.clear();
                    self.onPostOneSuccess.notify(args, e, self);
                });
                tumlTabViewManager.onDeleteOneSuccess.subscribe(function (e, args) {
                    self.clear();
                    self.onDeleteOneSuccess.notify(args, e, self);
                });
                tumlTabViewManager.onClickOneComponent.subscribe(function (e, args) {
                    //Get the meta data
                    $.ajax({
                        url:args.property.tumlMetaDataUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (metaDataResponse, textStatus, jqXHR) {
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitleName);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                metaDataResponse[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:false, forOneComponent:true, isOne:true, forCreation:true}
                            );
                            tumlTabViewManager.setProperty(args.property);
                            tumlOneComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });
                tumlTabViewManager.onClickManyComponent.subscribe(function (e, args) {
                    //Get the meta data
                    $.ajax({
                        url:args.property.tumlMetaDataUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (metaDataResponse, textStatus, jqXHR) {
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitleName);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                metaDataResponse[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:true, isOne:false, forCreation:true}
                            );
                            tumlTabViewManager.setProperty(args.property);
                            tumlOneComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });
            } else {
                tumlTabViewManager = new Tuml.TumlTabManyViewManager(tabEnum, tabContainer,
                    {propertyNavigatingTo:propertyNavigatingTo,
                        many:!options.isOne,
                        one:options.isOne,
                        query:false,
                        forLookup:options.forLookup,
                        forManyComponent:options.forManyComponent
                    }, tumlUri, result
                );
                tumlTabViewManager.onSelectButtonSuccess.subscribe(function (e, args) {
                    tumlTabViewManager.getLinkedTumlTabViewManager().addItems(args.items);
                    //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                    $('#tab-container').tabs('close', args.tabName + " Select");
                    $('#' + args.tabName + "Lookup").remove();
                    tumlTabViewManager.getLinkedTumlTabViewManager().enableTab();
                });
                tumlTabViewManager.onManyComponentSaveButtonSuccess.subscribe(function (e, args) {
                    tumlTabViewManager.getLinkedTumlTabViewManager().setValue(args.value);
                    closeTab(tumlTabViewManager);
                    tabContainer.tabs("enable", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                    tabContainer.tabs("option", "active", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                });
                tumlTabViewManager.onManyComponentCloseButtonSuccess.subscribe(function (e, args) {
                    closeTab(tumlTabViewManager);
                    tabContainer.tabs("enable", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                    tabContainer.tabs("option", "active", tumlTabViewManagers.indexOf(tumlTabViewManager.getLinkedTumlTabViewManager()));
                });
                tumlTabViewManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                    //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                    $('#tab-container').tabs('close', args.tabName + " Select");
                    $('#' + args.tabName + "Lookup").remove();
                    tumlTabViewManager.getLinkedTumlTabViewManager().enableTab();
                });
                tumlTabViewManager.onAddButtonSuccess.subscribe(function (e, args) {
                    $('#tab-container').tabs('disableTab', metaForData.name);
                    var tumlLookupTabViewManager = addTab(
                        tuml.tab.Enum.Properties,
                        args.data,
                        args.tumlUri,
                        args.propertyNavigatingTo,
                        {forLookup:true, forManyComponent:false}
                    );
                    tumlLookupTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
                });
                tumlTabViewManager.onClickManyComponentCell.subscribe(function (e, args) {
                    //Get the meta data
                    $.ajax({
                        url:args.property.tumlMetaDataUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (result, textStatus, jqXHR) {
                            result[0].data = args.data;
                            var tumlManyComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                result[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:true, forOneComponent:false, isOne:false, forCreation:true}
                            );
                            tumlTabViewManager.setCell(args.cell);
                            tumlManyComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
                            tabContainer.tabs("disable", tumlTabViewManagers.indexOf(tumlTabViewManager));
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
                            alert('error getting ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                        }
                    });
                });

                tumlTabViewManager.onClickOneComponentCell.subscribe(function (e, args) {
                    console.log('TumlManyViewManager onClickOneComponentCell fired');
                    //Get the meta data
                    $.ajax({
                        url:args.property.tumlMetaDataUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (result, textStatus, jqXHR) {
                            if (args.data.length !== 0) {
                                result[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                tuml.tab.Enum.Properties,
                                result[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:false, forOneComponent:true, isOne:true, forCreation:true}
                            );
                            tumlTabViewManager.setCell(args.cell);
                            tumlOneComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
                            tabContainer.tabs("disable", tumlTabViewManagers.indexOf(tumlTabViewManager));
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
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

            tumlTabViewManagers.push(tumlTabViewManager);
            tumlTabViewManager.createTab();
            tabContainer.tabs("option", "active", tumlTabViewManagers.length - 1);
            tabContainer.tabs("refresh");

            // close icon: removing the tab on click
            $("#tabs span.ui-icon-close").live("click", function () {
                var panelId = $(this).closest("li").remove().attr("aria-controls");
                for (var j = 0; j < tumlTabViewManagers.length; j++) {
                    if (panelId === tumlTabViewManagers[j].tabDivName) {
                        closeTab(tumlTabViewManagers[j]);
                    }
                }
                tabContainer.tabs("refresh");
            });

            //Create the grid
            if (!options.isOne) {
                tumlTabViewManager.createGrid(result);
            } else {
                tumlTabViewManager.createOne(result.data[0], metaForData, options.forCreation);
            }

            return tumlTabViewManager;

        }

        function closeTab(tumlTabViewManager) {
            var index = tumlTabViewManagers.indexOf(tumlTabViewManager)
            tumlTabViewManagers[index].clear();
            tumlTabViewManagers.splice(index, 1);
        }

        function reorderTabs() {
            var tabsNav = tabContainer.find(".ui-tabs-nav");
            var first = true;
            for (var j = 0; j < tumlTabViewManagers.length; j++) {
                var li = $('#li' + tumlTabViewManagers[j].tabDivName);
                if (first) {
                    tabsNav.append(li);
                } else {
                    li.insertAfter(tabsNav);
                }
                tabsNav = li;
                first = false;
            }
            tabContainer.tabs("refresh");
        }

        function addQueryTab(post, query, reorder) {
            if (reorder === undefined) {
                reorder = true;
            }
            //Check is there is already a tab open for this query
            var tumlTabViewManagerQuery;
            var tabIndex = 0;
            for (var j = 0; j < tumlTabViewManagers.length; j++) {
                if (tumlTabViewManagers[j].tabDivName == query.getDivName()) {
                    tumlTabViewManagerQuery = tumlTabViewManagers[j];
                    tabIndex = j;
                    break;
                }
            }
            if (tumlTabViewManagerQuery === undefined) {
                if (query.queryType === undefined) {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.Properties, tabContainer, instanceQueryTumlUri, classQueryTumlUri, query.getDivName(), query.name, query.id);
                } else if (query.queryType === 'instanceQuery') {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.InstanceQueries, tabContainer, instanceQueryTumlUri, '', query.getDivName(), query.name, query.id);
                } else {
                    tumlTabViewManagerQuery = new Tuml.TumlTabQueryViewManager(tuml.tab.Enum.ClassQueries, tabContainer, '', classQueryTumlUri, query.getDivName(), query.name, query.id);
                }
                tumlTabViewManagerQuery.createTab();

                if (query.id === -1) {
                    tumlTabViewManagers.push(tumlTabViewManagerQuery);
                    reorderTabs();
                    tabContainer.tabs("option", "active", tumlTabViewManagers.length - 1);
                } else {
                    tumlTabViewManagers.splice(tumlTabViewManagers.length - 1, 0, tumlTabViewManagerQuery);
                    reorderTabs();
                    tabContainer.tabs("option", "active", tumlTabViewManagers.length - 2);
                }

                tumlTabViewManagerQuery.createQuery(oclExecuteUri, query, post);
                tumlTabViewManagerQuery.onPutInstanceQuerySuccess.subscribe(function (e, args) {
                    closeTab(tumlTabViewManagerQuery);
                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
                    leftMenuManager.refreshInstanceQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPostInstanceQuerySuccess.subscribe(function (e, args) {
                    var previousIndex = tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
                    closeTab(tumlTabViewManagerQuery);
                    addDefaultQueryTab(false);
                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));

                    //place it back at the previousIndex
                    var currentIndex = tumlTabViewManagers.indexOf(newTumlTabViewManager);
                    tumlTabViewManagers.splice(currentIndex, 1);
                    tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
                    tabContainer.tabs("option", "active", previousIndex);

                    leftMenuManager.refreshInstanceQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPutClassQuerySuccess.subscribe(function (e, args) {
                    closeTab(tumlTabViewManagerQuery);
                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));
                    leftMenuManager.refreshClassQuery(args.query.id);
                });
                tumlTabViewManagerQuery.onPostClassQuerySuccess.subscribe(function (e, args) {
                    var previousIndex = tumlTabViewManagers.indexOf(tumlTabViewManagerQuery);
                    closeTab(tumlTabViewManagerQuery);
                    addDefaultQueryTab(false);
                    var newTumlTabViewManager = addQueryTab(false, new Tuml.Query(args.query.id, args.query.name, args.query.name, args.query.queryString, args.query.queryEnum, args.gridData, args.queryType));

                    //place it back at the previousIndex
                    var currentIndex = tumlTabViewManagers.indexOf(newTumlTabViewManager);
                    tumlTabViewManagers.splice(currentIndex, 1);
                    tumlTabViewManagers.splice(previousIndex, 0, newTumlTabViewManager);
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
                tabContainer.tabs("option", "active", tumlTabViewManagers.indexOf(tumlTabViewManagerQuery));
            }
            return tumlTabViewManagerQuery;

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

        function clear() {
            for (var i = 0; i < tumlTabViewManagers.length; i++) {
                tumlTabViewManagers[i].clear();
            }
            tumlTabViewManagers.length = 0;
        }

        //Public api
        $.extend(this, {
            "TumlManyViewManagerVersion":"1.0.0",
            //These events are propogated from the grid
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

            "onPutOneSuccess":new Tuml.Event(),
            "onPostOneSuccess":new Tuml.Event(),
            "onDeleteOneSuccess":new Tuml.Event(),
            "onPutOneFailure":new Tuml.Event(),
            "onPostOneFailure":new Tuml.Event(),
            "onPostInstanceQuerySuccess":new Tuml.Event(),
            "onPutInstanceQuerySuccess":new Tuml.Event(),
            "onPostClassQuerySuccess":new Tuml.Event(),
            "onPutClassQuerySuccess":new Tuml.Event(),

            "refresh":refresh,
            "addQueryTab":addQueryTab,
            "closeTab":closeTab,
            "clear":clear
        });

        init();
    }

})
    (jQuery);
