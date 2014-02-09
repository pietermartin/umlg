(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabQueryManager: TumlTabQueryManager
        }
    });

    function TumlTabQueryManager(parentTumlTabViewManager, instanceQueryUri, classQueryUri, rootQueryUri, queryId) {

        var tumlQueryGridManager;
        var codeMirror;
        this.instanceQueryUri = instanceQueryUri;
        this.classQueryUri = classQueryUri;
        this.rootQueryUri = rootQueryUri;
        if (queryId !== undefined) {
            this.queryId = queryId;
        }

        this.tumlTabViewManager = parentTumlTabViewManager;

        function init() {
            tumlQueryGridManager = new Tuml.TumlQueryGridManager(parentTumlTabViewManager);
        }

        this.executeQuery = function () {
            var self = this;
            $.ajax({
                url: this.oclExecuteUri + '?query=' + codeMirror.getValue() + '&type=' + this.tumlTabViewManager.parentTabContainerManager.executeButton.val(),
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

        this.saveToInstance = function () {
            var self = this;
            this.synchronizeQuery(this.queryId);
            this.query.qualifiedName = 'umlglib::org::umlg::query::InstanceQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (this.query.post) {
                overloadedPostData.insert.push(this.query);
            } else {
                overloadedPostData.update.push(this.query);
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
                        if (queryFromDb.name == self.query.name) {
                            break;
                        }
                    }
                    if (self.query.post) {
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
            this.synchronizeQuery(this.queryId);

            var url;
            if (this.instanceQueryUri !== '') {
                url = this.instanceQueryUri;
            } else if (this.classQueryUri !== '') {
                url = this.classQueryUri;
            } else if (this.rootQueryUri !== '') {
                url = this.rootQueryUri;
            } else {
                throw 'Unknown query uri!';
            }

            var isInstanceQuery = this.instanceQueryUri !== '' ? true : false;
            this.query.qualifiedName = isInstanceQuery ? 'umlglib::org::umlg::query::InstanceQuery' : 'umlglib::org::umlg::query::ClassQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            overloadedPostData.delete.push(this.query);
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data, textStatus, jqXHR) {

                    if (self.instanceQueryUri !== '') {
                        self.afterDeleteInstance({queryType: 'instanceQuery', query: self.query, gridData: tumlQueryGridManager.getResult()});
                    } else if (self.classQueryUri !== '') {
                        self.afterDeleteClassQuery({queryType: 'classQuery', query: self.query, gridData: tumlQueryGridManager.getResult()});
                    } else if (self.rootQueryUri !== '') {
                        self.afterDeleteRootQuery({queryType: 'rootQuery', query: self.query, gridData: tumlQueryGridManager.getResult()});
                    } else {
                        throw 'Unknown query uri!';
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg_' + queryTabDivName).addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.cancelQuery = function () {
            var executeButton = this.queryExecuteButtonFormGroupDiv.find('#executeButton');
            executeButton.text('Execute ' + this.query.type.toUpperCase());
            executeButton.val(this.query.type.toUpperCase());
            codeMirror.setValue(this.query.queryString);
            var queryNameInput = this.queryQueryNameFormGroupDiv.find('#queryName');
            queryNameInput.val(this.query.name);
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

        this.afterSaveRootQuery = function (result) {
            this.tumlTabViewManager.afterSaveRootQuery(result);
        }

        this.afterUpdateRootQuery = function (result) {
            this.tumlTabViewManager.afterUpdateRootQuery(result);
        }

        this.afterDeleteRootQuery = function (result) {
            this.tumlTabViewManager.afterDeleteRootQuery(result);
        }

        this.saveToClass = function () {
            var self = this;
            this.synchronizeQuery(this.queryId);
            this.query.qualifiedName = 'umlglib::org::umlg::meta::ClassQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (this.query.post) {
                overloadedPostData.insert.push(this.query);
            } else {
                overloadedPostData.update.push(this.query);
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
                        if (queryFromDb.name == self.query.name) {
                            break;
                        }
                    }

                    if (self.query.post) {
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

        this.saveToRoot = function () {
            var self = this;
            this.synchronizeQuery(this.queryId);
//            this.query.qualifiedName = 'umlglib::org::umlg::meta::ClassQuery';
            var overloadedPostData = {insert: [], update: [], delete: []};
            if (this.query.post) {
                overloadedPostData.insert.push(this.query);
            } else {
                overloadedPostData.update.push(this.query);
            }
            $.ajax({
                url: this.rootQueryUri,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(overloadedPostData),
                success: function (data) {
                    var queryFromDb;
                    var queries = data[0].data;
                    for (var i = 0; i < queries.length; i++) {
                        queryFromDb = queries[i];
                        if (queryFromDb.name == self.query.name) {
                            break;
                        }
                    }

                    if (self.query.post) {
                        self.afterSaveRootQuery({queryType: 'rootQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
                    } else {
                        self.afterUpdateRootQuery({queryType: 'rootQuery', query: queryFromDb, gridData: tumlQueryGridManager.getResult()});
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
            this.query.post = post;
            this.oclExecuteUri = oclExecuteUri;
            var queryTab = $('#' + queryTabDivName);

            //create a panel with a header and body.
            //header contains the a form for executing and saving the query
            //body contains a layout manager. at the top is the query text and at the bottom the results
            var queryPanel = $('<div />', {class: 'umlg-query-panel panel panel-default'}).appendTo(queryTab);
            var queryPanelBody = $('<div />', {class: 'panel-body'}).appendTo(queryPanel);
            //Create a horizontal inline form for the queries details
            var tabFooter = this.tumlTabViewManager.parentTabContainerManager.tabLayoutTabFooterDiv;
            this.queryExecuteButtonFormGroupDiv;
            this.queryQueryNameFormGroupDiv;
            this.querySaveInstanceFormGroupDiv;
            this.querySaveClassFormGroupDiv;
            this.queryCancelButtonFormGroupDiv;
            this.queryDeleteButtonFormGroupDiv;

            var queryFormDiv = tabFooter.find('.form-inline');
            if (queryFormDiv.length !== 0) {
                this.queryExecuteButtonFormGroupDiv = queryFormDiv.find('#queryExecuteButtonFormGroupDiv');
                this.queryQueryNameFormGroupDiv = queryFormDiv.find('#queryNameFormDiv');
                this.querySaveInstanceFormGroupDiv = queryFormDiv.find('#saveInstanceFormGroup');
                this.querySaveClassFormGroupDiv = queryFormDiv.find('#saveClassFormGroup');
                this.queryCancelButtonFormGroupDiv = queryFormDiv.find('#cancelButtonFormGroup');
                this.queryDeleteButtonFormGroupDiv = queryFormDiv.find('#deleteButtonFormGroup');
            } else {
                this.tumlTabViewManager.parentTabContainerManager.addQueryButtons(this.query);
            }

            var windowHeight = $('.ui-layout-center').height() - 165;
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
            textArea.text(query.queryString);
            textArea.appendTo(oclTextAreaDiv);
            codeMirror = CodeMirror.fromTextArea(textArea[0], {
                    lineNumbers: true,
                    extraKeys: {"Ctrl-Space": "autocomplete"},
                    matchBrackets: true,
                    mode: "text/x-groovy",
                    onKeyEvent: function (o, e) {
                        if ((e.which === 13 && e.altKey && e.type === "keydown") || (e.which === 13 && e.ctrlKey && e.type === "keydown")) {
                            self.executeQuery();
                            e.preventDefault();
                        }
                    }
                }
            );

            CodeMirror.commands.autocomplete = function (cm) {
                CodeMirror.showHint(cm, CodeMirror.hint.ocl, {async: true, completeSingle: false});
            };

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

                onresize_end: function () {
                    //Resize the textarea
                    var northHeight = $('.query-north').height() - 15;
                    $('.CodeMirror').height(northHeight);
                    return true;
                }
            });

        }

        this.synchronizeQuery = function (id) {
            this.query.name = this.tumlTabViewManager.parentTabContainerManager.queryQueryNameFormGroupDiv.find('#queryName').val();
            this.query.queryString = codeMirror.getValue();
            this.query.queryEnum = this.tumlTabViewManager.parentTabContainerManager.executeButton.val();
            if (id !== undefined) {
                this.query.id = id;
            }
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
