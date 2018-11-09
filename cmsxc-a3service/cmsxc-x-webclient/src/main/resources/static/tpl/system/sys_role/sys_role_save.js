/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_role_save_controller', ['$scope', '$http','$modal', '$modalInstance','toaster', function ($scope, $http,$modal, $modalInstance,toaster) {

    $scope.sysRole={};

    $scope.formValidate = false;

    $scope.submit = false;

    $scope.sysResources = [];

    $http.get('sys_resource/findSysResAll').success(function(data){
        $scope.sysResources = data.data;
    })

    /**
     * 保存角色信息
     */
    $scope.save = function () {

        if(!$scope.form.$invalid) {
            $scope.selectedResources = [];
            for(var i in $scope.sysResources){
                recursionOrg($scope.sysResources[i]);
            }
            $scope.sysRole.resources = $scope.selectedResources;

            $scope.submit = true;

            $http.post('sys_role/saveSysRole', $scope.sysRole).success(function (data) {
                if(data.code == Response.successCode)
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

    function recursionOrg(resource){
        if(resource.selected || resource.__ivhTreeviewIndeterminate){
            $scope.selectedResources.push(resource);
        }
        if(resource.children != null || resource.children.length != 0){
            for(var i in resource.children){
                recursionOrg(resource.children[i]);
            }
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


