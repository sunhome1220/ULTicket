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
SET presentStatus= 1
where presentStatus<>1 and evid=20181103 and tickid in(select tickid from showtick where evid=20181103)

UPDATE proctick SET presentStatus= 1
where presentStatus<>1 and tickid in(select tickid from showtick)

--出席狀況清單
SELECT evid as 場編,event as 場次, team as 組別, procman as 發票人 ,procaddr as 發票地,tickid as 票號,
tickname as 索票人, ticktel as 電話,tickmemo as 備註, iif(presentStatus>0,'V','') as 出席狀況 
FROM proctick where team='合歡' and evid=20181103

--
SELECT p.evid as 場編,event as 場次, team as 組別, procman as 發票人 ,procaddr as 發票地,p.tickid as 票號,
tickname as 索票人, p.ticktel as 電話,tickmemo as 備註, iif(presentStatus>0,'V','') as 出席狀況 ,
c.calltimes 已, c.contactStatus, c.updatetime,c.lastestCallernm
FROM proctick p left join tickcomment c on p.evid=c.evid and p.tickid=c.tickid 
where team='合歡' and p.evid=20181103


update perform set perform.reqcnt=p.reqcnt
from perform left join(SELECT evid,count(*) as reqcnt FROM proctick group by evid) p on perform.evid=p.evid 

update perform set perform.showcnt=s.showcnt
from perform left join(SELECT evid,count(*) as showcnt FROM showtick group by evid) s on perform.evid=s.evid 


