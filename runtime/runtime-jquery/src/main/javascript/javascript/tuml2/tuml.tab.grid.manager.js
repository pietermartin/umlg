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

    function TumlQueryGridManager(tumlTabViewManager, propertyNavigatingTo) {

        this.TumlQueryGridManager = "1.0.0";

        var self = this;
        this.tumlTabViewManager = tumlTabViewManager;

        //Public api
        $.extend(this, {
            "TumlQueryGridManager": "1.0.0",
            "onSelfCellClick": new Tuml.Event(),
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

    function TumlManyComponentGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlManyComponentGridManager = "1.0.0";

        TumlBaseGridManager.call(this, tumlTabViewManager, tumlUri, propertyNavigatingTo);

        this.setupColumns = function () {
            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);
            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
            this.columns.push(
                {id: "delete", name: "delete", field: "delete", sortable: false,
                    formatter: TumlSlick.Formatters.TumlDelete }
            );
        };

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

    function TumlForManyLookupGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlForManyLookupGridManager = "1.0.0";

        TumlBaseGridManager.call(this, tumlTabViewManager, tumlUri, propertyNavigatingTo);

        //Public api
        $.extend(this, {
            "TumlForManyLookupGridManager": "1.0.0",
            "onSelectButtonSuccess": new Tuml.Event(),
            "onSelectCancelButtonSuccess": new Tuml.Event(),
            "onSelfCellClick": new Tuml.Event(),
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

    }

    TumlForManyLookupGridManager.prototype = new TumlBaseGridManager;

    TumlForManyLookupGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        $('<li data="delete" />').text("delete").appendTo(contextMenuUl);
        return contextMenuUl;
    };

    function TumlTabGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlTabGridManager = "1.0.0";

        TumlBaseGridManager.call(this, tumlTabViewManager, tumlUri, propertyNavigatingTo);

        this.setupColumns = function () {

            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);

            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
            this.columns.push(
                {id: "delete", name: "delete", field: "delete", sortable: false,
                    formatter: TumlSlick.Formatters.TumlDelete }
            );
        };

    }

    TumlTabGridManager.prototype = new TumlBaseGridManager;

    TumlTabGridManager.prototype.createContextMenu = function () {
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        $('<li data="delete" />').text("delete").appendTo(contextMenuUl);
        return contextMenuUl;
    };

    function TumlBaseGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlBaseGridManager = "1.0.0";

        this.tumlTabViewManager = tumlTabViewManager;
        this.tumlUri = tumlUri;
        this.propertyNavigatingTo = propertyNavigatingTo;
        this.active = false;

        var self = this;
        this.columns = [];

        this.containsOneToOne = false;

        this.calculateContainsOne = function () {
            for (var i = 0; i < this.metaForData.properties.length; i++) {
                var property = this.metaForData.properties[i];
                if (property.oneToOne && !property.onePrimitive) {
                    this.containsOneToOne = true;
                    break;
                }
            }
        }

        this.cancel = function (data) {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                this.grid.resetActiveCell();
                this.setData(data);
                this.grid.invalidateAllRows();
                this.grid.updateRowCount();
                this.grid.render();
            }
        }

        this.clearArraysAfterCommit = function () {
            this.dataView.clearArraysAfterCommit();
        }

        this.beginUpdate = function () {
            this.dataView.beginUpdate();
        }

        this.endUpdate = function (editActiveCell) {
            this.dataView.endUpdate();
            this.grid.invalidateAllRows();
            this.grid.render();
            if (editActiveCell && this.active) {
                this.grid.editActiveCell();
            }
        }

        this.updateGridAfterCommitOrRollback = function (item) {
            this.dataView.refreshItemAfterCommitOrRollback(item);
        }

        this.handleContextMenuClickLink = function (tumlUri) {
            this.tumlTabViewManager.refreshContext(tumlUri);
        }

        this.handleLookup = function (lookupUri, qualifiedName, loadDataCallback) {
            this.tumlTabViewManager.handleLookup(lookupUri, qualifiedName, loadDataCallback);
        }

        this.handleAddNewRow = function () {
            if (Slick.GlobalEditorLock.cancelCurrentEdit()) {
                var newItem = {};
                //Generate a fake id, its required for the grid to work nicely
                newItem.id = 'fake::' + Tuml.TumlFakeIndex++;
                newItem.tmpId = newItem.id;
                newItem.qualifiedName = this.localMetaForData.qualifiedName;
                this.dataView.addItem(newItem);
                this.tumlTabViewManager.saveNewRow();
                if (this.containsOneToOne) {
                    this.tumlTabViewManager.parentTabContainerManager.addToOneToOneIndex(newItem.id, {});
                }
            }
        }

        this.updateDataModel = function () {
            if (this.containsOneToOne) {
                var newItems = this.dataView.getNewItems();
                for (var i = 0; i < newItems.length; i++) {
                    var data = newItems[i];

                    for (var j = 0; j < this.metaForData.properties.length; j++) {
                        var property = this.metaForData.properties[j];
                        if (property.oneToOne && !property.composite && !property.inverseComposite && !property.onePrimitive && data[property.name] !== undefined && data[property.name] !== null && data[property.name].id !== undefined) {
                            this.tumlTabViewManager.updateDataModel(data[property.name].previousId, property.inverseName, {});
                            this.tumlTabViewManager.updateDataModel(data[property.name].id, property.inverseName, data);
                        }
                    }

                }
            }
        }

        this.handleDeleteRow = function (row, data) {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                var moveCell = this.grid.getActiveCell() != null && this.grid.getActiveCell().row >= row;
                var item = this.dataView.getItem(row);
                this.dataView.deleteItem(item.id);
                this.tumlTabViewManager.handleDeleteRow();
                if (moveCell) {
                    //Move the active cell one up
                    if (this.dataView.getItems().length > 0) {
                        this.grid.setActiveCell(this.grid.getActiveCell().row - 1, this.grid.getActiveCell().cell);
                    } else {
                        this.grid.resetActiveCell();
                    }
                }
            }
        }

        this.createGrid = function (data, tumlUri) {
            var columnFilters = {};
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
            this.dataView = new Tuml.Slick.Data.DataView();

            this.instantiateGrid();

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
                    self.handleContextMenuClickLink(url);
                } else {
                    self.handleDeleteRow(row, data);
                }
            });

            this.grid.onClick.subscribe(function (e, args) {

                if (Slick.GlobalEditorLock.commitCurrentEdit()) {

                    var column = self.grid.getColumns()[args.cell];

                    if (isClickOnNewRow(args.row)) {
                        //Add new row
                    } else if (column.name == 'id') {
                        e.stopImmediatePropagation();
                    } else if (column.name == 'delete') {
                        self.handleDeleteRow(args.row, self.data);
                        e.stopImmediatePropagation();
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
                                //Get the data currently for the component
                                data = self.dataView.getItem(args.row)[column.name];
                            }
                            var id = self.dataView.getItem(args.row)["id"];
                            self.tumlTabViewManager.openManyComponent(data, args, column.options.property.tumlUri, column.options.property);
                        }
                    } else if (!column.options.property.manyPrimitive && !column.options.property.manyEnumeration && column.options.property.composite &&
                        column.options.property.lower === 1 && column.options.property.upper === 1) {
                        //Component one
                        var data = null;
                        if (isCellEditable(args.row, args.cell, self.dataView.getItem(args.row))) {
                            if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                                //Get the data currently for the component
                                data = self.dataView.getItem(args.row)[column.name];
                            } else {
                                data = {};
                            }
                            self.tumlTabViewManager.openOneComponent(data, args, column.options.property.tumlUri, column.options.property);
                        }
                    }
                    //unbind the document click event to close many editors
                    self.grid['clicked'] = true;
                }
            });

            this.grid.onCellChange.subscribe(function (e, args) {
                self.dataView.updateItem(args.item.id, args.item, self.grid.getColumns()[args.cell]);
            });

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
                if (isClickOnNewRow(args.row)) {
                    //If click on select one then the editor will call handleNewRow
                    var column = self.grid.getColumns()[args.cell];
                    var editor = selectEditor(column.options.property);
                    if (editor !== Tuml.Slick.Editors.SelectOneToOneCellEditor) {
                        self.handleAddNewRow();
                    }
                }
            });

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

            function isClickOnNewRow(row) {
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
                alert('XXX' + args.validationResults.msg);
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
            if (item === undefined) {
                throw 'item not found for given row in TumlBaseGridManager.setCellValue';
            }
            item[self.grid.getColumns()[cell.cell].name] = value;
            self.grid.invalidateRows([cell.row]);
            self.grid.updateRowCount();
            self.grid.render();
        }

    }

    TumlBaseGridManager.prototype.refresh = function (result, gridDiv) {

        this.metaForData = result.meta.to;
        this.calculateContainsOne();

        var tabDiv = gridDiv;
        tabDiv.children().remove();

        $('<div id="serverErrorMsg" />').appendTo(tabDiv);

        var windowHeight = $('.ui-layout-center').height() - 135;
        $('<div />', {id: 'myGrid' + this.metaForData.name, style: 'width:auto;height:' + windowHeight + 'px;', class: 'tumlSlickGrid'}).appendTo(tabDiv);
        $('<div />', {id: 'pager' + this.metaForData.name, style: 'width:auto;height:20px;'}).appendTo(tabDiv);

        $('#contextMenu' + this.metaForData.name).remove();
        this.createGrid(result.data, this.tumlUri);

    };

    TumlBaseGridManager.prototype.setupColumnFormatter = function () {
        var self = this;
        this.dataView.getItemMetadata = function (row) {
            var item = this.getItem(row);
            var result = {cssClasses: null, columns: null};
            var column = {};

            var isNew = self.dataView.isRowNew(row);
            var isUpdated = self.dataView.isRowUpdated(row);

            for (var i = 0; i < self.localMetaForData.properties.length; i++) {
                var property = self.localMetaForData.properties[i];
                if (property.composite && property.lower > 0) {
                    if (item === undefined || this.getNewItems().indexOf(item) !== -1) {
                        //Check if the component has validation errors
                        if (self.tumlTabViewManager.validationResults !== null && self.tumlTabViewManager.validationResults.length > 0) {

                            var found = false;
                            for (var j = 0; j < self.tumlTabViewManager.validationResults.length; j++) {
                                var validationResult = self.tumlTabViewManager.validationResults[j];
                                if (validationResult.property === property.name) {
                                    found = true;
                                    column[property.name] = {"formatter": TumlSlick.Formatters.TumlValidationFailedFormatter};
                                    break;
                                }
                            }
                            if (!found) {
                                column[property.name] = {"formatter": TumlSlick.Formatters.TumlRequired};
                            }

                        } else {
                            column[property.name] = {"formatter": TumlSlick.Formatters.TumlRequired};
                        }
                    } else {
                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlComponentFormatter};
                    }
                } else {
                    column[property.name] = {"formatter": selectFormatter(property, isNew, isUpdated)};
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
        this.columns = [];
        for (var i = 0; i < this.localMetaForData.properties.length; i++) {
            var property = this.localMetaForData.properties[i];
            if ((property.composite && property.lower > 0) || !property.composite && !property.inverseComposite && ((property.oneToOne || property.manyToOne) || property.manyPrimitive || property.manyEnumeration)) {
                //Place the id column first
                if (property.name == "id") {
                    this.columns.splice(0, 0, {
                        id: property.name,
                        name: property.name,
                        field: property.name,
                        sortable: true
                    });
                } else {
                    this.columns.push({
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
                            rowLookupMap: new RowLookupMap(this.contextVertexId, property.tumlCompositeParentLookupUri, property.tumlCompositeParentLookupUriOnCompositeParent),
                            compositeParentLookupMap: new CompositeParentLookupMap(this.contextVertexId, property.tumlLookupUri, property.tumlLookupOnCompositeParentUri),
                            ordered: property.ordered,
                            unique: property.unique,
                            property: property,
                            tumlBaseGridManager: this},
                        width: 120
                    });
                }
            }
        }
        ;
    };

    TumlBaseGridManager.prototype.instantiateGrid = function () {

        this.grid = new Slick.Grid("#myGrid" + this.localMetaForData.name, this.dataView, this.columns, this.options);
        this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $("#pager" + this.localMetaForData.name));
        $("<div id='grid-button" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pager' + this.localMetaForData.name + ' .slick-pager-settings');

        //Add in a property on the grid to tell if a many editor is open
        this.grid['manyEditorOpen'] = false;
        this.grid.setSelectionModel(new Slick.RowSelectionModel());
        new Slick.Controls.ColumnPicker(this.columns, this.grid, this.options);
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


})
    (jQuery);
