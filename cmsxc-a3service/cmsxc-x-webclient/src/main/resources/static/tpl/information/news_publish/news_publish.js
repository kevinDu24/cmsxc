/**
 * Created by LEO on 16/9/28.
 */
app.controller('newsPublishController', ["toaster",'$rootScope', '$scope', '$http', '$cookies', function (toaster,$rootScope, $scope, $http,$cookies) {
    $scope.types = [{name:'公司新闻', type:1}, {name:'首页广告', type:2}, {name:'用户协议', type:3}, {name:'常见问题', type:4}, {name:'关于我们', type:5}];
    $scope.type = $scope.types[0];
    $scope.news = {};
    $scope.news.content = '';
    $scope.news.coverUrl = '';
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    $scope.save = function(){
        $scope.news.type = $scope.type.type;
        if($scope.news.content == ''){
            $scope.pop('error', '', '新闻内容不可为空');
            return;
        }
        //TODO
        if($scope.news.type == '1' && $scope.coverUrl == ''){
            $scope.pop('error', '', '请选择封面');
            return;
        }
        if($scope.news.title == ''){
            $scope.pop('error', '', '标题不可为空');
            return;
        }
        $scope.news.coverUrl = $scope.coverUrl;
        $http.post('/informations/newsPublish', $scope.news).success(function(result){
            if(result.code == Response.successCode){
                $scope.pop('success', '', '发布成功');
                $scope.news = {};
                $scope.news.coverUrl = '';
                $scope.news.content = '';
                $scope.url = null;
                $scope.type = $scope.types[0];
            }else{
                $scope.pop('error', '', result.message);
            }
        });
    };

    $scope.upload = function(){
        var file = new FormData();
        file.append('file', $scope.file);
        $http.post('/file/uploadFile?type=newsImg', file, {
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}
               }).success(function(result){
            if(result.code == Response.successCode){
                $scope.url = result.data.url;
                imgUrl.push($scope.url);
            }else{
                $scope.pop('error', '', result.message);
            }
        });
    };

    $scope.uploadCover = function(){
        var file = new FormData();
        file.append('file', $scope.file);
        $http.post('/file/uploadFile?type=newsImg', file, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function(result){
            if(result.code == Response.successCode){
                $scope.coverUrl = result.data.url;
            }else{
                $scope.pop('error', '', result.message);
            }
        });
    };
}]);