function CompositeLookupMap(uriToCompositeParent, lookupUri) {
    var compositeParentMap = {};
    this.getOrLoadMap = function(rowVertexId,$select) {
        if (compositeParentMap[rowVertexId] == undefined) {
            var adjustedUri = uriToCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
            $.ajax({
                type: 'GET',
                url: adjustedUri,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response){
                    getLookupData(rowVertexId, $select, compositeParentMap[response.data.id]);
                },
                error: function(e){
                    alert(e);
                }
            });
        }
        return compositeParentMap;
    };

    function getLookupData(rowVertexId, $select, compositeParentMapItem) {
        var adjustedUri = lookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
        $.ajax({
            type: 'GET',
            url: adjustedUri,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            success: function(response){
                compositeParentMapItem = response.data;
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
            },
            error: function(e){
                alert(e);
            }
        });
    }
}
