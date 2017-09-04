var tempJson;
var sort=0;
var length = 0;
function initData(){
    //加载模板
    $.myajax({
        url:"dictAction/getNewsDictionary?dict_group="+dict_group,
        datatype:"json",
        success:function(data){
            var result = data.result.data;
            for(var i in result) {
                var dictVO = result[i];
                appendToWeb(dictVO);
            }
            bindBtnAddClick();
            deleteDictionary()
            bindDeleteNewsGroup();
            bindNewsCodeValueClick();
            initNewsCssImgList();
            initNewsCssList();
            if (dict_group=='022'){
                $('.new-add-btn').show();
                $('.collapse').trigger("click");
            }
            $('.btn-cancel').hide();
            $('.btn-confirm').hide();
        }
    });
}

function appendToWeb(dictVO){
    if (dictVO==null) return;
    sort++;
    var _li = '<li class="dd-item news-group"  data-id="'+sort+'" data-dict_code="'+dictVO.dict_code+'">' +
        '<button data-action="collapse" class="collapse" type="button">关闭</button><button data-action="expand" type="button" style="display: none;">展开</button>'
        +'<div class="dd-handle"> <span class="label label-info"><i class="fa fa-cog"></i></span><span id="dict_value'+dictVO.dict_code+'">'+dictVO.dict_value+'</span><span id="btn-dict_code'+dictVO.dict_code+'"></span><span id="btn-group'+dictVO.dict_code+'">' +
        '<input type="button" class="btn btn-success btn-sm btn-del pull-right btn-outline zwf-button-add-css " data-dict_code="'+dictVO.dict_code+'" id="new-add-btn" value="新增子栏目"/>' +
        '<input class="btn btn-danger btn-sm btn-del pull-right del-dict-group btn-outline " data-dict_code="'+dictVO.dict_code+'" type="button" value="删除"/>' +
        '<input class="btn btn-primary btn-sm btn-del pull-right btn-rename btn-outline zwf-hide'+dictVO.dict_code+'" data-dict_code="'+dictVO.dict_code+'" type="button" value="重命名"/>' +
        '<input type="button" class="btn btn-danger btn-sm btn-qx pull-right btn-outline btn-cancel" id="btn-cancel'+dictVO.dict_code+'" data-dict_code="'+dictVO.dict_code+'" style="margin-left: 5px;" value="取消"/>' +
        '<input type="button" class="btn btn-primary btn-sm btn-qx pull-right btn-outline btn-confirm" id="btn-confirm'+dictVO.dict_code+'" data-dict_code="'+dictVO.dict_code+'" style="margin-left: 5px;" value="确定"/></span></div>' +
        '<div id="news-group'+dictVO.dict_code+'" class="zwf-news zwf-css-message" data-dict_code="'+dictVO.dict_code+'"></div><ol class="dd-list">';
    var obj = eval(dictVO.news_code_list);
    for( var i = 0; i < obj.length; i++){
        sort++;
        var cssVO = eval('('+obj[i].css_list+')');
        _li += '<li class="dd-item news-code" data-id="'+sort+'" data-dict_code="'+obj[i].dict_code+'">' +
            '<div class="dd-handle"><span class="label label-warning"><i class="fa fa-cog"></i></span><span id="dict_value'+obj[i].dict_code+'">'+obj[i].dict_value+'</span>' +
            '<span class="zwf-img-css" id="description'+obj[i].dict_code+'">栏目样式:'+obj[i].css_value+'</span><span id="btn-dict_code'+obj[i].dict_code+'"></span>' +
            '<sapn id="news-code'+obj[i].dict_code+'">' +
            '<input data-css_code="'+obj[i].other_field+'" data-dict_code="'+obj[i].dict_code+'" class="btn btn-success btn-outline btn-sm btn-del pull-right btn-editor-css " type="button" value="编辑栏目样式"/>'+
            '<input  data-dict_code="'+obj[i].dict_code+'"  class="btn btn-danger btn-outline btn-sm btn-del pull-right del-news-code " type="button" value="删除"/>'+
            '<input data-dict_code="'+obj[i].dict_code+'" class="btn btn-primary btn-outline btn-sm btn-rename pull-right btn-qx zwf-hide'+obj[i].dict_code+'" type="button"  value="重命名" />' +
            '<input type="button" class="btn btn-danger btn-outline btn-sm btn-qx pull-right btn-cancel"  id="btn-cancel'+obj[i].dict_code+'" data-dict_code="'+obj[i].dict_code+'" style="margin-left: 5px;" value="取消"/>' +
            '<input type="button" class="btn btn-primary btn-outline btn-sm btn-qx pull-right btn-confirm" id="btn-confirm'+obj[i].dict_code+'" data-dict_code="'+obj[i].dict_code+'" style="margin-left: 5px;" value="确定"/></sapn></div>' +
            '<div class="zwf-css-message" id="newsCssImg'+obj[i].dict_code+'" data-dict_code="'+obj[i].dict_code+'" hidden="hidden"></div></li>';
    }
    sort = obj.length+sort;
    _li += '</ol></li>';
    $('#mainol').append(_li);
}

function bindNewsCodeValueClick() {
    $('.btn-rename').unbind('click').on('click',function () {
        if ($('#input1').attr('data-status') == 'false') {bindMessage("请先完成当前重命名操作!"); return;}
        var news_code = $(this).attr('data-dict_code');
        var editor_group = $('#dict_value'+news_code+'');
        var content = editor_group.text();
        editor_group.hide();
        var _input = $('<input type="text" placeholder="'+content+'" class="input-text" id="input1" data-status="false">');
        $('#btn-dict_code'+news_code+'').append(_input);
        $('.zwf-hide'+news_code+'').hide();
        $('#btn-cancel'+news_code+'').show();
        $('#btn-confirm'+news_code+'').show();
        bindButtonCancelClick();
        updateDictName();
        bindInputClick();
    });
}

function bindButtonCancelClick() {
    $('.btn-cancel').unbind('click').on('click',function () {
        var dict_code = $(this).attr('data-dict_code');
        if (dict_code != null){
            $('#dict_value'+dict_code+'').show();
            $('#btn-group'+dict_code+'').show();
            $('#btn-cancel'+dict_code+'').hide();
            $('#btn-confirm'+dict_code+'').hide();
            $('#input1').remove();
            $('.zwf-hide'+dict_code+'').show();
        }
        var news_code = $(this).attr('data-dict_code');
        if (news_code !=null){
            $('#dict_value'+news_code+'').show();
            $('#news-code'+news_code+'').show();
            $('#btn-cancel'+news_code+'').hide();
            $('#btn-confirm'+news_code+'').hide();
            $('#input1').remove();
            $('.zwf-hide'+news_code+'').show();
        }

    });
}

function bindUpdataDictSchoolClick() {
    $('.btn-confirm').unbind('click').on('click',function () {
        if($('.input-text').val() == ''){alert('请输入栏目'); return;}
        var dict_code=$(this).attr('data-dict_code');
        var dict_value=$('.input-text').val();
        $('#btn-cancel'+dict_code+'').hide();
        $('#btn-confirm'+dict_code+'').hide();
        $('#input1').remove();
        $('.zwf-hide'+dict_code+'').show();
        $.myajax({
            url:"dictAction/updateDictSchool",
            datatype:"json",
            data:{dict_code:dict_code,dict_value:dict_value},
            success:function(data){
                $('#dict_value'+dict_code+'').empty().text(dict_value);
                $('#dict_value'+dict_code+'').show();
                bindMessage("栏目修改成功!");
            }
        });
    });
}

//删除模块栏目
function bindDeleteNewsGroup(){
    $(".del-dict-group").unbind('click').on('click', function() {
        var dict_code = $(this).attr('data-dict_code');
        $.myajax({
            url:"dictAction/deleteNewsDictSchool",
            datatype:"json",
            data: {dict_code:dict_code},
            success:function(data){
                $('#mainol').empty();
                initData();
                bindMessage("栏目删除成功!");
            }
        });
    });
}

//显示添加栏目框
function bindAddDictGroupClick(){
    $('#add-dict-group').unbind('click').on('click',function(){
        $(this).attr("disabled", true);
        var li='<li id="addDictGroup"><input id="iptDictName" type="text" placeholder="请输入栏目名称" class="form-control" style="width:70%;float:left;">'
            +'<button id="btn-confirm" type="button" class="btn btn-primary" style="width: 13%;left:800px;height: 34px;" >确定</button>'
            +'<button type="button" class="btn btn-danger zwf-button-empty" style="width: 13%;left:950px;height: 34px;" >取消</button></li>';
        $('#newObject').append(li);
        bindBtnDictGroupClick();
        bindEmptyList();
    });
}

//添加栏目
function bindBtnDictGroupClick(){
    $("#btn-confirm").unbind('click').on('click', function() {
        if ($('#iptDictName').val() == null || $('#iptDictName').val() == "") {bindMessage("请输入栏目名称!"); return;}
        $.myajax({
            url:"dictAction/addDictSchool",
            datatype:"json",
            data: {dict_group:'022',dict_value:$("#iptDictName").val()},
            success:function(data){
                $('#add-dict-group').attr("disabled", false);
                $('#addDictGroup').remove();
                var dictVO = data.result.data;
                $('#mainol').empty();
                initData();
                bindBtnAddClick();
                bindMessage("栏目添加成功!");
            }
        });
    });
}

function initNewsCssList(){
    $('.zwf-button-add-css').unbind('click').on('click',function () {
        $('.zwf-button-add-css').attr("disabled", true);
        var dict_code = $(this).attr('data-dict_code');
        var li='<li id="iptLiEetpy'+dict_code+'" class="hidden-iamges"><input id="iptDictName" type="text" placeholder="请输入栏目名称" class="form-control" style="width:70%;float:left;">'
            +'<button id="btn-confirm" type="button" class="btn btn-primary btn-outline add-news-code" data-dict_code="'+dict_code+'" style="width: 13%;left:800px;height: 34px;" >确定</button>'
            +'<button data-dict_code="'+dict_code+'" type="button" class="btn btn-danger btn-outline zwf-button-empty" style="width: 13%;left:950px;height: 34px;" >取消</button></li>'
            +'<div class="hxx-line clearfloat zwf-group-remove'+dict_code+'" style="padding: 14px 0 20px 15px;">'
            +'<div class="hxx-container-right"><h5 class="hxx-style-title">选择栏目样式</h5>';
        $.myajax({
            url:"dictAction/getNewsCssList?dict_group=032",
            datatype:"json",
            success:function(data){
                var result = data.result.data;
                for(var i in result) {
                    var dictVO = result[i];
                    var cssVO = eval('('+dictVO.other_field+')');
                    var cssSrc = 'dict/images/'+cssVO.css_value+dictVO.dict_code+".png";
                    var total_graph = 'dict/images/'+cssVO.css_value+dictVO.dict_code+"-total-graph.png";
                    li += '<div data-css_code="'+dictVO.dict_code+'" data-css_src="'+total_graph+'" data-news-css_value="'+dictVO.description+'" data-css_value="'+dictVO.dict_value+'" data-i="'+i+'" data-total="'+result.length+'" data-dict_group="'+dict_code+'" class="hxx-style-cell add-news-code"><span>'+dictVO.dict_value+'</span><img src="'+cssSrc+'" title="'+dictVO.dict_value+'"/></div>';
                }
                var id = "#news-group"+dict_code+"";
                $(''+id+'').append(li);
                bindBtnAddClick();
                bindBtnSaveClick();
                bindAddNewsPhotoClick();
                bindMouseup();
                bindAddNewsMarkRigthClick();
                bindAddNewsMarkLeftClick();
                bindUpdateCssCodeClick();
                bindEmptyList();
                bindCloseMarkClick();
            }
        });
    });
}

function bindAddNewsPhotoClick() {
    $('.hxx-style-cell').unbind('click').on('click',function () {
        $('.hxx-style-cell.add-news-code').removeClass('current');
        var news_code = $(this).attr('data-dict_code');
        var css_code = $(this).attr('data-css_code');
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',$(this).attr('data-css_src'));
        $('.zwf-css-name').empty().text($(this).attr('data-css_value'));
        $('.zwf-css-content').empty().text($(this).attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.add-news-code.active').attr('data-css_code');
        var this_code = $(this).attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.zwf-css-mark').show();
        $(this).addClass("current")
    });
}


function bindAddNewsMarkRigthClick() {
    $('.zwf-mark-right-click').unbind('click').on('click',function (e) {
        e.stopPropagation();//阻止冒泡
        var lowerBrother = $('.hxx-style-cell.add-news-code.current').next();
        var news_code = lowerBrother.attr('data-css_code');
        if ( news_code == null || news_code== undefined){bindMessage("这是最后一张,到底了！"); return;}
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',lowerBrother.attr('data-css_src'));
        $('.zwf-css-name').empty().text(lowerBrother.attr('data-css_value'));
        $('.zwf-css-content').empty().text(lowerBrother.attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.add-news-code.active').attr('data-css_code');
        var this_code = lowerBrother.attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.hxx-style-cell.add-news-code').removeClass('current');
        lowerBrother.addClass("current");
    });
}

function bindAddNewsMarkLeftClick() {
    $('.zwf-mark-left-click').unbind('click').on('click',function (e) {
        e.stopPropagation();//阻止冒泡
        var upperBrother = $('.hxx-style-cell.add-news-code.current').prev();
        var news_code = upperBrother.attr('data-css_code');
        if ( news_code == null || news_code== undefined){bindMessage("这是第一张,到顶了！"); return;}
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',upperBrother.attr('data-css_src'));
        $('.zwf-css-name').empty().text(upperBrother.attr('data-css_value'));
        $('.zwf-css-content').empty().text(upperBrother.attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.add-news-code.active').attr('data-css_code');
        var this_code = upperBrother.attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.hxx-style-cell.add-news-code').removeClass('current');
        upperBrother.addClass("current");
    });
}

function bindEmptyList(){
    $('.zwf-button-empty').unbind('click').on('click',function () {
        var dict_code = $(this).attr('data-dict_code');
        if (dict_code == null || dict_code == '' || dict_code == undefined) {
            $('#add-dict-group').attr("disabled", false);
            $('#addDictGroup').remove();
        } else {
            $('.zwf-button-add-css').attr("disabled", false);
            $('#iptLiEetpy'+dict_code+'').remove();
            $('.zwf-group-remove'+dict_code+'').remove();
        }
    });
}
//添加栏目
function bindBtnSaveClick(){
    $(".add-news-code").unbind('click').on('click', function() {
        if ($("#iptDictName").val() == null||$("#iptDictName").val() == undefined||$("#iptDictName").val() == "") {bindMessage("请输入栏目名称!"); return;}
        var dict_group = $(this).attr('data-dict_code');
        var css_code = $('.hxx-style-cell.add-news-code.active').attr('data-css_code');
        if (css_code == undefined||css_code==null||css_code==''){bindMessage("请选择新增的栏目样式!"); return;}
        $.myajax({
            url:"dictAction/addNewsDictSchool",
            datatype:"json",
            data: {dict_group:dict_group,dict_value:$("#iptDictName").val(),other_field:css_code},
            success:function(data){
                bindMessage("栏目添加成功！");
                $('#iptLiEetpy').remove();
                var dictVO = data.result.data;
                $('#mainol').empty();
                initData();
                bindBtnAddClick();
            }
        });
    });
}
function bindBtnAddClick(){
    $(".pull-right").on("mousedown",function(e) {
        e.stopPropagation();//阻止冒泡
        dict_code=$(this).attr('data-dict_code');
        dict_value=$(this).attr('data-dict_value');
    });
}

function bindInputClick(){
    $(".input-text").on("mousedown",function(e) {
        e.stopPropagation();//阻止冒泡
    });
}

//删除字典
function deleteDictionary(){
    $('.del-news-code').unbind('click').on('click',function () {
        $.myajax({
            url:"dictAction/deleteNewsDictSchoolByCode",
            datatype:"json",
            data:{dict_code:$(this).attr('data-dict_code')},
            success:function(data){
                $('#mainol').empty();
                initData();
                bindMessage("栏目删除成功!");
            }
        });
    });
}

//重命名
function updateDictName(){
    $(".btn-confirm").unbind('click').on("click",function(){
        if($('.input-text').val() == ''){bindMessage('请输入栏目名称!'); return;}
        if($(this).attr('data-dict_code') == '') return;
        var news_code=$(this).attr('data-dict_code');
        var dict_value=$('#input1').val();
        $('#btn-cancel'+news_code+'').hide();
        $('#btn-confirm'+news_code+'').hide();
        $('#input1').remove();
        $('.zwf-hide'+news_code+'').show();
        $('#dict_value'+news_code+'').show();
        $.myajax({
            url:"dictAction/updateDictSchool",
            datatype:"json",
            data:{dict_code:news_code,dict_value:dict_value},
            success:function(data){
                $('.zwf-button-add-css').attr("disabled", false);
                bindMessage("栏目名称修改成功!");
                $('#dict_value'+news_code+'').text(dict_value);
            }
        });
    });
}

function initNewsCssImgList(){
    $(".btn-editor-css").unbind('click').on("click",function(){
        if ($('.zwf-css-message').text()!="") {bindMessage("请先修改当前栏目样式,如果不想修改请点“取消”退出后选择其余栏目......"); return;}
        if($(this).attr("data-dict_code") == '') return;
        var newsCode = $(this).attr("data-dict_code");
        $("#newsCssImg"+newsCode+"").empty();
        var src = $(this).attr('data-src');
        var cssCode = $(this).attr("data-css_code");
        var _li = '<div class="hxx-line clearfloat" style="padding: 14px 0 20px 15px;">'
            +'<div class="hxx-container-right"><h5 class="hxx-style-title">选择栏目样式</h5>';
        $.myajax({
            url:"dictAction/getNewsCssList?dict_group=032",
            datatype:"json",
            success:function(data){
                var result = data.result.data;
                for(var i in result) {
                    length = result.size;
                    var dictVO = result[i];
                    var cssVO = eval('('+dictVO.other_field+')');
                    var cssSrc = 'dict/images/'+cssVO.css_value+dictVO.dict_code+".png";
                    var total_graph = 'dict/images/'+cssVO.css_value+dictVO.dict_code+"-total-graph.png";
                    if (cssCode == dictVO.dict_code) {
                        _li += '<div data-css_src="'+total_graph+'" data-css_value="'+dictVO.dict_value+'" data-css_code="'+dictVO.dict_code+'" data-news-css_value="'+dictVO.description+'" data-dict_code="'+newsCode+'" class="hxx-style-cell active"><span class="css_title'+dictVO.dict_code+'">'+dictVO.dict_value+'</span><img src="'+cssSrc+'" class="imgCss" title="'+dictVO.dict_value+'"/></div>';
                    } else {_li += '<div data-css_src="'+total_graph+'" data-css_value="'+dictVO.dict_value+'" data-css_code="'+dictVO.dict_code+'" data-news-css_value="'+dictVO.description+'" data-dict_code="'+newsCode+'" class="hxx-style-cell"><span class="css_title'+dictVO.dict_code+'">'+dictVO.dict_value+'</span><img src="'+cssSrc+'" class="imgCss" title="'+dictVO.dict_value+'"/></div>';}
                }
                _li += '</div></div><div data-css_code="'+cssCode+'" data-code="'+newsCode+'" data-src="'+src+'"><button class="btn btn-primary btn-outline btn-sm btn-del pull-right btn-finish" data-i="false"><i class="finish">完成</i></button>'
                    +'<button class="btn btn-danger btn-outline btn-sm btn-del pull-right btn-cancel"><i class="cancel">取消</i></button></div>';
                $("#newsCssImg"+newsCode+"").append(_li);
                $("#newsCssImg"+newsCode+"").show();
                bindCloseMarkClick();
                bindCssPhotoClick();
                bindMarkLeftClick();
                bindMarkRigthClick();
                bindUpdateCssCodeClick();
                bindUpdateNewsCssCodeClick();
                bindCancelUpdateClick();
            }
        });
    });
}

function bindCssPhotoClick() {
    $('.hxx-style-cell').unbind('click').on('click',function () {
        if($(this).attr("data-dict_code") == '') return;
        if($(this).attr("data-css_code") == '') return;
        $('.hxx-style-cell').removeClass('current');
        var news_code = $(this).attr('data-dict_code');
        var css_code = $(this).attr('data-css_code');
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',$(this).attr('data-css_src'));
        $('.zwf-css-name').empty().text($(this).attr('data-css_value'));
        $('.zwf-css-content').empty().text($(this).attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.active').attr('data-css_code');
        var this_code = $(this).attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.zwf-css-mark').show();
        $(this).addClass("current")
    });
}


function bindMarkRigthClick() {
    $('.zwf-mark-right-click').unbind('click').on('click',function (e) {
        e.stopPropagation();//阻止冒泡
        var lowerBrother = $('.hxx-style-cell.current').next();
        var news_code = lowerBrother.attr('data-dict_code');
        if ( news_code == null || news_code== undefined){bindMessage("这是最后一张,到底了！"); return;}
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',lowerBrother.attr('data-css_src'));
        $('.zwf-css-name').empty().text(lowerBrother.attr('data-css_value'));
        $('.zwf-css-content').empty().text(lowerBrother.attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.active').attr('data-css_code');
        var this_code = lowerBrother.attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.hxx-style-cell').removeClass('current');
        lowerBrother.addClass("current");
    });
}

function bindMarkLeftClick() {
    $('.zwf-mark-left-click').unbind('click').on('click',function (e) {
        e.stopPropagation();//阻止冒泡
        var upperBrother = $('.hxx-style-cell.current').prev();
        var news_code = upperBrother.attr('data-dict_code');
        if ( news_code == null || news_code== undefined){bindMessage("这是第一张,到顶了！"); return;}
        $('.zwf-code-name').empty().text( $('#dict_value'+news_code+'').text());
        $('.zwf-css-photo').attr('src',upperBrother.attr('data-css_src'));
        $('.zwf-css-name').empty().text(upperBrother.attr('data-css_value'));
        $('.zwf-css-content').empty().text(upperBrother.attr('data-news-css_value'));
        var css_code = $('.hxx-style-cell.active').attr('data-css_code');
        var this_code = upperBrother.attr('data-css_code');
        if (css_code != this_code) {
            $('.zl-sure-btn').removeClass('zl-sure-btn-after');
            $('.zl-surebtn-img').removeClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').removeClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('选择此模版');
        } else {
            $('.zl-sure-btn').addClass('zl-sure-btn-after');
            $('.zl-surebtn-img').addClass('zl-surebtn-img-after');
            $('.zl-surebtn-text').addClass('zl-surebtn-text-after');
            $('.zl-surebtn-text').empty().text('已选择该模板');
        }
        $('.hxx-style-cell').removeClass('current');
        upperBrother.addClass("current");
    });
}

function bindCloseMarkClick() {
    $('.zwf-mark-click').unbind('click').on('click',function () {
        $('.zwf-css-mark').hide();
        $('.hxx-style-cell').removeClass('current');
    });
}

function bindUpdateNewsCssCodeClick(){
    $('.btn-finish').unbind('click').on('click',function(){
        if($(this).attr("data-data-code") == '') return;
        if($(this).attr("data-data-css_code") == '') return;
        var newsCode = $(this).parent().attr("data-code");
        var cssCode = $('.hxx-style-cell.active').attr('data-css_code');
        var css_code = $(this).parent().attr('data-css_code');
        if (cssCode == undefined || cssCode == null || cssCode == '') { $('#newsCssImg'+newsCode+'').empty(); return;}
        if (css_code==cssCode){ bindMessage("当前已是该样式无需修改......"); $('#newsCssImg'+newsCode+'').empty(); return;}
        $.myajax({
            url:"dictAction/updateDictSchool",
            datatype:"json",
            data:{dict_code:newsCode,other_field:cssCode},
            success:function(data){
                $('#mainol').empty();
                initData();
                bindMessage("样式修改成功!");
            }
        });
    });
}

function bindCancelUpdateClick(){
    $('.btn-cancel').unbind('click').on('click',function(){
        var newsCode = $(this).parent().attr("data-code")+'';
        $("#newsCssImg"+newsCode+"").empty();
        $("#newsCssImg"+newsCode+"").hide();
    });
}

function bindMouseup() {
    $(document).mouseup(function(e){
        var _con = $('.zwf-mark-content');   // 设置目标区域
        if(!_con.is(e.target) && _con.has(e.target).length == 0){ // Mark 1
            $('.zwf-css-mark').hide();
            $('.hxx-style-cell').removeClass('current');
        }
    });
}

function bindUpdateCssCodeClick() {
    $('.update-css').unbind('click').on('click',function (e) {
        e.stopPropagation();
        $('.hxx-style-cell').removeClass('active');
        $('.zl-sure-btn').toggleClass('zl-sure-btn-after');
        $('.zl-surebtn-img').toggleClass('zl-surebtn-img-after');
        $('.zl-surebtn-text').toggleClass('zl-surebtn-text-after');
        $('.hxx-style-cell.current').addClass('active');
    });
}

function bindMessage(content) {
    if (isEmpty(content)) return;
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "1500",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.info(content,"提示");
}

//排序
function updateDictSort(jsonStr){
    $.myajax({
        url:"dictAction/updateDictSchoolSort",
        datatype:"json",
        data: {json_array:jsonStr,dict_group:dict_group},
        success:function(data) {
            var dictVO = data.result.data;
            appendToWeb(dictVO);
            bindBtnAddClick();
        }
    });
}
