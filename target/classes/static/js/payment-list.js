$(document).ready(function() {
	var table = $('#employeesTable').DataTable({
		"order" : [ [ 1, "desc" ] ]
	});
	process_data = function(id) {
		if (confirm("do you want to process?")) {
			$.ajax({
				type : "GET",
				url : "/payments/process.html",
				cache: true,
				data : {
					id : id
				},
				success : function(data) {
					location.reload(true);
					console.log("Successfully");
				},
				error : function(jqXHR, textStatus, errorThrown) {
                    alert('Error: ' + textStatus + ' ' + errorThrown);
				}
			})
		}
	}

	void_data = function(id) {
		if (confirm("do you want to void?")) {
			$.ajax({
				type : "GET",
				url : "/payments/void.html",
				data : {
					id : id
				},
				success : function(data) {
					location.reload(true);
					console.log("Successfully");
				},
				error : function(e) {
					console.log("ERROR : ", e);
				}
			})
		}
	}
	
});