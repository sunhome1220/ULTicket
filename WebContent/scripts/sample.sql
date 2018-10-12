--由票根號查出某人索票資料
SELECT * FROM proctick where tickname+ticktel in(
SELECT tickname+ticktel FROM proctick where tickid=22409)

--以某張回條查出該票索票人索得票出席狀況
SELECT * FROM showtick where tickid in(
SELECT tickid FROM proctick where tickname+ticktel in(
SELECT tickname+ticktel FROM proctick where tickid=22409))

SELECT count(*) FROM proctick where tickname+ticktel in(
SELECT tickname+ticktel FROM proctick where tickid=22409)
group by tickname, ticktel