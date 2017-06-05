##################################################################################################################
##                  주요 문제 필터링 쿼리
##                  수행 위치 : /home/6546788/Crawler_N/keyword_export
##
##
##
## 스크립트 설명 :
##       key000001.sh : 차선이탈경보(LDWS)
##       key000002.sh : 헤드업디스플레이(HUD)
##       key000003.sh : 자동주차시스템(SPAS
##       key000004.sh : 사각지대 작동 불량
##
##
##################################################################################################################
차선이탈 경보


select
              regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
            regexp_replace(
regexp_replace(contents,'  ',' ')
                       ,'   ',' ')
                       ,'    ',' ')
                       ,'     ',' ')
                       ,'      ',' ')
                       ,'       ',' ')
                       ,'        ',' ')
                       ,'         ',' ')
                       ,'          ',' ')
                       ,'\"','')
from dura_master.cr_contents where keyword in ('K0000001','K0000002','K0000003','K0000004','K0000005')
and (contents like '%문제%' or contents like '%이슈%' or contents like '%오작동%'  or contents like '%불만%')
and ( contents not like '%이뻐%' and contents not like'%좋아%' and contents not like '%만족%'
      and contents not like '%상담%'  and contents not like '%할인%' and contents not like '%팝니다%'
      and contents not like '%행사%' and contents not like '%판매%');


--문제 키워드 : 문제, 이슈, 오작동, 불만
--긍정 및 광고 키워드 : 이뻐, 좋아, 만족


select H.keyword_nm, A.* from dura_master.cr_contents as A , dura_master.keyword_head as H
where a.keyword in ('K0000006','K0000007','K0000008','K0000009','K00000010')
and A.keyword = H.keyword
and (contents like '%문제%' or contents like '%이슈%' or contents like '%오작동%'  or contents like '%불만%')
and ( contents not like '%이뻐%' and contents not like'%좋아%' and contents not like '%만족%'
      and contents not like '%상담%'  and contents not like '%할인%' and contents not like '%팝니다%'
      and contents not like '%행사%' and contents not like '%판매%');


select H.keyword_nm, A.* from dura_master.cr_contents as A , dura_master.keyword_head as H
where a.keyword in ('K0000006','K0000007','K0000008','K0000009','K00000010')
and A.keyword = H.keyword
and (contents like '%문제%' or contents like '%이슈%' or contents like '%오작동%'  or contents like '%불만%')
and ( contents not like '%이뻐%' and contents not like'%좋아%' and contents not like '%만족%'
      and contents not like '%상담%'  and contents not like '%할인%' and contents not like '%팝니다%'
      and contents not like '%행사%' and contents not like '%판매%');