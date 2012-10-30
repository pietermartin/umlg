(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlOneViewManager: TumlOneViewManager
        }
    });

    function TumlOneViewManager() {

        var self = this;
        var exist = false;

        function init() {
        }

        function refresh(tumlUri, result) {
            if (result instanceof Array) {
                response = result[0];
            } else {
                response = result;
            }
            metaForData = response.meta[1];
            var tumlTabViewManager = new Tuml.TumlTabViewManager(false, tumlUri, response.meta[0].qualifiedName, metaForData.name);
            tumlTabViewManager.onPutOneSuccess.subscribe(function(e, args) {
                console.log('TumlOneViewManager onPutOneSuccess fired');
                //On a put success ensure that the one becomes the context
                var data;
                if (args.data instanceof Array) {
                    data = result[0];
                } else {
                    data = result;
                }
                var uri = data.meta[1].uri;
                var adjustedUri = uri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), data.data.id);
                self.onPutOneSuccess.notify({name: "unused", uri: adjustedUri}, e, self);
            });
            tumlTabViewManager.onPutOneFailure.subscribe(function(e, args) {
                console.log('TumlOneViewManager onPutOneFailure fired');
                alert('fail');
            });
            //Only create the tab if it does not exist. This function is called initially and after an update (PUT)
            var tabLi = $('#tabsul').find('li');
            if (tabLi.length == 0 || tabLi === undefined || tabLi == null) {
                tumlTabViewManager.createTab(result);
            }
            tumlTabViewManager.createOne(response.data, metaForData);
        }

        function clear() {
        }

        //Public api
        $.extend(this, {
            "TumlOneViewManagerVersion": "1.0.0",
            "onPutOneSuccess": new Tuml.Event(),
            "onPutOneFailure": new Tuml.Event(),
            "refresh": refresh,
            "clear": clear
        });

        init();
    }

})(jQuery);
