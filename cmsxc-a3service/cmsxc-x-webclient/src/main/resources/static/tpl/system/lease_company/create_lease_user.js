/**
 * Created by yuanzhenxia on 2018/1/10.
 */
app.controller('create_lease_user_controller', ['$scope', '$http','$modal', '$modalInstance', function ($scope, $http,$modal, $modalInstance) {

    $scope.Lease={};
    /**
     * 保存用户信息
     */
    $scope.save = function () {
        if(!$scope.form.$invalid) {
            $http.post('lease_user/saveLeaseUser', $scope.Lease).success(function (data) {
                if (data.code == Response.successCode){
                    // alert(data.message);
                    $scope.close(Response.successMark);
            }else{
                    modalAlert($modal, data.message);
            }})
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


