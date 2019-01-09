$(document).ready(function(){


    /* ========================== REGISTER DATA ==========================*/
    $("#registerForm").submit(function(event){
        event.preventDefault();

        var form = $(this);
        var dataForm = ConvertFormToJSON(form);

        register(JSON.stringify(dataForm));
    });

    function register(data_json){
        $.ajax({
            type         : "POST",
            data         : data_json,
            contentType  : "application/json; charset=utf-8",
            url          : "http://localhost:8080/library/member",
            success: function(response){
                jQuery("#registerModal").modal("hide");
                viewAll();
            },
            error: function(response, error){
                jQuery("#registerModal").modal("hide");
                viewAll();
            }
        }).responseText;
    }


    /* ================================================= LOGIN ===========================================================*/
    $("#loginForm").submit(function(event){
        event.preventDefault();
        var form = $(this);
        var dataForm = ConvertFormToJSON(form);
        var dataJson = JSON.stringify(dataForm);
        if(localStorage.getItem("token") == null) {
            $.ajax({
                type: "POST",
                data: dataJson,
                contentType: "application/json",
                url: "http://localhost:8080/library/login",
                success: function (response) {
                    jQuery("#registerModal").modal("hide");
                    localStorage.setItem("token", response.value);
                    location.href = "http://localhost:80/book/index.html";
                },
                error: function (response, error) {
                    jQuery("#registerModal").modal("hide");
                }
            }).responseText;
        }
        else
            location.href = "http://localhost:80/book/index.html";
    });



    /* ================================= CONVERT FORM ============================*/
    function ConvertFormToJSON(form){
        var array = jQuery(form).serializeArray();
        var json = {};

        jQuery.each(array, function() {
            json[this.name] = this.value || '';
        });

        return json;
    }


});