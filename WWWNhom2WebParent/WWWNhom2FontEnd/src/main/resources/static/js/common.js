$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
	var timeRun = loadListCartCus();
	customDropdownMenu();
	setTimeout(quantityField, timeRun*1000);
	setTimeout(quantityPort, timeRun*1000);
	
	var action = $.urlParam = urlParam("action");
	
});

function urlParam(name){
	    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	    if (results==null) {
	       return null;
	    }
	    return decodeURI(results[1]) || 0;
}

function quantityPort() {
	$(".quantity-button").on("click", function(e) {
		e.preventDefault();
		var href = $(this).attr("href");
		$.get(href, function(data) {
			loadListCartCus();
		})
	});
}

function quantityField() {
	$(".quantityField").each(function(index) {
		if ($(this).text() == 1) {
			$(this).parent().children(':first-child').addClass('disable');
		}
	});
}
function customDropdownMenu() {
	$(".dropdown").hover(
		function() {
			$(this).find('.dropdown-menu').stop(true, true).delay(250).slideDown();
		},
		function() {
			$(this).find('.dropdown-menu').stop(true, true).delay(100).slideUp();
		}
	);

	$(".dropdown > a").click(function() {
		location.href = this.href;
	});
}

function loadListCartCus() {
	contextPath = "http://localhost/Nhom2/customers/get_email_authen";
	var timeStart = $.now();
	$.get(contextPath, function(data) {
		contextPathCart = "http://localhost/Nhom2/customers/get_cart/" + data;
		$.get(contextPathCart, function(listCart) {
			let total = 0;
			let htmlListItem = "";
			for (var i = 0; i < listCart.length; i++) {
				var imagePath = "/Nhom2" + listCart[i].image;
				var htmlItem = `
				  				<li>
					  				<a href="/Nhom2/delete-to-cart/${listCart[i].id}" class="remove" title="Remove this item">
					  					<i class="fa fa-remove"></i>
					  				</a>
					  				<a class="cart-img" href="/Nhom2/p/${listCart[i].alias}">
					  					<img src="${imagePath}" alt="#">
					  				</a>
									<h4>
										<a href="/Nhom2/p/${listCart[i].alias}">${listCart[i].name}</a>
									</h4>
									<p class="quantity">
										<span>
											<a class="quantity-button" href="/Nhom2/update-to-cart/${listCart[i].id}/${listCart[i].quantity}?status=down">
												<i class="fa fa-minus-circle"></i>
											</a>
											&nbsp;&nbsp;<span class="quantityField">${listCart[i].quantity}</span>&nbsp;&nbsp;
											<a class="quantity-button" href="/Nhom2/update-to-cart/${listCart[i].id}/${listCart[i].quantity}?status=up">
												<i class="fa fa-plus-circle"></i>
											</a>
										</span>
										&nbsp;&nbsp;-&nbsp;&nbsp;
										<span class="amount">$${listCart[i].discountPrice * listCart[i].quantity}</span>
									</p>
								</li>`;
				htmlListItem += htmlItem;
				total += listCart[i].discountPrice * listCart[i].quantity;
			}

			document.getElementById('dropdown-cart-header').innerHTML = `
							<span>${listCart.length} Items</span>
							<a href="/Nhom2/customers/view-cart">View Cart</a>
						`;
			document.getElementById('shopping-list').innerHTML = htmlListItem;
			document.getElementById('total').innerHTML = `
							<span>Total</span> <span class="total-amount">$${total}</span>
						`;
			document.getElementById('total-count').innerHTML = listCart.length;
		});
	})
	var timeEnd = $.now();
	return timeEnd - timeStart;
}

