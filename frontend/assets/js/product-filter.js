document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const subId = urlParams.get('sub');
    const filterContainer = document.querySelector('.sidebar-filter .common-filter');
    const productBox = document.getElementById('productBox');
    const currentSize = 12;
    let currentPage = 0;
    let filters = { sub: subId };
    let flag = false;

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
            .then(data => {
                renderProducts(data.content);
                createPagination(data.totalPages, data.pageable.pageNumber);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    // Render products
    function renderProducts(products) {
        productBox.innerHTML = '';

        products.forEach(product => {
            const productHtml = `
                <div class="col-lg-4 col-md-6">
                    <div class="single-product modern-style">
                        <div class="img-container">
                            <img class="img-fluid" src="${product.image}" alt="${product.name}">
                        </div>
                        <div class="product-details">
                            <h6>${product.name}</h6>
                            <div class="price">
                                <h6>${(Number.parseFloat(product.price) / 100).toFixed(2)} EGP</h6>
                            </div>
                            <div class="prd-bottom">
                                <div class="social-info" onclick="event.preventDefault(); handleAddItem(1,${product.id},true);">
                                    <span class="ti-bag"></span>
                                    <p class="hover-text">add to bag</p>
                                </div>
                                <div class="social-info" href="/web/single-product.html?id=${product.id}">
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

    // Rest of the code (fetchFilteredProducts, applyFilters, pagination functions, etc.)



    // Apply filters and fetch filtered products
 // Apply filters and fetch filtered products
function applyFilters() {
        currentPage = 0;
    flag = true;
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
        .then(data => {renderProducts(data.content);
            createPagination(data.totalPages,data.pageable.pageNumber);
        })
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

    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');

    function applySearch() {
        currentPage = 0;
        flag = false;
        var searchParameter = searchInput.value;  // Start with an empty filters object since subId is now in the URL path
        localStorage.setItem("searchParam", searchParameter);
        fetchBySearch(searchParameter);
    }

    searchButton.addEventListener('click', () => {
        applySearch()
    });

    searchInput.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            applySearch();
        }
    });
    function fetchBySearch(){
        var searchValue = localStorage.getItem("searchParam");
        if(searchValue == null)searchValue="";
        searchInput.value=searchValue;
        const queryParams = new URLSearchParams({ page: currentPage, size: currentSize, name: searchValue }).toString();
        fetch(`http://localhost:9002/api/products/subcategory/${subId}/search?${queryParams}`)
            .then(response => response.json())
            .then(data => {renderProducts(data.content);
                createPagination(data.totalPages,data.pageable.pageNumber);
            })
            .catch(error => console.error('Error fetching products:', error));
        resetFilters();
    }
    function resetFilters() {
        // Clear color selections
        document.querySelectorAll('.color-option.selected').forEach(option => {
            option.classList.remove('selected');
        });

        // Clear checkbox selections
        document.querySelectorAll('.pixel-checkbox:checked').forEach(checkbox => {
            checkbox.checked = false;
        });

        // Reset filters object
        filters = { sub: subId }; // Reset to only include the subcategory filter

        // Clear stored filters from localStorage
        localStorage.removeItem('selectedFilters');

        // Fetch and render the default product list
    }







    //Pagination
    function createPagination(totalPages, currentPage) {
        const container = document.getElementById('pagination');
        container.innerHTML = '';  // Clear previous pagination

        const paginationNumbers = document.createElement('span');
        paginationNumbers.classList.add('pagination-numbers');
        container.appendChild(paginationNumbers);

        const delta = 0; // Pages to show around the current page

        // Left Arrow
        const createLeftButton = () => {
            const leftArrow = document.createElement('a');
            leftArrow.classList.add('arrow', 'prev-arrow');
            leftArrow.innerHTML = '<i class="fa fa-long-arrow-left" aria-hidden="true"></i>';
            leftArrow.style.pointerEvents = currentPage === 0 ? 'none' : 'auto';

            leftArrow.onclick = function() {
                if (currentPage > 0) {
                    loadPage(currentPage - 1);
                }
            };
            container.insertBefore(leftArrow, paginationNumbers);
        };

        // Right Arrow
        const createRightButton = () => {
            const rightArrow = document.createElement('a');
            rightArrow.classList.add('arrow', 'next-arrow');
            rightArrow.innerHTML = '<i class="fa fa-long-arrow-right" aria-hidden="true"></i>';
            rightArrow.style.pointerEvents = currentPage === totalPages - 1 ? 'none' : 'auto';

            rightArrow.onclick = function() {
                if (currentPage < totalPages - 1) {
                    loadPage(currentPage + 1);
                }
            };
            container.appendChild(rightArrow);
        };

        // Individual Page Buttons
        const createPageButton = (pageNum) => {
            const pageButton = document.createElement('a');
            pageButton.classList.add('pagination-link');
            pageButton.innerText = pageNum;
            if (pageNum === currentPage + 1 ) {
                pageButton.classList.add('active');  // Highlight current page
            }
            else{
                pageButton.onclick = function() {
                    loadPage(pageNum - 1);
                };
            }

            paginationNumbers.appendChild(pageButton);
        };

        createLeftButton();

        // First page
        createPageButton(1);


        // Dots if currentPage is not close to the beginning
        if (currentPage - delta > 1) {
            const dots = document.createElement('a');
            dots.classList.add('pagination-link');
            dots.innerText = '...';
            paginationNumbers.appendChild(dots);
        }

        // Pages around currentPage
        for (let i = Math.max(2, currentPage + 1 - delta); i <= Math.min(totalPages - 1 , currentPage + 1 + delta); i++) {
            createPageButton(i);
        }

        // Dots if currentPage is not close to the end
        if (currentPage + 1 + delta < totalPages - 1) {
            const dots = document.createElement('a');
            dots.classList.add('pagination-link');
            dots.innerText = '...';
            paginationNumbers.appendChild(dots);
        }

        // Last page
        if(totalPages>1)createPageButton(totalPages);


        createRightButton();
    }

// Load Page Function
    function loadPage(pageNumber) {
        currentPage = pageNumber;
        if(flag)applyFilters();
        else fetchBySearch();
    }

});

