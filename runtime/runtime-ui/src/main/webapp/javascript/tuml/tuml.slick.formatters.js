/***
 * Contains basic SlickGrid formatters.
 * @module Formatters
 * @namespace Slick
 */

(function ($) {
    // register namespace
    $.extend(true, window, {
        "TumlSlick":{
            "Formatters":{
                "TumlPassword":TumlPasswordFormatter,
                "TumlRequired":TumlRequiredFormatter,
                "TumlRequiredPassword":TumlRequiredPasswordFormatter,
                "TumlDerivedPropertyFormatter":TumlDerivedPropertyFormatter,
                "TumlToOneRequiredFormatter": TumlToOneRequiredFormatter,
                "TumlToAssociationClassRequiredFormatter": TumlToAssociationClassRequiredFormatter,
                "TumlToAssociationClassFormatter": TumlToAssociationClassFormatter,
                "TumlValidationFailedFormatter": TumlValidationFailedFormatter,
                "TumlDelete":TumlDeleteFormatter,
                "TumlBoolean":TumlBooleanFormatter,
                "TumlManyBoolean":TumlManyBooleanFormatter,
                "Link":LinkFormatter,
                "TumlRegularFormatter":TumlRegularFormatter,
                "TumlIdNewFormatter": TumlIdNewFormatter,
                "TumlIdUpdatedFormatter": TumlIdUpdatedFormatter,
                "TumlAssociationComponentFormatter":TumlAssociationComponentFormatter,
                "TumlComponentFormatter":TumlComponentFormatter,
                "UmlgRequiredManyFormatter":UmlgRequiredManyFormatter
            }
        }
    });

    function TumlComponentFormatter(row, cell, value, columnDef, dataContext) {
        return 'component';
    }

    function UmlgRequiredManyFormatter(row, cell, value, columnDef, dataContext) {
        return '[non composite many]';
    }

    function TumlAssociationComponentFormatter(row, cell, value, columnDef, dataContext) {
        return 'association class';
    }

    function TumlRegularFormatter(row, cell, value, columnDef, dataContext) {
        return value;
    }

    function TumlIdNewFormatter(row, cell, value, columnDef, dataContext) {
        return '<span class="umlg-new-id">' + value + '</span>';
    }

    function TumlIdUpdatedFormatter(row, cell, value, columnDef, dataContext) {
        return '<span class="umlg-updated-id">' + value + '</span>';
    }

    function TumlValidationFailedFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>required</div>";
        } else {
            return "<div style='color:red;'>" + value + "</div>";
        }
    }

    function TumlToOneRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === undefined) {
            return "<div style='color:red;'>required</div>";
        } else if (value.id == null) {
            return "<div style='color:red;'>required</div>";
        } else {
            return value.displayName;
        }
    }

    function TumlToAssociationClassRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === undefined) {
            return "<div style='color:red;'>required</div>";
        } else if (value.id == null) {
            return "<div style='color:red;'>required</div>";
        } else {
            if (value.displayName !== undefined && value.displayName !== 'null') {
                return value.id + ' | ' + value.displayName;
            } else if (value.name !== undefined) {
                return value.id + ' | ' + value.name;
            } else {
                return value.id;
            }
        }
    }

    function TumlToAssociationClassFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === undefined) {
            return "";
        } else if (value.id == null) {
            return "";
        } else {
            if (value.displayName !== undefined && value.displayName !== 'null') {
                return value.id + ' | ' + value.displayName;
            } else if (value.name !== undefined) {
                return value.id + ' | ' + value.name;
            } else {
                return value.id;
            }
        }
    }

    function TumlPasswordFormatter(row, cell, value, columnDef, dataContext) {
        return "*****";
    }

    function TumlRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>required</div>";
        } else {
            return value;
        }
    }

    function TumlRequiredPasswordFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>required</div>";
        } else {
            return "*****";
        }
    }

    function TumlDerivedPropertyFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>derived</div>";
        } else {
            return value;
        }
    }

    function TumlDeleteFormatter(row, cell, value, columnDef, dataContext) {
        return "<img class='umlg-delete-img' src='/"+tumlModelName+"/javascript/images/uml2/Pseudostate_terminate.gif'>";
    }

    function TumlBooleanFormatter(row, cell, value, columnDef, dataContext) {
        return value ? "<img src='/"+tumlModelName+"/javascript/images/tick.png'>" : "<img src='/"+tumlModelName+"/javascript/images/cross.png'>";
    }

    function LinkFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "") {
            return "<b class='selfUrlNewRow'>-</b>";
        }
        var url = value.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), dataContext.id);
        return "<b class='selfUrl' tumlUriData='" + url + "'>self</b>"
    }

    function TumlManyBooleanFormatter(row, cell, value, columnDef, dataContext) {
        var result = '';
        if (value == null || value == '') {
            return "";
        }
        for (var i = 0; i < value.length; i++) {
            var booleanValue = value[i];
            if (booleanValue) {
                result += "<img src='/"+tumlModelName+"/javascript/slickgrid/images/tick.png'>";
            } else {
                result += "<img src='/"+tumlModelName+"/javascript/images/delete.png'>";
            }
        }
        return result;
    }

})(jQuery);
