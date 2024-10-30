export function showNotification(message) {
    // Save the message in sessionStorage
    sessionStorage.setItem('notificationMessage', message);

    // Display the notification
    displayNotification(message);
}

export function displayNotification(message) {
    // Check if the banner already exists, if not, create one
    let banner = document.querySelector('.notification-banner');
    if (!banner) {
        banner = document.createElement('div');
        banner.className = 'notification-banner';
        document.body.appendChild(banner);
    }

    // Set the message and show the banner
    banner.textContent = message;
    banner.classList.add('show');

    // Hide the banner after 2 seconds
    setTimeout(() => {
        banner.classList.remove('show');
        sessionStorage.removeItem('notificationMessage'); // Clear the message after it fades out
    }, 4000);
}

// Check for a notification message on page load
window.addEventListener('load', () => {
    const message = sessionStorage.getItem('notificationMessage');
    if (message) {
        displayNotification(message);
    }
});