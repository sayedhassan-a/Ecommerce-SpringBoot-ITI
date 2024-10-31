function loadingHeaderContent(){
    const token = localStorage.getItem("token");
    if(token === null){
        document.getElementById("login").innerHTML = '<a class="nav-link" href="/web/auth/login.html">Login</a>';

    }
    else{
        document.getElementById("login").innerHTML = '<a class="nav-link" onclick="invalidateToken()">Logout</a>\n';
        document.getElementById("profileSection").innerHTML = `
                        <li class="nav-item">
                            <a href="/web/profile.html" style="font-size: 20px;color: white" class="cart-icon fas fa-user-alt" aria-label="Cart">
                            </a>
                        </li>
                        `
    }
}

function invalidateToken(){
    localStorage.removeItem("token");
    location.href = "/web/index.html";
}