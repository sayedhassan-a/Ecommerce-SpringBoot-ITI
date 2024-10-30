function loadHeader() {
    fetch('header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
            loadingHeaderContent();
            displayShop();
        });
    fetch('footer.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('footer').innerHTML = data;
    });

}

function displayShop() {
    const shopDropdownContainer = document.getElementById("shopDropdownContainer");

    fetch("https://improved-ghastly-midge.ngrok-free.app/api/categories",{headers:{'ngrok-skip-browser-warning':'abc'}})
    .then(response => response.json())
    .then(categories => {
        // Build the dropdown menu dynamically
        const dropdownHtml = `
            <a class="nav-link dropdown-toggle" href="#" id="shopDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Shop
            </a>
            <div class="dropdown-menu" aria-labelledby="shopDropdown">
                ${categories.map(category => {
                    if (category.subCategories && category.subCategories.length > 0) {
                        return `
                            <div class="dropdown-submenu">
                                <span class="dropdown-item dropdown-toggle">${category.name}</span>
                                <div class="dropdown-menu">
                                    ${category.subCategories.map(subCat => `
                                        <a class="dropdown-item" href="/web/category.html?sub=${subCat.id}">${subCat.name}</a>
                                    `).join('')}
                                </div>
                            </div>
                        `;
                    } else {
                        // No subcategories to display, so skip this category
                        return '';
                    }
                }).join('')}
            </div>
        `;

        // Insert HTML into the dropdown container
        shopDropdownContainer.innerHTML = dropdownHtml;
        initSubmenu();
    })
    .catch(error => console.error("Error fetching categories:", error));

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
}

    document.addEventListener("DOMContentLoaded", function() {

        loadHeader();
    });