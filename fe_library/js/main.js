$.get("/menu/navbar.html", function (data) {
      $("aside").append(data);
}); 

$.get("/menu/sidebar.html", function (data) {
      $("aside").append(data);
}); 
