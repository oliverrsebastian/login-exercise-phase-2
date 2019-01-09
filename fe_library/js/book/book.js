$(document).ready(function(){
	viewAll();

	function getToken(){
		var token = localStorage.getItem("token");
		if(token == null)
			return null;
		return token;
	}

  	function viewAll(){
      $("#table-content").html('');

      $.ajax({
          type         : "GET",
          url          : "http://localhost:8080/library/book/all",
          responseType : "application/json",
		  beforeSend : function(xhr){
          	xhr.setRequestHeader("Authorization", "Bearer " + getToken());
		  },
          success: function(response){
              if (response == null) {
                  $("#main_table").append(
                      '<tr><td> No data on records.</td> </tr>'
                  );
              }
              else{
                  $.each(response, function (i, value) {
                      $("#main_table").append(
                          '<tr>'
                          +'<td>' + (i+1) + '</td>'
                          +'<td>' + value.name + '</td>'
                          +'<td>' + value.type + '</td>'
                          +'<td class="col-md-1">' +
                          '<div class="dropdown">'+
                          '<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><i class="icon fa fa-wrench"></i> Tools</button>'+
                          '<ul class="dropdown-menu dropdown-menu-right" id="dropdownTools">'+
                          '<li><button id="viewButton" class="btn" value="' + value.id + '" '+
                          'data-toggle="modal" data-target="#viewModal" ><i class="icon fa fa-eye"></i> View</button></li>'+
                          '<li><button id="updateButton" class="btn " value="' + value.id + '" '+
                          'data-toggle="modal" data-target="#updateModal" ><i class="icon fa fa-edit"></i> Edit</button></li>'+
                          '<li><button id="deleteButton" class="btn " value="' + value.id + '" >'+
                          '<i class="icon fa fa-trash"></i> Delete</button></li>'+
                          '</ul>'+
                          '</div>'
                          +'</td>' +
                          '</tr>'
                      );
                  });
              }
          },
          error: function(jqXHR, textStatus, errorThrown){
          	localStorage.removeItem("token");
          	  	location.href = "http://localhost/member";
          }
      });
  	}

 	/* ========================== CREATE DATA ==========================*/
 	$("#bookForm").submit(function(event){
	      event.preventDefault();

	      var form = $(this);
	      var dataForm = ConvertFormToJSON(form);

	      addNewData(JSON.stringify(dataForm));
	});

	function addNewData(data_json){
		alert(data_json);
	   $.ajax({
          type         : "POST",
          data         : data_json,
		   dataType : "JSON",
          contentType  : "application/json",
          url          : "http://localhost:8080/library/book",
           beforeSend : function(xhr){
               xhr.setRequestHeader("Authorization", "Bearer " + getToken());
           },
          success: function(){
              jQuery("#formModal").modal("hide");
              viewAll();
          },
           error: function(jqXHR, response, errorThrown){
               localStorage.removeItem("token");
                   location.href = "http://localhost/member";
           }
        }).responseText;
	}


	/* ================================================= VIEW DATA BY ID ===========================================================*/
	$(document).on("click", "#viewButton", function(){
	      var ID = $(this).val();
	  	  $("#name, #type").html('');

	      $.ajax({
	  	    type         : "GET",
	        url          : "http://localhost:8080/library/book/" + ID,
              beforeSend : function(xhr){
                  xhr.setRequestHeader("Authorization", "Bearer " + getToken());
              },
			  success : function(response){
				$("#view-name").html(response.name);
				$("#view-type").html(response.type);
			  },
              error: function(jqXHR, textStatus, errorThrown){
                      location.href = "http://localhost/member";
                  }
	      });
	  });


	/* ========================= UPDATE DATA  ===========================*/
	$(document).on("click", "#updateButton", function(event){
	      event.preventDefault(); 
	      
	      var ID = $(this).val();
	      var form = document.getElementById('bookFormUpdate');   
	      
	      $("#submitFormUpdate").val(ID);  

	      var data = $.parseJSON(
	  	      $.ajax({
	  	          type         : "GET",
	  	          url          : "http://localhost:8080/library/book/" + ID,
	  	          contentType  : "application/json",
	  	      	  dataType     : "json",
	      		  async: false
	  	      }).responseText
	  	  );

	  	  var dataJson = JSON.parse(JSON.stringify(data), function (key, value) {
	       		var formInput = $('[name='+ key +']', form);  
	       		formInput.val(value);   
	      });
		
	});

	$("#bookFormUpdate").submit(function(event){
	      event.preventDefault();

	      var ID = $("#submitFormUpdate").val();
	      var form = $(this);
	      var dataForm = ConvertFormToJSON(form);

	      updateData(ID, JSON.stringify(dataForm)); 
	});
 
  function updateData(ID, data_json){
	  $.ajax({
	      type       	: "PUT",
	      url     		: "http://localhost:8080/library/book",
	      data       	: data_json,
	      contentType   : "application/json",
		    dataType  	: "json",
          beforeSend : function(xhr){
              xhr.setRequestHeader("Authorization", "Bearer " + getToken());
          },
	      success: function(response){
	      	jQuery("#updateModal").modal("hide");
	      	viewAll();
          },
          error: function(jqXHR, response, errorThrown){
              localStorage.removeItem("token");
                  location.href = "http://localhost/member";
          }
	  }).responseText;

  }



	/* =========================== DELETE DATA ==========================*/
	$(document).on("click", "#deleteButton", function(){
	      var ID = $(this).val();
	  
	      $.ajax({
	          type        : "DELETE",
	          url         : "http://localhost:8080/library/book/" + ID,
              beforeSend : function(xhr){
                  xhr.setRequestHeader("Authorization", "Bearer " + getToken());
              },
	          success: function(response){
	          	viewAll();
	          },
              error: function(jqXHR, response, errorThrown) {
                  localStorage.removeItem("token");
                  location.href = "http://localhost/member";
              }});
	});


    $(document).on("click", "#logout", function(){
    	localStorage.removeItem("token");
    	location.href = "http://localhost:80/member/index.html";
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

	/* ============================================== ALERT ========================================================*/
	function showAllert(code, message, description){
	    if (code == 200) {
	        $("#alert").append(
	           '<div class="alert alert-success " id="alert-success">' + 
	              '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
	              '<h4><i class="icon fa fa-check"></i> '+ message +'</h4>'+ description +
	            '</div>'    
	        );
	    }
	    else{
	        $("#alert").append(
	           '<div class="alert alert-danger " id="alert-success">' + 
	              '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'+
	              '<h4><i class="icon fa fa-ban"></i> '+ message +'</h4>'+ description +
	            '</div>'    
	        );
	    }
	}

  

});