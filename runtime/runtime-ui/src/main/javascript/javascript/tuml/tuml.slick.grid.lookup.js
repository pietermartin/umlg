//This map stores to composite parent of the 2 entities in an association for each row
function RowLookupMap(contextVertexId, uriToCompositeParent, uriToCompositeParentOnCompositeParent) {
//    var rowLookupMap = {};
//    this.getOrLoadMap = function(rowVertexId, callBack) {
//        if (rowLookupMap[rowVertexId] == undefined) {
//            if (isNaN(rowVertexId)) {
//                var adjustedUri = uriToCompositeParentOnCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
//            } else {
//                var adjustedUri = uriToCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
//            }
//            $.ajax({
//                type: 'GET',
//                url: adjustedUri,
//                dataType: 'json',
//                contentType: 'application/json; charset=utf-8',
//                success: function(response){
//                    rowLookupMap[rowVertexId] = {compositeId: response.data.id};
//                    callBack(response.data.id);
//                },
//                error: function(e){
//                    alert(e);
//                }
//            });
//        } else {
//            callBack(rowLookupMap[rowVertexId].compositeId);
//        }
//    };
}

function CompositeParentLookupMap(contextVertexId, lookupUri, lookupUriOnCompositeParent) {
//    var compositeParentLookupMap = {};
//    this.getOrLoadMap = function (compositeParentVertexId, rowVertexId, callBack) {
//        if (compositeParentLookupMap[compositeParentVertexId] == undefined) {
//            if (isNaN(rowVertexId)) {
//                var adjustedUri = lookupUriOnCompositeParent.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
//            } else {
//                var adjustedUri = lookupUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), rowVertexId);
//            }
//            $.ajax({
//                type: 'GET',
//                url: adjustedUri,
//                dataType: 'json',
//                contentType: 'application/json; charset=utf-8',
//                success: function(response){
//                    compositeParentLookupMap[compositeParentVertexId] = [];
//                    for (var i = 0; i < response.data.length; i++) {
//                        compositeParentLookupMap[compositeParentVertexId].push({id: parseInt(response.data[i].id), displayName: response.data[i].name});
//                    }
//                    callBack(compositeParentLookupMap);
//                },
//                error: function(e){
//                    alert(e);
//                }
//            });
//        } else {
//            callBack(compositeParentLookupMap);
//        }
//    }
}

function RowEnumerationLookupMap(enumClassName, enumLookupUri) {
    var rowEnumerationLookupMap = {};
    this.getOrLoadMap = function(callBack) {
        if (rowEnumerationLookupMap[enumClassName] === undefined) {
            $.ajax({
                type: 'GET',
                url: enumLookupUri + '?enumQualifiedName=' + enumClassName,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function(response){
                    rowEnumerationLookupMap[enumClassName] = response.data;
                    callBack(response.data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert("error getting enumeration lookup data: " + textStatus);
                }
            });
        } else {
            callBack(rowEnumerationLookupMap[enumClassName]);
        }
    }
}
