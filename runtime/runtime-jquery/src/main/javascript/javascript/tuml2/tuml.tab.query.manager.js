(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabQueryManager: TumlTabQueryManager
        }
    });

    function TumlTabQueryManager(tumlUri, queryTabDivName) {

        var self = this;
        var tumlTabGridManager;

        function init() {
            tumlTabGridManager = new Tuml.TumlTabGridManager();
        }

        function createQuery(oclExecuteUri, queryEnum, queryString) {
            var queryTab = $('#' + queryTabDivName);
            //Outer div for entering ocl
            var oclOuter = $('<div />', {id: queryTabDivName + '_' + 'OclOuter', class: 'oclouter'});
            oclOuter.appendTo(queryTab);
            
            //Inner div for entering ocl and buttons
            var oclInner = $('<div />', {id: queryTabDivName + '_' + 'OclInner', class: 'oclinner'}).appendTo(oclOuter);
            var oclTextAreaDiv = $('<div />', {class: 'ocltextarea'}).appendTo(oclInner);
            $('<textarea />', {id: 'oclquerytextfield'}).text(queryString).appendTo(oclTextAreaDiv);
            var oclInnerButton = $('<div />', {id: queryTabDivName + '+' + 'OclInnerButton', class: 'oclinnerbutton'}).appendTo(oclInner);

            var oclExecuteButtonDiv = $('<div />', {class: "oclexecutebutton"}).appendTo(oclInnerButton);
            $('<button />', {id: queryTabDivName + '_' + 'ExecuteButton'}).click(function() {
                $.ajax({
                    url: oclExecuteUri + '?ocl=' + $('#oclquerytextfield').val(),
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
                    success: function(data, textStatus, jqXHR) {
                        alert('create the grid');
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                    }
                });
            }).text('execute').appendTo(oclExecuteButtonDiv);

            var inputEditButtonDiv = $('<div />', {class: 'oclinputeditbutton'}).appendTo(oclInnerButton);
            
            var oclQueryNameInputDiv = $('<div />', {class: 'oclqueryname'}).appendTo(inputEditButtonDiv);
            $('<input >', {id: queryTabDivName + '_' + 'QueryName', type: 'text'}).val(queryTabDivName).appendTo(oclQueryNameInputDiv);

            var oclEditButtonDiv = $('<div />', {class: "ocleditbutton"}).appendTo(inputEditButtonDiv);
            
            $('<button />', {id: queryTabDivName + '_' + 'SaveButton'}).text('save').click(function() {
                $.ajax({
                    url: tumlUri,
                    type: "PUT",
                    dataType: "json",
                    contentType: "json",
                    data: JSON.stringify(dataView.getUpdatedItems()),
                    success: function(data, textStatus, jqXHR) {
                        self.onPutSuccess.notify({tumlUri: tumlUri, tabId: metaForData.name, data: data}, null, self);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        self.onPutFailure.notify({tumlUri: tumlUri, tabId: metaForData.name}, null, self);
                    }
                });
            }).appendTo(oclEditButtonDiv);
            $('<button />', {id: queryTabDivName + '_' + 'CancelButton'}).text('cancel').appendTo(oclEditButtonDiv);
            $('<button />', {id: queryTabDivName + '_' + 'DeleteButton'}).text('delete').appendTo(oclEditButtonDiv);

            //Outer div for results
            var oclResult = $('<div />', {id: queryTabDivName + '_' + 'OclResult', class: 'oclresult'}).text('results');
            oclResult.appendTo(queryTab);
        }

        //Public api
        $.extend(this, {
            "TumlTabQueryManagerVersion": "1.0.0",
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
            "createQuery": createQuery
        });

        init();
    }

})(jQuery);
