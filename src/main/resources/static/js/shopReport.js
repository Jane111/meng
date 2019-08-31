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

    var myvar1 = setInterval(function(){ test1() },1000)
    var myvar2 = setInterval(function(){ test2() },1000)   
    var myvar3 = setInterval(function(){ test3() },1000)    
   
 });

var sign1 = true
var sign2 = true
var sign3 = true
var content = new Vue({
    el:"#content",
    data:{     
        shopuncheck:[
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            }
        ],
        shoppass:[
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            }
        ],
        shopreject:[
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            },
            {
                name:"黄明昊",
                title:"黄米高的店~",
                address:"湖北省武汉市南湖大道中南财经政法大学",
                area:"299平方米",
                fee:"23447元",                
            }
        ],
        storeList:[],
        goodstype :0,
        goodsresult :0,
    },
    created(){
        this.showAbStoreList(0)
     
    },
    methods:{
     
        //改变选项卡
        changetabs(type){
            this.showAbStoreList(type)
        },
        //显示异常店铺
        showAbStoreList(asStatus){
            this.$http.post(
            requestIP+'/admin/showAbStoreList',{
                asStatus:asStatus,//0未审核 1审核成功(将店铺状态改为异常店铺）2审核失败                             
            },{emulateJSON:true}).then(result=>{            
                if(result.body.resultCode == 10000){
                    sign1 = true
                    this.storeList = result.body.data 
                    for(var i=0;i<this.storeList.length;i++){
                        if(this.storeList[i].sColumn === 1){
                            this.storeList[i].sColumn = '店铺出租'
                        } 
                        else if(this.storeList[i].sColumn === 2){
                            this.storeList[i].sColumn = '生意转让'
                        }
                        else if(this.storeList[i].sColumn === 3){
                            this.storeList.sColumn = '店铺出售'
                        }
                        else if(this.storeList[i].sColumn === 4){
                            this.storeList[i].sColumn = '仓库出租'
                        }  
                        if(this.storeList[i].asType === 0){
                            this.storeList[i].asType = '商品严重不符描述'
                        }
                        else if(this.storeList[i].asType === 1){
                            this.storeList[i].asType = '临时加价'
                        }
                        else if(this.storeList[i].asType === 2){
                            this.storeList[i].asType = '保障期内拒绝服务'
                        }
                    }                       
                }
                else if(result.body.resultCode == 10007){
                    this.storeList = []
                    layer.msg('暂无数据',{time: 1500})                 
                } 
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            })     
        },

        gotoDetails(sId){
            window.location.href = "shopReportDetails.html?sId="+sId;
        },

        //改变商品
        changegoodstype(type){
            this.goodsresult = type
            this.showBadWare()
        },


        //审核举报商品
        agreegoods(result,agiId){
            this.$http.post(
            requestIP+'8003/ware/checkBadWare',{
                agiId:agiId,//异常商品信息ID              
                agiAId:'1',	//处理者  
                agiResult:result, //处理结果    
            },{emulateJSON:true}).then(result=>{            
                if(result.body.resultCode == 10000){
                    this.goodsList = result.body.data        
                } else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            })    
        },
        //21、显示举报店铺
        showstore(){
            this.$http.post(
            requestIP+'8006/store/showBadStore',{           
                amiType:0,	//举报类型 0表示全部  
                amiResult:0, //处理结果  0为未处理，1为已经处理（是否分为同意和非同意）？？？  
            },{emulateJSON:true}).then(result=>{            
                if(result.body.resultCode == 10000){
                    this.storeList = result.body.data     
                    sign2 = true       
                } else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            })     
        },
        //21、显示举报评论
        showcomment(){
            this.$http.post(
            requestIP+'8003/ware/showBadComment',{                               
                aerState:0,	//处理状态 0为未处理，1为已经处理（是否分为同意和非同意）？？？  
                aerType:1, //举报类型  0表示全部 
            },{emulateJSON:true}).then(result=>{            
                if(result.body.resultCode == 10000){                    
                    this.commentList = result.body.data  
                    sign3 = true          
                } else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            })     
        }
    }
})
