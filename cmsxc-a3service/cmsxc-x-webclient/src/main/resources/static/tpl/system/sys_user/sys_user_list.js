/**
 * Created by qiaohao on 2018/1/9.
 */
app.controller('sys_user_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', '$localStorage',function ($scope, $http, $modal, toaster,$compile,$localStorage) {

    $scope.isShow = false;
    $scope.loginUser=$localStorage.user;
    var loginUserRole = $scope.loginUser.userRole;
    if(loginUserRole == '0' || loginUserRole == '11'){
        $scope.isShow = true;
    }

    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_user/findSysUserByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_user_table',

        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            defaultCheckBox(),
            // setuserId('detailSysUser',$compile,$scope),
            {title:'用户账号',data:'userId',width:'100px'},
            {title:'用户姓名',data:'userName',width:'100px'},
            {title:'用户角色',data:'userRole',width:'120px',
                render: function (data, type, row, meta) {
                    if(row.userRole == '0')
                        return '超级管理员';
                    else if(row.userRole == '01')
                        return '收车公司一级管理员';
                    else if(row.userRole == '02')
                        return '收车公司主管';
                    else if(row.userRole == '03')
                        return '收车公司业务员';
                    else if(row.userRole == '04')
                        return '散户';
                    else if(row.userRole == '11')
                        return '委托公司一级管理员';
                    else if(row.userRole == '12')
                        return '委托公司审核专员';
                    else
                        return '';
                }
            },
            {title:'所属收车公司',data:'recoveryFullName',width:'120px'},
            {title:'所属金融机构',data:'leaseFullName',width:'120px'}
            //拓展操作按钮
            //defaultHandle('modifySysUser','detailSysUser','deleteSysUser',$compile,$scope)
        ],

        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
     function dataTableParams($scope){
        params = {};
        params.userId = $scope.userId;
        params.userName = $scope.userName;
         params.userRole = $scope.userRole;
        return params;
    }

    //创建dataTable 封装了datatable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.searchSysUser = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    $scope.resetSysUser = function(){
        $scope.userId="";
        $scope.userName="";
        $scope.userRole = "";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    /******************请求列表查询结束*******************/


    $scope.saveSysUser = function(){

        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_user/sys_user_save.html?datetime='+getTimestamp(),
            controller: 'sys_user_save_controller',
            resolve:{
                userRole:function () {
                    return $scope.loginUser.userRole;
                }
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('添加用户信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){

        });
    }


    $scope.modifySysUser = function(sysUserId){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
         else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_user/sys_user_modify.html?datetime='+getTimestamp(),
                controller: 'sys_user_modify_controller',
                resolve:{
                    sysUserId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('编辑用户信息成功',toaster);
                    $scope.dataTable.fnDraw(true);
                }
            },function(){
            });
        }
    }


    $scope.detailSysUser = function(sysUserId){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_user/sys_user_detail.html?datetime='+getTimestamp(),
            controller: 'sys_user_detail_controller',
            resolve:{
                sysUser : function (){ return $scope.dataTable.getRow(sysUserId) }
            }
        });
        rtn.result.then(function (status) {

        },function(){
        });
    }

    $scope.deleteSysUser = function(){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键
        if(rowsIds.length < 1){
            modalAlert($modal,'请您选择需要删除的数据！');
        }else{
            modalConfirm($modal,function(){
                $http.post('sys_user/deleteSysUserByIds',rowsIds).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除用户信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }else
                        modalAlert($modal,data.message);
                })
            },null,'您确定需要删除吗？')
        }
    }

    $scope.modifySysUserPwd = function(){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
        else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_user/sys_user_modify_pwd.html?datetime='+getTimestamp(),
                controller: 'sys_user_modify_pwd_controller',
                resolve:{
                    sysUserId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {

                if(status == Response.successMark) {
                    toaster_success('修改密码成功',toaster);
                }

            },function(){
            });

        }

    }


    // function setuserId(fucName,$compile,$scope) {
    //     return {
    //         title: '用户账号',
    //         data: 'userId',
    //         width:'120px',
    //         render: function (data, type, row, meta) {
    //             return '<a class="a1" ng-click="'+fucName+'(\'' + data + '\')">'+row.userId+'</a>';
    //         },
    //         fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
    //             $compile(nTd)($scope);
    //         }
    //     }
    // }

    
}])
;