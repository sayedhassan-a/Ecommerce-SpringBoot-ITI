document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const subId = urlParams.get('sub');
    const filterContainer = document.querySelector('.sidebar-filter .common-filter');

    if (subId) {
        fetch(`http://localhost:9002/api/subcategories/${subId}/specifications`)
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch specifications');
                return response.json();
            })
            .then(data => {
                const specs = data.subCategorySpecification.specs;
                filterContainer.innerHTML = ''; // Clear any existing filters

                specs.forEach(spec => {
                    // Specification header
                    const headDiv = document.createElement('div');
                    headDiv.classList.add('head');
                    headDiv.innerText = spec.specKey;
                    filterContainer.appendChild(headDiv);

                    const optionsList = document.createElement('ul');
                    optionsList.classList.add('main-categories');

                    if (spec.specKey.toLowerCase() === "color") {
                        spec.options.forEach(option => {
                            const li = document.createElement('li');
                            li.classList.add('filter-list');

                            const colorSwatch = document.createElement('span');
                            colorSwatch.classList.add('color-option');
                            colorSwatch.style.backgroundColor = option.toLowerCase();
                            colorSwatch.dataset.color = option;

                            const colorName = document.createElement('span');
                            colorName.classList.add('color-name');
                            colorName.innerText = option;

                            colorSwatch.addEventListener('click', () => {
                                colorSwatch.classList.toggle('selected');
                            });

                            li.appendChild(colorSwatch);
                            li.appendChild(colorName);
                            optionsList.appendChild(li);
                        });
                    } else {
                        spec.options.forEach(option => {
                            const li = document.createElement('li');
                            li.classList.add('filter-list');

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
                            optionsList.appendChild(li);
                        });
                    }

                    filterContainer.appendChild(optionsList);
                });

                // Add the search button
                const searchButton = document.createElement('button');
                searchButton.innerText = "Apply Filters";
                searchButton.classList.add('filter-search-button');
                searchButton.addEventListener('click', applyFilters);

                filterContainer.appendChild(searchButton);
            })
            .catch(error => console.error('Error fetching specifications:', error));
    }

    function applyFilters() {
        const filters = { sub: subId }; // Initialize filters with sub ID

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

        // Store filters in localStorage
        localStorage.setItem('selectedFilters', JSON.stringify(filters));

        // Redirect to the target URL without adding parameters in the URL
        window.location.href = 'category.html';
    }

    // On page load, check if there are saved filters and apply them
    const storedFilters = JSON.parse(localStorage.getItem('selectedFilters'));
    if (storedFilters && storedFilters.sub === subId) {
        applyStoredFilters(storedFilters);
    }

    function applyStoredFilters(filters) {
        // Apply color filters
        (filters.color || []).forEach(color => {
            const colorElement = document.querySelector(`.color-option[data-color="${color}"]`);
            if (colorElement) colorElement.classList.add('selected');
        });

        // Apply other filters
        Object.keys(filters).forEach(key => {
            if (key !== 'sub' && key !== 'color') {
                filters[key].forEach(value => {
                    const checkbox = document.getElementById(`${key}-${value}`);
                    if (checkbox) checkbox.checked = true;
                });
            }
        });
    }
});
