;(function($){
/**
 * jqGrid Chinese Translation for v4.2
 * henryyan 2011.11.30
 * http://www.wsria.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * update 2011.11.30
 *		add double u3000 SPACE for search:odata to fix SEARCH box display err when narrow width from only use of eq/ne/cn/in/lt/gt operator under IE6/7
**/
$.jgrid = $.jgrid || {};
$.extend($.jgrid, {
	defaults : {
		recordtext: "{0} - {1}\u3000共 {2} 筆",	// 共字前是全形空格
		emptyrecords: "無資料顯示",
		loadtext: "讀取中...",
		pgtext: " {0} 共 {1} 頁"
	},
	search : {
		caption: "搜尋...",
		Find: "查詢",
		Reset: "重設",
		odata : ['等於\u3000\u3000', '不等\u3000\u3000', '小於\u3000\u3000', '小於等於','大於\u3000\u3000','大於等於', 
			'開始於','不開始於','屬於\u3000\u3000','不屬於','結束於','不結束於','包含\u3000\u3000','不包含','空值於\u3000\u3000','非空值'],
		groupOps: [	{ op: "AND", text: "所有" },	{ op: "OR",  text: "任一" }	],
		matchText: " 匹配",
		rulesText: " 規則"
	},
	edit : {
		addCaption: "新增資料",
		editCaption: "編輯資料",
		bSubmit: "送出",
		bCancel: "取消",
		bClose: "關閉",
		saveData: "資料已改變，是否儲存？",
		bYes : "是",
		bNo : "否",
		bExit : "取消",
		msg: {
			required:"此欄位必需",
			number:"請輸入有效數字",
			minValue:"輸入值必須大於等於 ",
			maxValue:"輸入值必須小於等於 ",
			email: "這不是有效的e-mail位址",
			integer: "請輸入有效整數",
			date: "請輸入有效時間",
			url: "無效網址。首碼必須為 ('http://' 或 'https://')",
			nodefined : " 未定義！",
			novalue : " 需要傳回值！",
			customarray : "自訂函數需要傳回陣列！",
			customfcheck : "Custom function should be present in case of custom checking!"
			
		}
	},
	view : {
		caption: "瀏覽資料",
		bClose: "關閉"
	},
	del : {
		caption: "刪除",
		msg: "刪除選取資料？",
		bSubmit: "刪除",
		bCancel: "取消"
	},
	nav : {
		edittext: "",
		edittitle: "編輯選取資料",
		addtext:"",
		addtitle: "新增資料",
		deltext: "",
		deltitle: "刪除選取資料",
		searchtext: "",
		searchtitle: "尋找",
		refreshtext: "",
		refreshtitle: "重整表格",
		alertcap: "注意",
		alerttext: "請選擇資料",
		viewtext: "",
		viewtitle: "瀏覽選取資料"
	},
	col : {
		caption: "選擇列",
		bSubmit: "確定",
		bCancel: "取消"
	},
	errors : {
		errcap : "錯誤",
		nourl : "沒有設定url",
		norecords: "沒有要處理的資料",
		model : "colNames 和 colModel 長度不等！"
	},
	formatter : {
		integer : {thousandsSeparator: " ", defaultValue: '0'},
		number : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, defaultValue: '0.00'},
		currency : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0, prefix: "", suffix:"", defaultValue: ''},
		date : {
			dayNames:   [
				"日", "一", "二", "三", "四", "五", "六",
		         "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
			],
			monthNames: [
				"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二",
				"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
			],
			AmPm : ["早","晚","上午","下午"],
			S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th'},
			srcformat: 'Y-m-d',
			newformat: 'm-d-Y',
			masks : {
				ISO8601Long:"Y-m-d H:i:s",
				ISO8601Short:"Y-m-d",
				ShortDate: "Y/j/n",
				LongDate: "l, F d, Y",
				FullDateTime: "l, F d, Y g:i:s A",
				MonthDay: "F d",
				ShortTime: "g:i A",
				LongTime: "g:i:s A",
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				YearMonth: "F, Y"
			},
			reformatAfterEdit : false
		},
		baseLinkUrl: '',
		showAction: '',
		target: '',
		checkbox : {disabled:true},
		idName : 'id'
	}
});
})(jQuery);