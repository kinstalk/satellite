//list
function run(id) {
    $.ajax({
        url: '/run/run_timer.vm',
        data: {'id':id},
        type: "POST",
        success: function (data) {

            if(data=='处于运行状态'){

                swal("WARN", "处于运行状态,不能再重复执行", "");
                // alert(data);
                return;

            }
            window.location.reload();
        }
    });

}

function deleteagent(id) {
    layer.confirm('确认删除本条数据吗？', {
        time: 0 //不自动关闭
        ,btn: ['删除', '取消']
        ,yes: function(index){
            $.ajax({
                url: '/agent/delete.vm',
                data: {'id':id},
                type: "POST",
                success: function (data) {

                    if(data=='fail'){
                        swal("WARN", "删除失败", "");

                        return;
                    }
                    window.location.reload();
                }
            });
        }
    });
}

function checkOnline() {

    $.ajax({
        url: '/agent/check.vm',
        type: "POST",

        beforeSend:function(XMLHttpRequest){
            swal("WARN", "正在连接请稍后...", "");
        },
        success: function (data) {

            if (data == 'success') {

                window.location.reload();

            } else if(data == 'none'){
                swal("WARN", "全部在线", "");
                return;
            }else{


                //swal("WARN","Agent("+data+")连接失败", "");

                swal({
                    title: "WARNING",
                    text: "Agent("+data+")连接失败",
                    type: "warning",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    confirmButtonText: "ok",
                    confirmButtonColor: "#ec6c62"
                }, function() {

                        window.location.reload();

                });

            }

        }
    });

}

//new_edit

function btnadd() {
    if(!$("#newEditForm").valid()) {
        return;
    }

    $.ajax({
        url: '/agent/save.vm',
        data: $("#newEditForm").serialize(),
        type: "POST",
        success: function (data) {
            window.location.href = "list.vm";
        }
    });
}
//detail

function finddetail(timerid,agentid) {

    addModal("view_detial","明细列表", function(obj) {

        $.ajax({

            url: '/agent/finddetail.vm',
            data: {'agentid':agentid,"timerid":timerid},
            type: "POST",
            success: function (data) {


                $(obj).html(data);
                //window.location.reload();
            }
        });
    },700,300,60,"true");

}


