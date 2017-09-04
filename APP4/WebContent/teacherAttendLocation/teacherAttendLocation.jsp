<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="utf-8" />
    <title>定位地址</title>
    <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=YIKIqyjPsVPrstHp7aeCrGlX"></script>
    <link rel="stylesheet" type="text/css" href="teacherAttendLocation/css/build.css" />
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="teacherAttendLocation/css/bootstrap.min.css" />
    <style>
        .container {
            padding: 41px 42px;
            position: relative;
        }

        .qbb-btn {
            position: absolute;
            right: 42px;
            top: 41px;
            width: 100px;
            height: 30px;
            line-height: 30px;
            background-color: #1CBB9E;
            border-radius: 5px;
            color: #fff;
            text-align: center;
        }

        .q_info {
            font-size: 18px;
            color: #4A4A4A;
            line-height: 19px;
            margin-top: 50px;
        }

        .q_info:last-child {
            margin-top: 80px;
        }

        .q_info h4 {
            font-size: 18px;
            margin-bottom: 27px;
        }

        .q_info .q_info_row {
            line-height: 40px;
        }

        .map-content {
            margin-top: 40px;
            width: 738px;
            height: 419px;
            background: #FFFFFF;
            box-shadow: 0 2px 4px 0 rgba(133, 147, 168, 0.40);
            padding: 20px;
        }

        .map {
            width: 700px;
            height: 380px;
        }

        .tips {
            font-size: 14px;
            color: #A9AAB4;
            line-height: 19px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="qbb-btn" id="save">
        保存
    </div>
    <div class="q_info">
        <div class="q_info_row">
            <span>考勤地点定位：</span>
            <input type="text" id="suggestId" size="20"  style="width:350px;height:30px;" />
            <span id="pos" class="tips"></span>
            <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:350px;height:auto; display:none;"></div>
        </div>
        <div class="q_info_row">
            <span>考勤限定范围：</span>
            <input type="text" style="width:350px;height:30px;" id="range"/>
            <span class="tips">单位：米</span>
        </div>
    </div>
    <div class="q_info_row">
        <div class="map-content">
            <div id="l-map" class="map"></div>
        </div>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="teacherAttendLocation/js/location.js?d=${time}"></script>
<script>
    function G(id) {
        return document.getElementById(id);
    }

    var longitude;//经度
    var latitude;
    getInfo();
    var map = new BMap.Map("l-map");
    map.enableScrollWheelZoom();
    if (address=='') {//未设置考勤地址
        map.centerAndZoom('北京', 12);
    }
    else {
        map.centerAndZoom(new BMap.Point(longitude, latitude), 12);
    }
    var ac = new BMap.Autocomplete({
        "input": 'suggestId',
        "location": map
    });
    if (address!='') {
        ac.setInputValue(address);
        var marker = new BMap.Marker(new BMap.Point(longitude, latitude));
        map.addOverlay(marker); //添加标注红点
        marker.enableDragging();
        marker.addEventListener('dragstart', function () {
            marker.setAnimation(BMAP_ANIMATION_DROP);
        });
        marker.addEventListener('dragend', function () {
            marker.setAnimation(BMAP_ANIMATION_BOUNCE);//跳到
            G("pos").innerText = "坐标：" + marker.getPosition().lng + "，" + marker.getPosition().lat;
            longitude = marker.getPosition().lng;
            latitude = marker.getPosition().lat;
        });
    }

    ac.addEventListener("onhighlight", function(e) {
                var str = "";
                var _value = e.fromitem.value;
                var value = "";
                if(e.fromitem.index > -1) {
                    value = _value.province + _value.city + _value.district + _value.street + _value.business;
                }
                str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

                value = "";
                if(e.toitem.index > -1) {
                    _value = e.toitem.value;
                    value = _value.province + _value.city + _value.district + _value.street + _value.business;
                }
                str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
                G("searchResultPanel").innerHTML = str;
            }
    );

    var myValue;
    ac.addEventListener("onconfirm", function(e) {
        //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
        G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        setPlace();
    });


    function setPlace() {
        map.clearOverlays(); //清除地图上所有覆盖物
        function myFun() {
            var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            var marker = new BMap.Marker(pp);
            map.addOverlay(marker); //添加标注
            marker.setAnimation(BMAP_ANIMATION_BOUNCE);
            marker.enableDragging();
            G("pos").innerText = "坐标："+marker.getPosition().lng+"，"+marker.getPosition().lat;
            longitude=marker.getPosition().lng;
            latitude=marker.getPosition().lat;
            marker.addEventListener('dragstart',function(){
                marker.setAnimation(BMAP_ANIMATION_DROP);
            });
            marker.addEventListener('dragend',function(){
                marker.setAnimation(BMAP_ANIMATION_BOUNCE);
                G("pos").innerText = "坐标："+marker.getPosition().lng+"，"+marker.getPosition().lat;
                longitude=marker.getPosition().lng;
                latitude=marker.getPosition().lat;
            });
        }
        var local = new BMap.LocalSearch(map, {
            onSearchComplete: myFun
        });
        local.search(myValue);
    }
    //session失效，页面跳转
    pageJump();
    save();
</script>
</body>

</html>
