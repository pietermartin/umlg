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

        }

        init();
    }
})(jQuery);
