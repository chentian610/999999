//图片滑动样式开始-----------------------------------------------------
var phone_left;
var ul_left;
var n = 0;
var _this;
var oImg;
var map = {};
var dictVO = {};
var storeMap = {};
var moduleIDs = {};
var parameterVO = {
    school_name:localStorage.getItem('school_name'),
    school_id:localStorage.getItem('school_id'),
    user_id:localStorage.getItem('user_id'),
    user_type:localStorage.getItem('user_type'),
    image_cut_action:localStorage.getItem('image_cut_action'),
    file_upload_action:localStorage.getItem('file_upload_action'),
    phone:localStorage.getItem('phone'),
    record_no:localStorage.getItem('web_domain_record'),
    school_types:{},
    totalPrice:0,
    totalModulePrice:0,
    defaultImg:''
};
var functionVO = {
    toastrMessage:function(content) {
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
        toastr.info(content,"提示！");
    },
    errorPrompt:function (title,content) {
        $('#subtitle').empty().append(title);
        $('#error_content').empty().append(content);
        $('#btn_error').click();
    },
    uploadFile:function(){
        $('#uploadLogo').unbind('change').on('change',function(){
            var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
            if (mb > 1) functionVO.toastrMessage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
            var formData = new FormData(document.getElementById("form-file"));
            $.myajax({
                type: "POST",
                url: parameterVO.file_upload_action,//file_upload_action
                cache: false,
                dataType : "JSON",
                data: formData,
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                processData: false, // 告诉jQuery不要去处理发送的数据
                success: function (result) {
                    var item = result.result.data;
                    map['organize_pic_url'] = item[0].file_url;
                    $('#schoolLogo').empty().append('<img src="'+item[0].file_url+'" style="width: 100%;height: 100%;margin-top: 0;">');
                    $('#uploadLogo').val('');
                }
            });
        });
    },
    initSchoolTypeList:function () {
        $.myajax({
            url : 'dictAction/getDictionary?dict_group=002',
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var result = data.result.data;
                var _option = '<option value="">选择学校类型</option>';
                for ( var i in result) {
                    var item = result[i];
                    parameterVO.school_types[item.dict_code] = item.dict_value;
                    _option +=  '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
                }
                _select = _option;
                $('#school_type').empty().append(_option);
            }
        });
    },
    judgeDomainName:function () {
        $('#domain').on('blur',function () {
            var domain = $('#domain').val();
            if (domain == null||domain=='') return functionVO.errorPrompt("学校域名错误..","域名不能为空,请输入域名...");
            $.myajax({
                url:'schoolApplyAction/examineDomainName',
                data:{domain:domain+'-m',main_domain:domain},
                datatype:'json',
                type: 'POST',
                success:function(data){
                    map['domain'] = domain+'-m';
                    map['main_domain'] = domain;
                    map['host_url'] = 'https://'+domain+'.classtao.cn';
                    map['manager_url'] = 'https://'+domain+'-m.classtao.cn';
                }
            });
        });
    },
    bindUploadSchoolLogoClickEvent:function(){
        $('#schoolLogo').unbind('click').on('click',function(){
            $('#uploadLogo').click();
            functionVO.uploadFile();
        });
    },
    bindSchoolTypeChangeEvent:function () {
        $('#school_type').unbind('change').on('change',function () {
            map['school_type'] = $('#school_type option:selected').val();
            $.myajax({
                url:'templateAction/getMandatoryTemplateList',
                data:{school_type: map['school_type']},
                datatype:'json',
                success:function(data){
                    var result = data.result.data;
                    var item = result[0];
                    var obj = eval(item.module_list);
                    for (var i in obj) {
                        storeMap[obj[i].module_code] = obj[i].is_must;
                    }
                }
            });
        });
    },
    checkShoolInfo:function (){//检查设置学校信息
        if (isEmpty(parameterVO.phone)) return toastrMassage('数据缺失，请重新登录后在进行操作...');
        if(isEmpty($('#school_name').val()))
            return functionVO.errorPrompt("学校名称错误...","请填写学校名称...");
        if(isEmpty(map['school_type']))
            return functionVO.errorPrompt("学校类型错误...","请选择学校类型...");
        if (isEmpty(map['organize_pic_url']))
            return  functionVO.errorPrompt("学校logo错误...","请选择学校logo...");
        if(isEmpty(map['domain']))
            return functionVO.errorPrompt("学校域名错误...","请填写学校域名...");;
        if($('#school_motto').val().length > 255)
            return functionVO. errorPrompt("学校校训错误...","您输入的学校校训超过了存储上限请缩简...");
        map['school_name'] = $('#school_name').val();
        map['school_motto'] = $('#school_motto').val();
        map['agent_phone'] = parameterVO.phone;
        map['record_no'] = parameterVO.record_no;
        map['copyright'] = 'Copyright ©010 '+map['school_name']+' All Rights Reserved';
        return true;
    },
    bindNextStepClickEvent:function () {
        $('#NextStep').unbind('click').on('click',function () {
            if ($(this).attr('data-index') == 1) {
                var flag = functionVO.checkShoolInfo();
                if (!flag) return;
                functionVO.initDefaultModuleList();
                $('#FirstStep').hide();
                $('#SecondSteps').show();
                $('#PreviousStep').show();
                $(this).attr('data-index',2);
                phone_left = $('#thisModule').position().left;
                ul_left = $('#zjq-picList').position().left;
            } else if ($(this).attr('data-index') == 2){
                if (isEmpty(parameterVO.config_id)) return functionVO.toastrMessage('请选择服务器配置...');
                $('#FirstStep').hide();
                $('#SecondSteps').hide();
                $('#ThirdSteps').show();
                $(this).attr('data-index',3);
                $('#PreviousStep').attr('data-index',3);
            } else if ($(this).attr('data-index') == 3) {
                if ($('#adminList tr').length <= 0) return functionVO.errorPrompt('学校管理员账号错误','请输入至少一位管理员电话号码...');
                $('#schoolName').text('学校名称：'+map['school_name']);
                $('#schoolType').text('学校类型：'+ parameterVO.school_types[map['school_type']]);
                $('#schoolLog').attr('src',map['organize_pic_url']);
                $('#schoolMotto').text('学校校训：'+map['school_motto']);
                $('#schoolDomain').text('学校域名：'+map['main_domain']);
                $('#schoolModulePrice').text('收费模块：￥'+parameterVO.totalModulePrice+'/年');
                $('#schoolTotalPrice').text('费用总计：￥'+parameterVO.totalPrice+'/年');
                if (isEmpty(parameterVO.SelectedPhoto)) {
                    $('#schoolModuleImg').attr('src',parameterVO.defaultImg);
                    var dict_code = parameterVO.defaultImg.substring(parameterVO.defaultImg.indexOf('?dict_code=')+11,parameterVO.defaultImg.length);
                    map['app_dict'] = JSON.stringify([dictVO[dict_code]]);
                } else {
                    $('#schoolModuleImg').attr('src',parameterVO.SelectedPhoto);
                    var dict_code = parameterVO.SelectedPhoto.substring(parameterVO.SelectedPhoto.indexOf('?dict_code=')+11,parameterVO.SelectedPhoto.length);
                    map['app_dict'] = JSON.stringify([dictVO[dict_code]]);
                }
                if (isNotEmpty(moduleIDs)) {
                    for (var i in moduleIDs) {
                        map['module_ids'] += ',' + moduleIDs[i];
                    }
                }
                var _li = '';
                var data = storeMap['moduleList'];
                var moduleIds = map['module_ids'].split(',');
                for (var i = 0;i < moduleIds.length;i++) {
                    var item = data[moduleIds[i]]
                    _li += '<li><div class="zjq-mess-block"><img src="'+item.icon_url+'"/><span>￥'+item.module_price+'</span></div><p>'+item.module_name+'</p></li>';
                }
                $('#schoolModuleList').empty().append(_li);
                var date = storeMap["config"];
                map['school_server'] = JSON.stringify([date[parameterVO.config_id]]);
                var price = accAdd(date[parameterVO.config_id].bandwidth_price,accAdd(date[parameterVO.config_id].memory_price,date[parameterVO.config_id].disk_price));
                $('#schoolConfig').empty().append('<td>'+date[parameterVO.config_id].memory+'G</td><td>'+date[parameterVO.config_id].disk+'G</td><td>'+date[parameterVO.config_id].bandwidth+'M</td><td>￥'+price+'</td>');
                var _tr = '';
                var phoneData = [];
                var phones = '';
                $('#adminList tr').each(function () {
                    phones = isEmpty(phones)?$(this).find('td').eq(1).text():phones+','+$(this).find('td').eq(1).text();
                    phoneData.push({phone:$(this).find('td').eq(1).text(),user_name:$(this).find('td').eq(0).text()});
                    _tr += '<tr class="zjq-service-sel"><td>'+$(this).find('td').eq(0).text()+'</td><td>'+$(this).find('td').eq(1).text()+'</td></tr>';
                });
                $('#userPhoneList').empty().append(phones);
                map['user_role_list'] = JSON.stringify(phoneData);
                map['school_admin_phone'] = phones;
                $('#schoolAdmin').empty().append(_tr);
                $('#btnMyModal5').click();
            }
        });
    },
    initAPPHomeModule:function () {
        $.myajax({
            url:'dictAction/getDictionary',
            data:{dict_group:'045'},
            datatype:'json',
            type: 'POST',
            success:function(data){
                var _li = '';
                var result = data.result.data;
                for (var i in result) {
                    var item = result[i];
                    var file_url = item.other_field+'?dict_code='+item.dict_code;
                    if (i==3) {$('#thisModule').empty().append('<img src="'+file_url+'"/>');parameterVO.defaultImg =file_url;}
                    _li += '<li><img src="'+file_url+'"/></li>';
                    dictVO[item.dict_code] = item;
                }
                $('#zjq-picList').empty().append(_li);
                functionVO.initImgCarousel();
            }
        });
    },
    initImgCarousel:function(){
        function getImg(){//右滑时改变模型手机里img的值
            var ul_left = $('#zjq-picList').position().left;
            var dis = phone_left - ul_left;
            var $current = $('#zjq-picList').find("li");
            $current.find("img").each(function(i){
                if(parseInt(dis/110) == i-1){
                    _this = $(this);
                    setTimeout(function(){
                        oImg = _this[0].src;
                        parameterVO.SelectedPhoto = oImg;
                        $('.zjq-mobile-model img').attr('src',oImg);
                        _this.parent().width(107);
                        _this.parent().siblings().width(90);
                    }, 300 )
                }
            });
        }
        function getImg2(){//左滑时改变模型手机里img的值
            var ul_left = $('#zjq-picList').position().left;
            var dis = phone_left - ul_left;
            var $current = $('#zjq-picList').find("li");
            $current.find("img").each(function(i){
                if(parseInt(dis/110) == i+1){
                    _this = $(this);
                    setTimeout(function(){
                        oImg = _this[0].src;
                        parameterVO.SelectedPhoto = oImg;
                        $('.zjq-mobile-model img').attr('src',oImg);
                        _this.parent().width(107);
                        _this.parent().siblings().width(90);
                    }, 300 )
                }
            });
        }
        function startWidth(){//初始化图片宽度
            var $ali = $('#zjq-picList li');
            $ali.each(function(i){
                if(i == 3){
                    $(this).width(107);
                    $(this).siblings().width(90);
                }
            });
        }
        startWidth();
        var num = 0;
        $(".zjq-arrow-right").click(function(){ //右滑
            var len = $('#zjq-picList li').length;
            num++;
            if(num >= len-3){
                num = 0;
                $('.zjq-mobile-model img').attr('src',parameterVO.defaultImg);
                startWidth();
            }
            $('#zjq-picList').animate({left: ul_left-110*num + 'px'},500);
            getImg();
        });
        $(".zjq-arrow-left").click(function(){ //左滑
            var len = $('#zjq-picList li').length;
            num--;
            if(num < -3){
                num = 0;
                $('.zjq-mobile-model img').attr('src',parameterVO.defaultImg);
                startWidth();
            }
            $('#zjq-picList').animate({left: ul_left-110*num + 'px'},500);
            getImg2();
        });
        //图片滑动样式结束-----------------------------------------------------
    },
    initDefaultModuleList:function () {//获取默认模板
        $.myajax({
            url:'templateAction/getModuleBasicsList',
            data:{school_type:map['school_type']},
            datatype:'json',
            type: 'POST',
            success:function(data){
                var result = data.result.data;
                var _div = functionVO.setModuleListHtml(result);
                _div = '<h3>请选择功能模块<span id="totalModulePrice">收费模块：￥'+parameterVO.totalModulePrice+'/年</span></h3>' + _div;
                $('#moduleList').empty().append(_div);
                $('#totalPrice').empty().append(parameterVO.totalPrice);
                /*多选框点击给父元素添加或移除active*/
                $('.styled').unbind('click').on('click',function () {
                    $(this).parent().parent().parent().toggleClass('active');
                    if ($(this).is(':checked')) {
                        moduleIDs[$(this).attr('data-index')] = $(this).attr('module_id');
                        parameterVO.totalPrice = accAdd(parameterVO.totalPrice,$(this).parent().siblings('span').attr('price'));
                        parameterVO.totalModulePrice = accAdd(parameterVO.totalModulePrice,$(this).parent().siblings('span').attr('price'));
                    } else {
                        if (isEmpty(moduleIDs[$(this).attr('data-index')])) return;
                        moduleIDs[$(this).attr('data-index')] = null;
                        parameterVO.totalPrice = accSub(parameterVO.totalPrice,$(this).parent().siblings('span').attr('price'));
                        parameterVO.totalModulePrice = accSub(parameterVO.totalModulePrice,$(this).parent().siblings('span').attr('price'));
                    }
                    $('#totalPrice').empty().append(parameterVO.totalPrice);
                    $('#totalModulePrice').empty().append('收费模块：￥'+parameterVO.totalModulePrice+'/年');
                })
            }
        });
    },
    setModuleListHtml:function (result) {
        var _divDefault = '';
        var _div = '';
        var data = {};
        //var moduleType = eval(storeMap["module_type"]);
        _divDefault += '<div class="zjq-function-module"> <h4><span></span>基本类</h4>'
            +'<ul class="zjq-must-sel zjq-function-block clearfix">';
        _div += '<div class="zjq-function-module"> <h4><span></span>可选类</h4>'
            +'<ul class="zjq-function-block clearfix">';
        for (var i in result) {
            var item = result[i];
            data[item.module_id] = item;
            if (storeMap[item.module_code] == 1) {
                map['module_ids'] = isEmpty(map['module_ids'])?item.module_id:map['module_ids']+','+item.module_id;
                _divDefault += '<li><div class="zjq-mess-block">'
                    +'<img src="'+item.icon_url+'"/> <span>￥'+item.module_price+'</span>'
                    +'<i class="fa fa-check"></i> </div> <p>'+item.module_name+'</p> </li>';
                parameterVO.totalPrice = accAdd(parameterVO.totalPrice,item.module_price);
                parameterVO.totalModulePrice = accAdd(parameterVO.totalModulePrice,item.module_price);
                continue;
            }
            _div += '<li> <div class="zjq-mess-block">'
                +'<img src="'+item.icon_url+'"/> <span price="'+item.module_price+'">￥'+item.module_price+'</span>'
                +'<div class="checkbox checkbox-success">'
                +'<input id="checkbox'+i+'" class="styled" type="checkbox" module_id="'+item.module_id +'" data-index="'+i+'">'
                +'<label for="checkbox'+i+'"></label></div></div><p>'+item.module_name+'</p></li>';
        }
        storeMap['moduleList'] = data;
        _divDefault += '</ul></div>';
        _div += '</ul></div>';
        return _divDefault+_div;
    },
    initServerConfig:function () {
        $.myajax({
            url:'systemAction/getServerConfigList',
            datatype:'json',
            success:function(data){
                var  _div = '<div class="zjq-row-style clearfix">'
                    +'<div class="zjq-table-cell"></div>'
                    +'<div class="zjq-table-cell">内存</div>'
                    +'<div class="zjq-table-cell">硬盘</div>'
                    +'<div class="zjq-table-cell">带宽</div>'
                    +'<div class="zjq-table-cell">元/年</div>'
                    +'<div class="zjq-acive-border"></div></div>';
                var result = data.result.data;
                var date = {};
                for (var i in result) {
                    var item = result[i];
                    if (i > 2) continue;
                    var price = accAdd(item.bandwidth_price,accAdd(item.memory_price,item.disk_price));
                    _div += '<div class="zjq-row-style clearfix"><div class="zjq-table-cell">'
                        +'<div class="radio radio-info radio-inline zjq-grade-name">'
                        +'<input type="radio" id="sel'+i+'" name="servicer" class="inputRadio"><label for="sel'+i+'"></label>'
                        +'</div></div><div class="zjq-table-cell">'+item.memory+'G</div>'
                        +'<div class="zjq-table-cell">'+item.disk+'G</div><div class="zjq-table-cell">'+item.bandwidth+'M</div>'
                        +'<div class="zjq-table-cell" price="'+price+'" config_id="'+item.server_config_id+'">￥'+price+'</div><div class="zjq-acive-border"></div>'
                        +'</div>';
                    item.disk_id = item.server_config_id;
                    item.memory_id = item.server_config_id;
                    item.bandwidth_id = item.server_config_id;
                    date[item.server_config_id] = item;
                }
                storeMap["config"] = date;
                $('#ConfigList').empty().append(_div);
                /*单选框点击给父元素添加active*/
                $(".inputRadio").unbind('click').on('click',function() {
                    if (isEmpty(parameterVO.configPrice)){
                        parameterVO.configPrice = $(this).parent().parent().siblings().eq(3).attr('price');
                        parameterVO.totalPrice = accAdd(parameterVO.totalPrice, parameterVO.configPrice);
                    } else {
                        parameterVO.totalPrice = accSub(parameterVO.totalPrice, parameterVO.configPrice);
                        parameterVO.configPrice = $(this).parent().parent().siblings().eq(3).attr('price');
                        parameterVO.totalPrice = accAdd(parameterVO.totalPrice, parameterVO.configPrice);
                    }
                    parameterVO.config_id = $(this).parent().parent().siblings().eq(3).attr('config_id');
                    $('#totalPrice').empty().append(parameterVO.totalPrice);
                    $(this).parent().parent().parent().addClass('active').siblings().removeClass('active');
                });
            }
        });
    },
    bindPreviousStepClickEvent:function () {
        $('#PreviousStep').unbind('click').on('click',function () {
            if ($(this).attr('data-index') == 2) {
                $('#FirstStep').show();
                $('#SecondSteps').hide();
                $(this).hide();
                parameterVO.totalPrice = 0;
                parameterVO.totalModulePrice = 0;
                $('#NextStep').attr('data-index',1);
            } else if ($(this).attr('data-index') == 3) {
                $('#FirstStep').hide();
                $('#SecondSteps').show();
                $('#ThirdSteps').hide();
                $('#NextStep').attr('data-index',2);
                $(this).attr('data-index',2);
            }
        });
    },
    bindPreservationClickEvent:function () {
        $('#Preservation').unbind('click').on('click',function () {
            var mobile = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
            if (isEmpty($('#adminName').val())) return functionVO.error($('#adminName'),'请输入昵称');
            if (isEmpty($('#adminPhone').val()) || !$('#adminPhone').val().match(mobile)) return functionVO.error($('#adminPhone'),'请输入正确的手机号码');
            if (isEmpty($('#adminPhoneOld').val()) || !$('#adminPhoneOld').val().match(mobile)) return functionVO.error($('#adminPhoneOld'),'请输入正确的手机号码');
            if ($('#adminPhone').val() != $('#adminPhoneOld').val()) return functionVO.error($('#adminPhoneOld'),'两次输入手机号码不同');
            var flag = false;
            $('#adminList tr').each(function () {
                if ($(this).find('td').eq(1).text() == $('#adminPhone').val()) flag = true;
            });
            if (flag) return functionVO.toastrMessage('学校管理员电话号码重复，请重新输入..');
            $('#adminList').append('<tr class="zjq-service-sel"> <td>'+$('#adminName').val()+'</td>'
                +'<td>'+$('#adminPhone').val()+'</td> <td><img src="application/icon/shanchu.png" class="removeAdmin"/></td> </tr>');
            $('#adminPhone').val('');
            $('#adminPhoneOld').val('');
            $('#adminName').val('');
            $('.btn-white').click();
            $('.removeAdmin').unbind('click').on('click',function () {
                $(this).parent().parent().remove();
            });
        });
    },
    bindDetermineClickEvent:function () {
        $('#Determine').unbind('click').on('click',function () {
            $.myajax({
                url:'schoolApplyAction/addSchool',
                data:map,
                datatype:'json',
                type: 'POST',
                success:function(data){
                    var result = data.result.data;
                    $('#ThirdSteps').hide();
                    $('#FourthSteps').show();
                    $('#BtnStep').hide();
                }
            });
        });
    },
    bindBtnDetermineClickEvent:function () {
        $('#btnDetermine').unbind('click').on('click',function () {
            $('.form-control').val('');
            $('#FourthSteps').hide();
            $('#FirstStep').show();
            $('#BtnStep').show();
            $('#PreviousStep').attr('data-index',2).hide();
            $('#NextStep').attr('data-index',1);
            $('#adminList').empty();
            $('#schoolLogo').empty().append('<img src="application/icon/plus.png">');
            map = {};
        });
    },
    error:function ($this,content) {
        if ($this.parent().find('span').length > 0) return;
        $this.parent().append('<span><i class="fa fa-times-circle"></i>'+content+'</span>');
        $this.css({border: '1px solid #EF4F4F'});
        $('.form-control').unbind('keyup').on('keyup',function () {
            $(this).css({border: '1px solid #e5e6e7'});
            $(this).parent().find('span').remove();
        });
    }
};

function initContent(){
    functionVO.initSchoolTypeList();//初始化学校类型
    functionVO.judgeDomainName();//判断学校域名
    functionVO.bindUploadSchoolLogoClickEvent();//判断图片上传
    functionVO.bindSchoolTypeChangeEvent();//选择学校类型
    functionVO.bindNextStepClickEvent();//点击下一步
    functionVO.initAPPHomeModule();//初始化APP启动页面列表
    functionVO.initServerConfig();//初始化服务器配置项
    functionVO.bindPreviousStepClickEvent();//点击上一步
    functionVO.bindPreservationClickEvent();//点击添加
    functionVO.bindDetermineClickEvent();//点击确定
    functionVO.bindBtnDetermineClickEvent();//点击完成后的确定
}