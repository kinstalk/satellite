/**
 * 新增Modal弹出框
 * @param newModalId 指定新增的弹出框id
 * @param newModalName 指定新增的弹出框显示名称
 * @param modalCallBack 回调函数 
 * @param width 弹出框的宽度
 * @param height 弹出框的高度
 * @param paddingtop 弹出框的位置
 * each engineer has a duty to keep the code elegant
 */
function addModal(newModalId,newModalName,modalCallBack,width,height,paddingtop,isclose){
	contentHTML = "";
	// 设置宽度
	width = width ? width + "px" : '700px';
	// 设置高度
	height = height ? height + "px" : 'auto';

	// 清理旧叠加
	$('#'+newModalId).modal('hide');
	$('#'+newModalId+'_div').remove();
	// 初始化提示框代码
	modalModalHTML = '<div id="'+newModalId+'_div"><div class="modal fade" id="'+newModalId+'" tabindex="-1" role="dialog" data-backdrop="static" style="overflow: auto;" aria-labelledby="'+newModalId+'Label" aria-hidden="true">';
	modalModalHTML += '<div class="modal-dialog" style="left:0px;width:'+width+'; '
	modalModalHTML = paddingtop?(modalModalHTML +'padding-top:'+paddingtop+'px;'):modalModalHTML;
	modalModalHTML +='">';
	modalModalHTML += '<div class="modal-content">';
	modalModalHTML += '<div class="modal-header">';
	if(isclose=='true'){
		modalModalHTML += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
	}

	modalModalHTML += '<h4 class="modal-title" id="'+newModalId+'Label">';
	modalModalHTML += newModalName;// 标题
	modalModalHTML += '</h4></div>';                                                                    //overflow: auto;滚动条(暂去)
	modalModalHTML += '<div class="modal-body" id="modal-body-'+newModalId+'"  style="height:'+height+';">';
	modalModalHTML += '<div class="tab_loading">&nbsp;</div>'; //加入正在加载的提示
	modalModalHTML += '</div>';
	modalModalHTML += '<div class="modal-footer" id="modal-footer-'+newModalId+'" style="border:0">';
	modalModalHTML += '</div></div></div></div></div>';
	
	$('body').append(modalModalHTML);
	if(modalCallBack){
		//shown.bs.modal modal显示之后 ，show.bs.modal modal显示之前 ,隐藏之后hidden.bs.modal,hide.bs.modal隐藏之前
		$('#'+newModalId).one('shown.bs.modal', function (e) {
			modalCallBack($("#modal-body-"+newModalId).get(0));
		})
	}
	$('#'+newModalId).modal('show');
}

/**
 * 关闭Modal弹出框
 * @param newModalId 弹出框id
 * each engineer has a duty to keep the code elegant
 */
function closeModal(newModalId){
	$('#'+newModalId).modal('hide');
}
