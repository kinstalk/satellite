function btnadd() {


    if(!$("#configForm").valid()) {
        return;
    }
        var groups=$("#groups").val();
        var size=$("#size").val();




        if(groups==null||groups==''){
            swal("WARN", "请填写分组数", "");
            return;
        }
        if(size==null||size==''){
            swal("WARN", "每组号码数", "");
            return;
        }


        $.ajax({
            url: '/pool/save.vm',
            data: $("#configForm").serialize(),
            type: "POST",
            success: function (data) {

                if(data=='success'){

                    swal("WARN", "保存成功", "");


                }else{
                    swal("WARN", "保存失败", "");
                }
            }
        });
    return false;

}