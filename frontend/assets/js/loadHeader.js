
    function loadHeader() {
    fetch('header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
            loadingHeaderContent();
        });
    fetch('footer.html')
    .then(response => response.text())
    .then(data => {
        document.getElementById('footer').innerHTML = data;
    });

    }
    document.addEventListener("DOMContentLoaded", function() {
        loadHeader();
    });