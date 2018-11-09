/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_organization_detail_controller', ['$scope', '$http','$modal', '$modalInstance','toaster','sysOrg', function ($scope, $http,$modal, $modalInstance,toaster,sysOrg) {

    $scope.sysOrg = sysOrg;

    if($scope.sysOrg.orgPropertyId == 0){
        $scope.sysOrg.orgPropertyName = "无组织属性";
    }
    if($scope.sysOrg.orgParentId == 0){
        $scope.sysOrg.orgParentName = "组织机构";
    }
    if($scope.sysOrg.orgType == 0){
        $scope.sysOrg.orgTypeName = "公司/集团";
    }else if($scope.sysOrg.orgType == 1){
        $scope.sysOrg.orgTypeName = "区域";
    }else if($scope.sysOrg.orgType == 2){
        $scope.sysOrg.orgTypeName = "分公司";
    }else if($scope.sysOrg.orgType == 3){
        $scope.sysOrg.orgTypeName = "SP";
    }

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


