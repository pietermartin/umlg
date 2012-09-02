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
            }
        });
        var result = JSON.stringify(dataToSend);
        return result;
    }

    init();

}
