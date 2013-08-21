/***
 * Contains basic TumlSlickGrid validators.
 * @module Validators
 * @namespace TumlSlick
 */

(function ($) {
    // register namespace
    $.extend(true, window, {
        "TumlSlick": {
            "Validators": {
                "TumlObject": TumlObjectValidator,
                "TumlString": TumlStringValidator,
                "TumlDateTime": TumlDateTimeValidator,
                "RangeLength": RangeLengthValidator,
                "MaxLength": MaxLengthValidator,
                "MinLength": MinLengthValidator,
                "Url": UrlValidator,
                "Email": EmailValidator,
                "DateTime": DateTimeValidator,
                "Date": DateValidator,
                "Time": TimeValidator,
                "TumlManyEnumerationValidator": TumlManyEnumerationValidator,
                "TumlNumber": TumlNumberValidator,
                "TumlBoolean": TumlBooleanValidator,
                "TumlManyNumber": TumlManyNumberValidator,
                "TumlManyString": TumlManyStringValidator,
                "TumlManyBoolean": TumlManyBooleanValidator,
                "Range": RangeValidator,
                "Max": MaxValidator,
                "Min": MinValidator,
                "Required": RequiredValidator,
                "Number": NumberValidator
            }
        }
    });

    function TumlObjectValidator(property) {
        //Public api
        $.extend(this, {
            "TumlObjectValidator": "1.0.0",
            "validate": validate
        });

        function validate(value) {
            var result = TumlSlick.Validators.Required(property, value);
            if (!result.valid) {
                return result;
            }
            return {
                valid: true,
                msg: null
            };
        }
    };

    function TumlDateTimeValidator(property) {
        //Public api
        $.extend(this, {
            "TumlDateTimeValidator": "1.0.0",
            "validate": validate
        });

        function validate(value) {
            var result = TumlSlick.Validators.Required(property, value);
            if (!result.valid) {
                return result;
            }
            if (value !== undefined && value !== null && value !== '') {
                if (property.validations !== null) {
                    if (property.validations.date !== undefined) {
                        result = TumlSlick.Validators.Date(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.time !== undefined) {
                        result = TumlSlick.Validators.Time(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.dateTime !== undefined) {
                        result = TumlSlick.Validators.DateTime(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };
    function TumlStringValidator(property) {
        //Public api
        $.extend(this, {
            "TumlStringValidator": "1.0.0",
            "validate": validate
        });

        function validate(value) {
            var result = TumlSlick.Validators.Required(property, value);
            if (!result.valid) {
                return result;
            }
            if (value !== undefined && value !== null && value !== '') {
                if (property.validations !== null) {
                    if (property.validations.rangeLength !== undefined) {
                        result = TumlSlick.Validators.RangeLength(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.maxLength !== undefined) {
                        result = TumlSlick.Validators.MaxLength(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.minLength !== undefined) {
                        result = TumlSlick.Validators.MinLength(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.url !== undefined) {
                        result = TumlSlick.Validators.Url(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.email !== undefined) {
                        result = TumlSlick.Validators.Email(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };

    function TumlManyStringValidator(property) {

        //Public api
        $.extend(this, {
            "TumlManyStringValidator": "1.0.0",
            "validate": validate
        });

        function validateSingleProperty(tmp) {
            return new TumlSlick.Validators.TumlString(property).validate(tmp);
        }

        function validate(currentValues, valueToAdd) {
            return manyValidate(property, validateSingleProperty, currentValues, valueToAdd)
        }

    };

    function TumlManyEnumerationValidator(property) {

        //Public api
        $.extend(this, {
            "TumlManyEnumerationValidator": "1.0.0",
            "validate": validate
        });

        function validateSingleProperty(tmp) {
            return new TumlSlick.Validators.TumlString(property).validate(tmp);
        }

        function validate(currentValues, valueToAdd) {
            return manyValidate(property, validateSingleProperty, currentValues, valueToAdd)
        }

    };

    function TumlManyBooleanValidator(property) {

        //Public api
        $.extend(this, {
            "TumlManyBooleanValidator": "1.0.0",
            "validate": validate
        });

        function validateSingleProperty(tmp) {
            return new TumlSlick.Validators.TumlBoolean(property).validate(tmp);
        }

        function validate(currentValues, valueToAdd) {
            return manyValidate(property, validateSingleProperty, currentValues, valueToAdd)
        }

    };

    function TumlManyNumberValidator(property) {

        //Public api
        $.extend(this, {
            "TumlManyNumberValidator": "1.0.0",
            "validate": validate
        });

        function validateSingleProperty(tmp) {
            return new TumlSlick.Validators.TumlNumber(property).validate(tmp);
        }

        function validate(currentValues, valueToAdd) {
            return manyValidate(property, validateSingleProperty, currentValues, valueToAdd)
        }

    };

    function manyValidate(property, validateSingleProperty, currentValues, valueToAdd) {
        //Validate that the property is not already present for a unique set.
        //The value itself has already been validated to be correct.
        if (currentValues instanceof Array && valueToAdd !== undefined && property.unique) {
            for (var i = 0; i < currentValues.length; i++) {
                var tmp = currentValues[i];
                if (tmp == valueToAdd) {
                    return {
                        valid: false,
                        msg: 'The list must be unique, ' + valueToAdd + ' is already present.'
                    };
                }
            }
            return {
                valid: true,
                msg: null
            };
        } else {
            //Validate the current values, this gets called for new items to post
            var result = {
                valid: false,
                msg: null
            };
            //Nothing to validate but the property is required
            if (property.lower > 0 && (currentValues == undefined || currentValues == null || currentValues.length == 0)) {
                return {
                    valid: false,
                    msg: property.name + " is a required field!"
                };
            }
            //Nothing to validate and the property is not required
            if (property.lower == 0 && (currentValues == undefined || currentValues == null || currentValues.length == 0)) {
                return {
                    valid: true,
                    msg: null
                };
            }
            //Validate each property
            for (var i = 0; i < currentValues.length; i++) {
                var tmp = currentValues[i];
                result = validateSingleProperty(tmp);
                if (!result.valid) {
                    return result;
                }
            }
            return result;
        }
        return {
            valid: false,
            msg: null
        };
    }


    function TumlBooleanValidator(property) {
        //Public api
        $.extend(this, {
            "TumlBooleanValidator": "1.0.0",
            "validate": validate
        });

        function validate(value) {
            var result = TumlSlick.Validators.Required(property, value);
            if (!result.valid) {
                return result;
            }
            if (value !== undefined && value !== null && value !== '') {
                if (!(value === 'true' || value === 't' || value === '1' || value === 'false' || value === 'f' || value === '0')) {
                    return {valid: false, msg: 'Value must be "true" or "t" or "1" or "false" or "f" or "0"'};
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };

    function TumlNumberValidator(property) {
        //Public api
        $.extend(this, {
            "TumlNumberValidator": "1.0.0",
            "validate": validate
        });
        function validate(value) {
            var result = TumlSlick.Validators.Required(property, value);
            if (!result.valid) {
                return result;
            }
            if (value !== undefined && value !== null && value !== '') {
                result = TumlSlick.Validators.Number(property, value);
                if (!result.valid) {
                    return result;
                }
                if (property.validations !== null) {
                    if (property.validations.range !== undefined) {
                        result = TumlSlick.Validators.Range(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.max !== undefined) {
                        result = TumlSlick.Validators.Max(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                    if (property.validations.min !== undefined) {
                        result = TumlSlick.Validators.Min(property, value);
                    }
                    if (!result.valid) {
                        return result;
                    }
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };

    function NumberValidator(property, value) {
        if (value === undefined || value === null || value === '' || isNaN(value)) {
            return {
                valid: false,
                msg: "Please enter a number!"
            };
        } else {
            return {valid: true};
        }
    }

    function RangeLengthValidator(property, value) {
        if (value !== undefined && (value.length < property.validations.rangeLength.min || value.length > property.validations.rangeLength.max)) {
            return {
                valid: false,
                msg: "Value's length needs to be between " + property.validations.rangeLength.min + " and " + property.validations.rangeLength.max
            }
        } else {
            return {valid: true};
        }
    }

    function MaxLengthValidator(property, value) {
        if (value !== undefined && value.length > property.validations.maxLength) {
            return {
                valid: false,
                msg: "Value's length needs to be smaller than or equal to " + property.validations.maxLength
            }
        } else {
            return {valid: true};
        }
    }

    function MinLengthValidator(property, value) {
        if (value !== undefined && value.length < property.validations.minLength) {
            return {
                valid: false,
                msg: "Value's length needs to be greater than or equal to " + property.validations.minLength
            }
        } else {
            return {valid: true};
        }
    }

    function RangeValidator(property, value) {
        var intValue = parseInt(value, 10);
        if (intValue < property.validations.range.min || intValue > property.validations.range.max) {
            return {
                valid: false,
                msg: "Value need to be between " + property.validations.range.min + " and " + property.validations.range.max + " (including)"
            }
        } else {
            return {valid: true};
        }
    }

    function MaxValidator(property, value) {
        if (isNaN(value)) {
            return {
                valid: false,
                msg: "Value must be anumber"
            }
        } else {
            var intValue = parseInt(value, 10);
            if (intValue > property.validations.max) {
                return {
                    valid: false,
                    msg: "Value needs to be smaller than or equal to " + property.validations.max
                }
            } else {
                return {valid: true};
            }
        }
    }

    function MinValidator(property, value) {
        if (isNaN(value)) {
            return {
                valid: false,
                msg: "Value must be a number"
            }
        } else {
            var intValue = parseInt(value, 10);
            if (intValue < property.validations.min) {
                return {
                    valid: false,
                    msg: "Value needs to be greater than or equal to " + property.validations.min
                }
            } else {
                return {valid: true};
            }
        }
    }

    function UrlValidator(property, value) {
        return {valid: true};
    }

    function EmailValidator(property, value) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (!re.test(value)) {
            return {
                valid: false,
                msg: "Value is not a valid email address!"
            }
        } else {
            return {valid: true};
        }
    }

    function DateTimeValidator(property, value) {
        try {
            var result = $.datepicker.parseDateTime('yy-mm-dd', 'HH:mm:ss', value);
            return {valid: true};
        } catch (error) {
            return {valid: false, msg: value + "'s format is incorrect, the format is 'yy-mm-dd HH:mm:ss'"}
        }
    }


    function DateValidator(property, value) {
        //datetimepicker thinks there is a time part to the value
        value = value + ' 00:00:00';
        try {
            var result = $.datepicker.parseDate('yy-mm-dd', value);
            return {valid: true};
        } catch (error) {
            return {valid: false, msg: value + "'s format is incorrect, the format is 'yy-mm-dd'"}
        }
    }

    function TimeValidator(property, value) {
        var result = $.datepicker.parseTime('HH:mm', value, {});
        if (result) {
            return {valid: true};
        } else {
            return {valid: false, msg: value + "'s format is incorrect, the format is 'HH:mm'"}
        }
    }

    function RequiredValidator(property, value) {
        if (property.lower > 0 && value !== false && value !== true && (value === '' || value === undefined || value === null)) {
            return {
                valid: false,
                msg: property.name + " is a required field!",
                present: false
            };
        } else {
            return {valid: true, present: true};
        }
    }
})(jQuery);
