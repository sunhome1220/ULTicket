// JavaScript Document
//FatFooter
$(document).ready(function() {
    $(".FatFooterBtn").click(function() {
        $('#FatFooter>nav>ul>li>ul').slideToggle(function() {
            if ($(this).is(':visible')) {
                document.getElementById("FatFooterBtn").value = "收合";
            } else {
                document.getElementById("FatFooterBtn").value = "展開";
            }
        });
        $(this).toggleClass('close');
    });
});

//Tab
$(document).ready(function() {
    // 預設顯示第一個 Tab
    var _showTab = 0;
    var $defaultDiv = $('div.tabs').eq(_showTab).addClass('active');
    $($defaultDiv.find('a').attr('href')).siblings('div.tab_container').hide();

    // 當 li 頁籤被點擊時...
    // 若要改成滑鼠移到 li 頁籤就切換時, 把 click 改成 mouseover
    $('div.tabs').click(function() {
        // 找出 li 中的超連結 href(#id)
        var $this = $(this),
            _clickTab = $this.find('a').attr('href');
        // 把目前點擊到的 li 頁籤加上 .active
        // 並把兄弟元素中有 .active 的都移除 class
        $this.addClass('active').siblings('.active').removeClass('active');
        // 淡入相對應的內容並隱藏兄弟元素
        $(_clickTab).stop(false, true).fadeIn().siblings('div.tab_container').hide();

        return false;
    })
    $("#font_l").trigger('click');

    function Focus(id) {
        $("#" + id).trigger('click');
        document.getElementById(id).focus();
    }
});

//Single_slider
$(document).ready(function() {
    $('.Single_slider').slick({
        arrows: true,
        dots: true,
        infinite: true,
		autoplay: true,
        autoplaySpeed: 6000,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1
    });
});

//Responsive_slider
$(document).ready(function() {
    $('.Responsive_slider').slick({
        dots: true,
        infinite: true,
        speed: 1000,
        slidesToShow: 4,
        slidesToScroll: 4,
        autoplay: true,
        autoplaySpeed: 6000,
        responsive: [{
            breakpoint: 900,
            settings: {
                slidesToShow: 3,
                slidesToScroll: 3,
                infinite: true,
                dots: true
            }
        }, {
            breakpoint: 720,
            settings: {
                slidesToShow: 2,
                slidesToScroll: 2
            }
        }, {
            breakpoint: 480,
            settings: {
                slidesToShow: 1,
                slidesToScroll: 1
            }
        }]
    });
});

//share
$(function () {
	
	$('.share').click(function(){
		$(this).toggleClass('close');
		$('.shareArea').slideToggle('fast');
	});

})

// 多層選單
$(document).ready(function() {
	var menu = $('#Header .menu');

	$('#Header .menu>ul>li').addClass('hasChild');
	
	menu.find('li.hasChild').hover(
		function(){$(this).children().stop().fadeIn(200);},
		function(){$(this).children('ul').stop().fadeOut(200);}
		);

	menu.find('li').keyup(	
		function(){
			$(this).siblings().children('ul').hide();
		});

	menu.find('li.hasChild').keyup(
		function(){
			$(this).children().show();
		});

	$('#Header .menu li:last>a').focusout(
	function(){
		$('#Header .menu li>ul').hide();
	});
	
	// 資料大類開合
	$('.categoryqq').find('.here a').clone().insertBefore('.categoryqq ul').addClass('here');
	$('.categoryqq').append('<button class="cateCtrl"></button>');
	$('.cateCtrl').click(function(){
		$(this).toggleClass('close2');
		$('.categoryqq ul').slideToggle();
		$('.categoryqq a.here').slideToggle();
		});
	$('.categoryqq li a').click(function(){
		if($('.categoryqq ul').is(':visible')){
			$('.categoryqq ul').slideUp();
			$('.categoryqq a.here').slideDown()};
			$('.cateCtrl').removeClass('close2');
	});
	//為 list table 的 td 加上對應的 data-title.
	$('.listqq table').each(function(){
		var $row = $(this).find('tr');
		rowCount = $row.length;
		for ( var n=1; n<=rowCount ; n++ ) {
			$(this).find('th').each(function(index) {
				var thText = $(this).text();
				$row.eq(n).find('td').eq(index).attr('data-title', thText);
			});
		}
    });

});

$(document).ready(function() {
        //為 list table 的 td 加上對應的 data-title.
        $('.questionnaire').each(function(){
                var $row = $(this).find('tr');
                rowCount = $row.length;
                for ( var n=1; n<=rowCount ; n++ ) {
                        $(this).find('th').each(function(index) {
                                var thText = $(this).text();
                                $row.eq(n).find('td').eq(index).attr('data-title', thText);
                        });
                }
    });

});
