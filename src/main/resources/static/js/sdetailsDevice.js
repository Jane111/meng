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
				dId:"",
				cleanDisplay:false,//二手true新false
				checkDisplay:false,//是否需要审核
				details:{
					dOwnerName:'',//联系人姓名
					dPhone:'',//电话
					dOtherConnect:'',//其他联系方式
					dDcpt:'',//描述
					dPhoto:'',//图片
					dLocation:'',//具体位置
					dName:'',//设备名称
					dType:'',//设备分类
					dClean:'',//是否清洁-0-否1-是
					dDiscuss:'',//价格面议 -0否1是
					dOPrice:'',//原价
					dSPrice:'',//售价
					dPostage:''	//是否包邮-0否1是
				},
				reducetype:0,//拒绝类型
				reducetext:"",//拒绝原因
    },
    created(){
		var getdata = this.GetRequest()
		this.dId = getdata.dId
		if(getdata.clean=="yes"){
			this.cleanDisplay=true
		}
		if(getdata.needAuditFlag==1){
			this.checkDisplay=true
		}
		this.showInfo()
    },
    methods:{
		showInfo(){
			this.$http.post(requestIP+'/admin/getDeviceInfo',
			{
				dId:this.dId
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					console.log(result.body.data)
					var temp=result.body.data
					var dClean=temp.dClean
					var dPostage=temp.dPostage
					if(dClean==0){
						temp.dClean="未清洁"
					}else{
						temp.dClean="已清洁"
					}
					if(dPostage==0){
						temp.dPostage="不包邮"
					}else{
						temp.dPostage="包邮"
					}	
					this.details=temp
				}
			})
		},
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
   
		//审核通过
		agreem(){
			this.$http.post(requestIP+'/admin/passOrNotD',
			{
				dId:this.dId,
				operate:2
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('通过成功',{time: 1500})	
					window.location.href = '../pages/deviceAudit.html';        
				}
			})
		},
		//审核拒绝
		reducem(){
				this.$http.post(requestIP+'/admin/passOrNotD',
				{
					dId:this.dId,
					operate:1
				},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						layer.msg('退回成功',{time: 1500})	
						window.location.href = '../pages/deviceAudit.html'; 										
					}
				})		
		},
    }
})