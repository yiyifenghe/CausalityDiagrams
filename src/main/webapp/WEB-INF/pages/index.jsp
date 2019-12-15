<%--
  Created by IntelliJ IDEA.
  User: yiyif
  Date: 2019/12/10
  Time: 21:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>系统基模分析</title>
    <script language="JavaScript" src="${pageContext.request.contextPath}/static/scripts/boot.js"></script>
    <style type="text/css">
        #matrix td {
            padding: 0px 5px;
            text-align: center;
        }
    </style>
</head>
<body>
<h1>系统基模分析</h1>
<fieldset id="fd1" style="width:100%;">
    <legend><h3>输入关系矩阵</h3></legend>
    <div class="fieldset-body">
        <table class="form-table" border="0" cellpadding="1" cellspacing="2" style="width: 100%;">
            <tr>
                <td class="form-label" style="width:150px;">关系矩阵</td>
                <td>
                    <input name="arrStr" class="mini-textarea" height="150px" style="width: 100%;"/>
                </td>
            </tr>
        </table>
    </div>
</fieldset>
<div id="toolbar0" class="mini-toolbar" style="padding:2px; margin-top: 5px">
    <table style="width:100%;">
        <tr>
            <td style="width: 100%;">
                <a class="mini-button" iconCls="icon-node" onclick="formatMatrix()">检查矩阵</a>
            </td>
        </tr>
    </table>
</div>

<fieldset id="fd2" style="width:100%;">
    <legend><h3 id="matrixSize">关系矩阵</h3></legend>
    <table id="matrix">
        <tr>
            <td>0</td>
            <td>1</td>
            <td>0</td>
        </tr>
        <tr>
            <td>1</td>
            <td>0</td>
            <td>1</td>
        </tr>
        <tr>
            <td>1</td>
            <td>0</td>
            <td>0</td>
        </tr>
    </table>
</fieldset>
<div id="toolbar1" class="mini-toolbar" style="padding:2px;margin-top: 5px">
    <table style="width:100%;">
        <tr>
            <td style="width: 100%;">
                <a class="mini-button" iconCls="icon-goto" onclick="calculateCircle()">计算基模</a>
            </td>
        </tr>
    </table>
</div>
<fieldset id="fd3" style="width:100%;">
    <legend><h3>计算结果</h3></legend>
    <table border="0" cellpadding="1" cellspacing="2" style="width: 100%;">
        <tr>
            <td style="width: 160px;">反馈环</td>
            <td>
                <input name="circle" class="mini-textarea" height="150px" style="width: 100%;"/>
            </td>
        </tr>
        <tr>
            <td>成长上限基模</td>
            <td>
                <input name="growthLimit" class="mini-textarea" height="150px" style="width: 100%;"/>
            </td>
        </tr>
        <tr>
            <td>舍本逐末基模</td>
            <td>
                <input name="neglect" class="mini-textarea" height="150px" style="width: 100%;"/>
            </td>
        </tr>
        <tr>
            <td>成长与投资不足基模</td>
            <td>
                <input name="underinvestment" class="mini-textarea" height="150px" style="width: 100%;"/>
            </td>
        </tr>
    </table>
</fieldset>
</body>
<script language="JavaScript">
    mini.parse();
    var formatedParam = "";
    mini.getByName("arrStr").setValue("0,1,0;1,0,1;1,0,0");
    function formatMatrix() {
        var arrStr = mini.getByName("arrStr").getValue();
        if (arrStr.length === 0){
            mini.alert("请输入关系矩阵，行元素','分隔，列元素';'分隔");
            return;
        }
        var rows = arrStr.split(";");
        var rowsCount = rows.length;
        var html = "";
        for (i = 0; i < rowsCount; i++) {
            html += "<tr>";
            var cols = rows[i].split(",");
            var colsCount = cols.length;
            if (rowsCount !== colsCount) {
                mini.alert("矩阵输入错误，行列数量不匹配");
                formatedParam = "";
                return;
            }
            for (j = 0; j < colsCount; j++) {
                html += "<td>" + cols[j] + "</td>";
            }
            html += "</tr>";
        }
        formatedParam = arrStr;
        $('#matrixSize').text("关系矩阵[" + rowsCount + "*" + rowsCount + "]");
        $('#matrix').html(html);
    }

    function calculateCircle() {
        if (formatedParam.length === 0) {
            mini.alert("请先点击检查矩阵，进行参数检查");
            return;
        }
        $.ajax({
            url: "/calculate/result",
            type: "post",
            data: {
                arrStr: formatedParam
            },
            success: function (result) {
                if (result.success === "false") {
                    mini.alert("计算过程发生异常");
                    return;
                }
                mini.showTips({
                    content: "<b>操作成功</b><br/>反馈环及基模计算完成",
                    state: "success",
                    x: "center",
                    y: "top",
                    timeout: 3000
                });
                mini.getByName("circle").setValue(result.circle);
                mini.getByName("growthLimit").setValue(result.growthLimit);
                mini.getByName("neglect").setValue(result.neglect);
                mini.getByName("underinvestment").setValue(result.underinvestment);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                mini.alert("执行过程异常");
            }

        })
    }
</script>
</html>
