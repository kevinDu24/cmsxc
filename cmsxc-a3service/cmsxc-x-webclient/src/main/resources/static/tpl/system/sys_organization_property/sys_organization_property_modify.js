/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_organization_property_modify_controller', ['$scope', '$http','$modal', '$modalInstance','toaster','sysOrgProId', function ($scope, $http,$modal, $modalInstance,toaster,sysOrgProId) {

    $scope.sysOrgPro={};

    $scope.formValidate = false;

    $scope.submit = false;

    $http.get('sys_organization_property/findSysOrganizationPropertyById?id='+ sysOrgProId).success(function(data){
        $scope.sysOrgPro = data.data;
    });

    /**
     * 保存组织机构属性信息
     */
    $scope.modify = function () {

        if(!$scope.form.$invalid) {

            $scope.submit = true;

            $http.put('sys_organization_property/modifySysOrganizationProperty', $scope.sysOrgPro).success(function (data) {
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


