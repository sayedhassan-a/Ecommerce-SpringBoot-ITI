document.addEventListener("DOMContentLoaded", async () => {
    const formContainer = document.getElementById("form-container");

    // Replace with the actual SubId parameter
    const subId = "13"; // Example SubId
    const apiUrl = `http://localhost:9002/api/subcategories/${subId}/specifications`;

    try {
        // Fetch specifications from API
        const response = await fetch(apiUrl);
        const data = await response.json();

        // Create the form
        const form = document.createElement("form");
        form.innerHTML = `<h2>${data.subCategorySpecification.name} Specifications</h2>`;

        data.subCategorySpecification.specs.forEach(spec => {
            // Label for each specification
            const label = document.createElement("label");
            label.textContent = spec.specKey;
            form.appendChild(label);

            // Input element depending on the type
            if (spec.inputType === "dropdown") {
                const select = document.createElement("select");
                select.required = spec.required;
                select.name = spec.specKey;

                // Create an option for each dropdown item
                spec.options.forEach(option => {
                    const optionElement = document.createElement("option");
                    optionElement.value = option;
                    optionElement.textContent = option;
                    select.appendChild(optionElement);
                });
                form.appendChild(select);
            } else if (spec.inputType === "text") {
                const input = document.createElement("input");
                input.type = "text";
                input.required = spec.required;
                input.name = spec.specKey;
                form.appendChild(input);
            }
        });

        // Add submit button
        const submitButton = document.createElement("button");
        submitButton.type = "submit";
        submitButton.textContent = "Submit";
        form.appendChild(submitButton);

        // Append form to container
        formContainer.appendChild(form);

        // Optional: Handle form submission
        form.addEventListener("submit", event => {
            event.preventDefault();
            const formData = new FormData(form);
            const entries = Object.fromEntries(formData.entries());
            console.log("Form Data Submitted:", entries);
        });
    } catch (error) {
        console.error("Error fetching data:", error);
    }
});
