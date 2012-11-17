function retrieveVertexId(url) {
    var urlId = url.match(/\/\d+/);
    if (urlId != null) {
        return urlId[0].match(/\d+/);
    } else {
        return urlId;
    }
}
