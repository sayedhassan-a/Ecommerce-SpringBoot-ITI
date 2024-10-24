    function loadCartItems() {
    fetch('http://localhost:9002/api/v1/carts') // Adjust the URL to match your endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(cartItems => {
            const tableBody = document.getElementById('cart-table-body'); // Ensure you have this element
            tableBody.innerHTML = ''; // Clear existing content
            cartItems = [{
                product:{
                    image:"image.com",
                    name:"Asus",
                    id:1,
                    price:500
                },
                quantity:10}
            ];
            console.log(cartItems);
            cartItems.forEach(item => {
                const row = document.createElement('tr');

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
                            <h5>${(item.product.price / 100).toFixed(2)} EGP</h5>
                        </td>
                        <td>
                            <div class="product_count">
                                <input type="text" name="qty" id="sst-${item.product.id}" maxlength="12" value="${item.quantity}" title="Quantity:" class="input-text qty" readonly>
                                <button onclick="updateQuantity(${item.product.id}, 'increase');" class="increase items-count" type="button"><i class="lnr lnr-chevron-up"></i></button>
                                <button onclick="updateQuantity(${item.product.id}, 'decrease');" class="reduced items-count" type="button"><i class="lnr lnr-chevron-down"></i></button>
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

                tableBody.appendChild(row); // Append the new row to the table body
            });
        })
        .catch(error => console.error('Error loading cart items:', error));
}

    // Call the loadCartItems function on page load
    document.addEventListener('DOMContentLoaded', loadCartItems);