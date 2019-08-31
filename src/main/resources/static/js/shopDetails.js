//------------- gallery.js -------------//
$(document).ready(function() {

	//get object with colros from plugin and store it.
	var objColors = $('body').data('sprFlat').getColors();
	var colours = {
		white: objColors.white,
		dark: objColors.dark,
		red : objColors.red,
		blue: objColors.blue,
		green : objColors.green,
		yellow: objColors.yellow,
		brown: objColors.brown,
		orange : objColors.orange,
		purple : objColors.purple,
		pink : objColors.pink,
		lime : objColors.lime,
		magenta: objColors.magenta,
		teal: objColors.teal,
		textcolor: '#5a5e63',
		gray: objColors.gray
	}

	//------------- MixitUp sorting -------------//
	$('.gallery-inner').mixItUp({
		animation: {
	        effects: 'fade translateZ(500px)',
	        duration: 300
	    },
	}); 

	//------------- Open image -------------//
	$('.gallery-inner').magnificPopup({
	  	delegate: 'a.gallery-image-open', // child items selector, by clicking on it popup will open
	  	type: 'image',
	  	gallery: {
	    	enabled: true
	    }
	});

	//------------- hover direction plugin -------------//
    $(function () {
		$('.mix').hoverDirection();      
		$('.mix .gallery-image-controls ').on('animationend', function (event) {
			var $box = $(this).parent();
			$box.filter('[class*="-leave-"]').hoverDirection('removeClass');
		});
    });
  

	//------------- Sparklines -------------//
	$('#usage-sparkline').sparkline([35,46,24,56,68, 35,46,24,56,68], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});

	$('#cpu-sparkline').sparkline([22,78,43,32,55, 67,83,35,44,56], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});

	$('#ram-sparkline').sparkline([12,24,32,22,15, 17,8,23,17,14], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});

 	
});


var content = new Vue({
    el:"#content",
    data:{     
			sId:"",
			type:1,
            details:{},
            reducetype:0,//拒绝类型
			reducetext:"",//拒绝原因
			map:{
				0:'微信',
				1:'QQ',
				2:'邮箱'
			},
			mapnum: {
				0:'一层', 
				1:'二层', 
				2:'三层', 
				3:'四层及以上'
			},//店铺所有层数
    },
    created(){
		var getdata = this.GetRequest()
		this.sId = getdata.sId
		this.type = getdata.type
        this.showStoreDetail()
    },
    methods:{
		//接受mmid
		GetRequest() {
			var url = location.search; //获取url中"?"符后的字串
			var theRequest = new Object();
			if (url.indexOf("?") != -1) {
				var str = url.substr(1);
				strs = str.split("&");
				for (var i = 0; i < strs.length; i++) {
					theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);
				}
			}
			return theRequest;
        },
        
        //显示店铺想详情
		showStoreDetail(){
			this.$http.post(requestIP+'/admin/showStoreDetail',
			{
				sId:this.sId//店铺id
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
                    this.details = result.body.data  
					this.details.picture = result.body.data.sphoto.split("###") 
					this.details.stag = result.body.data.stag.split("#") 
                    //1“店铺出租” 2“生意转让” 3“店铺出售” 4“仓库出租”
                    if(result.body.data.scolumn === 1){
                        this.details.scolumn = '店铺出租'
                    }
                    else if(result.body.data.scolumn === 2){
                        this.details.scolumn = '生意转让'
                    }
                    else if(result.body.data.scolumn === 3){
                        this.details.scolumn = '店铺出售'
                    }
                    else if(result.body.data.scolumn === 4){
                        this.details.scolumn = '仓库出租'
                    }
                    if(result.body.data.sStoreStatus === 0){
                        this.details.sStoreStatus = '停业'
                    }
                    else if(result.body.data.sStoreStatus === 1){
                        this.details.sStoreStatus = '经营中'
                    }
				}
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
            });
        },
        
        //处理店铺
        checkStore(sStatus){
            this.$http.post(requestIP+'/admin/checkStore',
			{
                sId:this.sId,//店铺id
                sStatus:sStatus//1通过申请 2拒绝入驻
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
                    if(sStatus === 1){
                        layer.msg('已通过',{time: 1500}) 
                        window.location.href = "shopAudit.html";
                    }
                    else if(sStatus === 2){
                        layer.msg('已拒绝',{time: 1500}) 
                        window.location.href = "shopAudit.html";
                    }                  
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
            }).catch(function(){
                    layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			})
        },

   
		//审核通过M
		agreem(){
			this.$http.post(requestIP+'8001/admin/UpdateMStatus',
			{
				mmId:this.mmid//供货商ID
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('通过成功',{time: 1500})	
					window.location.href = '../pages/user-audit.html';        
				}
			})
		},
		//审核拒绝
		reducem(){
			if(this.reducetype == 0){
				layer.msg('请选择退回原因',{time: 1500})
			}
			else if(this.reducetext == ''){
				layer.msg('请填写详细原因',{time: 1500})
			}
			else{
				this.$http.post(requestIP+'8001/admin/UpdateMText',
				{
					mmId:this.mmid,//供货商ID
					mmFialReasonSelect:this.reducetype,//品牌商审核失败原因选择
					mmFailReasonText:this.reducetext,//品牌商审核失败的原因说明
				},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						layer.msg('退回成功',{time: 1500})	
						window.location.href = '../pages/user-audit.html'; 										
					}
				})
			}			
		},
    }
})