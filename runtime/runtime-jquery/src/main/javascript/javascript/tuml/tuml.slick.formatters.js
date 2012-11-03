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
                "Link" : LinkFormatter
            }
        }
    });

    function TumlRequiredFormatter(row, cell, value, columnDef, dataContext) {
        if (value == null || value === "") {
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

})(jQuery);
