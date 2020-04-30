	$(document).ready(function() {
		var table = $('#employeesTable').DataTable({
			"order" : [ [ 0, "desc" ] ]
		});
		delete_data = function(id) {
			if (confirm("do you want to delete?")) {
				$.ajax({
					type : "GET",
					url : "/items/delete",
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
