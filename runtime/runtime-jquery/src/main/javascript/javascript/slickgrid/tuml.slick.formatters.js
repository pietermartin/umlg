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
				"Link" : LinkFormatter
			}
		}
	});

	function LinkFormatter(row, cell, value, columnDef, dataContext) {
		if (value == null || value === "") {
			return "";
		}
		var url = value.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), dataContext.id);
		return "<a href='javascript:void(0)' onclick='change_my_url(\"unused\", \"" + url + "\"); refreshPageTo(\"" + url + "\");' >" + url + "</a>";

	}

})(jQuery);
