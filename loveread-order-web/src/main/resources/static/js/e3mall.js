var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("LOVEREAD_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8085/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到爱阅书城！<a href=\"http://localhost:8085/user/logout/\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
					$("#myOrder").css("display","inline");
					$("#myMenus").css("display","block");
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});