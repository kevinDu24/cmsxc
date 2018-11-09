/**
 * Created by yuanzhenxia on 2018/1/10.
 */
app.controller('create_recovery_user_controller', ['$scope', '$http','$modal', '$modalInstance', function ($scope, $http,$modal, $modalInstance) {

    $scope.recovery={};
    /**
     * 保存用户信息
     */
    $scope.save = function () {
        if(!$scope.form.$invalid) {
            $http.post('recovery_user/saveRecoveryUser', $scope.recovery).success(function (data) {
                if (data.code == Response.successCode) {
                    // alert(data.message);
                    $scope.close(Response.successMark);

            }else {
                    alert(data.message);
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


