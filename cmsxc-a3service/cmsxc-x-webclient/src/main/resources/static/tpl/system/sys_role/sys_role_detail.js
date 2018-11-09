/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_role_detail_controller', ['$scope', '$http','$modal', '$modalInstance','sysRoleId', function ($scope, $http,$modal, $modalInstance,sysRoleId) {

    $scope.sysRole={};

    $scope.sysResources = [];
    $http.get('sys_resource/findSysResAll').success(function(data){
        $scope.sysResources = data.data;
        $http.get('sys_resource/findSysResBySysRoleId?roleId='+sysRoleId).success(function(result){
            for(var i in result.data){
                checkSysResource($scope.sysResources, result.data[i]);
            }
        })

    });

    function checkSysResource(allResources ,contailSysResource){
        for(var i in allResources){
            if(allResources[i].id == contailSysResource.id){
                allResources[i].selected = true;
                return;
            }
            if(allResources[i].children != null || allResources[i].children.length != 0){
                checkSysResource(allResources[i].children, contailSysResource);
            }
        }
    }


    $http.get('sys_role/findSysRoleById?id='+ sysRoleId).success(function(data){
        $scope.sysRole = data.data;
    });


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


