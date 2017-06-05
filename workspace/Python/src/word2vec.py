#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]


from konlpy.tag import Twitter; t=Twitter()
pos = lambda d: ['/'.join(p) for p in t.pos(d, stem=True, norm=True)]
texts_ko = [pos(doc) for doc in docs_ko]


#encode tokens to integers
from gensim import corpora
dictionary_ko = corpora.Dictionary(texts_ko)
dictionary_ko.save('ko.dict')  # save dictionary to file for future use

#calulate TF-IDF
from gensim import models
tf_ko = [dictionary_ko.doc2bow(text) for text in texts_ko]
tfidf_model_ko = models.TfidfModel(tf_ko)
tfidf_ko = tfidf_model_ko[tf_ko]
corpora.MmCorpus.serialize('ko.mm', tfidf_ko) # save corpus to file for future use

#train topic model
#LSI
ntopics, nwords = 3, 5
lsi_ko = models.lsimodel.LsiModel(tfidf_ko, id2word=dictionary_ko, num_topics=ntopics)
print(lsi_ko.print_topics(num_topics=ntopics, num_words=nwords))

#LDA
import numpy as np; np.random.seed(42)  # optional
lda_ko = models.ldamodel.LdaModel(tfidf_ko, id2word=dictionary_ko, num_topics=ntopics)
print(lda_ko.print_topics(num_topics=ntopics, num_words=nwords))

#HDP
import numpy as np; np.random.seed(42)  # optional
hdp_ko = models.hdpmodel.HdpModel(tfidf_ko, id2word=dictionary_ko)
print(hdp_ko.print_topics(topics=ntopics, topn=nwords))


#Scoring document
bow = tfidf_model_ko[dictionary_ko.doc2bow(texts_ko[0])]
sorted(lsi_ko[bow], key=lambda x: x[1], reverse=True)
sorted(lda_ko[bow], key=lambda x: x[1], reverse=True)
sorted(hdp_ko[bow], key=lambda x: x[1], reverse=True)


bow = tfidf_model_ko[dictionary_ko.doc2bow(texts_ko[1])]
sorted(lsi_ko[bow], key=lambda x: x[1], reverse=True)
sorted(lda_ko[bow], key=lambda x: x[1], reverse=True)
sorted(hdp_ko[bow], key=lambda x: x[1], reverse=True)

result) 아래 결과를 통해 육야휴직, 임신에 대한 글들이 대부분이라는 것을 알수 있다.

==LSI==
[u'0.527*"육아휴직/Noun" + 0.261*"만/Noun" + 0.231*"%xd7/Foreign" + 0.217*"대체/Noun" + 0.204*"고용/Noun"', u'0.455*"파견/Noun" + 0.417*"부대/Noun" + 0.266*"UAE/Alpha" + 0.246*"○/Foreign" + 0.195*"국군/Noun"', u'-0.313*"결혼/Noun" + -0.276*"손해/Noun" + -0.255*"예고/Noun" + -0.210*"ㆍ/Foreign" + -0.204*"입법/Noun”']

===LDA===
[u'0.002*육아휴직/Noun + 0.001*만/Noun + 0.001*…/Foreign + 0.001*%xd7/Foreign + 0.001*고용/Noun', u'0.001*결혼/Noun + 0.001*손해/Noun + 0.001*학위/Noun + 0.001*간호/Noun + 0.001*원사/Noun', u'0.002*육아휴직/Noun + 0.001*만/Noun + 0.001*부대/Noun + 0.001*대체/Noun + 0.001*파견/Noun’]

==HDP==
[u'topic 0: 0.004*임신/Noun + 0.003*놓다/Verb + 0.003*육아휴직/Noun + 0.003*부/Noun + 0.003*하루/Noun', u'topic 1: 0.004*공/Noun + 0.003*시험/Noun + 0.003*생략/Noun + 0.003*러버/Noun + 0.003*학년/Noun', u'topic 2: 0.004*질타/Noun + 0.003*724/Number + 0.003*열/Noun + 0.003*15/Number + 0.003*WHO/Alpha']
[출처] 한글을 이용한 데이터마이닝및 word2vec이용한 유사도 분석|작성자 IDEO