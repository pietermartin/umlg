/**
 * Created with JetBrains WebStorm.
 * User: pieter
 * Date: 2012/12/24
 * Time: 8:00 AM
 * To change this template use File | Settings | File Templates.
 */

(function ($) {

    // register namespace
    $.extend(true, window, {
        Tuml:{
            Query:Query
        }
    });

    function Query(id, name, description, queryString, type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.queryString = queryString;
        this.type = type;
    }

    Query.prototype.getDivName = function () {
        if (this.id !== undefined || this.id !== null || this.id !== -1) {
            return 'queryDivName' + this.id;
        } else {
            return "newQueryDivName";
        }

    }
})(jQuery);


