var currentPage = 1;
var limit = 10;

var map = {};
var parameterVO = {
    school_name:localStorage.getItem('school_name'),
    school_id:localStorage.getItem('school_id'),
    user_id:localStorage.getItem('user_id'),
    user_type:localStorage.getItem('user_type'),
    image_cut_action:localStorage.getItem('image_cut_action'),
    file_upload_action:localStorage.getItem('file_upload_action'),
    phone:localStorage.getItem('phone'),
    totalModulePrice:0,
    AgentVO:{},
    school_list:{},
    moduleCode_list:'',
    moduleID_list:{},
    config_list:{}
};

var Method = {
    toastrMassage:function (content) {
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
    },errorMessage:function (title,content) {
        $('.subtitle').empty().append(title);
        $('#error_content').empty().append(content);
        $('#btn_error').click();
    },initAgentBalance:function () {
        $.myajax({
            url : 'balanceAction/getBalanceByPhone',
            data:{phone:parameterVO.phone},
            datatype : 'json',
            type : 'post',
            success : function(data) {
                parameterVO.AgentVO = data.result.data;
                $('#Balance').empty().append( parameterVO.AgentVO.balance);
            }
        });
    },
    initSchoolList:function () {
        $.myajax({
            url:'schoolAction/getAgentApplySchoolList',
            data:{agent_phone:parameterVO.phone,start_id:(currentPage-1)*limit,limit:limit,page:currentPage,order_sql:'desc'},
            datatype:'json',
            type:'post',
            success:function(data){
                var result = data.result;
                var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
                Method.addSchoolListToWeb(data);
                if (pageCount<2) {
                    $("#page_pagintor").hide();
                    return;
                }
                var options = {
                    bootstrapMajorVersion: 3, //版本
                    currentPage: currentPage, //当前页数
                    totalPages: pageCount, //总页数
                    alignment:"center",
                    itemTexts: function (type, page, current) {
                        switch (type) {
                            case "first":
                                return "首页";
                            case "prev":
                                return "上一页";
                            case "next":
                                return "下一页";
                            case "last":
                                return "末页";
                            case "page":
                                return page;
                        }
                    },//点击事件，用于通过Ajax来刷新整个list列表
                    onPageClicked: function (event, originalEvent, type, page) {
                        currentPage=page;
                        Method.initSchoolList();
                    }
                };
                $("#page_pagintor").bootstrapPaginator(options);
                $("#page_pagintor").show();
            }
        });
    },
    addSchoolListToWeb:function (data) {
        var _li = '';
        var result = data.result.data;
        var date = {};
        for ( var i in result) {
            var item = result[i];
            var organize_pic_url
            if (isEmpty(item.organize_pic_url)) organize_pic_url = 'images/gzh_wx.jpg';
            else organize_pic_url = item.organize_pic_url;
            _li += '<li school_id="'+item.school_id+'"><img src="'+organize_pic_url+'" alt="" />'
                +'<span class="time">'+item.school_name+'<i>申请时间：'+getDateStr(item.create_date,'day')+'</i></span>'
                +Method.judgeAppStatus(item.status,item.valid_date,item.version)+'</li>';
            date[item.school_id] = item;
        }
        parameterVO.school_list = date;
        $('.school_list').empty().append(_li);
        Method.initButtonClickEvent();
    },
    judgeAppStatus:function (str,parameter1,parameter2) {
        if (str == '007005'&& isEmpty(parameter1) && Date.parse(parameter1) == Date.parse(getDateStr(new Date(),'day'))) return '<span class="pasted btn_remove">已过期<i>过期时间：'+getDateStr(parameter1,'day')+'</i></span>';
        else if (str == '007005' && parameter2 > 0 && isNotEmpty(parameter2)) return '<span class="state">更新审核中</span>';
        else if (str == '007005') return '<span class="state btn_remove">待审核</span>';
        else if (str == '007010') return '<span class="state btn_remove">生成中</span>';
        else if (str == '007015') return '<span class="buttons btn_remove"><span class="refuse">被拒绝：资料不全</span> '
            +'<input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/>'
            +'<input type="button" class="btn btn-default recovery" app_status="007005" value="重新提交"/>'
            +'<input type="button" class="btn btn-default del recovery" app_status="007020" value="删除"/></span>';
        else if (str == '007025') return '<span class="online btn_remove">已上线 <input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/></span>';
        else if (str == '007030') return '<span class="online btn_remove">已下线 </span>';
        else if (str == '007020') return '<span class="buttons btn_remove"><input type="button" class="btn btn-default recovery" app_status="007015" value="恢复"/>'
            +'<input type="button" class="btn btn-default del Remove_completely" value="彻底删除"/></span>';
    },
    judgeAppStatusAppend:function (obj) {
        var str = obj.attr('app_status');
        var className = 'btn_remove';
        if (obj.parent().hasClass('btn_remove')) className = 'btn_remove_old';
        else className = 'btn_remove';
        if (str == '007005') obj.parent().parent().append( '<span class="state '+className+'">待审核</span>');
        else if (str == '007010') obj.parent().parent().append( '<span class="state '+className+'">生成中</span>');
        else if (str == '007015') obj.parent().parent().append( '<span class="buttons '+className+'"><span class="refuse">被拒绝：资料不全</span> '
            +'<input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/>'
            +'<input type="button" class="btn btn-default recovery" app_status="007005" value="重新提交"/>'
            +'<input type="button" class="btn btn-default del recovery" app_status="007020" value="删除"/></span>');
        else if (str == '007025') obj.parent().parent().append( '<span class="online '+className+'">已上线 <input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/></span>');
        else if (str == '007030') obj.parent().parent().append( '<span class="online '+className+'">已下线 </span>');
        else if (str == '007020') obj.parent().parent().append( '<span class="buttons '+className+'"><input type="button" class="btn btn-default recovery" app_status="007015" value="恢复"/>'
            +'<input type="button" class="btn btn-default del Remove_completely" value="彻底删除"/></span>');
        if (obj.parent().hasClass('btn_remove')) obj.parent().parent().children('.btn_remove').remove();
        else obj.parent().parent().children('.btn_remove_old').remove();
        Method.initButtonClickEvent();
    },
    initButtonClickEvent:function () {
        $('.recovery').unbind('click').on('click',function () {
            var school_id = $(this).parent().parent().attr('school_id');
            Method.judgeAppStatusAppend($(this));
            $.myajax({
                url:'schoolAction/updateSchool',
                data:{school_id:school_id,status:$(this).attr('app_status')},
                datatype:'json',
                type: 'POST',
                success:function(data){
                    Method.errorMessage('操作成功...','信息提示！');
                }
            });
        });
        $('.recover').on('click',function () {
            parameterVO.totalModulePrice = 0;
            var school_id = $(this).parent().parent().attr('school_id');
            if (isNotEmpty(parameterVO.school_list[school_id].main_url)) {
                $('#yesMainUrl').prop('checked',true);
                $('#noMainUrl').prop('checked',false);
                $('#MainUrl').show();
                $('#MainUrl').val(parameterVO.school_list[school_id].main_url);
            } else {
                $('#MainUrl').val('');
                $('#MainUrl').hide();
                $('#noMainUrl').prop('checked',true);
                $('#yesMainUrl').prop('checked',false);
            }
            $('#Determine').attr('school_id',school_id);
            Method.loadSchoolServerConfig(school_id);
            Method.loadSchoolAppTemplateList(school_id);
            Method.bindSubmitClickEvent();
        });
        $('.Remove_completely').on('click',function () {
            var school_id = $(this).parent().parent().attr('school_id');
            $.myajax({
                url:'schoolAction/removeSchool',
                data:{school_id:school_id},
                datatype:'json',
                type: 'POST',
                success:function(data){
                    Method.errorMessage('该学校删除成功...','删除提示！');
                    Method.initSchoolList();
                }
            });
        })
    },
    loadAppTemplateList:function (school_id) {//系统默认模块
        $.myajax({
            url:'templateAction/getMandatoryTemplateList',
            data:{school_type:parameterVO.school_list[school_id].school_type},
            datatype:'json',
            success:function(data){
                var date = {};
                var result = data.result.data;
                var item = result[0];
                var obj = eval(item.module_list);
                for (var i in obj) {
                    date[obj[i].module_code] = obj[i].is_must;
                }
                parameterVO.moduleCode_list = date;
                Method.loadAppModuleList(school_id);
            }
        });
    },
    loadSchoolAppTemplateList:function (school_id) {//学校当前选中模块
        $.myajax({
            url:'moduleAction/getSchoolModuleBasicsList',
            data:{school_type:parameterVO.school_list[school_id].school_type,is_inactive:0,school_id:school_id},
            datatype:'json',
            success:function(data){
                var date = {};
                var result = data.result.data;
                for(var i in result){
                    var item = result[i];
                    date[item.module_id] = item;
                }
                parameterVO.moduleID_list = date;
                Method.loadAppTemplateList(school_id);
            }
        });
    },
    loadAppModuleList:function (school_id) {
        $.myajax({
            url:'templateAction/getModuleBasicsList',
            data:{school_type:parameterVO.school_list[school_id].school_type},
            datatype:'json',
            success:function(data){
                var result = data.result.data;
                var _div = Method.setModuleListHtml(result);
                _div = '<h3 style="font-size: 15px;line-height: 0;">请选择功能模块<span id="totalModulePrice">收费模块：￥'+parameterVO.totalModulePrice+'/年</span></h3>' + _div;
                $('#schoolModuleList').empty().append(_div);
                $('.styled').unbind('click').on('click',function () {
                    if ($(this).is(':checked')) {
                        parameterVO.totalModulePrice = accAdd(parameterVO.totalModulePrice,parameterVO.moduleList[$(this).attr('module_id')].module_price);
                    } else {
                        parameterVO.totalModulePrice = accSub(parameterVO.totalModulePrice,parameterVO.moduleList[$(this).attr('module_id')].module_price);
                    }
                    if ($(this).hasClass('checked')) {
                        $(this).removeClass('checked');
                    } else {
                        $(this).addClass('checked');
                    }
                    $('#totalModulePrice').empty().append('收费模块：￥'+parameterVO.totalModulePrice+'/年');
                });
            }
        });
    },
    setModuleListHtml:function (result) {
        var _divDefault = '';
        var _div = '';
        var data = {};
        _divDefault += '<div class="zjq-function-module"> <h4><span></span>基本类</h4>'
            +'<ul class="zjq-must-sel zjq-function-block clearfix">';
        _div += '<div class="zjq-function-module"> <h4><span></span>可选类</h4>'
            +'<ul class="zjq-function-block clearfix">';
        for (var i in result) {
            var item = result[i];
            data[item.module_id] = item;
            if (parameterVO.moduleCode_list[item.module_code] == 1) {
                _divDefault += '<li><div class="zjq-mess-block">'
                    +'<img src="'+item.icon_url+'"/> <span>￥'+item.module_price+'</span>'
                    +'<i class="fa fa-check"></i> </div> <p>'+item.module_name+'</p> </li>';
                parameterVO.totalModulePrice = accAdd(parameterVO.totalModulePrice,item.module_price);
                continue;
            }
            var checkbox = '';
            if (isNotEmpty(parameterVO.moduleID_list[item.module_id])) {
                parameterVO.totalModulePrice = accAdd(parameterVO.totalModulePrice,item.module_price);
                checkbox = 'checked';
            }
            _div += '<li> <div class="zjq-mess-block">'
                +'<img src="'+item.icon_url+'"/> <span price="'+item.module_price+'">￥'+item.module_price+'</span>'
                +'<div class="checkbox checkbox-success">'
                +'<input id="checkbox'+i+'" class="styled" type="checkbox" module_id="'+item.module_id +'" data-index="'+i+'"  '+checkbox+'>'
                +'<label for="checkbox'+i+'"></label></div></div><p>'+item.module_name+'</p></li>';
        }
        parameterVO['moduleList'] = data;
        _divDefault += '</ul></div>';
        _div += '</ul></div>';
        return _divDefault+_div;
    },
    loadSchoolServerConfig:function (school_id) {
        $.myajax({
            url:'schoolAction/getSchoolServerConfig',
            data:{school_id:school_id},
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
                var obj = parameterVO.config_list;
                for (var i in obj) {
                    var item = obj[i];
                    if (i > 3) continue;
                    var price = accAdd(item.bandwidth_price,accAdd(item.memory_price,item.disk_price));
                    _div += '<div class="zjq-row-style clearfix"><div class="zjq-table-cell">'
                        +'<div class="radio radio-info radio-inline zjq-grade-name">'
                        +'<input type="radio" id="sel'+i+'" name="servicer" class="inputRadio" config_id="'+item.server_config_id+'"><label for="sel'+i+'"></label>'
                        +'</div></div><div class="zjq-table-cell">'+item.memory+'G</div>'
                        +'<div class="zjq-table-cell">'+item.disk+'G</div><div class="zjq-table-cell">'+item.bandwidth+'M</div>'
                        +'<div class="zjq-table-cell" price="'+price+'">￥'+price+'</div><div class="zjq-acive-border"></div>'
                        +'</div>';
                }
                var price = accAdd(result.bandwidth_price,accAdd(result.memory_price,result.disk_price));
                _div += '<div class="zjq-row-style clearfix" id="clearfix"><div class="zjq-table-cell">'
                    +'<div class="radio radio-info radio-inline zjq-grade-name">'
                    +'<input type="radio" id="sel4" name="servicer" class="inputRadio radioed" checked><label for="sel4"></label>'
                    +'</div></div><div class="zjq-table-cell">'+result.memory+'G</div>'
                    +'<div class="zjq-table-cell">'+result.disk+'G</div><div class="zjq-table-cell">'+result.bandwidth+'M</div>'
                    +'<div class="zjq-table-cell" price="'+price+'">￥'+price+'</div><div class="zjq-acive-border"></div>'
                    +'</div>';
                $('#ConfigList').empty().append(_div);
            }
        });
    },
    initServerConfig:function () {
        $.myajax({
            url:'systemAction/getServerConfigList',
            datatype:'json',
            success:function(data){
                var result = data.result.data;
                var date = {};
                for (var i in result) {
                    var item = result[i];
                    item.disk_id = item.server_config_id;
                    item.memory_id = item.server_config_id;
                    item.bandwidth_id = item.server_config_id;
                    date[item.server_config_id] = item;
                }
                parameterVO.config_list = date;
            }
        });
    },
    initMain:function () {
        $('#yesMainUrl').unbind('click').on('click',function () {
            $('#MainUrl').val(parameterVO.school_list[$('#Determine').attr('school_id')].main_url).show();
        });
        $('#noMainUrl').unbind('click').on('click',function () {
            $('#MainUrl').val('').hide();
        });
    },
    bindDetermineRechargeClickEvent:function () {
        $('#DetermineRecharge').on('click',function () {
            if (isEmpty($('#btn_balance').val()) || Method.judgeSign($('#btn_balance').val())) {toastrMassage('请输入正确的收费金额...');return false;}
            var consumption_money = $('#btn_balance').val();
            var  agent_id = parameterVO.AgentVO.agent_id;
            var _content = '<div><div>跳转成功，是/否成功充值......</div>' +
                '<button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn-info" data-dismiss="modal" id="Yes">是</button><button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn_Close" data-dismiss="modal">否</button></div>';
            $('#error_content').empty().append(_content);
            $('#myModal2').on('click',function () {
                if (isEmpty($('#Yes').attr("agent_id"))) return;
                $.myajax({
                    url:'balanceAction/getBalanceByID',
                    data:{agent_id:parameterVO.AgentVO.agent_id},
                    datatype:'json',
                    type: 'POST',
                    success:function(data){
                        var result = data.result.data;
                        $('#Balance').attr("balance",result.balance);
                        $('#Balance').empty().append(result.balance);
                    }
                });
            });
            window.open("balanceAction/webPayment?consumption_money="+ consumption_money +"&agent_id="+ agent_id +"");
        });
    },bindRechargeClickEvent:function() {
        $('#recharge').on('click',function () {
            var _content = '<div><div>输入充值金额:<input class="form-control judgeNumber" type="number" min="0" onpaste="return false;" id="btn_balance" placeholder="￥'+$('#Balance').text()+'" style=" margin-top: 15px;" min="0"/></div>' +
                '<button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn-info" id="DetermineRecharge">确定</button><button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn_Close" data-dismiss="modal">关闭</button></div>';
            Method.errorMessage('账户充值',_content);
            Method.bindDetermineRechargeClickEvent();
            Method.judgeNumber();
        });
    },judgeNumber:function () {
        $('.judgeNumber').keyup(function () {
            if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val('');}
        });
    },judgeSign:function(num) {
        var reg = new RegExp("^-?[0-9]*.?[0-9]*$");
        if ( reg.test(num) ) {
            var absVal = Math.abs(num);
            return num==absVal?false:true;
        } else {
            return true;
        }
    },bindSubmitClickEvent:function () {
        $('#Determine').unbind('click').on('click',function () {
            var schoolVO = {};
            schoolVO.school_id = $(this).attr('school_id');
            schoolVO.school_type = parameterVO.school_list[$(this).attr('school_id')].school_type;
            schoolVO.module_ids = Method.checkSelectedModuleInfo();
            if (!$('input:radio[name="servicer"]:checked').hasClass('radioed'))
                schoolVO.school_server = JSON.stringify([parameterVO.config_list[$('input:radio[name="servicer"]:checked').attr('config_id')]]);
            schoolVO.main_url = $('#MainUrl').val();
            $.myajax({
                url:'schoolApplyAction/addSchool',
                data:schoolVO,
                datatype:'json',
                type: 'POST',
                success:function(data){
                    Method.errorMessage('该学校修改学校成功...','信息提示！');
                }
            });
        });
    },
    checkSelectedModuleInfo:function () {
        var module_ids;
        $('.checked').each(function(){
            if (isNotEmpty(module_ids)){
                module_ids = module_ids+','+$(this).attr('module_id');
            }else{
                module_ids = $(this).attr('module_id');
            }
        });
        return module_ids;
    }
};

function initAgentAdminPage() {
    Method.initAgentBalance();
    Method.initSchoolList();
    Method.initServerConfig();
    Method.initMain();
    Method.bindRechargeClickEvent();
}