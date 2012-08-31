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
        } else {
            //Property is a one
            isOne = true;
            $.each(response.meta.properties, function(index, metaProperty) {
                menuArray[index] = metaProperty;
            });
            classNameLowerCased = response.meta.name.toLowerCase();
            metaForData = response.meta;
            createPageForOne(response.data, metaForData);
        }

        var vertexId = tumlUri.match(/\d+/);
        if (vertexId != null) {
            createLeftMenu(menuArray, classNameLowerCased, vertexId[0]);
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

function createPageForOne(data, metaForData) {
    //First destroy the grid if it exist
    $('.ui-layout-center').children().remove();

    $('<div>' + metaForData.name + '</div>').appendTo('.ui-layout-center');

    $.each(metaForData.properties, function(index, property) {
        if (property.onePrimitive) {

            $.each(data, function(index, value) {
                if (value.name == property.name) {
                    $('<div><label for="' + property.name + 'Id">' + property.name + '</label><input id="' + property.name + 'Id" type="text" class="field" name="' + property.name + '" value="' + value.value + '" /></div>').appendTo('.ui-layout-center');
                }
            });

        }
    });

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
             columns.push({
                 id: property.name,
                 name: property.name,
                 field: property.name,
                 sortable: true,
                 editor: property.name == 'uri' ? null : Slick.Editors.Text,
                 validator: requiredFieldValidator,
                 formatter: property.name !== 'uri' ? null : TumlSlick.Formatters.Link
             });
         }
     });

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
        updateHeaderRow();
    });

    grid.onColumnsResized.subscribe(function(e, args) {
        updateHeaderRow();
    });

    function updateHeaderRow() {
        for (var i = 0; i < columns.length; i++) {
            if (columns[i].id !== "selector") {
                var header = grid.getHeaderRowColumn(columns[i].id);
                $(header).empty();
                $("<input type='text'>").data("columnId", columns[i].id).val(columnFilters[columns[i].id]).appendTo(header);
            }
        }
    }

    function filter(item, metaForData) {
        for (var columnId in columnFilters) {
            if (columnId !== undefined && columnFilters[columnId] !== "") {
                var c = grid.getColumns()[grid.getColumnIndex(columnId)];
                if (item[c.field].indexOf(columnFilters[columnId]) == -1) {
                    return false;
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
    updateHeaderRow();

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


function createLeftMenu(menuArray, classNameLowerCased, vertexId) {
    $('.ui-left-menu-link').children().remove();
    $.each(menuArray, function(index, value) {
        var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), vertexId);
        // var adjustedUri = menuArray[index].tumlUri.replace('{' + classNameLowerCased + 'Id}', vertexId);
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
