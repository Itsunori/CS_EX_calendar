function setCookie(name, value, days, path = '/', domain) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    let cookie = name + "=" + (value || "") + expires + "; path=" + path;
    if (domain) {
        cookie += "; domain=" + domain;
    }
    document.cookie = cookie;
}

function setAccessToken() {
    const hash = window.location.hash.substring(1);
    const params = new URLSearchParams(hash);
    const token = params.get('access_token');

    setCookie("access_token", token, 1, "/")
}

setAccessToken();
