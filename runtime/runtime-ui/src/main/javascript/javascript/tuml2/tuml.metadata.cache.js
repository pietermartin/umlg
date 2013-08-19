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

        this.getMetaDataIdx = function () {
            return metaDataIdx;
        }

        this.add = function (qualifiedName, metaData) {
            metaDataIdx[qualifiedName] = metaData;
        }

        this.getFromCache = function (qualifiedName) {
            return metaDataIdx[qualifiedName];
        }

        this.get = function (qualifiedName, uri, callback) {
            var self = this;
            var metaData = metaDataIdx[qualifiedName];
            if (metaData === undefined) {
                $.ajax({
                    url: uri,
                    type: "GET",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (result) {
                        self.add(qualifiedName, result);
                        callback.call(this, result);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('error getting ' + uri + '\n textStatus: ' + textStatus + '\n errorThrown: ' + errorThrown)
                    }
                });
            } else {
                callback.call(this, metaData);
            }
        }
    }

})
    (jQuery);
