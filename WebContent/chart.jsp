<%@page import="chartTest.util.*"%>
<%@page import="java.util.*"%>
<%@page import="chartTest.entity.*"%>
<%@page import="org.apache.commons.lang3.tuple.Pair"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="Chart.js"></script>
<script type="text/javascript" src="jquery-3.4.1.min.js"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<canvas id="myChart"></canvas>

	<button id="shift_left"><</button>
	<button id="shift_right">></button>
	<br> 表示期間数 :
	<input type="number" id="divnum" name="divnum" value="10" min="5"
		max="100">
	<br> 集計期間間隔 :
	<select name="duration" id="duration">
		<option value="1">1日間</option>
		<option value="3">3日間</option>
		<option value="7" selected>1週間</option>
		<option value="10">10日間</option>
		<option value="30">1ヵ月</option>
		<option value="90">3ヵ月</option>
		<option value="365">1年間</option>
		<option value="1095">3年間</option>
	</select>
	<br>
	<button id="addData">データ追加</button>
	<button id="resetData">リセット</button>

	<script>
		var ctx = document.getElementById('myChart').getContext('2d');
		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type : 'line',

			// The data for our dataset
			data : {
				labels : [],
				datasets : [ {
					label : 'My First dataset',
					backgroundColor : 'rgb(255, 99, 132)',
					borderColor : 'rgb(255, 99, 132)',
					data : [],
					fill : false
				} ]
			},

			// Configuration options go here
			options : {
				responsive : true
			}
		});

		// 分割数
		var dnum = document.getElementById('divnum').value;
		// シフト数
		var snum = 0;
		// IDリスト
		var idlist = [ 1 ];

		// 更新処理
		function updateChart() {
			var url = "/charttest/receive";
			var request = {
				"divnum" : $("#divnum").val(),
				"shiftnum" : snum,
				"duration" : $("#duration").val(),
			};
			var i = 0;
			idlist.forEach(function(id) {
				request["idlist" + String(i)] = id;
				i++;
			});
			var callback = function(response) {
				// 初期化
				// 				chart.data.labels.splice(0, chart.data.labels.length);
				// 				chart.data.datasets.forEach(function(dataset) {
				// 					dataset.data.splice(0, dataset.data.length);
				// 				});

				// 				response.forEach(function(resset) {
				// 					// データ追加
				// 					chart.data.labels.push(resset["label"]);
				// 					chart.data.datasets.forEach(function(dataset) {
				// 						dataset.data.push(resset["value"]);
				// 					});
				// 				});
				var i = 0, j = 0;
				response.forEach(function(dataset) {
					dataset.forEach(function(resset) {
						// データ追加
						chart.data.labels[i] = resset["label"];
						// 						chart.data.datasets.forEach(function(dataset) {
						// 							dataset.data[i] = resset["value"];
						// 						});
						chart.data.datasets[j].data[i] = resset["value"];
						i++;
					});
					j++;
					i = 0;
				});

				chart.update();
			};
			$.ajaxSetup({
				traditional : true
			});
			$.post(url, request, callback);
		}
		updateChart();

		// ボタン操作
		$("#shift_left").on("click", function() {
			snum = snum + 1;
			updateChart();
		});
		$("#shift_right").on("click", function() {
			if (snum > 0) {
				snum = snum - 1;
				updateChart();
			}
		});
		$("#divnum").on("change", function() {
			snum = 0;

			// データの数を調整する
			var numDiv = document.getElementById("divnum").value;
			chart.data.labels.length = numDiv;
			chart.data.datasets.forEach(function(dataset) {
				dataset.data.length = numDiv;
			});

			updateChart();
		});
		$("#duration").on("change", function() {
			snum = 0;
			updateChart();
		});
		$("#addData").on("click", function() {
			idlist.push(idlist[idlist.length - 1] + 1);
			var color = (Math.random() * 0xFFFFFF | 0).toString(16);
			var newColor = "#" + ("000000" + color).slice(-6);
			var newDataset = {
				label : 'Dataset ' + chart.data.datasets.length,
				backgroundColor : newColor,
				borderColor : newColor,
				data : [],
				fill : false
			};
			chart.data.datasets.push(newDataset);
			updateChart();
		});
		$("#resetData").on("click", function() {
			// データセットを最初の1つにする
			idlist.splice(1, idlist.length - 1);
			chart.data.datasets.splice(1, chart.data.datasets.length - 1);
			// オプションの初期化
			snum = 0;
			document.getElementById("divnum").value = 10;
			document.getElementById("duration").value = 7;
			chart.data.labels.length = 10;
			chart.data.datasets[0].data.length = 10;
			updateChart();
		});
	</script>


</body>
</html>