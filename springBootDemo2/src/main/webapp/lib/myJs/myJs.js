function showLayer(title,url,ww,hh,full){
	if(ww==null||ww==""){
		ww="800px";
	}
	if(hh==null||hh==""){
		hh="600px";
	}
	var index=layer.open({
		title:title,
		area:[ww,hh],
		type:2,
		shadeClose:true,
		content:url
	});
	if(full){
		layer.full(index);
	}
}

function timestampToTime(timestamp,full) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';
    //D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    if(full=="full"){
    	return Y+M+D+h+m+s;
    }
    else{
    	return Y+M+D;
    }
}