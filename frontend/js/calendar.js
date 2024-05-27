function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

window.onload = function() {
    const token = getCookie('access_token');
    const url = new URL('http://localhost:8000/event-list/');
    url.searchParams.append('accessToken', token);

    fetch(url, {
        method: 'GET',
    })
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
}

function createEvent() {
    const token = getCookie('access_token');
    const url = new URL('http://localhost:8000/create-event/');
    url.searchParams.append('accessToken', token);
    const body = JSON.stringify({
        title: 'Test Event',
        description: 'This is a test event.',
        startedAt: '2024/05/01 00:00:00',
        endedAt: '2024/05/01 03:00:00',
    });

    fetch(url, {
        method: 'POST',
    })
    .then(response => console.log(response.ok))
}