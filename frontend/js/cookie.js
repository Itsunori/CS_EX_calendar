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

setCookie("access_token")

