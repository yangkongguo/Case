//修改titl IOS/Android通用版
function changeTilte(title){
	var $body = $('body');
	document.title =title;

	var $iframe = $('<iframe src="/favicon.ico"></iframe>');
	$iframe.on('load',function() {
		setTimeout(function() {
			$iframe.off('load').remove();
		}, 0);
	}).appendTo($body);
}
//毫秒转成字符串格式
function getDate(mills){
	var date = new Date(mills);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minu = date.getMinutes();
	date = year + "-" + (month<10?"0"+month:month) + "-" + (day<10?"0"+day:day) + " "+ (hour<10?"0"+hour:hour) + ":" + (minu<10?"0"+minu:minu)+":"+"00";
	return date;
}
//dateTime-local转字符串
function getDateVal(mills){
	var date = new Date(mills);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minu = date.getMinutes();
	date = year + "-" + (month<10?"0"+month:month) + "-" + (day<10?"0"+day:day) + "T"+ (hour<10?"0"+hour:hour) + ":" + (minu<10?"0"+minu:minu)+":"+"00";

	return date;
}
//解析成时间戳
function parseToTimes(strValue){
	var str=strValue.replace("T"," ");
	var times=new Date(str.replace(/-/g, "/")).getTime();
	return times;
};
//图片水印
function getImg(text,text1,text2,fsz,dataUrl){
	var c = document.getElementById("myCanvas");
	var cxt = c.getContext("2d");
	var img = new Image();
	img.src = dataUrl;
	img.onload=function(){//图片加载完成，才可处理
		//cxt.translate(c.width/2,c.height/2);
		/*
				   var x = c.width/2; //画布宽度的一半
		           var y = c.height/2;//画布高度的一半
		           cxt.clearRect(0,0, c.width, c.height);//先清掉画布上的内容
		           cxt.translate(x,y);//将绘图原点移到画布中点
		           cxt.rotate(Math.PI/2);//旋转角度
		           cxt.translate(-x,-y);//将画布原点移动
		 */
		cxt.drawImage(img,0,0,300,200);
		cxt.font = fsz+"px microsoft yahei";
		cxt.textBaseline = 'middle';//更改字号后，必须重置对齐方式，否则居中麻烦。设置文本的垂直对齐方式
		cxt.textAlign = 'center';
		var tw = cxt.measureText(text).width;
		var ftop = c.height-25;
		var fleft = c.width-30;
		alert(ftop+":"+fleft);
		// 	cxt.fillStyle="#ff0000";
		//		var hig=fsz*3+5;
		// 	cxt.fillRect(fleft-tw/2,ftop-fsz/2,tw,hig);//矩形在画布居中方式
		cxt.fillStyle="#ffffff";
		cxt.fillText(text,fleft,ftop);//文本元素在画布居中方式
		var ftop2=ftop+fsz;
		var fleft1= c.width/2;
		cxt.fillStyle="#fff";
		cxt.fillText(text1,fleft1,ftop2);
		var ftop3=ftop2+fsz;
		var fleft2= fleft-20;
		cxt.fillStyle="rgba(255,255,255,1)";
		cxt.fillText(text2,fleft2,ftop3);
		cxt.save();
		cxt.fill();
	}

}
//未完成工单
function workTaskUndo(myApp,userName,userid){
	$("#engineerWorkTask").siblings().addClass("hide");
	$("#engineerWorkTask").removeClass("hide");
	$("#searchWorkNameDiv").addClass("hide");
	$("#searchSponsorNameDiv").removeClass("hide");
	$("#myCanvas").show();
	changeTilte("我的未完成工单");
	myApp.showToolbar($(".toolbar"));
	//获取到用户的名字
	var engineerName=userName;
	//工单统计数请求
	$.ajax({
		url : base_path+ "/workTask/findNumberByTheMon.do",
		data : {"engineerId":userid},
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				var year=result.data.year;
				var mon=result.data.mon;
				var num=result.data.num;
				$("#yearNum").html(year);
				$("#monNum").html(mon);
				$("#undoNum").html(num);
			}else{
				myApp.alert(result.msg,'通威');
			}
		},
		error:function(){
			myApp.alert("查看工单数异常！",'通威');	
		}
	})
	//排名请求
	$.ajax({
		url : base_path+ "/workTask/findPaiMingByEID.do",
		data : {"engineerId":userid},
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				var level=result.data;
				$("#paiNum").text(level);
			}else{
				//alert(result.msg);
				$("#paiNum").text("--");
				$("#paiNum").addClass("fight");
			}
		},
		error:function(){
			myApp.alert("查看工单数异常！",'通威');	
		}
	})
	$.ajax({
		url : base_path+ "/workTask/workTaskEngineerUnFinsh.do",
		data : {"engineerId":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				$("#engineerWorkTask_Body").html("");
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=data[i].workTask_start_time;
					start_time=getDate(start_time);
					var finsh_time=data[i].workTask_finsh_time;
					finsh_time=getDate(finsh_time);
					var type=data[i].workTask_type;
					var　typeText=type==1?"上门售前":type==2?"方案标书":type==3?"其它售前":type==4?"安装实施":type==5?"售后":type==6?"远程":"学习培训";
					str+='<div class="card">';
					str+='<div class="card-header" >';
					str+='	<a style="color:#333" data-toggle="collapse" data-parent="#accordion"';
					str+='	href="#'+data[i].workTask_id+'">名称：'+data[i].workTask_name+"   ";
					str+='<i class="fa fa-caret-down"></i>'
						str+='</a>';
					str+='<a style="color:#8B3626" href="#" class="fa fa-edit color-deeporange open-login-screen">'
						str+='</a>';
					str+='<a style="color:#8B3626" href="#" class="fa fa-close btn-delete">'
						str+='</a>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].workTask_id+'" class="panel-collapse collapse">';
					str+='        <div class="panel-body">';
					str+='		<p>工单要请求人 :'+data[i].workTask_sponsor_name+'</p>';
					str+='		<p>客户名称:'+data[i].workTask_guest_name+'</p>'
					str+='		<p>工单内容:'+data[i].workTask_desc+'</p>';
					str+='		<p>工单地点:'+data[i].workTask_map+'</p>';
					str+='		<p>工单类型:'+typeText+'</p>';
					str+='		<p>工单要求开始时间 :'+start_time+'</p>';
					str+='		<p>工单要求结束时间 :'+finsh_time+'</p>';
					str+='		电话:<a class="tel" href="">'+data[i].workTask_guest_tel+'</a>'	
					str+='		<p>工单备注 :'+data[i].workTask_engineer_remark+'</p>';
					str+='		<p class="real_start_time hide">工单实际开始时间 :</p>';
					str+='		<p class="real_finsh_time hide">工单实际结束时间 :</p>';
					if(data[i].workTask_status==6){
						str+=' 		<p><a style="color:#8B3626" href="#" class="button button-fill color-gray completeTask">申请关闭该Case</a></p>'
					}else{
						str+='<a  class="files-class fa fa-camera">拍照'
							str+='<input class="input-file" value="拍照" type="file" accept="image/x-photo-cd" capture="camera">'
								str+='</a>'		
					}

					//		str+='<canvas  style="border:1px solid #000000; height: 100%;width: 500px;margin: 0;padding: 0;display: block;""></canvas>';
					//		str+='<img src="" style="display:none;width:100%;height:500px;">'
					str+='		</div>';
					str+='		</div>';
					str+='	</div>';
					str+='</div>';
					str+='<div class="card-footer" id="'+data[i].workTask_id+'1" >';
					str+='执行人：'+data[i].workTask_engineer_name;
					str+='</div>';
					str+='</div>';
					var $str=$(str).data("workTask_id",data[i].workTask_id);
					$("#engineerWorkTask_Body").append($str);
				}
			}else{
				$("#engineerWorkTask_Body").html('<span class="fa-stack fa-lg" style="width:100%;height:100%;margin: 0 auto;vertical-align: middle;font-size:40px;">'+
						'<i class="fa fa-newspaper-o fa-stack-1x"></i>'+
						'<i class="fa fa-ban fa-stack-2x "></i>'+
						'<br>'+
						'</span>'+'');
				//myApp.alert(result.msg,"通威");								
			}
		},error:function(){
			myApp.alert("警告，搜索指定工程师异常","通威");
		}
	})
	//发送签到的显示请求
	$.ajax({
		url : base_path+ "/signin/signinTableList.do",
		data : {"engineerId":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var str="";
					str+="<p class='buttons-row'>"
						if(data[i].signin_status==0){
							str+='<a href="#" class="button button-fill color-green"  style="background-color: #10D49E;">到</a>';
							//str+='	<a href="#" class="button button-round button-fill color-red"  >退</a>';
						}else if(data[i].signin_status==1){
							//str+='<a href="#" class="button button-round button-fill color-green hide">到</a>';
							str+='<a href="#" class="button  button-fill color-red" >退</a>';
						}else if(data[i].signin_status==2){
							//str+='	<a href="#" class="button button-round button-fill color-red hide">退</a>';
						}
					str+='<p>'
						var $str=$(str).data("signin_id",data[i].signin_id);
					$str.data("status",data[i].signin_status);
					var select="#"+data[i].workTask_id+"1";
					$(select).append($str);
					//实际开始时间
					var startTime=data[i].signin_start_time;
					startTime=getDate(startTime);
					if(startTime=="1970-01-01 08:00:00"||!startTime){
						startTime="暂无";
					}
					if(startTime.length>16){
						startTime=startTime.substr(0,16) ;
					}
					$(select).prev().find(".real_start_time").text("签到时间: "+startTime);
					$(select).prev().find(".real_start_time").removeClass("hide");
					//实际结束时间
					var finshTime=data[i].signin_finsh_time;
					finshTime=getDate(finshTime);
					if(finshTime=="1970-01-01 08:00:00"||!finshTime){
						finshTime="暂无";
					}
					$(select).prev().find(".real_finsh_time").text("签退时间:"+finshTime);
					$(select).prev().find(".real_finsh_time").removeClass("hide");
				}
			}else{
				//myApp.alert(result.msg,"通威");
			}
		},error:function(){
			myApp.alert("我的工单数据异常","通威")
		}
	})

	//myApp.alert('显示工程师当前任务工单和确认是否完成的功能','通威');
}
//所有已派发请求
function alreadyReq(myApp){
	$("#alreadyReq_body").html("");
	//发送查看所有请求的数据
	$.ajax({
		url : base_path+ "/request/requestListByAlreadySend.do",
		data : {},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				var data = result.data;
				for (var i = 0; i < data.length; i++) {
					var start_time=data[i].request_start_time;
					start_time=getDateVal(start_time);
					var finsh_time=data[i].request_finsh_time;
					finsh_time=getDateVal(finsh_time);
					//var type=data[i].request_type;
					//var　typeText=type==1?"销售工单":type==2?"上门售前":type=="3"?"其它售前":type=='4'?"学习培训":type==5?"售后":"远程工单";
					var str = "";
					str+='<div class="card">';
					str+='<div class="card-header" >';
					str+='<a data-toggle="collapse" data-parent="#accordion" ';
					str+='href="#'+data[i].request_id+i+'">名称:'+data[i].request_name+"";
					str+='<i class="fa fa-caret-down"></i>'
					str+='</a>';
					//str+='<i>申请人：'+data[i].request_sponsor_name+'</i>';
					//str+='<i>时间：'+start_time+'</i>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='&nbsp;&nbsp;&nbsp;<a>申请人：'+data[i].request_sponsor_name+'</a><br>&nbsp;&nbsp;&nbsp;<a>'+start_time+'</a>'
					str+='<div class="card-content-inner">';
					str+='	 <div id="'+data[i].request_id+i+'" class="panel-collapse collapse">';
					str+='        <div class="panel-body">';
					str+='        	<div class="list-block">';
					str+='				<ul>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-envelope"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<div class="item-input">';
					str+='									任务内容:<textarea placeholder="任务内容"  style="font-family: 楷体;vertical-align: middle;">'+data[i].request_desc+'</textarea>';
					str+='								</div>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='<li>';
					str+='<div class="item-content">';
					str+='	<div class="item-media">';
					str+='		<i class="fa fa-bookmark"></i>';
					str+='	</div>';
					str+='	<div class="item-inner">';
					str+='		<div class="item-input">';
					str+='			<select>';
					str+='	<option value="1">上门售前</option>';
					str+='	<option value="2">方案标书</option>';
					str+='	<option value="3">其它售前</option>';
					str+='	<option value="4">安装实施</option>';
					str+='	<option value="5">售后</option>';
					str+='	<option value="6">远程</option>';
					str+='	<option value="7">学习培训</option>';
					str+='			</select>';
					str+='		</div>';
					str+='	</div>';
					str+='</div>';
					str+='</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-map-marker" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								地点：<a href="">'+data[i].request_map+'</a>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-user-o" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								派工申请人：<a href="">'+data[i].request_sponsor_name+'</a>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='					 <a href="#" class="item-link smart-select" data-page-title=" " data-back-text="确认" data-back-icon=" ">';
					str+='					 	<select class="engineersPerson" style="font-family: 楷体;font-weight: bold;" multiple="multiple">';
					str+='						</select>';
					str+='					<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-male" data-toggle="tooltip" title="任务工程师"></i>';
					str+='							</div>';
					str+='						<div class="item-inner">';
					str+='								 <div class="item-title">执行工程师：</div>';
					str+='								 <div class="item-after"></div>';
					str+='						</div>';
					str+='					</div>';
					str+='					</a>';
					str+='				</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-calendar-o" data-toggle="tooltip"';
					str+='									></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<a>开始时间：</a><input type="datetime-local" value="'+start_time+'" class="start_time">';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-calendar" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<a>结束时间：</a>';
					str+='								<input type="datetime-local" value="'+finsh_time+'"class="finsh_time">';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='				</ul>';
					str+='			</div>';
					str+='        </div>';
					str+='       </div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer ">';
					str+='	<a href="#" class="button button-round button-fill color-green">确认</a>';
					//str+='	<a href="#" class="button button-round button-fill color-red">忽略</a>';
					str+='</div>';
					str+='</div>';
					var $str=$(str).data("request_id",data[i].request_id);
					$str.find("select").val(data[i].request_type);
					$("#alreadyReq_body").append($str);
				}
			} else {
				myApp.alert("接收人载入失败","通威");
			}
				datePick(".myDatePicker",1);
		},
		error : function() {
			myApp.alert("异常！！警告！","通威");
		}
	});
	//工程师数据
	$.ajax({
		url : base_path+ "/table/dingdingTest-deptList.do",
		data : {},
		dataType : "json",
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				var data = result.data;
				$(".engineersPerson").html(" ");
				for (var i = 0; i < data.length; i++) {
					var str = "";
					str += "<option value='"+data[i].name+","+data[i].userid+"'>";
					str += data[i].name;
					str += "</option>";
					$(".engineersPerson").append(str);
				}
			} else {
				myApp.alert("接收人载入失败","通威");
			}
		},
		error : function() {
			myApp.alert("异常！！警告！","通威");
		}
	});
}
//管理员的确认派工按钮
function mangerConfirmBtn($this,myApp){
	myApp.modal({
	    title:  '通威',
	    text: '确认派工?',
	    buttons: [{
	        text: '确认',
	        onClick: function() {
	        	//获取工程师的名字数组
	        	var engineers=$this.parent().prev().find("select:eq(1)").val();
	        	var ok=true;
	        	if(!engineers||engineers.length<=0){
	        		ok=false;
	        		myApp.alert("还未选定工程师，至少需要有一位😃","通威");
	        		return ;
	        	}
	        	//获取工单类型
	        	var type=$this.parent().prev().find("select:eq(0)").val();
	        	//获取开始结束时间
	        	var start_time=$this.parent().prev().find(".start_time").val();
	        	var startTimes=parseToTimes(start_time);
	        	var finsh_time=$this.parent().prev().find(".finsh_time").val();
	        	var finshTimes=parseToTimes(finsh_time);
	        	//发送Ding的text
				var　typeText=type==1?"上门售前":type==2?"方案标书":type==3?"其它售前":type==4?"安装实施":type==5?"售后":type==6?"远程":"学习培训";
	        	var txt="工单类型:"+typeText+";"+$this.parent().prev().prev().find("a").text();
	        	if(finshTimes<startTimes){
	        		alert("结束时间小于开始时间!");
	        		ok=false;
	        		return;
	        	}
	        	if(ok){
	        		myApp.showPreloader();
	        		var engineersStr="";
	        		for(var i=0;i<engineers.length;i++){
	        			engineersStr+=engineers[i]+";";
	        		}
	        		//获取到请求ID
					var　request_id=$this.parent().parent().data("request_id");
					$.ajax({
						url : base_path+ "/request/requestConfirm.do",
						data : {"request_id":request_id,"engineers":engineersStr,"type":type,"startTimes":startTimes,"finshTimes":finshTimes},
						dataType : "json",
						type : "post",
						traditional: true,
						success : function(result) {
							$this.show();
							myApp.hidePreloader();
							if(result.status==0){
								$this.parent().parent().remove();
					        	var toast = myApp.toast('派工成功', '', {});
					        	toast.show();
					        	var date=new Date();
								dd.biz.ding.post({
								    users : result.data,//用户列表，工号
								    corpId: 'dingdda4cfb59500c8f635c2f4657eb6378f', //企业id
								    type: 2, //钉类型 1：image  2：link
								    alertType: 2,
								    alertDate: {"format":"yyyy-MM-dd HH:mm","value":date},
								    attachment: {
								        title: '',
								        url: '',
								        image: '',
								        text: ''
								    },
								    text: txt+'!🐱🚀', //消息
								    onSuccess : function() {
								    	myApp.alert("Ding成功","通威");
								    },
								    onFail : function(error) {
								    	myApp.alert("Ding失败"+JSON.stringify(error),"通威");
								    }
								})

							}else{
								myApp.alert(result.msg);
							}
						},error:function(){
							myApp.alert("数据异常","通威");
						}
					})
	        	}
	        }
	      },
	      {
	        text: '取消',
	        onClick: function() {
	        	alert("取消");
	        }
	        }],
	  })
}
//关闭按钮
function findCloseTask(name,myApp){
	 changeTilte("我的完成请求");
	$.ajax({
		url:base_path+"/request/findCloseTask.do",
		data:{"userName":name},
		type:"post",
		async:false,
		dataType:"json",
		success:function(result){
			if(result.status==0){
				var data=result.data;
				$("#salePrv_body").html("");
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=getDate(data[i].request_real_startTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_startTime);
					var finsh_time=getDate(data[i].request_real_finshTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_finshTime);
					str+='<div class="card">';
					str+='<div class="card-header" href="#'+data[i].request_id+'i" data-toggle="collapse" data-parent="#accordion">';
					str+='	<a>';
					str+='名称：'+data[i].request_name;
					str+='</a>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].request_id+'i" class="panel-collapse collapse">';
					str+='       <div class="panel-body">';
					str+='	<p>工单内容:'+data[i].request_desc+'</p>';
					str+='	<p>工单地点:'+data[i].request_map+'</p>';
					str+='	<p>工单执行人:'+data[i].request_excute_engineerName+'</p>';
					str+='	<p class="">工单签到时间: '+start_time+'</p>';
					str+='	<p class="">工单签退时间:'+finsh_time+'</p>';
					str+='	</div>';
					str+='	</div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer">';
					str+='工单应该执行时间：'+getDate(data[i].request_start_time);
					str+='</div>';
					str+='</div>';
					$("#salePrv_body").append(str);
				}
			}else{
				myApp.alert(result.msg,'通威');
			}
		},
		error:function(){
			myApp.alert("查询请求异常。",'通威');
		}
	})
}
//同意删除Case申请
function agreeDeleteCase($this,myApp){
	myApp.confirm("确认同意删除该工单？","通威",function(){
		var workTask_id=$this.parent().parent().data("workTask_id");
		if(workTask_id){
			myApp.showIndicator();
			$.ajax({
				url:base_path+"/workTask/DeleteWorkTaskByWID.do",
				data:{"workTask_id":workTask_id},
				dataType:"json",
				type:"post",
				success:function(result){
					myApp.hideIndicator();
					$this.parent().parent().remove();
					myApp.alert(result.msg,"通威");
				},error:function(){
					myApp.hideIndicator();						
					myApp.alert("警告，同意删除该工单异常","通威");
				}
			})
			}
		},function(){
			return;
		});
}
//同意关闭该Case
function agreeCloseCase($this,myApp){
	myApp.confirm("确认同意关闭该Case？","通威",function(){
	var workTask_id=$this.parent().parent().data("workTask_id");
	if(workTask_id){
		myApp.showIndicator();
		$.ajax({
			url:base_path+"/workTask/CompleteWorkTaskByWID.do",
			data:{"workTask_id":workTask_id},
			dataType:"json",
			type:"post",
			success:function(result){
				myApp.hideIndicator();
				$this.parent().parent().remove();
				myApp.alert(result.msg,"通威");
			},error:function(){
				myApp.hideIndicator();						
				myApp.alert("警告，关闭该工单异常","通威");
			}
		})
		}
	},function(){
		return;
	});
}
//拒绝删除该工单
function refuseDeleteWorkTask($this,myApp){
	myApp.confirm("确认拒绝删除该工单？","通威",function(){
	var workTask_id=$this.parent().parent().data("workTask_id");
	if(workTask_id){
		myApp.showIndicator();
		$.ajax({
			url:base_path+"/workTask/rejectDeleteWorkTaskByWID.do",
			data:{"workTask_id":workTask_id},
			dataType:"json",
			type:"post",
			success:function(result){
				myApp.hideIndicator();
				$this.parent().parent().remove();
				myApp.alert(result.msg,"通威");
			},error:function(){
				myApp.hideIndicator();						
				myApp.alert("警告，拒绝删除该工单异常","通威");
			}
		})
		}
	},function(){
		return;
	});
}
//拒绝关闭工单
function refuseCloseWorkTask($this,myApp){
	myApp.confirm("确认拒绝关闭该Case？","通威",function(){
	var workTask_id=$this.parent().parent().data("workTask_id");
	if(workTask_id){
		myApp.showIndicator();
		$.ajax({
			url:base_path+"/workTask/rejectCompleteWorkTaskByWID.do",
			data:{"workTask_id":workTask_id},
			dataType:"json",
			type:"post",
			success:function(result){
				myApp.hideIndicator();
				$this.parent().parent().remove();
				myApp.alert(result.msg,"通威");
			},error:function(){
				myApp.hideIndicator();						
				myApp.alert("警告，拒绝关闭该工单异常","通威");
			}
		})
		}
	},function(){
		return;
	});
}
//全部Casea按钮
function allCaseFindByUser(name,myApp){
	$("#salePrv").siblings().addClass("hide");
	$("#salePrv").removeClass("hide");
	 changeTilte("我的请求");
	$.ajax({
		url:base_path+"/request/findAllReqBySponsorName.do",
		data:{"userName":name},
		type:"post",
		async:false,
		dataType:"json",
		success:function(result){
			$("#salePrv_body").html(" ");
			if(result.status==0){
				var data=result.data;
				$("#salePrv_body").html(" ");
				for(var i=0;i<data.length;i++){
					var requestStatus=data[i].request_status;
					requestStatus=requestStatus==0?"未处理":requestStatus==1?"草稿箱":requestStatus==2?"已派工":requestStatus==3?"已被忽略":requestStatus==4?"执行中":requestStatus==5?"已关闭":"其它";
					var start_time=data[i].request_start_time;
					start_time=getDate(start_time);
					var finsh_time=data[i].request_finsh_time;
					finsh_time=getDate(finsh_time);
					var str="";
					var start_time=getDate(data[i].request_start_time);
					str+='<div class="card">';
					str+='<div class="card-header" href="#'+data[i].request_id+'i" data-toggle="collapse" data-parent="#accordion">';
					str+='	<a>';
					str+='名称：'+data[i].request_name;
					str+='</a>';
					str+='<a>'+requestStatus+'</a>'
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].request_id+'i" class="panel-collapse collapse">';
					str+='       <div class="panel-body">';
					str+='	<p>工单内容:'+data[i].request_desc+'</p>';
					str+='	<p>工单地点:'+data[i].request_map+'</p>';
					str+='	<p>客户单位:'+data[i].request_guest_company+'</p>';
					str+='	<p>请求开始时间:'+start_time+'</p>';
					str+='	<p>请求结束时间:'+finsh_time+'</p>';
					str+='	</div>';
					str+='	</div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer">';
					str+='工单应该执行时间：'+start_time;
					str+='</div>';
					str+='</div>';
					$("#salePrv_body").append(str);
				}
				//设置全部的数量
				$("#allReqNum").text(data.length);
			}else{
				myApp.alert(result.msg,"通威");
			}
		},
		error:function(){
			alert("查询请求异常。");
		}
	})
	$.ajax({
	url : base_path+ "/signin/signinTableListAllLT2.do",
	data : {},
	async:false,
	dataType : "json",
	type : "post",
	success : function(result) {
		if(result.status==0){
			var data=result.data;
			for(var i=0;i<data.length;i++){
				var select="#"+data[i].workTask_id+"i";
				//实际实际开始时间
				var startTime=data[i].signin_start_time;
				if(!startTime||startTime=="1970-1-1 08:00"){
					startTime="暂无";
				}
				if(startTime.length>16){
					startTime=startTime.substr(0,16) ;
				}
				$(select).find(".real_start_time").text("签到时间: "+startTime);
				$(select).find(".real_start_time").removeClass("hide");
				//实际结束时间
				var finshTime=data[i].signin_finsh_time;
				finshTime=getDate(finshTime);
				if(!startTime||finshTime=="1970-01-01 08:00:00"){
					finshTime="暂无";
				}
				$(select).find(".real_finsh_time").text("签退时间:"+finshTime);
				$(select).find(".real_finsh_time").removeClass("hide");
			}
		}else{
			myApp.alert(result.msg,"通威");
		}
	},error:function(){
		myApp.alert("我的工单数据异常","通威")
	}
})
}
//执行按钮
function findExcuteTask(name,myApp){
	 changeTilte("我的派工执行");
	$.ajax({
		url:base_path+"/request/findExcute.do",
		data:{"userName":name},
		type:"post",
		async:false,
		dataType:"json",
		success:function(result){
			if(result.status==0){
				var data=result.data;
				$("#salePrv_body").html("");
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=getDate(data[i].request_real_startTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_startTime);
					var finsh_time=getDate(data[i].request_real_finshTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_finshTime);
					str+='<div class="card">';
					str+='<div class="card-header" href="#'+data[i].request_id+'i" data-toggle="collapse" data-parent="#accordion">';
					str+='	<p><a>';
					str+='名称：'+data[i].request_name;
					str+='</a></p>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].request_id+'i" class="panel-collapse collapse">';
					str+='       <div class="panel-body">';
					str+='	<p>工单内容:'+data[i].request_desc+'</p>';
					str+='	<p>工单地点:'+data[i].request_map+'</p>';
					str+='	<p>工单执行人:'+data[i].request_excute_engineerName+'</p>';
					str+='	<p class="">工单签到时间: '+start_time+'</p>';
					str+='	<p class="">工单签退时间:'+finsh_time+'</p>';
					str+='	</div>';
					str+='	</div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer">';
					str+='工单应该执行时间：'+getDate(data[i].request_start_time);
					str+='</div>';
					str+='</div>';
					$("#salePrv_body").append(str);
				}
			}else{
				myApp.alert(result.msg,'通威');
			}
		},
		error:function(){
			myApp.alert("查询请求异常~~。",'通威');
		}
	})
}
//派工按钮
function findSendTask(name,myApp){
	 changeTilte("我的派工");
	$.ajax({
		url:base_path+"/request/findSendTask.do",
		data:{"userName":name},
		type:"post",
		dataType:"json",
		async:false,
		success:function(result){
			if(result.status==0){
				var data=result.data;
				$("#salePrv_body").html("");
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=getDate(data[i].request_real_startTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_startTime);
					var finsh_time=getDate(data[i].request_real_finshTime)=="1970-01-01 08:00:00"?"暂无":getDate(data[i].request_real_finshTime);

					str+='<div class="card">';
					str+='<div class="card-header" href="#'+data[i].request_id+'i" data-toggle="collapse" data-parent="#accordion">';
					str+='	<a>';
					str+='名称：'+data[i].request_name;
					str+='</a>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].request_id+'i" class="panel-collapse collapse">';
					str+='       <div class="panel-body">';
					str+='	<p>工单内容:'+data[i].request_desc+'</p>';
					str+='	<p>工单地点:'+data[i].request_map+'</p>';
					str+='	<p>工单执行人:'+data[i].request_excute_engineerName+'</p>';
					str+='	<p class="">工单签到时间: '+start_time+'</p>';
					str+='	<p class="">工单签退时间: '+finsh_time+'</p>';
					str+='	</div>';
					str+='	</div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer">';
					str+='工单应该执行时间：'+getDate(data[i].request_start_time);
					str+='</div>';;
					str+='</div>';
					$("#salePrv_body").append(str);
				}
			}else{
				myApp.alert(result.msg,"通威");
			}
		},
		error:function(){
			myApp.alert("查询请求异常。",'通威');
		}
	})
}
//所有请求
function allReq(myApp){
	$("#manger").siblings().addClass("hide");
	$("#manger").removeClass("hide");
	 changeTilte("所有派工请求");
	$("#allReq-body").html("");
	//发送查看所有请求的数据
	$.ajax({
		url : base_path+ "/request/requestList.do",
		data : {},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				var data = result.data;
				for (var i = 0; i < data.length; i++) {
					var start_time=data[i].request_start_time;
					start_time=getDateVal(start_time);
					var finsh_time=data[i].request_finsh_time;
					finsh_time=getDateVal(finsh_time);
					//var type=data[i].request_type;
					//var　typeText=type==1?"销售工单":type==2?"上门售前":type=="3"?"其它售前":type=='4'?"学习培训":type==5?"售后":"远程工单";
					var str = "";
					str+='<div class="card">';
					str+='<div class="card-header" >';
					str+='<a data-toggle="collapse" data-parent="#accordion" ';
					str+='href="#'+data[i].request_id+i+'">名称:'+data[i].request_name+"";
					str+='<i class="fa fa-caret-down"></i>'
					str+='</a>';
					//str+='<i>申请人：'+data[i].request_sponsor_name+'</i>';
					//str+='<i>时间：'+start_time+'</i>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='&nbsp;&nbsp;&nbsp;<a>申请人：'+data[i].request_sponsor_name+'</a><br>&nbsp;&nbsp;&nbsp;<a>'+start_time+'</a>'
					str+='<div class="card-content-inner">';
					str+='	 <div id="'+data[i].request_id+i+'" class="panel-collapse collapse">';
					str+='        <div class="panel-body">';
					str+='        	<div class="list-block">';
					str+='				<ul>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-envelope"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<div class="item-input">';
					str+='									任务内容:<textarea placeholder="任务内容"  style="font-family: 楷体;vertical-align: middle;">'+data[i].request_desc+'</textarea>';
					str+='								</div>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='<li>';
					str+='<div class="item-content">';
					str+='	<div class="item-media">';
					str+='		<i class="fa fa-bookmark"></i>';
					str+='	</div>';
					str+='	<div class="item-inner">';
					str+='		<div class="item-input">';
					str+='			<select>';
					str+='	<option value="1">上门售前</option>';
					str+='	<option value="2">方案标书</option>';
					str+='	<option value="3">其它售前</option>';
					str+='	<option value="4">安装实施</option>';
					str+='	<option value="5">售后</option>';
					str+='	<option value="6">远程</option>';
					str+='	<option value="7">学习培训</option>';
					str+='			</select>';
					str+='		</div>';
					str+='	</div>';
					str+='</div>';
					str+='</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-map-marker" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								地点：<a href="">'+data[i].request_map+'</a>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-user-o" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								派工申请人：<a href="">'+data[i].request_sponsor_name+'</a>';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='					 <a href="#" class="item-link smart-select" data-page-title=" " data-back-text="确认" data-back-icon=" ">';
					str+='					 	<select class="engineersPerson" style="font-family: 楷体;font-weight: bold;" multiple="multiple">';
					str+='						</select>';
					str+='					<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-male" data-toggle="tooltip" title="任务工程师"></i>';
					str+='							</div>';
					str+='						<div class="item-inner">';
					str+='								 <div class="item-title">执行工程师：</div>';
					str+='								 <div class="item-after"></div>';
					str+='						</div>';
					str+='					</div>';
					str+='					</a>';
					str+='				</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-calendar-o" data-toggle="tooltip"';
					str+='									></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<a>开始时间：</a><input type="datetime-local" value="'+start_time+'" class="start_time">';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='					<li>';
					str+='						<div class="item-content">';
					str+='							<div class="item-media">';
					str+='								<i class="fa fa-calendar" data-toggle="tooltip"></i>';
					str+='							</div>';
					str+='							<div class="item-inner">';
					str+='								<a>结束时间：</a>';
					str+='								<input type="datetime-local" value="'+finsh_time+'"class="finsh_time">';
					str+='							</div>';
					str+='						</div>';
					str+='					</li>';
					str+='				</ul>';
					str+='			</div>';
					str+='        </div>';
					str+='       </div>';
					str+='</div>';
					str+='</div>';
					str+='<div class="card-footer ">';
					str+='	<a href="#" class="button button-round button-fill color-green">确认</a>';
					str+='	<a href="#" class="button button-round button-fill color-red">忽略</a>';
					str+='</div>';
					str+='</div>';
					var $str=$(str).data("request_id",data[i].request_id);
					$str.find("select").val(data[i].request_type);
					$("#allReq-body").append($str);
				}
			} else {
				myApp.alert("接收人载入失败","通威");
			}
				datePick(".myDatePicker",1);
		},
		error : function() {
			myApp.alert("异常！！警告！","通威");
		}
	});
	//工程师数据
	$.ajax({
		url : base_path+ "/table/dingdingTest-deptList.do",
		data : {},
		dataType : "json",
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				var data = result.data;
				$(".engineersPerson").html(" ");
				for (var i = 0; i < data.length; i++) {
					var str = "";
					str += "<option value='"+data[i].name+","+data[i].userid+"'>";
					str += data[i].name;
					str += "</option>";
					$(".engineersPerson").append(str);
				}
			} else {
				myApp.alert("接收人载入失败","通威");
			}
		},
		error : function() {
			myApp.alert("异常！！警告！","通威");
		}
	});
}
//查看月度完成工单
function monFinshWorkTask(myApp){
	myApp.showIndicator();
	$("#engineerWorkTask").siblings().addClass("hide");
	$("#engineerWorkTask").removeClass("hide");
	$("#searchWorkNameDiv").removeClass("hide");
	changeTilte('当月完成工单');
	//获取到用户的名字
	var engineerName=userName;
	$.ajax({
		url : base_path+ "/workTask/workTaskEngineerFinshByMon.do",
		data : {"engineer_id":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			myApp.hideIndicator();
			if(result.status==0){
				$("#engineerWorkTask_Body").html("");
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=data[i].workTask_start_time;
					start_time=getDate(start_time);
					var finsh_time=data[i].workTask_finsh_time;
					finsh_time=getDate(finsh_time);
					var type=data[i].workTask_type;
					var　typeText=type==1?"上门售前":type==2?"方案标书":type==3?"其它售前":type==4?"安装实施":type==5?"售后":type==6?"远程":"学习培训";
					str+='<div class="card" >';
					str+='<div class="card-header">';
					str+='	<a data-toggle="collapse" data-parent="#accordion" ';
					str+='	href="#'+data[i].workTask_id+'">名称：'+data[i].workTask_name+"   ";
					str+='<i class="fa fa-caret-down"></i>'
					str+='</a>';
					str+='<a href="#" class="button button-round button-fill color-deeporange open-login-screen">'
					str+='备注</a>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].workTask_id+'" class="panel-collapse collapse">';
					str+='        <div class="panel-body">';
					str+='		<p>工单要请求人 :'+data[i].workTask_sponsor_name+'</p>';
					str+='		<p>工单内容:'+data[i].workTask_desc+'</p>';
					str+='		<p>工单地点:'+data[i].workTask_map+'</p>';
					str+='		<p>工单类型:'+typeText+'</p>';
					str+='		<p>工单要求开始时间 :'+start_time+'</p>';
					str+='		<p>工单要求结束时间 :'+finsh_time+'</p>';
					str+='		<p>工单备注 :'+data[i].workTask_engineer_remark+'</p>';
					str+='		<p class="real_start_time hide">工单实际开始时间 :</p>';
					str+='		<p class="real_finsh_time hide">工单实际结束时间 :</p>';
				//	str+='<input class="input-file" value="拍照" type="file" accept="image/*" capture="camera">'
				//	str+='<canvas  style="border:1px solid #000000; height: 100%;width: 500px;margin: 0;padding: 0;display: block;""></canvas>';
				//	str+='<img src="" style="display:none;width:100%;height:500px;">'
					str+='		</div>';
					str+='		</div>';
					str+='	</div>';
					str+='</div>';
					str+='<div class="card-footer" id="'+data[i].workTask_id+'1" >';
					str+='执行人：'+data[i].workTask_engineer_name;
					str+='</div>';
					str+='</div>';
					var $str=$(str).data("workTask_id",data[i].workTask_id);
					$("#engineerWorkTask_Body").append($str);
					$("#myCanvas").hide();
				}
			}else{
				$("#engineerWorkTask_Body").html("");
				myApp.alert(result.msg,"通威");								
			}
		},error:function(){
			myApp.hideIndicator();
			myApp.alert("警告，搜索指定工程师异常","通威");
		}
	})
	//发送签到的显示请求
	$.ajax({
		url : base_path+ "/signin/whoFinsh.do",
		data : {"engineerId":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var select="#"+data[i].workTask_id+"1";
					//实际实际开始时间
					var startTime=data[i].signin_start_time;
					startTime=getDate(startTime);
					if(startTime=="1970-01-01 08:00:00"||!startTime){
						startTime="暂无";
					}
					if(startTime.length>16){
						startTime=startTime.substr(0,16) ;
					}
					$(select).prev().find(".real_start_time").text("签到时间: "+startTime);
					$(select).prev().find(".real_start_time").removeClass("hide");
					//实际结束时间
					var finshTime=data[i].signin_finsh_time;
					finshTime=getDate(finshTime);
					if(finshTime=="1970-01-01 08:00:00"){
						finshTime="暂无";
					}
					$(select).prev().find(".real_finsh_time").text("签退时间: "+finshTime);
					$(select).prev().find(".real_finsh_time").removeClass("hide");
				}
			}else{
				myApp.alert(result.msg,"通威");
			}
		},error:function(){
			myApp.alert("我的工单数据异常","通威")
		}
	})
}
//年度完成工单
function yearFinshWorkTask(myApp){
	myApp.showIndicator();
	$("#engineerWorkTask").siblings().addClass("hide");
	$("#engineerWorkTask").removeClass("hide");
	$("#searchWorkNameDiv").removeClass("hide");
	//$("#navbar").text("我的完成工单");
	
	//获取到用户的名字
	var engineerName=userName;
	$.ajax({
		url : base_path+ "/workTask/workTaskEngineerFinsh.do",
		data : {"engineerId":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			myApp.hideIndicator();
			if(result.status==0){
				$("#engineerWorkTask_Body").html("");
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var str="";
					var start_time=data[i].workTask_start_time;
					start_time=getDate(start_time);
					var finsh_time=data[i].workTask_finsh_time;
					finsh_time=getDate(finsh_time);
					var type=data[i].workTask_type;
					var　typeText=type==1?"上门售前":type==2?"方案标书":type==3?"其它售前":type==4?"安装实施":type==5?"售后":type==6?"远程":"学习培训";
					str+='<div class="card" >';
					str+='<div class="card-header">';
					str+='	<a data-toggle="collapse" data-parent="#accordion" ';
					str+='	href="#'+data[i].workTask_id+'">名称：'+data[i].workTask_name+"  ";
					str+='<i class="fa fa-caret-down"></i>'
					str+='</a>';
					str+='<a href="#" class="button button-round button-fill color-deeporange open-login-screen">'
					str+='备注</a>';
					str+='</div>';
					str+='<div class="card-content">';
					str+='	<div class="card-content-inner">';
					str+='		<div id="'+data[i].workTask_id+'" class="panel-collapse collapse">';
					str+='        <div class="panel-body">';
					str+='		<p>工单要请求人 :'+data[i].workTask_sponsor_name+'</p>';
					str+='		<p>工单内容:'+data[i].workTask_desc+'</p>';
					str+='		<p>工单地点:'+data[i].workTask_map+'</p>';
					str+='		<p>工单类型:'+typeText+'</p>';
					str+='		<p>工单要求开始时间 :'+start_time+'</p>';
					str+='		<p>工单要求结束时间 :'+finsh_time+'</p>';
					str+='		<p>工单备注 :'+data[i].workTask_engineer_remark+'</p>';
					str+='		<p class="real_start_time hide">工单实际开始时间 :</p>';
					str+='		<p class="real_finsh_time hide">工单实际结束时间 :</p>';
				//	str+='<input class="input-file" value="拍照" type="file" accept="image/*" capture="camera">'
				//	str+='<canvas  style="border:1px solid #000000; height: 100%;width: 500px;margin: 0;padding: 0;display: block;""></canvas>';
				//	str+='<img src="" style="display:none;width:100%;height:500px;">'
					str+='		</div>';
					str+='		</div>';
					str+='	</div>';
					str+='</div>';
					str+='<div class="card-footer" id="'+data[i].workTask_id+'1" >';
					str+='执行人：'+data[i].workTask_engineer_name;
					str+='</div>';
					str+='</div>';
					var $str=$(str).data("workTask_id",data[i].workTask_id);
					$("#engineerWorkTask_Body").append($str);
					$("#myCanvas").hide();
				}
			}else{
				$("#engineerWorkTask_Body").html("");
				myApp.alert(result.msg,"通威");								
			}
		},error:function(){
			myApp.hideIndicator();
			myApp.alert("警告，搜索指定工程师异常","通威");
		}
	})
	//发送签到的显示请求
	$.ajax({
		url : base_path+ "/signin/whoFinsh.do",
		data : {"engineerId":userid},
		async:false,
		dataType : "json",
		type : "post",
		success : function(result) {
			if(result.status==0){
				var data=result.data;
				for(var i=0;i<data.length;i++){
					var select="#"+data[i].workTask_id+"1";
					//实际实际开始时间
					var startTime=data[i].signin_start_time;
					startTime=getDate(startTime);
					if(startTime=="1970-01-01 08:00:00"||!startTime){
						startTime="暂无";
					}
					if(startTime.length>16){
						startTime=startTime.substr(0,16) ;
					}
					$(select).prev().find(".real_start_time").text("签到时间: "+startTime);
					$(select).prev().find(".real_start_time").removeClass("hide");
					//实际结束时间
					var finshTime=data[i].signin_finsh_time;
					finshTime=getDate(finshTime);
					if(finshTime=="1970-01-01 08:00:00"){
						finshTime="暂无";
					}
					$(select).prev().find(".real_finsh_time").text("签退时间: "+finshTime);
					$(select).prev().find(".real_finsh_time").removeClass("hide");
				}
			}else{
				myApp.alert(result.msg,"通威");
			}
		},error:function(){
			myApp.alert("我的工单数据异常","通威")
		}
	})
}
//概览页面
function previewPage(myApp,userName,dept,userid){
	myApp.showIndicator();
	if(userName=="官丹"){
		allReq(myApp);
	}else if(dept=="[23518073]"){
		workTaskUndo(myApp,userName,userid);
	}else{
		//默认显示全部
		allCaseFindByUser(userName,myApp);			
		//发送请求获取其它类型的数据
		$.ajax({
			//获取销售需要查看到数据
			url:base_path+"/request/findSaleReqNum.do",
			data:{"userName":userName},
			dataType:"json",
			type:"post",
			success:function(result){
				if(result.status==0){
					var data=result.data;
					//派工数据
					$("#sendNum").text(data.sub);
					//执行数据
					$("#excuteNum").text(data.excute);
					//关闭数据
					$("#closeNum").text(data.close);
				}
			}
		})
	}
}
//日期选择器
function datePick(id,dayNum){
	var today = new Date();
	var myApp = new Framework7();
	var today = new Date();
	var MonthsDays = {
			一月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			二月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28],
			三月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			四月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30],
			五月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			六月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30],
			七月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			八月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			九月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30],
			十月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],
			十一月 : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30],
			十二月: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31]
	};
	var months=['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月',
		'十一月','十二月'];
	//获取该月
	var nowMonth=today.getMonth()*1;
	//获取当前日期 加上权限时间
	var nowDay=today.getDate()+dayNum;
	//获取该月最大日期
	var maxInMonth= new Date(today.getFullYear(), today.getMonth()*1 + 1, 0).getDate()*1;
	var newMonthDay=[];
	for(var i=nowDay;i<=maxInMonth;i++){
		newMonthDay.push(i);
	}
	//重新赋值
	//var newMonthsDays=MonthsDays;
	//MonthsDays[months[nowMonth]]=newMonthDay;
	//生成日期选择器
	var pickerDependent = myApp.picker({
		input: id,
		rotateEffect: true,
		toolbarCloseText:"完成",
		formatValue: function (picker, values) {
			return values[0]+"-"+(values[1]<10?"0"+values[1]:values[1])+"-"+(values[2]<10?"0"+values[2]:values[2])+" "+values[4]+":"+values[5];
		},
		value: [today.getFullYear(),today.getMonth()+1, today.getDate(), "一" ,today.getHours(), (today.getMinutes() < 10 ? '0' + today.getMinutes() : today.getMinutes())],
		onChange:function(picker,values){
			var year= picker.cols[0].value;
			var month= picker.cols[1].value;
			month=month<10?"0"+month:month;
			var day= picker.cols[2].value;
			day=day<10?"0"+day:day;
			var weekDay=new Date(year+"-"+month+"-"+day).getDay();
			var xingqi=['日','一','二','三','四','五','六'];
			var attr=[xingqi[weekDay]];
			if(picker.cols[3].replaceValues){
				picker.cols[3].replaceValues(attr);
			}
		},
		cols: [{
			values: (function () {
				var arr = [today.getFullYear(),today.getFullYear()+1];
				return arr;
			})(),
			onChange:function(picker){
				//年份在变
				if(today.getFullYear()==picker.cols[0].value){
					var vls=[];
					//获取当前月份
					var nowMonth=today.getMonth()*1+1;
					for(var i=nowMonth;i<=12;i++){
						vls.push(i);
					}
					if(picker.cols[1].replaceValues){
						picker.cols[1].replaceValues(vls);
					}
				}else{
					var ls=['1','2','3','4','5','6','7','8','9','10','11','12'];
					if(picker.cols[1].replaceValues){
						picker.cols[1].replaceValues(ls);
					}
				}
			}
		},{
			textAlign: 'left',
			values: ["1","2","3"],
			displayValues:months ,
			onChange:function(picker,value,display){
				if(picker.cols[2].replaceValues){
					//  alert(display);
					// alert(today.getMonth()+1==display);
					var MonthDay=[];
					if(today.getMonth()+1==display&&today.getFullYear()==picker.cols[0].value){
						var maxInMonth= new Date(today.getFullYear(), today.getMonth()*1 + 1, 0).getDate()*1;
						var nowDay=today.getDate();
						for(var i=nowDay+dayNum;i<=maxInMonth;i++){
							MonthDay.push(i);
						}
						picker.cols[2].replaceValues(MonthDay);
					}else{
						if(display==1||display==3||display==5||display==7||display==8||display==10||display==12){
							for(var i=1;i<=31;i++){
								MonthDay.push(i);
							}
						}
						if(display==4||display==6||display==9||display==11){
							for(var i=1;i<=30;i++){
								MonthDay.push(i);
							}
						}
						if(display==2){
							var maxIn2Month= new Date(picker.cols[0].value, 3, 0).getDate()*1;
							for(var i=1;i<=maxIn2Month;i++){
								MonthDay.push(i);
							}
						}
						picker.cols[2].replaceValues(MonthDay);
						//MonthsDays[display]
					}
				}
			}

		},{
			values: MonthsDays.一月,
		},{
			values:['日','一','二','三','四','五','六'],
		},
		// Space divider
		{
			divider: true,
			content: '  '
		},
		// Hours
		{
			values: (function () {
				var arr = [];
				for (var i = 0; i <= 23; i++) { arr.push(i); }
				return arr;
			})(),
		},
		// Divider
		{
			divider: true,
			content: ':'
		},
		// Minutes
		{
			values: (function () {
				var arr = [];
				for (var i = 0; i <= 59; i++) { arr.push(i < 10 ? '0' + i : i); }
				return arr;
			})(),
		}
		]
	});
}
//选择图片
function selectFileImage1(file,text,text1,text2) { 
	if (file) { 
		var rFilter = /^(image\/jpeg|image\/png)$/i; // 检查图片格式 
		if (!rFilter.test(file.type)) { 
			return; 
		} 

		var oReader = new FileReader(); 
		oReader.onload = function(e) { 
			var image = new Image(); 
			image.src = e.target.result; 
			image.onload = function() { 

				var square = 320;

				var imageWidth;
				var imageHeight;
				var offsetX = 0;
				var offsetY = 0;
				if (this.width > this.height) {
					imageWidth = Math.round(square * this.width / this.height);
					imageHeight = square;
					offsetX = -Math.round((imageWidth - square) / 2);
				} else {
					imageHeight = Math.round(square * this.height / this.width);
					imageWidth = square;
					offsetY = -Math.round((imageHeight - square) / 2);
				}
				var  canvas = document.querySelector('canvas'); 
				var ctx = canvas.getContext("2d"); 

				canvas.width=imageWidth;
				canvas.height=imageHeight;
				this.width=imageWidth;
				this.height=imageHeight;
				ctx.drawImage(this, 0,0, imageWidth, imageHeight); 

				var  canvas = document.querySelector('canvas'); 
				var ctx = canvas.getContext("2d"); 
				ctx.font = 15+"px microsoft yahei";
				ctx.textBaseline = 'middle';//更改字号后，必须重置对齐方式，否则居中麻烦。设置文本的垂直对齐方式
				ctx.textAlign = 'center';
				var ftop = canvas.height-15*4-15;
				var fleft = canvas.width-30;
				ctx.fillStyle="#ffffff";
				ctx.fillText(text,fleft,ftop);//文本元素在画布居中方式
				var size=text1.length;
				var top=0;
				var left=0;
				if(size>16){
					var a=text1.substr(0,16);
					var ftop1=ftop+15;
					var fleft1= canvas.width-130;
					ctx.fillStyle="#fff";
					ctx.fillText(a,fleft1,ftop1);
					top=ftop1+15;
					left= canvas.width-130;
					ctx.fillStyle="#fff";
					ctx.fillText(text1.substr(16,text1.length),left,top);
				}else{
					top=ftop+15;
					left= canvas.width-130;
					ctx.fillStyle="#fff";
					ctx.fillText(text1,left,top);
				}
				var ftop2=top+15;
				var fleft2= canvas.width-55;
				ctx.fillStyle="#fff";
				ctx.fillText(text2,fleft2,ftop2);

				ctx.fill();
				ctx.save();
			}; 
		}; 
		oReader.readAsDataURL(file); 
	} 
}
//水印
function shuiyan(text,text2,text3){
	alert("shuiyan");
	var cxt = $("canvas")[0].getContext('2d');
	cxt.font = 20+"px microsoft yahei";
	cxt.textBaseline = 'middle';//更改字号后，必须重置对齐方式，否则居中麻烦。设置文本的垂直对齐方式
	cxt.textAlign = 'center';
	var fsz = 20;
	var ftop = canvas.height-50;
	var fleft = canvas.width-30;
	cxt.fillStyle="#ffffff";
	cxt.fillText(text,ftop,fleft);//文本元素在画布居中方式

	var ftop2=ftop+fsz;
	var fleft1= canvas.width/2;
	cxt.fillStyle="#fff";
	cxt.fillText(text1,ftop2,fleft1);
	cxt.save();
	var ftop3=ftop2+fsz;
	var fleft2= fleft-20;
	context.fillStyle="rgba(255,255,255,1)";
	context.fillText(text2,fleft2,ftop3);
	context.strokeStyle = 'yellow';
	context.save();
	context.fill();
}
//对图片旋转处理 added by lzk 
function rotateImg(img, direction,canvas) {  
	//alert(img); 
	alert("旋转处理"); 
	//最小与最大旋转方向，图片旋转4次后回到原方向  
	var min_step = 0;  
	var max_step = 3;  
	//var img = document.getElementById(pid);  
	if (img == null)return;  
	//img的高度和宽度不能在img元素隐藏后获取，否则会出错  
	var height = img.height;  
	var width = img.width;  
	//var step = img.getAttribute('step');  
	var step = 2;  
	if (step == null) {  
		step = min_step;  
	}  
	if (direction == 'right') {  
		step++;  
		//旋转到原位置，即超过最大值  
		step > max_step && (step = min_step);  
	} else {  
		step--;  
		step < min_step && (step = max_step);  
	}  
	//旋转角度以弧度值为参数  
	var degree = step * 90 * Math.PI / 180;  
	var ctx = canvas.getContext('2d');  
	switch (step) {  
	case 0:  
		canvas.width = width;  
		canvas.height = height;  
		ctx.drawImage(img, 0, 0);
		shuiyan();
		break;  
	case 1:  
		canvas.width = height;  
		canvas.height = width;  
		ctx.rotate(degree);  
		ctx.drawImage(img, 0, -height);  
		shuiyan();
		break;  
	case 2:  
		canvas.width = width;  
		canvas.height = height;  
		ctx.rotate(degree);  
		ctx.drawImage(img, -width, -height);
		shuiyan();
		break;  
	case 3:  
		canvas.width = height;  
		canvas.height = width;  
		ctx.rotate(degree);  
		ctx.drawImage(img, -width, 0);  
		shuiyan();
		break;  
	}
} 
//清空Canvas
function clearCanvas(){
	var c=document.getElementById("myCanvas");  
	var cxt=c.getContext("2d");  
	c.height=c.height;  
}
//选择图片1
function selectFileImage(file,name,map,date) { 
	//图片方向角 added by lzk 
	var Orientation = null; 
	if (file) { 
		var rFilter = /^(image\/jpeg|image\/png)$/i; // 检查图片格式 
		if (!rFilter.test(file.type)) { 
			return; 
		} 
		//获取照片方向角属性，用户旋转控制 
		EXIF.getData(file, function() { 
			EXIF.getAllTags(this);  
			Orientation = EXIF.getTag(this, 'Orientation'); 
		}); 

		var oReader = new FileReader(); 
		oReader.onload = function(e) { 
			var image = new Image(); 
			image.src = e.target.result; 
			image.onload = function() { 
				var expectWidth = this.naturalWidth; 
				var expectHeight = this.naturalHeight; 

				if (this.naturalWidth > this.naturalHeight && this.naturalWidth > 800) { 
					expectWidth = 800; 
					expectHeight = expectWidth * this.naturalHeight / this.naturalWidth; 
				} else if (this.naturalHeight > this.naturalWidth && this.naturalHeight > 1200) { 
					expectHeight = 1200; 
					expectWidth = expectHeight * this.naturalWidth / this.naturalHeight; 
				} 
				var  canvas = document.querySelector('canvas'); 
				var ctx = canvas.getContext("2d"); 
				canvas.width = expectWidth; 
				canvas.height = expectHeight; 
				ctx.drawImage(this, 0, 0, expectWidth, expectHeight); 
				var base64 = null; 
				var fsz=0;
				//修复ios 
				if (navigator.userAgent.match(/iphone/i)) { 
					//console.log('iphone'); 
					if(Orientation != "" && Orientation != 1){ 
						//alert('旋转处理'); 
						switch(Orientation){ 
						case 6://需要顺时针（向左）90度旋转 
							// alert('需要顺时针（向左）90度旋转'); 
							rotateImg(this,'left',canvas); 
							break; 
						case 8://需要逆时针（向右）90度旋转 
							//alert('需要顺时针（向右）90度旋转'); 
							rotateImg(this,'right',canvas); 
							break; 
						case 3://需要180度旋转 
							//alert('需要180度旋转'); 
							rotateImg(this,'right',canvas);//转两次 
							rotateImg(this,'right',canvas); 
							break; 
						}     
					} 
					fsz=50;
					base64 = canvas.toDataURL("image/jpeg", 0.8); 
				}else if (navigator.userAgent.match(/Android/i)) {// 修复android 
					var encoder = new JPEGEncoder();
					fsz=50;
					base64 = encoder.encode(ctx.getImageData(0, 0, expectWidth, expectHeight), 80); 
				}else{ 
					//alert(Orientation); 
					if(Orientation != "" && Orientation != 1){ 
						//alert('旋转处理'); 
						switch(Orientation){ 
						case 6://需要顺时针（向左）90度旋转 
							//alert('需要顺时针（向左）90度旋转'); 
							rotateImg(this,'left',canvas); 
							break; 
						case 8://需要逆时针（向右）90度旋转 
							//alert('需要顺时针（向右）90度旋转'); 
							rotateImg(this,'right',canvas); 
							break; 
						case 3://需要180度旋转 
							//alert('需要180度旋转'); 
							rotateImg(this,'right',canvas);//转两次 
							rotateImg(this,'right',canvas); 
							break; 
						}     
					} 
					fsz=30;
					base64 = canvas.toDataURL("image/png", 0.8); 
				}
				ctx.save();
				//ctx.clearRect(0,0, canvas.width, canvas.height);
				ctx.fillStyle = "green";//填充样式演示
				ctx.strokeStyle="red";//轮廓颜色
				ctx.strokeRect(250,250,250,250);//绘制矩形轮廓
				ctx.fillRect(250,250, 250, 250);//绘制矩形

				ctx.font = fsz+"px microsoft yahei";
				ctx.textBaseline = 'middle';//更改字号后，必须重置对齐方式，否则居中麻烦。设置文本的垂直对齐方式
				ctx.textAlign = 'center';
				var nameL=name.length;
				alert(nameL+";"+fsz);
				var ftop = canvas.height/2
				var fleft = canvas.width/2;
				ctx.fillStyle="#ffffff";
				alert(fleft+":"+ftop);
				ctx.fillText(name,fleft,ftop);

				var mapL=map.length;
				alert(mapL);
				var ftop1 = ftop+fsz+5;
				var fleft1 = canvas.width/2;
				ctx.fillStyle="#ffffff";
				ctx.fillText(map,fleft1,ftop1); 

				var dateL=date.length;
				alert(dateL);
				var ftop2 = ftop1+fsz+5;
				var fleft2 = canvas.width/2;
				ctx.fillStyle="#ffffff";
				ctx.fillText(date,fleft2,ftop2); 
			}; 
		}; 
		oReader.readAsDataURL(file); 
	} 
} 
