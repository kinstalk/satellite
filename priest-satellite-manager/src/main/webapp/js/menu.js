;$(document).ready(function() {
    // 绑定菜单单击事件
    $("#menu a.menu").click(function(){
        // 一级菜单焦点
        $("#menu li.menu").removeClass("active");
        $(this).parent().addClass("active");
        // 左侧区域隐藏
        if ($(this).attr("target") == "mainFrame"){
            $("#left,#openClose").hide();
            return true;
        }
        // 显示二级菜单
        var menuId = "#menu-" + $(this).attr("data-id");
        if ($(menuId).length > 0){
            $("#left .accordion").hide();
            $(menuId).show();
        }else{
            // 获取二级菜单数据
            $.get($(this).attr("data-href"), function(data){
                if (data.indexOf("id=\"loginForm\"") != -1){
                    alert('未登录或登录超时。请重新登录，谢谢！');
                    top.location = "./a";
                    return false;
                }
                $("#left .accordion").hide();
                $("#left").append(data);

            });
        }
        // 链接去掉虚框
        //$(menuId + " a").bind("focus",function() {
        //    if(this.blur) {this.blur()};
        //});
        
        // 二级标题
        $(menuId + " .accordion-heading a").click(function(){
            // 所有二级标题 标识还原为合并
            $(menuId + " .accordion-heading a i").removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-right');
            
            // 标识打开的二级标题
            if(!$($(this).attr('data-target')).hasClass('in')){
                $(this).children("i").removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-down');
            } 
        });

        return false;
    });
    
    // 初始化点击第一个一级菜单
    $("#menu a.menu:first").click();
    
    //页面跳转后,自动打开左侧菜单
    //$(".accordion-body a").each(function () {
    //    cur_href = $(this).attr("href");
    //    if( cur_href.substring(0, cur_href.lastIndexOf("/")) == request_uri.substring(0, request_uri.lastIndexOf("/"))) {
    //        //alert($(this).parent().html());
    //        //alert($(this).parent().prev().children("a").children("i").size())
    //        $(this).parent().removeClass("in").addClass("in");
    //        $(this).parent().prev().children("a").children("i").removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-down');;
    //
    //    }
    //});

    // 二级内容
    $(".show-inner").click(function(){
        var inner_html = '<div class="panel panel-default" id="right-content"><iframe class="J_iframe" id="iframe" name="iframe" width="100%" height="'+($(window).height()-90)+'px" src="' + $(this).attr("href") + '" frameborder="0" seamless></iframe></div>';
        $("#right").html(inner_html);
        return false;
    });
});