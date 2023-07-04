var mycart = null;

$(document).ready(() => {
	loadCartdata();

	combineTimeBar();
});

function loadCartdata(){
	var url = '/user/load-cart';
	$.ajax({
		url: encodeURI(url),
		dataType: 'json',
		cache: false,
		async: false,
		success: function(response) {
			if (response.status == 'error') {
				if (response.code == '403')
					loginRequest();
				return false;
			}
			if (response.status == 'ok') {
				eval("mycart=" + request.data);
				showCart();
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			debugOutput(textStatus);
		}, 
		complete: function(data) {
			combineImageAndCharge();
		}
	});
}

function loginRequest() {
	window.location.href = '/member/logout';
}

/**
{
	"cart_id" : 1,
	"user_id" : 1,
	"total_price" : 123,
	"total_item" : 1
	"item" : [
		{
			"id" : 1,
			"name : "product_name",
			"price" : 123,
			"in_stoke" : 5,
			"amount" : 1,
			"image_url" : "1.png"
			"category_id" : 1
		}
	]
}
 */
 /**
 	<form class="cart-table">
        <fieldset>
            <div>
                <span>item: 2</span>
            </div>
            <div>
                <table class="cart-product">
                    <tr>
                        <th>product</th>
                        <th>price</th>
                        <th>size</th>
                        <th>total</th>
                    </tr>
                    <tbody>
                        <tr>
                            <td style="display: flex; flex-direction: row">
                                <div class="product-name">
                                    <h3>product name</h3>
                                    <image height="50px" src="https://images.ctfassets.net/23aumh6u8s0i/4JFn93iA5DZgomgcIPJOPx/700a59ae9668acf22df959dcf45b409a/spring"></image>
                                </div>
                                <div>
                                    product description
                                </div>
                            </td>
                            <td>123</td>
                            <td>2</td>
                            <td>246</td>
                        </tr>
                        
                    </tbody>
                </table>
            </div>
            <div>
                <span>total: 246</span>
            </div>
        </fieldset>
    </form>
  */
function showCart(){
	
}