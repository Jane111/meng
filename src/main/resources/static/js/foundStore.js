var content = new Vue({
    el:"#content",
    data:{ 
        findStoreList:[]
    },
    created(){
        this.showFindStoreInfo(0)
    },
    methods:{
        showFindStoreInfo:function(type){
            this.$http.post(requestIP+'/admin/showFindStoreInfo',
            {
                fdStatus:type //0：未处理 1：已处理
            },{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000 || result.body.resultCode == 10007){
                    if(result.body.data == null){
                        this.findStoreList = []
                    }
                    else{
                        this.findStoreList = result.body.data      
                    }
                                 
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        updateFindStoreInfo:function(id){
            this.$http.post(requestIP+'/admin/updateFindStoreInfo',
            {
                fdId:id
            },{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    layer.msg('成功处理~',{time: 1500})                  
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        deleteFindStoreInfo:function(id){
            this.$http.post(requestIP+'/admin/deleteFindStoreInfo',
            {
                fdId:id
            },{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    layer.msg('删除成功~',{time: 1500})                  
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        changetabs(type){
           this.showFindStoreInfo(type)
        },

        cancel:function(){
            layer.msg('取消成功',{time: 1500})
            this.showHint()
        }

    }
})
