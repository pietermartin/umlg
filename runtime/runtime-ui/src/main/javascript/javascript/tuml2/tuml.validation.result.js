(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            ValidationResult: ValidationResult
        }
    });

    function ValidationResult(row, property) {
        this.row = row;
        this.property  = property;
        this.qualifiedName = null;
        this.validationResults = [];
        this.message = null;
        this.count = null;

        this.internalToString = function() {
            var result = "Validation errors for:\n    row = " + this.row + "\n    property = " + this.property.name + "\n    qualified name = " + this.qualifiedName;
            result += "\n    message = " + this.message + "\n\n";
            for (var i = 0; i < this.validationResults.length; i++) {
                var validationResult = this.validationResults[i];
                result += validationResult.internalToString();
            }
            return result;
        }

        this.toString = function() {
            return this.internalToString();
        }
    }

})
    (jQuery);
