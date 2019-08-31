var content = new Vue({
    el:"#content",
    data:{ 
        cutwordList:[]
    },
    created(){
        this.showHint()
    },
    methods:{
        showHint:function(){
            this.$http.post(requestIP+'/admin/showHint',{},{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    this.cutwordList = result.body.data.slice(0,7)                    
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        updatecw:function(id,hcontent){
            console.log(this.cutwordList)
            this.$http.post(requestIP+'/admin/updateHint',
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
