/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_organization_save_controller', ['$scope', '$http','$modal', '$modalInstance','toaster', function ($scope, $http,$modal, $modalInstance,toaster) {

    $scope.sysOrg={orgDisable:0,orgType:0,orgPropertyName:'无组织属性',orgPropertyId:0,orgParentName:'组织机构',orgParentId:0};

    $scope.formValidate = false;

    $scope.submit = false;


    /**
     * 保存组织机构属性信息
     */
    $scope.save = function () {

        if(!$scope.form.$invalid) {

            $scope.submit = true;

            $http.post('sys_organization/saveSysOrganization', $scope.sysOrg).success(function (data) {
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

    $scope.selectOrgPro = function(){

        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization_property/sys_organization_property_list_select.html?datetime='+getTimestamp(),
            controller: 'sys_organization_property_list_select_controller',
            resolve:{
            }
        });
        rtn.result.then(function (data) {
            if(data != null) {
                $scope.sysOrg.orgPropertyName = data.orgPropertyName;
                $scope.sysOrg.orgPropertyId = data.id;
            }
        },function(){

        });

    }

    $scope.selectOrg = function(){

        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization/sys_organization_list_select.html?datetime='+getTimestamp(),
            controller: 'sys_organization_list_select_controller',
            resolve:{
            }
        });
        rtn.result.then(function (data) {
            if(data != null) {
                $scope.sysOrg.orgParentName = data.orgName;
                $scope.sysOrg.orgParentId = data.id;
            }
        },function(){

        });


    }



    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


