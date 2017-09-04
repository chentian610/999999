<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
	<title>教学云资源</title>
	<link rel="stylesheet" type="text/css" href="teachCloud/css/swiper-3.3.1.min.css"/>
	<link rel="stylesheet" type="text/css" href="teachCloud/css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="teachCloud/css/style.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/css.css">
	
</head>

<body>

<% String source_id=request.getParameter("id");%>
<% String source_name=request.getParameter("source_name"); %>
<% String browse_count=request.getParameter("browse_count");%>
<% String download_count=request.getParameter("download_count");%>
<% String fav_count=request.getParameter("fav_count");%>
<% String resourceUrl=request.getParameter("resourceUrl"); %>
<% String source_type=request.getParameter("source_type"); %>

<div class="back"></div>
<div class="mainwrap">
	<div class="ftab fatb1 clearfix">
		<!-- <div class="list">教材资源</div> -->
		<%--<div class="back" style="right"><a href= "javascript:history.back(); " class="back"><h4><b>返回上一层</b> </h4> </a></div>--%>
		<h2 id="source_list"><%=source_name %></h2>
		<div class="star1">
			星级：
			<span>
				<b class="ic ic-star-s-on"></b>
				<b class="ic ic-star-s-on"></b>
				<b class="ic ic-star-s-on"></b>
				<b class="ic ic-star-s-on"></b>
				<b class="ic ic-star-s-off"></b>
			</span>
			<span><%=fav_count %></span>次收藏 | <span><%=download_count %></span>次下载 | <span><%=browse_count %></span>次阅读
		</div>
		<% if (source_type.equals("DOC")||source_type.equals("DOCX")||source_type.equals("PPT")||source_type.equals("PPTX")||source_type.equals("TXT")) { %>
			<div style="height:80%;"><%--width:640px;--%>
			<center>
                 <iframe name="resource" src="<%=resourceUrl %>" width="100%" height="100%" scrolling="auto" frameborder="0"></iframe>
            </center>
			</div>
		<% } else if (source_type.equals("MP4")) {%>
			<div id="mp4" class="detail_content" style="width: 640px; height: 352px; margin:50px; padding: 20px; border:5px solid #999; dispaly:none">
			<script src="teachCloud/player/sewise.player.min.js" type="text/javascript"></script>
			<script type="text/javascript">
			SewisePlayer.setup({
				server: "vod",
				type: "m3u8",
				autostart: "true",
				videourl:"<%=resourceUrl%>",
				skin: "vodWhite",
		        title: "<%=source_name%>",
		        claritybutton: "disable",
		        lang: "zh_CN"
			});
			</script>
			</div>
		<% } else if (source_type.equals("SWF")) { %>
			<div id="swf">
				<%-- <EMBED  src="<%=resourceUrl %>" width="1000" quality=high height="800" autostart="true" wmode="transparent"
				pluginspage="http://www.macromedia.com/go/getflashplayer"> --%>
				
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"   
   					    codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,16,0"   
    					width="320" height="400" >  
   				 <param name="movie" value="<%=resourceUrl%>">  
   				 <param name="quality" value="high">  
    			 <param name="play" value="true">  
   				 <param name="LOOP" value="false">  
   				 <embed src="<%=resourceUrl %>" width="1000" height="800" play="true" loop="false" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer">  
   				 </embed>  
    			</object>   
				
				
			</div>
			
		<% } else if (source_type.equals("MP3")) { %>
			<div id="mp3" style="width:800px; height:800px;">
				<embed src="<%=resourceUrl %>" width="800" height="300"
          			 loop="false" autostart="false">
    			</embed>
			</div>
		<% } else if (source_type.equals("TXT")) { %>
			<div id="txt" style="width:800; height:800;">
				<iframe src="<%=resourceUrl %>"></iframe>
			</div>
		<% } %>
		<!-- <div class="detail_content">
			<img src="teachCloud/images/video.jpg" />
			<div class="detail_line clearfix">
				<a class="detail_icon detail_icon1" href="##">
					下载
				</a>
				<a class="detail_icon detail_icon2" href="##">
					收藏
				</a>
				<a class="detail_icon detail_icon3" href="##">
					3
				</a>
				<a class="detail_icon detail_icon4" href="##">
					推荐到班级
				</a>
				<div class="share">
					<span>分享：</span>
					<img src="teachCloud/images/share1.jpg"  />
					<img src="teachCloud/images/share2.jpg"  />
					<img src="teachCloud/images/share3.jpg"  />
					<img src="teachCloud/images/share4.jpg"  />
					<img src="teachCloud/images/share5.jpg"  />
					<img src="teachCloud/images/share6.jpg"  />
				</div>
			</div>
		</div> -->
		<!-- <div class="shower">
			<ul class="nav4 clearfix">
				<li class="active">相关资源</li>
			</ul>
			<div class="swiper-container" style="height: 140px;">
			    <div class="swiper-wrapper">
			        <div class="swiper-slide">
			        	<a href="##">
			        		<img src="teachCloud/images/swiper1.jpg"  />
			        	</a>
			        </div>
			        <div class="swiper-slide">
			        	<a href="##">
			        		<img src="teachCloud/images/swiper1.jpg"  />
			        	</a>
			        </div>
			        <div class="swiper-slide">
			        	<a href="##">
			        		<img src="teachCloud/images/swiper1.jpg"  />
			        	</a>
			        </div>
			        <div class="swiper-slide">
			        	<a href="##">
			        		<img src="teachCloud/images/swiper1.jpg"  />
			        	</a>
			        </div>
			    </div>    
			    如果需要导航按钮
			    <div class="swiper-button-prev"></div>
			    <div class="swiper-button-next"></div>
			</div>
		</div> -->
		<!-- <div class="comment">
			您的评论
			<span id="star">
				<b class="ic ic-star-s-off"></b>
				<b class="ic ic-star-s-off"></b>
				<b class="ic ic-star-s-off"></b>
				<b class="ic ic-star-s-off"></b>
				<b class="ic ic-star-s-off"></b>
			</span>
			<textarea class="textarea"></textarea>
			<div class="submit1 clearfix">
				<button>发布评论</button>
			</div>
		</div> -->
		<!-- <div class="pagination1">
			<ul class="pagination">
				<li>
					<a href="#" aria-label="Previous">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>
				<li><a href="#">1</a></li>
				<li><a href="#">2</a></li>
				<li><a href="#">3</a></li>
				<li><a href="#">4</a></li>
				<li><a href="#">5</a></li>
				<li>
					<a href="#" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
			</ul>
			<div class="pagin">
				跳到<input type="text" class="form-control pagin1" maxlength="3" />页
				<input class="btn btn-default pagin2" type="submit" value="确定">
			</div>
		</div> -->
		<!-- <ul class="nav4 clearfix">
			<li class="active">全部评论</li>
			<li class="comm"><span>1</span>条评论</li>
		</ul> -->
		<!-- <ul class="comment_list">
			<li class="clearfix">
				<img src="teachCloud/images/head.jpg"/>
				<div class="comment_list_l">
					<span>
						<b class="ic ic-star-s-on"></b>
						<b class="ic ic-star-s-on"></b>
						<b class="ic ic-star-s-on"></b>
						<b class="ic ic-star-s-on"></b>
						<b class="ic ic-star-s-off"></b>
					</span>
					<span class="name">高峰</span>
					<span class="date">12天前</span><br />
					<div class="comment_list_detail">
						好
					</div>
				</div>
			</li>
		</ul> -->
	</div>
</div>


<script src="teachCloud/js/source_detail.js?d=${time}"></script>
<script src="teachCloud/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/swiper-3.3.1.jquery.min.js" type="text/javascript" charset="utf-8"></script>  
<script src="teachCloud/player/js/jquery.min.js" type="text/javascript"></script>
<script src="teachCloud/player/js/swfobject.js" type="text/javascript"></script>
<script src="teachCloud/player/sewise.player.min.js" type="text/javascript"></script>
			
<script>
  
$(function(){

	$("#mp4").hide;
	
	/* var mySwiper = new Swiper ('.swiper-container', {
	    direction: 'horizontal',
	    loop: true,
	    
	    // 如果需要前进后退按钮
	    nextButton: '.swiper-button-next',
	    prevButton: '.swiper-button-prev',
	    slidesPerView : 4,
		spaceBetween : 20,
	});
	$("#star b").hover(function(){
		$(this).toggleClass("star");
		$(this).prevAll().toggleClass("star");
	})
	$("#star b").click(function(){
		$(this).addClass("star1");
		$(this).prevAll().addClass("star1");
		$("#star b").unbind("mouseenter").unbind("mouseleave"); 
	}) */
	/* $("#source_list").val = request.getParameter("item.source_name"); */
});
</script>
</body>
</html>