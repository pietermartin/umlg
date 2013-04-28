(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabOneManager: TumlTabOneManager,
            TumlTabComponentOneManager: TumlTabComponentOneManager
        }
    });

    function TumlTabComponentOneManager(tumlUri, tumlTabViewManager) {
        var self = this;
        TumlBaseTabOneManager.call(this, tumlUri, tumlTabViewManager);

        //Public api
        $.extend(this, {
            "TumlTabComponentOneManager": "1.0.0",
            "onOneComponentSaveButtonSuccess": new Tuml.Event(),
            "onOneComponentCloseButtonSuccess": new Tuml.Event()
        });

        this.doSave = function () {
            if (self.validateFields().length === 0) {
                var oneObject = self.fieldsToObject();
                self.onOneComponentSaveButtonSuccess.notify({value: oneObject, tabName: self.metaForData.name}, null, self);
            }
        }

        this.getDiv = function () {
            return $('#' + this.metaForData.to.name + "OneComponent");
        }

        this.doClose = function () {
            this.doCancel();
            self.onOneComponentCloseButtonSuccess.notify({tabName: self.metaForData.name}, null, self);
        }

    }

    TumlTabComponentOneManager.prototype = new TumlBaseTabOneManager;

    function TumlTabOneManager(tumlUri) {
        var self = this;
        this.tumlUri = tumlUri;
        TumlBaseTabOneManager.call(this, tumlUri);

        this.doSave = function () {
            var validationResults = self.validateFields();
            if (validationResults.length === 0) {
                if (this.data !== undefined && this.data !== null) {
                    $.ajax({
                        url: this.metaForData.qualifiedName == this.qualifiedName ? tumlUri : tumlUri + '_' + this.metaForData.name,
                        type: "PUT",
                        dataType: "json",
                        contentType: "json",
                        data: self.fieldsToJson(),
                        success: function (data, textStatus, jqXHR) {
                            self.data = data[0].data[0];
                            self.onPutOneSuccess.notify({tumlUri: tumlUri, data: data}, null, self);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                            self.onPutOneFailure.notify({tumlUri: tumlUri, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}, null, self);
                        }
                    });
                } else {
                    $.ajax({
                        url: tumlUri + '_' + this.metaForData.name,
                        type: "POST",
                        dataType: "json",
                        contentType: "json",
                        data: self.fieldsToJson(),
                        success: function (data, textStatus, jqXHR) {
                            self.onPostOneSuccess.notify({tumlUri: tumlUri, data: data}, null, self);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                            self.onPostOneFailure.notify({tumlUri: tumlUri, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}, null, self);
                        }
                    });
                }
            } else {
                var errorMsg = '\n';
                for (var i = 0; i < validationResults.length; i++) {
                    errorMsg += validationResults[i].msg + '\n';
                }
                alert('There are validation errors: ' + errorMsg);
            }
        }

        this.doDelete = function () {
            $.ajax({
                url: self.tumlUri,
                type: "DELETE",
                dataType: "json",
                contentType: "json",
                data: self.fieldsToJson(),
                success: function (data, textStatus, jqXHR) {
                    self.onDeleteOneSuccess.notify({tumlUri: tumlUri, data: data}, null, self);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                    self.onDeleteOneFailure.notify({tumlUri: tumlUri, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown}, null, self);
                }
            });
        }

        this.getDiv = function () {
            return $('#' + this.metaForData.name);
        }

    }

    TumlTabOneManager.prototype = new TumlBaseTabOneManager;

    function TumlBaseTabOneManager(tumlUri, tumlTabViewManager) {

        var self = this;
        this.tumlTabViewManager = tumlTabViewManager;

        function init() {
        }

        this.setValue = function (property, value) {
            var input = $("#" + property.name + this.metaForData.name + 'Id');
            $.data(input[0], "componentValue", value);
            input[0].defaultValue = value;
        }

        this.validateFields = function () {
            var validationResults = [];
            $.each(this.metaForData.properties, function (index, property) {
                if (!property.readOnly && self.isPropertyForOnePage(property)) {
                    var validationResult = self.validateField(property);
                    if (!validationResult.valid) {
                        validationResults.push(validationResult);
                    }
                }
            });
            return validationResults;
        }

        this.updateGridAfterRollback = function (item) {
            for (var i = 0; i < this.metaForData.to.properties.length; i++) {
                var property = this.metaForData.to.properties[i];
                if (this.isPropertyForOnePage(property, false)) {
                    this.setValueOfInputForField(item, property);
                }
            }
        }

        this.handleLookup = function (lookupUri, qualifiedName, loadDataCallback) {
            this.tumlTabViewManager.handleLookup(lookupUri, qualifiedName, loadDataCallback);
        }

        function findPropertyNavigatingTo(qualifiedName, metaDataNavigatingFrom) {
            if (metaDataNavigatingFrom == undefined) {
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

        //Public api
        $.extend(this, {
            "TumlTabOneManagerVersion": "1.0.0",
            "onClickOneComponent": new Tuml.Event(),
            "onClickManyComponent": new Tuml.Event(),
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "onPostOneSuccess": new Tuml.Event(),
            "onPostOneFailure": new Tuml.Event(),
            "onDeleteOneSuccess": new Tuml.Event(),
            "onDeleteOneFailure": new Tuml.Event()
        });

        init();
    }

    TumlBaseTabOneManager.prototype.fieldsToJson = function () {
        return JSON.stringify(this.fieldsToObject());
    }

    TumlBaseTabOneManager.prototype.fieldsToObject = function () {
        var dataToSend = {};
        dataToSend.qualifiedName = this.metaForData.qualifiedName;
//        if (this.isForCreation) {
//            dataToSend.tmpId = 'fake:0';
//        }
        for (var i = 0; i < this.metaForData.to.properties.length; i++) {
            var property = this.metaForData.to.properties[i];
            if (property.name === 'id') {
                dataToSend.id = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                dataToSend.tmpId = dataToSend.id;
            } else if (property.readOnly) {
                //Do nothing
            } else if (property.name !== 'uri') {
                if (property.onePrimitive) {
                    var stringValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
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
                        dataToSend[property.name] = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').attr("checked") == "checked";
                    }
                } else if (property.dataTypeEnum !== null && property.dataTypeEnum !== undefined) {
                    if (property.dataTypeEnum == 'DateTime') {
                        dataToSend[property.name] = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val().replace(/ /g, "T");
                    } else if (property.dataTypeEnum == 'Image') {
                    } else {
                        dataToSend[property.name] = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                    }
                } else if (property.oneEnumeration) {
                    var $select = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                    var options = $select.children();
                    for (var j = 0; i < options.length; j++) {
                        if (options[j].selected) {
                            dataToSend[property.name] = $select.val();
                            break;
                        }
                    }
                } else if (property.manyPrimitive) {
                    var inputValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                    var array = inputValue.split(',');
                    dataToSend[property.name] = array;
                } else if (property.manyEnumeration) {
                    var inputValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                    dataToSend[property.name] = inputValue;
                } else if (property.composite && property.lower > 0) {
                    var input = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                    if (input.length !== 0) {
                        dataToSend[property.name] = $.data(input[0], "componentValue");
                    }
                } else if (!property.onePrimitive && !property.manyPrimitive && !property.inverseComposite) {
                    var $select = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                    var options = $select.children();
                    for (var j = 0; j < options.length; j++) {
                        if (options[j].selected) {
                            dataToSend[property.name] = {id: $select.val(), displayName: options[j].label};
                            break;
                        }
                    }
                }
            }
        }
        ;
        return dataToSend;
    }


    TumlBaseTabOneManager.prototype.getDiv = function () {
        console.log('TumlBaseTabOneManager.prototype.getDiv must be overriden');
    }

    TumlBaseTabOneManager.prototype.refresh = function (data, metaForData, qualifiedName, isForCreation) {
        var self = this;
        this.data = data;
        this.metaForData = metaForData;
        this.qualifiedName = qualifiedName;
        this.isForCreation = isForCreation;
        //Clear all elements
        var tabDiv = this.getDiv();
        tabDiv.children().remove();
        $('<div id="serverErrorMsg" />').appendTo(tabDiv);
        var formDiv = $('<div />', {id: 'formDiv'}).appendTo(tabDiv);

        var ul = $('<ul class="oneUl" />')
        ul.appendTo(formDiv);
        if (this.metaForData.to.name !== 'Root') {
            for (var i = 0; i < this.metaForData.to.properties.length; i++) {
                var property = this.metaForData.to.properties[i];
                if (this.isPropertyForOnePage(property, isForCreation)) {
                    var li = $('<li>')
                    li.appendTo(ul);
                    $('<label />', {for: property.name + 'Id'}).text(property.name + ' :').appendTo(li);
                    var $input = this.constructInputForField(data, property, isForCreation);
                    $input.appendTo(li);
                    if (property.manyPrimitive) {
                        var $manyDiv = $('<div />', {class: "many-primitive-one-img"}).appendTo(li);
                        $manyDiv.click(function (e) {
                            self.openEditorForMany($input, property, $manyDiv, li);
                        });
                        $input.keypress(function (e) {
                            if (e.which == 13) {
                                self.openEditorForMany($input, property, $manyDiv, li);
                            }
                        });
                    } else if (property.composite && property.lower == 1 && property.upper == 1) {
                        $input.click(function (e, args) {
                            var data = [];
                            var componentValue = $.data(this, "componentValue");
                            if (componentValue !== undefined) {
                                data.push(componentValue);
                            }
                            this.onClickOneComponent.notify(
                                {data: data, tumlUri: property.tumlUri, property: property},
                                null,
                                self
                            );
                        });
                    } else if (property.composite && property.lower >= 1 && (property.upper > 1 || property.upper == -1)) {
                        $input.click(function (e, args) {
                            var data = [];
                            var componentValue = $.data(this, "componentValue");
                            if (componentValue !== undefined) {
                                data = componentValue;
                            }
                            this.onClickManyComponent.notify(
                                {data: data, tumlUri: property.tumlUri, property: property},
                                null,
                                self
                            );
                        });
                    }
                    if ($input !== undefined) {
                        if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
                            if (property.dataTypeEnum == 'Date') {
                                $input.datepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    dateFormat: "yy-mm-dd",
                                    buttonImage: "/" + tumlModelName + "/javascript/slickgrid/images/calendar.gif"
                                });
                            } else if (property.dataTypeEnum == 'Time') {
                                $input.timepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    buttonImage: "/" + tumlModelName + "/javascript/slickgrid/images/calendar.gif"
                                });
                            } else if (property.dataTypeEnum == 'DateTime') {
                                $input.datetimepicker({
                                    showOn: "button",
                                    buttonImageOnly: true,
                                    buttonImage: "/" + tumlModelName + "/javascript/slickgrid/images/calendar.gif",
                                    dateFormat: "yy-mm-dd",
                                    timeFormat: "hh:mm:ss"
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    TumlBaseTabOneManager.prototype.doSave = function () {
        alert("doSave must be overridden");
    };

    TumlBaseTabOneManager.prototype.doDelete = function () {
        alert("doDelete must be overridden");
    };

    TumlBaseTabOneManager.prototype.doCancel = function () {
        this.refresh(this.data, this.metaForData, this.qualifiedName);
    }

    TumlBaseTabOneManager.prototype.isPropertyForOnePage = function (property, isForCreation) {
        return(!property.inverseComposite
            && (!property.composite && (property.oneToOne || property.manyToOne || property.manyPrimitive || property.manyEnumeration))
            || (isForCreation && property.composite && property.lower > 0)
            && property.name !== 'uri');
    }

    TumlBaseTabOneManager.prototype.constructInputForField = function (data, property, isForCreation) {
        var self = this;
        var $input;
        if (property.name == 'id') {
            $input = $('<input />', {disabled: 'disabled', type: 'text', class: 'field', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
            }
        } else if (property.readOnly) {
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input = $('<p />').text(data[property.name]);
            } else {
                $input = $('<p />');
            }
        } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
            if (property.dataTypeEnum == 'Date' || property.dataTypeEnum == 'Time' || property.dataTypeEnum == 'DateTime' || property.dataTypeEnum == 'InternationalPhoneNumber' || property.dataTypeEnum == 'LocalPhoneNumber' || property.dataTypeEnum == 'Email') {
                $input = $("<input />", {type: 'text', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            } else if (property.dataTypeEnum == 'Video') {
                $input = $("<input />", {type: 'text', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            } else if (property.dataTypeEnum == 'Audio') {
                $input = $("<input />", {type: 'text', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            } else if (property.dataTypeEnum == 'Image') {
                $input = $("<input />", {type: 'text', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
            }
        } else if (property.composite && property.lower > 0) {
            $input = $("<input />", {type: 'text', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
                $.data($input[0], "componentValue", data[property.name]);
            }

        } else if (property.oneEnumeration) {
            $input = $('<select />', {class: 'chzn-select', style: 'width:350px;', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (property.manyEnumeration) {
            $input = $('<select />', {multiple: '', id: property.name + this.metaForData.qualifiedName + 'Id', class: 'chzn-select', style: 'width:350px;', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.oneToOne) {
            $input = $('<select />', {class: 'chzn-select', style: 'width:350px;', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});

            if (!isForCreation) {
                this.appendLoopupOptionsToSelect2(property, data['id'], $input, data[property.name]);
            }

        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.manyToOne) {
            $input = $('<select />', {class: 'chzn-select', style: 'width:350px;', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                this.appendLoopupOptionsToSelect(property.tumlLookupUri, property.lower > 0, data['id'], data[property.name], $input);
            }
        } else if (property.fieldType == 'String') {
            $input = $('<input />', {type: 'text', class: 'field', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Integer') {
            $input = $('<input />', {type: 'text', class: 'field', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Long') {
            $input = $('<input />', {type: 'text', class: 'field', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
            if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                $input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Boolean') {
            if (!property.manyPrimitive && data !== undefined && data !== null) {
                if (!isForCreation && data[property.name] !== undefined) {
                    $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name, checked: 'checked'});
                } else {
                    $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name });
                }
            } else if (!property.manyPrimitive) {
                $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name });
            } else {
                $input = $('<input />', {type: 'text', class: 'field', id: property.name + this.metaForData.qualifiedName + 'Id', name: property.name});
                if (!isForCreation && data[property.name] !== undefined && data[property.name] !== null) {
                    $input[0].defaultValue = data[property.name];
                }
            }
        }
        if (!(property.composite && property.lower > 0)) {
            $input.blur(function () {
                self.validateField(property);
            });
        }
        return $input;
    }

    TumlBaseTabOneManager.prototype.setValueOfInputForField = function (data, property) {
        var self = this;
        if (property.name == 'id') {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
            }
        } else if (property.readOnly) {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input.text(data[property.name]);
            }
        } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
            }
        } else if (property.composite && property.lower > 0) {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
                $.data($input[0], "componentValue", data[property.name]);
            }
        } else if (property.oneEnumeration) {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (property.manyEnumeration) {
            if (data[property.name] !== undefined && data[property.name] !== null) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.oneToOne) {

            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            this.appendLoopupOptionsToSelect2(property, data['tmpId'], input, data[property.name]);

        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.manyToOne) {
            if (data[property.name] !== undefined && data[property.name] !== null) {
                this.appendLoopupOptionsToSelect(property.tumlLookupUri, property.lower > 0, data['id'], data[property.name], input);
            } else {
                this.appendLoopupOptionsToSelect(property.tumlLookupOnCompositeParentUri, property.lower > 0, data['id'], data[property.name], input);
            }
        } else if (property.fieldType == 'String') {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Integer') {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Long') {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            if (data[property.name] !== undefined && data[property.name] !== null) {
                input[0].defaultValue = data[property.name];
            }
        } else if (property.fieldType == 'Boolean') {
            if (!property.manyPrimitive) {
                var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                if (data[property.manyPrimitive] !== undefined && data[property.name] !== null) {
                    input[0].attr('checked', 'checked');
                } else {
                    input[0].attr('checked', '');
                }
            } else if (property.manyPrimitive) {
                var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                if (data[property.name] !== undefined && data[property.name] !== null) {
                    input[0].defaultValue = data[property.name];
                }
            }
        }
        if (!(property.composite && property.lower > 0)) {
            var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
            input.blur(function () {
                self.validateField(property);
            });
        }
        return input;
    }

    TumlBaseTabOneManager.prototype.openEditorForMany = function ($inputToSet, property, $manyDiv, $li) {
        var $div = $("<div class='many-primitive-editor-for-one' />");
        $div.keyup(function (e) {
            if (e.which == 27) {
                $('.many-primitive-editor-cancel').click();
            }
        });
        $(document).mouseup(function (e) {
            var container = $(".many-primitive-editor-for-one");
            if (container.has(e.target).length === 0) {
                container.remove();
            }
        });
        $div.css('left', $manyDiv.position().left + 10);
        $div.css('top', $manyDiv.position().top + 5);
        var $table;
        var $input;
        var editor = selectEditor(property);
        var serializer = new editor().serializeValueForOne;
        var button = $('<button id="many-primitive-editor-input-add-button" />').text('Add').click(function () {
            var valueToAdd = $('.many-primitive-editor-input').val();
            var currentValues = serializer($table);
            var testArray = [];
            testArray.push(valueToAdd);
            var validator = selectFieldValidator(property);
            var validationResults = validator(testArray);
            if (currentValues.length !== 0 && validationResults.valid && property.unique) {
                validationResults = validator(currentValues, valueToAdd);
            }
            if (!validationResults.valid) {
                alert('YYY' + validationResults.msg);
            } else {
                addTr(valueToAdd);
                //Need to reapply the drag and drop plugin if the table was empty
                if (currentValues.length == 0) {
                    if (property.ordered) {
                        $table.tableDnD();
                    }
                }
                $input.val('');
            }
        }).appendTo($div);
        $input = this.constructInputForField(null, property);
        $input.addClass("many-primitive-editor-input");
        $input.appendTo($div);
        $input.keypress(function (e) {
            if (e.which == 13) {
                $('#many-primitive-editor-input-add-button').click();
            }
        });
        var resultDiv = $('<div class="many-primitive-editor-result" />').appendTo($div);
        $table = $('<table class="many-primitive-editor-result-table" />').appendTo(resultDiv);
        //Add in current value
        var existingValues = $inputToSet.val().split(',');
        for (var i = 0; i < existingValues.length; i++) {
            if (existingValues[i] !== '') {
                addTr(existingValues[i]);
            }
        }
        if (property.ordered) {
            $table.tableDnD();
        }
        var selectButtonDiv = $('<div class="many-primitive-editor-select-div"/>').appendTo($div);
        selectButtonDiv.append($('<button class="many-primitive-editor-select"/>').click(function () {
            var currentValues = serializer($table);
            $inputToSet.val(currentValues.join(","));
            $div.remove();
        }).text('Select'));
        selectButtonDiv.append($('<button class="many-primitive-editor-cancel"/>').click(function () {
            $div.remove();
        }).text('Cancel'));
        $div.appendTo($li);
        $input.focus();

        function addTr(value) {
            var row = $('<tr />').addClass('many-primitive-editor-row');
            var rowValue = $('<td class="many-primitive-editor-cell" />').text(value);
            row.append(rowValue);
            row.data('value', value);
            var img = $('<img/>', {class: 'tuml-many-select-img', src: '/' + tumlModelName + '/javascript/images/delete.png'}).click(function () {
                var liClicked = $(this).parent().parent();
                liClicked.remove();
            });
            var imgValue = $('<td class="many-primitive-editor-cell many-primitive-editor-cell-img" />');
            imgValue.append(img);
            row.append(imgValue);
            $table.append(row);
        }

    }

    TumlBaseTabOneManager.prototype.appendEnumerationLoopupOptionsToSelect = function (tumlLookupUri, propertyJavaClassName, required, currentValue, select) {
        var jqxhr = $.getJSON(tumlLookupUri + '?enumQualifiedName=' + propertyJavaClassName,function (response, b, c) {
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
        }).fail(function (a, b, c) {
                alert("error " + a + ' ' + b + ' ' + c);
            }
        );
    }

    TumlBaseTabOneManager.prototype.appendLoopupOptionsToSelect = function (tumlLookupUri, required, contextVertexId, currentValue, $select) {

        var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);

        this.handleLookup(adjustedUri, item.qualifiedName,
            function (data) {
                if (!required) {
                    $select.append($('<option />)').val("").html(""));
                }
                for (var i = 0; i < data.length; i++) {
                    var obj = data[i];
                    $select.append($('<option />)').val(obj.id).html(obj.name));
                }
                ;
                //currentValue is the vertex id of the oneToOne or manyToOne
                currentValue = item[args.column.field];
                //append the current value to the dropdown
                if (currentValue !== undefined && currentValue !== null) {
                    $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                }
                if (!required) {
                    $select.chosen({allow_single_deselect: true});
                } else {
                    $select.chosen();
                }
                $select.focus();
            }
        );

//        var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
//        var jqxhr = $.getJSON(adjustedUri,function (response, b, c) {
//            //if not a required field add a blank value
//            if (!required) {
//                $select.append($('<option />)').val("").html(""));
//            }
//            //append the current value to the dropdown
//            $select.append($('<option />)').val(currentValue.id).html(currentValue.displayName));
//            //Property is a many, meta has 2 properties, one for both sides of the association
//            //The first one is the parent property, i.e. the context property for which the menu is built
//            var options = [];
//            $.each(response.data, function (index, obj) {
//                var option = {};
//                option['value'] = obj.id;
//                option['desc'] = obj.name;
//                options.push(option);
//            });
//            for (var i = 0; i < options.length; i++) {
//                $select.append($('<option />)').val(options[i].value).html(options[i].desc));
//            }
//            $select.val(currentValue.id);
//            $select.chosen({allow_single_deselect: true});
//        }).fail(function (a, b, c) {
//                alert("error " + a + ' ' + b + ' ' + c);
//            }
//        );
    }

    TumlBaseTabOneManager.prototype.appendLoopupOptionsToSelect2 = function (property, contextVertexId, $select, currentValue) {

        var adjustedUri = property.tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        var required = property.lower > 0;

        this.handleLookup(adjustedUri, this.metaForData.qualifiedName,
            function (data) {
                if (!required) {
                    $select.append($('<option />)').val("").html(""));
                }
                for (var i = 0; i < data.length; i++) {
                    var obj = data[i];
                    $select.append($('<option />)').val(obj.id).html(obj.name));
                }
                ;
                //append the current value to the dropdown
                if (currentValue !== undefined && currentValue !== null) {
                    $select.append($('<option selected="selected"/>)').val(currentValue.id).html(currentValue.displayName));
                }
                if (!required) {
                    $select.chosen({allow_single_deselect: true});
                } else {
                    $select.chosen();
                }
                $select.focus();
            }
        );

    }

    TumlBaseTabOneManager.prototype.validateField = function (property) {
        this.removeServerErrorMessage();
        var validateInput;
        validateInput = $('#' + property.name + this.metaForData.name + 'Id');

        var serializedValue = [];
        var editor = selectEditor(property);
        var validationResult;
        if (property.manyPrimitive || property.manyEnumeration) {
            var stringValueArray;
            if (property.manyPrimitive) {
                stringValueArray = validateInput.val().split(',');
            } else {
                stringValueArray = validateInput.val();
            }
            if (stringValueArray == undefined || stringValueArray == null) {
                stringValueArray = [];
            }
            //Remove empty strings
            stringValueArray.remove('');
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
    TumlBaseTabOneManager.prototype.removeServerErrorMessage = function () {
        $('#serverErrorMsg').removeClass('server-error-msg').empty();
    }


})(jQuery);
