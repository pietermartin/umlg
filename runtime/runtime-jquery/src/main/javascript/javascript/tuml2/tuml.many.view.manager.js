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

        function init() {
        }

        function refresh(tumlUri, result) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            var propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            recreateTabContainer();
            tabContainer.tabs({border:false, onClose:function (title, index) {
            }, onSelect:function (title, index) {
                leftMenuManager.refreshQueryMenu(title);
            }});
            var contextVertexId;
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many
                contextVertexId = retrieveVertexId(tumlUri);
                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                refreshInternal(tumlUri, result, propertyNavigatingTo, false);
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
                            contextVertexId = result[i].data[0].id;
                            //If property is a one then there is n navigating from
                            leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, contextVertexId);
                            refreshInternal(tumlUri, result, propertyNavigatingTo, true, false);
                            break;
                        }
                    }
                } else {
                    //This is for creation of the one
                    qualifiedName = result[0].meta.qualifiedName;
                    contextVertexId = retrieveVertexId(tumlUri);
                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                    refreshInternal(tumlUri, result, propertyNavigatingTo, true, true);

                }
            }

            addQueryTab(true, "/restAndJson/basetumlwithquerys/" + contextVertexId + "/query", "/restAndJson/" + contextVertexId + "/oclExecuteQuery", "NewQuery", "New Query", "ocl", "self.name");

            tumlTabViewManagers[0].selectTab();

            $('#ui-layout-center-heading').children().remove();
            $('#ui-layout-center-heading').append($('<span />').text(qualifiedName));
            $('body').layout().resizeAll();
        }

        function recreateTabContainer() {
            if (tabContainer !== undefined) {
                tabContainer.remove();
            }
            tabContainer = $('<div />', {id:'tab-container', class:'easyui-tabs' }).appendTo('.ui-layout-center');
        }

        function refreshInternal(tumlUri, result, propertyNavigatingTo, isOne, forCreation) {
            tumlTabViewManagers = [];
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            for (var i = 0; i < result.length; i++) {
                addTab(result[i], tumlUri, propertyNavigatingTo, {forLookup:false, forManyComponent:false, isOne:isOne, forCreation:forCreation});
            }
        }

        function addTab(result, tumlUri, propertyNavigatingTo, options) {
            var metaForData = result.meta.to;

            var tumlTabViewManager;
            if (options.isOne) {
                tumlTabViewManager = new Tuml.TumlTabOneViewManager(
                    {propertyNavigatingTo:propertyNavigatingTo,
                        many:!options.isOne,
                        one:options.isOne,
                        query:false,
                        forLookup:options.forLookup,
                        forManyComponent:options.forManyComponent,
                        forOneComponent:options.forOneComponent
                    }, tumlUri, result
                );
                tumlTabViewManager.onPutOneSuccess.subscribe(function (e, args) {
                    self.onPutOneSuccess.notify(args, e, self);
                });
                tumlTabViewManager.onPostOneSuccess.subscribe(function (e, args) {
                    self.onPostOneSuccess.notify(args, e, self);
                });
                tumlTabViewManager.onClickOneComponent.subscribe(function (e, args) {
                    //Get the meta data
                    $.ajax({
                        url:args.property.tumlMetaDataUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (metaDataResponse, textStatus, jqXHR) {
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitle);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
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
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitle);
                            if (args.data !== null) {
                                metaDataResponse[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
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
                tumlTabViewManager = new Tuml.TumlTabManyViewManager(
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
                tumlTabViewManager.onSelectCancelButtonSuccess.subscribe(function (e, args) {
                    //Closing the tab fires closeTab event which removes the tumlTabViewManager from the array
                    $('#tab-container').tabs('close', args.tabName + " Select");
                    $('#' + args.tabName + "Lookup").remove();
                    tumlTabViewManager.getLinkedTumlTabViewManager().enableTab();
                });
                tumlTabViewManager.onAddButtonSuccess.subscribe(function (e, args) {
                    $('#tab-container').tabs('disableTab', metaForData.name);
                    var tumlLookupTabViewManager = addTab(
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
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitle);
                            result[0].data = args.data;
                            var tumlManyComponentTabViewManager = addTab(
                                result[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:true, forOneComponent:false, isOne:false, forCreation:true}
                            );
                            tumlTabViewManager.setCell(args.cell);
                            tumlManyComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
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
                            $('#tab-container').tabs('disableTab', tumlTabViewManager.tabTitle);
                            if (args.data.length !== 0) {
                                result[0].data = args.data;
                            }
                            var tumlOneComponentTabViewManager = addTab(
                                result[0],
                                args.tumlUri,
                                args.property,
                                {forLookup:false, forManyComponent:false, forOneComponent:true, isOne:true, forCreation:true}
                            );
                            tumlTabViewManager.setCell(args.cell);
                            tumlOneComponentTabViewManager.setLinkedTumlTabViewManager(tumlTabViewManager);
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

            tumlTabViewManager.createTab();
            tumlTabViewManagers.push(tumlTabViewManager);

            //Create the grid
            if (!options.isOne) {
                tumlTabViewManager.createGrid(result);
            } else {
                tumlTabViewManager.createOne(result.data[0], metaForData, options.forCreation);
            }

            return tumlTabViewManager;

        }

        function closeTab(title, index) {
            tumlTabViewManagers[index].clear();
            tumlTabViewManagers.splice(index, 1);
        }

        function addQueryTab(post, tumlUri, oclExecuteUri, tabDivName, tabTitle, queryEnum, queryString) {
            //Check is there is already a tab open for this query
            var tumlTabViewManagerQuery;
            var tabIndex = 0;
            for (var j = 0; j < tumlTabViewManagers.length; j++) {
                if (tumlTabViewManagers[j].tabDivName == tabDivName) {
                    tumlTabViewManagerQuery = tumlTabViewManagers[j];
                    tabIndex = j;
                    break;
                }
            }
            if (tumlTabViewManagerQuery === undefined) {
                var tumlTabViewManager = new Tuml.TumlTabQueryViewManager(post, tumlUri, tabDivName, tabTitle);
                tumlTabViewManager.createTab();
                tumlTabViewManagers.push(tumlTabViewManager);
                tumlTabViewManager.createQuery(oclExecuteUri, queryEnum, queryString);
                tumlTabViewManager.onPutQuerySuccess.subscribe(function (e, args) {
                    self.onPutQuerySuccess.notify(args, e, self);
                });
                tumlTabViewManager.onPostQuerySuccess.subscribe(function (e, args) {
                    self.onPostQuerySuccess.notify(args, e, self);
                });
            } else {
                //Just make the tab active
                $('#tab-container').tabs('select', tabIndex);
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
            "onPutOneFailure":new Tuml.Event(),
            "onPostOneFailure":new Tuml.Event(),
            "onPostQuerySuccess":new Tuml.Event(),
            "onPutQuerySuccess":new Tuml.Event(),

            "refresh":refresh,
            "addQueryTab":addQueryTab,
            "closeTab":closeTab,
            "clear":clear
        });

        init();
    }

})(jQuery);
