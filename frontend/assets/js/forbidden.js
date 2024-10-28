checkAuth();
function checkAuth(){
    var adminToken = localStorage.getItem("adminToken");
    if(adminToken){
        location.href = location.origin + "/dashboard/list-product.html";
        return;
    }
    var token = localStorage.getItem("token");
    if(token){
        $.ajax({
                   url: `http://localhost:9002/api/v1/validate-token`, // Servlet URL
                   type: 'POST',
                   headers: {
                       Authorization: "Bearer " + token,
                   },
                   success: function(response) {
                       if(response.data.userInfo.role==="ROLE_USER"){

                       }else{
                           localStorage.removeItem("token");
                           location.href = location.origin + "/web/auth/login.html";

                       }
                   },
                   error: function() {
                       token = null;
                       localStorage.removeItem("token");
                       location.href = location.origin + "/web/auth/login.html";
                   }
               });
    }
    else {
        location.href = location.origin + "/web/auth/login.html";
    }
}