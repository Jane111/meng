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
				rdId:"",
				checkDisplay:false,//是否需要审核 默认不需要
				statusDisplay:true,//是否有审核状态 有审核状态
				details:{
					name:'黄明昊',
					connect:'zxyue25',
					title:"火锅",
					type:"商品严重不符描述、临时加价、保障期内拒绝服务",
					describe:"店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好店铺描述店铺描述我们这个店非常好",
					picture:[
							'../images/131.jpg',	'../images/131.jpg'
					],
					pictureone:'../images/131.jpg'
				},
				reducetype:0,//拒绝类型
				reducetext:"",//拒绝原因
    },
    created(){
		var getdata = this.GetRequest()
		this.rdId = getdata.rdId
		if(getdata.needCheckFlag==1){//需要审核
			this.checkDisplay=true
			this.statusDisplay=false
		}
		this.showInfo()
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
		showInfo(){
			var temp=[]
			if(this.checkDisplay==false){//是已审核举报设备详情
				this.$http.post(requestIP+'/admin/checkedDeviceInfo',
				{
					rdId:this.rdId//举报ID
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							temp = result.body.data
							if(temp.rdStatus==1){
								temp.rdStatus="未通过"
							}
							else{
								temp.rdStatus="通过"
							}
							if(temp.rdOption==0){
								temp.rdOption="设备严重不符合描述"
							}
							else if(temp.rdOption==1){
								temp.rdOption="临时加价"
							}else{
								temp.rdOption="保证期内拒绝服务"
							}
							this.details=temp;
							console.log(result)
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}else{//是待审核举报设备详情
					this.$http.post(requestIP+'/admin/checkedReportDeviceInfo',
				{
					rdId:this.rdId//举报ID
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							temp = result.body.data
							if(temp.rdOption==0){
								temp.rdOption="设备严重不符合描述"
							}
							else if(temp.rdOption==1){
								temp.rdOption="临时加价"
							}else{
								temp.rdOption="保证期内拒绝服务"
							}
							this.details=temp;
							console.log(result)
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}
		},		
		//审核通过M
		agreem(){
			this.$http.post(requestIP+'/admin/passOrNotRD',
			{
				rdId:this.rdId,
				operate:2//2通过1拒绝
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('通过成功',{time: 1500})	
					window.location.href = '../pages/deviceReport.html';        
				}
				else{
					layer.msg("通过失败",{time:1500})
				}
			})
		},
		//审核拒绝
		reducem(){
				this.$http.post(requestIP+'/admin/passOrNotRD',
			{
				rdId:this.rdId,
				operate:1
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('拒绝成功',{time: 1500})	
					window.location.href = '../pages/deviceReport.html';        
				}else{
					layer.msg("拒绝失败",{time:1500})
				}
			})
		},
    }
})