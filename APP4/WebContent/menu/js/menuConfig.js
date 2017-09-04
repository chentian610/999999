var school_id = localStorage.getItem('school_id');
var $this;

function initRoleList() {
    var DictVO = {"dict_group":"016","school_id":school_id};
    $.myajax({
        url : 'dictAction/getDictSchoolList',
        data:DictVO,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var _li = '';
            var result = data.result.data;
            result = removalDuplicate(result);
            for (var i in result) {
                var item = result[i];
                if (item.dict_code == '016025'||item.dict_code == '016030') continue;
                var isOperable = judgeIsOperable(item.dict_code);
                _li += '<li '+(i==0?'class="active clickRole"':'class="clickRole"')+' data-dict_code="'+item.dict_code+'" isOperable="'+isOperable+'" data-dict_value="'+item.dict_value+'"><span></span>'+item.dict_value+'</li>';
            }
            $('#RoleList').empty().append(_li);
            $this = $('.active');
            initRoleMenuList();
            bindClickRoleClickEvent();
        }
    });
}

function bindEditRoleClickEvent() {
    $('#editRole').on('click',function () {
        $this.empty().append('<span></span><input type="text" id="RoleValue" placeholder="'+$this.attr('data-dict_value')+'" class="form-control nav-input">');
        $('#navIcon').addClass('display');
        $('#navBtn').removeClass('display');
    });
}

function bindDeleteRoleClickEvent() {
    $('#deleteRole').on('click',function () {
        swal({
            title: "您确定要删除这些信息吗",
            text: "删除后将无法恢复，请谨慎操作！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "删除",
            closeOnConfirm: false
        }, function () {
            var DictVO = {"school_id":school_id,"dict_code":$this.attr('data-dict_code')}
            $.myajax({
                url:"dictAction/removeSchoolRoleDict",
                data:DictVO,
                datatype:"json",
                success:function(data){
                    swal("删除提示！", "删除"+data.msg, "success");
                    $('#nav').css({top: '17.4%'});
                    initRoleList();
                }
            });
        });
    });
}

function bindUpdateRoleClickEvent() {
    $('#updateRole').on('click',function () {
        var roleValue = $('#RoleValue').val();
        if (isEmpty(roleValue)) {toastrMassage("请输入角色新名称...");return;}
        var DictVO = {"school_id":school_id,"dict_code":$this.attr('data-dict_code'),"dict_value":roleValue};
        $.myajax({
            url : 'dictAction/updateDictSchool',
            data: DictVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                if (!equals(data.success,true)) {toastrMassage("修改角色名称错误.请刷新页面重试...");return;}
                toastrMassage("修改角色名称成功...");
                $this.empty().append('<span></span>'+roleValue+'');
                $('#navIcon').removeClass('display');
                $('#navBtn').addClass('display');
            }
        });
    });
}

function binNavBtnCloseClickEvent() {
    $('#navBtnClose').on('click',function () {
        $this.empty().append('<span></span>'+$this.attr('data-dict_value')+'');
        $('#navIcon').removeClass('display');
        $('#navBtn').addClass('display');
    });
}

function bindClickRoleClickEvent() {
    $('.clickRole').unbind('click').on('click',function () {
        $('.clickRole').css("margin-bottom","27px");
        if (equals($(this).attr('isOperable'),true)) {
            $this.empty().append('<span></span>'+$this.attr('data-dict_value')+'');
            $(this).css("margin-bottom","53px");
            var left = accSub($('#body').css('width').replace('px',''),$('.zjq-permission-page').css('width').replace('px',''))/2;
            $('#nav').css({top: accAdd($(this).offset().top,20),left:accAdd(left,50)});
            $('#nav').removeClass('display');
            $('#navIcon').removeClass('display');
            $('#navBtn').addClass('display');
        } else  $('#nav').addClass('display');
        $('.clickRole').removeClass('active');
        $(this).addClass('active');
        $this =  $(this);
        getThisSchoolRoleMenuList();
    });
}

function bindWindowSize() {
    $(window).resize(function (){
        var left = accSub($('#body').css('width').replace('px',''),$('.zjq-permission-page').css('width').replace('px',''))/2;
        $('#nav').css({left:accAdd(left,50)});
    });
}

function getThisSchoolRoleMenuList() {
    var thisRoleMenuList =  {};
    var RoleMenuVO = {"role_code":$('.active').attr('data-dict_code'),"school_id":school_id};
    $.myajax({
        url : 'menuAction/getSchoolRoleMenu',
        data: RoleMenuVO,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var result = data.result.data;
            for (var i in result) {
                var item = result[i];
                thisRoleMenuList[''+item.menu_id+''] = true;
            }
            initRolMenu(thisRoleMenuList);
        }
    });
}

function initRolMenu(thisRoleMenuList) {
    $('.styled').removeClass('isClick');
    $('.styled').prop('checked',false);
    $('.styled').each(function () {
        $(this).prop('checked',thisRoleMenuList[$(this).attr('data-menu_id')]);
    });
    $('.isOpen').each(function () {
        $(this).find('img').attr('isOpen',false);
        $(this).parent().parent().children('.clearfix').hide();
        $(this).find('img').attr('src','menu/icon/plus.png');
    });
}

function bindUpdateRoleMenuClickEvent() {
    $('#updateRoleMenu').on('click',function () {
        var RoleMenuVO = {"school_id":school_id,"role_code":$this.attr('data-dict_code'),"menu_ids":setIsClickMenuList()}
        $.myajax({
            url : 'menuAction/updateRoleMenu',
            data: RoleMenuVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                toastrMassage("修改角色菜单"+data.msg+"...");
            }
        });
    });
}

function setIsClickMenuList() {
    var menu_ids;
    $('.isClick').each(function () {
        if (isNotEmpty(menu_ids)) menu_ids += ',' + $(this).attr('data-menu_id');
        else menu_ids = $(this).attr('data-menu_id');
    });
    return menu_ids;
}

function bindAddRoleClickEvent() {
    $('#addRole').on('click',function () {
        var roleName = $('#RoleName').val();
        if (isEmpty(roleName)) {toastrMassage("请输入角色名称...");return;}
        var DictVO = {"dict_group":"016","school_id":school_id,"dict_value":roleName};
        $.myajax({
            url : 'dictAction/addDictSchool',
            data: DictVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var result = data.result.data;
                if (isEmpty(result)) {toastrMassage("添加角色失败..."); return;}
                $('.btnClose').click();
                $('#RoleList').append('<li class="clickRole" data-dict_code="'+result.dict_code+'"><span></span>'+result.dict_value+'</li>');
                toastrMassage("添加角色成功...");
                bindClickRoleClickEvent();
            }
        });
    });
}

function initRoleMenuList() {
    var menuVO = {};
    menuVO.school_id = localStorage.getItem("school_id");
    menuVO.user_type = localStorage.getItem('user_type');
    menuVO.is_active = 1;
    menuVO.role_code = localStorage.getItem("user_type");
    $.myajax({
        url : 'menuAction/getParentMenuList',
        data:menuVO,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var _li = '';
            var result = data.result.data;
            for (var i in result) {
                var item = result[i];
                if (item.parent_id != 0) continue;
                var _div = initChildNode(result,item.menu_id);
                _li += '<li><div class="zjq-check-block"> <span class="isOpen" ><img src="menu/icon/plus.png" isOpen="false" style="'+(isEmpty(_div)?'display:none':'')+'"></span>'
                    +'<div class="checkbox checkbox-success"> <input data-menu_id="'+item.menu_id+'" isSelected="false" id="checkbox'+i+'" class="styled classA" type="checkbox">'
                    +'<label for="checkbox'+i+'">'+item.menu_name+'</label> </div> </div>';
                _li += _div;
                _li += '</li>';
            }
            $('#RoleMenuList').empty().append(_li);
            bindisOpenClickEvent();
            bindClassAClickEvent();
            getThisSchoolRoleMenuList();
            bindSecondLevelClickEvent();
        }
    });
}

function bindClassAClickEvent() {
    $('.classA').on('click',function () {
        if ($(this).hasClass('isClick')) $(this).removeClass('isClick');
        else $(this).addClass('isClick');
        var flag = $(this).prop("checked");
        $(this).parent().parent().parent().children('.clearfix').find('div').each(function () {
            if ($(this).find('input').hasClass('isClick')) $(this).find('input').removeClass('isClick');
            else $(this).find('input').addClass('isClick');
            $(this).find('input').prop('checked',flag);
        });
    });
}

function bindSecondLevelClickEvent() {
    $('.secondLevel').on('click',function (){
        if ($(this).hasClass('isClick')) $(this).removeClass('isClick');
        else $(this).addClass('isClick');
        var flag = $(this).prop("checked");
        var obj = $(this).parent().parent().parent().children('.zjq-check-block').children('.checkbox-success').find('input');
        var success =  obj.prop("checked");
        if (success) return;
        if (obj.hasClass('isClick')) obj.removeClass('isClick');
        else obj.addClass('isClick');
        obj.prop("checked",flag);
    });
}

function bindisOpenClickEvent() {
    $('.isOpen').on('click',function () {
        var flag = $(this).find('img').attr('isOpen');
        if (equals(flag,true)) {
            $(this).parent().parent().children('.clearfix').hide();
            $(this).find('img').attr('isOpen',false);
            $(this).find('img').attr('src','menu/icon/plus.png');
        } else {
            $(this).parent().parent().children('.clearfix').show();
            $(this).find('img').attr('isOpen',true);
            $(this).find('img').attr('src','menu/icon/minus.png');
        }
    });
}

function initChildNode(result,menu_id) {
    var _div = "";
    for (var i in result) {
        var item = result[i];
        if (item.parent_id != menu_id) continue;
        _div += '<div class="checkbox checkbox-success checkbox-inline col-md-5">'
            +'<input data-menu_id="'+item.menu_id+'" id="checkbox'+i+'" class="styled secondLevel" type="checkbox">'
            +'<label for="checkbox'+i+'">'+item.menu_name+'</label></div>';
    }
    if (isEmpty(_div)) return _div;
    _div = '<div class="zjq-chilid-table clearfix" style="display: none;">'+_div+ '</div>';
    return _div;
}

function removalDuplicate(data) {
    var date = [];
    var roleCodes = {};
    for (var i in data) {
        var item = data[i];
        if (roleCodes[item.dict_code]) continue;
        date.push(item);
        roleCodes[item.dict_code] = true;
    }
    return date;
}

function judgeIsOperable(dict_code) {
    var str = parseInt("016020");
    var dictCode = parseInt(dict_code);
    if (str >= dictCode) return false;
    else return true;
}

function equals(str,str1) {
    if (str == str1 || str == ''+str1+'') return true;
    else return false;
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'|| str.length ==0;
}

function toastrMassage(content) {
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