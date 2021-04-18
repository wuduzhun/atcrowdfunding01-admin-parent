// 声明专门的函数用来再分配Auth的模态框中显示Auth的树形结构
function fillAuthTree() {

    // 发送Ajax请求查询Auth数据
    var ajaxResult= $.ajax({
        "url":"assign/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false
    });

    if(ajaxResult.status != 200)
    {
        layer.msg("请求处理出错！响应状态码是："+ajaxResult.status+"说明是："+ajaxResult.statusText);
        return;
    }

    // 从响应结果获取Auth的JSON数据
    // 从服务器端查询到的list不需要组装成树形结构，这里我们交给zTree去组装
    var authList = ajaxResult.responseJSON.data;

    // 准备对zTree进行设置的JSON对象
    var setting = {
        "data":{
            "simpleData":{

                // 开启简单JSON功能
                "enable":true,

                // 使用categoryId属性关联父节点，不用默认Pid
                "pIdKey":"categoryId"
            },
            "key":{
                "name":"title"
            }
        },
        "check":{
            "enable":true
        }
    };

    // 生成树形结构
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"),setting,authList);

    // 获取zTreeObj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

    // 调用zTreeObj对象方法，把节点展开
    zTreeObj.expandAll(true);

    // 查询已分配Auth的id组成的List
    ajaxResult = $.ajax({
        "url":"assign/get/assigned/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false
    });

    if(ajaxResult.status != 200)
    {
        layer.msg("请求处理出错！响应状态码是："+ajaxResult.status+"说明是："+ajaxResult.statusText);
        return;
    }

    // 从响应结果中获取authIdArray
    var authIdArray = ajaxResult.responseJSON.data;

    // 根据authIdArray把树形结构对应的节点勾选上
    // 遍历authIdArray数组
    for (var i = 0; i < authIdArray.length; i++)
    {
        var authId = authIdArray[i];

        // 根据id查询树形结构对应的节点
        var treeNode = zTreeObj.getNodeByParam("id",authId);

        // 将treeNode设置为勾选

        // checked设置为true表示节点勾选
        var checked = true;

        // checkTypeFlag设置false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;

        // 执行
        zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
    }
}

// 声明专门的函数显示模态框
function showConfirmModal(roleArray)
{
    // 打开模态框
    $("#confirmModal").modal("show");

    // 清除旧的数据
    $("#roleNameDiv").empty();

    // 在全局变量范围创建数组用来存放角色id
    window.roleArray = [];
    // 遍历数组
    for(var i = 0; i < roleArray.length; i++)
    {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br>");

        var roleId = role.roleId;

        // 调用数组对象的push()方法存入新元素
        window.roleArray.push(roleId);
    }

}


// 执行分页，生成页面效果
function generatePage() {
    // 1.获取分页数据
    var pageInfoRemote = getPageInfoRemote();
    // 2.填充表格
    fillTableBody(pageInfoRemote);
}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {
    var ajaxRs = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });
    var statusCode = ajaxRs.status;
    // 发生错误
    if (statusCode != 200) {
        layer.msg("服务器端程序调用失败！响应状态码是=" + statusCode + "说明信息=" + ajaxRs.statusText);
        return null;
    }
    // 如果响应状态码是200表示请求成功
    var resultEntity = ajaxRs.responseJSON;
    // 从resultEntity中获取result属性
    var result = resultEntity.result;
    // 判断result是否成功
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }
    // 确认result为成功后获取pageInfo
    var pageInfo = resultEntity.data;
    // 返回pageInfo
    return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo) {
    // 清除tbody中的旧的数据
    $("#rolePageBody").empty();
    $("#Pagination").empty();
    // 为了搜索没有结果时不显示页码
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有查询到您要的数据！</td></tr>");
        return;
    }
    // 使用pageInfo的list属性填充tbody
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkboxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>"
        var roleNameTd = "<td>" + roleName + "</td>"

        // 通过button标签的id属性把roleId值传递到button按钮的单击响应函数中
        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {
// 获取总记录数
    var totalRecord = pageInfo.total;
// 声明相关属性
    var properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageSize,
        "current_page": pageInfo.pageNum - 1,
        "prev_text": "上一页",
        "next_text": "下一页"
    }
// 调用 pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
// 修改 window 对象的 pageNum 属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}