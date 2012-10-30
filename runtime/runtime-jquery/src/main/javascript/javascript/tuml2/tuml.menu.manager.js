(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            MenuManager: MenuManager
        }
    });

    function MenuManager() {

        function init() {
            //Create the meny
            var usersMenuUl = $('<ul />', {id: 'jsddm'}).appendTo('.ui-layout-north');
            var usersLi = $('<li />').appendTo(usersMenuUl);
            $('<a />', {href: '#'}).text('Users').appendTo(usersLi);
            var userAdminUl = $('<ul />').appendTo(usersLi);
            $('<li><a href="#">Admin1</a></li>').appendTo(userAdminUl);
            $('<li><a href="#">Admin2</a></li>').appendTo(userAdminUl);

            
            var searchMenuUl = $('<ul />', {id: 'jsddm'}).appendTo('.ui-layout-north');
            var searchLi = $('<li />').appendTo(searchMenuUl);
            $('<a />', {href: '#'}).text('Search').appendTo(searchLi);
            var searchUl = $('<ul />').appendTo(searchLi);
            $('<li><a href="#">Search1</a></li>').appendTo(searchUl);
            $('<li><a href="#">Search2</a></li>').appendTo(searchUl);

            $('#jsddm > li').bind('mouseover', jsddm_open);
            $('#jsddm > li').bind('mouseout',  jsddm_timer);
            document.onclick = jsddm_close;
        }

        var timeout    = 200;
        var closetimer = 0;
        var ddmenuitem = 0;

        function jsddm_open() {  
            jsddm_canceltimer();
            jsddm_close();
            ddmenuitem = $(this).find('ul').css('visibility', 'visible');
        }

        function jsddm_close() {  
            if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');
        }

        function jsddm_timer() {  
            closetimer = window.setTimeout(jsddm_close, timeout);
        }

        function jsddm_canceltimer() {  
            if(closetimer) {  
                window.clearTimeout(closetimer);
                closetimer = null;
            }
        }

        init();
    }
})(jQuery);
