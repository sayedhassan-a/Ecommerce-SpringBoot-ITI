function loadCheckout(){
    var token = localStorage.getItem("token");
// Fetch cart items from API
    fetch('http://localhost:9002/api/v1/carts', {
        method: "GET",
        headers: {
            Authorization: "Bearer " + token,
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
            // Sample cart items for demonstration
            populateCartItems(cartItems);
            calculateTotalPrice(cartItems);
        })
        .catch(error => console.error('Error loading cart items:', error));
}

// Function to populate cart items in the HTML
function populateCartItems(cartItems) {
    const listContainer = document.querySelector('.list');
    listContainer.innerHTML = `<li><a href="#">Product <span>Total</span></a></li>`;

    cartItems.forEach(item => {
        const itemElement = document.createElement('li');
        itemElement.innerHTML = `
            <a href="#">${item.product.name}
                <span class="middle">x ${item.quantity}</span>
                <span class="last">EGP ${(item.product.price / 100 * item.quantity).toFixed(2)}</span>
            </a>
        `;
        listContainer.appendChild(itemElement);
    });
}

// Function to calculate and display the total price with optional discount
function calculateTotalPrice(cartItems) {
    let totalPrice = cartItems.reduce((total, item) => total + (item.product.price * item.quantity), 0) / 100;

    const newTotalPrice = totalPrice;

    const totalContainer = document.querySelector('.list_2');
    totalContainer.innerHTML = `
        <li><a href="#">Subtotal <span>EGP ${totalPrice.toFixed(2)}</span></a></li>
    `;


    totalContainer.innerHTML += `
         <li><a href="#">Total <span>EGP ${totalPrice.toFixed(2)}</span></a></li>
    `;

}