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
    .then(data => console.log("event list: " + data))
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
        accessToken: token,
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body
    })
    .then(response => console.log("create event: " + response.ok))
}

createEvent();

function eventDetail() {
    const token = getCookie('access_token');
    const eventID = "1234"
    const url = new URL('http://localhost:8000/event-detail/');
    url.searchParams.append('accessToken', token);
    url.searchParams.append('eventID', eventID);

    fetch(url, {
        method: 'GET',
    })
    .then(response => response.json())
    .then(data => console.log("event detail: " + data))
    .catch(error => console.error('Error:', error));
}

eventDetail();

function editEvent() {
    const token = getCookie('access_token');
    const url = new URL('http://localhost:8000/edit-event/');
    const body = JSON.stringify({
        title: 'Test Event',
        eventID: '1234',
        description: 'This is a test event.',
        startedAt: '2024/05/01 00:00:00',
        endedAt: '2024/05/01 03:00:00',
        accessToken: token,
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body
    })
    .then(response => console.log("edit event: " + response.ok))
}

editEvent();

function deleteEvent() {
    const token = getCookie('access_token');
    const eventID = "1235"
    const url = new URL('http://localhost:8000/delete-event/');
    const body = JSON.stringify({
        eventID: '1235',
        accessToken: token,
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body
    })
    .then(response => console.log("delete event: " + response.ok))
}

deleteEvent();