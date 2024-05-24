function checkAccessToken() {
    const token = localStorage.getItem('access_token');
    console.log(token);
    if (!token) {
        window.location.replace("http://localhost:3009/login/");
    }
}

checkAccessToken();