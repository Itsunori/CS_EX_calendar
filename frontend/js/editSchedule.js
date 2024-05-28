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

function sendEvent(event) {
    event.preventDefault();

    const token = getCookie('access_token');
    const url = new URL('http://localhost:8000/edit-event/');
    const formData = new FormData(event.target);
    const body = JSON.stringify({
        title: formData.get('title'),
        eventID: formData.get('id'),
        description: formData.get('description'),
        startedAt: formData.get('start_datetime').replaceAll('-','/').replace('T', ' ') + ':00',
        endedAt: formData.get('end_datetime').replaceAll('-','/').replace('T', ' ') + ':00',
        accessToken: token,
    });
    console.log(body);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body
    })
    .then(response => {
        console.log("edit event: " + response.ok);
        if (response.ok) {
            const scheduleLink = document.getElementById('schedule-link').href;
            window.location.href = scheduleLink;
        } else {
            submitButton.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        submitButton.disabled = false;
    });
}