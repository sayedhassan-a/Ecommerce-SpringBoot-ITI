function loadingAdminHeaderContent(){
    const token = localStorage.getItem("token");
    if(token === null){
        document.getElementById("login").innerHTML = '<a class="nav-link" href="/web/auth/login.html">Login</a>';

    }
    else{
        document.getElementById("login").innerHTML = '<a class="nav-link" onclick="invalidateToken()">Logout</a>';
        document.getElementById("profileSection").innerHTML = `
                        <br>
                        <li class="nav-item">
                            <button class="search">
                                <a href="/web/profile.html" class="cart"><span class="lnr fa-regular fa-user" id="search"></span></a>
                            </button>
                        </li>`
    }
}

function invalidateToken(){
    localStorage.removeItem("adminToken");
    location.href = "/web/index.html";
}