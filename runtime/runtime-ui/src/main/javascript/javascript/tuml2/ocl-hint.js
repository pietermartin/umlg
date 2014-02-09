(function () {
    "use strict";

    function getHints(cm, callback) {
        var cur = cm.getCursor(null), token = cm.getTokenAt(cur);

        $.ajax({
            url: postUri,
            type: AJAX_TYPE,
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(overloadedPostData),
            success: function (result) {
                callback(
                    {
                        list: result,
                        from: CodeMirror.Pos(cur.line, token.start + 1),
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
