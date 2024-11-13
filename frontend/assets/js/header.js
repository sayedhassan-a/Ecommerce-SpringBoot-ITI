document.addEventListener("DOMContentLoaded", function () {
    const shopDropdownContainer = document.getElementById("shopDropdownContainer");

    // Fetch categories and build dropdown dynamically
    fetch("https://improved-ghastly-midge.ngrok-free.app/api/categories",{headers:{'ngrok-skip-browser-warning':'abc'}})
        .then(response => response.json())
        .then(categories => {
            // Initialize dropdown structure
            const dropdownHtml = `
                <a class="nav-link dropdown-toggle" href="#" id="shopDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Shop
                </a>
                <div class="dropdown-menu" aria-labelledby="shopDropdown">
                    ${categories.map(category => {
                        // Check if subcategories exist for this category
                        if (category.subCategories && category.subCategories.length > 0) {
                            return `
                                <div class="dropdown-submenu">
                                    <a class="dropdown-item dropdown-toggle" href="/web/category.html?cat=${category.id}">${category.name}</a>
                                    <div class="dropdown-menu">
                                        ${category.subCategories.map(subCat => `
                                            <a class="dropdown-item" href="/web/category.html?cat=${category.id}&sub=${subCat.id}">${subCat.name}</a>
                                        `).join('')}
                                    </div>
                                </div>
                            `;
                        } else {
                            return `<a class="dropdown-item" href="/web/category.html?cat=${category.id}">${category.name}</a>`;
                        }
                    }).join('')}
                </div>
            `;

            // Insert HTML into the dropdown container
            shopDropdownContainer.innerHTML = dropdownHtml;

            // Enable dropdown submenu functionality
            initSubmenu();
        })
        .catch(error => console.error('Error fetching categories:', error));

    // Function to handle submenu dropdown
    function initSubmenu() {
        const dropdownSubmenus = document.querySelectorAll('.dropdown-submenu');
        dropdownSubmenus.forEach(submenu => {
            submenu.addEventListener('mouseover', function () {
                this.querySelector('.dropdown-menu').classList.add('show');
            });
            submenu.addEventListener('mouseout', function () {
                this.querySelector('.dropdown-menu').classList.remove('show');
            });
        });
    }
});
