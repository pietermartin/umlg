(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            TumlTabQueryManager:TumlTabQueryManager
        }
    });

    function TumlTabQueryManager(post, tumlUri) {

        var self = this;
        var tumlTabGridManager;

        function init() {
            tumlTabGridManager = new Tuml.TumlTabGridManager();
        }

        function createQuery(queryTabDivName, oclExecuteUri, queryName, queryEnum, queryString) {
            var queryTab = $('#' + queryTabDivName);

            $('<div />', {id:'serverErrorMsg_' + queryTabDivName}).appendTo(queryTab);

            //Outer div for entering ocl
            var oclOuter = $('<div />', {id:queryTabDivName + '_' + 'OclOuter', class:'oclouter'});
            oclOuter.appendTo(queryTab);

            //Inner div for entering ocl and buttons
            var oclInner = $('<div />', {id:queryTabDivName + '_' + 'OclInner', class:'oclinner'}).appendTo(oclOuter);
            var oclTextAreaDiv = $('<div />', {class:'ocltextarea'}).appendTo(oclInner);
            $('<textarea />', {id:queryTabDivName + '_' + 'QueryString'}).text(queryString).appendTo(oclTextAreaDiv);
            var oclInnerButton = $('<div />', {id:queryTabDivName + '+' + 'OclInnerButton', class:'oclinnerbutton'}).appendTo(oclInner);

            var oclExecuteButtonDiv = $('<div />', {class:"oclexecutebutton"}).appendTo(oclInnerButton);
            $('<button />', {id:queryTabDivName + '_' + 'ExecuteButton'}).click(function () {
                $.ajax({
                    url:oclExecuteUri + '?ocl=' + $("#" + queryTabDivName + '_' + 'QueryString').val(),
                    type:"GET",
                    dataType:"json",
                    contentType:"json",
                    success:function (data, textStatus, jqXHR) {
                        alert('create the grid');
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                    }
                });
            }).text('execute').appendTo(oclExecuteButtonDiv);

            var inputEditButtonDiv = $('<div />', {class:'oclinputeditbutton'}).appendTo(oclInnerButton);

            var oclQueryNameInputDiv = $('<div />', {class:'oclqueryname'}).appendTo(inputEditButtonDiv);
            $('<input >', {id:queryTabDivName + '_' + 'QueryName', type:'text'}).val(queryName).appendTo(oclQueryNameInputDiv);

            var oclEditButtonDiv = $('<div />', {class:"ocleditbutton"}).appendTo(inputEditButtonDiv);

            $('<button />', {id:queryTabDivName + '_' + 'SaveButton'}).text('save').click(function () {
                var query = queryToJson(queryTabDivName);
                $.ajax({
                    url:tumlUri,
                    type:post ? "POST" : "PUT",
                    dataType:"json",
                    contentType:"json",
                    data:JSON.stringify(query),
                    success:function (data, textStatus, jqXHR) {
                        if (post) {
                            self.onPostQuerySuccess.notify({tumlUri:tumlUri, tabId:queryTabDivName, data:data}, null, self);
                        } else {
                            self.onPutQuerySuccess.notify(
                                {tumlUri:tumlUri,
                                    queryName:query.name,
                                    oclExecuteUri:oclExecuteUri,
                                    queryEnum:query.queryEnum,
                                    queryString:query.queryString,
                                    data:data}, null, self);
                        }
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                    }
                });
            }).appendTo(oclEditButtonDiv);
            if (!post) {
                $('<button />', {id:queryTabDivName + '_' + 'CancelButton'}).text('cancel').appendTo(oclEditButtonDiv);
                $('<button />', {id:queryTabDivName + '_' + 'DeleteButton'}).text('delete').appendTo(oclEditButtonDiv);
            }

            //Outer div for results
            var oclResult = $('<div />', {id:queryTabDivName + '_' + 'OclResult', class:'oclresult'}).text('results');
            oclResult.appendTo(queryTab);
        }

        function queryToJson(queryTabDivName) {
            var query = {};
            query.name = $("#" + queryTabDivName + '_' + 'QueryName').val();
            query.queryString = $("#" + queryTabDivName + '_' + 'QueryString').val();
            query.queryEnum = "OCL";
            return query;
        }

        //Public api
        $.extend(this, {
            "TumlTabQueryManagerVersion":"1.0.0",
            //These events are propogated from the grid
            "onPutQuerySuccess":new Tuml.Event(),
            "onPostQuerySuccess":new Tuml.Event(),
            "onPutQueryFailure":new Tuml.Event(),
            "onPostQueryFailure":new Tuml.Event(),
            "onDeleteSuccess":new Tuml.Event(),
            "onDeleteFailure":new Tuml.Event(),
            "onCancel":new Tuml.Event(),
            "onSelfCellClick":new Tuml.Event(),
            "onContextMenuClickLink":new Tuml.Event(),
            "onContextMenuClickDelete":new Tuml.Event(),
            "createQuery":createQuery
        });

        init();
    }

})(jQuery);
