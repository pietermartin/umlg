(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabOneManager: TumlTabOneManager,
            TumlTabComponentOneManager: TumlTabComponentOneManager
        }
    });

    function TumlTabComponentOneManager(tumlTabViewManager) {
        TumlBaseTabOneManager.call(this, tumlTabViewManager);

        //Public api
        $.extend(this, {
            "TumlTabComponentOneManager": "1.0.0"
        });

        this.getDiv = function () {
            return $('#' + this.metaForData.name + "OneComponent");
        }

    }

    TumlTabComponentOneManager.prototype = new TumlBaseTabOneManager;

    function TumlTabOneManager(tumlTabViewManager) {
        TumlBaseTabOneManager.call(this, tumlTabViewManager);

        this.getDiv = function () {
            return $('#' + this.metaForData.name);
        }

    }

    TumlTabOneManager.prototype = new TumlBaseTabOneManager;

    function TumlBaseTabOneManager(tumlTabViewManager) {

        this.TumlBaseTabOneManager = "1.0.0";
        this.tumlTabViewManager = tumlTabViewManager;

        function init() {
        }

        this.updateGridAfterCommitOrRollback = function (item) {
            this.setDataModel(item);
            this.refreshFromDataModel();
        }

        this.handleLookup = function (lookupUri, qualifiedName, loadDataCallback) {
            this.tumlTabViewManager.handleLookup(lookupUri, qualifiedName, loadDataCallback);
        }

        this.calculateContainsOne = function () {
            for (var i = 0; i < this.metaForData.properties.length; i++) {
                var property = this.metaForData.properties[i];
                if (property.oneToOne && !property.onePrimitive) {
                    this.containsOneToOne = true;
                    break;
                }
            }
        }

        this.updateDataModelForOneToOne = function (data) {
            //update the global one to one index for every one
            for (var i = 0; i < this.metaForData.properties.length; i++) {
                var property = this.metaForData.properties[i];
                if (data[property.name] !== undefined && data[property.name] !== null && property.oneToOne && !property.inverseComposite && !property.onePrimitive) {
                    this.tumlTabViewManager.updateDataModelForOneToOne(data[property.name].previousId, property.inverseName, {});
                    this.tumlTabViewManager.updateDataModelForOneToOne(data[property.name].id, property.inverseName, data);
                }
            }
        }

        init();
    }

    TumlBaseTabOneManager.prototype.getDiv = function () {
        console.log('TumlBaseTabOneManager.prototype.getDiv must be overriden');
    }

    //TODO check this for updateData
    /**
     * This is called from tum.tab.view.manager.setCellValue for saving a component on this one
     * @param property
     * @param value
     */
    TumlBaseTabOneManager.prototype.updateData = function (property, value) {
        var foundProperty = false;
        for (var i = 0; i < this.metaForData.properties.length; i++) {
            var metaProperty = this.metaForData.properties[i];
            if (metaProperty.name == property.name) {
                foundProperty = true;
                break;
            }
        }
        if (!foundProperty) {
            throw 'Updating non existing property on one. Property = ' + property.name;
        }
        this.updatedData[property.name] = value;
    }

    TumlBaseTabOneManager.prototype.refresh = function (data, metaForData, isForCreation) {
        var self = this;
        this.currentActiveProperty = null;
        this.metaForData = metaForData.to;
        this.qualifiedName = this.metaForData.qualifiedName;
        this.isForCreation = isForCreation;
        if (this.isForCreation) {
            this.updatedData = JSON.parse(JSON.stringify(data));
            this.updatedData.qualifiedName = this.qualifiedName;
            this.data = data;
            this.data.qualifiedName = this.qualifiedName;
        } else {
            this.updatedData = {};
            this.updatedData.qualifiedName = this.qualifiedName;
            this.data = data;
            this.data.qualifiedName = this.qualifiedName;
        }

        this.calculateContainsOne();

        //Clear all elements
        var tabDiv = this.getDiv();
        tabDiv.children().remove();
        $('<div id="serverErrorMsg" />').appendTo(tabDiv);
        //The main div that holds all elements
        var formDiv = $('<div />', {id: 'formDiv' + this.metaForData.name, role: 'form', class: 'form-horizontal'}).appendTo(tabDiv);

        if (this.metaForData.name !== tumlModelName) {
            //Do everything except for select boxes that require lookups.
            //Lookups need to do a server call and as such need the id field set.
            var first = true;
            for (var i = 0; i < this.metaForData.properties.length; i++) {
                var property = this.metaForData.properties[i];
                if (this.isPropertyForOnePage(property)) {

                    if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.oneToOne) {
                        continue;
                    } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.manyToOne) {
                        continue;
                    }

                    //Each input is wrapped in a form-group
                    var formGroupDiv = $('<div>', {class: 'umlg-form-group form-group'}).appendTo(formDiv);

                    //TODO What is this about?
                    if (property.fieldType !== 'Boolean' || property.upper > 1 || property.upper === -1) {
                        var label = $('<label />', {for: property.name + 'Id', class: "col-lg-3 control-label"});
                        if (property.derived) {
                            //TODO use font here for derived indicator
                            label.text('/ ' + property.name + ' :');
                        } else {
                            label.text(property.name + ' :')
                        }
                        label.appendTo(formGroupDiv);
                    } else {
                        console.log('wf');
                    }
                    var $input = this.constructInputForField(property, false);

                    if (first) {
                        first = false;
                        this.currentActiveProperty = property;
                    }

                    var inputDiv = $('<div />', {class: 'input-group col-lg-5'}).appendTo(formGroupDiv);
                    if (property.fieldType !== 'Boolean' && !property.oneEnumeration && !property.readOnly && property.lower > 0) {
                        inputDiv.append('<span class="input-group-addon glyphicon glyphicon-asterisk"></span>');
                    }
                    if (property.fieldType == 'Boolean' && property.upper == 1) {
                        inputDiv.addClass('col-lg-offset-3');
                        var checkboxLabel = $('<label />');
                        checkboxLabel.append($input);
                        checkboxLabel.append(' ' + property.name);
                        checkboxLabel.appendTo(inputDiv);
                    } else {
                        $input.appendTo(inputDiv);
                        if (property.fieldType !== 'Boolean' && !property.oneEnumeration && !property.readOnly && property.lower > 0) {
                        } else {
                            if (property.lower === 0 &&  property.dataTypeEnum !== 'Date' && property.dataTypeEnum !== 'Time' && property.dataTypeEnum !== 'DateTime') {
                                inputDiv.append('<span class="input-group-addon glyphicon glyphicon-pencil"></span>');
                            }
                        }
                        if (property.readOnly) {
                            $input.attr("disabled", "disabled");
                        }
                    }
                    if (property.manyPrimitive || property.manyEnumeration) {
                        var $manyDiv = $('<label for="' + $input.attr('id') + '" class="input-group-addon"><i class="glyphicon glyphicon-bell"></i></label>');
                        $input.parent().append($manyDiv);

                        $manyDiv.click(
                            function (inputScoped, propertyScoped, manyDivScoped, liScoped) {
                                return function (e) {
                                    self.openEditorForMany(inputScoped, propertyScoped, manyDivScoped, liScoped)
                                    e.preventDefault();
                                    e.stopImmediatePropagation();
                                };

                            }($input, property, $input, formGroupDiv));

                        $input.keypress(
                            function (inputScoped, propertyScoped, manyDivScoped, liScoped) {
                                return function (e) {
                                    if (e.which == 13) {
                                        self.openEditorForMany(inputScoped, propertyScoped, manyDivScoped, liScoped)
                                        e.preventDefault();
                                        e.stopImmediatePropagation();
                                    }
                                };

                            }($input, property, $input, formGroupDiv));

                    } else if (property.composite && property.lower == 1 && property.upper == 1) {
                        $input.click(function (e, args) {
                            alert('TumlBaseTabOneManager.prototype.refresh $input.click not implemented');
                        });
                    } else if (property.composite && property.lower >= 1 && (property.upper > 1 || property.upper == -1)) {
                        $input.click(
                            function (tumlUriScoped, propertyScoped) {
                                return function (e) {
                                    if (self.updatedData[propertyScoped.name] === undefined || self.updatedData[propertyScoped.name] === null) {
                                        self.tumlTabViewManager.openManyComponent([], tumlUriScoped, propertyScoped);
                                    } else {
                                        self.tumlTabViewManager.openManyComponent(self.updatedData[propertyScoped.name], tumlUriScoped, propertyScoped);
                                    }
                                };
                            }(property.tumlUri, property));

                    }
                    if ($input !== undefined) {
                        if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined && !property.readOnly) {
                            if (property.dataTypeEnum == 'Date') {

                                $input.parent().addClass("input-group date");
                                $input.parent().attr('data-date-format', 'YYYY-MM-DD');
                                $input.parent().append('<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>');

                                //this is called 'freezing your closures', help to get the property into the closure
                                (function (p) {
                                    $input.parent().datetimepicker({
                                        pickTime: false
                                    });
                                    $input.parent().on("dp.change",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                    $input.parent().on("dp.show",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                })(property);

                            } else if (property.dataTypeEnum == 'Time') {

                                $input.parent().addClass("input-group date");
                                $input.attr('data-date-format', 'HH:mm');
                                $input.parent().append('<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>');

                                //this is called 'freezing your closures', help to get the property into the closure
                                (function (p) {
                                    $input.parent().datetimepicker({
                                        pickDate: false
                                    });
                                    $input.parent().on("dp.change",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                    $input.parent().on("dp.show",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                })(property);

                            } else if (property.dataTypeEnum == 'DateTime') {

                                $input.parent().addClass("input-group date");
                                $input.parent().attr('data-date-format', 'YYYY-MM-DD HH:mm');
                                $input.parent().append('<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>');

                                //this is called 'freezing your closures', help to get the property into the closure
                                (function (p) {
                                    $input.parent().datetimepicker();
                                    $input.parent().on("dp.change",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                    $input.parent().on("dp.show",function (e) {
                                        self.synchronizeModel(p);
                                    });
                                })(property);

                            }
                        }
                    }
                }
            }

            //Do lookups latest
            for (var i = 0; i < this.metaForData.properties.length; i++) {
                var property = this.metaForData.properties[i];
                if (this.isPropertyForOnePage(property) && !property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && (property.oneToOne || property.manyToOne)) {

                    var formGroupDiv = $('<div>', {class: 'umlg-form-group form-group'});
                    formGroupDiv.appendTo(formDiv);

                    $('<label />', {for: property.name + 'Id', class: "col-lg-3 control-label"}).text(property.name + ' :').appendTo(formGroupDiv);
                    var $input = this.constructInputForField(property);

                    var inputDiv = $('<div />', {class: 'input-group col-lg-4'}).appendTo(formGroupDiv);
//                    if (property.lower > 0) {
//                        inputDiv.append('<span class="input-group-addon glyphicon glyphicon-asterisk"></span>');
//                    }
                    $input.appendTo(inputDiv);
                }
            }

        }
    }

    TumlBaseTabOneManager.prototype.doSave = function () {
        alert("doSave must be overridden");
    };

    TumlBaseTabOneManager.prototype.doCancel = function () {
        this.refresh(this.data, this.metaForData, this.qualifiedName);
    }

    TumlBaseTabOneManager.prototype.isPropertyForOnePage = function (property) {
        return (
            (!property.associationClassOne && !property.inverseComposite && (!property.composite && (property.oneToOne || property.manyToOne || property.manyPrimitive || property.manyEnumeration)))
            ||
            (!property.associationClassOne && (this.isForCreation && property.composite && property.lower > 0) && property.name !== 'uri'));
    }

    /**
     * This method saves the value of the given property on the ui with the underlying model.
     *
     * @param property The property to synchronize the model with
     */
    TumlBaseTabOneManager.prototype.synchronizeModel = function (property) {
        if (property.name === 'id') {
            var id = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
            if (id.indexOf('fake') === -1) {
                this.updatedData.id = id;
            } else {
                this.updatedData.id = id;
                this.updatedData.tmpId = this.updatedData.id;
            }
        } else if (property.readOnly) {
            //Do nothing
        } else if (property.name !== 'uri') {
            if (property.onePrimitive) {
                var stringValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                    if (stringValue == '') {
                        if (this.data[property.name] === null && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = null;
                        }
                    } else {
                        if (this.data[property.name] === parseInt(stringValue) && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = parseInt(stringValue);
                        }
                    }
                } else if (property.fieldType == 'Real') {
                    if (stringValue == '') {
                        if (this.data[property.name] === null && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = null;
                        }
                    } else {
                        if (this.data[property.name] === parseFloat(stringValue) && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = parseFloat(stringValue);
                        }
                    }
                } else if (property.fieldType == 'String') {
                    if (stringValue == '') {
                        if (this.data[property.name] === null && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = null;
                        }
                    } else {
                        if (this.data[property.name] === stringValue && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = stringValue;
                        }
                    }
                } else if (property.fieldType == 'Boolean') {
                    var value = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').is(':checked')
                    if (this.data[property.name] === value && !this.isForCreation) {
                        this.updatedData[property.name] = undefined;
                    } else {
                        this.updatedData[property.name] = value;
                    }
                }
            } else if (property.dataTypeEnum !== null && property.dataTypeEnum !== undefined) {
                if (property.dataTypeEnum == 'DateTime') {
                    var value = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').parent().data('DateTimePicker').getDate().format('YYYY-MM-DD HH:mm:ss');
                    if (this.data[property.name] === value && !this.isForCreation) {
                        this.updatedData[property.name] = undefined;
                    } else {
                        this.updatedData[property.name] = value;
                    }
                } else if (property.dataTypeEnum == 'Date') {
                    var value = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').parent().data('DateTimePicker').getDate().format('YYYY-MM-DD');
                    if (this.data[property.name] === value && !this.isForCreation) {
                        this.updatedData[property.name] = undefined;
                    } else {
                        this.updatedData[property.name] = value;
                    }
                } else if (property.dataTypeEnum == 'Time') {
                    var value = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').parent().data('DateTimePicker').getDate().format('HH:mm:ss');
                    if (this.data[property.name] === value && !this.isForCreation) {
                        this.updatedData[property.name] = undefined;
                    } else {
                        this.updatedData[property.name] = value;
                    }
                } else if (property.dataTypeEnum == 'Image') {
                } else {
                    var value = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                    if (value === '') {
                        if (this.data[property.name] === value && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = null;
                        }
                    } else {
                        if (this.data[property.name] === value && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = value;
                        }
                    }
                }
            } else if (property.oneEnumeration) {
                var $select = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                var options = $select.children();
                for (var j = 0; j < options.length; j++) {
                    if (options[j].selected) {
                        var value = $select.val();
                        if (this.data[property.name] === value && !this.isForCreation) {
                            this.updatedData[property.name] = undefined;
                        } else {
                            this.updatedData[property.name] = value;
                        }
                        break;
                    }
                }
            } else if (property.manyPrimitive) {
                var inputValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                var array = inputValue === '' ? [] : inputValue.split(',');
                if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                    for (var j = 0; j < array.length; j++) {
                        array[j] = parseInt(array[j], 10);
                    }
                } else if (property.fieldType == 'Real') {
                    for (var j = 0; j < array.length; j++) {
                        array[j] = parseFloat(array[j]);
                    }
                } else if (property.fieldType == 'Boolean') {
                    for (var j = 0; j < array.length; j++) {
                        array[j] = array[j] === 'true';
                    }
                }
                if (this.data[property.name] === array && !this.isForCreation) {
                    this.updatedData[property.name] = undefined;
                } else {
                    this.updatedData[property.name] = array;
                }
            } else if (property.manyEnumeration) {
                var inputValue = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id').val();
                var array = inputValue === '' ? [] : inputValue.split(',');
                if (this.data[property.name] === array && !this.isForCreation) {
                    this.updatedData[property.name] = undefined;
                } else {
                    this.updatedData[property.name] = array;
                }
            } else if (property.composite && property.lower > 0) {
                //This is already in the data model. the input is not editable
            } else if (!property.onePrimitive && !property.manyPrimitive && !property.inverseComposite) {
                var $select = $('#' + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
                var options = $select.children();
                for (var j = 0; j < options.length; j++) {
                    if (options[j].selected) {
                        var optionId = $select.val();
                        if (optionId.indexOf('fake') === -1) {
                            if (optionId === '') {
                                //the server side fromJson method does not like empty strings.
                                //it expects something or nothing
                                optionId = null;
                            }
                            var value = {id: optionId, displayName: options[j].label, previousId: this.updatedData[property.name].id};
                            if (this.data[property.name] === value && !this.isForCreation) {
                                this.updatedData[property.name] = undefined;
                            } else {
                                this.updatedData[property.name] = value;
                            }
                        } else {
                            var value = {id: optionId, displayName: options[j].label, previousId: this.updatedData[property.name].id};
                            if (this.data[property.name] === value && !this.isForCreation) {
                                this.updatedData[property.name] = undefined;
                            } else {
                                this.updatedData[property.name] = value;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    TumlBaseTabOneManager.prototype.constructInputForField = function (property, isForManyEditor) {
        var self = this;
        var $input;
        if (property.name == 'id') {
            $input = $('<input />', {disabled: 'disabled', type: 'text', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            if (this.data[property.name] !== undefined && this.data[property.name] !== null) {
                $input[0].defaultValue = this.data[property.name];
            }
        } else if (property.readOnly) {
            if (!this.isForCreation) {
                $input = $('<p />', {class: 'form-control', id: inputFieldId(property, this.metaForData, isForManyEditor)}).text(this.data[property.name]);
            } else {
                $input = $('<p />', {class: 'form-control'});
            }
        } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
            if (property.dataTypeEnum == 'Date' || property.dataTypeEnum == 'Time' || property.dataTypeEnum == 'DateTime' ||
                property.dataTypeEnum == 'InternationalPhoneNumber' || property.dataTypeEnum == 'LocalPhoneNumber' ||
                property.dataTypeEnum == 'Email' || property.dataTypeEnum == 'Host') {
                $input = $("<input />", {type: 'text', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            } else if (property.dataTypeEnum == 'Password') {
                $input = $("<input />", {type: 'password', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            } else if (property.dataTypeEnum == 'UnsecurePassword') {
                $input = $("<input />", {type: 'password', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            } else if (property.dataTypeEnum == 'Video' || property.dataTypeEnum == 'Audio' || property.dataTypeEnum == 'Image') {
                $input = $("<input />", {type: 'text', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            } else if (property.dataTypeEnum == 'QuartzCron') {
                $input = $("<input />", {type: 'text', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
            if (!this.isForCreation) {
                $input.val(this.data[property.name]);
            }
        } else if (property.composite && property.lower > 0) {
            $input = $("<input />", {type: 'text', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, class: 'form-control'});
            if (!this.isForCreation) {
                $input.val(this.data[property.name]);
            }
        } else if (property.oneEnumeration) {
            $input = $('<select />', {class: 'chzn-select form-control', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
            if (!this.isForCreation) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, this.data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (isForManyEditor && property.manyEnumeration) {
            $input = $('<select />', {id: inputFieldId(property, this.metaForData, isForManyEditor), class: 'chzn-select form-control', name: property.name});
            if (!this.isForCreation) {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, this.data[property.name], $input);
            } else {
                this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, $input);
            }
        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.oneToOne) {
            $input = $('<select />', {class: 'chzn-select form-control', style: 'width:350px;', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
            //On creation the new object must first be created server side before the handleLookup method can be invoked.
            //This is because the lookup needs the current tempory object to exist.
            if (!this.isForCreation) {
                this.appendLoopupOptionsToSelect2(property, this.data['id'], $input, this.data[property.name]);
            }

        } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.manyToOne) {
            $input = $('<select />', {class: 'chzn-select form-control', style: 'width:350px;', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
            if (!this.isForCreation) {
                this.appendLoopupOptionsToSelect2(property, this.data['id'], $input, this.data[property.name]);
            }
        } else if (property.fieldType == 'String' || property.fieldType == 'Integer' || property.fieldType == 'Long' || property.fieldType == 'Real' || property.manyEnumeration) {
            $input = $('<input />', {type: 'text', class: 'form-control', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
            if (!this.isForCreation) {
//                $input[0].defaultValue = (this.data[property.name] === null ? '' : this.data[property.name]);
                $input.val(this.data[property.name] === null ? '' : this.data[property.name]);
            }
        } else if (property.fieldType == 'Boolean') {
            if (!property.manyPrimitive && this.data !== undefined && this.data !== null) {
                if (!this.isForCreation) {
                    $input = $('<input />', {type: 'checkbox', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, checked: (this.data[property.name] ? 'checked' : false)});
                } else {
                    $input = $('<input />', {type: 'checkbox', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name, checked: false});
                }
            } else if (!property.manyPrimitive) {
                $input = $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
            } else {
                $input = $('<input />', {type: 'text', class: 'form-control', id: inputFieldId(property, this.metaForData, isForManyEditor), name: property.name});
                if (!this.isForCreation) {
//                    $input[0].defaultValue = this.data[property.name];
                    $input.val(this.data[property.name]);
                }
            }
        }
        if (!property.composite) {
            $input.focus(
                function () {
                    self.currentActiveProperty = property;
                }
            );
            if ($input.is('select') && !isForManyEditor) {
                $input.change(function () {
                    var validationResult = self.validateField(property, $input, isForManyEditor);
                    if (validationResult.valid) {
                        self.synchronizeModel(property)
                    } else {
                        setTimeout(
                            function () {
                                $input.focus();
                            }, 5
                        );
                    }
                    return false;
                });
            } else if ((property.fieldType === 'Boolean') && !isForManyEditor && property.upper === 1) {
                $input.change(function () {
                    var validationResult = self.validateField(property, $input, isForManyEditor);
                    if (validationResult.valid) {
                        self.synchronizeModel(property)
                    } else {
                        setTimeout(
                            function () {
                                $input.focus();
                            }, 5
                        );
                    }
                    return false;
                });
            } else if (!isForManyEditor) {
                $input.blur(function () {
                    var validationResult = self.validateField(property, $input, isForManyEditor);
                    if (validationResult.valid) {
                        self.synchronizeModel(property)
                    } else {
                        setTimeout(
                            function () {
                                $input.focus();
                            }, 5
                        );
                    }
                    return false;
                });
            }
            $input.keyup(function (e) {
                if (e.keyCode == 27) {
                    self.refreshFromDataModelForProperty(property);
                    self.validateField(property, $input, isForManyEditor);
                    return false;
                }
            });
        }
        return $input;
    }

    function inputFieldId(property, metaData, isForManyEditor) {
        var result = property.name + metaData.qualifiedName + 'Id';
        if (isForManyEditor) {
            result += '::manyEditor';
        }
        return result;
    }

    function inputFieldIdForValidate(property, metaData, isForManyEditor) {
        var result = '#' + property.name + escapeColon(metaData.qualifiedName) + 'Id';
        if (isForManyEditor) {
            result += '::manyEditor';
        }
        return result;
    }

    /**
     * This is called after saving a one and when creating a new component one. Called from updateGridAfterCommitOrRollback
     * @param dataMode
     */
    TumlBaseTabOneManager.prototype.setDataModel = function (dataModel) {
        if (dataModel.id.indexOf('fake') !== -1) {
            //new component one
            this.data = dataModel;
            //For new components the whole object will be updated, i.e. created really
            this.updatedData = JSON.parse(JSON.stringify(this.data));
        } else {
            this.data = dataModel;
        }
    }

    TumlBaseTabOneManager.prototype.refreshFromDataModelForProperty = function (property) {
        var self = this;
        if (this.isPropertyForOnePage(property)) {
            if (property.name == 'id') {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    input.val(this.data[property.name]);
                } else {
                    if (this.data[property.name] !== undefined) {
                        input.val(this.data[property.name]);
                    }
                }
            } else if (property.readOnly) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.data[property.name] !== undefined) {
                    input.text(this.data[property.name]);
                }
            } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    input.val(this.updatedData[property.name]);
                } else {
                    if (this.data[property.name] !== undefined) {
                        input.val(this.data[property.name]);
                    }
                }
            } else if (property.composite && property.lower > 0) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    input.val(this.updatedData[property.name]);
                } else {
                    if (this.data[property.name] !== undefined) {
                        input.val(this.data[property.name]);
                    }
                }
            } else if (property.oneEnumeration) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, this.updatedData[property.name], input);
                } else {
                    if (this.data[property.name] !== undefined) {
                        this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, this.data[property.name], input);
                    } else {
                        this.appendEnumerationLoopupOptionsToSelect("/" + tumlModelName + "/tumlEnumLookup", property.qualifiedName, property.lower > 0, null, input);
                    }
                }
            } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.oneToOne) {

                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    this.appendLoopupOptionsToSelect2(property, this.updatedData['id'], input, this.updatedData[property.name]);
                } else {
                    this.appendLoopupOptionsToSelect2(property, this.data['id'], input, this.data[property.name]);
                }

            } else if (!property.onePrimitive && property.dataTypeEnum == undefined && !property.manyPrimitive && !property.composite && property.manyToOne) {
                if (this.updatedData[property.name] !== undefined) {
                    this.appendLoopupOptionsToSelect2(property.tumlLookupUri, property.lower > 0, this.data['id'], this.updatedData[property.name], input);
                } else {
                    if (this.data[property.name] !== undefined) {
                        this.appendLoopupOptionsToSelect2(property.tumlLookupUri, property.lower > 0, this.data['id'], this.data[property.name], input);
                    } else {
                        //TODO when does this fire
                        this.appendLoopupOptionsToSelect2(property.tumlLookupOnCompositeParentUri, property.lower > 0, this.data['id'], this.data[property.name], input);
                    }
                }
            } else if (property.fieldType == 'String' || property.fieldType == 'Integer' || property.fieldType == 'Long' || property.fieldType == 'Real' || property.manyEnumeration) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                if (this.updatedData[property.name] !== undefined) {
                    input.val(this.updatedData[property.name]);
                } else if (this.data[property.name] !== undefined) {
                    input.val(this.data[property.name]);
                }
            } else if (property.fieldType == 'Boolean') {
                if (!property.manyPrimitive) {
                    var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                    if (this.updatedData[property.name]) {
                        input.attr('checked', 'checked');
                    } else if (this.data[property.name]){
                        input.attr('checked', true);
                    } else {
                        input.attr('checked', false);
                    }
                } else if (property.manyPrimitive) {
                    var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                    if (this.updatedData[property.name] !== undefined) {
                        input.val(this.updatedData[property.name]);
                    } else if (this.data[property.name] !== undefined) {
                        input.val(this.data[property.name]);
                    }
                }
            }
            if (!(property.composite && property.lower > 0)) {
                var input = $(inputFieldIdForValidate(property, this.metaForData, false));
                input.blur(function () {
                    self.validateField(property, input, false);
                });
            }
        }
    }

    TumlBaseTabOneManager.prototype.refreshFromDataModel = function () {
        if (!this.isForCreation) {
            this.updatedData = {};
            this.updatedData.qualifiedName = this.qualifiedName;
        }
        for (var i = 0; i < this.metaForData.properties.length; i++) {
            var property = this.metaForData.properties[i];
            this.refreshFromDataModelForProperty(property);
        }
        var input = $("#" + property.name + escapeColon(this.metaForData.qualifiedName) + 'Id');
        if (this.isForCreation) {
            input.addClass("umlg-new-id");
        } else {
            input.addClass("umlg-updated-id");
        }
        return input;
    }

    TumlBaseTabOneManager.prototype.updateDataModelForProperty = function (property, value) {
        this.updatedData[property.name] = value;
        this.refreshFromDataModelForProperty(property);
    }

    TumlBaseTabOneManager.prototype.openEditorForMany = function ($inputToSet, property, $manyDiv, $li) {
        var $div = $("<div class='panel panel-default many-primitive-editor-for-one' />");
        $div.keyup(function (e) {
            if (e.which == 27) {
                $div.click();
            }
        });
        $(document).mouseup(function (e) {
            var container = $(".many-primitive-editor-for-one");
            if (container.has(e.target).length === 0) {
                container.remove();
            }
        });
        $div.keydown(function (event) {
            if (event.which == 27) {
                event.preventDefault();
                event.stopImmediatePropagation();
                $div.remove();
            }
        });

        var panelHeading = $('<div class="panel-heading umlg-panel-heading-popup" />').appendTo($div);
        var topNavBar = $('<div class="collapse navbar-collapse" />').appendTo(panelHeading);
        var panelBody = $('<div class="panel-body umlg-panel-body-popup" />').appendTo($div);
        var panelFooter = $('<div class="panel-footer umlg-panel-footer-popup" />').appendTo($div);

        var $table;
        var $input;
        var editor = selectEditor(property);
        var serializer = new editor().serializeValueForOne;
        var button = $('<button type="button" id="many-primitive-editor-input-add-button" class="btn btn-default pull-left" />').text('Add').click(function () {
            var input = panelHeading.find('.form-control');
            var valueToAdd = input.val();
            var currentValues = serializer($table);
            var testArray = [];
            if (valueToAdd !== '') {
                testArray.push(valueToAdd);
                var validator = selectFieldValidator(property);
                var validationResults = validator(testArray);
                if (currentValues.length !== 0 && validationResults.valid && property.unique) {
                    validationResults = validator(currentValues, valueToAdd);
                }
                if (!validationResults.valid) {
                    alert(validationResults.msg);
                } else {
                    addTr(valueToAdd);
                    //Need to reapply the drag and drop plugin
                    $table.tableDnD();
                    if (!property.manyEnumeration) {
                        $input.val('');
                    }
                }
            }
        }).appendTo(topNavBar);
        $input = this.constructInputForField(property, true);
        $input.val('');
        $input.addClass("form-control");
        var formGroup = $('<div class="form-group pull-left" />')
        $input.appendTo(formGroup);
        formGroup.appendTo(topNavBar);
        $input.keypress(function (e) {
            if (e.which == 13) {
                $('#many-primitive-editor-input-add-button').click();
            }
        });
        var resultDiv = $('<div class="many-primitive-editor-result" />').appendTo(panelBody);
        $table = $('<table class="table table-bordered" />').appendTo(resultDiv);
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
        panelFooter.append($('<button type="button" class="btn btn-default many-primitive-editor-select"/>').click(function () {
            var currentValues = serializer($table);
            $inputToSet.val(currentValues.join(","));
            $inputToSet.blur();
            $div.remove();
            $inputToSet.focus();
        }).text('Select'));
        panelFooter.append($('<button type="button" class="btn btn-default many-primitive-editor-cancel"/>').click(function () {
            $div.remove();
            $inputToSet.focus();
        }).text('Cancel'));
        $div.appendTo($('body'));
        $input.focus();

        function addTr(value) {
            var row = $('<tr />').addClass('many-primitive-editor-row');
            var rowValue = $('<td class="many-primitive-editor-cell col-xs-6" />').text(value);
            row.append(rowValue);
            row.data('value', value);
            var img = $('<img/>', {class: 'umlg-many-select-img', src: '/' + tumlModelName + '/javascript/images/delete.png'}).click(function () {
                var liClicked = $(this).parent().parent();
                liClicked.remove();
            });
            var imgValue = $('<td class="many-primitive-editor-cell-img col-xs-2" />');
            imgValue.append(img);
            row.append(imgValue);
            $table.append(row);
        }

        var topBox = document.getElementById('umlgnavbar').getBoundingClientRect();
        var bottomBox = document.getElementById('footer').getBoundingClientRect();
        var box = document.getElementById($manyDiv[0].id).getBoundingClientRect();
        if ((bottomBox.bottom - box.bottom - topBox.height) < $div.height()) {
            $div.css('left', box.left);
            $div.css('top', box.top - $div.height() + 10);
        } else {
            $div.css('left', box.left);
            $div.css('top', box.top + box.height);
        }

    }

    TumlBaseTabOneManager.prototype.appendEnumerationLoopupOptionsToSelect = function (tumlLookupUri, propertyJavaClassName, required, currentValue, select) {
        $.getJSON(tumlLookupUri + '?enumQualifiedName=' + propertyJavaClassName,
            function (response, b, c) {
                //if not a required field add a black value
                //Clear the select box
                select.html('');
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

    TumlBaseTabOneManager.prototype.appendLoopupOptionsToSelect2 = function (property, contextVertexId, $select, currentValue) {

        var adjustedUri = property.tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(contextVertexId));
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

    TumlBaseTabOneManager.prototype.commitCurrentEdit = function () {
        this.synchronizeModel(this.currentActiveProperty);
        //Check if it is a component
        if (!(this.tumlTabViewManager.parentTabContainerManager instanceof Tuml.TumlMainViewManager)) {
            //Only validate the field it it is non empty
            var validateInput = $(inputFieldIdForValidate(this.currentActiveProperty, this.metaForData, false));
            if (validateInput.val() != "") {
                var validationResult = this.validateField(this.currentActiveProperty, validateInput, false);
                return validationResult.valid;
            } else {
                return true;
            }
        } else {
            if (this.currentActiveProperty !== undefined && !this.currentActiveProperty.readOnly) {
                var validationResult = this.validateField(this.currentActiveProperty, null, false);
                return validationResult.valid;
            } else {
                return true;
            }
        }
    }

    TumlBaseTabOneManager.prototype.validateField = function (property, input, isForManyEditor) {
        this.removeServerErrorMessage();
        var validateInput;
        if (input === undefined || input === null) {
            validateInput = $(inputFieldIdForValidate(property, this.metaForData, isForManyEditor));
        } else {
            validateInput = input;
        }

        var validationResult = null;
        if (property.manyPrimitive || property.manyEnumeration) {
            var stringValueArray = [];
            var value = validateInput.val();
            if (value !== '') {
                stringValueArray = value.split(',');
            }
            var tmpSerializedValue = [];
            for (var i = 0; i < stringValueArray.length; i++) {
                var tmpValue = stringValueArray[i];
                if (property.fieldType === 'Integer' || property.fieldType === 'Long') {
                    if (isNaN(tmpValue)) {
                        validationResult = {valid: false, msg: 'Please enter a valid number.'};
                    } else {
                        if (property.unique && tmpSerializedValue.indexOf(parseInt(tmpValue)) !== -1) {
                            validationResult = {valid: false, msg: 'Duplicates are not allowed.'};
                        } else {
                            tmpSerializedValue.push(parseInt(tmpValue));
                        }
                    }
                } else if (property.fieldType === 'Real') {
                    if (isNaN(tmpValue)) {
                        validationResult = {valid: false, msg: 'Please enter a valid number.'};
                    } else {
                        if (property.unique && tmpSerializedValue.indexOf(parseFloat(tmpValue)) !== -1) {
                            validationResult = {valid: false, msg: 'Duplicates are not allowed.'};
                        } else {
                            tmpSerializedValue.push(parseFloat(tmpValue));
                        }
                    }
                } else if (property.fieldType === 'Boolean') {
                    if (tmpValue != 'true' && tmpValue != 'false') {
                        validationResult = {valid: false, msg: 'Value must be "true" or "false".'};
                    } else {
                        tmpSerializedValue.push(tmpValue);
                    }
                } else {
                    if (property.unique && tmpSerializedValue.indexOf(tmpValue) !== -1) {
                        validationResult = {valid: false, msg: 'Duplicates are not allowed.'};
                    } else {
                        tmpSerializedValue.push(stringValueArray[i]);
                    }
                }
            }
            if (validationResult === undefined || validationResult === null) {
                validationResult = selectFieldValidator(property)(tmpSerializedValue);
            }
        } else {
            if (property.fieldType === 'Boolean') {
                validationResult = selectFieldValidator(property)((validateInput.is(':checked') ? 't' : 'f'));
            } else {
                if (property.dataTypeEnum === 'Date' || property.dataTypeEnum === 'DateTime' || property.dataTypeEnum === 'Time') {
                    validationResult = selectFieldValidator(property)(validateInput.parent().data("DateTimePicker").getDate());
                } else {
                    validationResult = selectFieldValidator(property)(validateInput.val());
                }
            }
        }
        //selectFieldValidator returns the validate function
        if (!validationResult.valid) {
            validateInput.parents('.form-group').removeClass('has-error');
            validateInput.parents('.form-group').addClass('has-error');
            validateInput.parent('div').siblings('p').remove();
            validateInput.parent('div').after($('<p class="help-block" />').text(validationResult.msg));
        } else {
            validateInput.parents('.form-group').removeClass('has-error');
            validateInput.parent('div').siblings('p').remove();
        }

        return validationResult;
    }
    TumlBaseTabOneManager.prototype.removeServerErrorMessage = function () {
        $('#serverErrorMsg').removeClass('server-error-msg').empty();
    }


})(jQuery);
