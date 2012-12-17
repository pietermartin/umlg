(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            MainViewManager:MainViewManager
        }
    });

    function MainViewManager(leftMenuManager) {

        var self = this;
        var tabContainer;
        var tumlManyViewManager;
        var isOne;

        function init() {

            tumlManyViewManager = new Tuml.TumlManyViewManager();
            tumlManyViewManager.onPutSuccess.subscribe(function (e, args) {
                console.log('TumlMainViewManager onPutSuccess fired');
                self.onPutSuccess.notify(args, e, self);
                if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                    alert('update the tree!');
                }
            });
            tumlManyViewManager.onPutFailure.subscribe(function (e, args) {
                self.onPutFailure.notify(args, e, self);
            });
            tumlManyViewManager.onPostSuccess.subscribe(function (e, args) {
                self.onPostSuccess.notify(args, e, self);
                if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                    var metaDataNavigatingTo = args.data[0].meta.to;
                    var metaDataNavigatingFrom = args.data[0].meta.from;
                    var contextVertexId = retrieveVertexId(args.tumlUri);
                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                }
            });
            tumlManyViewManager.onPostFailure.subscribe(function (e, args) {
                self.onPostFailure.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteSuccess.subscribe(function (e, args) {
                self.onDeleteSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteFailure.subscribe(function (e, args) {
                self.onDeleteFailure.notify(args, e, self);
            });
            tumlManyViewManager.onCancel.subscribe(function (e, args) {
                self.onCancel.notify(args, e, self);
            });
            tumlManyViewManager.onSelfCellClick.subscribe(function (e, args) {
                self.onSelfCellClick.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickLink.subscribe(function (e, args) {
                self.onContextMenuClickLink.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickDelete.subscribe(function (e, args) {
                self.onContextMenuClickDelete.notify(args, e, self);
            });

            tumlManyViewManager.onPutOneSuccess.subscribe(function (e, args) {
//                var metaDataNavigatingTo = args.data[0].meta.to;
//                var metaDataNavigatingFrom = args.data[0].meta.from;
//                var contextVertexId = retrieveVertexId(args.tumlUri);
//                if (metaDataNavigatingFrom === undefined) {
                    //Put directly on the resource
//                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data[0].id);
//                } else {
                    //Put on the to one property
                    //First param is contextMetaDataFrom second contextMetaDataTo
//                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data[0].id);
//                }
                refresh(args.tumlUri, args.data);
                self.onPutOneSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onPostOneSuccess.subscribe(function (e, args) {
//                var metaDataNavigatingTo = args.data[0].meta.to;
//                var metaDataNavigatingFrom = args.data[0].meta.from;
//                if (metaDataNavigatingFrom === undefined) {
//                    alert('metaDataNavigating is undefined, this should never happen!!');
//                } else {
//                    //First param is contextMetaDataFrom second contextMetaDataTo
//                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data.id);
//                    self.onPostOneSuccess.notify(args, e, self);
//                    refresh(args.tumlUri, args.data);
//                }
                refresh(args.tumlUri, args.data);
            });
        }

        function openQuery(tumlUri, oclExecuteUri, qualifiedName, name, queryEnum, queryString) {
            tumlManyViewManager.openQuery(tumlUri, oclExecuteUri, qualifiedName, name, queryEnum, queryString);
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

            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many
                isOne = false;
                var contextVertexId = retrieveVertexId(tumlUri);
                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                tumlManyViewManager.refresh(tumlUri, result, propertyNavigatingTo, isOne);
            } else {
                //Property is a one
                isOne = true;
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
                            var contextVertexId = result[i].data[0].id;
                            //If property is a one then there is n navigating from
                            leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, contextVertexId);
                            tumlManyViewManager.refresh(tumlUri, result, propertyNavigatingTo, isOne, false);
                            break;
                        }
                    }
                } else {
                    //This is for creation of the one
                    qualifiedName = result[0].meta.qualifiedName;
                    var contextVertexId = retrieveVertexId(tumlUri);
                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                    tumlManyViewManager.refresh(tumlUri, result, propertyNavigatingTo, isOne, true);

                }
            }
            $('#ui-layout-center-heading').children().remove();
            $('#ui-layout-center-heading').append($('<span />').text(qualifiedName));

            $('body').layout().resizeAll();
            return true;
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

        function recreateTabContainer() {
            if (tabContainer !== undefined) {
                tabContainer.remove();
            }
            tabContainer = $('<div />', {id:'tab-container', class:'easyui-tabs' }).appendTo('.ui-layout-center');
        }

        //Public api
        $.extend(this, {
            "TumlMainViewManagerVersion":"1.0.0",
            //These events are propagated from the grid
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
            "onPutOneFailure":new Tuml.Event(),
            "onPostOneSuccess":new Tuml.Event(),
            "onPostOneFailure":new Tuml.Event(),
            "refresh":refresh,
            "openQuery":openQuery
        });

        init();
    }
})(jQuery);
