import {showNotification} from "./popUp";

function loadCartItems() {
    var token = localStorage.getItem("token");
    if(token == null){
        var cartItems = localStorage.getItem("cart");
        if(cartItems == null) cartItems = [];
        else cartItems = JSON.parse(cartItems);
        if(cartItems.length>0){
            // Create a new button element
            const button = document.createElement("button");
            button.id = "checkOutButton";
            button.type = "button";
            button.className = "primary-btn";
            button.innerText = "Proceed To Checkout";
            button.onclick = goToLogin;  // Directly assign the function

            // Append the button to the checkout form
            document.getElementById("checkoutForm").appendChild(button);
        }
        else{
            // Create a new input element
            const input = document.createElement("input");
            input.id = "checkOutButton";
            input.type = "submit";
            input.className = "gray_btn";
            input.value = "Proceed to checkout";
            input.disabled = true;  // Set the disabled attribute

            // Append the input to the checkout form
            document.getElementById("checkoutForm").appendChild(input);

        }
        propagateCartItems(cartItems);
        calculateTotalPrice();
    }
    else{
        fetch('http://localhost:9002/api/v1/carts',{
            method:"GET",
            headers:{
                Authorization: "Bearer "+token,
                'Content-Type': "application/json",
                'ngrok-skip-browser-warning':'abc'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(cartItems => {
                // cartItems = [{
                //     product:{
                //         image:"image.com",
                //         name:"Asus",
                //         id:1,
                //         price:500
                //     },
                //     quantity:10}
                // ];
                if(cartItems.length>0){
                    // Create a new button element
                    const button = document.createElement("button");
                    button.id = "checkOutButton";
                    button.type = "button";
                    button.className = "primary-btn";
                    button.innerText = "Proceed To Checkout";
                    button.onclick = viewOrderDetails;  // Directly assign the function

                    // Append the button to the checkout form
                    document.getElementById("checkoutForm").appendChild(button);
                }
                else{
                    // Create a new input element
                    const input = document.createElement("input");
                    input.id = "checkOutButton";
                    input.type = "submit";
                    input.className = "gray_btn";
                    input.value = "Proceed to checkout";
                    input.disabled = true;  // Set the disabled attribute

                    // Append the input to the checkout form
                    document.getElementById("checkoutForm").appendChild(input);

                }

                console.log(cartItems);
                propagateCartItems(cartItems);
                calculateTotalPrice();
            })
            .catch(error => console.error('Error loading cart items:', error));
    }

}

function propagateCartItems(cartItems){
    const tableBody = document.getElementById('cart-table-body'); // Ensure you have this element

    console.log(cartItems);
    cartItems.forEach((item, index) => {
        const row = document.createElement('tr');
        row.setAttribute("index", index);
        row.innerHTML = `
                        <td>
                            <div class="media">
                                <div class="d-flex cart-item">
                                    <img src="${item.product.image}" alt="${item.product.name}" class="cart-item-icon">
                                </div>
                                <div class="media-body">
                                    <p>${item.product.name}</p>
                                </div>
                            </div>
                            <div class="hidden-item-id" style="display:none;">
                                <input type="hidden" value="${item.product.id}">
                            </div>
                        </td>
                        <td>
                            <h5 id="price-${item.product.id}">${(item.product.price / 100).toFixed(2)} EGP</h5>
                        </td>
                        <td>
                            <div class="product_count">
                                <input type="text" name="qty" id="sst-${item.product.id}" maxlength="12" value="${item.quantity}" title="Quantity:" class="input-text qty" readonly>
                                <button onclick="increase(${item.product.id});" class="increase items-count" type="button"><i class="lnr lnr-chevron-up"></i></button>
                                <button onclick="decrease(${item.product.id});" class="reduced items-count" type="button"><i class="lnr lnr-chevron-down"></i></button>
                            </div>
                            <div id="stock-error" class="popup">
                                quantity out of stock!
                            </div>
                        </td>
                        <td>
                            <h5 id="total-${item.product.id}" class="item-price">${(item.product.price / 100 * item.quantity).toFixed(2)} EGP</h5>
                        </td>
                        <td>
                            <a class="gray_btn" href="" onclick="event.preventDefault(); removeItem(${item.product.id});">Remove</a>
                        </td>
                    `;

        tableBody.prepend(row); // Append the new row to the table body
    });
}

function increase(itemId){
    var inputField = $('#sst-' + itemId);
    var currentQuantity = parseInt(inputField.val());
    updateQuantity(itemId, currentQuantity + 1);
}

function decrease(itemId){
    var inputField = $('#sst-' + itemId);
    var currentQuantity = parseInt(inputField.val());
    updateQuantity(itemId, currentQuantity - 1);
}

function updateQuantity(itemId, quantity) {


    var inputField = $('#sst-' + itemId);
    var oldQuantity = parseInt(inputField.val());
    var currentQuantity = parseInt(inputField.val());
    console.log(currentQuantity);

    if(isNaN(currentQuantity))return false;
    if(currentQuantity === quantity)return true;
    currentQuantity=Math.max(1,quantity);

    var token = localStorage.getItem("token");
    if(token == null){
        var cartItems = localStorage.getItem("cart");
        cartItems = JSON.parse(cartItems);
        const item = cartItems.find(item => item.product.id === itemId);
        $.ajax({
                   url: `http://localhost:9002/api/products/${item.product.id}/stock`, // Servlet URL/
                   type: 'GET',
                    headers:{'ngrok-skip-browser-warning':'abc'},
                   success: function(response) {
                       if(response.quantity < oldQuantity){
                           //$('#err-' + itemId).text("quantity out of stock!");
                           $('#price-' + itemId).text((Number.parseFloat(response.price)/100).toFixed(2) + ' EGP');
                           $('#total-' + itemId).text((Number.parseFloat(
                               response.quantity * response.price)/100).toFixed(2) + ' EGP');
                           // Update the input field with the new quantity
                           inputField.val(response.quantity);
                           calculateTotalPrice();
                           showStockError();
                       }
                       else{

                           item.quantity = currentQuantity;
                           item.product.price = response.price;

                           localStorage.setItem("cart", JSON.stringify(cartItems));
                           $('#price-' + itemId).text((Number.parseFloat(response.price)/100).toFixed(2) + ' EGP');
                           // Update the total price on success
                           $('#total-' + itemId).text((Number.parseFloat(currentQuantity*response.price)/100).toFixed(2) + ' EGP');
                           // Update the input field with the new quantity
                           inputField.val(currentQuantity);
                           calculateTotalPrice();
                           //$('#err-' + itemId).text("");
                       }
                   },
                   error: function() {
                       showNotification("Error updating quantity!");
                   }
               });

    }
    else {
        // Make the asynchronous request to the server
        $.ajax({
                   url: `http://localhost:9002/api/v1/carts`, // Servlet URL
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

                       // Update the total price on success
                       $('#price-' + itemId).text((Number.parseFloat(response.product.price)/100).toFixed(2) + ' EGP');
                       $('#total-' + itemId).text((Number.parseFloat(
                           response.quantity * response.product.price)/100).toFixed(2) + ' EGP');
                       // Update the input field with the new quantity
                       inputField.val(response.quantity);
                       calculateTotalPrice();
                       if(response.quantity < currentQuantity){
                           showStockError();
                       }
                       //$('#err-' + itemId).text("");

                   },
                   error: function() {
                       showStockError();
                   }
               });
    }
}

function removeItem(itemId, action) {
    var token = localStorage.getItem("token");
    if(token == null){
        var cartItems = localStorage.getItem("cart");
        cartItems = JSON.parse(cartItems);
        const index = cartItems.findIndex(item => item.product.id === itemId);

        // Check if the item exists
        if (index !== -1) {
            // Remove the item from the cart
            cartItems.splice(index, 1);
        }
        localStorage.setItem("cart", JSON.stringify(cartItems));
        location.reload();
    }
    else {
        // Make the asynchronous request to the server
        $.ajax({
                   url: `http://localhost:9002/api/v1/carts`, // Servlet URL
                   type: 'POST',
                   data: JSON.stringify({
                       productId: itemId,
                       quantity: 0
                   }),
                   headers: {
                       Authorization: "Bearer "+token,
                       'Content-Type': "application/json",
                       'ngrok-skip-browser-warning':'abc'
                   },
                   success: function(response) {
                       location.reload(true);
                   },
                   error: function() {
                       showNotification("Error removing item!");
                   }
               });
    }

}

function calculateTotalPrice() {
    // Select all elements with the class 'item-price'
    const priceElements = document.querySelectorAll('.item-price');

    let totalPrice = 0;

    // Loop through the NodeList and sum up the price values
    priceElements.forEach(function(priceElement) {
        // Get the inner text, remove the dollar sign and convert to a float
        const price = parseFloat(priceElement.innerText.replace(/[$,]/g, ''));
        totalPrice += price; // Add to totalPrice
    });

    // Display the total price
    //document.getElementById('total-price').innerText = `Total Price: $`;
    document.getElementById('total-price').innerText = totalPrice.toFixed(2)+" EGP";
}
function goToLogin(){
    showNotification("You need to login");
}
    // Call the loadCartItems function on page load
    document.addEventListener('DOMContentLoaded', loadCartItems);