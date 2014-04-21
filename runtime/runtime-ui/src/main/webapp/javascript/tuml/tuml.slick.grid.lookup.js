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
