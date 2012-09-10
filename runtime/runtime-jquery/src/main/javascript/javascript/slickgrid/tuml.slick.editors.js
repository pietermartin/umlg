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
                    "SelectCellEditor": SelectCellEditor,
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

    function SelectCellEditor(args) {
        var $select;
        var defaultValue;
        var scope = this;
        var options;

        this.init = function() {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);
            $select.focus();
        };

        function loadSelectOption(args, contextVertexId, defaultValue) {
            var tumlLookupUri = args.column.options.tumlLookupUri;
            var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
            $.ajax({
                type: 'GET',
                url: adjustedUri,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                asycn: false, 
                success: function(response){
                    if (response.meta instanceof Array) {
                        //Property is a many, meta has 2 properties, one for both sides of the association
                        //The first one is the parent property, i.e. the context property for which the menu is built
                        var options = [];
                        $.each(response.data, function(index, obj) {
                            var option = {};
                            option['value'] = obj.id;
                            option['desc'] = obj.name;
                            options.push(option);
                        });
                        for each (var value in options){
                            $select.append($('<option />)').val(value.value).html(value.desc));
                        }

                        $select.val(defaultValue);
                        $select.chosen();
                    } else {
                        //Property is a one
                        alert('this should not happen');
                    }
                },
                error: function(e){

                }
            });
        }

        this.destroy = function() {
            $select.remove();
        };

        this.focus = function() {
            $select.focus();
        };

        this.loadValue = function(item) {
            defaultValue = item[args.column.field];
            args.column.options.compositeLookupMap.getOrLoadMap(item['id']);
            loadSelectOption(args, item['id'], defaultValue);
        };

        this.serializeValue = function() {
            if(args.column.options){
                return $select.val();
            }else{
                return ($select.val() == "yes");
            }
        };

        this.applyValue = function(item,state) {
            item[args.column.field] = parseInt(state);
        };

        this.isValueChanged = function() {
            return ($select.val() != defaultValue);
        };

        this.validate = function() {
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
