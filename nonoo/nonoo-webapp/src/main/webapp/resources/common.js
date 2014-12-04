
(function($) {
	var zIndex = 100;
	// 消息框
	var $message;
	var messageTimer;
	$.message = function() {
		var message = {};
		if ($.isPlainObject(arguments[0])) {
			message = arguments[0];
		} else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
			message.type = arguments[0];
			message.content = arguments[1];
		} else {
			return false;
		}
		
		if (message.type == null || message.content == null) {
			return false;
		}
		
		if ($message == null) {
			
			$message = $('<div class="alert alert-' + message.type + ' alert-dismissible">  <\/div>');
			$message.appendTo("body");
		}
		
		
		$message.children("div").removeClass(" alert-success alert-info  alert-warning alert-danger").addClass("alert-" + message.type);
		$message.html(message.content);
		$message.css({"margin-top": 10,"z-index": zIndex ++}).show();
		clearTimeout(messageTimer);
		messageTimer = setTimeout(function() {$message.hide();}, 3000);
		return $message;
	}

	
	
	
})(jQuery);

$(document).ajaxSend(function(event, request, settings) {
	
	    var token = $.cookie("token");
		if (token != null) {
			request.setRequestHeader("token", token);
		}
	
});



//token 令牌
$().ready(function() {
	
	$("form").submit(function() {
		var $this = $(this);
		if ($this.attr("method") != null && $this.attr("method").toLowerCase() == "post" && $this.find("input[name='token']").size() == 0) {
			var token = $.cookie("token");
			if (token != null) {
				
				$this.append('<input type="hidden" name="token" value="' + token + '" \/>');
			}
		}
		
	});

});
