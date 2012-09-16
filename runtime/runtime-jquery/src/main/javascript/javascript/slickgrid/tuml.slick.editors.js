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
                    "Integer": IntegerEditor,
                    "SelectCellEditor": SelectCellEditor,
                    "Checkbox": CheckboxEditor
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
        var currentValue;
        var scope = this;
        var options;

        this.init = function() {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);
            $select.focus();
        };

        this.destroy = function() {
            $select.remove();
        };

        this.focus = function() {
            $select.focus();
        };

        this.loadValue = function(item) {
            if (!args.column.options.required) {
                $select.append($('<option />)').val("").html(""));
            }
            //currentValue is the vertex id of the oneToOne or manyToOne
            currentValue = item[args.column.field];
            //append the current value to the dropdown
            $select.append($('<option />)').val(currentValue.id).html(currentValue.displayName));
            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
                    $.each(compositeParentMap[compositeParentVertexId], function(index, obj) {
                        $select.append($('<option />)').val(obj.id).html(obj.displayName));
                    });
                    $select.val(currentValue.id);
                    if (!args.column.options.required) {
                        $select.chosen({allow_single_deselect: true});
                    } else {
                        $select.chosen();
                    }
                });
            });
        };

        this.serializeValue = function() {
            if(args.column.options){
                var options = $select.children();
                for (var i = 0; i < options.length; i++) {
                    if (options[i].selected) {
                        return {id: parseInt($select.val()), displayName: options[i].label};
                        break;
                    }
                }
            }else{
                return ($select.val() == "yes");
            }
        };

        this.applyValue = function(item,state) {
            var previousValue = item[args.column.field];
            item[args.column.field] = state;
            //remove state from the map and add the previousValue
            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
                    removeByValue(compositeParentMap[compositeParentVertexId], state);
                    compositeParentMap[compositeParentVertexId].push(previousValue);
                });
            });
        };

        function removeByValue(compositeLookup, toRemove) {
            for(var i=0; i<compositeLookup.length; i++) {
                if(compositeLookup[i].id == toRemove.id) {
                    compositeLookup.splice(i, 1);
                    break;
                }
            }
        }
        this.isValueChanged = function() {
            return ($select.val() != currentValue.id);
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
