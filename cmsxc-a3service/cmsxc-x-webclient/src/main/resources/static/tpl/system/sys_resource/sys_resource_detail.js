/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_resource_detail_controller', ['$scope', '$http','$modal', '$modalInstance','sysRes', function ($scope, $http,$modal, $modalInstance,sysRes) {

    $scope.sysRes=sysRes;

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


