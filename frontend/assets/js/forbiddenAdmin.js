checkAdminAuth();
function checkAdminAuth() {
    const userToken = localStorage.getItem("token");

    if (userToken) {
        location.href = `${location.origin}/web/index.html`;
        return;
    }

    const token = localStorage.getItem("adminToken");

    if (token) {
        console.log("Validating user token...");

        fetch(`http://localhost:9002/login/validate-token`, {
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
                if (data.data != "ROLE_USER") {

                }
                else {
                    localStorage.removeItem("adminToken");
                    location.href = "/web/auth/login.html";
                }
            })
            .catch(error => {
                console.error('Error during token validation:', error);
                localStorage.removeItem("adminToken");
                location.href = "/web/auth/login.html";
            });
    }
    else{
        location.href = "/web/index.html";
    }
}
