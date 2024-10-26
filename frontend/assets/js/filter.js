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

                            // Toggle selection on click
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
        const selectedFilters = {
            color: [], // Array to hold multiple colors
        };

        // Collect selected color options
        document.querySelectorAll('.color-option.selected').forEach(option => {
            selectedFilters.color.push(option.dataset.color); // Push all selected colors to the array
        });

        // Collect other selected filter checkboxes
        document.querySelectorAll('.pixel-checkbox:checked').forEach(checkbox => {
            const [specKey] = checkbox.id.split('-');
            if (!selectedFilters[specKey]) {
                selectedFilters[specKey] = [];
            }
            selectedFilters[specKey].push(checkbox.value);
        });

        // Store filters in local storage for security and reference
        localStorage.setItem('selectedFilters', JSON.stringify(selectedFilters));

        // Reset the filters
        resetFilters();

        // Fetch and display filtered data
        fetchFilteredData(selectedFilters);
    }

    function resetFilters() {
        // Reset color selections
        document.querySelectorAll('.color-option.selected').forEach(option => {
            option.classList.remove('selected');
        });

        // Reset checkbox selections
        document.querySelectorAll('.pixel-checkbox:checked').forEach(checkbox => {
            checkbox.checked = false;
        });
    }

    function fetchFilteredData(filters) {
        // Fetch products using the selected filters
        console.log('Fetching data with filters:', filters);
        
        // Example: Fetch data using the filters
        fetch(`http://localhost:9002/api/products?filters=${encodeURIComponent(JSON.stringify(filters))}`)
            .then(response => response.json())
            .then(data => {
                // Update your product display logic here
                console.log(data);
                // E.g., update the DOM to show the filtered products
            })
            .catch(error => console.error('Error fetching products:', error));
    }
});
