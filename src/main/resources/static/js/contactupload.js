var content = new Vue({
    el:"#content",
    data:{ 
        ContractModelList:[]     
    },
    created(){
        this.showContractModelList()      
    },
    methods:{
        showContractModelList:function(){
            this.$http.post(requestIP+'/admin/getContractModelList',{},{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    this.ContractModelList = result.body.data                 
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        updatecm:function(id,hcontent){
            this.$http.post(requestIP+'/admin/deleteContractModel',
            {
                hId:id,
                hContent:hcontent
            },{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    layer.msg('保存成功~',{time: 1500})                  
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        cancel:function(){
            layer.msg('取消成功',{time: 1500})
            this.showHint()
        }
       
    }
})
