(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            LeftMenuManager: LeftMenuManager,
            AccordionEnum: {
                PROPERTIES: {index: 0, label: 'Properties', id: 'propertiesAccordion'},
                OPERATIONS: {index: 1, label: 'Operations', id: 'operationAccordion'},
                INSTANCE_QUERIES: {index: 2, label: 'Instance Queries', id: 'instanceQueriesAccordion'},
                CLASS_QUERIES: {index: 3, label: 'Class Queries', id: 'classQueriesAccordion'},
                INSTANCE_GROOVY: {index: 4, label: 'Instance Groovy', id: 'instanceQueriesAccordion'},
                CLASS_GROOVY: {index: 5, label: 'Class Groovy', id: 'classGroovy'},
                DIAGRAMS: {index: 6, label: 'Diagrams', id: 'diagrams'}
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

            leftMenuPaneH3l.text(this.contextMetaDataFrom.name);

            this.tabContainer = leftMenuPaneBody;

            this.setupTabsAndAccordion();
            this.createPropertiesMenu(propertyNavigatingTo);
            this.createDiagramsTreeView();
            if (isUmlgLib && this.contextVertexId !== undefined && this.contextVertexId !== null) {
                this.createInstanceQueryMenu(-1);
                this.createClassQueryMenu(-1);
            }
            var windowHeight = calculateBodyHeight(this) + 45;
            leftMenuPaneBody.height(windowHeight);
        }

        this.setupTabsAndAccordion = function () {

            var leftPanelTabUl = $('<ul />', {id: 'tabContainer-menu-container', class: 'nav nav-tabs'}).appendTo(this.tabContainer);
            var tabDiv = $('<div />', {class: "tab-content"}).appendTo(this.tabContainer);

            //Do not bother with tabindex as the components sets the first one to 0 and the rest to -1 automatically
            var standardTabTemplate = "<li class='active'><a href='#Standard' data-toggle='tab'>Standard</a></li>";
            var standardLi = $(standardTabTemplate);
            leftPanelTabUl.append(standardLi);
            var treeTabTemplate = "<li><a href='#Tree' data-toggle='tab'>Tree</a></li>";
            var treeLi = $(treeTabTemplate);
            leftPanelTabUl.append(treeLi);

            var standardMenuDiv = $('<div />', {id: "Standard", class: "tab-pane active"});
            tabDiv.append(standardMenuDiv);

            var treeMenuDiv = $('<div />', {id: "Tree", class: "tab-pane"});
            tabDiv.append(treeMenuDiv);

            leftPanelTabUl.find('a').click(function (e) {
                e.preventDefault()
                $(this).tab('show')
            })
            leftPanelTabUl.find("a:first").tab('show')


            this.accordionDiv = $('<div />', {id: 'accordion', class: 'panel-group'}).appendTo(standardMenuDiv);

            this.umlPropertiesDiv = addAccordionMenu(this.accordionDiv, true, Tuml.AccordionEnum.PROPERTIES.label, Tuml.AccordionEnum.PROPERTIES.id);
            this.umlOperationsDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.OPERATIONS.label, Tuml.AccordionEnum.OPERATIONS.id);
            this.umlDiagramDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.DIAGRAMS.label, Tuml.AccordionEnum.DIAGRAMS.id);
            if (isUmlgLib && this.contextVertexId !== undefined && this.contextVertexId !== null) {
                this.umlInstanceQueriesDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.INSTANCE_QUERIES.label, Tuml.AccordionEnum.INSTANCE_QUERIES.id);
                this.umlClassQueriesDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.CLASS_QUERIES.label, Tuml.AccordionEnum.CLASS_QUERIES.id);
                this.umlInstanceGroovyDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.INSTANCE_GROOVY.label, Tuml.AccordionEnum.INSTANCE_GROOVY.id);
                this.umlClassGroovyDiv = addAccordionMenu(this.accordionDiv, false, Tuml.AccordionEnum.CLASS_GROOVY.label, Tuml.AccordionEnum.CLASS_GROOVY.id);
            }
        }

        function addAccordionMenu(accordionDiv, open, label, id) {
            //PROPERTIES
            //The clickable heading part of the properties accordion
            var propertiesAccordion = $('<div />', {class: 'panel panel-default'}).appendTo(accordionDiv);
            var propertiesAccordionHeading = $('<div />', {class: 'panel-heading'}).appendTo(propertiesAccordion);
            var propertiesH4 = $('<h4 />', {class: 'panel-title'}).appendTo(propertiesAccordionHeading);
            $('<a data-toggle="collapse" data-parent="#accordion" class="accordion-toggle" href="#' + id + '" />').text(label).appendTo(propertiesH4);
            //The context heading part of the properties accordion
            var propertiesDiv = $('<div />', {id: id, class: 'panel-collapse collapse ' + (open ? 'in' : '')}).appendTo(propertiesAccordion);
            var divClassBody = $('<div />', {class: 'umlg-leftmenu-panel-body panel-body'}).appendTo(propertiesDiv);
            return divClassBody;
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
                case Tuml.AccordionEnum.DIAGRAMS.index:
                    console.log('DIAGRAM');
                    break;
                default:
                    throw 'Unknown active accordion!';
                    break;
            }

        }

        this.createDiagramsTreeView = function () {
            var self = this;
            this.diagramsTreeViewDiv = $('<div />', {id: 'diagramTreeView'}).appendTo(this.umlDiagramDiv);

            $.ajax({
                url: '/restAndJson/diagramPackages',
                type: 'GET',
                dataType: "json",
                contentType: "application/json",
                success: function (result) {
                    $(function() {
                        self.diagramsTreeViewDiv.tree({
                            data: result,
                            selectable: true
                        });

                        self.diagramsTreeViewDiv.bind(
                            'tree.dblclick',
                            function(e) {

                                event.preventDefault();
                                event.stopImmediatePropagation();
                                if (e.node.children.length == 0) {
                                    self.onDiagramClick.notify(e.node, null, self);
                                }
                            }
                        );
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                }
            });

        }

        this.createPropertiesMenu = function (propertyNavigatingTo) {
            var dropDownDiv = $('<div />', {class: 'dropdown'}).appendTo(this.umlPropertiesDiv);
            $('<a href="#" class="sr-only dropdown-toggle" data-toggle="dropdown">Users <b class="caret"></b></a>').appendTo(dropDownDiv);
            var ulMenu = $('<ul id="propertiesMenu" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" />').appendTo(dropDownDiv);
            var menuArray = createLeftMenuDataArray(this.contextMetaDataFrom, propertyNavigatingTo);

            for (var i = 0; i < menuArray.length; i++) {
                var value = menuArray[i];
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), encodeURIComponent(this.contextVertexId));
                adjustedUri = addUiToUrl(adjustedUri)
                var li = $('<li />', {role: 'presentation'}).appendTo(ulMenu);
                li.data("contextData", {name: value.name, uri: adjustedUri});
                //Set the active link
                if (value.active) {
                    li.addClass('active');
                }
                var a = $('<a />', {title: value.name, role: 'menuitem', tabindex: -1, href: adjustedUri}).appendTo(li);
                a.on('click', function (e) {
                    var link = $(e.target);
                    link.parent().addClass('active');
                    var contextData = link.parent().data("contextData");
                    self.onMenuClick.notify({name: contextData.name, uri: removeUiFromUrl(contextData.uri)}, null, self);
                    e.preventDefault();
                    e.stopImmediatePropagation();
                });
                $('<i class="umlg-icon ' + value.menuIconClass + '"></i>').appendTo(a);
                if (value.multiplicityDisplay !== undefined) {
                    a.append(' ' + value.name + ' ' + value.multiplicityDisplay);
                } else {
                    a.append(' ' + value.name);
                }
            }
            //This is for enter keystroke on the menu
//            ulMenu.menu({
//                select: function (e, ui) {
//                    var contextData = ui.item.data("contextData");
//                    self.onMenuClick.notify({name: contextData.name, uri: removeUiFromUrl(contextData.uri)}, null, self);
//                    e.preventDefault();
//                }
//            });
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
            var dropDownDiv = $('<div />', {class: 'dropdown'}).appendTo(queryDiv);
            $('<a href="#" class="sr-only dropdown-toggle" data-toggle="dropdown">Users <b class="caret"></b></a>').appendTo(dropDownDiv);
            var ulMenu;
            if (isInstanceQuery) {
                ulMenu = $('<ul id="instanceQueryMenu" class="dropdown-menu" role="menu" aria-labelledby="dropdownQueryMenu1" />').appendTo(dropDownDiv);

            } else {
                ulMenu = $('<ul id="classQueryMenu" class="dropdown-menu" role="menu" aria-labelledby="dropdownQueryMenu1" />').appendTo(dropDownDiv);
            }
            for (var i = 0; i < queryArray.length; i++) {
                var value = queryArray[i];
                var adjustedUri = value.tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), this.contextVertexId);
                var li = $('<li />', {id: value.queryId, role: 'presentation'}).appendTo(ulMenu);

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
                        a.parent().parent().children('li.active').removeClass('active');
                        a.parent().addClass('active');
                        self.onQueryClick.notify(query, null, self);
                        a.focus();
                        e.preventDefault();
                        e.stopImmediatePropagation();
                    }
                );

                $('<i />', {class: 'fa fa-bolt'}).appendTo(a);
                a.append(' ' + value.name);
            }
//            ulMenu.menu({
//                select: function (e, ui) {
//                    var a = ui.item.find('a');
//                    var contextData = ui.item.data("contextData");
//                    var query = {
//                        post: false,
//                        tumlUri: contextData.tumlUri,
//                        oclExecuteUri: contextData.oclExecuteUri,
//                        qualifiedName: contextData.qualifiedName,
//                        name: contextData._name,
//                        queryEnum: contextData.queryEnum,
//                        queryString: contextData.queryString,
//                        queryType: contextData.queryType,
//                        id: contextData.queryId
//                    };
//                    self.onQueryClick.notify(query, null, self);
//                    a.focus();
//                    e.preventDefault();
//                    e.stopImmediatePropagation();
//                }
//            });
        }

        this.createInstanceQueryMenu = function (queryId) {
            var self = this;
            this.queryToHighlightId = queryId;
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

        this.continueCreateInstanceQueryMenu = function (tumlUri, result) {
            self.createQueryMenu(self.umlInstanceQueriesDiv, true, result);
            if (self.queryToHighlightId !== undefined && self.queryToHighlightId != -1) {
                self.refreshQueryMenuCss(self.queryToHighlightId, Tuml.AccordionEnum.INSTANCE_QUERIES.index);
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

        this.continueCreateClassQueryMenu = function (tumlUri, result) {
            self.createQueryMenu(self.umlClassQueriesDiv, false, result);
            if (self.queryToHighlightId !== -1) {
                self.refreshQueryMenuCss(self.queryToHighlightId, Tuml.AccordionEnum.CLASS_QUERIES.index);

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
//            this.accordionDiv.accordion("option", "active", 2);
        }

        this.refreshClassQuery = function (queryId) {
            this.umlClassQueriesDiv.children().remove();
            this.createClassQueryMenu(queryId);
//            this.accordionDiv.accordion("option", "active", 3);
        }

        this.refreshQueryMenuCss = function (/*queryData, */queryToHighlightId, leftAccordionIndex) {

            if (this.umlInstanceQueriesDiv !== null) {
                //Change the css active property
                this.umlInstanceQueriesDiv.find('li.active').removeClass('active');
                this.umlInstanceQueriesDiv.find('#' + queryToHighlightId).addClass('active');
            }
            if (this.umlClassQueriesDiv !== null) {
                //Change the css active property
                this.umlClassQueriesDiv.find('li.active').removeClass('active');
                this.umlClassQueriesDiv.find('#' + queryToHighlightId).addClass('active');
            }

            if (leftAccordionIndex != -1) {
                //First close all tabs
                $('#accordion').on('show.bs.collapse', function () {
                    $('#accordion .in').collapse('hide');
                });

                //Open the relevant accordion
                if (leftAccordionIndex == Tuml.AccordionEnum.PROPERTIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var propertiesAccordion = this.umlPropertiesDiv.parent();
                    if (!propertiesAccordion.hasClass('in')) {
                        propertiesAccordion.collapse('show');
                    }
                } else if (leftAccordionIndex == Tuml.AccordionEnum.OPERATIONS.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var operationsAccordion = this.umlOperationsDiv.parent();
                    if (!operationsAccordion.hasClass('in')) {
                        operationsAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.INSTANCE_QUERIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var instanceQueriesAccordion = this.umlInstanceQueriesDiv.parent();
                    if (!instanceQueriesAccordion.hasClass('in')) {
                        instanceQueriesAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.CLASS_QUERIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var classQueriesAccordion = this.umlClassQueriesDiv.parent();
                    if (!classQueriesAccordion.hasClass('in')) {
                        classQueriesAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.CLASS_GROOVY.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var classGroovyAccordion = this.umlClassGroovyDiv.parent();
                    if (!classGroovyAccordion.hasClass('in')) {
                        classGroovyAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.INSTANCE_GROOVY.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var instanceGroovyAccordion = this.umlInstanceGroovyDiv.parent();
                    if (!instanceGroovyAccordion.hasClass('in')) {
                        instanceGroovyAccordion.collapse('show');
                    }

                } else {
                    throw 'Unknown accordion index!'
                }
            }

        }

        this.refreshDiagramCss = function (/*queryData, */diagramNameHighlightId, leftAccordionIndex) {

            if (leftAccordionIndex != -1) {
                //First close all tabs
                $('#accordion').on('show.bs.collapse', function () {
                    $('#accordion .in').collapse('hide');
                });

                //Open the relevant accordion
                if (leftAccordionIndex == Tuml.AccordionEnum.PROPERTIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var propertiesAccordion = this.umlPropertiesDiv.parent();
                    if (!propertiesAccordion.hasClass('in')) {
                        propertiesAccordion.collapse('show');
                    }
                } else if (leftAccordionIndex == Tuml.AccordionEnum.OPERATIONS.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var operationsAccordion = this.umlOperationsDiv.parent();
                    if (!operationsAccordion.hasClass('in')) {
                        operationsAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.INSTANCE_QUERIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var instanceQueriesAccordion = this.umlInstanceQueriesDiv.parent();
                    if (!instanceQueriesAccordion.hasClass('in')) {
                        instanceQueriesAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.CLASS_QUERIES.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var classQueriesAccordion = this.umlClassQueriesDiv.parent();
                    if (!classQueriesAccordion.hasClass('in')) {
                        classQueriesAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.CLASS_GROOVY.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var classGroovyAccordion = this.umlClassGroovyDiv.parent();
                    if (!classGroovyAccordion.hasClass('in')) {
                        classGroovyAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.INSTANCE_GROOVY.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var instanceGroovyAccordion = this.umlInstanceGroovyDiv.parent();
                    if (!instanceGroovyAccordion.hasClass('in')) {
                        instanceGroovyAccordion.collapse('show');
                    }

                } else if (leftAccordionIndex == Tuml.AccordionEnum.DIAGRAMS.index) {
                    //Check if it is already open, if so do not call show as that closes it, eish
                    var instanceDiagramAccordion = this.umlDiagramDiv.parent();
                    if (!instanceDiagramAccordion.hasClass('in')) {
                        instanceDiagramAccordion.collapse('show');
                    }
                    var node = this.diagramsTreeViewDiv.tree('getNodeById', diagramNameHighlightId);
                    this.diagramsTreeViewDiv.tree('selectNode', node);
                } else {
                    throw 'Unknown accordion index!'
                }
            }

        }

        function createLeftMenuDataArray(contextMetaDataFrom, propertyNavigatingTo) {
            var menuArray = [];
            if (contextMetaDataFrom.name !== tumlModelName) {
                //add a menu item to the context object
                menuArray.push({tumlUri: contextMetaDataFrom.uri, name: contextMetaDataFrom.name, menuIconClass: 'fa fa-circle', aCssClass: ''});
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
            "onQueryClick": new Tuml.Event(),
            "onDiagramClick": new Tuml.Event()
        });

        init();
    }
})(jQuery);
