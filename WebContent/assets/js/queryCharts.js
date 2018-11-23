var reportData;
$(document).ready(function () {
    $("a[id^=href]").click(function(){
        window.location.href=this.id.replace('href_','')+'.jsp';
    });
    if (localStorage.statType) {
        $("#statType").val(localStorage.getItem("statType"));
    }
    $("#btnQry").click(function(){        
        try{
            $("#canvas").prop("height", $(window).height()*0.5);
            $("#canvas").prop("width", $(window).width()*0.7);
        }catch(e){
            
        }
        localStorage.setItem("statType", $("#statType").val());
        queryChart();        
    });   
    window.onload = function () {        
        try{
            $("#canvas").prop("height", $(window).height()*0.5);
            $("#canvas").prop("width", $(window).width()*0.7);
        }catch(e){
            
        }                
    };
    $(window).resize(function(){
        try{
            $("#canvas").prop("height", $(window).height()*0.5);
            $("#canvas").prop("width", $(window).width()*0.7);
        }catch(e){
            
        }
    });
    
       
    function queryChart(){        
        
        //var color = Chart.helpers.color;
        //getRequestNo();
        reportData = getRequestTickData();//eval('[[1200, 1400,1312,1334,0,0],[1100,0,0,0,0,0]]');
        var dataLabel = reportData[0];
        var dataRequestNo = reportData[1];
        var dataReprentNo = reportData[2];
        var dataConfirmNo = reportData[3];
        var color = Chart.helpers.color;
        var barChartData = {
            labels: dataLabel,
            datasets: [{
                    label: '索票已登錄',
                    backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
                    borderColor: window.chartColors.red,
                    borderWidth: 1,
                    data: dataRequestNo
                }, {
                    label: '預期將出席',
                    backgroundColor: color(window.chartColors.green).alpha(0.5).rgbString(),
                    borderColor: window.chartColors.green,
                    borderWidth: 1,
                    data: dataConfirmNo
                }, {
                    label: '實際出席數',
                    backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
                    borderColor: window.chartColors.blue,
                    borderWidth: 1,
                    data: dataReprentNo
                }]

        };
        var ctx = document.getElementById('canvas').getContext('2d');
        window.myBar = new Chart(ctx, {
            type: 'bar',
            data: barChartData,
            options: {
                responsive: true,
                xAxisID:0,
                legend: {
                    position: 'top'
                },
                title: {
                    display: false,
                    text: '索票/出席數'
                }
            }
        });   
        window.myBar.update();
    }
    
    document.getElementById('randomizeData').addEventListener('click', function () {
        var zero = Math.random() < 0.2 ? true : false;
        barChartData.datasets.forEach(function (dataset) {
            dataset.data = dataset.data.map(function () {
                return zero ? 0.0 : randomScalingFactor();
            });

        });
        window.myBar.update();
    });

    var colorNames = Object.keys(window.chartColors);
    document.getElementById('addDataset').addEventListener('click', function () {
        var colorName = colorNames[barChartData.datasets.length % colorNames.length];
        var dsColor = window.chartColors[colorName];
        var newDataset = {
            label: 'Dataset ' + barChartData.datasets.length,
            backgroundColor: color(dsColor).alpha(0.5).rgbString(),
            borderColor: dsColor,
            borderWidth: 1,
            data: []
        };

        for (var index = 0; index < barChartData.labels.length; ++index) {
            newDataset.data.push(randomScalingFactor());
        }

        barChartData.datasets.push(newDataset);
        window.myBar.update();
    });

    document.getElementById('addData').addEventListener('click', function () {
        if (barChartData.datasets.length > 0) {
            var month = MONTHS[barChartData.labels.length % MONTHS.length];
            barChartData.labels.push(month);

            for (var index = 0; index < barChartData.datasets.length; ++index) {
                // window.myBar.addData(randomScalingFactor(), index);
                barChartData.datasets[index].data.push(randomScalingFactor());
            }

            window.myBar.update();
        }
    });

    document.getElementById('removeDataset').addEventListener('click', function () {
        barChartData.datasets.splice(0, 1);
        window.myBar.update();
    });

    document.getElementById('removeData').addEventListener('click', function () {
        barChartData.labels.splice(-1, 1); // remove the label first

        barChartData.datasets.forEach(function (dataset) {
            dataset.data.pop();
        });

        window.myBar.update();
    });

});
function getRequestTickData(){
    var reportData;// = '[[\'新竹公演\', \'南門公演\', \'板橋公演\', \'國館公演\', \'板橋公演\'],[1200, 1400,1312,1334,0,0],[1100,0,0,0,0,110]]';
    //return eval(reportData);
    $.ajax({
        url: 'QueryServlet',
        method: 'POST',
        dataType: 'json',
        data: {
            ajaxAction: 'getReportData',
            statType:$("#statType").val()
            //eventid: $('#eventid').val(),
            //ticketnos: allTicNo
        },
        async: false,
        success: function (data) {
            //alert(data.infoMsg);
            reportData = data.infoMsg;            
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert('系統異常，請重新操作一次或通知系統管理者!');
        }
    });
    return eval(reportData);
}