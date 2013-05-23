(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabQueryManager: TumlTabQueryManager
        }
    });

    function TumlTabQueryManager(instanceQueryUri, classQueryUri, queryId) {

        var tumlTabGridManager;
        if (queryId !== undefined) {
            this.queryId = queryId;
        }

        function init() {
            tumlTabGridManager = new Tuml.TumlQueryGridManager();
        }

        this.executeQuery = function () {
            var self = this;
            $.ajax({
                url: this.oclExecuteUri + '?ocl=' + $("#" + this.queryTabDivName + '_' + 'QueryString').val(),
                type: "GET",
                dataType: "json",
                contentType: "application/json",
                success: function (data) {
                    self.afterExecuteQuery(data);
                },
                error: function (jqXHR) {
                    $('#serverErrorMsg_' + this.queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.afterExecuteQuery = function (data) {
            tumlTabGridManager.refresh(data[0], this.queryTabDivName + '_' + 'OclResult');
            $('#serverErrorMsg_' + this.queryTabDivName).removeClass('server-error-msg');
            $('#serverErrorMsg_' + this.queryTabDivName).empty();
            $('#tab-container').tabs('resize');
        }

        this.saveToInstance = function() {
            var query = queryToJson(this.queryTabDivName, this.queryId);
//            query.qualifiedName = tumlTabGridManager.metaForData.qualifiedName;
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (post) {
                overloadedPostData.insert.push(query);
            } else {
                overloadedPostData.update.push(query);
            }
            $.ajax({
                url: instanceQueryUri,
                type: post ? "POST" : "PUT",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data, textStatus, jqXHR) {
                    var queryFromDb;
                    var queries = data[0].data;
                    for (var i = 0; i < queries.length; i++) {
                        queryFromDb = queries[i];
                        if (queryFromDb.name == query.name) {
                            break;
                        }
                    }
                    if (post) {
                        self.onPostInstanceQuerySuccess.notify(
                            {queryType: 'instanceQuery', query: queryFromDb, gridData: tumlTabGridManager.getResult()}, null, self);
                    } else {
                        self.onPutInstanceQuerySuccess.notify(
                            {queryType: 'instanceQuery', query: queryFromDb, gridData: tumlTabGridManager.getResult()}, null, self);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                }
            });
        }

        this.saveToClass = function() {

        }

        this.createQuery = function (queryTabDivName, oclExecuteUri, query, post) {
            var self = this;
            this.queryTabDivName = queryTabDivName;
            this.query = query;
            this.oclExecuteUri = oclExecuteUri;
            var queryTab = $('#' + queryTabDivName);

            var windowHeight = $('.ui-layout-center').height() - 90;

            var layoutDiv = $('<div />', {id: 'queryLayoutDiv', style: 'height: ' + windowHeight + 'px; width" 100%; overflow: hidden;'});
            layoutDiv.appendTo(queryTab);

            //Create the layout's center and north pane
            var northDiv = $('<div />', {class: 'query-north'});
            var centerDiv = $('<div />', {class: 'query-center'});
            northDiv.appendTo(layoutDiv);
            centerDiv.appendTo(layoutDiv);

            $('<div />', {id: 'serverErrorMsg_' + queryTabDivName}).appendTo(northDiv);

            //Outer div for entering ocl
            var oclOuter = $('<div />', {id: queryTabDivName + '_' + 'OclOuter', class: 'oclouter'});
            oclOuter.appendTo(northDiv);

            //Inner div for entering ocl and buttons
            var oclInner = $('<div />', {id: queryTabDivName + '_' + 'OclInner', class: 'oclinner'}).appendTo(oclOuter);
            var oclTextAreaDiv = $('<div />', {class: 'ocltextarea'}).appendTo(oclInner);
            $('<textarea />', {id: queryTabDivName + '_' + 'QueryString'}).text(query.queryString).appendTo(oclTextAreaDiv);
            var oclInnerButton = $('<div />', {id: queryTabDivName + '+' + 'OclInnerButton', class: 'oclinnerbutton'}).appendTo(oclInner);

            var oclExecuteButtonDiv = $('<div />', {class: "oclexecutebutton"}).appendTo(oclInnerButton);
            $('<button />', {id: queryTabDivName + '_' + 'ExecuteButton'}).click(
                function () {
                    self.executeQuery();
                }
            ).text('execute').appendTo(oclExecuteButtonDiv);

            var inputEditButtonDiv;
            var oclQueryNameInputDiv;
            var oclEditButtonDiv;
            if (isTumlLib) {
                inputEditButtonDiv = $('<div />', {class: 'oclinputeditbutton'}).appendTo(oclInnerButton);
                oclQueryNameInputDiv = $('<div />', {class: 'oclqueryname'}).appendTo(inputEditButtonDiv);
                $('<input >', {id: queryTabDivName + '_' + 'QueryName', type: 'text'}).val(query.name).appendTo(oclQueryNameInputDiv);
                oclEditButtonDiv = $('<div />', {class: "ocleditbutton"}).appendTo(inputEditButtonDiv);
            }

            if (isTumlLib && instanceQueryUri !== '') {
                $('<button />', {id: queryTabDivName + '_' + 'SaveButton'}).text('save to instance').click(
                    function () {
                        self.saveToInstance();
                    }).appendTo(oclEditButtonDiv);
            }

            if (isTumlLib && classQueryUri !== '') {
                $('<button />', {id: queryTabDivName + '_' + 'SaveButton'}).text('save to class').click(function () {
                    var query = queryToJson(queryTabDivName, self.queryId);
                    $.ajax({
                        url: classQueryUri,
                        type: post ? "POST" : "PUT",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(query),
                        success: function (data, textStatus, jqXHR) {
                            var queryFromDb;
                            var queries = data[0].data;
                            for (var i = 0; i < queries.length; i++) {
                                queryFromDb = queries[i];
                                if (queryFromDb.name == query.name) {
                                    break;
                                }
                            }
                            if (post) {
                                self.onPostClassQuerySuccess.notify(
                                    {queryType: 'classQuery', query: queryFromDb, gridData: tumlTabGridManager.getResult()}, null, self);
                            } else {
                                self.onPutClassQuerySuccess.notify(
                                    {queryType: 'classQuery', query: queryFromDb, gridData: tumlTabGridManager.getResult()}, null, self);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                        }
                    });
                }).appendTo(oclEditButtonDiv);
            }
            if (isTumlLib && !post) {
                $('<button />', {id: queryTabDivName + '_' + 'CancelButton'}).text('cancel').appendTo(oclEditButtonDiv);
                $('<button />', {id: queryTabDivName + '_' + 'DeleteButton'}).text('delete').click(function () {

                        var query = queryToJson(queryTabDivName, id);
                        $.ajax({
                            url: tumlUri,
                            type: "DELETE",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(query),
                            success: function (data, textStatus, jqXHR) {
                                self.onDeleteQuerySuccess.notify(
                                    {tumlUri: tumlUri,
                                        queryName: query.name,
                                        oclExecuteUri: oclExecuteUri,
                                        queryEnum: query.queryEnum,
                                        queryString: query.queryString,
                                        data: data}, null, self);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                            }
                        });


                    }
                ).appendTo(oclEditButtonDiv);
            }

            //Outer div for results
            var oclResult = $('<div />', {id: queryTabDivName + '_' + 'OclResult', class: 'oclresult'});
            oclResult.appendTo(centerDiv);
            if (query.data !== undefined && query.data !== null) {
                tumlTabGridManager.refresh(query.data, queryTabDivName + '_' + 'OclResult');
            }

            layoutDiv.layout({
                center__paneSelector: ".query-center",
                north__paneSelector: ".query-north",
                north__size: 125,

                onresize_end: function () {
                    //Resize the textarea
                    var northHeight = $('.query-north').height() - 15;
                    $('.oclinner textarea').height(northHeight);
                    return true;
                }
            });

        }

        function queryToJson(queryTabDivName, id) {
            var query = {};
            query.name = $("#" + queryTabDivName + '_' + 'QueryName').val();
            query.queryString = $("#" + queryTabDivName + '_' + 'QueryString').val();
            query.queryEnum = "OCL";
            if (id !== undefined) {
                query.id = id;
            }
            return query;
        }

        //Public api
        $.extend(this, {
            "TumlTabQueryManagerVersion": "1.0.0",
            //These events are propagated from the grid
            "onPutInstanceQuerySuccess": new Tuml.Event(),
            "onPostInstanceQuerySuccess": new Tuml.Event(),
            "onPutClassQuerySuccess": new Tuml.Event(),
            "onPostClassQuerySuccess": new Tuml.Event(),
            "onPutQueryFailure": new Tuml.Event(),
            "onPostQueryFailure": new Tuml.Event(),
            "onDeleteQuerySuccess": new Tuml.Event(),
            "onDeleteSuccess": new Tuml.Event(),
            "onDeleteFailure": new Tuml.Event(),
            "onCancel": new Tuml.Event(),
            "onSelfCellClick": new Tuml.Event()
        });

        init();
    }

})(jQuery);
