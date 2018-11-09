/**
 * Created by qiaohao on 2017/5/6.
 */


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "H+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

/**
 * 判断是否为null
 * @param data
 * @returns {boolean}
 */
function isNull(data){
    if(data == null)
        return true;
}

/**
 * 判断是否非null
 * @param data
 * @returns {boolean}
 */
function isNotNull(data){
    return !isNull(data);
}


/**
 * 返回年月日时的时间戳
 * @returns {number}
 */
function getTimestamp(){

    var date = new Date();
    //date.getFullYear();
    //date.getMonth() + 1;
    //date.getDate();
    //date.getHours();
    //date.getSeconds();
    var newDate = new Date(date.getFullYear(),date.getMonth(),date.getDate(),date.getHours());
    return newDate.getTime();
}

/**
 * url后跟上缓存的时间戳
 * @returns {string}
 */
function getCacheTime(){
    return '?datetime='+getTimestamp();
}



/**
 * 判断是否为空
 * @param data
 * @returns {boolean}
 */
function isEmpty(data){
    if(data == "")
        return true;
}

/**
 * 判断是否非空
 * @param data
 * @returns {boolean}
 */
function isNotEmpty(data){
    return !isEmpty(data);
}

function retsetColumn(title,column,$compile,$scope) {
    return {
        title: title,
        data: 'id',
        width:'120px',
        render: function (data, type, row, meta) {
            return '<span translate="code.gender.'+row[column]+'">'+row[column]+'</span>';
        },
        fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
            $compile(nTd)($scope);
        }
    }
}


/**
 * 判断一个值是否是未定义
 * @param val
 * @returns {boolean}
 */
function isUndefined(data){
    if(typeof(data) == undefineds.str || typeof(data) == undefineds.obj){
        return true;
    }else if(data == undefineds.str || data == undefineds.obj){
        return true;
    }else{
        return false;
    }
}

/**
 * 判断一个值是否非未定义
 * @param data
 * @returns {boolean}
 */
function isNotUndefined(data){
    return !isUndefined(data);
}

/**
 * 判断一个值是否不为null和空字符
 * @param data
 * @returns {boolean}
 */
function isNotNullEmpty(data){
    return isNotNull(data) && isNotEmpty(data);
}

/**
 * 判断一个值是否为null或者空字符
 * @param data
 * @returns {boolean}
 */
function isNullEmpty(data){
    return isNull(data)  || isEmpty(data);

}

/**
 * 判断一个值是否不是未定义 并且 不为空
 * @param data
 * @returns {boolean}
 */
function isNotUndefinedNull(data){
    return isNotUndefined(data) && isNotNull(data);

}

/**
 * 判断一个值是否未定义 或者 为空
 * @param data
 * @returns {boolean}
 */
function isUndefinedNull(data){
    return isUndefined(data) || isNull(data);
}



var undefineds = {
    obj : undefined,
    str : "undefined"
}


function toaster_success (text,toaster){
    toaster.pop('success','',text);
}

function toaster_info (text,toaster){
    toaster.pop('info','',text);
}

function toaster_error (text,toaster){
    toaster.pop('error','',text);
}

//设置AJAX的全局默认选项
$.ajaxSetup({
    error: function(jqXHR, textStatus, errorMsg){ // 出错时默认的处理函数
        if(jqXHR.status == 401)
            location.href = "/#/access/signin";
    }
});

function modalConfirm($modal,confirm,cancel,info,header){
    if(isUndefined(header) || isNull(header))
        header = '提示信息';
    if(isUndefined(info) || isNull(info))
        info = '您确定需要执行当前动作吗？';
    var rtn = $modal.open({
        backdrop : 'static',
        size:'sm',
        templateUrl: 'tpl/alert/confirm.html'+getCacheTime(),
        controller: 'modal_confirm_controller',
        resolve:{
            header : function(){return header},
            info : function(){return info}
        }
    });
    rtn.result.then(function (status) {
        if(status == Response.successMark) {
            if(isNotUndefined(confirm))
                confirm();
        }
    },function(){
        if(isNotUndefined(cancel))
            cancel();
    });
}


function modalAlert($modal,info,header){
    if(isUndefined(header) || isNull(header))
        header = '提示信息';
    if(isUndefined(info) || isNull(info))
        info = '警告';

    var rtn = $modal.open({
        backdrop : 'static',
        size:'sm',
        templateUrl: 'tpl/alert/alert.html'+getCacheTime(),
        controller: 'modal_alert_controller',
        resolve:{
            header : function(){return header},
            info : function(){return info}
        }
    });
    rtn.result.then(function (status) {

    },function(){

    });
}


var promptInfo = {
    submitWarn : "请填写正确的信息"
}
