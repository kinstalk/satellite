//list
function deletescript(id) {

    layer.confirm('确认删除本条数据吗？', {
        time: 0 //不自动关闭
        ,btn: ['删除', '取消']
        ,yes: function(index){
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
    });

}
//new_edit

var swfu;
$(document).ready(function () {

    $("#btn_update").click(function () {



        if($("#updatepath").val().indexOf("/")<0){
            swal("WARN",$("#updatepath").val(), "");
            return false;
        }
        if(!$("#scriptForm").valid()) {
            return;
        }

        $.ajax({
            url: '/script/save.vm',
            data: $("#scriptForm").serialize(),
            type: "POST",
            success: function () {
                window.location.href = "list.vm";
            }
        });
        return false;
    });

    var settings = {
        flash_url : "/js/plugins/swfupload/swfupload.swf",
        flash9_url : "/js/plugins/swfupload/swfupload_fp9.swf",
        upload_url: "/upload/file.vm",
        post_params: {"111" : "aaa"},
        file_size_limit : "100 MB",
        file_types : "*.jmx",
        file_types_description : "Jmeter Script Files",
        file_upload_limit : 100,
        file_queue_limit : 0,
        custom_settings : {
            progressTarget : "fsUploadProgress",
            cancelButtonId : "btnCancel",
            progressTarget : "upload-queue",//显示上传文件队列信息的标签类
            cancelButtonId : "btnCancel"//取消所有上传文件的按钮标签类
        },
        debug: false,

        // Button settings
        button_image_url: "",
        button_width: "80",
        button_height: "30",
        button_placeholder_id: "spanButtonPlaceHolder",
        button_text: '选择文件',
        //button_text_style: "",
        button_text_left_padding: 10,
        button_text_top_padding: 10,


        // The event handler functions are defined in handlers.js
        swfupload_preload_handler : preLoad,
        swfupload_load_failed_handler : loadFailed,
        file_queued_handler : fileQueued,
        file_queue_error_handler : fileQueueError,
        file_dialog_complete_handler : fileDialogComplete,
        upload_start_handler : uploadStart,
        upload_progress_handler : my_uploadProgress,
        upload_error_handler : uploadError,
        upload_success_handler : uploadSuccess,
        upload_complete_handler : uploadComplete,
        queue_complete_handler : queueComplete	// Queue plugin event
    };

    if($("#spanButtonPlaceHolder").size() > 0) {
        swfu = new SWFUpload(settings);
    }

    function uploadStart(file){
        $("#upload_bar").show();
    }

    // 显示进度
    function my_uploadProgress(file, bytesLoaded, bytesTotal) {
        try {
            var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
            if (percent === 100) {
                $('.progress-bar').css('width','100%');
                //$('.progress-bar').html('100%');
            } else {
                $('.progress-bar').css('width',percent+'%');//用自定义进度条
                //$('.progress-bar').html(percent+'%');
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    //上传成功后函数
    function uploadSuccess(file, serverData) {
        if(serverData=='exception'){
            showErrTips("上传失败");
        } else {
            $("#updatepath").attr("value", serverData)
        }
        $("#upload_bar").hide();
    }

    function uploadError(file, serverData) {
        showErrTips("上传失败");
        $("#upload_bar").hide();
    }


});

