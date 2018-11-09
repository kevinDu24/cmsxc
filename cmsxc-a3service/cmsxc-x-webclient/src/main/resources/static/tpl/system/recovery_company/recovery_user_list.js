/**
 * Created by yuanzhenxia on 2018/1/9.
 */
app.controller('recovery_user_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {
    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'recovery_user/findRecoveryUserByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'recovery_company_table',
        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            // defaultCheckBox(),
            {title:'公司简称',data:'recoveryShortName',width:'15%'},
            {title:'公司全称',data:'recoveryFullName',width:'20%'},
            {title:'联系人姓名',data:'contactName',width:'10%'},
            {title:'联系人电话',data:'contactPhone',width:'15%'},
            {title:'联系人邮箱',data:'contactEmail',width:'15%'},
            {title:'联系人地址',data:'contactAddress',width:'20%'},
            {title:'主管注册码',data:'managerRegisterCode',width:'10%'},
            {title:'业务员注册码',data:'salesmanRegisterCode',width:'10%'},
            //操作按钮
            // defaultHandle('searchRecoveryUserr','saveRecoveryUser','deleteRecoveryUser',$compile,$scope)
        ],
        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: CheckBox
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.recoveryShortName = $scope.recoveryShortName;
        params.recoveryFullName = $scope.recoveryFullName;
        params.managerRegisterCode = $scope.managerRegisterCode;
        params.salesmanRegisterCode = $scope.salesmanRegisterCode;
        return params;
    }

    //创建dataTable 封装了datatable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.searchRecoveryUser = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    /******************请求列表查询结束*******************/


    $scope.saveRecoveryUser = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            //size:'lg',
            templateUrl: 'tpl/system/recovery_company/create_recovery_user.html?datetime='+getTimestamp(),
            controller: 'create_recovery_user_controller',
            resolve:{
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

    $scope.deleteRecoveryUser = function(id){
        if(isNull(id) || isUndefined(id))
            id = $scope.dataTable.getRowId();
        if(id == null)
            alert('请选择要删除的数据');
        $http.delete('sys_user/deleteSysUser?id='+id).success(function (data) {
            if(data.code == Response.successCode) {
                toaster_success('删除用户信息成功', toaster);
                $scope.dataTable.fnDraw(true);
            }
            else
                alert(data.message);
        })

    }





}])
;