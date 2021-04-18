// 生成树形结构函数
function generateTree() {
    // 1.准备生成树形结构JSON数据来源发送Ajax请求
    $.ajax({
        "url":"menu/get/whole/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function (response) {
            var result = response.result;
            if(result == "SUCCESS"){

                // 2.创建JSON对象用于存储对zTree所做的设置
                var setting = {
                    "view":{
                        "addDiyDom":myAddDiyDom,
                        "addHoverDom":myaddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "key":{
                            "url":"maomi"
                        }
                    }
                };

                // 3.从响应体中获取用来生成树形结构的JSON数据
                var zNode = response.data;

                // 4.初始化树形结构
                $.fn.zTree.init($("#treeDemo"),setting,zNode);
            }
            if(result == "fAILED"){
                layer.msg(response.message);
            }
        }
    });
}

// 在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {

    // 拼接按钮组id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 移除对应的元素
    $("#"+btnGroupId).remove();
}

// 在鼠标移入节点范围时添加按钮组
function myaddHoverDom(treeId,treeNode) {
    // 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>

    // 为了需要在移除按钮组的时候能够精确定位到按钮组所在的span，需要给span设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 判断一下以前是否添加了按钮组
    if($("#"+btnGroupId).length > 0)
    {
        return;
    }

    // 准备各个按钮的HTML标签
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='删除节点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 修改节点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 获取当前结点的级别
    var level = treeNode.level;

    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";

    // 判断当前节点的级别
    if(level == 0)
    {
        // 级别为 0 时是根节点，只能添加子节点
        btnHTML = addBtn;
    }
    if(level == 1)
    {
        // 级别为 1 时是分支节点，可以添加子节点、修改
        btnHTML = addBtn + " " + editBtn;

        // 获取当前节点的子节点数量
        var length = treeNode.children.length;

        // 如果没有子节点，可以删除
        if(length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }
    if(level == 2) {
        // 级别为 2 时是叶子节点，可以修改、删除
        btnHTML = editBtn + " " + removeBtn;
    }

    var anchorId = treeNode.tId + "_a";

    //执行在超链接后面附加span元素的操作
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHTML + "</span>");

}

// 修改默认的图标
function myAddDiyDom(treeId,treeNode)
{
    // treeId是整个树形结构附着的ul标签的id
    console.log("treeId = " +treeId);

    // treeNode是当前树形节点的全部数据，包括我们从后端得到的Menu对象的全部属性
    console.log(treeNode);

    // 根据id生成规则怕拼接出来span标签的id
    var spanId = treeNode.tId + "_ico";

    // 根据控制图标的span 标签的id找到这个span标签
    // 删除旧的class
    $("#"+spanId)
        .removeClass()
        .addClass(treeNode.icon);

}