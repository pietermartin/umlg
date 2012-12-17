(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml:{
            LeftMenuManager:LeftMenuManager
        }
    });

    function LeftMenuManager() {

        var self = this;
        var queryProperty;
        var queryData;
        var ulMenu;
        var contextMetaDataFrom;
        var contextMetaDataTo;
        var contextVertexId;

        function init() {
        }

        function refresh(_contextMetaDataFrom, _contextMetaDataTo, _contextVertexId) {
            contextMetaDataFrom = _contextMetaDataFrom;
            contextMetaDataTo = _contextMetaDataTo;
            contextVertexId = _contextVertexId;


            $('.ui-layout-west').children().remove();
            var tabs = $('<div />', {id:'tab-menu-container', class:'easyui-tabs'}).appendTo('.ui-layout-west');
            var tabDiv = $('<div />', {title:'Standard'}).appendTo(tabs);
            $('<div />', {title:'Tree'}).appendTo(tabs);

            ulMenu = createStdMenu(tabDiv);
            createQueryMenu();

            $('#tab-menu-container').tabs({border:false});
        }

        function refreshQuery() {
            $("#queryTree").remove();
            createQueryMenu();
        }

        function createQueryMenu() {
            //Add query tree
            var li = $('<li class="ui-left-menu-li-query-tree /">');
            li.appendTo(ulMenu);
            $('<div />', {id:"queryTree"}).appendTo(li);
            //Fetch the query data
            queryProperty = findQueryUrl();
            if (queryProperty != null) {
                var queryUri = queryProperty.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                $.ajax({
                    url:queryUri,
                    type:"GET",
                    dataType:"json",
                    contentType:"json",
                    success:function (response, textStatus, jqXHR) {
                        queryData = response;
                        internalCreateTree();
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        alert('Error getting query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        function refreshQueryMenu(queryTabName) {
            //Change the css activeproperty
            if (queryData !== undefined) {
                for (var i = 0; i < queryData[0].data.length; i++) {
                    var query = queryData[0].data[i];
                    $('#queryMenu' + query.name + 'Id').removeClass('querymenuactive');
                    $('#queryMenu' + query.name + 'Id').addClass('querymenuinactive');
                }
                var clickedNode = $('#queryMenu' + queryTabName + 'Id');
                clickedNode.removeClass("querymenuinactive");
                clickedNode.addClass("querymenuactive");
            }
        }

        function internalCreateTree() {
            var topNode = {label:'queries', _name:'queries'};
            var queryArray = [];
            for (var i = 0; i < queryData[0].data.length; i++) {
                var query = queryData[0].data[i];
                var queryUri = query.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), query.id);
                var oclExecuteUri = queryData[0].meta.oclExecuteUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                queryArray.push({label:query.name, tumlUri:queryUri, oclExecuteUri:oclExecuteUri, qualifiedName:queryProperty.qualifiedName, name:'<span id="queryMenu' + query.name + 'Id" class="querymenuinactive">' + query.name + '</span>', _name:query.name, queryEnum:query.queryEnum, queryString:query.queryString});
            }
            var treeData = $.extend(topNode, {children:queryArray})

            var treeDataArray = [];
            treeDataArray.push(treeData);

            $('#queryTree').tree({
                data:treeDataArray,
                autoEscape:false,
                autoOpen:true
            });

            // bind 'tree.click' event
            $('#queryTree').bind(
                'tree.click',
                function (event) {
                    // The clicked node is 'event.node'
                    var node = event.node;
                    if (node._name !== 'queries') {
                        //Change the css activeproperty
                        for (i = 0; i < queryData[0].data.length; i++) {
                            var query = queryData[0].data[i];
                            $('#queryMenu' + query.name + 'Id').removeClass('querymenuactive');
                            $('#queryMenu' + query.name + 'Id').addClass('querymenuinactive');
                        }
                        var clickedNode = $('#queryMenu' + node._name + 'Id');
                        clickedNode.removeClass("querymenuinactive");
                        clickedNode.addClass("querymenuactive");
                        self.onQueryClick.notify({post:false, tumlUri:node.tumlUri, oclExecuteUri:node.oclExecuteUri, qualifiedName:node.qualifiedName, name:node._name, queryEnum:node.queryEnum, queryString:node.queryString}, null, self);
                    }
                }
            );
        }

        function createStdMenu(tabDiv) {
            var ulMenu = $('<ul />', {class:'ui-left-menu-link'}).appendTo(tabDiv);
            var menuArray = createLeftMenuDataArray(contextMetaDataFrom, contextMetaDataTo);
            $.each(menuArray, function (index, value) {
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var li = $('<li class="ui-left-menu-li ' + value.menuCssClass + '"/>');
                var a = $('<a>', {
                    text:value.name,
                    title:value.name,
                    href:adjustedUri,
                    click:function () {
                        self.onMenuClick.notify({name:value.name, uri:adjustedUri}, null, self);
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
                menuArray.push({tumlUri:contextMetaDataFrom.uri, name:contextMetaDataFrom.name, menuCssClass:'contextactiveproperty'});
            }
            $.each(contextMetaDataFrom.properties, function (index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined && metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.oneEnumeration || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    var menuMetaProperty = {};
                    var menuCssClass;
                    $.each(contextMetaDataTo.properties, function (toIndex, toMetaProperty) {
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

            function compare(a, b) {
                if (a.name < b.name) return -1;
                if (a.name > b.name) return 1;
                return 0;
            }

            menuArray.sort(compare);
            return menuArray;
        }

        function findQueryUrl() {
            var result = null;
            for (var i = 0; i < contextMetaDataFrom.properties.length; i++) {
                var metaProperty = contextMetaDataFrom.properties[i];
                if (metaProperty.name == 'query') {
                    result = metaProperty;
                    break;
                }
            }
            return result;
        }

        $.extend(this, {
            "TumlLeftMenuManagerVersion":"1.0.0",
            "onMenuClick":new Tuml.Event(),
            "onQueryClick":new Tuml.Event(),
            "refresh":refresh,
            "refreshQueryMenu":refreshQueryMenu,
            "refreshQuery":refreshQuery
        });

        init();
    }
})(jQuery);
