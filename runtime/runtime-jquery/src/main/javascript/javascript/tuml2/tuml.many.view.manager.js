(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlManyViewManager: TumlManyViewManager
        }
    });

    function TumlManyViewManager() {

        var self = this;
        var tumlTabViewManagers = [];

        function init() {
        }

        function clear() {
            tumlTabViewManagers = [];
        }
        
        function refresh(tumlUri, result, propertyNavigatingTo) {
            tumlTabViewManagers = [];
            //A tab is created for every element in the array,
            //i.e. for every concrete subset of the many property
            for (i = 0; i < result.length; i++) {
                if (result[i].meta.from !== undefined) {
                    var metaForData = result[i].meta.to;
                    var tabContainer = $('#tab-container');
                    var tabDiv = $('<div />', {id: metaForData.name, title: metaForData.name}).appendTo(tabContainer);
                    var tumlTabViewManager = new Tuml.TumlTabViewManager({propertyNavigatingTo: propertyNavigatingTo, many: true, one: false, query: false}, tumlUri, result[i].meta.qualifiedName, metaForData.name);
                    tumlTabViewManager.onPutSuccess.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onPutSuccess fired');
                        self.onPutSuccess.notify(args, e, self);
                    });
                    tumlTabViewManager.onPutFailure.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onPutFailure fired');
                        self.onPutFailure.notify(args, e, self);
                    });
                    tumlTabViewManager.onPostSuccess.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onPostSuccess fired');
                        self.onPostSuccess.notify(args, e, self);
                    });
                    tumlTabViewManager.onPostFailure.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onPostFailure fired');
                        self.onPostFailure.notify(args, e, self);
                    });
                    tumlTabViewManager.onDeleteSuccess.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onDeleteSuccess fired');
                        self.onDeleteSuccess.notify(args, e, self);
                    });
                    tumlTabViewManager.onDeleteFailure.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onDeleteFailure fired');
                        self.onDeleteFailure.notify(args, e, self);
                    });
                    tumlTabViewManager.onCancel.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onCancel fired');
                        self.onCancel.notify(args, e, self);
                    });
                    tumlTabViewManager.onSelfCellClick.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onSelfCellClick fired');
                        self.onSelfCellClick.notify(args, e, self);
                    });
                    tumlTabViewManager.onContextMenuClickLink.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onContextMenuClickLink fired');
                        self.onContextMenuClickLink.notify(args, e, self);
                    });
                    tumlTabViewManager.onContextMenuClickDelete.subscribe(function(e, args) {
                        console.log('TumlManyViewManager onContextMenuClickDelete fired');
                        self.onContextMenuClickDelete.notify(args, e, self);
                    });
                    tumlTabViewManagers.push(tumlTabViewManager);
                    //tumlTabViewManager.createTab();
                } else {
                    alert('what is this about!!!');
                }
            }
            //Grids must be created after the tabs have been created.
            for (i = 0; i < result.length; i++) {
                if (result[i].meta.from !== undefined) {
                    var metaForData = result[i].meta.to;
                    for (j = 0; j < tumlTabViewManagers.length; j++) {
                        if (tumlTabViewManagers[j].tabDivName === metaForData.name) {
                            tumlTabViewManagers[j].createGrid(result[i]);
                        }
                    }
                }
            }
        }

        function closeQuery(title, index) {
            tumlTabViewManagers.splice(index, 1);
        }

        function openQuery(tumlUri, oclExecuteUri, qualifiedName, tabDivName, queryEnum, queryString) {
            //Check is there is already a tab open for this query
            var tumlTabViewManagerQuery; 
            var tabIndex = 0;
            for (j = 0; j < tumlTabViewManagers.length; j++) {
                if (tumlTabViewManagers[j].oneManyOrQuery.query && tumlTabViewManagers[j].tabDivName == tabDivName) {
                    tumlTabViewManagerQuery = tumlTabViewManagers[j];
                    tabIndex = j;
                    break;
                }
            }
            if (tumlTabViewManagerQuery === undefined) {
                $('#tab-container').tabs('add', {title: tabDivName, content: '<div id="'+tabDivName+'" />', closable: true});
                var tumlTabViewManager = new Tuml.TumlTabViewManager({many: false, one: false, query: true}, tumlUri, qualifiedName, tabDivName);
                tumlTabViewManagers.push(tumlTabViewManager);
                tumlTabViewManager.createQuery(oclExecuteUri, queryEnum, queryString);
            } else {
                //Just make the tab active
                $('#tab-container').tabs('select', tabIndex);
            }

        }

        function clear() {
            for (i = 0; i < tumlTabViewManagers.length; i++) {
                tumlTabViewManagers[i].clear();
            }
            tumlTabViewManagers.length = 0;
        }

        //Public api
        $.extend(this, {
            "TumlManyViewManagerVersion": "1.0.0",
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
            "refresh": refresh,
            "openQuery": openQuery,
            "closeQuery": closeQuery,
            "clear": clear
        });

        init();
    }

})(jQuery);
