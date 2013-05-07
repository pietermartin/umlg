function escapeColon(string) {
    return string.replace(/(:|\.)/g,'\\$1');
}

function retrieveVertexId(url) {
    var urlId = url.match(/\/\d+/);
    if (urlId != null) {
        var urlArray = urlId[0].match(/\d+/);
        return parseInt(urlArray[0]);
    } else {
        return urlId;
    }
}

function selectFormatter(property, newId, updatedId) {
    if (property.name == 'uri') {
        return new TumlSlick.Formatters.Link;
    } else if (property.name == 'id') {
        if (newId) {
            return TumlSlick.Formatters.TumlIdNewFormatter;
        } else if (updatedId) {
            return TumlSlick.Formatters.TumlIdUpdatedFormatter;
        } else {
            return TumlSlick.Formatters.TumlRegularFormatter;
        }
    } else if (property.dataTypeEnum !=null && property.dataTypeEnum !== undefined) {
        return null;
    } else if (property.lower > 0 && (property.oneEnumeration || property.manyEnumeration)) {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.composite && property.lower > 0) {
        return  null;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.manyEnumeration) {
        return function waitingFormatter(row, cell, value, columnDef, dataContext) {
            if (value !== undefined && value !== null) {
                return value.displayName;
            } else {
                return '';
            }
        };
        return null;
    } else if (property.lower > 0 && property.fieldType == 'Long') {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.lower > 0 && property.fieldType == 'Integer') {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.lower > 0 && property.fieldType == 'String') {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.manyPrimitive && property.fieldType == 'Boolean') {
        return  TumlSlick.Formatters.TumlManyBoolean;
    } else if (property.fieldType == 'Boolean') {
        return TumlSlick.Formatters.TumlBoolean;
    } else {
        return TumlSlick.Formatters.TumlRegularFormatter;
    }
}

function selectFieldValidator(property) {
    if (property.name == 'uri') {
    } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
        if (property.dataTypeEnum == 'Date') {
            return new TumlSlick.Validators.TumlDateTime(property).validate;
        } else if (property.dataTypeEnum == 'Time') {
            return new TumlSlick.Validators.TumlDateTime(property).validate;
        } else if (property.dataTypeEnum == 'DateTime') {
            return new TumlSlick.Validators.TumlDateTime(property).validate;
        } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
        } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
        } else if (property.dataTypeEnum == 'Email') {
            return new TumlSlick.Validators.TumlString(property).validate;
        } else if (property.dataTypeEnum == 'Video') {
        } else if (property.dataTypeEnum == 'Audio') {
        } else if (property.dataTypeEnum == 'Image') {
        } else {
            alert('Unsupported dataType ' + property.dataTypeEnum);
        }
    } else if (property.composite) {
        return new TumlSlick.Validators.TumlObject(property).validate;
    } else if (property.manyEnumeration) {
        return new TumlSlick.Validators.TumlManyEnumerationValidator(property).validate;
    } else if (property.oneEnumeration) {
        return new TumlSlick.Validators.TumlString(property).validate;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
    } else if (property.name == 'id') {
    } else if (!property.manyPrimitive && property.fieldType == 'String') {
        return new TumlSlick.Validators.TumlString(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'String') {
        return new TumlSlick.Validators.TumlManyString(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Integer') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Integer') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Long') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Long') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Boolean') {
        return new TumlSlick.Validators.TumlBoolean(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Boolean') {
        return new TumlSlick.Validators.TumlManyBoolean(property).validate;
    } else {
        alert('validator not found for property ' + property.name);
    }
    return function () {
        return {
            valid:true,
            msg:null}
    };
}

function selectEditor(property) {
    if (property.name == 'uri') {
        return null;
    } else if (property.readOnly) {
        return null;
    } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
        if (property.dataTypeEnum == 'Date') {
            return  Tuml.Slick.Editors.Date;
        } else if (property.dataTypeEnum == 'Time') {
            return  Tuml.Slick.Editors.Time;
        } else if (property.dataTypeEnum == 'DateTime') {
            return  Tuml.Slick.Editors.DateTime;
        } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
            //TODO
            return Tuml.Slick.Editors.Text;
        } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
            //TODO
            return Tuml.Slick.Editors.Text;
        } else if (property.dataTypeEnum == 'Email') {
            return Tuml.Slick.Editors.Text;
        } else if (property.dataTypeEnum == 'Video') {
            return Tuml.Slick.Editors.Text;
        } else if (property.dataTypeEnum == 'Audio') {
            return Tuml.Slick.Editors.Text;
        } else if (property.dataTypeEnum == 'Image') {
            return Tuml.Slick.Editors.Text;
        } else {
            alert('Unsupported dataType ' + property.dataTypeEnum);
        }
    } else if (property.composite && property.lower === 1 && property.upper == 1) {
        return null;
    } else if (property.composite && property.lower >= 1 && (property.upper > 1 || property.upper == -1)) {
        return null;
    } else if (property.oneEnumeration) {
        return  Tuml.Slick.Editors.SelectEnumerationCellEditor;
    } else if (property.manyEnumeration) {
        return  Tuml.Slick.Editors.ManyEnumerationEditor;
    } else if (property.manyPrimitive && property.fieldType === 'String') {
        return  Tuml.Slick.Editors.ManyStringPrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Integer') {
        return  Tuml.Slick.Editors.ManyIntegerPrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Boolean') {
        return  Tuml.Slick.Editors.ManyBooleanPrimitiveEditor;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && property.oneToOne) {
        return  Tuml.Slick.Editors.SelectOneToOneCellEditor;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && property.manyToOne) {
//        return  Tuml.Slick.Editors.SelectManyToOneCellEditor;
        return  Tuml.Slick.Editors.SelectOneToOneCellEditor;
    } else if (property.name == 'id') {
        return null;
    } else if (property.fieldType == 'String') {
        return Tuml.Slick.Editors.Text;
    } else if (property.fieldType == 'Integer') {
        return Tuml.Slick.Editors.Integer;
    } else if (property.fieldType == 'Long') {
        return Tuml.Slick.Editors.Integer;
    } else if (property.fieldType == 'Boolean') {
        return Tuml.Slick.Editors.Checkbox;
    } else {
        return  Slick.Editors.Text;
    }

}
