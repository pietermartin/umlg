(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabDiagramManager: TumlTabDiagramManager
        }
    });

    function TumlTabDiagramManager(parentTumlTabViewManager) {

        this.tumlTabViewManager = parentTumlTabViewManager;

        //Public api
        $.extend(this, {
            "TumlTabDiagramManagerVersion": "1.0.0"
        });

        this.createDiagram = function (diagramTabDivName, url, type) {
            var diagramTab = $('#' + diagramTabDivName);
            //create a panel with a header and body.
            //header contains the a form for executing and saving the query
            //body contains a layout manager. at the top is the query text and at the bottom the results
            var diagramPanel = $('<div />', {class: 'umlg-diagram-panel panel panel-default'}).appendTo(diagramTab);
            url = '/' + tumlModelName + '/diagram?path=' + url;
            if (type == 'SVG' || type == 'svg') {
                diagramPanel.load(url);
            } else {
                var image = $('<img />', {src: url}).appendTo(diagramPanel);
            }
        }

    }

})(jQuery);
