/***
 * Contains basic SlickGrid formatters.
 * @module Formatters
 * @namespace Slick
 */

(function($) {
    // register namespace
    $.extend(true, window, {
        "TumlSlick" : {
            "Formatters" : {
                "TumlRequired" : TumlRequiredFormatter,
                "TumlDelete" : TumlDeleteFormatter,
                "TumlManyBoolean" : TumlManyBooleanFormatter,
                "Link" : LinkFormatter,
                "TumlRegularFormatter": TumlRegularFormatter,
                "TumlComponentFormatter": TumlComponentFormatter
            }
        }
    });

    function TumlComponentFormatter(row, cell, value, columnDef, dataContext) {
        return 'component';
    }

    function TumlRegularFormatter(row, cell, value, columnDef, dataContext) {
        return value;
    }

    function TumlRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "" || value.length === 0) {
            return "<div style='color:red;'>required</div>";
        } else {
            return value;
        }
    }

    function TumlDeleteFormatter(row, cell, value, columnDef, dataContext) {
        return "<img class='tuml-delete-img' src='/restAndJson/javascript/images/delete.png'>";
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
            if (booleanValue === 'true') {
                result += "<img src='/restAndJson/javascript/slickgrid/images/tick.png'>";
            } else {
                result += "<img src='/restAndJson/javascript/images/delete.png'>";
            }
        }
        return result;
    }

})(jQuery);
