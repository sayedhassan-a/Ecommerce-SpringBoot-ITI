
    function loadHeader() {
    fetch('admin-header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('admin-header').innerHTML = data;
            loadingHeaderContent();
        });
    }
    document.addEventListener("DOMContentLoaded", function() {
        loadHeader();
    });