(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabQueryManager: TumlTabQueryManager
        }
    });

    function TumlTabQueryManager(parentTumlTabViewManager, instanceQueryUri, classQueryUri, queryId) {

        var tumlQueryGridManager;
        var codeMirror;
        var querySelect;
        this.instanceQueryUri = instanceQueryUri;
        this.classQueryUri = classQueryUri;
        if (queryId !== undefined) {
            this.queryId = queryId;
        }

        this.tumlTabViewManager = parentTumlTabViewManager;

        function init() {
            tumlQueryGridManager = new Tuml.TumlQueryGridManager();
        }

        this.executeQuery = function () {
            var self = this;
            $.ajax({
                url: this.oclExecuteUri + '?query=' + codeMirror.getValue() + '&type=' + querySelect.val(),
                type: "GET",
                contentType: "application/json",
                success: function (data) {
                    self.afterExecuteQuery(data);
                },
                error: function (jqXHR) {
                    $('#serverErrorMsg_' + self.queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.afterExecuteQuery = function (data) {
            if (Array.isArray(data)) {
                tumlQueryGridManager.refresh(data[0], this.queryTabDivName + '_' + 'OclResult', true);
            } else {
                var outerDivForResults = $('#' + this.queryTabDivName + '_' + 'OclResult');
                outerDivForResults.children().remove();
                var textAreaResult = $('<textarea />', {id: 'queryResultId'});
                textAreaResult.text(data).appendTo(outerDivForResults);
                CodeMirror.fromTextArea(textAreaResult[0], {mode: 'text/x-less', readOnly: true});
            }
            $('#serverErrorMsg_' + this.queryTabDivName).removeClass('server-error-msg');
            $('#serverErrorMsg_' + this.queryTabDivName).empty();
            $('#tab-container').tabs('resize');
        }

        this.saveToInstance = function (post) {
            var self = this;
            var query = queryToJson(this.queryTabDivName, this.queryId);
            query.qualifiedName = 'umlglib::org::umlg::query::InstanceQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (post) {
                overloadedPostData.insert.push(query);
            } else {
                overloadedPostData.update.push(query);
            }
            $.ajax({
                url: this.instanceQueryUri,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data) {
                    var queryFromDb;
                    var queries = data[0].data;
                    for (var i = 0; i < queries.length; i++) {
                        queryFromDb = queries[i];
                        if (queryFromDb.name == query.name) {
                            break;
                        }
                    }
                    if (post) {
                        self.afterSaveInstance({queryType: 'instanceQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
                    } else {
                        self.afterUpdateInstance({queryType: 'instanceQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg_' + this.queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                }
            });
        }

        this.deleteQuery = function () {
            var self = this;
            var query = queryToJson(this.queryTabDivName, this.queryId);
            var isInstanceQuery = this.instanceQueryUri !== '' ? true : false;
            query.qualifiedName = isInstanceQuery ? 'umlglib::org::umlg::query::InstanceQuery' : 'umlglib::org::umlg::query::ClassQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            overloadedPostData.delete.push(query);
            $.ajax({
                url: isInstanceQuery ? this.instanceQueryUri : this.classQueryUri,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data, textStatus, jqXHR) {
                    if (isInstanceQuery) {
                        self.afterDeleteInstance({queryType: 'instanceQuery', query: query, gridData: tumlQueryGridManager.getResult()});
                    } else {
                        self.afterDeleteClassQuery({queryType: 'classQuery', query: query, gridData: tumlQueryGridManager.getResult()});
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.cancelQuery = function () {
            codeMirror.setValue(this.query.queryString);
            $('#' + this.queryTabDivName + '_' + 'QueryString').val(this.query.queryString);
        }

        this.afterSaveInstance = function (result) {
            this.tumlTabViewManager.afterSaveInstance(result);
        }

        this.afterUpdateInstance = function (result) {
            this.tumlTabViewManager.afterUpdateInstance(result);
        }

        this.afterDeleteInstance = function (result) {
            this.tumlTabViewManager.afterDeleteInstance(result);
        }

        this.afterSaveClassQuery = function (result) {
            this.tumlTabViewManager.afterSaveClassQuery(result);
        }

        this.afterUpdateClassQuery = function (result) {
            this.tumlTabViewManager.afterUpdateClassQuery(result);
        }

        this.afterDeleteClassQuery = function (result) {
            this.tumlTabViewManager.afterDeleteClassQuery(result);
        }

        this.saveToClass = function (post) {
            var self = this;
            var query = queryToJson(this.queryTabDivName, this.queryId);
            query.qualifiedName = 'umlglib::org::umlg::meta::ClassQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (post) {
                overloadedPostData.insert.push(query);
            } else {
                overloadedPostData.update.push(query);
            }
            $.ajax({
                url: this.classQueryUri,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data) {
                    var queryFromDb;
                    var queries = data[0].data;
                    for (var i = 0; i < queries.length; i++) {
                        queryFromDb = queries[i];
                        if (queryFromDb.name == query.name) {
                            break;
                        }
                    }

                    if (post) {
                        self.afterSaveClassQuery({queryType: 'classQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
                    } else {
                        self.afterUpdateClassQuery({queryType: 'classQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg_' + self.queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);

                }
            });

        }

        this.createQuery = function (queryTabDivName, oclExecuteUri, query, post) {
            var self = this;
            this.queryTabDivName = queryTabDivName;
            this.query = query;
            this.oclExecuteUri = oclExecuteUri;
            var queryTab = $('#' + queryTabDivName);

            var windowHeight = $('.ui-layout-center').height() - 190;

            //create a panel with a header and body.
            //header contains the a form for executing and saving the query
            //body contains a layout manager. at the top is the query text and at the bottom the results
            var queryPanel = $('<div />', {class: 'umlg-query-panel panel panel-default'}).appendTo(queryTab);
            var queryPanelHeader = $('<div />', {class: 'panel-heading'}).appendTo(queryPanel);
            var queryPanelBody = $('<div />', {class: 'panel-body'}).appendTo(queryPanel);

            //Create a horizontal inline form for he queries details
            var queryFormDiv = $('<div />', {class: 'form-inline', role: 'form'}).appendTo(queryPanelHeader);
            var queryExecuteButtonFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(queryFormDiv);
            var executeButton = $('<button />', {type: 'button', id: queryTabDivName + '_' + 'ExecuteButton', class: 'form-control btn btn-success umlg-button'}).appendTo(queryExecuteButtonFormGroupDiv);
            $('<span class="glyphicon glyphicon-play-circle"></span>').appendTo(executeButton);
            $('<span />').text(' Execute').appendTo(executeButton);
            executeButton.button().click(
                function () {
                    self.executeQuery();
                }
            );

            var querySelectTypeFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(queryFormDiv);
            $('<label />', {class: 'sr-only', for: queryTabDivName + '_' + 'ExecuteButton'}).text('this is hidden').appendTo(querySelectTypeFormGroupDiv);
            querySelect = $('<select />', {class: 'chzn-select queryEnum', style: 'width: 100px;'});
            if (query.type.toUpperCase() === 'OCL') {

            } else if (query.type.toUpperCase() === 'GREMLIN') {

            } else if (query.type.toUpperCase() === 'NATIVE') {

            } else {
                throw 'Unknown query type ' + query.type;
            }
            querySelect.append($('<option ' + (query.type.toUpperCase() === 'OCL' ? 'selected="selected"' : '') + '/>)').val('OCL').html('OCL'));
            querySelect.append($('<option ' + (query.type.toUpperCase() === 'GREMLIN' ? 'selected="selected"' : '') + '/>)').val('GREMLIN').html('GREMLIN'));
            querySelect.append($('<option ' + (query.type.toUpperCase() === 'NATIVE' ? 'selected="selected"' : '') + '/>)').val('NATIVE').html('NATIVE'));
            querySelect.appendTo(querySelectTypeFormGroupDiv);
            querySelect.chosen({disable_search: true});

            var elementOnTheRight = $('<div />', {class: 'pull-right'}).appendTo(queryFormDiv);

            if (isUmlgLib) {
                var queryQueryNameFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(elementOnTheRight);
                var queryNameInput = $('<input >', {id: queryTabDivName + '_' + 'QueryName', type: 'text', class: 'form-control'});
                queryNameInput.val(query.name).appendTo(queryQueryNameFormGroupDiv);
            }

            if (isUmlgLib && this.instanceQueryUri !== '') {
                var querySaveInstanceFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(elementOnTheRight);
                var saveInstanceButton = $('<button />', {id: queryTabDivName + '_' + 'SaveButton', class: 'form-control btn btn-primary umlg-button'}).appendTo(querySaveInstanceFormGroupDiv);
                $('<span class="glyphicon glyphicon-save"></span>').appendTo(saveInstanceButton);
                $('<span />').text(' save to instance').appendTo(saveInstanceButton);

                saveInstanceButton.button().click(
                    function () {
                        self.saveToInstance(post);
                    });
            }
            if (isUmlgLib && this.classQueryUri !== '') {
                var querySaveClassFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(elementOnTheRight);
                var saveClassButton = $('<button />', {id: queryTabDivName + '_' + 'SaveButton', class: 'form-control btn btn-primary umlg-button'}).appendTo(querySaveClassFormGroupDiv);
                $('<span class="glyphicon glyphicon-save"></span>').appendTo(saveClassButton);
                $('<span />').text(' save to class').appendTo(saveClassButton);
                saveClassButton.button().click(
                    function () {
                        self.saveToClass(post);
                    });
            }
            if (isUmlgLib && !post) {
                var queryCancelButtonFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(elementOnTheRight);
                var cancelButton = $('<button />', {id: queryTabDivName + '_' + 'CancelButton', class: 'form-control btn btn-primary umlg-button'}).appendTo(queryCancelButtonFormGroupDiv);
                $('<span class="glyphicon glyphicon-ban-circle"></span>').appendTo(cancelButton);
                $('<span />').text(' cancel').appendTo(cancelButton);
                cancelButton.button().click(
                    function () {
                        self.cancelQuery();
                    }
                );
                var queryDeleteButtonFormGroupDiv = $('<div />', {class: 'form-group'}).appendTo(elementOnTheRight);
                var deleteButton = $('<button />', {id: queryTabDivName + '_' + 'DeleteButton', class: 'form-control btn btn-primary umlg-button'}).appendTo(queryDeleteButtonFormGroupDiv);
                $('<span class="glyphicon glyphicon-remove"></span>').appendTo(deleteButton);
                $('<span />').text(' delete').appendTo(deleteButton);
                deleteButton.button().click(
                    function () {
                        self.deleteQuery();
                    }
                );
            }

            var layoutDiv = $('<div />', {id: 'queryLayoutDiv', style: 'height: ' + windowHeight + 'px; width" 100%; overflow: hidden;'});
            layoutDiv.appendTo(queryPanelBody);

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
            var textArea = $('<textarea />', {id: queryTabDivName + '_' + 'QueryString'});
            textArea.text(query.queryString).appendTo(oclTextAreaDiv);

            codeMirror = CodeMirror.fromTextArea(textArea[0], {mode: 'text/x-mysql', onKeyEvent: function (o, e) {
                if ((e.which === 13 && e.altKey && e.type === "keydown") || (e.which === 13 && e.ctrlKey && e.type === "keydown")) {
                    self.executeQuery();
                    e.preventDefault();
                }
            }});

            //Outer div for results
            var oclResult = $('<div />', {id: queryTabDivName + '_' + 'OclResult', class: 'oclresult'});
            oclResult.appendTo(centerDiv);
            if (query.data !== undefined && query.data !== null) {
                tumlQueryGridManager.refresh(query.data, queryTabDivName + '_' + 'OclResult');
            }

            layoutDiv.layout({
                center__paneSelector: ".query-center",
                north__paneSelector: ".query-north",
                north__size: 125,
                spacing_open: 3,
//                north: {spacing_open: 3},

                onresize_end: function () {
                    //Resize the textarea
                    var northHeight = $('.query-north').height() - 15;
                    $('.CodeMirror').height(northHeight);
                    return true;
                }
            });

        }

        function queryToJson(queryTabDivName, id) {
            var query = {};
            query.name = $("#" + queryTabDivName + '_' + 'QueryName').val();
            query.queryString = codeMirror.getValue();
            query.queryEnum = querySelect.val();
            if (id !== undefined) {
                query.id = id;
            }
            return query;
        }

        //Public api
        $.extend(this, {
            "TumlTabQueryManagerVersion": "1.0.0",
            //These events are propagated from the grid
            "onSelfCellClick": new Tuml.Event()
        });

        init();
    }

})(jQuery);
