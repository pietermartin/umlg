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
    }

})
    (jQuery);
