/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_organization_property_detail_controller', ['$scope', '$http','$modal', '$modalInstance','sysOrgPro', function ($scope, $http,$modal, $modalInstance,sysOrgPro) {

    $scope.sysOrgPro=sysOrgPro;

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


