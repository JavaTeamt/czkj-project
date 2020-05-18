
var access_token=null;
function login() {
    $.ajax({
        url: "http://localhost:81/api/oauth/token?grant_type=password&username=1973849950&password=123&scope=all&client_id=client1&client_secret=client1",
        datatype:"cors",
        xhrFields: {
            withCredentials: true//跨域请求
        },
        type:"POST",
        // xhrFields:{withCredentials:true},允许cookie
        success:function (data) {
            alert(data);
            access_token = data.access_token;
            console.log(access_token)
        },
        error:function () {
            alert("sorry");
        }
    });
};

 function postData() {
    var formData = new FormData();
    formData.append("file",$("#photo")[0].files[0]);
    formData.append("userName","1973849930");
    formData.append("access_token",access_token);
    $.ajax({
        url:"http://localhost:81/api/sys/user/uploadFile", /*接口域名地址*/
        type:"post",
        data: formData,
        contentType: false,
        processData: false,
        success:function(res){
            console.log(res.data);
            if(res.code=="0"){
                alert(res.message);
                console.log(res.data)
            }else if(res.code="-1"){
                alert(res.message);
            }else{
                console.log(res.message);
            }
        }
    });
};