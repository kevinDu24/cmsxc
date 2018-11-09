/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_user_detail_controller', ['$scope', '$http','$modal', '$modalInstance','sysUser', function ($scope, $http,$modal, $modalInstance,sysUser) {

    $scope.sysUser=sysUser;

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


