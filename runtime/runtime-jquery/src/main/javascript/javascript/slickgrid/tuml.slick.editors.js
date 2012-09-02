/***
 * Contains basic SlickGrid editors.
 * @module Editors
 * @namespace Slick
 */

(function ($) {
    // register namespace
    $.extend(true, window, {
        "Tuml": { 
            "Slick": {
                "Editors": {
                    //"Text": TextEditor,
                    "Integer": IntegerEditor,
                    //"Date": DateEditor,
                    //"YesNoSelect": YesNoSelectEditor,
                    "Checkbox": CheckboxEditor
                    //"PercentComplete": PercentCompleteEditor,
                    //"LongText": LongTextEditor
                }
            }}
    });


    function CheckboxEditor(args) {
        var $select;
        var defaultValue;
        var scope = this;

        this.init = function () {
            $select = $("<INPUT type=checkbox value='true' class='editor-checkbox' hideFocus>");
            $select.appendTo(args.container);
            $select.focus();
        };

        this.destroy = function () {
            $select.remove();
        };

        this.focus = function () {
            $select.focus();
        };

        this.loadValue = function (item) {
            defaultValue = item[args.column.field];
            if (defaultValue) {
                $select.attr("checked", "checked");
            } else {
                $select.removeAttr("checked");
            }
        };

        this.serializeValue = function () {
            return ($select.attr("checked") == "checked");
        };

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            if (($select.attr("checked") == "checked") && defaultValue != true ) {
                return true;
            } else if (($select.attr("checked") !== "checked") && defaultValue != false) {
                return true;
            } else {
                return false;
            }
        };

        this.validate = function () {
            return {
                valid: true,
                msg: null
            };
        };

        this.init();
    }
    function IntegerEditor(args) {
        var $input;
        var defaultValue;
        var scope = this;

        this.init = function () {
            $input = $("<INPUT type=text class='editor-text' />");

            $input.bind("keydown.nav", function (e) {
                if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
                    e.stopImmediatePropagation();
                }
            });

            $input.appendTo(args.container);
            $input.focus().select();
        };

        this.destroy = function () {
            $input.remove();
        };

        this.focus = function () {
            $input.focus();
        };

        this.loadValue = function (item) {
            defaultValue = item[args.column.field];
            $input.val(defaultValue);
            $input[0].defaultValue = defaultValue;
            $input.select();
        };

        this.serializeValue = function () {
            return parseInt($input.val(), 10) || 0;
        };

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };

        this.validate = function () {
            if (isNaN($input.val())) {
                return {
                    valid: false,
                    msg: "Please enter a valid integer"
                };
            }

            return {
                valid: true,
                msg: null
            };
        };

        this.init();
    }
})(jQuery);
