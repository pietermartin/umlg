/***
 * Contains basic SlickGrid editors.
 * @module Editors
 * @namespace Slick
 */

(function ($) {
    // register namespace
    $.extend(true, window, {
        "Tuml":{
            "Slick":{
                "Editors":{
                    "Integer":IntegerEditor,
                    "Text":TextEditor,
                    "OneEditor":OneEditor,
                    "ComponentManyEditor":ComponentManyEditor,
                    "SelectOneToOneCellEditor":SelectOneToOneCellEditor,
                    "SelectManyToOneCellEditor":SelectManyToOneCellEditor,
                    "SelectEnumerationCellEditor":SelectEnumerationCellEditor,
                    "ManyPrimitiveEditor":ManyPrimitiveEditor,
                    "ManyStringPrimitiveEditor":ManyStringPrimitiveEditor,
                    "ManyEnumerationEditor":ManyEnumerationEditor,
                    "ManyIntegerPrimitiveEditor":ManyIntegerPrimitiveEditor,
                    "ManyBooleanPrimitiveEditor":ManyBooleanPrimitiveEditor,
                    "Checkbox":CheckboxEditor,
                    "Date":DateEditor,
                    "DateTime":DateTimeEditor,
                    "Time":TimeEditor
                }
            }}
    });

    function ComponentManyEditor(args) {
        //Public api
        $.extend(this, {
            "ComponentManyEditor":"1.0.0"
        });
        var scope = this;
        var tumlTabGridManager = null;
        var tabDiv;
        var property = null;
        var tabDivName = null;
        var onSave = false;
        var onLoad = false;

        this.init = function () {
            property = args.column.options.property;
            tumlTabGridManager = new Tuml.TumlTabGridManager(property.tumlUri);
            tumlTabGridManager.doSave = function () {
                //TODO work out moving next or previous
                onSave = true;
                args.grid.gotoCell(args.grid.getActiveCell().row, args.grid.getActiveCell().cell + 1);
            };
            tumlTabGridManager.doCancel = function () {
                args.grid.getEditorLock().cancelCurrentEdit();
                //args.grid.gotoCell(args.grid.getActiveCell().row, args.grid.getActiveCell().cell + 1);
            }
        };

        this.destroy = function () {
            $('#tab-container').tabs('close', tabDivName);
            tabDiv.remove();
            $('#tab-container').tabs('enableTab', 0);
        };

        this.focus = function () {
            $input.focus();
        };

        this.loadValue = function (item) {
            if (!onSave) {
                this.getMetaData(item[args.column.field]);
            }
            onSave = false;
            onLoad = true;
        };

        this.serializeValue = function () {
            if (onSave) {
                var json = tumlTabGridManager.fieldsToObject();
                return json;
            } else {
                if (!onLoad) {
                    this.getMetaData(null);
                }
                return null;
            }
            onLoad = false;
        };

        this.getMetaData = function (data) {
            $.ajax({
                url:'/restAndJson/' + property.name + 'sMeta',
                type:"GET",
                dataType:"json",
                contentType:"json",
                success:function (result, textStatus, jqXHR) {
                    //Create the tab
                    var tabContainer = $('#tab-container');
                    tabDivName = result[0].meta.to.name;
                    tabDiv = $('<div />', {id:tabDivName, title:tabDivName}).appendTo(tabContainer);
                    tumlTabGridManager.refresh(data, result[0].meta.to, property.qualifiedName);
                    $('#tab-container').tabs('add', {title:tabDivName, content:'<div id="' + tabDivName + '" />', closable:true});
                    $('#tab-container').tabs('disableTab', 0);
                    $('#tab-containter').tabs({
                        onClose:function (title) {
                            tabDiv.remove();
                            $('#tab-container').tabs('enableTab', 0);
                        }
                    });
                },
                error:function (jqXHR, textStatus, errorThrown) {
                    alert('error getting ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                }
            });
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return true;
        };

        this.validate = function () {
            var validationResults = tumlTabGridManager.validateFields();
            if (validationResults.length === 0) {
                return {
                    valid:true,
                    msg:null
                };
            } else {
                return {
                    valid:false,
                    msg:null
                };
            }
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function OneEditor(args) {
        //Public api
        $.extend(this, {
            "TumlOneEditor":"1.0.0"
        });
        var scope = this;
        var tumlTabOneManager = null;
        var tabDiv;
        var property = null;
        var tabDivName = null;
        var onSave = false;
        var onLoad = false;

        this.init = function () {
            property = args.column.options.property;
            tumlTabOneManager = new Tuml.TumlTabOneManager(property.tumlUri);
            tumlTabOneManager.doSave = function () {
                //TODO work out moving next or previous
                onSave = true;
                args.grid.gotoCell(args.grid.getActiveCell().row, args.grid.getActiveCell().cell + 1);
            };
            tumlTabOneManager.doCancel = function () {
                args.grid.getEditorLock().cancelCurrentEdit();
                //args.grid.gotoCell(args.grid.getActiveCell().row, args.grid.getActiveCell().cell + 1);
            }
        };

        this.destroy = function () {
            $('#tab-container').tabs('close', tabDivName);
            tabDiv.remove();
            $('#tab-container').tabs('enableTab', 0);
        };

        this.focus = function () {
            $input.focus();
        };

        this.loadValue = function (item) {
            if (!onSave) {
                this.getMetaData(item[args.column.field]);
            }
            onSave = false;
            onLoad = true;
        };

        this.serializeValue = function () {
            if (onSave) {
                var json = tumlTabOneManager.fieldsToObject();
                return json;
            } else {
                if (!onLoad) {
                    this.getMetaData(null);
                }
                return null;
            }
            onLoad = false;
        };

        this.getMetaData = function (data) {
            $.ajax({
                url:'/restAndJson/' + property.name + 'sMeta',
                type:"GET",
                dataType:"json",
                contentType:"json",
                success:function (result, textStatus, jqXHR) {
                    //Create the tab
                    var tabContainer = $('#tab-container');
                    tabDivName = result[0].meta.to.name;
                    tabDiv = $('<div />', {id:tabDivName, title:tabDivName}).appendTo(tabContainer);
                    tumlTabOneManager.refresh(data, result[0].meta.to, property.qualifiedName);
                    $('#tab-container').tabs('add', {title:tabDivName, content:'<div id="' + tabDivName + '" />', closable:true});
                    $('#tab-container').tabs('disableTab', 0);
                    $('#tab-containter').tabs({
                        onClose:function (title) {
                            tabDiv.remove();
                            $('#tab-container').tabs('enableTab', 0);
                        }
                    });
                },
                error:function (jqXHR, textStatus, errorThrown) {
                    alert('error getting ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                }
            });
        }

//        function serializeValueWithValue(input) {
//            var value = input.val();
//            if (value == '') {
//                return null;
//            } else {
//                return value;
//            }
//        };

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
            return true;
        };

        this.validate = function () {
            var validationResults = tumlTabOneManager.validateFields();
            if (validationResults.length === 0) {
                return {
                    valid:true,
                    msg:null
                };
            } else {
                return {
                    valid:false,
                    msg:null
                };
            }
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function TextEditor(args) {
        //Public api
        $.extend(this, {
            "TumlTextEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                valid:true,
                msg:null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }

    function ManyEnumerationEditor(args) {
        this.args = args;
        this.$item = args.item;
        this.init();
    }

    ManyEnumerationEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor();
    ManyEnumerationEditor.prototype.serializeValue = function () {
        var rowArray = this.$table.find('.many-primitive-editor-row');
        var arrayToSerialize = [];
        for (var i = 0; i < rowArray.length; i++) {
            var row = rowArray[i];
            arrayToSerialize.push($(row).data('value'));
        }
        return arrayToSerialize;
    }
    ManyEnumerationEditor.prototype.createInput = function () {
        $select = $("<SELECT tabIndex='0' class='editor-select many-primitive-editor-input' style='width:115px;'></SELECT>");
        $select.appendTo(this.args.container);
        $self = this;
        this.args.column.options.rowEnumerationLookupMap.getOrLoadMap(function (data) {
            if (!$self.args.column.options.required) {
                $select.append($('<option />)').val("").html(""));
            }
            $.each(data, function (index, obj) {
                $select.append($('<option />)').val(obj).html(obj));
            });
            currentValue = $self.$item[$self.args.column.field];
            $select.val(currentValue);
            if (!$self.args.column.options.required) {
                $select.chosen({allow_single_deselect:true});
            } else {
                $select.chosen();
            }
            $select.focus();
        });
        return $select;
    }
    ManyEnumerationEditor.prototype.resetInput = function (input) {
        input.val('').trigger('liszt:updated');
    }

    function ManyStringPrimitiveEditor(args) {
        this.args = args;
        this.init();
    }

    ManyStringPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor();
    ManyStringPrimitiveEditor.prototype.serializeValue = function () {
        var rowArray = this.$table.find('.many-primitive-editor-row');
        var arrayToSerialize = [];
        for (var i = 0; i < rowArray.length; i++) {
            var row = rowArray[i];
            arrayToSerialize.push($(row).data('value'));
        }
        return arrayToSerialize;
    }

    function ManyIntegerPrimitiveEditor(args) {
        this.args = args;
        this.init();
    }

    ManyIntegerPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor();
    ManyIntegerPrimitiveEditor.prototype.serializeValue = function () {
        var rowArray = this.$table.find('.many-primitive-editor-row');
        var arrayToSerialize = [];
        for (var i = 0; i < rowArray.length; i++) {
            var row = rowArray[i];
            arrayToSerialize.push(parseInt($(row).data('value'), 10));
        }
        return arrayToSerialize;
    }

    function ManyBooleanPrimitiveEditor(args) {
        this.args = args;
        this.init();
    }

    ManyBooleanPrimitiveEditor.prototype = new Tuml.Slick.Editors.ManyPrimitiveEditor();
    ManyBooleanPrimitiveEditor.prototype.serializeValue = function () {
        var rowArray = this.$table.find('.many-primitive-editor-row');
        var arrayToSerialize = [];
        for (var i = 0; i < rowArray.length; i++) {
            var row = rowArray[i];
            arrayToSerialize.push($(row).data('value'));
        }
        return arrayToSerialize;
    }

    function ManyPrimitiveEditor(args) {
        this.args = args;
        this.$input;
        var $div;
        var $addButton;
        this.$table;
        var $select;
        var $cancel;
        var defaultValue;
        var scope = this;
        this.justOpened = true;

        //Public api
        $.extend(this, {
            "TumlManyPrimitiveEditor":"1.0.0"
        });

        this.handleKeyPress = function (e) {
            if (e.target == this.$input[0]) {
                if (e.which == 13) {
                    this.$addButton.click();
                    e.stopImmediatePropagation();
                }
            }
        }

        this.destroy = function () {
            this.args.grid['manyPrimitiveEditorOpen'] = false;
            this.$div.remove();
        };

        this.focus = function () {
            this.$input.focus();
        };

        this.loadValue = function (item) {
            defaultValue = item[this.args.column.field];
            if (defaultValue !== undefined && defaultValue !== null) {
                for (var i = 0; i < defaultValue.length; i++) {
                    this.addTr(defaultValue[i]);
                }
                if (this.args.column.options.ordered) {
                    this.$table.tableDnD();
                }
            }
        };

        this.addTr = function (value) {
            var row = $('<tr />').addClass('many-primitive-editor-row');
            var rowValue = $('<td class="many-primitive-editor-cell" />').text(value);
            row.append(rowValue);
            row.data('value', value);
            var img = $('<img class="tuml-many-select-img" src="/restAndJson/javascript/images/delete.png">').click(function () {
                var liClicked = $(this).parent().parent();
                liClicked.remove();
            });
            var imgValue = $('<td class="many-primitive-editor-cell many-primitive-editor-cell-img" />');
            imgValue.append(img);
            row.append(imgValue);
            this.$table.append(row);
        }

        this.applyValue = function (item, state) {
            item[this.args.column.field] = state;
        };

        this.isValueChanged = function () {
            var rowArray = this.$table.find('.many-primitive-editor-row');
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
            if (this.args.column.validator) {
                var rowArray = this.$table.find('.many-primitive-editor-row');
                if (rowArray.length > 0) {
                    var arrayToSerialize = [];
                    for (var i = 0; i < rowArray.length; i++) {
                        var row = rowArray[i];
                        arrayToSerialize.push($(row).data('value'));
                    }
                    var validationResults = this.args.column.validator(arrayToSerialize);
                    if (!validationResults.valid) {
                        return validationResults;
                    }
                }
            }

            return {
                valid:true,
                msg:null
            };
        };
        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (this.args !== undefined) {
            this.init();
        }
    }

    ManyPrimitiveEditor.prototype.serializeValue = function () {
        alert('this must be overriden');
    };
    ManyPrimitiveEditor.prototype.createInput = function () {
        return $('<input type=text class="many-primitive-editor-input">');
    }
    ManyPrimitiveEditor.prototype.resetInput = function (input) {
        input.val('');
    }
    ManyPrimitiveEditor.prototype.init = function () {
        this.args.grid['manyPrimitiveEditorOpen'] = true;
        this.args.grid['manyPrimitiveEditor'] = this;
        this.$div = $("<div class='many-primitive-editor' />");
        var $self = this;
        this.$addButton = $('<button id="many-primitive-editor-input-add-button" />').text('Add').click(function () {
            var valueToAdd = $('.many-primitive-editor-input').val();
            var currentValues = $self.serializeValue();
            var testArray = [];
            testArray.push(valueToAdd);
            var validationResults = $self.args.column.validator(testArray);
            if (currentValues.length !== 0 && validationResults.valid && $self.args.column.options.unique) {
                validationResults = $self.args.column.validator(currentValues, valueToAdd);
            }
            if (!validationResults.valid) {
                alert(validationResults.msg);
            } else {
                $self.addTr(valueToAdd);
                //Need to reapply the drag and drop plugin if the table was empty
                if (currentValues.length == 0) {
                    if ($self.args.column.options.ordered) {
                        $self.$table.tableDnD();
                    }
                }
                $self.resetInput($self.$input);
            }
        }).appendTo(this.$div);
        this.$input = this.createInput();
        this.$input.appendTo(this.$div)
        var resultDiv = $('<div class="many-primitive-editor-result" />').appendTo(this.$div);
        this.$table = $('<table class="many-primitive-editor-result-table" />').appendTo(resultDiv);

        this.$div.bind("keydown.nav", function (e) {
            if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
                e.stopImmediatePropagation();
            }
        });

        this.$div.appendTo(this.args.container);
        this.$input.focus().select();
    };

    function IntegerEditor(args) {
        //Public api
        $.extend(this, {
            "TumlIntegerEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                valid:true,
                msg:null
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
            "TumlDateEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                showOn:"button",
                buttonImageOnly:true,
                buttonImage:"../javascript/slickgrid/images/calendar.gif",
                dateFormat:"yy-mm-dd",
                beforeShow:function () {
                    calendarOpen = true
                },
                onClose:function () {
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
                valid:true,
                msg:null
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
            "TumlDateTimeEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                showOn:"button",
                buttonImageOnly:true,
                buttonImage:"../javascript/slickgrid/images/calendar.gif",
                dateFormat:"yy-mm-dd",
                timeFormat:"hh:mm:ss",
                beforeShow:function () {
                    calendarOpen = true
                },
                onClose:function () {
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
            item[args.column.field] = state.replace(/ /g, "T");
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
                valid:true,
                msg:null
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
            "TumlTimeEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                showOn:"button",
                buttonImageOnly:true,
                buttonImage:"../javascript/slickgrid/images/calendar.gif",
                beforeShow:function () {
                    calendarOpen = true
                },
                onClose:function () {
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
                valid:true,
                msg:null
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
            "TumlCheckBoxEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
            if (($select.attr("checked") == "checked") && defaultValue != true) {
                return true;
            } else if (($select.attr("checked") !== "checked") && defaultValue != false) {
                return true;
            } else {
                return false;
            }
        };

        this.validate = function () {
            return {
                valid:true,
                msg:null
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
            "TumlSelectEnumerationCellEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function (item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowEnumerationLookupMap.getOrLoadMap(function (data) {
                if (!args.column.options.required) {
                    $select.append($('<option />)').val("").html(""));
                }
                $.each(data, function (index, obj) {
                    $select.append($('<option />)').val(obj).html(obj));
                });
                currentValue = item[args.column.field];
                $select.val(currentValue);
                if (!args.column.options.required) {
                    $select.chosen({allow_single_deselect:true});
                } else {
                    $select.chosen();
                }
                $select.focus();
            });

        };

        this.destroy = function () {
            $select.remove();
        };

        this.focus = function () {
            $select.focus();
        };

        this.loadValue = function (item) {
        };

        this.serializeValue = function () {
            if (args.column.options) {
                return serializeValueWithValue($select);
            } else {
                alert('why is this happening?');
                //return serializeValueWithValue($select.val() == "yes"));
            }
        };

        function serializeValueWithValue(select) {
            return select.val();
        }

        this.applyValue = function (item, state) {
            item[args.column.field] = state;
        };

        this.isValueChanged = function () {
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
                valid:true,
                msg:null
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
            "TumlSelectOneToOneEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function (item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function (compositeParentVertexId) {
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function (compositeParentMap) {
                    if (!args.column.options.required) {
                        $select.append($('<option />)').val("").html(""));
                    }
                    $.each(compositeParentMap[compositeParentVertexId], function (index, obj) {
                        $select.append($('<option />)').val(obj.id).html(obj.displayName));
                    });
                    //currentValue is the vertex id of the oneToOne or manyToOne
                    currentValue = item[args.column.field];
                    //append the current value to the dropdown
                    if (currentValue !== undefined && currentValue !== null) {
                        $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                    }
                    if (!args.column.options.required) {
                        $select.chosen({allow_single_deselect:true});
                    } else {
                        $select.chosen();
                    }
                    $select.focus();
                });
            });

        };

        this.destroy = function () {
            $select.remove();
        };

        this.focus = function () {
            $select.focus();
        };

        this.loadValue = function (item) {
        };

        this.serializeValue = function () {
            if (args.column.options) {
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
                    return {id:parseInt(select.val()), displayName:options[i].label};
                    break;
                }
            }
        }

        this.applyValue = function (item, state) {
            var previousValue = item[args.column.field];
            item[args.column.field] = state;
            //remove state from the map and add the previousValue
            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function (compositeParentVertexId) {
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function (compositeParentMap) {
                    removeByValue(compositeParentMap[compositeParentVertexId], state);
                    if (previousValue !== undefined && previousValue !== null) {
                        compositeParentMap[compositeParentVertexId].push(previousValue);
                    }
                });
            });
        };

        function removeByValue(compositeLookup, toRemove) {
            for (var i = 0; i < compositeLookup.length; i++) {
                if (compositeLookup[i].id == toRemove.id) {
                    compositeLookup.splice(i, 1);
                    break;
                }
            }
        }

        this.isValueChanged = function () {
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
                valid:true,
                msg:null
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
            "TumlSelectManyToOneCellEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
        });
        var $select;
        var currentValue;
        var scope = this;
        var options;

        this.init = function (item) {
            $select = $("<SELECT tabIndex='0' class='editor-select' style='width:115px;'></SELECT>");
            $select.appendTo(args.container);

            args.column.options.rowLookupMap.getOrLoadMap(item['id'], function (compositeParentVertexId) {
                args.column.options.compositeParentLookupMap.getOrLoadMap(compositeParentVertexId, item['id'], function (compositeParentMap) {
                    if (!args.column.options.required) {
                        $select.append($('<option />)').val("").html(""));
                    }
                    $.each(compositeParentMap[compositeParentVertexId], function (index, obj) {
                        $select.append($('<option />)').val(obj.id).html(obj.displayName));
                    });
                    //currentValue is the vertex id of the oneToOne or manyToOne
                    currentValue = item[args.column.field];
                    //append the current value to the dropdown
                    if (currentValue !== undefined && currentValue !== null) {
                        $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                    }
                    if (!args.column.options.required) {
                        $select.chosen({allow_single_deselect:true});
                    } else {
                        $select.chosen();
                    }
                    $select.focus();
                });
            });

        };

        this.destroy = function () {
            $select.remove();
        };

        this.focus = function () {
            $select.focus();
        };

        this.loadValue = function (item) {
        };

        this.serializeValue = function () {
            if (args.column.options) {
                return serializeValueWithValue($select);
            } else {
                alert('why is this happening 3?');
                //return ($select.val() == "yes");
            }
        };

        function serializeValueWithValue(select) {
        }

        this.applyValue = function (item, state) {
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
            for (var i = 0; i < compositeLookup.length; i++) {
                if (compositeLookup[i].id == toRemove.id) {
                    compositeLookup.splice(i, 1);
                    break;
                }
            }
        }

        this.isValueChanged = function () {
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
                valid:true,
                msg:null
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
            "TumlIntegerEditor":"1.0.0",
            "serializeValueWithValue":serializeValueWithValue
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
                valid:true,
                msg:null
            };
        };

        //This is called from the grid, the one only uses the serializeValueWithValue function
        if (args !== undefined) {
            this.init();
        }
    }
})(jQuery);
