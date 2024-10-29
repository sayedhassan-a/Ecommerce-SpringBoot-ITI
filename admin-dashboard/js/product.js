document.addEventListener('DOMContentLoaded', function() {
    let currentPage = 0;
    const pageSize = 10;

    function loadPage(pageNumber) {
        fetchProducts(pageNumber, pageSize);
    }

    fetchProducts(currentPage, pageSize);

    function fetchProducts(page, size) {
        const url = `http://localhost:9002/admins/products?page=${page}&size=${size}`;
        fetch(url)
            .then(response => response.json())
            .then(data => {
                renderProducts(data.data.content);
                createPagination(data.data.totalPages, data.data.pageable.pageNumber);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function renderProducts(products) {
        const productGrid = document.getElementById('productGrid');
        productGrid.innerHTML = '';

        products.forEach(product => {
            const card = document.createElement('div');
            card.classList.add('product-card');
            card.innerHTML = `
                <h4 class="product-title">${product.name}</h4>
                <p class="product-price">$${(product.price / 100).toFixed(2)}</p>
                <p class="product-stock">Stock: ${product.stock}</p>
                <p class="product-brand">Brand: ${product.brandName}</p>
                <div class="action-buttons">
                    <button class="btn btn-update" onclick="window.location.href='update-product.html?productId=${product.id}'">Update</button>
                    <button class="btn btn-delete" onclick="confirmDelete(${product.id})">Delete</button>
                </div>
            `;
            productGrid.appendChild(card);
        });
    }

    function confirmDelete(productId) {
        if (confirm("Are you sure you want to delete this product?")) {
            deleteProduct(productId);
        }
    }

    function deleteProduct(productId) {
        fetch(`http://localhost:9002/admins/products/${productId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    alert('Product deleted successfully');
                    fetchProducts(currentPage, pageSize);
                } else {
                    alert('Failed to delete the product');
                }
            });
    }

    function createPagination(totalPages, currentPage) {
        const paginationElement = document.getElementById('pagination');
        paginationElement.innerHTML = '';
        for (let i = 0; i < totalPages; i++) {
            const pageItem = document.createElement('li');
            pageItem.classList.add('page-item', currentPage === i ? 'active' : '');
            pageItem.innerHTML = `<button onclick="loadPage(${i})">${i + 1}</button>`;
            paginationElement.appendChild(pageItem);
        }
    }
});