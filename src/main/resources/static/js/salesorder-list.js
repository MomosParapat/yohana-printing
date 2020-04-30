	$(document).ready(function() {
		var table = $('#employeesTable').DataTable({
			"order" : [ [ 1, "desc" ] ]
		});
		delete_data = function(id) {
			if (confirm("do you want to delete?")) {
				$.ajax({
					type : "GET",
					url : "/salesorders/delete",
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
