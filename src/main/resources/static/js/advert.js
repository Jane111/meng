$(document).ready(function() {    
    function test1(){
        if(sign1){
            $('#datatable').dataTable({
                "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                "fnPreDrawCallback": function( oSettings ) {
                    $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                    $('.dataTables_length select').addClass('form-control input-small');
                },
                "oLanguage": {
                    "sProcessing": "处理中...",
                    "sLengthMenu": "显示 _MENU_ 项结果",
                    "sZeroRecords": "没有匹配结果",
                    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix": "",
                    "sSearch": "",
                    "sUrl": "",
                    "sEmptyTable": "表中数据为空",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands": ",",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上页",
                        "sNext": "下页",
                        "sLast": "末页"
                    },
                },
                "bJQueryUI": false,
                "bAutoWidth": false,
                "bLengthChange":false,
                "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
            });
            clearInterval(myvar1)
        }
    }
    var myvar1 = setInterval(function(){ test1() },1000)
});
var sign1 = false
var content = new Vue ({
    el:'#content',
    data:{
        item:[],
        newitem:[],
        advertList:[],
        astatus:null,
        selectstate:0
    },
    created(){
        sign1 = true ;
        this.showPhotoList()

    },
    methods:{
     
        //传递编辑子账号数据
        getModel:function(item){
            this.item=item
        },
        //更改select
        showPhotoList(){
                //显示管理员列表
            this.$http.post(
                requestIP+'/admin/showPhotoList',{},{emulateJSON:true}).then(result=>{
                    if(result.body.resultCode == 10000){
                        this.advertList = result.body.data                        
                    } else{
                        layer.msg('请求失败',{time: 1500})
                    }
                }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
          
        },

        //新加
        addPhotoList(){
            this.$http.post(
                requestIP+'/admin/addPhotoList',
                {
                    pUrl: this.newitem.pUrl,
                    pToId: this.newitem.pToId,
                    pType: this.newitem.pType,
                },{emulateJSON:true}).then(result=>{
                    if(result.body.resultCode == 10000){
                        layer.msg('保存成功',{time: 1500});
                        this.newitem = [];
                        this.showPhotoList()
                    } else{
                        layer.msg('请求失败',{time: 1500})
                    }
                }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },
        //保存修改
        updatePhotoList(){
            this.$http.post(
                requestIP+'/admin/updatePhotoList',
                {
                    pId: this.item.pId,
                    pUrl: this.item.pUrl,
                    pToId: this.item.pToId,
                    pType: this.item.pType,
                },{emulateJSON:true}).then(result=>{
                    if(result.body.resultCode == 10000){
                        layer.msg('保存成功',{time: 1500});
                        this.newitem = []
                    } else{
                        layer.msg('请求失败',{time: 1500})
                    }
                }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },
       
        //弹框修改状态
        changestateonly(){
            if(this.item.astatus == 1){
                this.astatus = 0
            }
            else{
                this.astatus = 1
            }           
        },

        //删除管理员
        deletePhotoList(pId){
            this.$http.post(
                requestIP+'/admin/deletePhotoList',
                {
                    pId:pId
                },{emulateJSON:true}).then(result=>{
                    if(result.body.resultCode == 10000){                        
                        layer.msg('删除成功',{time: 1500});
                        this.showPhotoList();
                    } else{
                        layer.msg('请求失败',{time: 1500})
                    }
                }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        }

    }

})


