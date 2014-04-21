(function ($) {
    // register namespace
    $.extend(true, window, {
        Umlg: {
            RootQuery: {
                Cache: new Cache()
            }
        }
    });

    /**
     * This caches the meta data for a class.
     * it call ClassNameMetaDataServerResourceImpl which returns the meta data.
     * The meta.to and meta.from are identical
     * @constructor
     */
    function Cache() {
        var rootQueryIdx = {};

        this.add = function (url, metaData) {
            rootQueryIdx[url] = metaData;
        }

        this.getFromCache = function (url) {
            return rootQueryIdx[url];
        }

        this.clear = function () {
            rootQueryIdx = {};
        }

        /**
         * This is called for a classes meta data only. i.e. from the meta url
         * @param qualifiedName
         * @param uri
         * @param callback
         */
        this.get = function (url, callback) {
            var self = this;
            var metaData = rootQueryIdx[url];
            if (metaData === undefined) {

                $.ajax({
                    url: url,
                    type: 'GET',
                    dataType: "json",
                    contentType: "application/json",
                    success: function (result) {
                        $(function () {
                            self.add(url, result);
                            callback.call(this, result);
                        });

                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting root query ' + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown);
                    }
                });

            } else {
                callback.call(this, metaData);
            }
        }
    }

})
(jQuery);
