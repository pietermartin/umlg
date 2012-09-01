//This adds in the /ui part after /${app.rootUrl}

function change_my_url(title, url) {
    var urlToPush = '/restAndJson/ui' + url.substring('restAndJson/'.length);
    history.pushState({}, title, urlToPush);
}

function refreshPageTo(tumlUri) {

    var jqxhr = $.getJSON(tumlUri, function(response, b, c) {
        var classNameLowerCased;
        var menuArray = [];
        var metaForData = {};
        var isOne;

        var contextVertexId = tumlUri.match(/\d+/);
        if (response.meta instanceof Array) {
            //Property is a many, meta has 2 properties, one for both sides of the association
            //The first one is the parent property, i.e. the context property for which the menu is built
            isOne = false;
            var contextMeta = response.meta[0];
            classNameLowerCased = contextMeta.name.toLowerCase();
            $.each(contextMeta.properties, function(index, metaProperty) {
                menuArray[index] = metaProperty;
            });
            metaForData = response.meta[1];
            createPageForMany(response.data, metaForData, tumlUri);
            contextVertexId = tumlUri.match(/\d+/);
        } else {
            //Property is a one
            isOne = true;
            $.each(response.meta.properties, function(index, metaProperty) {
                menuArray[index] = metaProperty;
            });
            classNameLowerCased = response.meta.name.toLowerCase();
            metaForData = response.meta;
            createPageForOne(response.data, metaForData, tumlUri);
            contextVertexId = response.data.id;
        }
        if (contextVertexId != null) {
            createLeftMenu(menuArray, classNameLowerCased, contextVertexId);
        } else {
            createLeftMenu(menuArray);
        }

    }).fail(function(a, b, c) {
        alert("error " + a + ' ' + b + ' ' + c);
    });

}

function createPageForMany(data, metaForData, tumlUri) {
    $('.ui-layout-center').children().remove();
    $('<div id="gridDiv" class="grid-header" style="width:100%"><label>' + metaForData.name + ':</label></div>)').appendTo(".ui-layout-center");
    $('<div id="myGrid" style="width:100%;height:90%;"></div>').appendTo(".ui-layout-center");
    $('<div id="pager" style="width:100%;height:20px;"></div>').appendTo(".ui-layout-center");
    createGrid(data, metaForData, tumlUri);
}

function createPageForOne(data, metaForData, tumlUri) {
    //First destroy the grid if it exist
    $('.ui-layout-center').children().remove();

    $('<div>' + metaForData.name + '</div>').appendTo('.ui-layout-center');
    $('<div id="formDiv"></div>').appendTo('.ui-layout-center');
    $.each(metaForData.properties, function(index, property) {
        if (property.onePrimitive) {
            $('<div><label for="' + property.name + 'Id">' + property.name + '</label><input id="' + property.name  + 'Id" type="text" class="field" name="' + property.name + '" value="' + data[property.name]+ '" /></div>').appendTo('#formDiv');
        }
    });
    $('#formDiv').wrap('<form id="formId"></form>');

    var $saveButton = $('<button />').text('Save').click(function() {
        $.ajax({
            url: tumlUri,
            type: "PUT",
            dataType: "json",
            contentType: "json",
            data: formToJson(),
            success: function() {
                refreshPageTo(tumlUri);
            },
            error: function() {
                alert("fail :-(");
            }
        });
        return false ;
    }).appendTo('#formDiv');

    function formToJson() {
        var dataToSend = {};
        $.each(metaForData.properties, function(index, property) {
            if (property.onePrimitive) {
                if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                    dataToSend[property.name] = parseInt($('#' + property.name + 'Id').val());
                } else if (property.fieldType == 'String') {
                    dataToSend[property.name] = $('#' + property.name + 'Id').val();
                }
            }
        });
        //var result = $('#formId').serializeObject();
        //alert(result);
        var result = JSON.stringify(dataToSend);
        return result;
    }
}

function requiredFieldValidator(value) {
    if (value == null || value == undefined || !value.length) {
        return {
            valid: false,
            msg: "This is a required field"
        };
    } else {
        return {
            valid: true,
            msg: null
        };
    }
}

function createGrid(data, metaForData, tumlUri) {
    var grid;
    var columns = [];
    var columnFilters = {};

     $.each(metaForData.properties, function(index, property) {
         if (!property.inverseComposite && (property.oneToOne || property.manyToOne)) {
             //Place the id column first
             if (property.name == "id") {
                  columns.splice(0,0,{
                     id: property.name,
                     name: property.name,
                     field: property.name,
                     sortable: true,
                     editor: selectEditor(property),
                     validator: requiredFieldValidator,
                     formatter: selectFormatter(property)
                 });           
             } else {
                 columns.push({
                     id: property.name,
                     name: property.name,
                     field: property.name,
                     sortable: true,
                     editor: selectEditor(property),
                     validator: requiredFieldValidator,
                     formatter: selectFormatter(property)
                 });
             }
         }
     });

    function selectFormatter(property) {
         if (property.name == 'uri') {
            return TumlSlick.Formatters.Link;
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
        } else if (property.name == 'id') {
            return null;
        } else if (property.fieldType == 'String') {
           return  Slick.Editors.Text; 
        } else if (property.fieldType == 'Integer') {
           return Slick.Editors.Integer;
        } else if (property.fieldType == 'Long') {
           return Slick.Editors.Integer;
        } else if (property.fieldType == 'Boolean') {
           return Tuml.Editors.Checkbox;
        } else {
           return  Slick.Editors.Text; 
        }
    }

    var options = {
        showHeaderRow: true,
        headerRowHeight: 30,
        editable: true,
        enableAddRow: true,
        enableCellNavigation: true,
        asyncEditorLoading: true,
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
                        if (property.fieldType == 'String') {
                            if (item[c.field].indexOf(columnFilters[columnId]) == -1) {
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


function requiredFieldValidator(value) {
    if (value == null || value == undefined || !value.length) {
        return {
            valid: false,
            msg: "This is a required field"
        };
    } else {
        return {
            valid: true,
            msg: null
        };
    }
}


function createLeftMenu(menuArray, classNameLowerCased, contextVertexId) {
    $('.ui-left-menu-link').children().remove();
    $.each(menuArray, function(index, value) {
        var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        // var adjustedUri = menuArray[index].tumlUri.replace('{' + classNameLowerCased + 'Id}', contextVertexId);
        $('.ui-left-menu-link').append('<li>').append(
            $('<a>', {
            text: menuArray[index].name,
            title: menuArray[index].name,
            href: adjustedUri,
            click: function() {
                change_my_url(menuArray[index].name, adjustedUri);
                refreshPageTo(adjustedUri);
                return false;
            }
        })).append('</li>');
    });
}
