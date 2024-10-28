checkAdminAuth();
function checkAdminAuth(){
    var userToken = localStorage.getItem("token");
    if(userToken){
        location.href = location.origin + "/web/profile.html";
        return;
    }
    var token = localStorage.getItem("adminToken");
    if(token){
        $.ajax({
                   url: `http://localhost:9002/api/v1/validate-token`, // Servlet URL
                   type: 'POST',
                   headers: {
                       Authorization: "Bearer " + token,
                   },
                   success: function(response) {
                       if(response.data.userInfo.role!=="ROLE_USER"){

                       }else{
                           localStorage.removeItem("adminToken");
                           location.href = location.origin + "/web/auth/login.html";

                       }
                   },
                   error: function() {
                       token = null;
                       localStorage.removeItem("adminToken");
                       location.href = location.origin + "/web/auth/login.html";
                   }
               });
    }
    else {
        location.href = location.origin + "/web/auth/login.html";
    }
}