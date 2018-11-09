/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_user_save_controller', ['$scope', '$http','$modal', '$modalInstance','toaster','$localStorage', function ($scope, $http,$modal, $modalInstance,toaster,$localStorage) {

    $scope.loginUser=$localStorage.user;
    var loginUserRole = $scope.loginUser.userRole;
    $scope.sysUser = {};
    $scope.isLeaseShow = false;
    $scope.roles = {};
    $scope.leaseCompanys = {};

    /**
     * 选择角色下拉框onchange事件
     */
    $scope.roleChange = function () {
        var type = $scope.role.type;
        if(type == '12'){
            if((JSON.stringify($scope.leaseCompanys) == "{}")){
                $http.get('lease_user/getLeaseUser').success(function(data){
                    if (data.code == Response.successCode) {
                        $scope.leaseCompanys = data.data;
                        $scope.leaseCompany = $scope.leaseCompanys[0];
                    }else {
                        $scope.leaseCompany = null;
                            modalAlert($modal, data.message);
                    }
                }).error(function(data){
                    modalAlert($modal,data);
                })
            }
            if(loginUserRole == '0'){
                $scope.isLeaseShow = true;
            }
        } else {
            $scope.isLeaseShow = false;
        }
    }

    if(loginUserRole == '0' || loginUserRole == '11'){
        $scope.roles = [{name:'委托公司审核专员', type:12}];
        $scope.role = $scope.roles[0];
        $scope.roleChange();
    }

    $scope.formValidate = false;

    $scope.submit = false;

    /**
     * 保存用户信息
     */
    $scope.save = function () {
        $scope.sysUser.userRole = $scope.role.type;
        $scope.sysUser.roleName = $scope.role.name;
        $scope.sysUser.leaseId = $scope.leaseCompany.id;

        if(!$scope.form.$invalid) {

            $scope.submit = true;

            $http.post('sys_user/saveSysUser', $scope.sysUser).success(function (data) {
                if (data.code == Response.successCode)
                    $scope.close(Response.successMark);
                else
                    modalAlert($modal,data.message);
                $scope.submit = false;
            }).error(function(data){
                modalAlert($modal,data);
                $scope.submit = false;
            })

        }else{
            $scope.formValidate = true;
            toaster_info(promptInfo.submitWarn,toaster);
        }
    }


    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


