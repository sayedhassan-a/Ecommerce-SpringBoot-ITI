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

        fetch(`https://improved-ghastly-midge.ngrok-free.app/login/validate-token`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'ngrok-skip-browser-warning':'abc'
            }
        })
            .then(response => {
                return response.json()
            })
            .then(data=>{
                console.log(data)
                if (data.data == "ROLE_USER") {

                }
                else {
                    localStorage.removeItem("token");
                    location.href = "/web/auth/login.html";
                }
            })
            .catch(error => {
                console.error('Error during token validation:', error);
                localStorage.removeItem("token");
                location.href = "/web/auth/login.html";
            });
    }
    else{
        location.href = "/web/auth/login.html";
    }
}
