--K0000001,차선이탈경보 문제
--K0000002,차선이탈경보 오류
--K0000003,차선이탈경보 불만
--K0000004,LDWS 불량
--K0000005,LDWS 오류
--K0000006,헤드업디스플레이 문제
--K0000007,헤드업디스플레이 오류
--K0000008,헤드업디스플레이 불만
--K0000009,HUD 불량
--K0000010,HUD 오류
--K0000011,자동주차시스템 문제
--K0000012,자동주차시스템 오류
--K0000013,자동주차시스템 불만
--K0000014,SPAS 문제
--K0000015,사각지대 경보 문제
--K0000016,사각지대 경보 오류
--K0000017,사각지대 경보 불만
--K0000018,BSD 불량
--K0000019,BSD 오류
--K0000020,배터리 불량


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