$(document).ready(
		function() {
			$("#orderNumber").blur(
					function(event) {
						event.preventDefault();
						$.ajax({
							type : "GET",
							url : "/salesorders/findbynumber",
							data : {
								number : $(this).val()
							},
							success : function(data) {
								$('#customerName').val(data.customer.firstName+' '+data.customer.lastName);
								$('#paymentAmount').val(data.totalOutstanding);
								$('#totalCharged').val(data.paymentNet);
								$('#totalDue').val(data.totalOutstanding);
								$('#totalPaid').val(data.totalPaid);
							},
							error : function(e) {
								$('#customerName').val('');
								$('#paymentAmount').val('0');
								console.log("ERROR : ", e);
							}
						})
					});

		});