(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            LeftMenuManager: LeftMenuManager
        }
    });

    function LeftMenuManager() {

        var self = this;
        var queryProperty;

        function init() {
        }

        function refresh(contextMetaDataFrom, contextMetaDataTo, contextVertexId) {
            $('.ui-layout-west').children().remove();
            var tabs = $('<div />', {id: 'tab-menu-container', class: 'easyui-tabs'}).appendTo('.ui-layout-west');
            var tabDiv = $('<div />', {title: 'Standard'}).appendTo(tabs);
            $('<div />', {title: 'Tree'}).appendTo(tabs);

            var ulMenu = createStdMenu(contextMetaDataFrom, contextMetaDataTo, tabDiv, contextVertexId);
            createQueryMenu(contextMetaDataFrom, ulMenu, contextVertexId);

            $('#tab-menu-container').tabs({border: false});
        }

        function createQueryMenu(contextMetaData, ulMenu, contextVertexId) {
            //Add query tree
            var li = $('<li class="ui-left-menu-li-query-tree /">');
            li.appendTo(ulMenu);
            $('<div />', {id: "queryTree"}).appendTo(li);
            //Fetch the query data
            queryProperty = findQueryUrl(contextMetaData);
            if (queryProperty != null) {
                var queryUri = queryProperty.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                $.ajax({
                    url: queryUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
                    success: function(response, textStatus, jqXHR) {
                        internalCreateTree(response, contextVertexId);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert('Error getting query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        function internalCreateTree(queryData, contextVertexId) {
            var topNode = {label: 'queries'};
            var queryArray = [];
            for (i = 0; i < queryData[0].data.length; i++) {
                var query = queryData[0].data[i];
                var queryUri = query.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), query.id);
                var oclExecuteUri = queryData[0].meta.oclExecuteUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                queryArray.push({label: query.name, tumlUri: queryUri, oclExecuteUri: oclExecuteUri, qualifiedName: queryProperty.qualifiedName, name: query.name});
            }
            var treeData = treedata = $.extend(topNode, {children: queryArray})

            var treeDataArray = [];
            treeDataArray.push(treeData);

            $('#queryTree').tree({
                data: treeDataArray
            });

            // bind 'tree.click' event
            $('#queryTree').bind(
                'tree.click',
                function(event) {
                    // The clicked node is 'event.node'
                    var node = event.node;
                    if (node.name !== 'queries') {
                        self.onQueryClick.notify({tumlUri: node.tumlUri, oclExecuteUri: node.oclExecuteUri, qualifiedName: node.qualifiedName, name: node.name}, null, self);
                    }
                }
            );
        }

        function createStdMenu(contextMetaDataFrom, contextMetaDataTo, tabDiv, contextVertexId) {
            var ulMenu = $('<ul />', {class: 'ui-left-menu-link'}).appendTo(tabDiv);
            var menuArray = createLeftMenuDataArray(contextMetaDataFrom, contextMetaDataTo);
            $.each(menuArray, function(index, value) {
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var li = $('<li class="ui-left-menu-li ' + value.menuCssClass + '"/>');
                var a = $('<a>', {
                    text: value.name,
                    title: value.name,
                    href: adjustedUri,
                    click: function() {
                        self.onMenuClick.notify({name: value.name, uri: adjustedUri}, null, self);
                        return false;
                    }
                });
                a.appendTo(li);
                li.appendTo(ulMenu);
            });
            return ulMenu;
        };

        function createLeftMenuDataArray(contextMetaDataFrom, contextMetaDataTo) {
            var menuArray = [];
            if (contextMetaDataFrom.name !== 'Root') {
                //add a menu item to the context object
                menuArray.push({tumlUri: contextMetaDataFrom.uri, name: contextMetaDataFrom.name, menuCssClass: 'contextactiveproperty'});
            }
            $.each(contextMetaDataFrom.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.oneEnumeration|| metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    var menuMetaProperty = {};
                    var menuCssClass;
                    $.each(contextMetaDataTo.properties, function(toIndex, toMetaProperty) {
                        if (toMetaProperty.inverseQualifiedName == metaProperty.qualifiedName) {
                            menuCssClass = 'activeproperty';
                        }
                    });
                    menuMetaProperty['tumlUri'] = metaProperty.tumlUri;
                    menuMetaProperty['name'] = metaProperty.name; 
                    if (menuCssClass === undefined) {
                        menuCssClass = 'inactiveproperty';
                    }
                    menuMetaProperty['menuCssClass'] = menuCssClass;
                    menuArray.push(menuMetaProperty);
                }
            });

            function compare(a,b) {
                if(a.name<b.name) return -1;
                if(a.name>b.name) return 1;
                return 0;
            }
            menuArray.sort(compare);
            return menuArray;
        }

        function findQueryUrl(contextMetaData) {
            var result = null;
            $.each(contextMetaData.properties, function(index, metaProperty) {
                if (metaProperty.name == 'query') {
                    result = metaProperty;
                }
            });
            return result;
        }

        $.extend(this, {
            "TumlLeftMenuManagerVersion": "1.0.0",
            "onMenuClick": new Tuml.Event(),
            "onQueryClick": new Tuml.Event(),
            "refresh": refresh
        });

        init();
    }
})(jQuery);
