(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabGridManager: TumlTabGridManager
        }
    });

    function TumlTabGridManager(tumlUri, propertyNavigatingTo) {

        var self = this;
        var metaForData;
        var mainDataView;
        var mainGrid;

        function init() {
        }

        function refresh(result) {
            metaForData = result.meta.to;
            var tabDiv = $('#' + metaForData.name);
            var myGridDiv = $('<div id="myGrid' + metaForData.name + '" style="width:auto;height:90%;"></div>').appendTo(tabDiv);
            var pagerDiv = $('<div id="pager' + metaForData.name + '" style="width:auto;height:20px;"></div>').appendTo(tabDiv);
            $('#contextMenu' + metaForData.name).remove();
            createGrid(result.data, metaForData, tumlUri, false);
        }

        //Public api
        $.extend(this, {
            "TumlTabGridManagerVersion": "1.0.0",
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
            "refresh": refresh
        });

        function createGrid(data, localMetaForData, tumlUri, forManyLookup) {
            var grid;
            var columns = [];
            var columnFilters = {};
            var lookupColumns;
            var contextVertexId;
            contextVertexId = retrieveVertexId(tumlUri); 

            $.each(localMetaForData.properties, function(index, property) {
                if (!property.inverseComposite && ((property.oneToOne || property.manyToOne) || property.manyPrimitive)) {
                    //Place the id column first
                    if (property.name == "id") {
                        columns.splice(0,0,{
                            id: property.name,
                            name: property.name,
                            field: property.name,
                            sortable: true,
                            formatter: selectFormatter(property)
                        });           
                    } else {
                        columns.push({
                            id: property.name,
                            name: property.name,
                            field: property.name,
                            sortable: true,
                            editor: selectEditor(property),
                            formatter: selectFormatter(property),
                            validator: selectFieldValidator(property),
                            options: {required: property.lower > 0, tumlLookupUri: property.tumlLookupUri,rowEnumerationLookupMap: new RowEnumerationLookupMap(property.qualifiedName, "/restAndJson/tumlEnumLookup"),  rowLookupMap: new RowLookupMap(contextVertexId, property.tumlCompositeParentLookupUri, property.tumlCompositeParentLookupUriOnCompositeParent), compositeParentLookupMap: new CompositeParentLookupMap(contextVertexId, property.tumlLookupUri, property.tumlLookupOnCompositeParentUri), ordered: property.ordered, unique: property.unique},
                            width: 120
                        });
                    }
                }
            });
            if (!forManyLookup) {
                columns.push({id: "uri", name: "uri", field: "uri", sortable: false, formatter: TumlSlick.Formatters.Link});
                columns.push(
                    {id: "delete", name: "delete", field: "delete", sortable: false, 
                        formatter: TumlSlick.Formatters.TumlDelete }
                );
            }

            var options = {
                showHeaderRow: true,
                headerRowHeight: 30,
                editable: !forManyLookup,
                enableAddRow: propertyNavigatingTo.composite,
                enableCellNavigation: true,
                asyncEditorLoading: false,
                enableAsyncPostRender: true,
                forceFitColumns: false,
                topPanelHeight: 25
            };

            var sortcol = "name";
            var sortdir = 1;
            var percentCompleteThreshold = 0;
            var searchString = ""; 
            var dataView = new Tuml.Slick.Data.DataView(); 
            if (!forManyLookup) {
                grid = new Slick.Grid("#myGrid" + localMetaForData.name , dataView, columns, options); 
                var pager = new Slick.Controls.Pager(dataView, grid, $("#pager" + localMetaForData.name));
                mainGrid = grid;
                mainDataView = dataView;
                $("<div id='grid-button" + localMetaForData.name + "' class='grid-button'/>").appendTo('#pager' + localMetaForData.name + ' .slick-pager-settings');
            } else {
                grid = new Slick.Grid("#myGridLookup" + localMetaForData.name , dataView, columns, options); 
                var pager = new Slick.Controls.Pager(dataView, grid, $("#pagerLookup" + localMetaForData.name));
                $("<div id='grid-buttonLookup" + localMetaForData.name + "' class='grid-button'/>").appendTo('#pagerLookup' + localMetaForData.name + ' .slick-pager-settings');
            }
            grid.setSelectionModel(new Slick.RowSelectionModel()); 
            var columnpicker = new Slick.Controls.ColumnPicker(columns, grid, options);

            if (!forManyLookup && !propertyNavigatingTo.composite) {
                //if grid is for non composite many include a add button, this will bring up a grid with the values to add
                var $addButton = $('<button />').text('Add').click(function() {
                    if (grid.getEditorLock().commitCurrentEdit()) {
                        //hide the current grid
                        $('#myGrid' + localMetaForData.name).hide();
                        $('#pager' + localMetaForData.name).hide();
                        var adjustedUri = propertyNavigatingTo.tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                        $.ajax({
                            url: adjustedUri,
                            type: "GET",
                            dataType: "json",
                            contentType: "json",
                            success: function(lookupResult, textStatus, jqXHR) {
                                var tabContainer = $('#tab-container');
                                var lookupMeta = lookupResult.meta.to;
                                var tabDiv = $('<div />', {id: lookupMeta.name, title: lookupMeta.name}).appendTo(tabContainer);
                                var selectHeader = $('<div id="myGridLookupHeader" class="selectheader" />').append($('<p />').text('Select the values to add.')).appendTo(tabDiv);
                                var myGridDiv = $('<div id="myGridLookup' + lookupMeta.name + '" style="width:auto;height:84%;"></div>').appendTo(tabDiv);
                                var pagerDiv = $('<div id="pagerLookup' + lookupMeta.name + '" style="width:auto;height:20px;"></div>').appendTo(tabDiv);
                                removeElementsAlreadyInGrid(dataView.getNewItems(), lookupResult.data);
                                createGrid(lookupResult.data, lookupMeta, adjustedUri, true);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                alert('fail for non composite lookup');
                            }
                        });
                    }
                }).appendTo('#grid-button' + localMetaForData.name);;
            }
            
            if (!forManyLookup) {
                var $saveButton = $('<button />').text('Save').click(function() {
                    if (grid.getEditorLock().commitCurrentEdit()) {
                        //put updated items
                        if (dataView.getUpdatedItems().length !== 0) {
                            $.ajax({
                                url: tumlUri,
                                type: "PUT",
                                dataType: "json",
                                contentType: "json",
                                data: JSON.stringify(dataView.getUpdatedItems()),
                                success: function(data, textStatus, jqXHR) {
                                    self.onPutSuccess.notify({tumlUri: tumlUri, tabId: localMetaForData.name, data: data}, null, self);
                                },
                                error: function(jqXHR, textStatus, errorThrown) {
                                    self.onPutFailure.notify({tumlUri: tumlUri, tabId: localMetaForData.name}, null, self);
                                }
                            });
                        }
                        //post new items
                        if (dataView.getNewItems().length !== 0) {
                            $.ajax({
                                url: tumlUri,
                                type: "POST",
                                dataType: "json",
                                contentType: "json",
                                data: JSON.stringify(dataView.getNewItems()),
                                success: function(data, textStatus, jqXHR) {
                                    self.onPostSuccess.notify({tumlUri: tumlUri, tabId: localMetaForData.name, data: data}, null, self);
                                },
                                error: function(jqXHR, textStatus, errorThrown) {
                                    self.onPostFailure.notify({tumlUri: tumlUri, tabId: localMetaForData.name}, null, self);
                                }
                            });
                        }
                        //delete new items
                        if (dataView.getDeletedItems().length !== 0) {
                            $.ajax({
                                url: tumlUri,
                                type: "DELETE",
                                dataType: "json",
                                contentType: "json",
                                data: JSON.stringify(dataView.getDeletedItems()),
                                success: function(data, textStatus, jqXHR) {
                                    self.onDeleteSuccess.notify({tumlUri: tumlUri, tabId: localMetaForData.name, data: data}, null, self);
                                },
                                error: function(jqXHR, textStatus, errorThrown) {
                                    self.onDeleteFailure.notify({tumlUri: tumlUri, tabId: localMetaForData.name}, null, self);
                                }
                            });
                        }
                    } else {
                        alert("Commit failed on current active cell!");
                    }
                }).appendTo('#grid-button' + localMetaForData.name);

                var $cancelButton = $('<button />').text('Cancel').click(function() {
                    if (grid.getEditorLock().commitCurrentEdit()) {
                        $.ajax({
                            url: tumlUri,
                            type: "GET",
                            dataType: "json",
                            contentType: "json",
                            data: JSON.stringify(dataView.getDeletedItems()),
                            success: function(data, textStatus, jqXHR) {
                                self.onCancel.notify({tumlUri: tumlUri, tabId: localMetaForData.name, data: data}, null, self);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                            }
                        });
                    }
                }).appendTo('#grid-button' + localMetaForData.name);
            }
            if (forManyLookup) {
                var $selectButton = $('<button />').text('Select').click(function() {
                    if (grid.getEditorLock().commitCurrentEdit()) {
                        var selectedRows = grid.getSelectedRows();
                        for (var i = 0; i < selectedRows.length; i++) {
                            var selectedRow  = selectedRows[i];
                            var item = dataView.getItem(selectedRow);
                            mainDataView.addItem(item);
                            mainGrid.invalidateRows([data.length - 1]);
                            mainGrid.updateRowCount();
                            mainGrid.render();
                            $('#myGridLookupHeader').remove();
                            $('#myGridLookup' + metaForData.name).remove();
                            $('#pagerLookup' + metaForData.name).remove();
                            $('#myGrid' + localMetaForData.name).show();
                            $('#pager' + localMetaForData.name).show();
                        }
                    }
                }).appendTo('#grid-buttonLookup' + localMetaForData.name);

                var $cancelButton = $('<button />').text('Cancel').click(function() {
                    if (grid.getEditorLock().commitCurrentEdit()) {
                        $('#myGridLookupHeader').remove();
                        $('#myGridLookup' + metaForData.name).remove();
                        $('#pagerLookup' + metaForData.name).remove();
                        $('#myGrid' + localMetaForData.name).show();
                        $('#pager' + localMetaForData.name).show();
                    }
                }).appendTo('#grid-buttonLookup' + localMetaForData.name);
            }

            //Create context menu
            var contextMenuUl = $('<ul />', {id: 'contextMenu' + localMetaForData.name, style: 'display:none;position:absolute', class: 'contextMenu'}).appendTo('body');
            $('<b />').text('Nav').appendTo(contextMenuUl);
            $('<li data="' + localMetaForData.uri + '" />').text("self").appendTo(contextMenuUl);
            $.each(localMetaForData.properties, function(index, property) {
                if (property.inverseComposite || !((property.dataTypeEnum !== undefined &&  property.dataTypeEnum !== null) || property.onePrimitive || property.manyPrimitive || property.name == 'id' || property.name == 'uri')) {
                    $('<li data="' + property.tumlUri + '" />').text(property.name).appendTo(contextMenuUl);
                }
            });
            $('<li data="delete" />').text("delete").appendTo(contextMenuUl);

            grid.onContextMenu.subscribe(function (e) {
                e.preventDefault();
                var cell = grid.getCellFromEvent(e);
                $("#contextMenu" + localMetaForData.name)
                .data("row", cell.row)
                .css("top", e.pageY)
                .css("left", e.pageX)
                .show();

                $("body").one("click", function () {
                    $("#contextMenu" + localMetaForData.name).hide();
                });
                $('#contextMenu' + localMetaForData.name).mouseleave(contextMenu_timer);
            });

            function contextMenu_close() {  
                $("#contextMenu" + localMetaForData.name).hide();
            }

            function contextMenu_timer() {  
                closetimer = window.setTimeout(contextMenu_close, 200);
            }

            $("#contextMenu" + localMetaForData.name).click(function (e) {
                if (!$(e.target).is("li")) {
                    return;
                }
                var row = $(this).data("row");
                var tumlUri = $(e.target).attr("data");
                if (tumlUri !== 'delete') {
                    var url = tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), data[row].id);
                    self.onContextMenuClickLink.notify({name: 'unused', tumlUri: url}, null, self);
                } else {
                    var item = dataView.getItem(row);
                    dataView.deleteItem(item.id);
                }
            }); 

            grid.onClick.subscribe(function(e, args) {
                if (grid.getColumns()[args.cell].name == 'delete') {
                    var item = dataView.getItem(args.row);
                    dataView.deleteItem(item.id);
                } else if (grid.getColumns()[args.cell].name == 'uri') {
                    var item = dataView.getItem(args.row);
                    var uri = item.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), item.id);
                    self.onSelfCellClick.notify({name: 'unused', tumlUri: uri}, null, self);
                }
            });

            grid.onCellChange.subscribe(function(e, args) {
                dataView.updateItem(args.item.id, args.item, grid.getColumns()[args.cell]);
            });

            grid.onAddNewRow.subscribe(function(e, args) {
                var $newItem = {};
                for (i = 0; i < grid.getColumns().length; i++) {
                    var column = grid.getColumns()[i];
                    $newItem[column.name] = null;
                }
                //Generate a fake id, its required for the grid to work nicely
                $newItem.id = 'fake::' + data.length + dataView.getNewItems().length + 1;
                dataView.addItem($.extend($newItem, args.item));
            });

            grid.onKeyDown.subscribe(function(e) {
                // select all rows on ctrl-a
                if (e.which != 65 || !e.ctrlKey) {
                    return false;
                }

                var rows = [];
                for (var i = 0; i < dataView.getLength(); i++) {
                    rows.push(i);
                }

                grid.setSelectedRows(rows);
                e.preventDefault();
            });

            grid.onSort.subscribe(function(e, args) {
                sortdir = args.sortAsc ? 1 : -1;
                sortcol = args.sortCol.field;

                if ($.browser.msie && $.browser.version <= 8) {
                    // using temporary Object.prototype.toString override
                    // more limited and does lexicographic sort only by default, but can be much faster
                    var percentCompleteValueFn = function() {
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
                    dataView.fastSort((sortcol == "percentComplete") ? percentCompleteValueFn : sortcol, args.sortAsc);
                } else {
                    // using native sort with comparer
                    // preferred method but can be very slow in IE with huge datasets
                    dataView.sort(function comparer(a, b) {
                        var x = a[sortcol],
                        y = b[sortcol];
                        return (x == y ? 0 : (x > y ? 1 : -1));
                    }, args.sortAsc);
                }
            });

            // wire up model events to drive the grid
            dataView.onRowCountChanged.subscribe(function(e, args) {
                grid.updateRowCount();
                grid.render();
            });

            dataView.onRowsChanged.subscribe(function(e, args) {
                grid.invalidateRows(args.rows);
                grid.render();
            });

            dataView.onPagingInfoChanged.subscribe(function(e, pagingInfo) {
                var isLastPage = pagingInfo.pageNum == pagingInfo.totalPages - 1;
                var options = grid.getOptions();
                var enableAddRow = options.enableAddRow && (isLastPage || pagingInfo.pageSize == 0);

                if (options.enableAddRow != enableAddRow) {
                    grid.setOptions({
                        enableAddRow: enableAddRow
                    });
                }
            });

            $(grid.getHeaderRow()).delegate(":input", "change keyup", function(e) {
                columnFilters[$(this).data("columnId")] = $.trim($(this).val());
                dataView.refresh();
            });

            grid.onColumnsReordered.subscribe(function(e, args) {
                updateHeaderRow(localMetaForData);
            });

            grid.onColumnsResized.subscribe(function(e, args) {
                updateHeaderRow(localMetaForData);
            });

            grid.onViewportChanged.subscribe(function (e, args) {
                var vp = grid.getViewport();
            });
            grid.onViewportChanged.notify();

            function updateHeaderRow(metaForData) {
                for (var i = 0; i < columns.length; i++) {
                    if (columns[i].id !== "selector" && columns[i].id !== 'uri' && columns[i].id !== 'delete') {
                        var header = grid.getHeaderRowColumn(columns[i].id);
                        $(header).empty();
                        $(constructHeaderRowBox(columns[i].id, metaForData)).data("columnId", columns[i].id).val(columnFilters[columns[i].id]).appendTo(header);
                    }
                }
            }

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
            }

            function filter(item, metaForData) {
                for (var columnId in columnFilters) {
                    if (columnId !== undefined && columnFilters[columnId] !== "") {
                        var c = grid.getColumns()[grid.getColumnIndex(columnId)];
                        for (var i = 0; i < metaForData.metaForData.properties.length; i++) {
                            var property = metaForData.metaForData.properties[i];
                            if (property.name == columnId) {
                                if (property.dataTypeEnum !== undefined) {
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
                                    if (item[c.field].displayName == undefined &&  item[c.field] != columnFilters[columnId]) {
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

            $(".grid-header .ui-icon").addClass("ui-state-default ui-corner-all").mouseover(function(e) {
                $(e.target).addClass("ui-state-hover")
            }).mouseout(function(e) {
                $(e.target).removeClass("ui-state-hover")
            });

            grid.onValidationError.subscribe(function (e, args) {
                alert(args.validationResults.msg);
            });

            // initialize the model after all the events have been hooked up
            dataView.beginUpdate();
            dataView.setItems(data);
            dataView.setFilterArgs({
                metaForData: localMetaForData
            });
            dataView.setFilter(filter);
            dataView.endUpdate();
            updateHeaderRow(localMetaForData);

            // if you don't want the items that are not visible (due to being filtered out
            // or being on a different page) to stay selected, pass 'false' to the second arg
            dataView.syncGridSelection(grid, true);

            $("#gridContainer").resizable();
        }

        function removeElementsAlreadyInGrid(items, itemsToAdd) {
            var elementsToRemoveIndex = [];
            for (var i = 0; i < items.length; i++) {
                var newItem = items[i];
                for (var j = 0; j < itemsToAdd.length; j++) {
                    if (newItem.id == itemsToAdd[j].id) {
                        elementsToRemoveIndex.push(i);
                    }
                }
            }
            for (var i = 0; i < elementsToRemoveIndex.length; i++) {
                itemsToAdd.splice(i, 1);
            }
        }

        init();
    }

})(jQuery);
