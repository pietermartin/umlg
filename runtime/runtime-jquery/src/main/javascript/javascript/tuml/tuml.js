//This adds in the /ui part after /${app.rootUrl}

function change_my_url(title, url) {
    var urlToPush = '/restAndJson/ui' + url.substring('restAndJson/'.length);
    history.pushState({}, title, urlToPush);
}

function refreshPageTo(tumlUri) {

    var jqxhr = $.getJSON(tumlUri, function(response, b, c) {
        var classNameLowerCased;
        var menuArray = [];
        var metaForData = {};
        var isOne;

        var contextVertexId = tumlUri.match(/\d+/);
        if (response.meta instanceof Array) {
            //Property is a many, meta has 2 properties, one for both sides of the association
            //The first one is the parent property, i.e. the context property for which the menu is built
            isOne = false;
            var contextMeta = response.meta[0];
            classNameLowerCased = contextMeta.name.toLowerCase();
            $.each(contextMeta.properties, function(index, metaProperty) {
                menuArray[index] = metaProperty;
            });
            metaForData = response.meta[1];
            createPageForMany(response.data, metaForData, tumlUri);
            contextVertexId = tumlUri.match(/\d+/);
        } else {
            //Property is a one
            isOne = true;
            $.each(response.meta.properties, function(index, metaProperty) {
                menuArray[index] = metaProperty;
            });
            classNameLowerCased = response.meta.name.toLowerCase();
            metaForData = response.meta;
            //createPageForOne(response.data, metaForData, tumlUri);
            createPageForOne(response.data, metaForData, tumlUri);
            contextVertexId = response.data.id;
        }
        if (contextVertexId != null) {
            createLeftMenu(menuArray, classNameLowerCased, contextVertexId);
        } else {
            createLeftMenu(menuArray);
        }

    }).fail(function(a, b, c) {
        alert("error " + a + ' ' + b + ' ' + c);
    });

}
