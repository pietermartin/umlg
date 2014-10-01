function calculateBodyHeight(container) {
    var windowHeight;
    if (container instanceof Tuml.TumlTabManyViewManager) {
        windowHeight = 600;
    } else {
        windowHeight = $('.ui-layout-center').height() - 102;
    }
    return windowHeight;
}

function retrieveMetaDataIfNotInCache(tumlUri, contextVertexId, result, callback) {

    var uriToCache = removeContextFromUrl(tumlUri);

    var metaDataFromCache = Tuml.Metadata.Cache.getFromCache(uriToCache);
    if (metaDataFromCache === undefined || metaDataFromCache === null) {
        //Get the meta data via the http OPTIONS method
        $.ajax({
            url: tumlUri,
            type: "OPTIONS",
            dataType: "json",
            contentType: "json",
            success: function (metaDataResult) {
                var metaDataArray = [];
                for (var i = 0; i < metaDataResult.length; i++) {
                    var metaData = {data: []};
                    metaData.meta = metaDataResult[i].meta;
                    metaDataArray.push(metaData);
                }
                Tuml.Metadata.Cache.add(uriToCache, metaDataArray);

                combineMetaDataWithResult(result, metaDataResult);

                callback(tumlUri, result, contextVertexId);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('error getting meta data from retrieveMetaDataIfNotInCache  ' + tumlUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
            }
        });
    } else {
        combineMetaDataWithResult(result, metaDataFromCache);
        callback(tumlUri, result, contextVertexId);
    }

    function removeContextFromUrl(url) {
        if (url == '/' + tumlModelName) {
            return tumlModelName;
        } else {
            var trimmedUrl = url.replace('/' + tumlModelName + '/', '');
            var firstIndexOf = trimmedUrl.indexOf('/');
            var secondIndexOf = trimmedUrl.indexOf('/', firstIndexOf + 1);
            var result = trimmedUrl.substring(0, firstIndexOf + 1) + '{param}' + trimmedUrl.substring(secondIndexOf);
            return result;
        }
    }
}


function combineMetaDataWithResult(result, metaDataResult) {
    for (var i = 0; i < result.length; i++) {
        if (metaDataResult[i].meta.from == undefined) {
            result[i].meta = metaDataResult[i].meta;
        } else {

            for (var j = 0; j < metaDataResult.length; j++) {

                if (result[i].meta.qualifiedNameTo == metaDataResult[j].meta.to.qualifiedName) {
                    //root scenario
                    if (result[i].meta.qualifiedNameFrom == metaDataResult[j].meta.from.qualifiedName) {
                        result[i].meta = metaDataResult[j].meta;
                        break;
                    }
                }

            }
        }
    }
}

function escapeColon(string) {
    return string.replace(/(:|\.)/g, '\\$1');
}

function retrieveVertexId(url) {
    if (url == '/' + tumlModelName) {
        return null;
    } else {
        var trimmedUrl = url.replace('/' + tumlModelName + '/', '');
        var firstIndexOfForwardSlash = trimmedUrl.indexOf('/');
        var lastIndexOfForwardSlash = trimmedUrl.lastIndexOf('/');
        if (lastIndexOfForwardSlash !== -1) {
            if (firstIndexOfForwardSlash == lastIndexOfForwardSlash) {
                //humans/123
                //Is a one
                var urlId = trimmedUrl.substring(lastIndexOfForwardSlash + 1);
                return urlId;
            } else {
                //humans/123/hand
                var tempUrl = trimmedUrl.substring(0, lastIndexOfForwardSlash);
                var secondLastIndexOfForwardSlash = tempUrl.lastIndexOf('/');
                var urlId = trimmedUrl.substring(secondLastIndexOfForwardSlash + 1, lastIndexOfForwardSlash);
                return urlId;
            }
        } else {
            return null;
        }
    }

}

function selectFormatter(property, isNew, updatedId) {
    if (property.name == 'uri') {
        return new TumlSlick.Formatters.Link;
    } else if (property.derived) {
        return TumlSlick.Formatters.TumlDerivedPropertyFormatter;
    } else if (property.name == 'id') {
        if (isNew) {
            return TumlSlick.Formatters.TumlIdNewFormatter;
        } else if (updatedId) {
            return TumlSlick.Formatters.TumlIdUpdatedFormatter;
        } else {
            return TumlSlick.Formatters.TumlRegularFormatter;
        }
    } else if (property.lower > 0 && property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
        if (property.dataTypeEnum === 'Password') {
            return  TumlSlick.Formatters.TumlRequiredPassword;
        } else if (property.dataTypeEnum === 'UnsecurePassword') {
            return  TumlSlick.Formatters.TumlRequiredPassword;
        } else {
            return  TumlSlick.Formatters.TumlRequired;
        }
    } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
        if (property.dataTypeEnum === 'Password') {
            return  TumlSlick.Formatters.TumlPassword;
        } else if (property.dataTypeEnum === 'UnsecurePassword') {
            return  TumlSlick.Formatters.TumlPassword;
        } else {
            return null;
        }
    } else if (property.lower > 0 && (property.oneEnumeration || property.manyEnumeration)) {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.composite && property.lower > 0) {
        return  null;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && (property.manyToOne || property.oneToOne) && property.lower > 0) {
        return  TumlSlick.Formatters.TumlToOneRequiredFormatter;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && (property.oneToMany || property.manyToMany) && property.lower > 0) {
        return TumlSlick.Formatters.TumlRequired;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && (property.manyToMany || property.oneToMany) && property.lower > 0) {
        return  TumlSlick.Formatters.TumlRequired;
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
    } else if (property.lower > 0 && property.fieldType == 'Real') {
        return  TumlSlick.Formatters.TumlRequired;
    } else if (property.lower > 0 && property.fieldType == 'UnlimitedNatural') {
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
    } else if (property.derived) {
    } else if (property.dataTypeEnum != null && property.dataTypeEnum !== undefined) {
        if (property.upper > 1 || property.upper === -1) {
            if (property.dataTypeEnum == 'Date') {
                return new TumlSlick.Validators.TumlManyDateValidator(property).validate;
            } else if (property.dataTypeEnum == 'Time') {
                return new TumlSlick.Validators.TumlManyTimeValidator(property).validate;
            } else if (property.dataTypeEnum == 'DateTime') {
                return new TumlSlick.Validators.TumlManyDateTimeValidator(property).validate;
            } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
            } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
            } else if (property.dataTypeEnum == 'Email') {
                return new TumlSlick.Validators.TumlManyString(property).validate;
            } else if (property.dataTypeEnum == 'Host') {
                return new TumlSlick.Validators.TumlManyString(property).validate;
            } else if (property.dataTypeEnum == 'Password') {
                return new TumlSlick.Validators.TumlManyString(property).validate;
            } else if (property.dataTypeEnum == 'UnsecurePassword') {
                return new TumlSlick.Validators.TumlManyString(property).validate;
            } else if (property.dataTypeEnum == 'QuartzCron') {
                return new TumlSlick.Validators.TumlManyString(property).validate;
            } else if (property.dataTypeEnum == 'Video') {
            } else if (property.dataTypeEnum == 'Audio') {
            } else if (property.dataTypeEnum == 'Image') {
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
        } else {
            if (property.dataTypeEnum == 'Date') {
                return new TumlSlick.Validators.Date(property).validate;
            } else if (property.dataTypeEnum == 'Time') {
                return new TumlSlick.Validators.Time(property).validate;
            } else if (property.dataTypeEnum == 'DateTime') {
                return new TumlSlick.Validators.DateTime(property).validate;
            } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
            } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
            } else if (property.dataTypeEnum == 'Email') {
                return new TumlSlick.Validators.TumlString(property).validate;
            } else if (property.dataTypeEnum == 'Host') {
                return new TumlSlick.Validators.TumlString(property).validate;
            } else if (property.dataTypeEnum == 'Password') {
                return new TumlSlick.Validators.TumlString(property).validate;
            } else if (property.dataTypeEnum == 'UnsecurePassword') {
                return new TumlSlick.Validators.TumlString(property).validate;
            } else if (property.dataTypeEnum == 'QuartzCron') {
                return new TumlSlick.Validators.TumlString(property).validate;
            } else if (property.dataTypeEnum == 'Video') {
            } else if (property.dataTypeEnum == 'Audio') {
            } else if (property.dataTypeEnum == 'Image') {
            } else {
                alert('Unsupported dataType ' + property.dataTypeEnum);
            }
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
    } else if (!property.manyPrimitive && property.fieldType == 'UnlimitedNatural') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'UnlimitedNatural') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Real') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Real') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Long') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Long') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Float') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Float') {
        return new TumlSlick.Validators.TumlManyNumber(property).validate;
    } else if (!property.manyPrimitive && property.fieldType == 'Double') {
        return new TumlSlick.Validators.TumlNumber(property).validate;
    } else if (property.manyPrimitive && property.fieldType == 'Double') {
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
            valid: true,
            msg: null}
    };
}

function selectEditor(property) {
    if (property.name == 'uri') {
        return null;
    } else if (property.readOnly) {
        return null;
    } else if (property.dataTypeEnum !== undefined && property.dataTypeEnum != null) {
        if (property.upper > 1 || property.upper === -1) {
            if (property.dataTypeEnum == 'Date') {
                return  Tuml.Slick.Editors.ManyDateEditor;
            } else if (property.dataTypeEnum == 'Time') {
                return  Tuml.Slick.Editors.ManyTime;
            } else if (property.dataTypeEnum == 'DateTime') {
                return  Tuml.Slick.Editors.ManyDateTime;
            } else if (property.dataTypeEnum == 'InternationalPhoneNumber') {
                //TODO
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'LocalPhoneNumber') {
                //TODO
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'Email') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'Host') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'Password') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'UnsecurePassword') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'QuartzCron') {
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
        } else {
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
            } else if (property.dataTypeEnum == 'Host') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'Password') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'UnsecurePassword') {
                return Tuml.Slick.Editors.Text;
            } else if (property.dataTypeEnum == 'QuartzCron') {
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
    } else if (property.manyPrimitive && property.fieldType === 'UnlimitedNatural') {
        return  Tuml.Slick.Editors.ManyIntegerPrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Real') {
        return  Tuml.Slick.Editors.ManyDoublePrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Long') {
        return  Tuml.Slick.Editors.ManyIntegerPrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Float') {
        return  Tuml.Slick.Editors.ManyDoublePrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Double') {
        return  Tuml.Slick.Editors.ManyDoublePrimitiveEditor;
    } else if (property.manyPrimitive && property.fieldType === 'Boolean') {
        return  Tuml.Slick.Editors.ManyBooleanPrimitiveEditor;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && !property.associationClassOne && property.memberEndOfAssociationClass && (property.oneToOne || property.manyToOne)) {
        return  Tuml.Slick.Editors.SelectToOneAssociationClassCellEditor;
    } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite && !property.associationClassOne && (property.oneToOne || property.manyToOne)) {
        return  Tuml.Slick.Editors.SelectOneToOneCellEditor;
    } else if (!property.composite && property.lower > 0 && !property.associationClassOne && (property.oneToMany || property.manyToMany)) {
        return  null;
    } else if (!property.composite && property.lower > 0 && property.associationClassOne) {
        return null;
    } else if (property.name == 'id') {
        return null;
    } else if (property.fieldType == 'String') {
        return Tuml.Slick.Editors.Text;
    } else if (property.fieldType == 'Integer') {
        return Tuml.Slick.Editors.Integer;
    } else if (property.fieldType == 'UnlimitedNatural') {
        return Tuml.Slick.Editors.Integer;
    } else if (property.fieldType == 'Real') {
        return Tuml.Slick.Editors.Double;
    } else if (property.fieldType == 'Long') {
        return Tuml.Slick.Editors.Integer;
    } else if (property.fieldType == 'Float') {
        return Tuml.Slick.Editors.Double;
    } else if (property.fieldType == 'Double') {
        return Tuml.Slick.Editors.Double;
    } else if (property.fieldType == 'Boolean') {
        return Tuml.Slick.Editors.Checkbox;
    } else {
        return  Slick.Editors.Text;
    }

}

function addUiToUrl(url) {
    var indexOfSecondBackSlash = url.indexOf('/', 1);
    var firstPart = url.substring(0, indexOfSecondBackSlash);
    var secondPart = url.substring(indexOfSecondBackSlash, url.length);
    var result;
    if (firstPart !== undefined && firstPart != '') {
        result = firstPart + '/ui2' + secondPart;
    } else {
        result = secondPart + '/ui2';
    }
    return result;
}

function removeUiFromUrl(url) {
    return url.replace("/ui2", "");
}

function createGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}