function RowLookupMap(uriToCompositeParent) {
    var rowLookupMap = {};
    this.getOrLoadMap = function(rowVertexId, callBack) {
        if (rowLookupMap[rowVertexId] == undefined) {
            var adjustedUri = uriToCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
            $.ajax({
                type: 'GET',
                url: adjustedUri,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response){
                    rowLookupMap[rowVertexId] = {compositeId: response.data.id}; 
                    callBack(response.data.id);
                },
                error: function(e){
                    alert(e);
                }
            });
        } else {
            callBack(rowLookupMap[rowVertexId].compositeId);
        }
    };
}

function CompositeParentLookupMap( lookupUri) {
    var compositeParentLookupMap = {};
    this.getOrLoadMap = function (compositeParentVertexId, rowVertexId, callBack) {
        if (compositeParentLookupMap[compositeParentVertexId] == undefined) {
            var adjustedUri = lookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
            $.ajax({
                type: 'GET',
                url: adjustedUri,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response){
                    compositeParentLookupMap[compositeParentVertexId] = [];
                    for (var i = 0; i < response.data.length; i++) {
                        compositeParentLookupMap[compositeParentVertexId].push({id: parseInt(response.data[i].id), displayName: response.data[i].name});
                    }
                    callBack(compositeParentLookupMap);
                },
                error: function(e){
                    alert(e);
                }
            });
        } else {
            callBack(compositeParentLookupMap);
        }
    }
}
