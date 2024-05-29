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

async function setAccessToken() {
    const hash = window.location.hash.substring(1);
    const params = new URLSearchParams(hash);
    const token = params.get('access_token');

    setCookie("access_token", token, 1, "/");

    try {
        const response = await fetch('http://localhost:8000/set-address/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                accessToken: token
            })
        });
        
        if (response.ok) {
            window.location.replace(`http://${location.host}/calendar`);
        } else {
            window.location.replace(`http://${location.host}/login/`);
        }
    } catch (error) {
        window.location.replace(`http://${location.host}/login`);
    }
}

setAccessToken();
