function setAccessToken() {
    const hash = window.location.hash.substring(1);
    const params = new URLSearchParams(hash);
    const token = params.get('access_token');

    localStorage.setItem('access_token', token);
    window.location.replace("http://localhost:3009/login/");
}

setAccessToken();