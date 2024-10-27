
    function loadHeader() {
    fetch('admin-header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('admin-header').innerHTML = data;
            loadingAdminHeaderContent();
        });
    }
    document.addEventListener("DOMContentLoaded", function() {
        loadHeader();
    });