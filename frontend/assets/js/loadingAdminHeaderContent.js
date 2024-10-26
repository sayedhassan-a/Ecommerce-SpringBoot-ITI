function loadingAdminHeaderContent(){
    const token = localStorage.getItem("token");
    if(token === null){
        document.getElementById("login").innerHTML = '<a class="nav-link" href="/ecommerce/dashboard/auth/login.jsp">Login</a>';

    }
    else{
        document.getElementById("login").innerHTML = '<a class="nav-link" href="/ecommerce/dashboard/auth/logout">Logout</a>';
        document.getElementById("profileSection").innerHTML = `
                        <br>
                        <li class="nav-item">
                            <button class="search">
                                <a href="profile.jsp" class="cart"><span class="lnr fa-regular fa-user" id="search"></span></a>
                            </button>
                        </li>`
    }
}
