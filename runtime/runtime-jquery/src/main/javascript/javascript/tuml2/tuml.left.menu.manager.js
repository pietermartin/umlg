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
        var accordionDiv;
        var umlPropertiesDiv;
        var umlOperationsDiv;
        var umlInstanceQueriesDiv;
        var umlClassQueriesDiv;
        var umlInstanceGroovyDiv;
        var umlClassGroovyDiv;
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

            setupTabsAndAccordian();

            createStdMenu();
            if (isTumlLib && contextVertexId !== undefined && contextVertexId !== null) {
                createInstanceQueryMenu();
                createClassQueryMenu();
            }
//            createTree();
            tabContainer.tabs("option", "active", 0);

        }

        function setupTabsAndAccordian() {
            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Standard",
                id = "Standard",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id:'Standard'});
            tabContainer.append(standardMenuDiv);
            accordionDiv = standardMenuDiv;
            accordionDiv.append($('<h3 />').text('Properties'));
            umlPropertiesDiv = $('<div />', {id:'umlProperties'});
            accordionDiv.append(umlPropertiesDiv);

            accordionDiv.append($('<h3 />').text('Operations'));
            umlOperationsDiv = $('<div />', {id:'umlOperations'});
            accordionDiv.append(umlOperationsDiv);

            if (isTumlLib && contextVertexId !== undefined && contextVertexId !== null) {
                accordionDiv.append($('<h3 />').text('Instance Queries'));
                umlInstanceQueriesDiv = $('<div />', {id:'umlInstanceQueries'});
                accordionDiv.append(umlInstanceQueriesDiv);
                accordionDiv.append($('<h3 />').text('Class Queries'));
                umlClassQueriesDiv = $('<div />', {id:'umlClassQueries'});
                accordionDiv.append(umlClassQueriesDiv);

                accordionDiv.append($('<h3 />').text('Instance Groovy'));
                umlInstanceGroovyDiv = $('<div />', {id:'umlInstanceGroovy'});
                accordionDiv.append(umlInstanceGroovyDiv);
                accordionDiv.append($('<h3 />').text('Class Groovy'));
                umlClassGroovyDiv = $('<div />', {id:'umlClassGroovy'});
                accordionDiv.append(umlClassGroovyDiv);
            }
            accordionDiv.accordion({
                heightStyle:"content",
                collapsible:true
            });

            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Tree",
                id = "Tree",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id:'Tree'});
            tabContainer.append(standardMenuDiv);
            tabContainer.tabs("refresh");
        }

        function createTree() {
        }

        function createStdMenu() {

            var ulMenu = $('<ul />', {class:'ui-left-menu-link'}).appendTo(umlPropertiesDiv);
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

        function createInstanceQueryMenu(queryId) {
            //Add query tree
            //Fetch the query data
            queryProperty = findQueryUrl('instanceQuery');
            if (queryProperty != null) {
                var queryUri = queryProperty.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                $.ajax({
                    url:queryUri,
                    type:"GET",
                    dataType:"json",
                    contentType:"json",
                    success:function (response, textStatus, jqXHR) {
                        queryData = response;
                        internalCreateTree(umlInstanceQueriesDiv, true);
                        if (queryId !== undefined) {
                            refreshQueryMenuCss(queryId);
                        }
                    },
                    error:function (jqXHR, textStatus, errorThrown) {
                        alert('Error getting query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        function createClassQueryMenu(queryId) {
            //Add query tree
            //Fetch the query data
            if (contextVertexId !== undefined && contextVertexId !== null) {
                var classQueryUri = "/" + tumlModelName + "/classquery/" + contextVertexId + "/query";
                if (classQueryUri != null) {
                    var queryUri = classQueryUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                    $.ajax({
                        url:queryUri,
                        type:"GET",
                        dataType:"json",
                        contentType:"json",
                        success:function (response, textStatus, jqXHR) {
                            queryData = response;
                            internalCreateTree(umlClassQueriesDiv, false);
                            if (queryId !== undefined) {
                                refreshQueryMenuCss(queryId);
                            }
                        },
                        error:function (jqXHR, textStatus, errorThrown) {
                            alert('Error getting query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                        }
                    });
                }
            }
        }

        function refreshInstanceQuery(queryId) {
            umlInstanceQueriesDiv.children().remove();
            createInstanceQueryMenu(queryId);
            accordionDiv.accordion("option", "active", 2);
        }

        function refreshClassQuery(queryId) {
            umlClassQueriesDiv.children().remove();
            createClassQueryMenu(queryId);
            accordionDiv.accordion("option", "active", 3);
        }

        function refreshQueryMenuCss(queryId, tabEnum) {
            if (umlInstanceQueriesDiv !== undefined) {
                //Change the css activeproperty
                umlInstanceQueriesDiv.find('.ui-left-menu-query-li').removeClass('querymenuactive');
                umlInstanceQueriesDiv.find('.ui-left-menu-query-li').addClass('querymenuinactive');
                if (queryData !== undefined) {
                    umlInstanceQueriesDiv.find('#' + queryId).removeClass("querymenuinactive");
                    umlInstanceQueriesDiv.find('#' + queryId).addClass("querymenuactive");
                }
            }
            if (umlClassQueriesDiv !== undefined) {
                umlClassQueriesDiv.find('.ui-left-menu-query-li').removeClass('querymenuactive');
                umlClassQueriesDiv.find('.ui-left-menu-query-li').addClass('querymenuinactive');
                if (queryData !== undefined) {
                    umlClassQueriesDiv.find('#' + queryId).removeClass("querymenuinactive");
                    umlClassQueriesDiv.find('#' + queryId).addClass("querymenuactive");
                }
            }
            //TODO link tabview manager with the accordion div to activate on tab select
            accordionDiv.accordion("option", "active", tabEnum);
        }

        function internalCreateTree(queryDiv, instanceQuery) {
            var queryArray = [];
            for (var i = 0; i < queryData[0].data.length; i++) {
                var query = queryData[0].data[i];
                var queryUri = query.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), query.id);
                var oclExecuteUri = queryData[0].meta.oclExecuteUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                queryArray.push({
                    label:query.name,
                    tumlUri:queryUri,
                    oclExecuteUri:oclExecuteUri,
                    name:query.name,
                    _name:query.name,
                    queryEnum:query.queryEnum,
                    queryString:query.queryString,
                    queryId:query.id,
                    queryType:instanceQuery ? 'instanceQuery' : 'classQuery',
                    menuCssClass:'querymenuinactive'});
            }

            var ulMenu = $('<ul />', {class:'ui-left-menu-query-link'}).appendTo(queryDiv);
            $.each(queryArray, function (index, value) {
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                var li = $('<li />', {id:value.queryId, class:'ui-left-menu-query-li' + ' ' + value.menuCssClass});
                var a = $('<a>', {
                    text:value.name,
                    title:value.name,
                    href:adjustedUri,
                    click:function () {
                        self.onQueryClick.notify({
                            post:false,
                            tumlUri:$.data(this, "query").tumlUri,
                            oclExecuteUri:$.data(this, "query").oclExecuteUri,
                            qualifiedName:$.data(this, "query").qualifiedName,
                            name:$.data(this, "query")._name,
                            queryEnum:$.data(this, "query").queryEnum,
                            queryString:$.data(this, "query").queryString,
                            queryType:$.data(this, "query").queryType,
                            id:$.data(this, "query").queryId}, null, self);

                        return false;
                    }
                });
                $.data(a[0], 'query', value);
                a.appendTo(li);
                li.appendTo(ulMenu);
            });
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

        function findQueryUrl(queryPropertyName) {
            var result = null;
            for (var i = 0; i < contextMetaDataFrom.properties.length; i++) {
                var metaProperty = contextMetaDataFrom.properties[i];
                if (metaProperty.name == queryPropertyName) {
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
            "refreshQueryMenuCss":refreshQueryMenuCss,
            "refreshInstanceQuery":refreshInstanceQuery,
            "refreshClassQuery":refreshClassQuery
        });

        init();
    }
})(jQuery);
