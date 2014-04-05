(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            MenuManager: MenuManager
        }
    });

    function MenuManager() {

        /*
             <ul class="nav navbar-nav navbar-right">
                 <li class="dropdown">
                     <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                     <ul class="dropdown-menu">
                         <li><a href="#">Action</a></li>
                         <li><a href="#">Another action</a></li>
                         <li><a href="#">Something else here</a></li>
                         <li><a href="#">Separated link</a></li>
                     </ul>
                 </li>
             </ul>
         */

        function init() {
            var ulClassNavbarRight = $('<ul />', {class: 'nav navbar-nav navbar-right'}).appendTo('.collapse.navbar-collapse');


            var liUMLGMenu = $('<li />', {class: 'dropdown'}).appendTo(ulClassNavbarRight);
            $('<a href="#" class="dropdown-toggle" data-toggle="dropdown">UMLG <b class="caret"></b></a>').appendTo(liUMLGMenu);
            var ulUMLGMenuHolder = $('<ul />', {class: 'dropdown-menu'}).appendTo(liUMLGMenu);
            var liDocumentationMenu = $('<li />').appendTo(ulUMLGMenuHolder);
            $('<a/>', {href: 'http://www.umlg.org/documentation.html', target: '_blank'}).text('Documentation').appendTo(liDocumentationMenu);
            var liSourceMenu = $('<li />').appendTo(ulUMLGMenuHolder);
            $('<a/>', {href: 'https://github.com/pietermartin/umlg', target: '_blank'}).text('Source').appendTo(liSourceMenu);

//            var liSearchMenu = $('<li />', {class: 'dropdown'}).appendTo(ulClassNavbarRight);
//            $('<a href="#" class="dropdown-toggle" data-toggle="dropdown">Search <b class="caret"></b></a>').appendTo(liSearchMenu);
//            var ulSearchMenuHolder = $('<ul />', {class: 'dropdown-menu'}).appendTo(liSearchMenu);
//            var liSearch1Menu = $('<li />').appendTo(ulSearchMenuHolder);
//            var linkSearch1Menu = $('<a/>', {href: '#'}).text('aaaaaa').appendTo(liSearch1Menu);
//            var liSearch2Menu = $('<li />').appendTo(ulSearchMenuHolder);
//            var linkSearch2Menu = $('<a/>', {href: '#'}).text('bbbbbb').appendTo(liSearch2Menu);

//            $('.embossed').remove();
//            var p = $('<div />', {class: 'embossed'});
//            $('<span />').text("UmlG").appendTo('.collapse.navbar-collapse');
//            p.appendTo(contextRoot);
        }

//        function init() {
//            //Create the menu
//            var usersMenuUl = $('<ul />', {id: 'jsddm'}).appendTo('.ui-layout-north');
//            var usersLi = $('<li />').appendTo(usersMenuUl);
//            $('<a />', {href: '#'}).text('Users').appendTo(usersLi);
//            var userAdminUl = $('<ul />').appendTo(usersLi);
//            $('<li><a href="#">Admin1</a></li>').appendTo(userAdminUl);
//            $('<li><a href="#">Admin2</a></li>').appendTo(userAdminUl);
//
//
//            var searchMenuUl = $('<ul />', {id: 'jsddm'}).appendTo('.ui-layout-north');
//            var searchLi = $('<li />').appendTo(searchMenuUl);
//            $('<a />', {href: '#'}).text('Search').appendTo(searchLi);
//            var searchUl = $('<ul />').appendTo(searchLi);
//            $('<li><a href="#">Search1</a></li>').appendTo(searchUl);
//            $('<li><a href="#">Search2</a></li>').appendTo(searchUl);
//
//            $('#jsddm > li').bind('mouseover', jsddm_open);
//            $('#jsddm > li').bind('mouseout',  jsddm_timer);
//            document.onclick = jsddm_close;
//        }
//
//        var timeout    = 200;
//        var closetimer = 0;
//        var ddmenuitem = 0;
//
//        function jsddm_open() {
//            jsddm_canceltimer();
//            jsddm_close();
//            ddmenuitem = $(this).find('ul').css('visibility', 'visible');
//        }
//
//        function jsddm_close() {
//            if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');
//        }
//
//        function jsddm_timer() {
//            closetimer = window.setTimeout(jsddm_close, timeout);
//        }
//
//        function jsddm_canceltimer() {
//            if(closetimer) {
//                window.clearTimeout(closetimer);
//                closetimer = null;
//            }
//        }

        init();
    }
})(jQuery);
