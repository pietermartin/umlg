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
                    "SelectCellEditor": SelectCellEditor,
                    "Checkbox": CheckboxEditor,
                    "Date": DateEditor,
                    "DateTime": DateTimeEditor,
                    "Time": TimeEditor
                }
            }}
    });

  function TextEditor(args) {
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
      return $input.val();
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
    this.init();
  }
    function DateEditor(args) {
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
            $input.mask("9999-99-99");
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
            return $input.val();
        };

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

        this.init();
    }

    function DateTimeEditor(args) {
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
            $input.mask("9999-99-99T99:99:99");
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
            return $input.val();
        };

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
        this.init();
    }


    function TimeEditor(args) {
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
            $input.mask("99:99");
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
            return $input.val();
        };

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

        this.init();
    }
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
        this.init(args.item);
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
        this.init();
    }
})(jQuery);
