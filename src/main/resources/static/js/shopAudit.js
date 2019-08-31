$(document).ready(function() {    
    function test1(){
        if(sign1){
              //------------- Datatables -------------//
                    $('#datatable').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                        "oLanguage": {
                            "sSearch": ""
                        },
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar1)
        }
    }

    function test2(){
        if(sign2){
              //------------- Datatables -------------//
                    $('#datatable1').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                        "oLanguage": {
                            "sSearch": ""
                        },
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar2)
        }
    }

    function test3(){
        if(sign3){
              //------------- Datatables -------------//
                    $('#datatable2').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                        "oLanguage": {
                            "sSearch": ""
                        },
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar3)
        }
    }

    function test4(){
        if(sign4){
              //------------- Datatables -------------//
                    $('#datatable3').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                        "oLanguage": {
                            "sSearch": ""
                        },
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar4)
        }
    } 
    var myvar1 = setInterval(function(){ test1() },1000)
    var myvar2 = setInterval(function(){ test2() },1000)   
    var myvar3 = setInterval(function(){ test3() },1000)
    var myvar4 = setInterval(function(){ test4() },1000)
 });
 var sign1 = false
 var sign2 = false
 var sign3 = false
 var sign4 = false
var content = new Vue({
    el:"#content",
    data:{ 
        storeList:[],
        type:1,//是否审核
    },
    created(){
        this.showStoreList(2,0)      
    },
    methods:{
        //切换选项卡
        changetab(sColumn,sStatus){
            //发布不同请求
            if(sStatus == 0){
                this.type = 1
            }
            else{
                this.type = 2
            }
            this.showStoreList(sColumn,sStatus)            
        },

        //显示店铺列表
        showStoreList:function(sColumn,sStatus){
            this.$http.post(requestIP+'/admin/showStoreList',
            {
                sColumn:sColumn,//店铺所属栏目  int	1“店铺出租” 2“生意转让” 3“店铺出售” 4“仓库出租”
                sStatus:sStatus//0未审核,1通过申请,2拒绝入驻,3异常店铺(被举报)
            },{emulateJSON:true}).then(result=>{				
                if(result.body.resultCode == 10000){
                    if(sColumn === 2)
                    {
                        sign1 = true
                    }
                    else if(sColumn === 1){
                        sign2 = true
                    }
                    else if(sColumn === 3){
                        sign3 = true
                    }
                    else if(sColumn === 4){
                        sign4 = true
                    }                    
                    this.storeList = result.body.data                    
                }
                else if(result.body.resultCode == 10007){
                    if(sColumn === 2)
                    {
                        sign1 = true
                    }
                    else if(sColumn === 1){
                        sign2 = true
                    }
                    else if(sColumn === 3){
                        sign3 = true
                    }
                    else if(sColumn === 4){
                        sign4 = true
                    }
                    this.storeList = []
                    layer.msg('暂无数据',{time: 1500})
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },

        //跳转到详情页
        hreftodetails(sId){            
            window.location.href = "shopDetails.html?sId="+sId+'&type='+this.type;
        },       
    }
})
