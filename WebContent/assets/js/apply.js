$(document).ready(function() {	
    //alert(localStorage.getItem("audienceName") + 'welcome!');
    if (localStorage.audienceName) {
        $("#audienceName").val(localStorage.getItem("audienceName"));
    }
    //alert(1);
    $( "#Birthday,#time1,#time2" ).datepicker();
    $("#btnSubmit").click(function(){
        alert($("#audienceName").val());
        // Store
        localStorage.setItem("audienceName", $("#audienceName").val());
        // Retrieve        
        document.getElementById("result").innerHTML = localStorage.getItem("audienceName");
     });
});
