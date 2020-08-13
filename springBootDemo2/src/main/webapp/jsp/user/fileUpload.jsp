<%@ page language="java" contentType="text/html;charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<%@ include file="/common/taglibs.jsp"%>

<style type="text/css">
.input-group-btn> .multiselect-clear-filter{height: 34px}
.item{display: inline-block;}
</style>
<script type="text/javascript">
$(function(){
	
		layui.use('layer',function(){
				
			})
	
		var $list = $("#fileList"),
		thumbnailWidth = 110,
        thumbnailHeight = 110 ,
		$btn = $("#btn-star"),
		state = "pending",
		isSupportBase64 = ( function() {
            var data = new Image();
            var support = true;
            data.onload = data.onerror = function() {
                if( this.width != 1 || this.height != 1 ) {
                    support = false;
                }
            }
            data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
            return support;
        } )(),
        percentages = {},
		uploader;
		
		var supportTransition = (function(){
             var s = document.createElement('p').style,
                 r = 'transition' in s ||
                         'WebkitTransition' in s ||
                         'MozTransition' in s ||
                         'msTransition' in s ||
                         'OTransition' in s;
             s = null;
             return r;
         })();

		var uploader = WebUploader.create({
			auto: false,
			formData:{dtId:123},
			swf: '${ctx}/lib/webuploader/0.1.5/Uploader.swf',
			// 文件接收服务端。
			server: '${ctx}/user/fileUploadSave',
			// 选择文件的按钮。可选。
			// 内部根据当前运行是创建，可能是input元素，也可能是flash.
			pick: '#filePicker',
			// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			resize: false,
			fileNumLimit:1,
			// 只允许选择图片文件。
			accept: {
				title: 'excel文件',
				extensions: 'jpg,jpeg,png,gif',
				mimeTypes: '.jpg,.jpeg,.png,.gif'
			}
		});
		uploader.on( 'fileQueued', function( file ) {
			addFile(file)
			 /* var $li = $(
				'<div id="' + file.id + '" class="item">' +
					'<div class="pic-box"><img></div>'+
					'<div class="info">' + file.name + '</div>' +
					'<p class="state">等待上传...</p>'+
				'</div>'
			),
			$img = $li.find('img');
			$list.append( $li );
		
			uploader.makeThumb( file, function( error, src ) {
				if ( error ) {
					$img.replaceWith('<span>不能预览</span>');
					return;
				}
		
				$img.attr( 'src', src );
			}, thumbnailWidth, thumbnailHeight );  */
		});
		uploader.on('fileDequeued',function(file){
			 removeFile( file );
		});
		// 文件上传过程中创建进度条实时显示。
		uploader.on( 'uploadProgress', function( file, percentage ) {
			var $li = $( '#'+file.id ),
				$percent = $li.find('.progress-box .sr-only');
		
			// 避免重复创建
			if ( !$percent.length ) {
				$percent = $('<div class="progress-box"><span class="progress-bar radius"><span class="sr-only" style="width:0%"></span></span></div>').appendTo( $li ).find('.sr-only');
			}
			$li.find(".state").text("上传中");
			$percent.css( 'width', percentage * 100 + '%' );
		});
		
		// 文件上传成功，给item添加成功class, 用样式标记上传成功。
		uploader.on( 'uploadSuccess', function( file,respone ) {
			$( '#'+file.id ).addClass('upload-state-success').find(".state").text("已上传");
			if(respone.result=="success"){
				$("#webUploadButton").hide();
				layer.alert("上传完毕",{title:"上传成功",icon:1,time:5000,end:function(){parent.layer.closeAll();}})
			}
			else if(respone.result=="fail"){
				var url='<a href="${ctx}'+respone.path+'" target="_blank">下载</a>'
				layer.alert(respone.msg+url,{title:"上传有错误",icon:2,end:function(){parent.layer.closeAll();}})
			}
			else
				layer.alert(respone.msg,{title:"上传失败",icon:2,end:function(){parent.layer.closeAll();}})
		});
		
		// 文件上传失败，显示上传出错。
		uploader.on( 'uploadError', function( file ) {
			$( '#'+file.id ).addClass('upload-state-error').find(".state").text("上传出错");
		});
		
		// 完成上传完了，成功或者失败，先删除进度条。
		uploader.on( 'uploadComplete', function( file ) {
			$( '#'+file.id ).find('.progress-box').fadeOut();
		});
		uploader.on('all', function (type) {
	        if (type === 'startUpload') {
	            state = 'uploading';
	        } else if (type === 'stopUpload') {
	            state = 'paused';
	        } else if (type === 'uploadFinished') {
	            state = 'done';
	        }

	        if (state === 'uploading') {
	            $btn.text('暂停上传');
	        } else {
	            $btn.text('开始上传');
	        }
	    });
		
	    $btn.on('click', function () {
	        if (state === 'uploading') {
	            uploader.stop();
	        } else {
	        	uploader.upload();
	        }
	    });
	    
	    function addFile( file ) {
            var $li = $( '<li id="' + file.id + '" >' +
                    '<p class="title">' + file.name + '</p>' +
                    '<p class="imgWrap"></p>'+
                    '<p class="progress"><span></span></p>' +
                    '</li>' ),

                $btns = $('<div class="file-panel">' +
                    '<span class="cancel">删除</span>' ).appendTo( $li ),
                $prgress = $li.find('p.progress span'),
                $wrap = $li.find( 'p.imgWrap' ),
                $info = $('<p class="error"></p>'),

                showError = function( code ) {
                    switch( code ) {
                        case 'exceed_size':
                            text = '文件大小超出';
                            break;

                        case 'interrupt':
                            text = '上传暂停';
                            break;

                        default:
                            text = '上传失败，请重试';
                            break;
                    }

                    $info.text( text ).appendTo( $li );
                };
			console.log(file.getStatus())
            if ( file.getStatus() === 'invalid' ) {
                showError( file.statusText );
            } else {
                // @todo lazyload
                $wrap.text( '预览中' );
                uploader.makeThumb( file, function( error, src ) {
                    var img;
                    if ( error ) {
                        $wrap.text( '不能预览' );
                        return;
                    }

                    if( isSupportBase64 ) {
                        img = $('<img src="'+src+'">');
                        $wrap.empty().append( img );
                    } else {
                        $.ajax('lib/webuploader/0.1.5/server/preview.php', {
                            method: 'POST',
                            data: src,
                            dataType:'json'
                        }).done(function( response ) {
                            if (response.result) {
                                img = $('<img src="'+response.result+'">');
                                $wrap.empty().append( img );
                            } else {
                                $wrap.text("预览出错");
                            }
                        });
                    }
                }, thumbnailWidth, thumbnailHeight );

                percentages[ file.id ] = [ file.size, 0 ];
                file.rotation = 0;
            }

            file.on('statuschange', function( cur, prev ) {
                if ( prev === 'progress' ) {
                    $prgress.hide().width(0);
                } else if ( prev === 'queued' ) {
                    $li.off( 'mouseenter mouseleave' );
                    $btns.remove();
                }

                // 成功
                if ( cur === 'error' || cur === 'invalid' ) {
                    console.log( file.statusText );
                    showError( file.statusText );
                    percentages[ file.id ][ 1 ] = 1;
                } else if ( cur === 'interrupt' ) {
                    showError( 'interrupt' );
                } else if ( cur === 'queued' ) {
                    percentages[ file.id ][ 1 ] = 0;
                } else if ( cur === 'progress' ) {
                    $info.remove();
                    $prgress.css('display', 'block');
                } else if ( cur === 'complete' ) {
                    $li.append( '<span class="success"></span>' );
                }

                $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
            });

            $li.on( 'mouseenter', function() {
                $btns.stop().animate({height: 30});
            });

            $li.on( 'mouseleave', function() {
                $btns.stop().animate({height: 0});
            });

            $btns.on( 'click', 'span', function() {
                var index = $(this).index(),
                    deg;

                switch ( index ) {
                    case 0:
                        uploader.removeFile( file );
                        return;
                }

                if ( supportTransition ) {
                    deg = 'rotate(' + file.rotation + 'deg)';
                    $wrap.css({
                        '-webkit-transform': deg,
                        '-mos-transform': deg,
                        '-o-transform': deg,
                        'transform': deg
                    });
                } else {
                    $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                }


            });
            //$li.appendTo( $queue );
            $list.append( $li );

        }
	    // 负责view的销毁
        function removeFile( file ) {
	    	console.log(file)
            var $li = $('#'+file.id);

            delete percentages[ file.id ];
            $li.off().find('.file-panel').off().end().remove();
        }
	    
      //回显
        var getFileBlob = function (orgFile, cb) {
        	  var xhr = new XMLHttpRequest();
        	  xhr.open("GET", orgFile.filePath);
        	  xhr.responseType = "blob";
        	  xhr.addEventListener('load', function() {
        	  cb(xhr.response);
        	  });
        	  xhr.send();
        };

       	var blobToFile = function (blob, name) {
       	  blob.lastModifiedDate = new Date();
       	  blob.name = name;
       	  return blob;
       	};

       	var getFileObject = function(orgFile, cb) {
       	  getFileBlob(orgFile, function (blob) {
       	  cb(blobToFile(blob, orgFile.name));
       	  });
       	};

       	//需要编辑的图片列表
       	var picList = [{filePath:'${ctx}/static/images/1.png',name:"1234test.jpg"},{filePath:'${ctx}/static/images/1.png',name:"1234test2.jpg"} ]
       	$.each(picList, function(index,item){
       	  getFileObject(item, function (fileObject) {
       	    var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),fileObject);
       	    var file = new WebUploader.File(wuFile);
       	    uploader.addFiles(file)
       	  })
       	});
})
</script>
</head>
<body>
<article class="page-container">
	<div class="row text-c mt-20">
				<div class=" uploader-list-container uploader-thum-container" style="display: inline-block;border: 0px;font-size: 14px">
					<div id="fileList" class="uploader-list filelist" style="height: 130px"></div>
					<div id="webUploadButton">
					<div id="filePicker" style="float: none;">选择文件</div>
					<button id="btn-star" class="btn btn-default btn-uploadstar radius ml-10" style="font-size: 14px;height: 31px">开始上传</button>
				    </div>
				</div>
		</div>
</article>
</body>
</html>