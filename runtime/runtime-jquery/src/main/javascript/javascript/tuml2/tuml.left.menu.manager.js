(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            LeftMenuManager: LeftMenuManager
        }
    });

    function LeftMenuManager() {

        var self = this;

        function init() {
        }

        function refresh(contextMetaData, contextVertexId) {
            $('.ui-layout-west').children().remove();
            var tabs = $('<div />', {id: 'tab-menu-container', class: 'tab-container' }).appendTo('.ui-layout-west');
            var ul = $('<ul />', {id: 'tabs-menu-ul', class: 'etabs'}).appendTo(tabs);

            //Add in 2 tabs, one for the std view and one for a tree view
            $('<li class="tab"><a href=#std-menu><span>Standard</span></a></li>').appendTo(ul);
            var tabDiv = $('<div />', {id: 'std-menu'}).appendTo(tabs);
            $('<li class="tab"><a href=#tree-menu><span>Tree</span></a></li>').appendTo(ul);
            var tabTree = $('<div />', {id: 'tree-menu'}).text('Check out that tree').appendTo(tabs);
            
            createStdMenu(contextMetaData, tabDiv, contextVertexId)

            $('#tab-menu-container').easytabs({/*updateHash: false,*/ animate: false});
        }

        function createLeftMenuDataArray(contextMetaData) {
            var menuArray = [];
            $.each(contextMetaData.properties, function(index, metaProperty) {
                if (metaProperty.inverseComposite || !((metaProperty.dataTypeEnum !== undefined &&  metaProperty.dataTypeEnum !== null) || metaProperty.onePrimitive || metaProperty.manyPrimitive || metaProperty.name == 'id' || metaProperty.name == 'uri')) {
                    menuArray.push(metaProperty);
                }
            });
            if (contextMetaData.name !== 'Root') {
                //add a menu item to the context object
                menuArray.push({tumlUri: contextMetaData.uri, name: contextMetaData.name});
            }
            return menuArray;
        }

        function createStdMenu(contextMetaData, tabDiv, contextVertexId) {
            var ulMenu = $('<ul />', {class: 'ui-left-menu-link'}).appendTo(tabDiv);
            var menuArray = createLeftMenuDataArray(contextMetaData);
            $.each(menuArray, function(index, value) {
                var property = menuArray[index];
                var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), contextVertexId);
                ulMenu.append('<li>').append(
                    $('<a>', {
                    text: menuArray[index].name,
                    title: menuArray[index].name,
                    href: adjustedUri,
                    click: function() {
                        self.onMenuClick.notify({name: menuArray[index].name, uri: adjustedUri}, null, self);
                        //change_my_url(menuArray[index].name, adjustedUri);
                        //refreshPageTo(adjustedUri);
                        return false;
                    }
                })).append('</li>');
            });
        }

        $.extend(this, {
            "TumlLeftMenuManagerVersion": "1.0.0",
            "onMenuClick": new Tuml.Event(),
            "refresh": refresh
        });
        
        init();
    }
})(jQuery);
