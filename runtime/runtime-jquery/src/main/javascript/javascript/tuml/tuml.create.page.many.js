function createPageForMany(data, metaForData, tumlUri) {
    $('.ui-layout-center').children().remove();
    $('<div id="gridDiv" class="grid-header" style="width:100%"><label>' + metaForData.name + ':</label></div>)').appendTo(".ui-layout-center");
    $('<div id="myGrid" style="width:100%;height:90%;"></div>').appendTo(".ui-layout-center");
    $('<div id="pager" style="width:100%;height:20px;"></div>').appendTo(".ui-layout-center");
    createGrid(data, metaForData, tumlUri);
}

function createGrid(data, metaForData, tumlUri) {
    var grid;
    var columns = [];
    var columnFilters = {};
    var lookupColumns;

    $.each(metaForData.properties, function(index, property) {
        if (!property.inverseComposite && (property.oneToOne || property.manyToOne)) {
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
                    options: {required: property.lower > 0, tumlLookupUri: property.tumlLookupUri, rowLookupMap: new RowLookupMap(property.tumlCompositeParentLookupUri), compositeParentLookupMap: new CompositeParentLookupMap(property.tumlLookupUri)},
                    width: 120
                });
            }
        }
    });

    function selectFormatter(property) {
        if (property.name == 'uri') {
            return TumlSlick.Formatters.Link;
        } else if (property.name == 'id') {
            return null;
        } else if (property.dataTypeEnum !== undefined) {
            return null;
        } else if (!property.onePrimitive && !property.manyPrimitive) {
            return function waitingFormatter(row, cell, value, columnDef, dataContext) {
                return value.displayName;
            };
            return null;
        } else if (property.fieldType == 'String') {
            return  null; 
        } else if (property.fieldType == 'Boolean') {
            return Slick.Formatters.Checkmark;
        } else {
            return null; 
        }
    }

    function selectEditor(property) {
        if (property.name == 'uri') {
            return null;
        } else if (property.dataTypeEnum !== undefined) {
            if (property.dataTypeEnum == 'Date') {
                return  Tuml.Slick.Editors.Date; 
            } else if (property.dataTypeEnum == 'Time') {
                return  Tuml.Slick.Editors.Time; 
            } else if (property.dataTypeEnum == 'DateTime') {
                return  Tuml.Slick.Editors.DateTime; 
            } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
                return null; 
            } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
                return null; 
            } else if (property.dataTypeEnum == 'Email') {
                return null; 
            } else if (property.dataTypeEnum == 'Video') {
                return null; 
            } else  if (property.dataTypeEnum == 'Audio') {
                return null; 
            } else if (property.dataTypeEnum == 'Image') {
                return null; 
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
        } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
            return  Tuml.Slick.Editors.SelectCellEditor; 
        } else if (property.name == 'id') {
            return null;
        } else if (property.fieldType == 'String') {
            return  Slick.Editors.Text; 
        } else if (property.fieldType == 'Integer') {
            return Tuml.Slick.Editors.Integer;
        } else if (property.fieldType == 'Long') {
            return Slick.Editors.Integer;
        } else if (property.fieldType == 'Boolean') {
            return Tuml.Slick.Editors.Checkbox;
        } else {
            return  Slick.Editors.Text; 
        }
    }

    function selectFieldValidator(property) {
        if (property.name == 'uri') {
        } else if (property.dataTypeEnum !== undefined) {
            if (property.dataTypeEnum == 'Date') {
            } else if (property.dataTypeEnum == 'Time') {
            } else if (property.dataTypeEnum == 'DateTime') {
            } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
            } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
            } else if (property.dataTypeEnum == 'Email') {
            } else if (property.dataTypeEnum == 'Video') {
            } else  if (property.dataTypeEnum == 'Audio') {
            } else if (property.dataTypeEnum == 'Image') {
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
        } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
        } else if (property.name == 'id') {
        } else if (property.fieldType == 'String') {
        } else if (property.fieldType == 'Integer') {
            return function (value) {
                if (property.lower > 0 && (value == '' || value == undefined || value == null)) {
                    return {
                        valie: false,
                        msg: property.name + " is a required field!"
                    };
                }
                if (isNaN(parseInt(value, 10))) {
                    return {
                        valid: false,
                        msg: "Please enter a valid positive number"
                    };
                }
                if (property.validations.range !== undefined) {
                    var intValue =  parseInt(value, 10);
                    if (intValue < property.validations.range.min || intValue > property.validations.range.max) {
                        return {
                            valid:false,
                            msg: "Value need to be between " + property.validations.range.min + " and " + property.validations.range.max
                        }
                    }
                }
                return {
                    valid: true,
                    msg: null
                };
            };
        } else if (property.fieldType == 'Long') {
            return function () {
                if (isNaN(parseInt($input.val(), 10))) {
                    return {
                        valid: false,
                        msg: "Please enter a valid positive number"
                    };
                }

                return {
                    valid: true,
                    msg: null
                };
            };
        } else if (property.fieldType == 'Boolean') {
        } else {
        }
        return function() {
            return {
                valid: true,
                msg: null}
        };
    }
    var options = {
        showHeaderRow: true,
        headerRowHeight: 30,
        editable: true,
        enableAddRow: true,
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


    dataView = new Slick.Data.DataView();
    grid = new Slick.Grid("#myGrid", dataView, columns, options);
    grid.setSelectionModel(new Slick.RowSelectionModel());
    var pager = new Slick.Controls.Pager(dataView, grid, $("#pager"));
    var columnpicker = new Slick.Controls.ColumnPicker(columns, grid, options);
    $("<div class='grid-button'/>").appendTo('.slick-pager');

    var $saveButton = $('<button />').text('Save').click(function() {
        if (grid.getEditorLock().commitCurrentEdit()) {
            $.ajax({
                url: tumlUri,
                type: "POST",
                dataType: "json",
                contentType: "json",
                data: JSON.stringify(data),
                success: function() {
                    refreshPageTo(tumlUri);
                },
                error: function() {
                    alert("fail :-(");
                }
            });
        } else {
            alert("Commit failed on current active cell!");
        }
    }).appendTo('.grid-button');

    var $cancelButton = $('<button />').text('Cancel').click(function() {
        refreshPageTo(tumlUri);
    }).appendTo('.grid-button');

    grid.onCellChange.subscribe(function(e, args) {
        dataView.updateItem(args.item.id, args.item);
    });

    grid.onAddNewRow.subscribe(function(e, args) {
        var item = {
            "num": data.length,
            "id": "new_" + (Math.round(Math.random() * 10000)),
            "title": "New task",
            "duration": "1 day",
            "percentComplete": 0,
            "start": "01/01/2009",
            "finish": "01/01/2009",
            "effortDriven": false
        };
        $.extend(item, args.item);
        dataView.addItem(item);
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
        var enableAddRow = isLastPage || pagingInfo.pageSize == 0;
        var options = grid.getOptions();

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
        updateHeaderRow(metaForData);
    });

    grid.onColumnsResized.subscribe(function(e, args) {
        updateHeaderRow(metaForData);
    });

    grid.onViewportChanged.subscribe(function (e, args) {
        var vp = grid.getViewport();
        console.log(vp);
        //loader.ensureData(vp.top, vp.bottom);
    });
    grid.onViewportChanged.notify();

    function updateHeaderRow(metaForData) {
        for (var i = 0; i < columns.length; i++) {
            if (columns[i].id !== "selector" && columns[i].id !== 'uri') {
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
                return "<input type='text'>";
            }
        }
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
        metaForData: metaForData
    });
    dataView.setFilter(filter);
    dataView.endUpdate();
    updateHeaderRow(metaForData);

    // if you don't want the items that are not visible (due to being filtered out
    // or being on a different page) to stay selected, pass 'false' to the second arg
    dataView.syncGridSelection(grid, true);

    $("#gridContainer").resizable();
}
