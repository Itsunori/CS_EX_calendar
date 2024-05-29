function oauthSignIn() {
    var oauth2Endpoint = 'https://accounts.google.com/o/oauth2/v2/auth';
  
    var form = document.createElement('form');
    form.setAttribute('method', 'GET');
    form.setAttribute('action', oauth2Endpoint);
  
    var params = {
        'client_id': '573497940872-il4qncfflllmfoedbpg5bv7l0hiln546.apps.googleusercontent.com',
        'redirect_uri': `http://${location.host}/callback/`,
        'response_type': 'token',
        'scope': 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile',
        'include_granted_scopes': 'true',
        'state': 'pass-through value'
    };
  
    for (var p in params) {
        if (params.hasOwnProperty(p)) {
            var input = document.createElement('input');
            input.setAttribute('type', 'hidden');
            input.setAttribute('name', p);
            input.setAttribute('value', params[p]);
            form.appendChild(input);
        }
    }
  
    document.body.appendChild(form);
    form.submit();
}
