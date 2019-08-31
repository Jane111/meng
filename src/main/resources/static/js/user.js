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
							"sProcessing": "处理中...",
							"sLengthMenu": "显示 _MENU_ 项结果",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索：",
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
//二手商
        Businesstransfer:[
            /*{
                type:'燃气设备、用电设备、包装机械、食品机械',
                addrss:"湖北省武汉市洪山区南湖大道118号",
                title:"中南财大火锅店",
                tel:"15171411165",
				discription:""
            },
           */
        ],
        //新商家
        Shoprental:[
            /*{
                type:'燃气设备、用电设备、包装机械、食品机械',
                addrss:"湖北省武汉市洪山区南湖大道118号",
                title:"中南财大火锅店",
                tel:"15171411165",
                fee:"36781元"
            },
            */
        ],
        //维修商
        Shopsale:[
            /*{
                type:'燃气设备、用电设备、包装机械、食品机械',
                addrss:"湖北省武汉市洪山区南湖大道118号",
                title:"中南财大火锅店",
                tel:"15171411165",
                fee:"36781元"
            },
           */
        ]
    },
    created(){
		this.showAllList()
    },
    methods:{
		showAllList()
		{
			//二手设备
			this.$http.post(requestIP+'/admin/allDB',{
				dbType:2
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.Businesstransfer = result.body.data
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
		},
        //切换选项卡
        changetab(type){
            //发布不同请求
			if(type==1){
			//二手设备列表
			this.$http.post(requestIP+'/admin/allDB',{
				dbType:2
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.Businesstransfer = result.body.data
						console.log(this.Businesstransfer)
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
			}
			else if(type==2){
			//新设备列表
			this.$http.post(requestIP+'/admin/allDB',{
				dbType:1
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.Shoprental = result.body.data
						console.log(this.Shoprental)
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
			}
			else if(type=='3'){
			//维修设备列表
			this.$http.post(requestIP+'/admin/allDB',{
				dbType:'3'
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.Shopsale = result.body.data
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
					
				});
			}
        },
        ////跳转到二手商详情页
        hreftodetails0(dbId){            
            window.location.href = "sdetailsProvider.html?verify=no&dbId="+dbId+"&merchant="+1;
        },
		////跳转到新商详情页
        hreftodetails1(dbId){            
            window.location.href = "sdetailsProvider.html?verify=no&dbId="+dbId+"&merchant="+2;
        },
		//维修商详情页
         hreftodetails2(dbId){            
            window.location.href = "sdetailsProvider.html?verify=no&dbId="+dbId+"&merchant="+3;
        },
    }
})

