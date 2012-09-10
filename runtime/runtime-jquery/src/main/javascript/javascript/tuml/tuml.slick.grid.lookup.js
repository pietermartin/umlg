function CompositeLookupMap(uriToCompositeParent) {
    var compositeParentMap = {};
    this.getOrLoadMap = function(rowVertexId) {
        if (compositeParentMap[rowVertexId] == undefined) {
            var adjustedUri = uriToCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
            $.ajax({
                type: 'GET',
                url: adjustedUri,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                asycn: false, 
                success: function(response){
                    compositeParentMap[rowVertexId] = response.data;
                },
                error: function(e){
                    alert(e);
                }
            });
        }
        return compositeParentMap;
    };
}
