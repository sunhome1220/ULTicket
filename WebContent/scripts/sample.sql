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

--更新出席狀態
UPDATE proctick
SET proctick.presentStatus= 1
where presentStatus<>1 and evid=20181014 and tickid in(select tickid from showtick where evid=20181014)

--出席狀況清單
SELECT evid as 場編,event as 場次, team as 組別, procman as 發票人 ,procaddr as 發票地,tickid as 票號,
tickname as 索票人, ticktel as 電話,tickmemo as 備註, iif(presentStatus=1,'V','') as 出席狀況 
FROM proctick where team='合歡' and evid=20181014

