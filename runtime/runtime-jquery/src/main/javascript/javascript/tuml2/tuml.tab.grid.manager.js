(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlBaseGridManager: TumlBaseGridManager,
            TumlManyComponentGridManager: TumlManyComponentGridManager,
            TumlForManyLookupGridManager: TumlForManyLookupGridManager,
            TumlQueryGridManager: TumlQueryGridManager,
            TumlTabGridManager: TumlTabGridManager
        }
    });

    function TumlQueryGridManager(propertyNavigatingTo) {
        var self = this;

        //Public api
        $.extend(this, {
            "TumlQueryGridManager": "1.0.0",
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
            "onManyEditorKeyPress": new Tuml.Event()
        });

        this.setupColumns = function () {
            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);
            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});

            self.columns.splice(0, 0, {
                id: "row",
                name: "row",
                field: "row",
                sortable: true
            });

        };

        this.refresh = function (result, gridDivName) {
            this.result = result;
            this.dataBeforeEdit = $.extend(true, [], result.data);
            this.metaForData = result.meta.to;
            this.gridDivName = gridDivName;
            var outerDivForResults = $('#' + this.gridDivName);
            outerDivForResults.children().remove();

            $('<div />', {id: "queryResultsDiv"})
            $('<div id="serverErrorMsg' + this.gridDivName + '" />').appendTo(outerDivForResults);

            var windowHeight = $('.query-center').height() - 30;

            $('<div />', {id: 'queryResultsDiv' + this.gridDivName, style: 'width:auto;height:' + windowHeight + 'px;'}).appendTo(outerDivForResults);
            $('<div />', {id: 'pagerQueryResultsDiv' + this.gridDivName, style: 'width:auto;height:20px;'}).appendTo(outerDivForResults);

            $('#contextMenu' + this.gridDivName).remove();
            this.createGrid(result.data/*, this.metaForData*/, -1);
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid('#queryResultsDiv' + this.gridDivName, this.dataView, this.columns, this.options);
            //Creating a pager for the component manies editor grid call commitCurrentEditor which buggers everything up
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $('#pagerQueryResultsDiv' + this.gridDivName));
            $("<div id='grid-buttonQueryComponent" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerQueryResultsDiv' + this.gridDivName + ' .slick-pager-settings');
            TumlBaseGridManager.prototype.instantiateGrid.call(this);
        };

        this.addButtons = function () {
        }
    }

    TumlQueryGridManager.prototype = new TumlBaseGridManager;

    TumlQueryGridManager.prototype.setupOptions = function () {
        this.options = {showHeaderRow: true,
            headerRowHeight: 30,
            editable: false,
            enableAddRow: false,
            enableCellNavigation: true,
            asyncEditorLoading: false,
            enableAsyncPostRender: true,
            forceFitColumns: false,
            topPanelHeight: 25
        };
    }

    TumlQueryGridManager.prototype.setData = function (data) {
        this.dataView.setItems(data, 'row');
    }

    function TumlManyComponentGridManager(tumlUri, propertyNavigatingTo) {
        var self = this;
        TumlBaseGridManager.call(this, tumlUri, propertyNavigatingTo);

        //Public api
        $.extend(this, {
            "TumlTabGridManagerVersion": "1.0.0",
            "onManyComponentSaveButtonSuccess": new Tuml.Event(),
            "onManyComponentCloseButtonSuccess": new Tuml.Event(),
            "onManyComponentCancelButtonSuccess": new Tuml.Event(),
            "onClickManyComponentCell": new Tuml.Event(),
            "onClickOneComponentCell": new Tuml.Event(),
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
            "onManyEditorKeyPress": new Tuml.Event()
        });

        this.setupColumns = function () {
            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);
            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
            this.columns.push(
                {id: "delete", name: "delete", field: "delete", sortable: false,
                    formatter: TumlSlick.Formatters.TumlDelete }
            );
        };

        this.refresh = function (result) {
            this.dataBeforeEdit = $.extend(true, [], result.data);
            this.metaForData = result.meta.to;
            var tabDiv = $('#' + this.metaForData.name + "ManyComponent");
            $('<div id="serverErrorMsg" />').appendTo(tabDiv);
            $('<div id="myGridManyComponentHeader" class="selectheader" />').append($('<p />').text('Create the values to add.')).appendTo(tabDiv);
            $('<div id="myGridManyComponent' + this.metaForData.name + '" style="width:auto;height:90%;"></div>').appendTo(tabDiv);
            $('<div id="pagerManyComponent' + this.metaForData.name + '" style="width:auto;height:20px;"></div>').appendTo(tabDiv);
            $('#contextMenu' + this.metaForData.name).remove();
            this.createGrid(result.data/*, this.metaForData*/, tumlUri);
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid("#myGridManyComponent" + this.localMetaForData.name, this.dataView, this.columns, this.options);
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $("#pagerManyComponent" + this.localMetaForData.name));
            $("<div id='grid-buttonManyComponent" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerManyComponent' + this.localMetaForData.name + ' .slick-pager-settings');
            TumlBaseGridManager.prototype.instantiateGrid.call(this);
        };

        this.addButtons = function () {
            $('<button />').text('Save').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    if (self.validateMultiplicity()) {
                        self.doSave();
//                        var validationResults = self.validateNewItems(self.dataView.getNewItems());
//                        if (validationResults.length == 0) {
//                            self.onManyComponentSaveButtonSuccess.notify({value:self.dataView.getItems(), tabName:self.metaForData.name}, null, self);
//                        } else {
//                            var errorMsg = '\n';
//                            for (var i = 0; i < validationResults.length; i++) {
//                                errorMsg += validationResults[i].msg + '\n';
//                            }
//                            alert('There are validation errors: ' + errorMsg);
//                        }
                    }
                }
            }).appendTo('#grid-buttonManyComponent' + this.localMetaForData.name);
            $('<button />').text('Cancel').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    self.cancel(self.dataBeforeEdit);
                    self.onManyComponentCancelButtonSuccess.notify({value: self.dataView.getItems(), tabName: self.metaForData.name}, null, self);
                }
            }).appendTo('#grid-buttonManyComponent' + this.localMetaForData.name);
            $('<button />').text('Close').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    self.cancel(self.dataBeforeEdit);
                    self.onManyComponentCloseButtonSuccess.notify({value: self.dataView.getItems(), tabName: self.metaForData.name}, null, self);
                }
            }).appendTo('#grid-buttonManyComponent' + this.localMetaForData.name);
        }

    }

    TumlManyComponentGridManager.prototype = new TumlBaseGridManager;

    TumlManyComponentGridManager.prototype.initializeDataModel = function (data, filter) {
        this.dataView.beginUpdate();
        this.dataView.setNewItems(data);
        this.dataView.setFilterArgs({
            metaForData: this.localMetaForData
        });
        this.dataView.setFilter(filter);
        this.dataView.endUpdate();
    }

    TumlManyComponentGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        $('<li data="delete" />').text("delete").appendTo(contextMenuUl);
        return contextMenuUl;
    };

    function TumlForManyLookupGridManager(tumlUri, propertyNavigatingTo) {
        var self = this;
        TumlBaseGridManager.call(this, tumlUri, propertyNavigatingTo);

        //Public api
        $.extend(this, {
            "TumlForManyLookupGridManager": "1.0.0",
            "onSelectButtonSuccess": new Tuml.Event(),
            "onSelectCancelButtonSuccess": new Tuml.Event(),
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
            "onManyEditorKeyPress": new Tuml.Event()
        });

        this.setupOptions = function () {
            TumlBaseGridManager.prototype.setupOptions.call(this);
            this.options['editable'] = false;
        }

        this.refresh = function (result) {
            this.metaForData = result.meta.to;
            var tabDiv = $('#' + this.metaForData.name + "Lookup");
            $('<div id="serverErrorMsg" />').appendTo(tabDiv);
            $('<div id="myGridLookupHeader" class="selectheader" />').append($('<p />').text('Select the values to add.')).appendTo(tabDiv);
            $('<div id="myGridLookup' + this.metaForData.name + '" style="width:auto;height:90%;"></div>').appendTo(tabDiv);
            $('<div id="pagerLookup' + this.metaForData.name + '" style="width:auto;height:20px;"></div>').appendTo(tabDiv);
            $('#contextMenu' + this.metaForData.name).remove();
            this.createGrid(result.data/*, this.metaForData*/, tumlUri);
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid("#myGridLookup" + this.localMetaForData.name, this.dataView, this.columns, this.options);
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $("#pagerLookup" + this.localMetaForData.name));
            $("<div id='grid-buttonLookup" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerLookup' + this.localMetaForData.name + ' .slick-pager-settings');
            TumlBaseGridManager.prototype.instantiateGrid.call(this);
        };

        this.addButtons = function () {
            $('<button />').text('Select').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    var items = [];
                    var selectedRows = self.grid.getSelectedRows();
                    for (var i = 0; i < selectedRows.length; i++) {
                        var selectedRow = selectedRows[i];
                        var item = self.dataView.getItem(selectedRow);
                        items.push(item);
                    }
                    self.onSelectButtonSuccess.notify({items: items, tabName: self.metaForData.name}, null, self);
                }
            }).appendTo('#grid-buttonLookup' + this.localMetaForData.name);

            $('<button />').text('Cancel').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    self.onSelectCancelButtonSuccess.notify({tabName: self.metaForData.name}, null, self);
                }
            }).appendTo('#grid-buttonLookup' + this.localMetaForData.name);
        };

    }

    TumlForManyLookupGridManager.prototype = new TumlBaseGridManager;

    TumlForManyLookupGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        $('<li data="delete" />').text("delete").appendTo(contextMenuUl);
        return contextMenuUl;
    };

    function TumlTabGridManager(tumlUri, propertyNavigatingTo) {
        var self = this;
        TumlBaseGridManager.call(this, tumlUri, propertyNavigatingTo);

        //Public api
        $.extend(this, {
            "TumlTabGridManagerVersion": "1.0.0",
            "onAddButtonSuccess": new Tuml.Event(),
            "onClickManyComponentCell": new Tuml.Event(),
            "onClickOneComponentCell": new Tuml.Event(),
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
            "onManyEditorKeyPress": new Tuml.Event()
        });

        this.setupColumns = function () {

            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);

            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
            this.columns.push(
                {id: "delete", name: "delete", field: "delete", sortable: false,
                    formatter: TumlSlick.Formatters.TumlDelete }
            );
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid("#myGrid" + this.localMetaForData.name, this.dataView, this.columns, this.options);
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $("#pager" + this.localMetaForData.name));
            $("<div id='grid-button" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pager' + this.localMetaForData.name + ' .slick-pager-settings');
            TumlBaseGridManager.prototype.instantiateGrid.call(this);
        };

        this.addButtons = function () {
            if (!this.propertyNavigatingTo.composite) {
                //if grid is for non composite many include a add button, this will bring up a grid with the values to add
                var $addButton = $('<button />').text('Add').click(function () {
                    if (self.grid.getEditorLock().commitCurrentEdit()) {
                        var adjustedUri = propertyNavigatingTo.tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), self.contextVertexId);
                        $.ajax({
                            url: adjustedUri,
                            type: "GET",
                            dataType: "json",
                            contentType: "json",
                            success: function (lookupResult, textStatus, jqXHR) {
                                lookupResult.data = self.removeElementsAlreadyInGrid(self.dataView.getNewItems(), lookupResult.data);
                                lookupResult.data = lookupResult.data.concat(self.dataView.getDeletedItems());
                                self.onAddButtonSuccess.notify({originalDataView: self.dataView, originalGrid: self.grid, propertyNavigatingTo: propertyNavigatingTo, tumlUri: tumlUri + '_' + self.localMetaForData.name, tabId: self.localMetaForData.name, data: lookupResult}, null, self);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                            }
                        });
                    }
                }).appendTo('#grid-button' + this.localMetaForData.name);
            }

            //Save button
            $('<button />').text('Save').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    if (self.validateMultiplicity()) {
                        self.doSave();
                    }
                } else {
                    alert("Commit failed on current active cell!");
                }
            }).appendTo('#grid-button' + this.localMetaForData.name);

            var $cancelButton = $('<button />').text('Cancel').click(function () {
                if (self.grid.getEditorLock().commitCurrentEdit()) {
                    $.ajax({
                        url: tumlUri,
                        type: "GET",
                        dataType: "json",
                        contentType: "json",
                        success: function (result, textStatus, jqXHR) {
                            //Only cancel this tab
                            for (var i = 0; i < result.length; i++) {
                                var metaForData = result[i].meta.to;
                                if (metaForData.name === self.localMetaForData.name) {
                                    self.cancel(result[i].data);
                                    return;
                                }
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                        }
                    });
                }
            }).appendTo('#grid-button' + this.localMetaForData.name);
        }
    }

    TumlTabGridManager.prototype = new TumlBaseGridManager;

    TumlTabGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        $('<li data="delete" />').text("delete").appendTo(contextMenuUl);
        return contextMenuUl;
    };

    function TumlBaseGridManager(tumlUri, propertyNavigatingTo) {
        this.tumlUri = tumlUri;
        this.propertyNavigatingTo = propertyNavigatingTo;

        var self = this;
        this.columns = [];

        this.cancel = function (data) {
            if (this.grid.getEditorLock().commitCurrentEdit()) {
                this.grid.resetActiveCell();
                this.setData(data);
                this.grid.invalidateAllRows();
                this.grid.updateRowCount();
                this.grid.render();
            }
        }

        this.refresh = function (result, gridDiv) {

            this.metaForData = result.meta.to;
            var tabDiv = gridDiv;
            tabDiv.children().remove();

            $('<div id="serverErrorMsg" />').appendTo(tabDiv);

            var windowHeight = $('.ui-layout-center').height() - 135;
            $('<div />', {id: 'myGrid' + this.metaForData.name, style: 'width:auto;height:' + windowHeight + 'px;', class: 'tumlSlickGrid'}).appendTo(tabDiv);
//            $('<div />', {id: 'myGrid' + this.metaForData.name, style: 'width:auto;height:65%;', class: 'tumlSlickGrid'}).appendTo(tabDiv);
            $('<div />', {id: 'pager' + this.metaForData.name, style: 'width:auto;height:20px;'}).appendTo(tabDiv);

            $('#contextMenu' + this.metaForData.name).remove();
            this.createGrid(result.data/*, this.metaForData*/, tumlUri);

        };

        //This is called from tuml.tab.view.manager.js, from the onPutSuccess and onPostSuccess events
        this.update = function (result, gridDiv) {
            this.metaForData = result.meta.to;
            this.updateGrid(result.data);
        };

        this.addNewRow = function () {
            $.ajax({
                url: tumlUri + "?rollback=true",
                type: "POST",
                dataType: "json",
                contentType: "json",
                success: function (data, textStatus, jqXHR) {
                    //Cancel prevent validation from happening
                    self.grid.getEditController().cancelCurrentEdit();
                    self.dataView.addItem(data[0].data[0]);
                    //This ensures the cell is in edit mode, i.e. the cursor is ready for typing
                    self.grid.editActiveCell();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                }
            });
        }

        this.doSave = function () {
            //put updated items
            if (this.dataView.getUpdatedItems().length !== 0 && this.dataView.getNewItems().length == 0 && this.dataView.getDeletedItems().length == 0) {
                $.ajax({
                    url: tumlUri + '_' + self.localMetaForData.name,
                    type: "PUT",
                    dataType: "json",
                    contentType: "json",
                    data: JSON.stringify(this.dataView.getUpdatedItems()),
                    success: function (data, textStatus, jqXHR) {
                        self.onPutSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                        self.onPutFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
                    }
                });
            }
            //post new items
            if (this.dataView.getNewItems().length !== 0 && this.dataView.getUpdatedItems().length == 0 && this.dataView.getDeletedItems().length == 0) {
                var validationResults = self.validateNewItems(self.dataView.getNewItems());
                if (validationResults.length == 0) {
                    $.ajax({
                        url: tumlUri + '_' + self.localMetaForData.name,
                        type: "POST",
                        dataType: "json",
                        contentType: "json",
                        data: JSON.stringify(this.dataView.getNewItems()),
                        success: function (data, textStatus, jqXHR) {
                            self.onPostSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                            self.onPostFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
                        }
                    });
                } else {
                    var errorMsg = '\n';
                    for (var i = 0; i < validationResults.length; i++) {
                        errorMsg += validationResults[i].msg + '\n';
                    }
                    alert('Validation errors: ' + errorMsg);
                }
            }
            //delete new items
            if (this.dataView.getDeletedItems().length !== 0 && this.dataView.getNewItems().length == 0 && this.dataView.getUpdatedItems().length == 0) {
                $.ajax({
                    url: tumlUri,
                    type: "DELETE",
                    dataType: "json",
                    contentType: "json",
                    data: JSON.stringify(this.dataView.getDeletedItems()),
                    success: function (data, textStatus, jqXHR) {
                        self.onDeleteSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                        self.onDeleteFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
                    }
                });
            }
            if ((this.dataView.getUpdatedItems().length != 0 && this.dataView.getNewItems().length != 0) ||
                (this.dataView.getUpdatedItems().length != 0 && this.dataView.getDeletedItems().length != 0) ||
                (this.dataView.getNewItems().length != 0 && this.dataView.getDeletedItems().length != 0)) {

                var overloadedPostUri = this.propertyNavigatingTo.tumlOverloadedPostUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);

                var overloadedPostData = {};
                var validationErrors = true;
                if (this.dataView.getNewItems().length > 0) {
                    var validationResults = this.validateNewItems(this.dataView.getNewItems());
                    if (validationResults.length == 0) {
                        validationErrors = false;
                        overloadedPostData['insert'] = this.dataView.getNewItems();
                    } else {
                        var errorMsg = '\n';
                        for (var i = 0; i < validationResults.length; i++) {
                            errorMsg += validationResults[i].msg + '\n';
                        }
                        alert('Validation errors: ' + errorMsg);
                    }
                }
                if (!validationErrors) {
                    if (this.dataView.getUpdatedItems().length > 0) {
                        overloadedPostData['update'] = this.dataView.getUpdatedItems();
                    }
                    if (this.dataView.getDeletedItems().length > 0) {
                        overloadedPostData['delete'] = this.dataView.getDeletedItems();
                    }
                    $.ajax({
                        url: overloadedPostUri,
                        type: "POST",
                        dataType: "json",
                        contentType: "json",
                        data: JSON.stringify(overloadedPostData),
                        success: function (data, textStatus, jqXHR) {
                            self.onDeleteSuccess.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name, data: data}, null, self);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                            self.onDeleteFailure.notify({tumlUri: tumlUri, tabId: self.localMetaForData.name}, null, self);
                        }
                    });
                }
            }
        }

        this.validateNewItems = function (newItems) {
            var validationResults = [];
            for (var i = 0; i < newItems.length; i++) {
                var item = newItems[i];
                for (var j = 0; j < this.grid.getColumns().length; j++) {
                    var column = this.grid.getColumns()[j];
                    if (column.name !== 'id' && column.name !== 'uri' && column.name !== 'delete') {
                        if (!column.options.property.readOnly) {
                            var validator = selectFieldValidator(column.options.property);
                            var valid = validator(item[column.name]);
                            if (!valid.valid) {
                                validationResults.push(valid);
                            }
                        }
                    }
                }
            }
            return validationResults;
        };

        this.validateMultiplicity = function () {
            if (self.dataView.getItems().length < self.propertyNavigatingTo.lower || (self.propertyNavigatingTo.upper !== -1 && self.dataView.getItems().length > self.propertyNavigatingTo.upper)) {
                alert('multiplicity falls outside the valid range [' + self.propertyNavigatingTo.lower + '..' + self.propertyNavigatingTo.upper + ']');
                return false;
            } else {
                return true;
            }
        }

        this.updateGrid = function (data) {
            this.dataView.refreshItem(data);
        }

        this.createGrid = function (data /*, localMetaForData*/, tumlUri) {
            var columnFilters = {};
            var lookupColumns;
            var self = this;
            this.data = data;
            //The query grid does not have a tumlUri or contextVertexId
            if (tumlUri !== -1) {
                this.contextVertexId = retrieveVertexId(tumlUri);
            } else {
                this.contextVertexId = -1;
            }
            this.localMetaForData = this.metaForData;

            this.setupColumns();

            this.setupOptions();

            var sortcol = "name";
            var sortdir = 1;
            var percentCompleteThreshold = 0;
            var searchString = "";
            this.dataView = new Tuml.Slick.Data.DataView();

            this.instantiateGrid();

//            this.addButtons();

            //Create context menu
            this.createContextMenu();

            this.grid.onContextMenu.subscribe(function (e) {
                e.preventDefault();
                var cell = self.grid.getCellFromEvent(e);
                $("#contextMenu" + self.localMetaForData.name)
                    .data("row", cell.row)
                    .css("top", e.pageY)
                    .css("left", e.pageX)
                    .show();

                $("body").one("click", function () {
                    $("#contextMenu" + self.localMetaForData.name).hide();
                });
                $('#contextMenu' + self.localMetaForData.name).mouseleave(contextMenu_timer);
            });

            function contextMenu_close() {
                $("#contextMenu" + self.localMetaForData.name).hide();
            };

            function contextMenu_timer() {
                window.setTimeout(contextMenu_close, 200);
            };

            $("#contextMenu" + this.localMetaForData.name).click(function (e) {
                if (!$(e.target).is("li")) {
                    return;
                }
                var row = $(this).data("row");
                var tumlUri = $(e.target).attr("data");
                if (tumlUri !== 'delete') {
                    var url = tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), data[row].id);
                    self.onContextMenuClickLink.notify({name: 'unused', tumlUri: url}, null, self);
                } else {
                    var item = this.dataView.getItem(row);
                    self.dataView.deleteItem(item.id);
                    updateValidationWarningHeader();
                }
            });

            this.grid.onClick.subscribe(function (e, args) {
                var column = self.grid.getColumns()[args.cell];

                if (isClickOnNewRow(args.row, args.cell, self.dataView.getItem(args.row))) {
//                    //Add new row
//                    self.addNewRow();
                } else if (column.name == 'id') {
                    e.stopImmediatePropagation();
                } else if (column.name == 'delete') {
                    var item = self.dataView.getItem(args.row);
                    self.dataView.deleteItem(item.id);
                    e.stopImmediatePropagation();
                    updateValidationWarningHeader();
                } else if (column.name == 'uri') {
                    var item = self.dataView.getItem(args.row);
                    var uri = item.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), item.id);
                    self.onSelfCellClick.notify({name: 'unused', tumlUri: uri}, null, self);
                } else if (!column.options.property.manyPrimitive && !column.options.property.manyEnumeration && column.options.property.composite &&
                    column.options.property.lower > 0 && ((column.options.property.upper > 1) || column.options.property.upper === -1)) {
                    //Component many
                    var data = [];
                    if (isCellEditable(args.row, args.cell, self.dataView.getItem(args.row))) {
                        if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                            data = self.dataView.getItem(args.row)[column.name];
                        }
                        //Ensure the row in the grid exist
                        //TODO Suspend the transaction
                        //TODO Save the grid
//                        self.suspendTransactionAndSave();
                        //TODO pass in the row's id
                        var id = self.dataView.getItem(args.row)["id"];
                        self.onClickManyComponentCell.notify({data: data, cell: args, tumlUri: column.options.property.tumlUri, property: column.options.property}, null, self);
                    }
                } else if (!column.options.property.manyPrimitive && !column.options.property.manyEnumeration && column.options.property.composite &&
                    column.options.property.lower === 1 && column.options.property.upper === 1) {
                    //Component one
                    var data = [];
                    if (isCellEditable(args.row, args.cell, self.dataView.getItem(args.row))) {
                        if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                            data.push(self.dataView.getItem(args.row)[column.name]);
                        }
                        self.onClickOneComponentCell.notify({data: data, cell: args, tumlUri: column.options.property.tumlUri, property: column.options.property}, null, self);
                    }
                }
                //unbind the document click event to close many editors
                self.grid['clicked'] = true;
            });

            this.grid.onCellChange.subscribe(function (e, args) {
                self.dataView.updateItem(args.item.id, args.item, self.grid.getColumns()[args.cell]);
            });

//            this.grid.onAddNewRow.subscribe(function (e, args) {
//                self.addNewRow(args);
//            });

            this.grid.onKeyDown.subscribe(function (e) {
                if (self.grid['manyPrimitiveEditorOpen']) {
                    self.grid['manyPrimitiveEditor'].handleKeyPress(e);
                }
                // select all rows on ctrl-a
                if (e.which != 65 || !e.ctrlKey) {
                    return false;
                }

                var rows = [];
                for (var i = 0; i < self.dataView.getLength(); i++) {
                    rows.push(i);
                }

                self.grid.setSelectedRows(rows);
                e.preventDefault();
            });

            this.grid.onSort.subscribe(function (e, args) {
                sortdir = args.sortAsc ? 1 : -1;
                sortcol = args.sortCol.field;

                if ($.browser.msie && $.browser.version <= 8) {
                    // using temporary Object.prototype.toString override
                    // more limited and does lexicographic sort only by default, but can be much faster
                    var percentCompleteValueFn = function () {
                        var val = this["percentComplete"];
                        if (val < 10) {
                            return "00" + val;
                        } else if (val < 100) {
                            return "0" + val;
                        } else {
                            return val;
                        }
                    };

                    // use numeric sort of % and lexicographic for everything else
                    self.dataView.fastSort((sortcol == "percentComplete") ? percentCompleteValueFn : sortcol, args.sortAsc);
                } else {
                    // using native sort with comparer
                    // preferred method but can be very slow in IE with huge datasets
                    self.dataView.sort(function comparer(a, b) {
                        var x = a[sortcol],
                            y = b[sortcol];
                        return (x == y ? 0 : (x > y ? 1 : -1));
                    }, args.sortAsc);
                }
            });

            // wire up model events to drive the grid
            this.dataView.onRowCountChanged.subscribe(function (e, args) {
                self.grid.updateRowCount();
                self.grid.render();
            });

            this.dataView.onRowsChanged.subscribe(function (e, args) {
                self.grid.invalidateRows(args.rows);
                self.grid.render();
            });

            this.dataView.onPagingInfoChanged.subscribe(function (e, pagingInfo) {
                var isLastPage = pagingInfo.pageNum == pagingInfo.totalPages - 1;
                var options = self.grid.getOptions();
                var enableAddRow = options.enableAddRow && (isLastPage || pagingInfo.pageSize == 0);

                if (options.enableAddRow != enableAddRow) {
                    self.grid.setOptions({
                        enableAddRow: enableAddRow
                    });
                }
            });

            $(this.grid.getHeaderRow()).delegate(":input", "change keyup", function (e) {
                columnFilters[$(this).data("columnId")] = $.trim($(this).val());
                self.dataView.refresh();
            });

            this.grid.onColumnsReordered.subscribe(function (e, args) {
                self.updateHeaderRow(self.localMetaForData);
            });

            this.grid.onColumnsResized.subscribe(function (e, args) {
                self.updateHeaderRow(self.localMetaForData);
            });

            this.grid.onViewportChanged.subscribe(function (e, args) {
                var vp = self.grid.getViewport();
            });
            this.grid.onViewportChanged.notify();

            this.grid.onBeforeEditCell.subscribe(function (e, args) {
                if (!isCellEditable(args.row, args.cell, args.item)) {
                    return false;
                }
            });

            this.grid.onActiveCellChanged.subscribe(function (e, args) {
//                alert('onActiveCellChanged ' + args.row);
                if (isClickOnNewRow(args.row, args.cell, self.dataView.getItem(args.row))) {
                    //Add new row
                    self.addNewRow();
                }
            });

            function updateValidationWarningHeader() {
                $('#validation-warning').children().remove();
                if (self.dataView.getItems().length < self.propertyNavigatingTo.lower || (self.propertyNavigatingTo.upper !== -1 && self.dataView.getItems().length > self.propertyNavigatingTo.upper)) {
                    $('#validation-warning').append($('<span />').text(
                        'multiplicity falls outside the valid range [' + self.propertyNavigatingTo.lower + '..' + self.propertyNavigatingTo.upper + ']'));
                }
            }

            function isCellEditable(row, cell, item) {
                for (var j = 0; j < self.grid.getColumns().length; j++) {
                    var column = self.grid.getColumns()[j];
                    if (cell === j && column.name !== 'id' && column.name !== 'uri' && column.name !== 'delete') {
                        if (!column.options.property.readOnly) {
                            var property = column.options.property;
                            if (property.composite && property.lower >= 1) {
                                if (row >= self.dataView.getItems().length || self.dataView.getNewItems().indexOf(item) !== -1) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                    if (j > cell) {
                        break;
                    }
                }
                return true;
            };

            function isClickOnNewRow(row, cell, item) {
                if (row >= self.dataView.getItems().length) {
                    return true;
                } else {
                    return false;
                }
            };

            this.updateHeaderRow = function (metaForData) {
                for (var i = 0; i < this.columns.length; i++) {
                    if (this.columns[i].id !== "selector" && this.columns[i].id !== 'uri' && this.columns[i].id !== 'delete') {
                        var header = this.grid.getHeaderRowColumn(this.columns[i].id);
                        $(header).empty();
                        $(constructHeaderRowBox(this.columns[i].id, metaForData)).data("columnId", this.columns[i].id).val(columnFilters[this.columns[i].id]).appendTo(header);
                    }
                }
            };

            function constructHeaderRowBox(columnId, metaForData) {
                for (var i = 0; i < metaForData.properties.length; i++) {
                    var property = metaForData.properties[i];
                    if (property.name == columnId && property.fieldType == 'String') {
                        return "<input type='text'>";
                    } else if (property.name == columnId && (property.fieldType == 'Integer' || property.fieldType == 'Long')) {
                        return "<input type='text'>";
                    } else if (property.name == columnId && property.fieldType == 'Boolean') {
                        //return "<INPUT type=checkbox value='true' class='editor-checkbox' hideFocus>";
                        return "<select><option value='None'>None</option><option value='true'>True</option><option value='false'>False</option></select>";
                    } else {
                        //return "<input type='text'>";
                    }
                }
                return "<input type='text'>";
            };

            function filter(item, metaForData) {
                for (var columnId in columnFilters) {
                    if (columnId !== undefined && columnFilters[columnId] !== "") {
                        var c = self.grid.getColumns()[self.grid.getColumnIndex(columnId)];
                        for (var i = 0; i < metaForData.metaForData.properties.length; i++) {
                            var property = metaForData.metaForData.properties[i];
                            if (property.name == columnId) {
                                if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
                                    if (property.dataTypeEnum == 'Date') {
                                        if (item[c.field] == null || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else if (property.dataTypeEnum == 'Time') {
                                        if (item[c.field] == undefined || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else if (property.dataTypeEnum == 'DateTime') {
                                        if (item[c.field] == undefined || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
                                        if (item[c.field] == undefined || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
                                        if (item[c.field] == undefined || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else if (property.dataTypeEnum == 'Email') {
                                        if (item[c.field] == undefined || item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                            return false;
                                        }
                                    } else {
                                        alert('Unsupported dataType ' + property.dataTypeEnum);
                                    }
                                } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
                                    if (item[c.field].displayName == undefined && item[c.field] != columnFilters[columnId]) {
                                        return false;
                                    }
                                    if (item[c.field].displayName !== undefined && item[c.field].displayName.indexOf(columnFilters[columnId]) == -1) {
                                        return false;
                                    }
                                } else if (property.fieldType == 'String') {
                                    if (item[c.field] !== undefined && item[c.field].indexOf(columnFilters[columnId]) == -1) {
                                        return false;
                                    }
                                } else if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                                    if (item[c.field] != columnFilters[columnId]) {
                                        return false;
                                    }
                                } else if (property.fieldType == 'Boolean') {
                                    var isNone = (columnFilters[columnId] === 'None');
                                    if (!isNone) {
                                        var isTrueSet = (columnFilters[columnId] === 'true');
                                        if (item[c.field] != isTrueSet) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }

            $(".grid-header .ui-icon").addClass("ui-state-default ui-corner-all").mouseover(function (e) {
                $(e.target).addClass("ui-state-hover")
            }).mouseout(function (e) {
                    $(e.target).removeClass("ui-state-hover")
                });

            this.grid.onValidationError.subscribe(function (e, args) {
                alert(args.validationResults.msg);
            });

            // initialize the model after all the events have been hooked up
            this.setupColumnFormatter();
            this.initializeDataModel(data, filter);
            this.updateHeaderRow(this.localMetaForData);

            // if you don't want the items that are not visible (due to being filtered out
            // or being on a different page) to stay selected, pass 'false' to the second arg
            this.dataView.syncGridSelection(this.grid, true);

        };

        this.addItems = function (items) {
            for (var i = 0; i < items.length; i++) {
                this.dataView.addItem(items[i]);
                this.dataView.getDeletedItems().remove(items[i]);
            }
            this.grid.invalidateAllRows();
            this.grid.updateRowCount();
            this.grid.render();
        }

        this.setCellValue = function (cell, value) {
            var item = self.dataView.getItemByIdx(cell.row);
            if (item !== undefined) {
                item[self.grid.getColumns()[cell.cell].name] = value;
            } else {
                //This happens when the component is the first entry in the grid, i.e. it needs to add the row
//                item = {};
//                item[self.grid.getColumns()[cell.cell].name] = value;
//                self.addNewRow({item: item});
                alert('This should not be happening no more');
            }
            self.grid.invalidateRows([cell.row]);
            self.grid.updateRowCount();
            self.grid.render();
        }

        this.removeElementsAlreadyInGrid = function (items, itemsToAdd) {
            var elementsToRemoveIndex = [];
            for (var i = 0; i < items.length; i++) {
                var newItem = items[i];
                elementsToRemoveIndex.push(newItem.id);
            }
            function needToRemove(element, index, array) {
                return (elementsToRemoveIndex.indexOf(element.id) == -1);
            }

            return itemsToAdd.filter(needToRemove);
        };

    }

    TumlBaseGridManager.prototype.setupColumnFormatter = function () {
        var self = this;
        this.dataView.getItemMetadata = function (row) {
            var item = this.getItem(row);
            var result = {"columns": null};
            var column = {};

            for (var i = 0; i < self.localMetaForData.properties.length; i++) {
                var property = self.localMetaForData.properties[i];
                if (property.composite && property.lower > 0) {
                    if (item === undefined || this.getNewItems().indexOf(item) !== -1) {
                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlRequired};
                    } else {
                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlComponentFormatter};
                    }
                } else {
                    column[property.name] = {"formatter": selectFormatter(property)};
                }
            }
            result["columns"] = column;
            return result;
        };
    }

    TumlBaseGridManager.prototype.initializeDataModel = function (data, filter) {
        this.dataView.beginUpdate();
        this.setData(data);
        this.dataView.setFilterArgs({
            metaForData: this.localMetaForData
        });
        this.dataView.setFilter(filter);
        this.dataView.endUpdate();
    }

    TumlBaseGridManager.prototype.setupColumns = function () {
        var self = this;
        this.columns = [];
        $.each(this.localMetaForData.properties, function (index, property) {
            if ((property.composite && property.lower > 0) || !property.composite && !property.inverseComposite && ((property.oneToOne || property.manyToOne) || property.manyPrimitive || property.manyEnumeration)) {
                //Place the id column first
                if (property.name == "id") {
                    self.columns.splice(0, 0, {
                        id: property.name,
                        name: property.name,
                        field: property.name,
                        sortable: true
                    });
                } else {
                    self.columns.push({
                        id: property.name,
                        name: property.name,
                        field: property.name,
                        sortable: true,
                        editor: selectEditor(property),
                        validator: selectFieldValidator(property),
                        options: {
                            required: property.lower > 0,
                            tumlLookupUri: property.tumlLookupUri,
                            rowEnumerationLookupMap: new RowEnumerationLookupMap(property.qualifiedName, "/" + tumlModelName + "/tumlEnumLookup"),
                            rowLookupMap: new RowLookupMap(self.contextVertexId, property.tumlCompositeParentLookupUri, property.tumlCompositeParentLookupUriOnCompositeParent),
                            compositeParentLookupMap: new CompositeParentLookupMap(self.contextVertexId, property.tumlLookupUri, property.tumlLookupOnCompositeParentUri),
                            ordered: property.ordered,
                            unique: property.unique,
                            property: property},
                        width: 120
                    });
                }
            }
        });
    };

    TumlBaseGridManager.prototype.instantiateGrid = function () {
        //Add in a property on the grid to tell if a many editor is open
        this.grid['manyEditorOpen'] = false;
        this.grid.setSelectionModel(new Slick.RowSelectionModel());
        new Slick.Controls.ColumnPicker(this.columns, this.grid, this.options);
    };

    TumlBaseGridManager.prototype.addButtons = function () {
        alert("addButtons must be overridden");
    };

    TumlBaseGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = $('<ul />', {id: 'contextMenu' + this.localMetaForData.name, style: 'display:none;position:absolute', class: 'contextMenu'}).appendTo('body');
        $('<b />').text('Nav').appendTo(contextMenuUl);
        $('<li data="' + this.localMetaForData.uri + '" />').text("self").appendTo(contextMenuUl);
        $.each(this.localMetaForData.properties, function (index, property) {
            if (property.inverseComposite || !((property.dataTypeEnum !== undefined && property.dataTypeEnum !== null) || property.onePrimitive || property.manyPrimitive || property.name == 'id' || property.name == 'uri')) {
                $('<li data="' + property.tumlUri + '" />').text(property.name).appendTo(contextMenuUl);
            }
        });
        return contextMenuUl;
    };


    TumlBaseGridManager.prototype.setupOptions = function () {
        this.options = {
            autoHeight: false,
            showHeaderRow: true,
            headerRowHeight: 30,
            editable: true,
            enableAddRow: this.propertyNavigatingTo.composite,
            enableCellNavigation: true,
            asyncEditorLoading: false,
            enableAsyncPostRender: true,
            forceFitColumns: false,
            topPanelHeight: 25
        };
    }

    TumlBaseGridManager.prototype.setData = function (data) {
        this.dataView.setItems(data);
    }

    TumlBaseGridManager.prototype.getResult = function (data) {
        return this.result;
    }

})(jQuery);
