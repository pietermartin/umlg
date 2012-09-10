function createPageForOne(data, metaForData, tumlUri) {

    function init() {
        //Clear all elements
        $('.ui-layout-center').children().remove();
        $('<div>' + metaForData.name + '</div>').appendTo('.ui-layout-center');
        $('<div />', {id: 'formDiv'}).appendTo('.ui-layout-center');

        var ul = $('<ul />').appendTo("#formDiv");
        $.each(metaForData.properties, function(index, property) {
            if (!property.inverseComposite && (property.oneToOne || property.manyToOne) && property.name !== 'uri') {
                var li = $('<li>').appendTo(ul);
                $('<label />', {for: property.name + 'Id'}).text(property.name).appendTo(li);
                var input = constructInputForField(property);
                if (input !== undefined) {
                    input.appendTo(li);
                }
            }
        });

        var $saveButton = $('<button />').text('Save').click(function() {
            $.ajax({
                url: tumlUri,
                type: "PUT",
                dataType: "json",
                contentType: "json",
                data: fieldsToJson(),
                success: function() {
                    refreshPageTo(tumlUri);
                },
                error: function() {
                    alert("fail :-(");
                }
            });
            return false ;
        }).appendTo('#formDiv');

        var $cancelButton = $('<button />').text('Cancel').click(function() {
            refreshPageTo(tumlUri);
        }).appendTo('#formDiv');

    }

    function constructInputForField(property) {
        if (property.name == 'id') {
            return $('<input />', {disabled: 'disabled', type:'text', class: 'field', id: property.name + 'Id', name: property.name, value: data[property.name]});
        } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
            var select = $('<select />', {class: 'chzn-select', style: 'width:350px;',  id: property.name + 'Id', name: property.name});
            appendLoopupOptionsToSelect(property.tumlLookupUri, data['id'], data[property.name], select);
            return select;
        } else if (property.fieldType == 'String') {
            return $('<input />', {type:'text', class: 'field', id: property.name + 'Id', name: property.name, value: data[property.name]});
        } else if (property.fieldType == 'Integer') {
            return $('<input />', {type:'text', class: 'field', id: property.name + 'Id', name: property.name, value: data[property.name]});
        } else if (property.fieldType == 'Long') {
            return $('<input />', {type:'text', class: 'field', id: property.name + 'Id', name: property.name, value: data[property.name]});
        } else if (property.fieldType == 'Boolean') {
            if (data[property.name]) {
                return $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + 'Id', name: property.name, checked: 'checked'});
            } else {
                return $('<input />', {type: 'checkbox', class: 'editor-checkbox', id: property.name + 'Id', name: property.name });
            }
        }
    }

    function appendLoopupOptionsToSelect(tumlLookupUri, contextVertexId, defaultValue, $select) {
        var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        var jqxhr = $.getJSON(adjustedUri, function(response, b, c) {
            var contextVertexId = tumlUri.match(/\d+/);
            if (response.meta instanceof Array) {
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
                $select.val(defaultValue);
                $select.chosen();
            } else {
                //Property is a one
                alert('this should not happen');
            }
        }).fail(function(a, b, c) {
            alert("error " + a + ' ' + b + ' ' + c);
        });
    }

    function fieldsToJson() {
        var dataToSend = {};
        $.each(metaForData.properties, function(index, property) {
            if (property.onePrimitive) {
                if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                    dataToSend[property.name] = parseInt($('#' + property.name + 'Id').val());
                } else if (property.fieldType == 'String') {
                    dataToSend[property.name] = $('#' + property.name + 'Id').val();
                } else if (property.fieldType == 'Boolean') {
                    dataToSend[property.name] = $('#' + property.name + 'Id').attr("checked") == "checked";
                }
            } else if (!property.onePrimitive && !property.manyPrimitive && !property.composite) {
                dataToSend[property.name] = parseInt($('#' + property.name + 'Id').val());
            }
        });
        var result = JSON.stringify(dataToSend);
        return result;
    }

    init();

}
