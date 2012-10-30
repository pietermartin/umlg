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

        function init() {

            tumlManyViewManager = new Tuml.TumlManyViewManager();
            tumlManyViewManager.onPutSuccess.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPutSuccess fired');
                self.onPutSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onPutFailure.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPutFailure fired');
                self.onPutFailure.notify(args, e, self);
            });
            tumlManyViewManager.onPostSuccess.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPostSuccess fired');
                self.onPostSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onPostFailure.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPostFailure fired');
                self.onPostFailure.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteSuccess.subscribe(function(e, args) {
                console.log('TumlMainViewManager onDeleteSuccess fired');
                self.onDeleteSuccess.notify(args, e, self);
            });
            tumlManyViewManager.onDeleteFailure.subscribe(function(e, args) {
                console.log('TumlMainViewManager onDeleteFailure fired');
                self.onDeleteFailure.notify(args, e, self);
            });
            tumlManyViewManager.onCancel.subscribe(function(e, args) {
                console.log('TumlMainViewManager onCancel fired');
                self.onCancel.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickLink.subscribe(function(e, args) {
                console.log('TumlMainViewManager onContextMenuClickLink fired');
                self.onContextMenuClickLink.notify(args, e, self);
            });
            tumlManyViewManager.onContextMenuClickDelete.subscribe(function(e, args) {
                console.log('TumlMainViewManager onContextMenuClickDelete fired');
                self.onContextMenuClickDelete.notify(args, e, self);
            });

            tumlOneViewManager = new Tuml.TumlOneViewManager();
            tumlOneViewManager.onPutOneSuccess.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPutOneSuccess fired');
                self.onPutOneSuccess.notify(args, e, self);
            });
            tumlOneViewManager.onPutOneFailure.subscribe(function(e, args) {
                console.log('TumlMainViewManager onPutOneFailure fired');
                self.onPutOneFailure.notify(args, e, self);
            });
        }

        function refresh(tumlUri, result) {
            var qualifiedName;
            if (result instanceof Array && result[0].meta.length === 3) {   
                recreateTabContainer();
                //Property is a many
                qualifiedName = result[0].meta[0].qualifiedName;
                var contextMetaData = result[0].meta[1];
                var contextVertexId = tumlUri.match(/\d+/);
                leftMenuManager.refresh(contextMetaData, contextVertexId);
                tumlManyViewManager.refresh(tumlUri, result);
            } else {
                //Property is a one
                var response;
                if (result instanceof Array) {
                    response = result[0];
                } else {
                    response = result;
                }
                if (response.data !== null) {
                    recreateTabContainer();
                    qualifiedName = response.meta[0].qualifiedName;
                    var contextMetaData = response.meta[1];
                    var contextVertexId = response.data.id;
                    leftMenuManager.refresh(contextMetaData, contextVertexId);
                    tumlOneViewManager.refresh(tumlUri, result);
                } else {
                    alert('The properties value is null. \nIt can not be navigated to.');
                }
            }
            $('#ui-layout-center-heading').children().remove();
            $('#ui-layout-center-heading').append($('<span />').text(qualifiedName));
            tabContainer.easytabs({animate: false});
        }

        function recreateTabContainer() {
            //Create the tab container with its ul
            //The tabsContainer must be created here, not in init, it don't wanna work otherwise
            if (tabContainer !== undefined) {
                tabContainer.remove();
            }
            tabContainer = $('<div />', {id: 'tab-container', class: 'tab-container' }).appendTo('.ui-layout-center');
            $('<ul />', {id: 'tabsul', class: 'etabs'}).appendTo(tabContainer);
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
            "onContextMenuClickLink": new Tuml.Event(),
            "onContextMenuClickDelete": new Tuml.Event(),
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "refresh": refresh
        });

        init();
    }
})(jQuery);
