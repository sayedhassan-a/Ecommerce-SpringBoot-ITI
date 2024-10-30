function mergeCart(){
    var cart = localStorage.getItem("cart");
    if(cart){
        var token = localStorage.getItem("token");
        cart = JSON.parse(cart);
        cart = cart.map(item=>{

            return {
                "productId": item.product.id,
                "quantity": item.quantity

            };
        })
        $.ajax({
                   url: `https://improved-ghastly-midge.ngrok-free.app/api/v1/carts`, // Servlet URL
                   type: 'PUT',
                   data: JSON.stringify(cart),
                   headers: {
                       Authorization: "Bearer "+token,
                       'Content-Type': "application/json",
                       'ngrok-skip-browser-warning':'abc'
                   },
                   success: function(response) {
                   },
                   error: function() {

                   }
               });
        localStorage.removeItem("cart");
    }
}