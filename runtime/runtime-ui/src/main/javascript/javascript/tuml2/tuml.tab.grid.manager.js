(function ($) {

    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlBaseGridManager: TumlBaseGridManager,
            TumlManyComponentGridManager: TumlManyComponentGridManager,
            TumlForManyLookupGridManager: TumlForManyLookupGridManager,
            TumlQueryGridManager: TumlQueryGridManager,
            TumlTabGridManager: TumlTabGridManager,
            UmlgListItemMovedOrder: 0
        }
    });

    function TumlQueryGridManager(tumlTabViewManager, propertyNavigatingTo) {

        this.TumlQueryGridManager = "1.0.0";

        var self = this;
        this.tumlTabViewManager = tumlTabViewManager;

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
//            this.dataBeforeEdit = $.extend(true, [], result.data);
            this.metaForDataTo = result.meta.to;
            this.gridDivName = gridDivName;
            var outerDivForResults = $('#' + this.gridDivName);
            outerDivForResults.children().remove();

            $('<div />', {id: "queryResultsDiv"})
            $('<div id="serverErrorMsg' + this.gridDivName + '" />').appendTo(outerDivForResults);

            var windowHeight = $('.query-center').height() - 30;
            $('<div />', {id: 'queryResultsDiv' + this.gridDivName, style: 'width:auto;height:' + windowHeight + 'px;'}).appendTo(outerDivForResults);
            $('<div />', {id: 'pagerQueryResultsDiv' + this.gridDivName, style: 'width:auto;height:20px;'}).appendTo(outerDivForResults);
            $('#contextMenu' + this.gridDivName).remove();
            this.createGrid(result.data, -1);
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid('#queryResultsDiv' + this.gridDivName, this.dataView, this.columns, this.options);
            //Creating a pager for the component manies editor grid call commitCurrentEditor which buggers everything up
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $('#pagerQueryResultsDiv' + this.gridDivName));
            $("<div id='grid-buttonQueryComponent" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerQueryResultsDiv' + this.gridDivName + ' .slick-pager-settings');

            //Add in a property on the grid to tell if a many editor is open
            this.grid['manyEditorOpen'] = false;
            this.grid.setSelectionModel(new Slick.RowSelectionModel());
            new Slick.Controls.ColumnPicker(this.columns, this.grid, this.options);
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

    /**
     * No need for a context menu as component grids are only used for creation.
     * I.e there is no where to navigate to as the entity does yet not exist.
     */
    TumlManyComponentGridManager.prototype.createContextMenu = function () {
        //Do nothing
    };

    function TumlForManyLookupGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlForManyLookupGridManager = "1.0.0";

        TumlBaseGridManager.call(this, tumlTabViewManager, tumlUri, propertyNavigatingTo);

        this.setupOptions = function () {
            TumlBaseGridManager.prototype.setupOptions.call(this);
            this.options['editable'] = false;
        }

        this.refresh = function (result) {
            this.metaForDataTo = result.meta.to;
            var tabDiv = $('#' + this.metaForDataTo.name + "Lookup");
            $('<div id="serverErrorMsg" />').appendTo(tabDiv);
            $('<div id="myGridLookup' + this.metaForDataTo.name + '" style="width:auto;height:90%;"></div>').appendTo(tabDiv);
            $('<div id="pagerLookup' + this.metaForDataTo.name + '" style="width:auto;height:20px;"></div>').appendTo(tabDiv);
            //Do not remove the contextMenu for lookups
            if (!(this instanceof TumlForManyLookupGridManager)) {
                $('#contextMenu' + this.metaForDataTo.name).remove();
            }
            this.createGrid(result.data/*, this.metaForDataTo*/, tumlUri);
        };

        this.instantiateGrid = function () {
            this.grid = new Slick.Grid("#myGridLookup" + this.localMetaForData.name, this.dataView, this.columns, this.options);
            this.pager = new Slick.Controls.Pager(this.dataView, this.grid, $("#pagerLookup" + this.localMetaForData.name));
            $("<div id='grid-buttonLookup" + this.localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerLookup' + this.localMetaForData.name + ' .slick-pager-settings');

            //Add in a property on the grid to tell if a many editor is open
            this.grid['manyEditorOpen'] = false;
            this.grid.setSelectionModel(new Slick.RowSelectionModel());
            new Slick.Controls.ColumnPicker(this.columns, this.grid, this.options);
        };

    }

    TumlForManyLookupGridManager.prototype = new TumlBaseGridManager;

    TumlForManyLookupGridManager.prototype.isPropertyForGrid = function (property) {
        if (property.composite && property.lower > 0) {
            return true;
        } else if (property.inverseQualifiedName === this.propertyNavigatingTo.qualifiedName) {
            //This prevents the one displaying on a non composite one to many relationship.
            //Its value is implicit seeing that one navigated from the one.
            //If one were to edit the one it would disappear from the grid as it is no longer part of the association.

            //This is must occur before the next if.
            return false;
        } else if (!property.composite && !property.inverseComposite) {
            if (property.associationClassOne && !property.memberEndOfAssociationClass) {
                return false;
            } else if (property.oneToOne || property.manyToOne || property.manyPrimitive || property.manyEnumeration) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    //Can not navigate from a lookup
    TumlForManyLookupGridManager.prototype.createContextMenu = function () {
        //Do nothing
    };

    function TumlTabGridManager(tumlTabViewManager, tumlUri, propertyNavigatingTo) {

        this.TumlTabGridManager = "1.0.0";

        TumlBaseGridManager.call(this, tumlTabViewManager, tumlUri, propertyNavigatingTo);

        this.setFocus = function () {
            this.grid.focus();
            var activeCell = this.grid.getActiveCell();
            if (activeCell === null) {
                this.grid.setActiveCell(0, 0);
            }
        }

        this.setupColumns = function () {

            TumlBaseGridManager.prototype.setupColumns.call(this, this.localMetaForData);

            this.columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
            //Association class property can not have a deleted column
            if (!(this.propertyNavigatingTo.associationClassOne && !this.propertyNavigatingTo.memberEndOfAssociationClass)) {
                this.columns.push(
                    {id: "delete", name: "delete", field: "delete", sortable: false,
                        formatter: TumlSlick.Formatters.TumlDelete }
                );
            }
        };

    }

    TumlTabGridManager.prototype = new TumlBaseGridManager;

    TumlTabGridManager.prototype.createContextMenu = function () {
        var self = this;
        var contextMenuUl = TumlBaseGridManager.prototype.createContextMenu.call(this);
        var li = $('<li></li>').appendTo(contextMenuUl);
        var a = $('<a href="#"></a>').appendTo(li);
        var span = $('<span class="ui-icon ui-icon-close"></span>').appendTo(a);
        a.append('delete');
        a.data('contextData', {name: 'delete'});
        a.on('click', function (e) {
            //Find the link
            var a = $(e.target);
            //The row gets saved into the contextMenuUl on right click show menu
            var row = a.parent().parent().data("row");
            self.handleContextMenuSelection(a, row);
            contextMenuUl.hide();
            return false;
        });
        contextMenuUl.hide();
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
            for (var i = 0; i < this.metaForDataTo.properties.length; i++) {
                var property = this.metaForDataTo.properties[i];
                if (property.oneToOne && !property.composite && !property.inverseComposite && !property.onePrimitive) {
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

        this.updateGridAfterRollback = function (item) {
            this.dataView.refreshItemAfterRollback(item, this.propertyNavigatingTo.composite);
        }

        this.updateGridAfterCommit = function (item) {
            this.dataView.refreshItemAfterCommit(item, this.propertyNavigatingTo.composite);
        }

        this.handleContextMenuClickLink = function (tumlUri) {
            this.tumlTabViewManager.refreshContext(tumlUri);
        }

        this.handleLookup = function (lookupUri, qualifiedName, loadDataCallback) {
            this.tumlTabViewManager.handleLookup(lookupUri, qualifiedName, loadDataCallback);
        }

        this.handleAddNewRow = function () {
            if (Slick.GlobalEditorLock.cancelCurrentEdit()) {
                var nextId = Tuml.TumlFakeIndex++;
                var newItem;
                //Check if the row is a memberEndOfAnAssociationClass, if so initialize the association class
                if (this.propertyNavigatingTo.memberEndOfAssociationClass) {
                    // {id: fakeId, tmpId: fakeId, displayName: null, refreshFromDb: true};
                    newItem = {id: 'fake::' + nextId, tmpId: 'fake::' + nextId};
                    var associationClassFakeId = 'fake::' + Tuml.TumlFakeIndex++;
                    newItem[this.propertyNavigatingTo.inverseAssociationClassPropertyName] = {id: associationClassFakeId, tmpId: associationClassFakeId, displayName: null, refreshFromDb: true};
                } else {
                    newItem = {id: 'fake::' + nextId, tmpId: 'fake::' + nextId};
                }
                newItem.qualifiedName = this.localMetaForData.qualifiedName;
                this.dataView.addItem(newItem);
                this.tumlTabViewManager.saveNewRow();
                if (this.containsOneToOne) {
                    this.tumlTabViewManager.parentTabContainerManager.addToOneToOneIndex(newItem.id, {id: newItem.id, qualifiedName: newItem.qualifiedName});
                }
            }
        }

        this.updateDataModelForOneToOne = function () {
            if (this.containsOneToOne) {
                var newItems = this.dataView.getNewItems();
                for (var i = 0; i < newItems.length; i++) {
                    var data = newItems[i];
                    for (var j = 0; j < this.metaForDataTo.properties.length; j++) {
                        var property = this.metaForDataTo.properties[j];
                        if (property.oneToOne && !property.composite && !property.inverseComposite && !property.onePrimitive && data[property.name] !== undefined && data[property.name] !== null && data[property.name].id !== undefined) {
                            this.tumlTabViewManager.updateDataModelForOneToOne(data[property.name].previousId, property.inverseName, {});
                            this.tumlTabViewManager.updateDataModelForOneToOne(data[property.name].id, property.inverseName, data);
                        }
                    }
                }
            }
        }

        this.updateDataModelForOneToOneForUpdate = function () {
            var updatedItems = this.dataView.getUpdatedItems();
            for (var i = 0; i < updatedItems.length; i++) {
                var data = updatedItems[i];
                for (var j = 0; j < this.metaForDataTo.properties.length; j++) {
                    var property = this.metaForDataTo.properties[j];
                    if (property.oneToOne && !property.composite && !property.inverseComposite && !property.onePrimitive && data[property.name] !== undefined && data[property.name] !== null && data[property.name].id !== undefined) {
                        //TODO name needs to be displayName, some backend strategy required dude
                        if (!isNaN(data[property.name].id)) {
                            this.tumlTabViewManager.updateDataModelForOneToOneForUpdatedItem(this.metaForDataTo.qualifiedName, data[property.name].id, data[property.name].displayName, property.inverseName, data);
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
            this.localMetaForData = this.metaForDataTo;

            this.setupColumns();

            this.setupOptions();

            var sortcol = "name";
            var sortdir = 1;
            this.dataView = new Tuml.Slick.Data.DataView();

            this.instantiateGrid();

            if (this.propertyNavigatingTo !== undefined && this.propertyNavigatingTo.ordered) {
                this.setupRowDrag();
            }

            //Create context menu
            this.createContextMenu();

            this.grid.onContextMenu.subscribe(
                function (e, args) {
                    e.preventDefault();
                    var cell = args.grid.getCellFromEvent(e);
                    //Do not show the context menu for new rows.
                    //TODO think about showing the context menu for new rows in order to navigate to 'self'
                    if (!self.dataView.isNewRow(self.dataView.getItemByIdx(cell.row).id)) {
                        self.showContextMenu(cell, e.pageX, e.pageY);
                    }
                }
            );

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
                        var uri = item.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(item.id));
                        self.tumlTabViewManager.refreshContext(uri);
                    } else if (isComponentMany(column)) {
                        doComponentMany(column, args);
                        e.stopImmediatePropagation();
                    } else if (isComponentOne(column)) {
                        //Component one
                        doComponentOne(column, args);
                        e.stopImmediatePropagation();
                    } else if (column.options.property.associationClassOne) {
                        //Component one
                        var data = null;
                        if (isCellEditableWithColumn(column, args.row, self.dataView.getItem(args.row))) {
                            if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                                //Get the data currently for the component
                                data = self.dataView.getItem(args.row)[column.name];
                            } else if (self.dataView.getItem(args.row)[column.name] !== null && self.dataView.getItem(args.row)[column.name].tmpId !== undefined) {
                                data = self.dataView.getItem(args.row)[column.name];
                            } else {
                                data = {};
                            }
                            self.tumlTabViewManager.openAssociationClass(data, args, column.options.property.tumlUri, column.options.property);
                        }
                        e.stopImmediatePropagation();
                    }
                    //unbind the document click event to close many editors
                    self.grid['clicked'] = true;
                }
            });

            function isComponentMany(column) {
                return !column.options.property.manyPrimitive && !column.options.property.manyEnumeration && column.options.property.composite &&
                    column.options.property.lower > 0 && ((column.options.property.upper > 1) || column.options.property.upper === -1);
            }

            function doComponentMany(column, args) {
                //Component many
                var data = [];
                if (isCellEditableWithColumn(column, args.row, self.dataView.getItem(args.row))) {
                    if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                        //Get the data currently for the component
                        data = self.dataView.getItem(args.row)[column.name];
                    }
                    var id = self.dataView.getItem(args.row)["id"];
                    self.tumlTabViewManager.openManyComponent(data, args, column.options.property.tumlUri, column.options.property);
                }
            }

            function isComponentOne(column) {
                return !column.options.property.manyPrimitive && !column.options.property.manyEnumeration && column.options.property.composite &&
                    column.options.property.lower === 1 && column.options.property.upper === 1;
            }

            function doComponentOne(column, args) {
                var data = null;
                if (isCellEditableWithColumn(column, args.row, self.dataView.getItem(args.row))) {
                    if (self.dataView.getItem(args.row) !== undefined && self.dataView.getItem(args.row) !== null && self.dataView.getItem(args.row)[column.name] !== undefined && self.dataView.getItem(args.row)[column.name] !== null) {
                        //Get the data currently for the component
                        data = self.dataView.getItem(args.row)[column.name];
                    } else {
                        data = {};
                    }
                    self.tumlTabViewManager.openOneComponent(data, args, column.options.property.tumlUri, column.options.property);
                }
            }

            this.grid.onCellChange.subscribe(function (e, args) {
                var column = self.grid.getColumns()[args.cell];
                self.dataView.updateItem(args.item.id, args.item, column.name);
                var property = column.options.property;

                //For a to one that is an association class the association class must be cleared.
                //This is because setting the one means a new association/class needs to be created
                if (!property.associationClassOne && property.memberEndOfAssociationClass) {
                    self.dataView.updateItem(args.item.id, args.item, property.associationClassPropertyName);
                }
                if (!property.composite && !property.inverseComposite && !property.onePrimitive && !property.oneEnumeration && property.upper === 1) {
                    if (self.tumlTabViewManager instanceof Tuml.TumlTabManyComponentViewManager) {
                        self.updateDataModelForOneToOne();
                    } else {
                        self.updateDataModelForOneToOneForUpdate();
                    }
                }
            });

            this.grid.onKeyDown.subscribe(function (e) {
                if (self.grid['manyPrimitiveEditorOpen']) {
                    self.grid['manyPrimitiveEditor'].handleKeyPress(e);
                }

                if (e.which == 39 && e.ctrlKey) {
                    e.preventDefault();
                    var cell = self.grid.getActiveCell();
                    var activeCellPosition = self.grid.getActiveCellPosition();
                    self.showContextMenu(cell, activeCellPosition.left + activeCellPosition.width, activeCellPosition.top);
                    e.stopImmediatePropagation();
                } else if (e.which === 13) {
                    var cell = self.grid.getActiveCell();
                    var column = self.grid.getColumns()[cell.cell];
                    if (isComponentMany(column)) {
                        doComponentMany(column, {row: cell.row ,cell: cell.cell, grid: self.grid});
                    } else if (isComponentOne(column)) {
                        doComponentOne(column, {row: cell.row ,cell: cell.cell, grid: self.grid});
                    }
                } else {
                    //if not cntrl A return
                    if (e.which != 65 || !e.ctrlKey) {
                        return false;
                    }

                    // select all rows on ctrl-a
                    var rows = [];
                    for (var i = 0; i < self.dataView.getLength(); i++) {
                        rows.push(i);
                    }

                    self.grid.setSelectedRows(rows);
                    e.preventDefault();
                    e.stopImmediatePropagation();
                }
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
                    if (column.options !== undefined) {
                        var editor = selectEditor(column.options.property);
                        if (editor !== Tuml.Slick.Editors.SelectOneToOneCellEditor) {
                            self.handleAddNewRow();
                        }
                    } else {
                        self.handleAddNewRow();
                    }
                }
            });

            function isCellEditableWithColumn(column, row, item) {
                if (column.name !== 'id' && column.name !== 'uri' && column.name !== 'delete' && !column.options.property.readOnly) {
                    var property = column.options.property;
                    if ((property.composite && property.lower >= 1) || property.associationClassOne) {
                        if (row >= self.dataView.getItems().length || self.dataView.getNewItems().indexOf(item) !== -1) {
                            //New rows
                            //Only click on a non composite association class once the non composite association has been selected
                            if (property.associationClassOne && !property.memberEndOfAssociationClass && !property.composite && (property.oneToOne || property.manyToOne)) {
                                //If the association class has been set then the association class will have an tmpId
                                if (self.dataView.getItem(row)[column.name] !== undefined && self.dataView.getItem(row)[column.name] !== null && self.dataView.getItem(row)[column.name].tmpId !== undefined) {
                                    return true;
                                } else {
                                    if (!(self instanceof Tuml.TumlForManyLookupGridManager)) {
                                        //TumlForManyLookupGridManager are read only, clicked on to select the row
                                        alert('Select an association before setting the association class!');
                                    }
                                    return false;
                                }
                            } else {
                                return true;
                            }
                        } else {
                            if (property.associationClassOne && !property.memberEndOfAssociationClass && !property.composite && (property.oneToOne || property.manyToOne)) {
                                //If the association class has been set then the association class will have an tmpId
                                if (self.dataView.getItem(row)[column.name] !== undefined && self.dataView.getItem(row)[column.name] !== null && self.dataView.getItem(row)[column.name].tmpId !== undefined) {
                                    return true;
                                } else {
                                    if (!(self instanceof Tuml.TumlForManyLookupGridManager) && property.inverseQualifiedName !== this.propertyNavigatingTo.qualifiedName) {
                                        //TumlForManyLookupGridManager are read only, clicked on to select the row
                                        alert('Select an association before setting the association class!');
                                    }
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }

            function isCellEditable(row, cell, item) {
                for (var j = 0; j < self.grid.getColumns().length; j++) {
                    var column = self.grid.getColumns()[j];
                    if (cell === j) {
                        return isCellEditableWithColumn(column, row, item);
                    }
                    if (j > cell) {
                        break;
                    }
                }
                return true;
            }

            function isClickOnNewRow(row) {
                if (row >= self.dataView.getItems().length) {
                    return true;
                } else {
                    return false;
                }
            }

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
                    } else if (property.name == columnId && (property.fieldType == 'Integer' || property.fieldType == 'Long' || property.fieldType == 'Real')) {
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
                                } else if (property.fieldType == 'Integer' || property.fieldType == 'Long' || property.fieldType == 'Real') {
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

//            $(".grid-header .ui-icon").addClass("ui-state-default ui-corner-all").mouseover(function (e) {
//                $(e.target).addClass("ui-state-hover")
//            }).mouseout(function (e) {
//                    $(e.target).removeClass("ui-state-hover")
//                });

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

        this.setupRowDrag = function () {

            var moveRowsPlugin = new Slick.RowMoveManager({
                cancelEditOnDrag: true
            });

            moveRowsPlugin.onBeforeMoveRows.subscribe(function (e, data) {
                for (var i = 0; i < data.rows.length; i++) {
                    // no point in moving before or after itself
                    if (data.rows[i] == data.insertBefore || data.rows[i] == data.insertBefore - 1) {
                        e.stopPropagation();
                        return false;
                    }
                }
                return true;
            });

            moveRowsPlugin.onMoveRows.subscribe(function (e, args) {
                var extractedRows = [], left, right;
                var rows = args.rows;
                var insertBefore = args.insertBefore;

                left = self.dataView.getItems().slice(0, insertBefore);
                right = self.dataView.getItems().slice(insertBefore, self.dataView.getItems().length);

                rows.sort(function(a,b) { return a-b; });

                for (var i = 0; i < rows.length; i++) {
                    var movedItem = self.dataView.getItems()[rows[i]];
                    extractedRows.push(movedItem);
                }

                rows.reverse();

                for (var i = 0; i < rows.length; i++) {
                    var row = rows[i];
                    if (row < insertBefore) {
                        left.splice(row, 1);
                    } else {
                        right.splice(row - insertBefore, 1);
                    }
                }

                //This if statement only applies if more than one row is being moved together
                //If so, then, depending on whether the rows are being moved up or down, the _index and moveOrder
                // is calculated in opposing directions
                if (rows[0] > insertBefore - 1) {
                    for (var i = 0; i < extractedRows.length; i++) {
                        var movedItem = extractedRows[i];
                        //Add to updated items
                        movedItem['_index'] = left.length + i;
                        self.dataView.updateItem(movedItem.id, movedItem, '_index');
                        movedItem['_movedOrder'] = Tuml.UmlgListItemMovedOrder++;
                        self.dataView.updateItem(movedItem.id, movedItem, '_movedOrder');
                    }
                } else {
                    for (var i = extractedRows.length - 1; i >= 0; i--) {
                        var movedItem = extractedRows[i];
                        //Add to updated items
                        movedItem['_index'] = left.length + i;
                        self.dataView.updateItem(movedItem.id, movedItem, '_index');
                        movedItem['_movedOrder'] = Tuml.UmlgListItemMovedOrder++;
                        self.dataView.updateItem(movedItem.id, movedItem, '_movedOrder');
                    }
                }

                self.dataView.setItems(left.concat(extractedRows.concat(right)));

                var selectedRows = [];
                for (var i = 0; i < rows.length; i++) {
                    selectedRows.push(left.length + i);
                }

                self.grid.resetActiveCell();
                self.grid.setSelectedRows(selectedRows);
                self.grid.render();
            });

            self.grid.registerPlugin(moveRowsPlugin);

            self.grid.onDragInit.subscribe(function (e, dd) {
                // prevent the grid from cancelling drag'n'drop by default
                e.stopImmediatePropagation();
            });

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

        this.metaForDataFrom = result.meta.from;
        this.metaForDataTo = result.meta.to;
        this.calculateContainsOne();

        var tabDiv = gridDiv;
        tabDiv.children().remove();

        $('<div id="serverErrorMsg" />').appendTo(tabDiv);

//        var count = this.calculateGridWindowHeight(0);
//        var windowHeight = count * 125;
//        windowHeight = $('.ui-layout-center').height() - windowHeight;
        var windowHeight = $('.ui-layout-center').height() - 142;
        $('<div />', {id: 'myGrid' + this.metaForDataTo.name, style: 'width:auto;height:' + windowHeight + 'px;', class: 'umlg-slick-grid'}).appendTo(tabDiv);
        $('<div />', {id: 'pager' + this.metaForDataTo.name, style: 'width:auto;height:20px;'}).appendTo(tabDiv);

        $('#contextMenu' + this.metaForDataTo.name).remove();
        this.createGrid(result.data, this.tumlUri);

    };

    TumlBaseGridManager.prototype.calculateGridWindowHeight = function (count) {
         return this.tumlTabViewManager.nestedCount(count);
    }

    TumlBaseGridManager.prototype.setupColumnFormatter = function () {
        var self = this;
        this.dataView.getItemMetadata = function (row) {
            var item = this.getItem(row);
            var result = {cssClasses: null, columns: null};
            var column = {};

            //This indicates that a new row has been saved
            //It is used to color in the id cell to indicate the successful creation
            var isNew = self.dataView.isRowSaved(row);
            //This indicates that an existing row has been saved
            //It is used to color in the id cell to indicate the successful update
            var isUpdated = self.dataView.isRowUpdated(row);

            for (var i = 0; i < self.localMetaForData.properties.length; i++) {
                var property = self.localMetaForData.properties[i];
                //skip primitive properties
                if (!property.onePrimitive && !property.manyPrimitive && !property.oneEnumeration && !property.manyEnumeration && property.dataTypeEnum === null) {

                    if (property.associationClassOne || (property.composite && property.lower > 0)) {
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
                                if (property.associationClassOne) {
                                    if (property.lower > 0) {
                                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlToAssociationClassRequiredFormatter};
                                    } else {
                                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlToAssociationClassFormatter};
                                    }
                                } else {
                                    column[property.name] = {"formatter": TumlSlick.Formatters.TumlRequired};
                                }
                            }
                        } else {
                            if (property.associationClassOne && !property.memberEndOfAssociationClass) {
                                if (property.manyToOne || property.oneToOne) {
                                    //this is the fake property pointing to the association class
                                    if (property.inverseComposite) {
                                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlAssociationComponentFormatter};
                                    } else if (property.lower > 0) {
                                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlToAssociationClassRequiredFormatter};
                                    } else {
                                        column[property.name] = {"formatter": TumlSlick.Formatters.TumlToAssociationClassFormatter};
                                    }
                                } else {
                                    column[property.name] = {"formatter": TumlSlick.Formatters.TumlAssociationComponentFormatter};
                                }
                            } else {
                                column[property.name] = {"formatter": TumlSlick.Formatters.TumlComponentFormatter};
                            }
                        }
                    } else {
                        column[property.name] = {"formatter": selectFormatter(property, isNew, isUpdated)};
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

    TumlBaseGridManager.prototype.isPropertyForGrid = function (property) {

        //this.contextVertexId  !== null && property.associationClassOne ensures that associationClass properties appear but not from the root.
        //from the root there is no association
        if (this.contextVertexId !== null && property.associationClassOne && property.memberEndOfAssociationClass) {
            return true;
        } else if (this.contextVertexId != null && property.associationClassOne) {
            return true;
        } else if (!property.composite && !property.inverseComposite && property.lower > 0 && !property.associationClassOne) {
            return true;
        } else if (property.associationClassProperty) {
            return false;
        } else if (property.composite && property.lower > 0) {
            return true;
        } else if (this.propertyNavigatingTo !== undefined && property.inverseQualifiedName === this.propertyNavigatingTo.qualifiedName) {
            //This prevents the one displaying on a non composite one to many relationship.
            //Its value is implicit seeing that one navigated from the one.
            //If one were to edit the one it would disappear from the grid as it is no longer part of the association.

            //This is must occur before the next if.
            return false;
        } else if (!property.composite && !property.inverseComposite && ((property.oneToOne || (property.manyToOne)) || property.manyPrimitive || property.manyEnumeration || (property.dataTypeEnum !== null && (property.upper > 1 || property.upper === -1)))) {
            return true;
        } else {
            return false;
        }

    }

    TumlBaseGridManager.prototype.setupColumns = function () {
        this.columns = [];
        for (var i = 0; i < this.localMetaForData.properties.length; i++) {
            var property = this.localMetaForData.properties[i];
            if (this.isPropertyForGrid(property)) {

                //Place the id column first
                if (property.name == "id") {
                    this.columns.splice(0, 0, {
                        id: property.name,
                        name: property.name,
                        field: property.name,
                        sortable: true
                    });
                    if (this.propertyNavigatingTo !== undefined && this.propertyNavigatingTo.ordered) {
                        //This is for slickgrid's row move/drag plugin
                        this.columns[0].behavior = "selectAndMove";
                        this.columns[0].cssClass = "cell-reorder";
                    }
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
                            ordered: property.ordered,
                            unique: property.unique,
                            property: property,
                            tumlBaseGridManager: this},
                        width: 120
                    });
                }
            }
        }
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

    TumlBaseGridManager.prototype.showContextMenu = function (cell, x, y) {
        var self = this;
        var contextMenu = $("#contextMenu" + this.localMetaForData.name);

        var links = contextMenu.find('a');
        for (var i = 0; i < links.length; i++) {
            var link = $(links[i]);
            var adjustedUri = link.attr('href').replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.data[cell.row].id));
            link.attr('href', adjustedUri);
        }

        contextMenu.menu({
            select: function (e, ui) {
                //Find the link
                var a = ui.item.find('a');
                //The row gets saved into the contextMenuUl on right click show menu
                var row = a.parent().parent().data("row");
                self.handleContextMenuSelection(a, row);
                contextMenu.hide();
                e.preventDefault();
                e.stopImmediatePropagation();
            }
        });

        contextMenu.data("row", cell.row).css("top", y).css("left", x).show();
        contextMenu.focus();
        $("body").one("click", function () {
            contextMenu.hide();
            //reset the links back to {property}
            resetLinks(cell);
        });
        contextMenu.mouseleave(contextMenu_timer);

        function contextMenu_close() {
            $("#contextMenu" + self.localMetaForData.name).hide();
            resetLinks(cell);
        };

        function contextMenu_timer() {
            window.setTimeout(contextMenu_close, 200);
        };

        function resetLinks(cell) {
            //reset the links back to {property}
            var links = contextMenu.find('a');
            for (var i = 0; i < links.length; i++) {
                var link = $(links[i]);
                var adjustedUri = link.attr('href').replace(self.data[cell.row].id, '{property}');
                link.attr('href', adjustedUri);
            }
        }
    }

    TumlBaseGridManager.prototype.createContextMenu = function () {
        var self = this;
        var contextMenuUl = $('<ul />', {id: 'contextMenu' + this.localMetaForData.name, class: 'contextMenu'}).appendTo('body');
        var li = $('<li></li>').appendTo(contextMenuUl);
        var a = $('<a href="#"></a>').appendTo(li);
        a.on('click', function (e) {
                //Find the link
                var a = $(e.target);
                //The row gets saved into the contextMenuUl on right click show menu
                var row = a.parent().parent().data("row");
                self.handleContextMenuSelection(a, row);
                contextMenuUl.hide();
                e.preventDefault();
                e.stopImmediatePropagation();
            }
        );
        var span = $('<span class="ui-icon ui-icon-home"></span>').appendTo(a);
        a.append('self');
        a.data("contextData", {name: 'self', uri: this.localMetaForData.uri});

        for (var i = 0; i < this.localMetaForData.properties.length; i++) {
            var property = this.localMetaForData.properties[i];
            if (property.inverseComposite || !((property.dataTypeEnum !== undefined && property.dataTypeEnum !== null) || property.onePrimitive || property.manyPrimitive || property.name == 'id' || property.name == 'uri')) {

                var text = property.name;
                if (property.upper == -1) {
                    text += ' [' + property.lower + '..*]';
                } else {
                    text += ' [' + property.lower + '..' + property.upper + ']';
                }

                var li = $('<li />').appendTo(contextMenuUl);
                var adjustedUri = addUiToUrl(property.tumlUri);
                var a = $('<a />', {href: adjustedUri}).appendTo(li);
                a.on('click', function (e) {
                        //Find the link
                        var a = $(e.target);
                        //The row gets saved into the contextMenuUl on right click show menu
                        var row = a.parent().parent().data("row");
                        self.handleContextMenuSelection(a, row);
                        contextMenuUl.hide();
                        e.preventDefault();
                        e.stopImmediatePropagation();
                    }
                );
                var span = $('<span class="contextMenu ui-icon ' + (property.composite ? 'ui-icon-umlcomposition' : 'ui-icon-umlassociation') + '"></span>').appendTo(a);
                a.append(text);
                a.data("contextData", {name: property.name, property: property});
            }
        }

        contextMenuUl.keydown(
            function (e) {
                //Escape key
                if (e.which === 27) {
                    $("#contextMenu" + self.localMetaForData.name).hide();
                    self.grid.focus();
                }
            }
        );
        return contextMenuUl;
    };

    TumlBaseGridManager.prototype.handleContextMenuSelection = function (a, gridRow) {
        var contextData = a.data("contextData");
        if (contextData.name !== 'delete') {
            var url;
            if (contextData.name !== 'self') {
                //If non composite one check if the element exist
                if (!contextData.property.composite && contextData.property.upper == 1) {
                    //For the composite parent property
                    if (!contextData.property.inverseComposite && this.data[gridRow][contextData.property.name].id == null) {
                        alert('Property ' + contextData.property.qualifiedName + ' on ' + this.localMetaForData.name + ' does not exist!\nIt can not be created as it is a non composite property.');
                        return;
                    } else {
                        url = contextData.property.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.data[gridRow].id));
                    }
                } else {
                    url = contextData.property.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.data[gridRow].id));
                }
            } else {
                url = contextData.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.data[gridRow].id));
            }
            this.handleContextMenuClickLink(url);
        } else {
            this.handleDeleteRow(gridRow, this.data);
        }
    }

    TumlBaseGridManager.prototype.setupOptions = function () {
        this.options = {
            autoHeight: false,
            showHeaderRow: true,
            headerRowHeight: 30,
            editable: true,
            enableAddRow: (this.propertyNavigatingTo.composite && !this.propertyNavigatingTo.associationClassOne) ? true : false,
            enableCellNavigation: true,
            asyncEditorLoading: false,
            enableAsyncPostRender: true,
            forceFitColumns: false,
            topPanelHeight: 25,
            autoEdit: false
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
