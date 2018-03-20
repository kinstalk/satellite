//list
function deletescript(id) {
    $.ajax({
        url: '/script/delete.vm',
        data: {'id':id},
        type: "POST",
        success: function (data) {
            if(data=='script'){
                showWarnTips("该脚本正在被使用")
                return;
            }
            if(data=='false'){
                showErrTips("删除失败")
                return;
            }

            showSuccTips("删除成功")
            window.location.reload();
        }
    });
}

function run(id) {
    $.ajax({
        url: '/run/run_timer.vm',
        data: {'id':id},
        type: "POST",
        success: function (data) {

            if(data=='phoneNull'){

                swal("WARN", "请配置手机号,无可用手机号", "");
                // alert(data);
                return;

            } if(data=='phoneNull'){
                swal("WARN", "运行失败", "");
                // alert(data);
                return;
            }
            if(data=='NotOnline'){
                swal("WARN", "无在线Agent", "");
                // alert(data);
                return;
            }

            window.location.reload();
        }
    });

}

function stop(id) {


    $.ajax({
        url: '/run/run_stop.vm',
        data: {'id':id},
        type: "POST",
        success: function (data) {



            window.location.reload();
        }
    });

}

function deletetimer(id) {


    swal({
        title: "您确定要删除这条信息吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false
    }, function () {

        $.ajax({
            url: '/run/delete.vm',
            data: {'id':id},
            type: "POST",
            success: function (data) {
                if(data=='run'){

                    swal("WARN", "处于运行状态,不能删除", "");

                    return;

                }
                if(data=='fail'){
                    swal("WARN", "删除失败", "");

                    return;
                }
                swal("删除成功！", "您已经永久删除了这条信息。", "success");
                window.location.reload();
            }
        });

    });


}
function finddetail(id) {

    addModal("view_detial","明细列表", function(obj) {

        $.ajax({

            url: '/run/finddetail.vm',
            data: {'id':id},
            type: "POST",
            success: function (data) {


                $(obj).html(data);
                //window.location.reload();
            }
        });
    },700,300,60,"true");

}
function addtask() {

    addModal("add_task","", function(obj) {

        $.ajax({

            url: '/run/addtask.vm',
            type: "POST",
            success: function (data) {


                $(obj).html(data);
                //window.location.reload();
            }
        });
    },700,350,60);

}
//new_edit

function btnadd() {

    if(!$("#runForm").valid()) {
        return;
    }
    var name=$("#name").val();
    var filepath=$("#scriptId").val();
    var textmoblie=$("#noticePhone").val();
    var noticeEmail=$("#noticeEmail").val();


        //var regexMoblie = /(^1[3|4|5|8][0-9]\d{8}$)|(1[3|4|5|8][0-9]\d{8};){1,}(1[3|4|5|8][0-9]\d{8}$)/;
        //if (!regexMoblie.test(textmoblie)){
        //    alert("手机号格式不正确");
        //    return false;
        //}


        if(filepath=='===请选择==='||filepath==null||filepath==''){

            showWarnTips("请选择脚本");
            return ;
        }
        //if(noticeEmail==null||noticeEmail==''){
        //
        //    alert("请填邮箱");
        //    return false;
        //}
        //
        //
        //
        //
        //var regexEmail =  /^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+(net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT)$/;
        //if (!regexEmail.test(noticeEmail)){
        //    alert("邮箱格式不正确");
        //    return false;
        //}



    $.ajax({
        url: '/run/save.vm',
        data: $("#runForm").serialize(),
        type: "POST",
        success: function (data) {

            if("false"==data){
                showWarnTips("保存失败");
                return false;
            }else{
                window.location.href = "list.vm";
            }


           // window.location.reload();
        }

    });
    return false;
}
function changeSelect(obj){

    var selectvalue=obj.val();

    if(selectvalue=='1'){
        $("#sleeptime").attr("class", "hidden");
    }else{
        $("#sleeptime").attr("class", "form-group");
    }

}