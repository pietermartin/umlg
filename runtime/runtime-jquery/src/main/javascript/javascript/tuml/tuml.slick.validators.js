/***
 * Contains basic TumlSlickGrid validators.
 * @module Validators
 * @namespace TumlSlick
 */

(function($) {
    // register namespace
    $.extend(true, window, {
        "TumlSlick" : {
            "Validators" : {
                "TumlString" : TumlStringValidator,
                "RangeLength" : RangeLengthValidator,
                "MaxLength" : MaxLengthValidator,
                "MinLength" : MinLengthValidator,
                "Url" : UrlValidator,
                "TumlNumber" : TumlNumberValidator,
                "Range" : RangeValidator,
                "Max" : MaxValidator,
                "Min" : MinValidator,
                "Required" : RequiredValidator,
                "Number" : NumberValidator
            }
        }
    });

    function TumlStringValidator(property) {
        this.validate = function(value) {
            result = TumlSlick.Validators.Required(property, value);
            if (!result.valid){
                return result;
            }
            if (property.validations !== null) {
                if (property.validations.rangeLength !== undefined) {
                    result = TumlSlick.Validators.RangeLength(property, value);
                }
                if (!result.valid){
                    return result;
                }
                if (property.validations.maxLength !== undefined) {
                    result = TumlSlick.Validators.MaxLength(property, value);
                }
                if (!result.valid){
                    return result;
                }
                if (property.validations.minLength !== undefined) {
                    result = TumlSlick.Validators.MinLength(property, value);
                }
                if (!result.valid){
                    return result;
                }
                if (property.validations.url !== undefined) {
                    result = TumlSlick.Validators.Url(property, value);
                }
                if (!result.valid){
                    return result;
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };
    function TumlNumberValidator(property) {
        this.validate = function(value) {
            result = TumlSlick.Validators.Required(property, value);
            if (!result.valid){
                return result;
            }
            result = TumlSlick.Validators.Number(property, value);
            if (!result.valid){
                return result;
            }
            if (property.validations !== null) {
                if (property.validations.range !== undefined) {
                    result = TumlSlick.Validators.Range(property, value);
                }
                if (!result.valid){
                    return result;
                }
                if (property.validations.max !== undefined) {
                    result = TumlSlick.Validators.Max(property, value);
                }
                if (!result.valid){
                    return result;
                }
                if (property.validations.min !== undefined) {
                    result = TumlSlick.Validators.Min(property, value);
                }
                if (!result.valid){
                    return result;
                }
            }
            return {
                valid: true,
                msg: null
            };
        }
    };

    function NumberValidator(property, value) {
        if (isNaN(parseInt(value, 10))) {
            return {
                valid: false,
                msg: "Please enter a valid positive number"
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
        var intValue =  parseInt(value, 10);
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
        var intValue =  parseInt(value, 10);
        if (intValue > property.validations.max) {
            return {
                valid: false,
                msg: "Value needs to be smaller than or equal to " + property.validations.max
            }
        } else {
            return {valid: true};
        }
    }

    function MinValidator(property, value) {
        var intValue =  parseInt(value, 10);
        if (intValue < property.validations.min) {
            return {
                valid: false,
                msg: "Value needs to be greater than or equal to " + property.validations.min
            }
        } else {
            return {valid: true};
        }
    }

    function UrlValidator(property, value) {
        return {valid: true};
    }

    function RequiredValidator(property, value) {
        if (property.lower > 0 && (value == '' || value == undefined || value == null)) {
            return {
                valid: false,
                msg: property.name + " is a required field!"
            };
        } else {
            return {valid: true};
        }
    }
})(jQuery);
