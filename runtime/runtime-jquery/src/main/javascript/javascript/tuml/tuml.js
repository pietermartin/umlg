//This adds in the /ui part after /${app.rootUrl}

function change_my_url(title, url) {
    var urlToPush = '/restAndJson/ui' + url.substring('restAndJson/'.length);
    history.pushState({}, title, urlToPush);
}

function refreshPageTo(tumlUri) {

    var contextVertexId = tumlUri.match(/\d+/);
    var jqxhr = $.getJSON(tumlUri, function(response, b, c) {
        var classNameLowerCased;
        var menuArray = [];
        var metaForData = {};
        var contextMeta = {};
        var isOne;

        if (response.meta.length === 3) {
            //Property is a many, meta has 3 properties, one uml qualified name, one for both sides of the association
            //The first one is the parent property, i.e. the context property for which the menu is built
            isOne = false;
            contextMeta = response.meta[1];

            if (contextMeta.name !== 'Root') {
                //add a menu item to the context object
                menuArray.push({tumlUri: contextMeta.uri, name: contextMeta.name});
            }

            $.each(contextMeta.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    menuArray.push(metaProperty);
                }
            });
            metaForData = response.meta[2];
            createPageForMany(response.data, metaForData, tumlUri, response.meta[0].qualifiedName);

        } else {
            //Property is a one
            isOne = true;
            metaForData = response.meta[1];
            contextMeta = metaForData;
            if (metaForData.name !== 'Root') {
                //add a menu item to the context object
                menuArray.push({tumlUri: metaForData.uri, name: metaForData.name});
            }
            $.each(metaForData.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    menuArray.push(metaProperty);
                }
            });
            createPageForOne(response.data, metaForData, tumlUri, response.meta[0].qualifiedName);
            contextVertexId = response.data.id;

        }
        if (contextVertexId != null) {
            createLeftMenu(menuArray, contextVertexId);
        } else {
            createLeftMenu(menuArray);
        }

        //build context path to root
        if (contextMeta.name === 'Root') {
            createContextPath([{name: 'Root', uri: contextMeta.uri}]);
        } else {
            var pathToCompositeRootUri = contextMeta.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId) + '/compositePathToRoot';
            var jqxhr = $.getJSON(pathToCompositeRootUri, function(response, b, c) {
                createContextPath(response.data);
            });
        }
    }).fail(function(a, b, c) {
        alert("error " + a + ' ' + b + ' ' + c);
    });

}
