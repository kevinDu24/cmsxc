/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_organization_modify_controller', ['$scope', '$http','$modal', '$modalInstance','toaster','sysOrgId', function ($scope, $http,$modal, $modalInstance,toaster,sysOrgId) {

    $scope.sysOrg={};

    $scope.formValidate = false;

    $scope.submit = false;

    $http.get('sys_organization/findSysOrganizationVoById?id='+ sysOrgId).success(function(data){
        $scope.sysOrg = data.data;
        if($scope.sysOrg.orgPropertyId == 0){
            $scope.sysOrg.orgPropertyName = "无组织属性";
        }
        if($scope.sysOrg.orgParentId == 0){
            $scope.sysOrg.orgParentName = "组织机构";
        }
    });



    /**
     * 保存组织机构属性信息
     */
    $scope.modify = function () {

        if(!$scope.form.$invalid) {

            $scope.submit = true;

            $http.put('sys_organization/modifySysOrganization', $scope.sysOrg).success(function (data) {
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


