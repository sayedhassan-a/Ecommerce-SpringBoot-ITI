checkAuth();
function checkAuth() {
    const adminToken = localStorage.getItem("adminToken");

    if (adminToken) {
        location.href = `${location.origin}/dashboard/list-product.html`;
        return;
    }

    const token = localStorage.getItem("token");

    if (token) {
        console.log("Validating user token...");

        fetch(`http://localhost:9002/login/validate-token`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                return response.json()
            })
            .then(data=>{
                console.log(data.data)

                if (data.data == "ROLE_USER") {
                    location.href = "/web/index.html";
                }
                else {
                    localStorage.removeItem("token");
                }
            })
            .catch(error => {
                console.error('Error during token validation:', error);
                localStorage.removeItem("token");
            });
    }
}