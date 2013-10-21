(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            LeftMenuManager: LeftMenuManager,
            AccordionEnum: {
                PROPERTIES: {index: 0, label: 'Properties'},
                OPERATIONS: {index: 1, label: 'Operations'},
                INSTANCE_QUERIES: {index: 2, label: 'Instance Queries'},
                CLASS_QUERIES: {index: 3, label: 'Class Queries'},
                INSTANCE_GROOVY: {index: 4, label: 'Instance Groovy'},
                CLASS_GROOVY: {index: 5, label: 'Class Groovy'}
            }
        }
    });

    function LeftMenuManager() {

        var self = this;
        this.accordionDiv = null;
        this.umlPropertiesDiv = null;
        this.umlOperationsDiv = null;
        this.umlInstanceQueriesDiv = null;
        this.umlClassQueriesDiv = null;
        this.umlInstanceGroovyDiv = null;
        this.umlClassGroovyDiv = null;
        this.contextMetaDataFrom = null;
        this.contextVertexId = null;
        this.tabContainer = null;
        this.queryToHighlightId = -1;

        function init() {
        }

        this.refresh = function (_contextMetaDataFrom, _contextMetaDataTo, _contextVertexId, propertyNavigatingTo) {
            this.contextMetaDataFrom = _contextMetaDataFrom;
            if (_contextVertexId !== undefined && _contextVertexId !== null) {
                this.contextVertexId = decodeURIComponent(_contextVertexId);
            } else {
                this.contextVertexId = null;
            }

            var leftMenuPaneH3l = $('#leftMenuPaneH3l');
            leftMenuPaneH3l.empty();
            var leftMenuPaneBody = $('#leftMenuPaneBody');
            leftMenuPaneBody.empty();

            leftMenuPaneH3l.text(this.contextMetaDataFrom.qualifiedName);

            this.tabContainer = $('<div />', {id: 'tabContainer-menu-container'}).appendTo(leftMenuPaneBody);
            this.tabContainer.append('<ul />');
            this.tabContainer.tabs();

            this.setupTabsAndAccordian();
            this.createPropertiesMenu(propertyNavigatingTo);
            if (isUmlgLib && this.contextVertexId !== undefined && this.contextVertexId !== null) {
                this.createInstanceQueryMenu(-1);
                this.createClassQueryMenu(-1);
            }
            this.tabContainer.tabs("option", "active", 0);
            this.setFocus();
        }

        this.setupTabsAndAccordian = function () {
            //Do not bother with tabindex as the components sets the first one to 0 and the rest to -1 automatically
            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Standard",
                id = "Standard",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            this.tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id: 'Standard'});
            this.tabContainer.append(standardMenuDiv);
            this.accordionDiv = standardMenuDiv;
            this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.PROPERTIES.label));
            this.umlPropertiesDiv = $('<div />', {id: 'umlProperties'});
            this.accordionDiv.append(this.umlPropertiesDiv);

            this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.OPERATIONS.label));
            this.umlOperationsDiv = $('<div />', {id: 'umlOperations'});
            this.accordionDiv.append(this.umlOperationsDiv);

            if (isUmlgLib && this.contextVertexId !== undefined && this.contextVertexId !== null) {
                this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.INSTANCE_QUERIES.label));
                this.umlInstanceQueriesDiv = $('<div />', {id: 'umlInstanceQueries'});
                this.accordionDiv.append(this.umlInstanceQueriesDiv);
                this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.CLASS_QUERIES.label));
                this.umlClassQueriesDiv = $('<div />', {id: 'umlClassQueries'});
                this.accordionDiv.append(this.umlClassQueriesDiv);

                this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.INSTANCE_GROOVY.label));
                this.umlInstanceGroovyDiv = $('<div />', {id: 'umlInstanceGroovy'});
                this.accordionDiv.append(this.umlInstanceGroovyDiv);
                this.accordionDiv.append($('<h3 />').text(Tuml.AccordionEnum.CLASS_GROOVY.label));
                this.umlClassGroovyDiv = $('<div />', {id: 'umlClassGroovy'});
                this.accordionDiv.append(this.umlClassGroovyDiv);
            }
            this.accordionDiv.accordion({
                heightStyle: "content",
                collapsible: true
            });

            var tabTemplate = "<li><a href='#{href}'>#{label}</a></li>";
            var label = "Tree",
                id = "Tree",
                li = $(tabTemplate.replace(/#\{href\}/g, "#" + id).replace(/#\{label\}/g, label));
            this.tabContainer.find(".ui-tabs-nav").append(li);
            var standardMenuDiv = $('<div />', {id: 'Tree'});
            this.tabContainer.append(standardMenuDiv);
            this.tabContainer.tabs("refresh");
        }

        this.setFocus = function () {
            var active = this.accordionDiv.accordion("option", "active");
            switch (active) {
                case Tuml.AccordionEnum.PROPERTIES.index:
                    var propertiesMenu = $('#propertiesMenu');
                    propertiesMenu.focus();
                    break;
                case Tuml.AccordionEnum.OPERATIONS.index:
                    console.log('OPERATIONS');
                    break;
                case Tuml.AccordionEnum.INSTANCE_QUERIES.index:
                    var propertiesMenu = $('#instanceQueryMenu');
                    propertiesMenu.focus();
                    break;
                case Tuml.AccordionEnum.CLASS_QUERIES.index:
                    var propertiesMenu = $('#classQueryMenu');
                    propertiesMenu.focus();
                    break;
                case Tuml.AccordionEnum.INSTANCE_GROOVY.index:
                    console.log('INSTANCE_GROOVY');
                    break;
                case Tuml.AccordionEnum.CLASS_GROOVY.index:
                    console.log('INSTANCE_GROOVY');
                    break;
                default:
                    throw 'Unknown active accordion!';
                    break;
            }

        }

        this.createPropertiesMenu = function (propertyNavigatingTo) {
            var ulMenu = $('<ul />', {id: 'propertiesMenu'}).appendTo(this.umlPropertiesDiv);
            var menuArray = createLeftMenuDataArray(this.contextMetaDataFrom, propertyNavigatingTo);

            for (var i = 0; i < menuArray.length; i++) {
                var value = menuArray[i];
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.contextVertexId));
                adjustedUri = addUiToUrl(adjustedUri)
                var li = $('<li />').appendTo(ulMenu);
                li.data("contextData", {name: value.name, uri: adjustedUri});
                var a = $('<a />', {title: value.name, href: adjustedUri, class: value.aCssClass}).appendTo(li);
                a.on('click', function (e) {
                    var link = $(e.target);
                    var contextData = link.parent().data("contextData");
                    self.onMenuClick.notify({name: contextData.name, uri: removeUiFromUrl(contextData.uri)}, null, self);
                    e.preventDefault();
                    e.stopImmediatePropagation();
                });
                var span = $('<span class="ui-icon ' + value.menuIconClass + '"></span>').appendTo(a);
                if (value.multiplicityDisplay !== undefined) {
                    a.append(value.name + ' ' + value.multiplicityDisplay);
                } else {
                    a.append(value.name);
                }
            }
            //This is for enter keystroke on the menu
            ulMenu.menu({
                select: function (e, ui) {
                    var contextData = ui.item.data("contextData");
                    self.onMenuClick.notify({name: contextData.name, uri: removeUiFromUrl(contextData.uri)}, null, self);
                    e.preventDefault();
                }
            });
            return ulMenu;
        };

        this.createQueryMenu = function (queryDiv, isInstanceQuery, queryData) {
            var queryArray = [];
            for (var i = 0; i < queryData[0].data.length; i++) {
                var query = queryData[0].data[i];
                var queryUri = query.uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(query.id));
                var oclExecuteUri = queryData[0].meta.oclExecuteUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);
                queryArray.push({
                    label: query.name,
                    tumlUri: queryUri,
                    oclExecuteUri: oclExecuteUri,
                    name: query.name,
                    _name: query.name,
                    queryEnum: query.queryEnum,
                    queryString: query.queryString,
                    queryId: query.id,
                    queryType: isInstanceQuery ? 'instanceQuery' : 'classQuery',
                    menuCssClass: 'querymenuinactive ' + (isInstanceQuery ? 'instance-query' : 'class-query')
                });
            }
            var ulMenu;
            if (isInstanceQuery) {
                ulMenu = $('<ul />', {id: 'instanceQueryMenu'}).appendTo(queryDiv);
            } else {
                ulMenu = $('<ul />', {id: 'classQueryMenu'}).appendTo(queryDiv);
            }
            for (var i = 0; i < queryArray.length; i++) {
                var value = queryArray[i];
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);
                var li = $('<li />').appendTo(ulMenu);
                li.data("contextData", value);
                var a = $('<a />', {title: value.name, href: adjustedUri}).appendTo(li);

                a.on('click',
                    function (e) {
                        var a = $(e.target);
                        var contextData = $(e.target).parent().data("contextData");
                        var query = {
                            post: false,
                            tumlUri: contextData.tumlUri,
                            oclExecuteUri: contextData.oclExecuteUri,
                            qualifiedName: contextData.qualifiedName,
                            name: contextData._name,
                            queryEnum: contextData.queryEnum,
                            queryString: contextData.queryString,
                            queryType: contextData.queryType,
                            id: contextData.queryId
                        };
                        self.onQueryClick.notify(query, null, self);
                        a.focus();
                        e.preventDefault();
                        e.stopImmediatePropagation();
                    }
                );

                var span = $('<span class="ui-icon ui-icon-gear"></span>').appendTo(a);
                a.append(value.name);
            }
            ulMenu.menu({
                select: function (e, ui) {
                    var a = ui.item.find('a');
                    var contextData = ui.item.data("contextData");
                    var query = {
                        post: false,
                        tumlUri: contextData.tumlUri,
                        oclExecuteUri: contextData.oclExecuteUri,
                        qualifiedName: contextData.qualifiedName,
                        name: contextData._name,
                        queryEnum: contextData.queryEnum,
                        queryString: contextData.queryString,
                        queryType: contextData.queryType,
                        id: contextData.queryId
                    };
                    self.onQueryClick.notify(query, null, self);
                    a.focus();
                    e.preventDefault();
                    e.stopImmediatePropagation();
                }
            });
        }

        this.createInstanceQueryMenu = function (queryId) {
            var self = this;
            //Add query tree
            //Fetch the query data
            var queryProperty = this.findQueryUrl('instanceQuery');
            if (queryProperty != null) {
                var queryUri = queryProperty.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.contextVertexId));
                $.ajax({
                    url: queryUri,
                    type: "GET",
                    dataType: "json",
                    contentType: "json",
                    success: function (response) {
                        retrieveMetaDataIfNotInCache(queryUri, this.contextVertexId, response, self.continueCreateInstanceQueryMenu);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('Error getting instance query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                    }
                });
            }
        }

        this.continueCreateInstanceQueryMenu = function (tumlUri, result, contextVertexId) {
            self.createQueryMenu(self.umlInstanceQueriesDiv, true, result);
            if (self.queryToHighlightId !== undefined) {
                self.refreshQueryMenuCss(self.queryToHighlightId);
            }
        }

        this.createClassQueryMenu = function (queryToHighLightId) {
            var self = this;
            this.queryToHighlightId = queryToHighLightId;
            //Add query tree
            //Fetch the query data
            if (this.contextVertexId !== null) {
                var classQueryUri = "/" + tumlModelName + "/classquery/" + encodeURIComponent(this.contextVertexId) + "/query";
                if (classQueryUri != null) {
//                    var queryUri = classQueryUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);
                    $.ajax({
                        url: classQueryUri,
                        type: "GET",
                        dataType: "json",
                        contentType: "json",
                        success: function (response) {
                            retrieveMetaDataIfNotInCache(classQueryUri, this.contextVertexId, response, self.continueCreateClassQueryMenu);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert('Error getting class query data. textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
                        }
                    });
                }
            }
        }

        this.continueCreateClassQueryMenu = function (tumlUri, result, contextVertexId) {
            self.createQueryMenu(self.umlClassQueriesDiv, false, result);
            if (self.queryToHighlightId !== -1) {
                self.refreshQueryMenuCss(self.queryToHighlightId);
            }
        }

        this.deleteInstanceQuery = function (queryId) {
            this.umlInstanceQueriesDiv.find('#' + queryId).remove();
        }

        this.deleteClassQuery = function (queryId) {
            this.umlClassQueriesDiv.find('#' + queryId).remove();
        }

        this.refreshInstanceQuery = function (queryId) {
            this.umlInstanceQueriesDiv.children().remove();
            this.createInstanceQueryMenu(queryId);
            this.accordionDiv.accordion("option", "active", 2);
        }

        this.refreshClassQuery = function (queryId) {
            this.umlClassQueriesDiv.children().remove();
            this.createClassQueryMenu(queryId);
            this.accordionDiv.accordion("option", "active", 3);
        }

        this.refreshQueryMenuCss = function (/*queryData, */queryToHighlightId, leftAccordionIndex) {

            if (this.umlInstanceQueriesDiv !== null) {
                //Change the css activeproperty
                this.umlInstanceQueriesDiv.find('.ui-left-menu-query-li').removeClass('querymenuactive');
                this.umlInstanceQueriesDiv.find('.ui-left-menu-query-li').addClass('querymenuinactive');
//                if (queryData !== undefined) {
                this.umlInstanceQueriesDiv.find('#' + queryToHighlightId).removeClass("querymenuinactive");
                this.umlInstanceQueriesDiv.find('#' + queryToHighlightId).addClass("querymenuactive");
//                }
            }
            if (this.umlClassQueriesDiv !== null) {
                this.umlClassQueriesDiv.find('.ui-left-menu-query-li').removeClass('querymenuactive');
                this.umlClassQueriesDiv.find('.ui-left-menu-query-li').addClass('querymenuinactive');
//                if (queryData !== undefined) {
                this.umlClassQueriesDiv.find('#' + queryToHighlightId).removeClass("querymenuinactive");
                this.umlClassQueriesDiv.find('#' + queryToHighlightId).addClass("querymenuactive");
//                }
            }
            //TODO link tabview manager with the accordion div to activate on tab select
            this.accordionDiv.accordion("option", "active", leftAccordionIndex);
        }

        function createLeftMenuDataArray(contextMetaDataFrom, propertyNavigatingTo) {
            var menuArray = [];
            if (contextMetaDataFrom.name !== 'Root') {
                //add a menu item to the context object
                menuArray.push({tumlUri: contextMetaDataFrom.uri, name: contextMetaDataFrom.name, menuIconClass: 'ui-icon-home', aCssClass: ''});
            }

            for (var i = 0; i < contextMetaDataFrom.properties.length; i++) {
                var metaProperty = contextMetaDataFrom.properties[i];
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined && metaProperty.dataTypeEnum !== null) ||
                    metaProperty.onePrimitive ||
                    metaProperty.oneEnumeration ||
                    metaProperty.manyEnumeration ||
                    metaProperty.manyPrimitive ||
                    metaProperty.name == 'id' ||
                    metaProperty.name == 'uri')) {
                    var menuMetaProperty = {active: false};

                    if (propertyNavigatingTo !== undefined && propertyNavigatingTo.qualifiedName == metaProperty.qualifiedName) {
                        //This makes the current active property red in the menu
                        menuMetaProperty.active = true;
                        menuMetaProperty['aCssClass'] = 'ui-state-highlight';
                    } else {
                        menuMetaProperty['aCssClass'] = '';
                    }

                    //add the icon
                    var menuIconClass = 'ui-icon';
                    if (metaProperty.composite) {
                        menuIconClass = menuIconClass + ' ui-icon-umlcomposition';
                    } else {
                        menuIconClass = menuIconClass + ' ui-icon-umlassociation';
                    }
                    menuMetaProperty['tumlUri'] = metaProperty.tumlUri;
                    menuMetaProperty['name'] = metaProperty.name;
                    menuMetaProperty['menuIconClass'] = menuIconClass;
                    if (metaProperty.upper == -1) {
                        menuMetaProperty['multiplicityDisplay'] = '[' + metaProperty.lower + '..*]';
                    } else {
                        menuMetaProperty['multiplicityDisplay'] = '[' + metaProperty.lower + '..' + metaProperty.upper + ']';
                    }
                    menuArray.push(menuMetaProperty);
                }
            }
            ;

            function compare(a, b) {
                if (a.name < b.name) return -1;
                if (a.name > b.name) return 1;
                return 0;
            }

            menuArray.sort(compare);
            return menuArray;
        }

        this.findQueryUrl = function (queryPropertyName) {
            var result = null;
            for (var i = 0; i < this.contextMetaDataFrom.properties.length; i++) {
                var metaProperty = this.contextMetaDataFrom.properties[i];
                if (metaProperty.name == queryPropertyName) {
                    result = metaProperty;
                    break;
                }
            }
            return result;
        }

        $.extend(this, {
            "TumlLeftMenuManagerVersion": "1.0.0",
            "onMenuClick": new Tuml.Event(),
            "onQueryClick": new Tuml.Event()
        });

        init();
    }
})(jQuery);
