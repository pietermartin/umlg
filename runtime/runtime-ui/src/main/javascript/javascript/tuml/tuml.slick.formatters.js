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
                "TumlRequired":TumlRequiredFormatter,
                "TumlToOneRequiredFormatter": TumlToOneRequiredFormatter,
                "TumlToAssociationClassRequiredFormatter": TumlToAssociationClassRequiredFormatter,
                "TumlValidationFailedFormatter": TumlValidationFailedFormatter,
                "TumlDelete":TumlDeleteFormatter,
                "TumlBoolean":TumlBooleanFormatter,
                "TumlManyBoolean":TumlManyBooleanFormatter,
                "Link":LinkFormatter,
                "TumlRegularFormatter":TumlRegularFormatter,
                "TumlIdNewFormatter": TumlIdNewFormatter,
                "TumlIdUpdatedFormatter": TumlIdUpdatedFormatter,
                "TumlComponentFormatter":TumlComponentFormatter
            }
        }
    });

    function TumlComponentFormatter(row, cell, value, columnDef, dataContext) {
        return 'component';
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

    function TumlRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>required</div>";
        } else {
            return value;
        }
    }

    function TumlDeleteFormatter(row, cell, value, columnDef, dataContext) {
        return "<img class='umlg-delete-img' src='/"+tumlModelName+"/javascript/images/delete.png'>";
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
