var token = localStorage.getItem("token");
if(token == null){
    location.href = location.origin + "/web/index.html";
}