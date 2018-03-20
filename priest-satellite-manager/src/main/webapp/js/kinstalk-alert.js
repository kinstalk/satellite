/**
 * 内置样式:layui-layer-molv, layui-layer-lan
 */
//加载您的扩展样式
layer.config({
    extend: ['skin/success/style.css',
        'skin/info/style.css',
        'skin/warn/style.css',
        'skin/progress/style.css',
        'skin/error/style.css'
    ]
});
function showSuccTips(cont) {
    showTips("succ", cont);
}
function showWarnTips(cont) {
    showTips("warn", cont);
}
function showErrTips(cont) {
    showTips("error", cont);
}

function showTips(cont) {
    showTips("info", cont);
}
function showTips(type, cont) {
    if(cont == null || cont == "") {
        cont = "默认提示信息";
    }
    var offset = $("#right").offset();
    var skin = 'layer-ext-green';
    var time = 1000;
    
    if(type == "succ") {
        skin = 'layer-ext-success';
    } else if(type == "warn") {
        skin = 'layer-ext-warn';
        time = 4000;
    } else if(type == "error") {
        skin = 'layer-ext-error';
        time = 5000;
    } else {
        time = 3000;
        skin = 'layer-ext-info';
    }
   
    //alert(skin);
    //alert(offset.top + "," + offset.left +","+ $("#right").width());
    layer.open({
        type: 1,
        skin: skin, //样式类名
        //skin: 'layui-layer-molv',
        area: $("#right").width()+'px',
        offset: ['4px', (offset.left+15) + 'px'],
        closeBtn: 0, //显示关闭按钮
        shift: 5,
        shade: 0,//[0.8, '#393D49'],
        time:time, // 显示时间(秒)
        shadeClose: true, //开启遮罩关闭
        title:cont,
        content: false
    });
}

function showProgress() {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        shadeClose: true,
        skin:'layer-ext-progresss',
        area: ['200px','20px'],
        content: '<div class="col-md-12">'+
        '<div class="progress progress-striped active">'+
        '<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="1" aria-valuemin="0" aria-valuemax="100" style="width:1%">'+
        ''+
        '</div>'+
        '</div>'+
        '</div>'
    });
}
