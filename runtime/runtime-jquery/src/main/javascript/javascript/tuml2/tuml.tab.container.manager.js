(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabContainerManager: TumlTabContainerManager
        }
    });

    function TumlTabContainerManager() {

        this.tumlTabViewManagers = [];
        this.tabContainer = null;

    }

    TumlTabContainerManager.prototype.closeTab = function () {
        var index = this.tumlTabViewManagers.indexOf(tumlTabViewManager)
        this.tumlTabViewManagers[index].clear();
        this.tumlTabViewManagers.splice(index, 1);
    }

    TumlTabContainerManager.prototype.recreateTabContainer = function () {
        if (this.tabContainer !== undefined) {
            this.tabContainer.remove();
        }
        var tabLayoutDiv = $('#tabs-layout');
        this.tabContainer = $('<div />', {id: 'tabs'}).appendTo(tabLayoutDiv);
        this.tabContainer.append('<ul />');
        this.tabContainer.tabs();
        this.tabContainer.find(".ui-tabs-nav").sortable({
            axis: "x",
            stop: function () {
                tabContainer.tabs("refresh");
            }
        });
        this.tabContainer.tabs({
            activate: function (event, ui) {
                var queryId = $.data(ui.newPanel[0], 'queryId');
                var tabEnum = $.data(ui.newPanel[0], 'tabEnum');
//                leftMenuManager.refreshQueryMenuCss(queryId, tabEnum);
            }
        });
    }

})(jQuery);
