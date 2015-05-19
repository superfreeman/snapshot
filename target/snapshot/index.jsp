<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>

    <title>Alcatel-lucent-ASB for snapshot play aux tools</title>
    <link rel="STYLESHEET" type="text/css" href="css/dhtmlxtree.css">
    <link rel='STYLESHEET' type='text/css' href='css/style.css'>
    <script type="text/javascript" src="js/dhtmlxcommon.js"></script>
    <script type="text/javascript" src="js/dhtmlxtree.js"></script>
</head>

<body>
<form name="fname" method="post">
    <table width="100%">
        <tr>
            <td colspan=2>
                <table cellspacing="0" cellpadding="0" class="sample_header" border="0">
                    <tr valign="middle">
                        <td width="100%">
                        </td>
                    </tr>
                </table>
            <td>
        </tr>

        <tr>
            <td colspan=1 width="30%">
                <table width="95%">
                    <tr>
                        <td nowrap align=center>
                            <a href="javascript:void(0);" onclick="tree.setCheck(tree.getSelectedItemId(),true);">CHECK ALL</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0);" onclick="tree.setCheck(tree.getSelectedItemId(),false);">UNCHECK ALL</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <div id="treeboxbox_tree"
                                 style="width:100%; height:400;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
                        </td>
                    </tr>



                </table>
            </td>
            <td colspan="1" width="70%">
                <table width="100%" align="left">
                    <tr>
                        <td nowrap><label style="font-weight: bold;font-size:12 ">Output snapshot filename:<input
                                title="please input the right snapshot file name,and te file will be save at the server"
                                type="text" style="width: 60%" id='outputid' value=""
                                onmouseover="getsnapshotfilename(this)"/></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a
                                href="javascript:void(0);"
                                onclick="showvalues(tree.getAllChecked())"
                                title="click to export the snapshot file only include the enbs you have selected on the left tree">CONFIRM</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="exportMergeFile()">DOWNLOAD</a>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" nowrap="false"><textarea id="testid" name="chuanchuan"
                                                                  style="left:0;width:100%;height:400;align:top;" readonly="readonly"></textarea>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>


        <tr><td colspan="2" align="left">&nbsp;&nbsp;<input type=file name=fileforload size=20 id="fileforloadid"/></td></tr>

        <tr><td colspan="2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="upload()">UPLOAD SNAPSHOT FILE</a>&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="reloadme()">RELOAD TREE</a></td></tr>

        <tr>
            <td colspan=2>
                <table cellspacing="0" cellpadding="0" class="sample_header" border="0">
                    <tr valign="middle">
                        <td width="100%">
                        </td>
                    </tr>
                </table>
            <td>
        </tr>

    </table>
    <input type=hidden name="source" value="source"/>
    <input type=hidden name="target" value="target"/>
    <input type=hidden name="filevalue" value="filevalue"/>
</form>
<script>
    var tree;
    (function () {
        try {
            tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
            tree.setImagePath("./imgs/csh_bluebooks/");
            tree.enableCheckBoxes(1);
            tree.enableThreeStateCheckboxes(true);
            var path = "<%=request.getContextPath()%>/data/data.xml";
//        alert(path);
            tree.loadXML(path);
        } catch (e) {
        }
    })();
    var uploaded="<%=request.getParameter("uploaded")%>";
    if(uploaded=="true"){
        alert("upload file success!");
    }
    function upload(){
        try {
            var filename = document.getElementById("fileforloadid").value;
            if (filename == '') {
                alert("please input the file name you want to upload");
                return;
            }
            //document.getElementById("filevalue").value=filename;
            var doc=document.forms[0];
            doc.action="<%=request.getContextPath()%>/upload?filename="+filename;
            doc.enctype="multipart/form-data";
            doc.method="POST";
            doc.submit();
        } catch (e) {
            alert(e.message)
        }
    }

    function showvalues(values) {
        try {
            if(values==''){alert('Please check the left tree,confirm you have select the enb');return false;}
            if(document.getElementById('outputid').value==''){alert('Please input the output snapshot filename!');return false;}
            var str = '<soap><source>' + values + '</source><target>' + document.getElementById('outputid').value + '</target></soap>';
            document.getElementById('testid').value = str;
        } catch (e) {
            alert(e.message)
        }
    }

    function getsnapshotfilename(me){
        var days=new Date();
        me.value= 'snapshot-lteran-'+(formatstr(days.getFullYear())+'-'+formatstr(days.getMonth()+1)+'-'+formatstr(days.getDate())+'-'+formatstr(days.getHours())+'-'+formatstr(days.getMinutes())+'-'+formatstr(days.getSeconds()))+'-1.xcm';
    }

    function formatstr(instr){
        var instr=new String(instr);
        if(instr.length==1)return '0'+instr;
        return instr;
    }
//    getsnapshotfilename();
    function exportMergeFile(){
        if(document.getElementById('outputid').value==''){alert('Please input the output snapshot filename!');return false;}
        if(document.getElementById('testid').value==''){alert('Please confirm the select enb is right!');return false;}
        var doc=document.forms[0];
        doc.action="<%=request.getContextPath()%>/mergeFile";
        doc.submit();
    }

    function reloadme(){
        window.location.href="<%=request.getContextPath()%>/dataxml"
    }

</script>


</body>
</html>
