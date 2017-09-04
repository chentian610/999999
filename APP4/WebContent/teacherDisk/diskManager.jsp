<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//设置缓存为空   	
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires",   0);    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	  <base href="<%=basePath%>">
  
	<meta charset="UTF-8">
	<title>Lezhi</title>
	<link href="hplus/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/jquery.treeview.css">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/style.css">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/css.css">
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/css2.css"/>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/moveto.css"/>
	<link rel="stylesheet" type="text/css" href="teacherDisk/css/diskicon.css"/>
</head>

<body>
<iframe id="downloadframe" style="display:none"></iframe>
<div class="mainwrap">
	<div class="ftab clearfix">
		<div class="textbook res clearfix">
			<div class="textbook_content clearfix" style="clear: both;">
				<div class="textbook_right">
					<div class="btn2 clearfix">
						<button class="btn-middle " id="uploadFile">上传</button>
					
					<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden='hidden' >	
	    				<input type="File" value="上传文件" name="uploadfile" id="uploadLogo" />
	 				</form>
	 				
	 					<!-- <div class="dividing-line">‖</div>	 -->
						<div class="search-btn">
							<i class="fa fa-search"></i>
							<input type="text" class="input-medium search-query" placeholder="请输入要搜索的内容" id="searchInput">
						</div>
						<button type="submit" class="btn btn-middle "  id="search-button">搜索</button>
					<!-- <div class="dividing-line">‖</div> -->
					 
					<div class="newfile-btn" style="display:none">
					<input type="text" class="input-medium search-query" placeholder="请输入新文件夹名" id="anew-folder"/>
					</div>
					
					<div class="sub" style="display:none">
					<button type="submit" class="btn btn-middle "  id="new-button">确定</button>
					</div>
					<div><button type="submit" class="btn btn-middle "  id="new-folder">新建文件夹</button></div>
					</div>
					<div>
					<div class="back" id="folderIndex"><a id = "a0" >全部文件</a></div> <div class="this"></div><div class="this-folderName"></div>
					</div>
					<table class="table disk">
						<thead>
							<tr class="active">
								<th width="510">
									<!-- 文件名 -->
									<label>
										 <input type="checkbox" name="head" id="selectAll"  />
										<span class="disk-txt1">文件名</span>
										<span class="disk-txt2">已选择<span>0</span>个文件/文件夹</span>
										<!-- <button class="btn-middle-disk">加入发送列表</button> -->
										<button class="btn-middle-disk" id="downloadCheck">下载</button>
										<button class="btn-middle-disk" id="moveCheck">移动到</button>
										<button class="btn-middle-disk" id="deleteCheck">删除</button> 
 									</label>
								</th>
								<th width="70">大小</th>
								<th width="167">修改日期</th>
							</tr>
						</thead>
						<tbody id="disk_list">
							
						</tbody>
					</table>
					
					<div class="course-btn1">已选课件（<span>0</span>）</div>
					<div class="course-btn">
						<div class="course-btn2" id="checkFile">
							<div class="icon6">
								<img src="teacherDisk/images/icon19.png"/>
								<div class="icon6-circle">0</div>
							</div>
							已选课件（<span>0</span>）
						</div>
						<div class="classList-btn">
							选择班级并发送
						</div>
					</div>
					
					<ul class="course-list" id="course_list">
						<li>
							<!-- <img src="teacherDisk/images/icon10.png" class="icon5" />语文第一册
							<img src="teacherDisk/images/icon18.png" class="icon7"/> -->
						</li>
					</ul>
					<div class="classList">
						<h4 class="classList-title">选择班级并发送</h4>
						<ul class="classList-content">
							<li>
								<label id=classname><!-- <input type="checkbox" name="classList" id="" value="" />一（1）班 --></label>
							</li>
							<!-- <li>
								<label><input type="checkbox" name="classList" id="" value="" />一（2）班</label>
							</li> -->
						</ul>
						<div class="classList-btn1">取消</div>
						<div class="classList-btn2" id=sendToClass>发送</div>
					</div>
					<div class= "pull-right">
						<ul id="page_pagintor"></ul>
					</div>
					<!-- <div class="fileList" hidden='hidden'>
						<h5 class="fileList-title">选择移动的位置</h5>
						<ul class="fileList-content" id="file_list">
							<li></li>
						</ul>
					</div> -->
					<div class="fileList">
						<h4 class="fileList-title">选择移动的位置</h4>
						<ul class="fileList-content" id="file_list">
							<li></li>
						</ul>
						<div class="fileList-btn1">取消</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="teacherDisk/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="js/myajax.js"></script>
<script type="text/javascript" src="teacherDisk/js/jquery.cookie.js"></script>
<script src="teacherDisk/js/disk_manager.js?d=${time}" type="text/javascript"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="teacherDisk/js/functionUtil.js"></script>
<!-- <script type="text/javascript" src="teacherDisk/js/jquery-2.0.0.min.js"></script> -->
<script type="text/javascript">
	$("#form-file").attr('action',localStorage.getItem('file_upload_action'));
	var indent = 0;
	loadDisk(0);
	bindSearchButtonClickEvent(); //搜索
	showClassInfo();//显示待选择的班级
	bindSendToClassClickEvent();//发送到班级
	bindCheckFileClickEvent();//选中文件
	bindRenameClickEvent();//重命名点击
	bindUploadFileClickEvent();//触发文件上传按钮点击事件
	uploadFileClickEvent();//上传文件功能
	bindGetAllFolderEvent();//获取所有文件夹
	bindNewFolderButtonEvent();//新建文件夹
	bindDownloadClickEvent();
	bindFileAllCheck();//获取所有文件夹（选中的多个）
	bindFileDeleteCheckEvent();//选中后的删除（选中的多个）
	bindMorefileDownloadCheck();//多个文件下载
	bindFolderCheckEvent();//查询文件夹下面的文件
	bindFolderIndexClickEvent();
	bindDeleteBtnClickEvent();//删除文件事件
	//删除前判断
	
	$(function(){
		$('.mainwrap').on("click",".icon5",function(){
			$(this).children(".icon5-dowm").show();
		}).on("click",".classList-btn",function(){
			$(".classList").show();
		}).on("click",".classList-btn1",function(){
			$(".classList").hide();
		}).on("click",".fileList-btn1",function(){
			$(".fileList").hide();
		}).on("click","#selectAll",function(){
			if($("#selectAll").is(":checked")){
				$("input[name='file']").prop("checked",true);
				$(".course-btn1").hide();
				$(".disk-txt1").hide();
				$(".course-btn2").show();
				$(".classList-btn").show();
				$(".disk-txt2").show();
				$(".btn-middle-disk").show();
				indent = $("input[name='file']").length;
				$(".icon6-circle").text(indent);
				$(".course-btn2 span").text(indent);
				$(".disk-txt2 span").text(indent);
			}else{
				$(".course-btn1").show();
				$(".disk-txt1").show();
				$(".course-btn2").hide();
				$(".classList-btn").hide();
				$(".disk-txt2").hide();
				$(".btn-middle-disk").hide();
				$("input[name='file']").prop("checked",false);
				indent = 0;
			}
		}).on("click","input[name='file']",function(){
			
			$(".course-btn1").hide();
			$(".disk-txt1").hide();
			$(".course-btn2").show();
			$(".classList-btn").show();
			$(".disk-txt2").show();
			$(".btn-middle-disk").show();
			if($(this).is(":checked")){
				$(this).parent().parent().find(".icon4-circle").addClass("active");
				$(this).parent().parent().find(".icon4").addClass("active");
				$(this).parent().parent().parent().parent().parent().find(".arr-box li").prop("checked",true);
				indent++;
				$(".icon6-circle").text(indent);
				$(".course-btn2 span").text(indent);
				$(".disk-txt2 span").text(indent);
			}else{
				$("#selectAll").prop("checked",false);
				$(this).parent().parent().find(".icon4-circle").removeClass("active");
				$(this).parent().parent().find(".icon4").removeClass("active");
				indent--;
				$(".icon6-circle").text(indent);
				$(".course-btn2 span").text(indent);
				$(".disk-txt2 span").text(indent);
			}
			if(indent == 0){
				$(".course-btn1").show();
				$(".disk-txt1").show();
				$(".course-btn2").hide();
				$(".classList-btn").hide();
				$(".disk-txt2").hide();
				$(".btn-middle-disk").hide();
				$(".course-list").hide();
			}
		}).on("click",".icon4.active",function(){
			$(".course-list").show();
		}).on("click",".course-btn2",function(){
			$(".course-list").toggle();
		}).on("click",".icon7",function(){
			$(this).parent().remove();
		}).on("mouseleave",".icon5",function(){
			$(this).children(".icon5-dowm").hide();
		}).on("click",".move",function(){
			$(".fileList").show();
		}).on("MouseOut",".icon5-dowm",function(){
			$(".fileList").hide();
		}).on("click","#moveCheck",function(){
			$(".fileList").show();
		}).on("click","#new-folder",function(){
			$(".newfile-btn").show();
			$(".sub").show();
			$("#new-folder").hide();
		});
	});
	
</script>
</body>


</html>