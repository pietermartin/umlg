(function () {
    "use strict";

    function getHints(cm, callback, options) {

        var cur = cm.getCursor(null), token = cm.getTokenAt(cur);
        var url;
        if (options.contextClassifierQualifiedName !== undefined && options.contextClassifierQualifiedName !== null) {
            url = '/' + tumlModelName + '/oclCodeInsight?query=' + cm.getValue() + '&contextClassifierQualifiedName=' + options.contextClassifierQualifiedName;
        } else {
            url = '/' + tumlModelName +'/oclCodeInsight?query=' + cm.getValue();
        }

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            success: function (result) {
                var end = 0;
                if (token.string === '.' || token.string === '->') {
                    end = token.string.length;
                }
                callback(
                    {
                        list: result,
                        from: CodeMirror.Pos(cur.line, token.start + end),
                        to: CodeMirror.Pos(cur.line, token.end)
                    }
                );
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
            }
        });

    }

    CodeMirror.registerHelper("hint", "ocl", getHints);
})();
