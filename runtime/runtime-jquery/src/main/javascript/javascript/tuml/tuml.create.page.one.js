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
                var $input = constructInputForField(property);
                if ($input !== undefined) {
                    $input.appendTo(li);
                    if (property.date) {
                        $input.datepicker({
                            showOn: "button",
                            buttonImageOnly: true,
                            buttonImage: "../../javascript/slickgrid/images/calendar.gif"
                        });
                    } else if (property.time) {
                        $input.timepicker({
                            showOn: "button",
                            buttonImageOnly: true,
                            buttonImage: "../../javascript/slickgrid/images/calendar.gif"
                        });
                    } else if (property.dateTime) {
                        $input.datetimepicker({
                            showOn: "button",
                            buttonImageOnly: true,
                            buttonImage: "../../javascript/slickgrid/images/calendar.gif"
                        });
                    }
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
        } else if (property.date) {
            var $input = $("<input type=text />");
            $input[0].defaultValue = data[property.name];
            return $input;
        } else if (property.time) {
            var $input = $("<input type=text />");
            return $input;
        } else if (property.dateTime) {
            var $input = $("<input type=text />");
            return $input;
        } else if (!property.onePrimitive && !property.internationalPhoneNumber && !property.localPhoneNumber && !property.email && !property.video && !property.audio && !property.image && !property.manyPrimitive && !property.composite) {
            var $select = $('<select />', {class: 'chzn-select', style: 'width:350px;',  id: property.name + 'Id', name: property.name});
            appendLoopupOptionsToSelect(property.tumlLookupUri, property.lower > 0, data['id'], data[property.name], $select);
            return $select;
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

    function appendLoopupOptionsToSelect(tumlLookupUri, required, contextVertexId, currentValue, $select) {
        var adjustedUri = tumlLookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
        var jqxhr = $.getJSON(adjustedUri, function(response, b, c) {
            var contextVertexId = tumlUri.match(/\d+/);
            if (response.meta instanceof Array) {
                //if not a required field add a black value
                if (!required) {
                    $select.append($('<option />)').val("").html(""));
                }
                //append the current value to the dropdown
                $select.append($('<option />)').val(currentValue.id).html(currentValue.displayName));
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
                $select.val(currentValue.id);
                $select.chosen({allow_single_deselect: true});
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
            if (property.name !== 'id' && property.name !== 'uri') {
                if (property.onePrimitive) {
                    if (property.fieldType == 'Integer' || property.fieldType == 'Long') {
                        dataToSend[property.name] = parseInt($('#' + property.name + 'Id').val());
                    } else if (property.fieldType == 'String') {
                        dataToSend[property.name] = $('#' + property.name + 'Id').val();
                    } else if (property.fieldType == 'Boolean') {
                        dataToSend[property.name] = $('#' + property.name + 'Id').attr("checked") == "checked";
                    }
                } else if (!property.onePrimitive && !property.manyPrimitive && !property.inverseComposite) {
                    var $select = $('#' + property.name + 'Id');
                    var options = $select.children();
                    for (var i = 0; i < options.length; i++) {
                        if (options[i].selected) {
                            dataToSend[property.name]= {id: parseInt($select.val()), displayName: options[i].label};
                            break;
                        }
                    }
                }
            }});
            var result = JSON.stringify(dataToSend);
            return result;
    }

    init();

}
