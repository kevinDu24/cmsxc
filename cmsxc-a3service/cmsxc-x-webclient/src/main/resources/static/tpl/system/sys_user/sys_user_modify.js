/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_user_modify_controller', ['$scope', '$http','$modal', '$modalInstance','toaster' ,'sysUserId', function ($scope, $http,$modal, $modalInstance,toaster,sysUserId) {

    $scope.sysUser={};

    $scope.formValidate = false;

    $scope.submit = false;

    $http.get('sys_user/findSysUserById?id='+ sysUserId).success(function(data){
        $scope.sysUser = data.data;
    });

    /**
     * 保存用户信息
     */
    $scope.modify = function () {
        if(!$scope.form.$invalid) {
            $http.put('sys_user/modifySysUser', $scope.sysUser).success(function (data) {
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


