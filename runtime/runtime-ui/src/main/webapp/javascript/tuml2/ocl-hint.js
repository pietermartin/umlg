(function () {
    "use strict";

    var timeout;

    function getHints(cm, callback, options) {
        if (timeout) clearTimeout(timeout);
        timeout = setTimeout(function () {
            var cur = cm.getCursor(null), token = cm.getTokenAt(cur);
            var url;
            if (options.contextClassifierQualifiedName !== undefined && options.contextClassifierQualifiedName !== null) {
                url = '/' + tumlModelName + '/oclCodeInsight?query=' + cm.getValue() + '&contextClassifierQualifiedName=' + options.contextClassifierQualifiedName;
            } else {
                url = '/' + tumlModelName + '/oclCodeInsight?query=' + cm.getValue();
            }
            $.ajax({
                url: url,
                type: "GET",
                dataType: "json",
                contentType: "application/json",
                success: function (result) {
                    var end = 0;
                    if (token.string === '.' || token.string === '->' || token.string === ':') {
                        end = token.string.length;
                    }
                    var completionObject = {
                        list: result,
                        from: CodeMirror.Pos(cur.line, token.start + end),
                        to: CodeMirror.Pos(cur.line, token.end)
                    }
                    callback(completionObject);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#serverErrorMsg').addClass('server-error-msg').html(jqXHR.responseText);
                }
            });

            clearTimeout(timeout);
            timeout = undefined;
            console.log('aaaa');

        }, 150);

        console.log(timeout);
    }

    CodeMirror.registerHelper("hint", "ocl", getHints);
})();
