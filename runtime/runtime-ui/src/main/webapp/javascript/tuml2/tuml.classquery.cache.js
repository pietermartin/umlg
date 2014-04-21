(function ($) {
    // register namespace
    $.extend(true, window, {
        Umlg: {
            ClassQuery: {
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
        var classQueryIdx = {};

        this.add = function (url, metaData) {
            classQueryIdx[url] = metaData;
        }

        this.getFromCache = function (url) {
            return classQueryIdx[url];
        }

        this.clear = function () {
            classQueryIdx = {};
        }

        /**
         * This is called for a classes meta data only. i.e. from the meta url
         * @param qualifiedName
         * @param uri
         * @param callback
         */
        this.get = function (url, callback) {
            var self = this;
            var metaData = classQueryIdx[url];
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
                        alert('error getting class query ' + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown);
                    }
                });

            } else {
                callback.call(this, metaData);
            }
        }
    }

})
(jQuery);
