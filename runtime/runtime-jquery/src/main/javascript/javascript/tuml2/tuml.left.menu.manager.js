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
        var tabContainer;

        function init() {
        }

        function refresh(_contextMetaDataFrom, _contextMetaDataTo, _contextVertexId) {
            contextMetaDataFrom = _contextMetaDataFrom;
            contextMetaDataTo = _contextMetaDataTo;
            contextVertexId = _contextVertexId;

            $('.ui-layout-west').children().remove();
            tabContainer = $('<div />', {id:'tabContainer-menu-container'}).appendTo('.ui-layout-west');
            tabContainer.append('<ul />');
            tabContainer.tabs();

            ulMenu = createStdMenu();
            createQueryMenu();
            createTree();
            tabContainer.tabs("option", "active", 0);

        }

        function createTree() {
            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Tree",
                id = "Tree",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id:'Tree'});
            tabContainer.append(standardMenuDiv);
            tabContainer.tabs("refresh");
        }

        function createStdMenu() {
            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Standard",
                id = "Standard",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id:'Standard'});
            tabContainer.append(standardMenuDiv);
            tabContainer.tabs("refresh");

            var ulMenu = $('<ul />', {class:'ui-left-menu-link'}).appendTo(standardMenuDiv);
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

        function createQueryMenu(selectedQueryTabName) {
            var li = $('<li class="ui-left-menu-li-query-tree /">');
            li.appendTo(ulMenu);
            $('<div />', {id:"queryTree"}).appendTo(li);

            //Add query tree
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
                        if (selectedQueryTabName !== undefined) {
                            refreshQueryMenu(selectedQueryTabName);
                        }
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        alert('Error getting query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        function refreshQuery(queryId) {
            $("#queryTree").remove();
            createQueryMenu(queryId);
        }

        function refreshQueryMenu(queryId) {
            //Change the css activeproperty
            if (queryData !== undefined) {
                for (var i = 0; i < queryData[0].data.length; i++) {
                    var query = queryData[0].data[i];
                    $('#queryMenu' + query.id).removeClass('querymenuactive');
                    $('#queryMenu' + query.id).addClass('querymenuinactive');
                }
                var clickedNode = $('#queryMenu' + queryId);
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
                queryArray.push({
                    label:query.name,
                    tumlUri:queryUri,
                    oclExecuteUri:oclExecuteUri,
                    qualifiedName:queryProperty.qualifiedName,
                    name:'<span id="queryMenu' + query.id + '" class="querymenuinactive queryitem">' + query.name + '</span>',
                    _name:query.name,
                    queryEnum:query.queryEnum,
                    queryString:query.queryString,
                    queryId:query.id});
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
                        self.onQueryClick.notify({
                            post:false,
                            tumlUri:node.tumlUri,
                            oclExecuteUri:node.oclExecuteUri,
                            qualifiedName:node.qualifiedName,
                            name:node._name,
                            queryEnum:node.queryEnum,
                            queryString:node.queryString,
                            id:node.queryId}, null, self);
                    }
                }
            );
        }

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
                if (metaProperty.name == 'instanceQuery') {
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
