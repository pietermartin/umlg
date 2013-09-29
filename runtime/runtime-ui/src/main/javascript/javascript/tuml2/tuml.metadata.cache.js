(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            Metadata: {
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
        var metaDataIdx = {};

        this.add = function (url, metaData) {
            metaDataIdx[url] = metaData;
        }

        this.getFromCache = function (url) {
            return metaDataIdx[url];
        }

        /**
         * This is called for a classes meta data only. i.e. from the meta url
         * @param qualifiedName
         * @param uri
         * @param callback
         */
        this.get = function (qualifiedName, property, callback) {
            var self = this;
            var metaData = metaDataIdx[qualifiedName];
            if (metaData === undefined) {
                $.ajax({
                    url: property.tumlMetaDataUri,
                    type: "OPTIONS",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (result) {
                        self.add(qualifiedName, result);
                        callback.call(this, result);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting meta data ' + property.tumlMetaDataUri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                    }
                });
            } else {
                callback.call(this, metaData);
            }
        }
    }

})
    (jQuery);
