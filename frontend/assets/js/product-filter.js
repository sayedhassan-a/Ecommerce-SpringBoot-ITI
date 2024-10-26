document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const subId = urlParams.get('sub');
    const filterContainer = document.querySelector('.sidebar-filter .common-filter');
    const productBox = document.getElementById('productBox');
    const currentSize = 12;
    let currentPage = 0;
    let filters = { sub: subId };

    if (subId) {
        fetchSpecifications(subId);
        fetchProducts(subId);
    }

    // Fetch specifications based on subcategory
    function fetchSpecifications(subId) {
        fetch(`http://localhost:9002/api/subcategories/${subId}/specifications`)
            .then(response => response.json())
            .then(data => renderSpecifications(data.subCategorySpecification.specs))
            .catch(error => console.error('Error fetching specifications:', error));
    }

    // Render specification filters
    function renderSpecifications(specs) {
        filterContainer.innerHTML = '';

        specs.forEach(spec => {
            const headDiv = document.createElement('div');
            headDiv.classList.add('head');
            headDiv.innerText = spec.specKey;
            filterContainer.appendChild(headDiv);

            const optionsList = document.createElement('ul');
            optionsList.classList.add('main-categories');

            spec.options.forEach(option => {
                const li = document.createElement('li');
                li.classList.add('filter-list');

                if (spec.specKey.toLowerCase() === "color") {
                    const colorSwatch = document.createElement('span');
                    colorSwatch.classList.add('color-option');
                    colorSwatch.style.backgroundColor = option.toLowerCase();
                    colorSwatch.dataset.color = option;

                    colorSwatch.addEventListener('click', () => {
                        colorSwatch.classList.toggle('selected');
                    });

                    li.appendChild(colorSwatch);
                } else {
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.id = `${spec.specKey}-${option}`;
                    checkbox.value = option;
                    checkbox.classList.add('pixel-checkbox');

                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.innerText = option;

                    li.appendChild(checkbox);
                    li.appendChild(label);
                }
                optionsList.appendChild(li);
            });
            filterContainer.appendChild(optionsList);
        });

        const searchButton = document.createElement('button');
        searchButton.innerText = "Apply Filters";
        searchButton.classList.add('filter-search-button');
        searchButton.addEventListener('click', applyFilters);

        filterContainer.appendChild(searchButton);
    }

    // Fetch products by subcategory
    function fetchProducts(subId) {
        const queryParams = new URLSearchParams({ page: currentPage, size: currentSize }).toString();
        fetch(`http://localhost:9002/api/products/subcategory/${subId}?${queryParams}`)
            .then(response => response.json())
            .then(data => renderProducts(data.content))
            .catch(error => console.error('Error fetching products:', error));
    }

    // Render products
    function renderProducts(products) {
        productBox.innerHTML = '';

        products.forEach(product => {
            const productHtml = `
                <div class="col-lg-4 col-md-6">
                    <div class="single-product">
                        <div class="img-container">
                            <img class="img-fluid" src="${product.image}" alt="${product.name}">
                        </div>
                        <div class="product-details">
                            <h6>${product.name}</h6>
                            <div class="price">
                                <h6>${(Number.parseFloat(product.price) / 100).toFixed(2)} EGP</h6>
                            </div>
                            <div class="prd-bottom">
                                <div class="social-info" onclick="event.preventDefault(); addToCart(${product.id},1);">
                                    <span class="ti-bag"></span>
                                    <p class="hover-text">add to bag</p>
                                </div>
                                <div class="social-info">
                                    <span class="lnr lnr-move"></span>
                                    <p class="hover-text">view more</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            productBox.innerHTML += productHtml;
        });
    }

    // Apply filters and fetch filtered products
 // Apply filters and fetch filtered products
function applyFilters() {
    filters = {};  // Start with an empty filters object since subId is now in the URL path

    // Collect selected color options
    document.querySelectorAll('.color-option.selected').forEach(option => {
        if (!filters.color) filters.color = [];
        filters.color.push(option.dataset.color);
    });

    // Collect other selected filter checkboxes
    document.querySelectorAll('.pixel-checkbox:checked').forEach(checkbox => {
        const [specKey] = checkbox.id.split('-');
        if (!filters[specKey]) filters[specKey] = [];
        filters[specKey].push(checkbox.value);
    });

    localStorage.setItem('selectedFilters', JSON.stringify(filters));
    fetchFilteredProducts(subId, filters);
}

// Fetch products with applied filters
function fetchFilteredProducts(subId, filters) {
    const filterParams = encodeURIComponent(JSON.stringify(filters));
    const queryParams = `filters=${filterParams}&page=${currentPage}&size=${currentSize}`;
    fetch(`http://localhost:9002/api/products/subcategory/${subId}/filter?${queryParams}`)
        .then(response => response.json())
        .then(data => renderProducts(data.content))
        .catch(error => console.error('Error fetching filtered products:', error));
}


    // Restore filters from localStorage and apply them if present
    const storedFilters = JSON.parse(localStorage.getItem('selectedFilters'));
    if (storedFilters && storedFilters.sub === subId) {
        applyStoredFilters(storedFilters);
    }

    function applyStoredFilters(storedFilters) {
        if (storedFilters.color) {
            storedFilters.color.forEach(color => {
                const colorElement = document.querySelector(`.color-option[data-color="${color}"]`);
                if (colorElement) colorElement.classList.add('selected');
            });
        }

        Object.keys(storedFilters).forEach(key => {
            if (key !== 'sub' && key !== 'color') {
                storedFilters[key].forEach(value => {
                    const checkbox = document.getElementById(`${key}-${value}`);
                    if (checkbox) checkbox.checked = true;
                });
            }
        });
    }
});
