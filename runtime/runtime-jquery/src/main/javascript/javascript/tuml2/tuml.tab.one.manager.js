(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabOneManager: TumlTabOneManager
        }
    });

    function TumlTabOneManager(tumlUri) {

        var self = this;
        var $data;
        var $metaForData;
        var $qualifiedName;

        function init() {
        }

        function refresh(data, metaForData, qualifiedName) {
            var $contextVertexId = retrieveVertexId(tumlUri);
            $data = data;
            $metaForData = metaForData;
            $qualifiedName = qualifiedName;
            //Clear all elements
            var tabDiv = $('#' + $metaForData.name);
            tabDiv.children().remove();
            $('<div id="serverErrorMsg" />').appendTo(tabDiv);
            var $formDiv = $('<div />', {id: 'formDiv'}).appendTo(tabDiv);

            var ul = $('<ul class="oneUl" />')
            ul.appendTo($formDiv);
            $.each($metaForData.properties, function(index, property) {
                if (isPropertyForOnePage(property)) {
                    var li = $('<li>')
                    li.appendTo(ul);
                    $('<label />', {for: property.name + 'Id'}).text(property.name + ' :').appendTo(li);
                    var $input = constructInputForField(data, property);
                    $input.appendTo(li);
                    if ($input !== undefined) {
                        if (property.dataTypeEnum !== undefined) {
                            if (property.dataTypeEnum == 'Date') {
                                $input.datepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    dateFormat: "yy-mm-dd",
                                    buttonImage: "/restAndJson/javascript/slickgrid/images/calendar.gif"
                                });
                            } else if (property.dataTypeEnum == 'Time') {
                                $input.timepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    buttonImage: "/restAndJson/javascript/slickgrid/images/calendar.gif"
                                });
                            } else if (property.dataTypeEnum == 'DateTime') {
                                $input.datetimepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    buttonImage: "/restAndJson/javascript/slickgrid/images/calendar.gif",
                                    dateFormat: "yy-mm-dd",
                                    timeFormat: "hh:mm:ss"
                                });
                            }
                        }
                    }
                }
            });

            var $buttonDiv = $('<div class="onesavebuttondiv" />').appendTo($formDiv);

            var $saveButton = $('<button />').text('Save').click(function() {
                var validationResults = [];
                $.each($metaForData.properties, function(index, property) {
                    if (!property.readOnly && isPropertyForOnePage(property)) {
                        var validationResult = validateField(property);
                        if (!validationResult.valid) {
                            validationResults.push(validationResult);
                        }
                    }
                });
                if (validationResults.length === 0) {
                    if (data !== undefined && data !== null) {
                        $.ajax({
                            url: metaForData.qualifiedName == qualifiedName ? tumlUri :tumlUri + '_' + $metaForData.name, 
                            type: "PUT",
                            dataType: "json",
                            contentType: "json",
                            data: fieldsToJson(),
                            success: function(data, textStatus, jqXHR) {
                                self.onPutOneSuccess.notify({tumlUri: tumlUri, data: data}, null, self);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                                self.onPutOneFailure.notify({tumlUri: tumlUri, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}, null, self);
                            }
                        });
                    } else {
                        $.ajax({
                            url: tumlUri + '_' + $metaForData.name,
                            type: "POST",
                            dataType: "json",
                            contentType: "json",
                            data: fieldsToJson(),
                            success: function(data, textStatus, jqXHR) {
                                self.onPostOneSuccess.notify({tumlUri: tumlUri, data: data}, null, self);
                            },
                            error: function(jqXHR, textStatus, errorThrown) {
                                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                                self.onPostOneFailure.notify({tumlUri: tumlUri, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}, null, self);
                            }
                        });
                    }
                }
                return false ;
            }).appendTo($buttonDiv);

            var $cancelButton = $('<button />').text('Cancel').click(function() {
                refresh($data, $metaForData, $qualifiedName);
            }).appendTo($buttonDiv);

        }

        function isPropertyForOnePage(property) {
            return(!property.inverseComposite && !property.composite && ((property.oneToOne || property.manyToOne) || property.manyPrimitive) && property.name !== 'uri'); 
        }

        function removeServerErrorMessage() {
            $('#serverErrorMsg').removeClass('server-error-msg').empty();
        }

        function constructInputForField(data, property) {
            var $input;
            if (property.name == 'id') {
                $input = $('<input />', {disabled: 'disabled', type:'text', class: 'field', id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            } else if (property.readOnly) {

                if (data !== undefined && data !== null) {
                    $input = $('<p />').text(data[property.name]);
                } else {
                    $input = $('<p />');
                }
            } else if (property.dataTypeEnum !== undefined) {
                if (property.dataTypeEnum == 'Date' || property.dataTypeEnum == 'Time' || property.dataTypeEnum == 'DateTime' || property.dataTypeEnum == 'InternationalPhoneNumber' || property.dataTypeEnum == 'LocalPhoneNumber' || property.dataTypeEnum == 'Email') {
                    $input = $("<input />", {type:'text',id: property.name + $metaForData.name + 'Id', name: property.name});
                } else if (property.dataTypeEnum == 'Video') {
                    $input = $("<input />", {type:'text',id: property.name + $metaForData.name + 'Id', name: property.name});
                } else  if (property.dataTypeEnum == 'Audio') {
                    $input = $("<input />", {type:'text',id: property.name + $metaForData.name + 'Id', name: property.name});
                } else if (property.dataTypeEnum == 'Image') {
                    $input = $("<input />", {type:'text',id: property.name + $metaForData.name + 'Id', name: property.name});
                } else {
                    alert('Unsupported dataType ' + property.dataTypeEnum);
                }
                if (data !== undefined && data !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            } else if (property.oneEnumeration) {
                $input = $('<select />', {class: 'chzn-select', style: 'width:350px;',  id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    appendEnumerationLoopupOptionsToSelect("/restAndJson/tumlEnumLookup", property.qualifiedName, property.lower > 0, data[property.name], $input);
                } else {
                    appendEnumerationLoopupOptionsToSelect("/restAndJson/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
                }
            } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite) {
                $input = $('<select />', {class: 'chzn-select', style: 'width:350px;',  id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    appendLoopupOptionsToSelect(property.tumlLookupUri, property.lower > 0, data['id'], data[property.name], $input);
                } else {
                    appendLoopupOptionsToSelect(property.tumlLookupOnCompositeParentUri, property.lower > 0, data['id'], data[property.name], $input);
                }
            } else if (property.fieldType == 'String') {
                $input = $('<input />', {type:'text', class: 'field', id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            } else if (property.fieldType == 'Integer') {
                $input = $('<input />', {type:'text', class: 'field', id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            } else if (property.fieldType == 'Long') {
                $input = $('<input />', {type:'text', class: 'field', id: property.name + $metaForData.name + 'Id', name: property.name});
                if (data !== undefined && data !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            } else if (property.fieldType == 'Boolean') {
                if (!property.manyPrimitive && data !== undefined && data !== null) {
                    if (data[property.name]) {
                        $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + $metaForData.name + 'Id', name: property.name, checked: 'checked'});
                    } else {
                        $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + $metaForData.name + 'Id', name: property.name });
                    }
                } else if (!property.manyPrimitive) {
                    $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + $metaForData.name + 'Id', name: property.name });
                } else {
                    $input = $('<input />', {type:'text', class: 'field', id: property.name + $metaForData.name + 'Id', name: property.name});
                    if (data !== undefined && data !== null) {
                        $input[0].defaultValue = data[property.name];
                    }
                }
            }
            $input.blur(function() {
                validateField(property);
            });
            return $input;
        }

        function validateField(property) {
            removeServerErrorMessage();
            var validateInput = $('#' + property.name + $metaForData.name + 'Id');
            
            var serializedValue = [];
            var editor = selectEditor(property);
            var validationResult;
            if (property.manyPrimitive) {
                var stringValueArray = validateInput.val().split(',');
                var tmpSerializedValue = [];
                for (var i = 0; i < stringValueArray.length; i++) {
                    var tmpValue = stringValueArray[i];
                    if (property.fieldType === 'Integer') {
                        if (isNaN(tmpValue)) {
                            validationResult = {valid: false, msg: 'Please enter a valid number.'};
                        } else {
                            if (property.unique && tmpSerializedValue.indexOf(parseInt(stringValueArray[i])) !== -1) {
                                validationResult = {valid: false, msg: 'Duplicates are not allowed.'};
                            } else {
                                tmpSerializedValue.push(parseInt(stringValueArray[i]));
                            }
                        }
                    } else if (property.fieldType === 'Boolean') {
                        if (tmpValue != 'true' && tmpValue != 'false') {
                            validationResult = {valid: false, msg: 'Value must be "true" or "false".'};
                        }
                    } else {
                        if (property.unique && tmpSerializedValue.indexOf(stringValueArray[i]) !== -1) {
                            validationResult = {valid: false, msg: 'Duplicates are not allowed.'};
                        } else {
                            tmpSerializedValue.push(stringValueArray[i]);
                        }
                    }
                }
                if (validationResult === undefined) {
                    validationResult = selectFieldValidator(property)(tmpSerializedValue);
                }
            } else {
                serializedValue = new editor().serializeValueWithValue(validateInput);
                validationResult = selectFieldValidator(property)(serializedValue);
            }
            //selectFieldValidator returns the validate function
            if (!validationResult.valid) {
                validateInput.removeClass('validation-error');
                validateInput.parent().children('.validation-error-msg').remove();
                validateInput.addClass('validation-error');
                validateInput.parent().append($('<span class="validation-error-msg" />').text(validationResult.msg));
            } else {
                validateInput.removeClass('validation-error');
                validateInput.parent().children('.validation-error-msg').remove();
            }

            return validationResult;
        }
        
        function appendEnumerationLoopupOptionsToSelect(tumlLookupUri, propertyJavaClassName, required, currentValue, select) {
            var jqxhr = $.getJSON(tumlLookupUri + '?enumQualifiedName=' + propertyJavaClassName, function(response, b, c) {
                //if not a required field add a black value
                if (!required) {
                    select.append($('<option />)').val("").html(""));
                }
                for (var i = 0; i < response.data.length; i++) {
                    select.append($('<option />)').val(response.data[i]).html(response.data[i]));
                }
                if (currentValue !== null) {
                    select.val(currentValue);
                }
                if (!required) {
                    select.chosen({allow_single_deselect: true});
                } else {
                    select.chosen();
                }
                select.focus();
            }).fail(function(a, b, c) {
                alert("error " + a + ' ' + b + ' ' + c);
            });
        }
        function appendLoopupOptionsToSelect(tumlLookupUri, required, contextVertexId, currentValue, $select) {
            var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
            var jqxhr = $.getJSON(adjustedUri, function(response, b, c) {
                var contextVertexId = retrieveVertexId(tumlUri);
                    //if not a required field add a black value
                if (!required) {
                    $select.append($('<option />)').val("").html(""));
                }
                //append the current value to the dropdown
                $select.append($('<option />)').val(currentValue.id).html(currentValue.displayName));
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
                $select.val(currentValue.id);
                $select.chosen({allow_single_deselect: true});
            }).fail(function(a, b, c) {
                alert("error " + a + ' ' + b + ' ' + c);
            });
        }

        function findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom) {
            if (metaDataNavigatingFrom  == undefined) {
                return null;
            } else {
                //The property one is navigating from is in the metaDataNavigatingFrom,
                //Find the property with the qualifiedName for the metaDataNavigatingTo.qualifiedName
                for (var i = 0; i < metaDataNavigatingFrom.properties.length; i++) {
                    var property = metaDataNavigatingFrom.properties[i];
                    if (property.qualifiedName == qualifiedName) {
                        return property;
                    }
                }
                alert('Property navigatingTo not found!!!');
                return null;
            }
        }

        function fieldsToJson() {
            var dataToSend = {};
            $.each($metaForData.properties, function(index, property) {
                if (property.name === 'id') {
                    dataToSend[property.name] = parseInt($('#' + property.name + $metaForData.name + 'Id').val());
                } else if (property.readOnly) {
                    //Do nothing
                } else if (property.name !== 'uri') {
                    if (property.onePrimitive) {
                        var stringValue = $('#' + property.name + $metaForData.name + 'Id').val();
                        if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                            if (stringValue == '') {
                                dataToSend[property.name] = null;
                            } else {
                                dataToSend[property.name] = parseInt(stringValue);
                            }
                        } else if (property.fieldType == 'String') {
                            if (stringValue == '') {
                                dataToSend[property.name] = null;
                            } else {
                                dataToSend[property.name] = stringValue;
                            }
                        } else if (property.fieldType == 'Boolean') {
                            dataToSend[property.name] = $('#' + property.name + $metaForData.name + 'Id').attr("checked") == "checked";
                        }
                    } else if (property.dataTypeEnum !== undefined) {
                        if (property.dataTypeEnum == 'DateTime') {
                            dataToSend[property.name] = $('#' + property.name + $metaForData.name + 'Id').val().replace(/ /g,"T");
                        } else {
                            dataToSend[property.name] = $('#' + property.name + $metaForData.name + 'Id').val();
                        }
                    } else if (property.oneEnumeration) {
                        var $select = $('#' + property.name + $metaForData.name + 'Id');
                        var options = $select.children();
                        for (var i = 0; i < options.length; i++) {
                            if (options[i].selected) {
                                dataToSend[property.name]= $select.val();
                                break;
                            }
                        }
                    } else if (property.manyPrimitive) {
                        var inputValue = $('#' + property.name + $metaForData.name + 'Id').val();
                        var array = inputValue.split(',');
                        dataToSend[property.name] = array;
                    } else if (!property.onePrimitive && !property.manyPrimitive && !property.inverseComposite) {
                        var $select = $('#' + property.name + $metaForData.name + 'Id');
                        var options = $select.children();
                        for (var i = 0; i < options.length; i++) {
                            if (options[i].selected) {
                                dataToSend[property.name]= {id: parseInt($select.val()), displayName: options[i].label};
                                break;
                            }
                        }
                    }
                }});
                var result = JSON.stringify(dataToSend);
                return result;
        }
        //Public api
        $.extend(this, {
            "TumlTabOneManagerVersion": "1.0.0",
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "onPostOneSuccess": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "refresh": refresh
        });

        init();
    }

})(jQuery);
