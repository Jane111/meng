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
    var myvar1 = setInterval(function(){ test1() },1000)
});
var sign1 = false
var content = new Vue({
    el: "#content",
    data: {
        contracList:[],
        check:1
    },
    created() {
        this.showUncheckedContractList()
    },
    methods: {
        showUncheckedContractList(){
            this.check = 1
			this.$http.post(requestIP+'/admin/showUncheckedContractList',
			{
				
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
                    sign1 = true
                    this.contracList = result.body.data.map(item => {
                        item.ucContractUrl = item.ucContractUrl.split("###"),
                        item.ucIdUrl = item.ucIdUrl.split("###").reverse().splice(-0,item.ucContractUrl.length + 1)

                    }),
                    this.contracList = result.body.data  
                    console.log(this.contracList)     
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
			})
        },
        showCheckedContractList(){
            this.check = 2
			this.$http.post(requestIP+'/admin/showCheckedContractList',
			{
				
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
                    this.contracList = result.body.data == null?  null : result.body.data.map(item => {
                        item.ucContractUrl = item.ucContractUrl.split("###"),
                        item.ucIdUrl = item.ucIdUrl.split("###").reverse().splice(-0,item.ucContractUrl.length + 1)

                    }),
                    this.contracList = result.body.data  
                    console.log(this.contracList)     
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
			})
        },
        checkContract(ucId,type){
			this.$http.post(requestIP+'/admin/checkContract',
			{
                ucStatus:type,
                ucId:ucId				
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
                    layer.msg('操作成功',{time: 1500})
                   this.showUncheckedContractList() 
                }
                else{
                    layer.msg('请求失败',{time: 1500})
                }
			})
        },
        
        

        

    }
})