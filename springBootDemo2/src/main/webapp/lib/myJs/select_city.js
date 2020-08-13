		
		//获取省信息
		function getProvince(){
			$('#prov').html('<option value="">--请选择省份--</option>');
			$.ajax({
				 type:'get',
				 url:'${ctx}/base/area/list&parent_no=0',
				 dataType: 'json', 
				 success:function(data){
							 $.each(data, function(i, it) {
								var option = '<option value="' + it.area_no + '">' + it.area_name + '</option>';
					         	$('#province').append(option);
							 });
				 		  }
			});
		}
		
		//获取市信息
		function getCity(parent_no){
			$('#city').html('<option value="">--请选择城市--</option>');
			$.ajax({
				 type:'get',
				 url:'${ctx}/base/area/list&parent_no='+parent_no,
				 dataType: 'json', 
				 success:function(data){
							 $.each(data, function(i, it) {
								var option = '<option value="' + it.area_no + '">' + it.area_name + '</option>';
					         	$('#city').append(option);
							 });
				 		  }
			});
		}
		
		//获取区县信息
		function getCounty(parent_no){
			$('#county').html('<option value="">--请选择区县--</option>');
			$.ajax({
				 type:'get',
				 url:'${ctx}/base/area/list&parent_no='+parent_no,
				 dataType: 'json', 
				 success:function(data){
							 $.each(data, function(i, it) {
								var option = '<option value="' + it.area_no + '">' + it.area_name + '</option>';
					         	$('#area').append(option);
							 });
				 		  }
			});
		}
		
		getProvince();
