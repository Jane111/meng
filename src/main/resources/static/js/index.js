var content = new Vue({
    el:"#content",
    data:{
        num:{}       
    },
    created(){
        this.showindex()    
    },
    methods:{
        showindex(){
            this.$http.post(requestIP+'/admin/getNumOfStore',{},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					this.num = result.body.data
				}
				else{
					layer.msg('请求失败',{time: 1500})
				}
			}).catch(function(){
                layer.msg('服务器异常',{time: 1500})
        });
        },
     
    }
})