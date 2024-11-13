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
function handleAddItem(num, id, redirect){
    var quantity;
    var itemId;
    if(num){
        quantity = num;
        itemId = id;
    }
    else{
        var result = document.getElementById('sst');
        quantity = parseInt(result.value);
        // Use URLSearchParams to parse the query string
        const params = new URLSearchParams(window.location.search);
        // Get the 'id' parameter
        itemId = parseInt(params.get('id'));
    }



    var currentQuantity = quantity;
    var token = localStorage.getItem("token");
    if(token == null){
        var cartItems = localStorage.getItem("cart");
        if(!cartItems)cartItems="[]";
        cartItems = JSON.parse(cartItems);
        let item = cartItems.find(item => item.product.id === itemId);
        console.log(cartItems);
        console.log(item);
        if(!item){
            item={
                "quantity":quantity,
                "product":{
                    "id":itemId,
                    "price":0,
                    "name":"",
                    "image":"",
                }};
            cartItems=[...cartItems,item];
        }
        else {
            currentQuantity = quantity   + item.quantity;
        }
        $.ajax({
                   url: `https://improved-ghastly-midge.ngrok-free.app/api/products/${itemId}/stock`, // Servlet URL/
                   type: 'GET',
                    headers:{'ngrok-skip-browser-warning':'abc'},
                   success: function(response) {
                       if(response.quantity < currentQuantity){
                           //$('#err-' + itemId).text("quantity out of stock!");
                           showStockError();
                       }
                       else{
                           item.quantity = currentQuantity;
                           item.product.price = response.price;
                           item.product.name = response.name;
                           item.product.image = response.image;
                           localStorage.setItem("cart", JSON.stringify(cartItems));
                           showNotification("Item added to cart");
                           if(!redirect)location.href = location.origin + "/web/cart.html";
                       }
                   },
                   error: function() {
                       showStockError();
                   }
               });

    }
    else {
        // Make the asynchronous request to the server
        $.ajax({
                   url: `https://improved-ghastly-midge.ngrok-free.app/api/v1/carts/add`, // Servlet URL
                   type: 'POST',
                   data: JSON.stringify({
                                            productId: itemId,
                                            quantity: currentQuantity
                                        }),
                   headers: {
                       Authorization: "Bearer "+token,
                       'Content-Type': "application/json",
                       'ngrok-skip-browser-warning':'abc'
                   },
                   success: function(response) {
                       console.log("heeeere" ,response);
                       showNotification("Item added to cart");
                       if(!redirect)location.href = location.origin + "/web/cart.html";
                   },
                   error: function() {
                       showStockError();
                   }
               });
    }
}
function showStockError() {
    var popup = document.getElementById("stock-error");
    popup.style.display = "block"; // Show the popup

    // Hide the popup after 3 seconds (3000 milliseconds)
    setTimeout(function() {
        popup.style.display = "none"; // Hide the popup
    }, 3000);
}