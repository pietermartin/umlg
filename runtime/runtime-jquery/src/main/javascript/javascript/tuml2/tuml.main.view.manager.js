(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            MainViewManager: MainViewManager
        }
    });

    function MainViewManager(leftMenuManager) {

        var self = this;
        var tabContainer;
        var tumlManyViewManager;
        var tumlOneViewManager;
        var isOne;

        function init() {

            tumlManyViewManager = new Tuml.TumlManyViewManager();
            tumlManyViewManager.onPutSuccess.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPutSuccess fired');
                self.onPutSuccess.notify(args, e, self);
                if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                    alert('update the tree!');
                }
            });
            tumlManyViewManager.onPutFailure.subscribe(function(e, args) {
                self.onPutFailure.notify(args, e, self);
            });
            tumlManyViewManager.onPostSuccess.subscribe(function(e, args) {
                self.onPostSuccess.notify(args, e, self);
                if (args.data[0].meta.to.qualifiedName === 'tumllib::org::tuml::query::Query') {
                    var metaDataNavigatingTo = args.data[0].meta.to;
                    var metaDataNavigatingFrom = args.data[0].meta.from;
                    var contextVertexId = retrieveVertexId(args.tumlUri);
                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                }
            });
            tumlManyViewManager.onPostFailure.subscribe(function(e, args) {
                self.onPostFailure.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteSuccess.subscribe(function(e, args) {
                self.onDeleteSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteFailure.subscribe(function(e, args) {
                self.onDeleteFailure.notify(args, e, self);
            });
            tumlManyViewManager.onCancel.subscribe(function(e, args) {
                self.onCancel.notify(args, e, self);
            });
            tumlManyViewManager.onSelfCellClick.subscribe(function(e, args) {
                self.onSelfCellClick.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickLink.subscribe(function(e, args) {
                self.onContextMenuClickLink.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickDelete.subscribe(function(e, args) {
                self.onContextMenuClickDelete.notify(args, e, self);
            });

            tumlOneViewManager = new Tuml.TumlOneViewManager();
            tumlOneViewManager.onPutOneSuccess.subscribe(function(e, args) {
                var metaDataNavigatingTo = args.data[0].meta.to;
                var metaDataNavigatingFrom = args.data[0].meta.from;
                var contextVertexId = retrieveVertexId(args.tumlUri);
                if (metaDataNavigatingFrom === undefined) {
                    //Put directly on the resouce
                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data.id);
                } else {
                    //Put on the to one property
                    //First param is contextMetaDataFrom second contextMetaDataTo
                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data.id);
                }
                self.onPutOneSuccess.notify(args, e, self);
            });
            tumlOneViewManager.onPutOneFailure.subscribe(function(e, args) {
                self.onPutOneFailure.notify(args, e, self);
            });
            tumlOneViewManager.onPostOneSuccess.subscribe(function(e, args) {
                var metaDataNavigatingTo = args.data[0].meta.to;
                var metaDataNavigatingFrom = args.data[0].meta.from;
                if (metaDataNavigatingFrom === undefined) {
                    alert('metaDataNaviging is undefined, this should never happen!!');
                } else {
                    //First param is contextMetaDataFrom second contextMetaDataTo
                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, args.data[0].data.id);
                    self.onPostOneSuccess.notify(args, e, self);
                }
            });
            tumlOneViewManager.onPostOneFailure.subscribe(function(e, args) {
                self.onPostOneFailure.notify(args, e, self);
            });
        }

        function openQuery(tumlUri, oclExecuteUri, qualifiedName, name, queryEnum, queryString) {
            if (isOne === undefined) {
                alert('can not open the query as isOne is undefined!');
            } else if (isOne) {
                tumlOneViewManager.openQuery(tumlUri, oclExecuteUri, qualifiedName, name, queryEnum, queryString);
            } else {
                tumlManyViewManager.openQuery(tumlUri, oclExecuteUri, qualifiedName, name, queryEnum, queryString);
            }
        }

        function refresh(tumlUri, result) {
            var qualifiedName = result[0].meta.qualifiedName;
            var metaDataNavigatingTo = result[0].meta.to;
            var metaDataNavigatingFrom = result[0].meta.from;
            var propertyNavigatingTo = (metaDataNavigatingFrom == undefined ? null : findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom));
            if (propertyNavigatingTo != null && (propertyNavigatingTo.oneToMany || propertyNavigatingTo.manyToMany)) {
                //Property is a many
                isOne = false;
                recreateTabContainer();
                var contextVertexId = retrieveVertexId(tumlUri);
                leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                tumlManyViewManager.refresh(tumlUri, result, propertyNavigatingTo);
                tumlOneViewManager.clear();
            } else {
                //Property is a one
                isOne = true;
                if (result[0].data !== null) {
                    recreateTabContainer();
                    qualifiedName = result[0].meta.qualifiedName;
                    var contextVertexId = result[0].data.id;
                    //If property is a one then there is n navigating from
                    leftMenuManager.refresh(metaDataNavigatingTo, metaDataNavigatingTo, contextVertexId);
                    tumlOneViewManager.refresh(tumlUri, result);
                    tumlManyViewManager.clear();
                } else {
                    recreateTabContainer();
                    qualifiedName = result[0].meta.qualifiedName;
                    var contextVertexId = retrieveVertexId(tumlUri);
                    leftMenuManager.refresh(metaDataNavigatingFrom, metaDataNavigatingTo, contextVertexId);
                    tumlOneViewManager.refresh(tumlUri, result);
                    tumlManyViewManager.clear();
                }
            }
            $('#ui-layout-center-heading').children().remove();
            $('#ui-layout-center-heading').append($('<span />').text(qualifiedName));
            tabContainer.tabs({border: false, onClose:function(title, index){  
                if (isOne) {
                    tumlOneViewManager.closeQuery(title, index);
                } else {
                    tumlManyViewManager.closeQuery(title, index);
                }
            }, onSelect: function(title, index){
                leftMenuManager.refreshQueryMenu(title);
            }});
            //This is the layout
            $('body').layout().resizeAll();
            return true;
        }

        function findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom) {
            if (metaDataNavigatingFrom  == undefined) {
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
            tabContainer = $('<div />', {id: 'tab-container', class: 'easyui-tabs' }).appendTo('.ui-layout-center');
        }

        //Public api
        $.extend(this, {
            "TumlMainViewManagerVersion": "1.0.0",
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
            "onPutOneFailure": new Tuml.Event(),
            "onPostOneSuccess": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "refresh": refresh,
            "openQuery": openQuery
        });

        init();
    }
})(jQuery);
