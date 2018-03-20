var curMenu = null, zTree_Menu = null;
var setting = {
	view: {
		showLine: false,
		showIcon: false,
		selectedMulti: false,
		dblClickExpand: false,
		addDiyDom: addDiyDom
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	 check: {
	        enable: true
//	        ,chkboxType : { "Y" : "", "N" : "" }
	    },
	async: {
		enable: true,
	       url: getBaseURL()+"/client/cmt/InfoManage!getSendUsers.action?flag="+$('#flag').val()+"&informationid="+$('#informationid').val(),
		autoParam: ["id=treeNode.id"]
		//dataFilter: treefilter 配置此属性对节点数据做特殊处理
	},
	callback: {
		beforeClick: beforeClick
	}
};

/*function treefilter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}*/

function addDiyDom(treeId, treeNode) {
/*	console.log("**********"+treeNode.tId+"......"+treeNode.id);*/
	var spaceWidth = 5;
	$("#" + treeNode.tId + "_span").attr("userid",treeNode.id);
	$("#" + treeNode.tId + "_check").attr("userid",treeNode.id);
	
	/*var switchObj = $("#" + treeNode.tId + "_switch"),
	icoObj = $("#" + treeNode.tId + "_ico");
	switchObj.remove();
	icoObj.before(switchObj);
	if (treeNode.level > 1) {
		var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
		switchObj.before(spaceStr);
	}*/
}

function closeAllTab(){
	var tabname = ['resource_list_input','resource_list'];
	for(var i=0; i<tabname.length; i++){
		if($('#'+tabname[i]+'_name').length>0){	
			removeTab(tabname[i],"resourcetab");
		}
	}
}

function beforeClick(treeId, treeNode,viewtreediv) {
	//在此处添加事件发送请求更新右侧的列表
	removeAllTabs("resourcetab");
	 var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	   nodes = zTree.getCheckedNodes(true);
}
//checkbox_true_full

function initTree(){
	var viewtreediv = "treeDemo";
	treeObj = $("#"+viewtreediv);
	if($.fn.zTree){
		$.fn.zTree.init(treeObj, setting);
		treeObj.hover(function () {
			if (!treeObj.hasClass("showIcon")) {
				treeObj.addClass("showIcon");
			}
		}, function() {
			treeObj.removeClass("showIcon");
		});
	} else {
		window.setTimeout(initTree, 100);
	}
};
initTree();
