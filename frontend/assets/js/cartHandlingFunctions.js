function showNotification(message) {
    // Save the message in sessionStorage
    sessionStorage.setItem('notificationMessage', message);

    // Display the notification
    displayNotification(message);
}

function displayNotification(message) {
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

function addToCart(itemId, buyQuantity, action) {
                                                // Make the asynchronous request to the server
                                                $.ajax({
                                                    url: 'addCartItem', // Servlet URL
                                                    type: 'POST', headers:{'ngrok-skip-browser-warning':'abc'},
                                                    data: {
                                                        id: itemId,
                                                        quantity: buyQuantity
                                                    },
                                                    success: function(response) {
                                                        if(response.succeeded === "1"){
                                                             //$('#err-' + itemId).text("quantity out of stock!");
                                                             showStockError("Added Successfully!");
                                                         }
                                                        else{
                                                            showStockError("quantity out of stock!");
                                                        }
                                                        
                                                    },
                                                    error: function(response) {
                                                        if(response.status == 401){
                                                            window.location.href = window.location.origin + "/ecommerce/web/auth/login.jsp"
                                                        }
                                                        else{
                                                            showNotification("Error adding item!");
                                                        }

                                                    },
                                                });
                                            }
                                            function showStockError(msg) {
                                                var popup = document.getElementById("stock-error");
                                                popup.innerHTML = msg;
                                                popup.style.display = "block"; // Show the popup

                                                // Hide the popup after 3 seconds (3000 milliseconds)
                                                setTimeout(function() {
                                                    popup.style.display = "none"; // Hide the popup
                                                }, 3000);
                                                }