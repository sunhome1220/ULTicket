$(document).ready(function() {	
    $("a[id^=href]").click(function(){
        window.location.href=this.id.replace('href_','')+'.jsp';
    });
});
