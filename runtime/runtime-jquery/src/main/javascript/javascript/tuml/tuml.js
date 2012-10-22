//This adds in the /ui part after /${app.rootUrl}

function change_my_url(title, url) {
    var urlToPush = '/restAndJson/ui' + url.substring('restAndJson/'.length);
    history.pushState({}, title, urlToPush);
}

function refreshPageTo(tumlUri, tabId) {

    var contextVertexId = tumlUri.match(/\d+/);
    var jqxhr = $.getJSON(tumlUri, function(result, b, c) {
        var classNameLowerCased;
        var menuArray = [];
        var metaForData = {};
        var contextMeta = {};
        var isOne;

        //only using the context metatData here so no need to be in the look
        if (result instanceof Array && result[0].meta.length === 3) {   
            contextMeta = result[0].meta[1];
            $.each(contextMeta.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    menuArray.push(metaProperty);
                }
            });
            //Property is a many, meta has 3 properties, one uml qualified name, one for both sides of the association
            //The first one is the parent property, i.e. the context property for which the menu is built
            isOne = false;
            var tabs = {};
            var ul = {};
            if (tabId === undefined || tabId === null) {
                //Clear the whole layout
                $('.ui-layout-center').children().remove();
                tabs = $('<div />', {id: 'tab-container', class: 'tab-container' }).appendTo('.ui-layout-center');
                ul = $('<ul />', {id: 'tabsul', class: 'etabs'}).appendTo(tabs);
            } else {
                tabs = $('#tab-container');
                ul = $('#tabsul');
            }

            //Reponse is an array, each element represents a concrete realization of the property.
            //Each concrete realization is displayed on its own tab.
            if (tabId !== undefined && tabId !== null) {
                for (i = 0; i < result.length; i++) {
                    response = result[i];
                    if (response.meta.length === 3) {
                        metaForData = response.meta[2];
                        if (metaForData.name === tabId) {
                            //Clear the div for the grid on the tab
                            $('#' + metaForData.name).children().remove();
                            var tabDiv = $('#' + metaForData.name);
                            $('<div id="myGrid' + metaForData.name + '" style="width:100%;height:90%;"></div>').appendTo(tabDiv);
                            $('<div id="pager' + metaForData.name + '" style="width:100%;height:20px;"></div>').appendTo(tabDiv);
                        }
                    }
                }
                //The grid creation must be after all the tabs have been created
                for (i = 0; i < result.length; i++) {
                    response = result[i];
                    if (response.meta.length === 3) {
                        metaForData = response.meta[2];
                        if (metaForData.name === tabId) {
                            createPageForMany(response.data, metaForData, tumlUri, response.meta[0].qualifiedName);
                        }
                    }
                }
            } else {
                for (i = 0; i < result.length; i++) {
                    response = result[i];
                    if (response.meta.length === 3) {
                        metaForData = response.meta[2];
                        //Add in a tab
                        $('<li class="tab"><a href=#' + metaForData.name + '><span>' + response.meta[0].qualifiedName + '</span></a></li>').appendTo(ul);
                        var tabDiv = $('<div />', {id: metaForData.name}).appendTo(tabs);
                        $('<div id="myGrid' + metaForData.name + '" style="width:100%;height:90%;"></div>').appendTo(tabDiv);
                        $('<div id="pager' + metaForData.name + '" style="width:100%;height:20px;"></div>').appendTo(tabDiv);
                    }
                }

                //The grid creation must be after all the tabs have been created
                for (i = 0; i < result.length; i++) {
                    response = result[i];
                    if (response.meta.length === 3) {
                        metaForData = response.meta[2];
                        createPageForMany(response.data, metaForData, tumlUri, response.meta[0].qualifiedName);
                    }
                }
            }

            //Add in a tab for queries
            $('<li class="tab"><a href=#query><span>Query</span></a></li>').appendTo(ul);
            var tabQuery = $('<div />', {id: 'query'}).text('Write them queries here').appendTo(tabs);

            $('#tab-container').easytabs({/*updateHash: false,*/ animate: false});
        } else {
            if (result instanceof Array) {
                response = result[0];
            } else {
                response = result;
            }
            //Property is a one
            isOne = true;
            metaForData = response.meta[1];
            contextMeta = metaForData;
            $.each(metaForData.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    menuArray.push(metaProperty);
                }
            });
            createPageForOne(response.data, metaForData, tumlUri, response.meta[0].qualifiedName);
            contextVertexId = response.data.id;
        }
        if (contextMeta.name !== 'Root') {
            //add a menu item to the context object
            menuArray.push({tumlUri: contextMeta.uri, name: contextMeta.name});
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
