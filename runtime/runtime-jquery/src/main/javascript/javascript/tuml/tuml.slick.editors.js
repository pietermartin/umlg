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
                    "Text": TextEditor,
                    "SelectOneToOneCellEditor": SelectOneToOneCellEditor,
                    "SelectManyToOneCellEditor": SelectManyToOneCellEditor,
                    "SelectEnumerationCellEditor": SelectEnumerationCellEditor,
                    "ManyPrimitiveEditor": ManyPrimitiveEditor,
                    "ManyStringPrimitiveEditor": ManyStringPrimitiveEditor,
                    "ManyEnumerationEditor": ManyEnumerationEditor,
                    "ManyIntegerPrimitiveEditor": ManyIntegerPrimitiveEditor,
                    "ManyBooleanPrimitiveEditor": ManyBooleanPrimitiveEditor,
                    "Checkbox": CheckboxEditor,
                    "Date": DateEditor,
                    "DateTime": DateTimeEditor,
                    "Time": TimeEditor
                }
            }}
    });

    function TextEditor(args) {
        //Public api
        $.extend(this, {
            "TumlTextEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $input;
        var defaultValue;
        var scope = this;

        this.init = function () {
            $input = $("<INPUT type=text class='editor-text' />")
            .appendTo(args.container)
            .bind("keydown.nav", function (e) {
                if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
                    e.stopImmediatePropagation();
                }
            })
            .focus()
            .select();
        };

        this.destroy = function () {
            $input.remove();
        };

        this.focus = function () {
            $input.focus();
        };

        this.getValue = function () {
            return $input.val();
        };

        this.setValue = function (val) {
            $input.val(val);
        };

        this.loadValue = function (item) {
            defaultValue = item[args.column.field] || null;
            $input.val(defaultValue);
            $input[0].defaultValue = defaultValue;
            $input.select();
        };

        this.serializeValue = function () {
            return serializeValueWithValue($input);
        };

        function serializeValueWithValue(input) {
            var value = input.val();
            if (value == '') {
                return null;
            } else {
                return value;
            }
        };

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return ($input.val() != defaultValue);
        };

        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function ManyEnumerationEditor(args) {
        var serializer = function serializeValueWithValue(table) {
            var rowArray = table.find('.many-primitive-editor-row');
            var arrayToSerialize = [];
            for (var i = 0; i < rowArray.length; i++) {
                var row = rowArray[i];
                arrayToSerialize.push($(row).data('value'));
            }
            return arrayToSerialize;
        }
        //return new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
        ManyEnumeration.prototype = new  Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
    }

    function ManyStringPrimitiveEditor(args) {
        var serializer = function serializeValueWithValue(table) {
            var rowArray = table.find('.many-primitive-editor-row');
            var arrayToSerialize = [];
            for (var i = 0; i < rowArray.length; i++) {
                var row = rowArray[i];
                arrayToSerialize.push($(row).data('value'));
            }
            return arrayToSerialize;
        }
        //return new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
        ManyStringPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
    }

    function ManyIntegerPrimitiveEditor(args) {
        var serializer = function serializeValueWithValue(table) {
            var rowArray = table.find('.many-primitive-editor-row');
            var arrayToSerialize = [];
            for (var i = 0; i < rowArray.length; i++) {
                var row = rowArray[i];
                arrayToSerialize.push(parseInt($(row).data('value'), 10));
            }
            return arrayToSerialize;
        }
        //return new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
        ManyIntegerPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
    }

    function ManyBooleanPrimitiveEditor(args) {
        var serializer = function (table) {
            var rowArray = table.find('.many-primitive-editor-row');
            var arrayToSerialize = [];
            for (var i = 0; i < rowArray.length; i++) {
                var row = rowArray[i];
                arrayToSerialize.push($(row).data('value'));
            }
            return arrayToSerialize;
        }
        //return new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
        ManyBooleamPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor(args, serializer);
    }

    function ManyPrimitiveEditor(args, serializer) {
        var $input;
        var $div;
        var $addButton;
        var $table;
        var $select;
        var $cancel;
        var defaultValue;
        var scope = this;

        //Public api
        $.extend(this, {
            "TumlManyPrimitiveEditor": "1.0.0",
            "serializeValue": serializeValue, 
            "serializeValueWithValue": serializeValueWithValue,
            "handleKeyPress": handleKeyPress 
        });

        this.init = function () {
            args.grid['manyPrimitiveEditorOpen'] = true;
            args.grid['manyPrimitiveEditor'] = this;

            $div = $("<div class='many-primitive-editor' />");
            $addButton = $('<button id="many-primitive-editor-input-add-button" />').text('Add').click(function() {
                var valueToAdd = $('.many-primitive-editor-input').val();
                var currentValues = serializeValue();
                var testArray = [];
                testArray.push(valueToAdd);
                var validationResults = args.column.validator(testArray);
                if (currentValues.length !== 0 && validationResults.valid && args.column.options.unique) {
                    validationResults = args.column.validator(currentValues, valueToAdd);
                }
                if (!validationResults.valid) {
                    alert(validationResults.msg);
                } else {
                    addTr(valueToAdd);
                    $input.val('');
                }
            }).appendTo($div);
            $input = $('<input type=text class="many-primitive-editor-input">');
            $input.appendTo($div)
            var resultDiv = $('<div class="many-primitive-editor-result" />').appendTo($div);
            $table = $('<table class="many-primitive-editor-result-table" />').appendTo(resultDiv);

            $div.bind("keydown.nav", function (e) {
                if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
                    e.stopImmediatePropagation();
                }
            });

            $div.appendTo(args.container);
            $input.focus().select();
        };

        function handleKeyPress(e) {
            if (e.target == $input[0]) {
                if (e.which == 13) {
                    $addButton.click();
                    e.stopImmediatePropagation();
                }
            }
        }

        this.destroy = function () {
            args.grid['manyEditorOpen'] = false;
            $div.remove();
        };

        this.focus = function () {
            $input.focus();
        };

        this.loadValue = function (item) {
            defaultValue = item[args.column.field];
            if (defaultValue !== undefined && defaultValue !== null) {
                for (var i = 0; i < defaultValue.length; i++) {
                    addTr(defaultValue[i]);
                }
                if (args.column.options.ordered) {
                    $table.tableDnD();
                }
            }
        };

        function addTr(value) {
            var row = $('<tr />').addClass('many-primitive-editor-row');
            var rowValue = $('<td class="many-primitive-editor-cell" />').text(value);
            row.append(rowValue);
            row.data('value', value);
            var img = $('<img class="tuml-many-select-img" src="/restAndJson/javascript/images/delete.png">').click(function() {
               var liClicked =  $(this).parent().parent();
               liClicked.remove();
            });
            var imgValue = $('<td class="many-primitive-editor-cell many-primitive-editor-cell-img" />');
            imgValue.append(img);
            row.append(imgValue);
            $table.append(row);
        }

        function serializeValue() {
            return serializeValueWithValue($table);
        };

        function serializeValueWithValue(table) {
            return serializer(table);
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            var rowArray = $table.find('.many-primitive-editor-row');
            var arrayToSerialize = [];
            for (var i = 0; i < rowArray.length; i++) {
                var row = rowArray[i];
                arrayToSerialize.push($(row).data('value'));
            }
            var result = false;
            if (defaultValue !== undefined && defaultValue !== null && defaultValue.length == arrayToSerialize.length) {
                for (var i = 0; i < arrayToSerialize.length; i++) {
                    if (defaultValue[i] != arrayToSerialize[i]) {
                        result = true;
                        break;
                    }
                }
            } else {
                result = true;
            }
            return result;
        };

        this.validate = function () {
            if (args.column.validator) {
                var rowArray = $table.find('.many-primitive-editor-row');
                if (rowArray.length > 0) {
                    var arrayToSerialize = [];
                    for (var i = 0; i < rowArray.length; i++) {
                        var row = rowArray[i];
                        arrayToSerialize.push($(row).data('value'));
                    }
                    var validationResults = args.column.validator(arrayToSerialize);
                    if (!validationResults.valid) {
                        return validationResults;
                    }
                }
            }

            return {
                valid: true,
                msg: null
            };
        };
        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function IntegerEditor(args) {
        //Public api
        $.extend(this, {
            "TumlIntegerEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
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

        this.serializeValue = function() {
            return serializeValueWithValue($input);
        };

        function serializeValueWithValue(input) {
            return parseInt(input.val(), 10) || 0;
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };

        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function DateEditor(args) {
        //Public api
        $.extend(this, {
            "TumlDateEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue
        });
        var $input;
        var defaultValue;
        var scope = this;
        var calendarOpen = false;

        this.init = function () {
            $input = $("<INPUT type=text />");
            $input.appendTo(args.container);
            $input.focus().select();
            $input.datepicker({
                showOn: "button",
                buttonImageOnly: true,
                buttonImage: "../javascript/slickgrid/images/calendar.gif",
                dateFormat: "yy-mm-dd",
                beforeShow: function () {
                    calendarOpen = true
                },
                onClose: function () {
                    calendarOpen = false
                }
            });
            $input.width($input.width() - 18);
            //$input.mask("9999-99-99");
        };

        this.destroy = function () {
            $.datepicker.dpDiv.stop(true, true);
            $input.datepicker("hide");
            $input.datepicker("destroy");
            $input.remove();
        };

        this.show = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).show();
            }
        };

        this.hide = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).hide();
            }
        };

        this.position = function (position) {
            if (!calendarOpen) {
                return;
            }
            $.datepicker.dpDiv
            .css("top", position.top + 30)
            .css("left", position.left);
        };

        this.focus = function () {
            if (!$input.is(":hidden")) {
                $input.focus();
            }
        };

        this.loadValue = function (item) {
            defaultValue = item[args.column.field];
            $input.val(defaultValue);
            $input[0].defaultValue = defaultValue;
            $input.select();
        };

        this.serializeValue = function () {
            return serializeValueWithValue($input);
        };

        function serializeValueWithValue(input) {
            return input.val();
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function DateTimeEditor(args) {
        //Public api
        $.extend(this, {
            "TumlDateTimeEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $input;
        var defaultValue;
        var scope = this;
        var calendarOpen = false;

        this.init = function () {
            $input = $("<INPUT type=text />");
            $input.appendTo(args.container);
            $input.focus().select();
            $input.datetimepicker({
                showOn: "button",
                buttonImageOnly: true,
                buttonImage: "../javascript/slickgrid/images/calendar.gif",
                dateFormat: "yy-mm-dd",
                timeFormat: "hh:mm:ss",
                beforeShow: function () {
                    calendarOpen = true
                },
                onClose: function () {
                    calendarOpen = false
                }
            });
            $input.width($input.width() - 18);
            //$input.mask("9999-99-99T99:99:99");
        };

        this.destroy = function () {
            $.datepicker.dpDiv.stop(true, true);
            $input.datepicker("hide");
            $input.datepicker("destroy");
            $input.remove();
        };

        this.show = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).show();
            }
        };

        this.hide = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).hide();
            }
        };

        this.position = function (position) {
            if (!calendarOpen) {
                return;
            }
            $.datepicker.dpDiv
            .css("top", position.top + 30)
            .css("left", position.left);
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
            //$input.unmask();
            return serializeValueWithValue($input);
        };

        function serializeValueWithValue(input) {
            return input.val();
        }

        this.applyValue = function (item, state) {
            //This is to remove the space between date and time fiels in order for joda time not to moan
            item[args.column.field] = state.replace(/ /g,"T");
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }


    function TimeEditor(args) {
        //Public api
        $.extend(this, {
            "TumlTimeEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $input;
        var defaultValue;
        var scope = this;
        var calendarOpen = false;

        this.init = function () {
            $input = $("<INPUT type=text />");
            $input.appendTo(args.container);
            $input.focus().select();
            $input.timepicker({
                showOn: "button",
                buttonImageOnly: true,
                buttonImage: "../javascript/slickgrid/images/calendar.gif",
                beforeShow: function () {
                    calendarOpen = true
                },
                onClose: function () {
                    calendarOpen = false
                }
            });
            $input.width($input.width() - 18);
            //$input.mask("99:99");
        };

        this.destroy = function () {
            $.datepicker.dpDiv.stop(true, true);
            $input.datepicker("hide");
            $input.datepicker("destroy");
            $input.remove();
        };

        this.show = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).show();
            }
        };

        this.hide = function () {
            if (calendarOpen) {
                $.datepicker.dpDiv.stop(true, true).hide();
            }
        };

        this.position = function (position) {
            if (!calendarOpen) {
                return;
            }
            $.datepicker.dpDiv
            .css("top", position.top + 30)
            .css("left", position.left);
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
            return serializeValueWithValue($input);
        };

        function serializeValueWithValue(input) {
            return input.val();
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };

        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function CheckboxEditor(args) {
        //Public api
        $.extend(this, {
            "TumlCheckBoxEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
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
            return serializeValueWithValue($select);
        };

        function serializeValueWithValue(select) {
            return (select.attr("checked") == "checked");
        }

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

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function SelectEnumerationCellEditor(args) {
        //Public api
        $.extend(this, {
            "TumlSelectEnumerationCellEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function(item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowEnumerationLookupMap.getOrLoadMap(function(data){
                if (!args.column.options.required) {
                    $select.append($('<option />)').val("").html(""));
                }
                $.each(data, function(index, obj) {
                    $select.append($('<option />)').val(obj).html(obj));
                });
                currentValue = item[args.column.field];
                $select.val(currentValue);
                if (!args.column.options.required) {
                    $select.chosen({allow_single_deselect: true});
                } else {
                    $select.chosen();
                }
                $select.focus();
            });

        };

        this.destroy = function() {
            $select.remove();
        };

        this.focus = function() {
            $select.focus();
        };

        this.loadValue = function(item) {
        };

        this.serializeValue = function () {
            if(args.column.options){
                return serializeValueWithValue($select);
            }else{
                alert('why is this happening?');
                //return serializeValueWithValue($select.val() == "yes"));
            }
        };

        function serializeValueWithValue(select) {
            return select.val();
        }

        this.applyValue = function(item,state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function() {
            if (currentValue === null || currentValue === undefined) {
                return true;
            } else {
                return ($select.val() != currentValue);
            }
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($select.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };


        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init(args.item);
        }
    }

    function SelectOneToOneCellEditor(args) {
        //Public api
        $.extend(this, {
            "TumlSelectOneToOneEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function(item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
                    if (!args.column.options.required) {
                        $select.append($('<option />)').val("").html(""));
                    }
                    $.each(compositeParentMap[compositeParentVertexId], function(index, obj) {
                        $select.append($('<option />)').val(obj.id).html(obj.displayName));
                    });
                    //currentValue is the vertex id of the oneToOne or manyToOne
                    currentValue = item[args.column.field];
                    //append the current value to the dropdown
                    if (currentValue !== undefined && currentValue !== null) {
                        $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                    }
                    if (!args.column.options.required) {
                        $select.chosen({allow_single_deselect: true});
                    } else {
                        $select.chosen();
                    }
                    $select.focus();
                });
            });

        };

        this.destroy = function() {
            $select.remove();
        };

        this.focus = function() {
            $select.focus();
        };

        this.loadValue = function(item) {
        };

        this.serializeValue = function () {
            if(args.column.options){
                return serializeValueWithValue($select);
            } else {
                alert('why is this happening? 2');
                //return ($select.val() == "yes");
            }
        };

        function serializeValueWithValue(select) {
            var options = select.children();
            for (var i = 0; i < options.length; i++) {
                if (options[i].selected) {
                    return {id: parseInt(select.val()), displayName: options[i].label};
                    break;
                }
            }
        }

        this.applyValue = function(item,state) {
            var previousValue = item[args.column.field];
            item[args.column.field] = state;
            //remove state from the map and add the previousValue
            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
                    removeByValue(compositeParentMap[compositeParentVertexId], state);
                    if (previousValue !== undefined && previousValue !== null) {
                        compositeParentMap[compositeParentVertexId].push(previousValue);
                    }
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
            if (currentValue === null || currentValue === undefined) {
                return true;
            } else {
                return ($select.val() != currentValue.id);
            }
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($select.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init(args.item);
        }
    }

    function SelectManyToOneCellEditor(args) {
        //Public api
        $.extend(this, {
            "TumlSelectManyToOneCellEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function(item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
                    if (!args.column.options.required) {
                        $select.append($('<option />)').val("").html(""));
                    }
                    $.each(compositeParentMap[compositeParentVertexId], function(index, obj) {
                        $select.append($('<option />)').val(obj.id).html(obj.displayName));
                    });
                    //currentValue is the vertex id of the oneToOne or manyToOne
                    currentValue = item[args.column.field];
                    //append the current value to the dropdown
                    if (currentValue !== undefined && currentValue !== null) {
                        $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                    }
                    if (!args.column.options.required) {
                        $select.chosen({allow_single_deselect: true});
                    } else {
                        $select.chosen();
                    }
                    $select.focus();
                });
            });

        };

        this.destroy = function() {
            $select.remove();
        };

        this.focus = function() {
            $select.focus();
        };

        this.loadValue = function(item) {
        };

        this.serializeValue = function () {
            if(args.column.options){
                return serializeValueWithValue($select);
            }else{
                alert('why is this happening 3?');
                //return ($select.val() == "yes");
            }
        };

        function serializeValueWithValue(select) {
        }

        this.applyValue = function(item,state) {
            var previousValue = item[args.column.field];
            item[args.column.field] = state;
            //remove state from the map and add the previousValue
            //args.column.options.rowLookupMap.getOrLoadMap(item['id'], function(compositeParentVertexId){
            //args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function(compositeParentMap) {
            //removeByValue(compositeParentMap[compositeParentVertexId], state);
            //if (previousValue !== undefined && previousValue !== null) {
            //compositeParentMap[compositeParentVertexId].push(previousValue);
            //}
            //});
            //});
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
            if (currentValue === null || currentValue === undefined) {
                return true;
            } else {
                return ($select.val() != currentValue.id);
            }
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($select.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init(args.item);
        }
    }


    function IntegerEditor(args) {
        //Public api
        $.extend(this, {
            "TumlIntegerEditor": "1.0.0",
            "serializeValueWithValue": serializeValueWithValue 
        });
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
            return parseInt($input);
        };
        
        function serializeValueWithValue(input) {
            return parseInt(input.val(), 10) || 0;
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
        };


        this.validate = function () {
            if (args.column.validator) {
                var validationResults = args.column.validator($input.val());
                if (!validationResults.valid) {
                    return validationResults;
                }
            }

            return {
                valid: true,
                msg: null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }
})(jQuery);
