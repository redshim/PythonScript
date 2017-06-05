#!/usr/bin/env python
# -*- coding: utf-8 -*-

from konlpy.corpus import kobill    # Docs from pokr.kr/bill
files_ko = kobill.fileids()         # Get file ids
doc_ko = kobill.open('test.txt').read() 
# news.txt는 http://boilerpipe-web.appspot.com/ 를 통해 포탈뉴스 부분에서 긁어왔다.
# news.txt 는  konlpy의 corpus아래에 있는 kobill directory에 미리 저장되어있어야 한다. 
# /Library/Python/2.7/site-packages/konlpy/data/corpus/kobill

2.Tokenize (의미단어 검출)
from konlpy.tag import Twitter; t = Twitter()
tokens_ko = t.morphs(doc_ko)

3. Token Wapper 클래스 만들기(token에대해 이런 저런 처리를 하기 위해)
import nltk
ko = nltk.Text(tokens_ko, name='뉴스')

4. 토근 정보및 단일 토큰 정보 알아내기
print(len(ko.tokens))       # returns number of tokens (document length)
print(len(set(ko.tokens)))  # returns number of unique tokens
ko.vocab()                  # returns frequency distribution



#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
reload(sys)

sys.setdefaultencoding('utf-8')

#load
from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]

#Tokenize
from konlpy.tag import Twitter; t = Twitter()
pos = lambda d: ['/'.join(p) for p in t.pos(d)]
texts_ko = [pos(doc) for doc in docs_ko]


#train
from gensim.models import word2vec
wv_model_ko = word2vec.Word2Vec(texts_ko)
wv_model_ko.init_sims(replace=True)
 
wv_model_ko.save('ko_word2vec_e.model')

#test
print(wv_model_ko.most_similar(pos('메르스')))



#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
reload(sys)

sys.setdefaultencoding('utf-8')

#load



from konlpy.corpus import kobill
docs_ko = [kobill.open(i).read() for i in kobill.fileids()]




#Tokenize
from konlpy.tag import Twitter; t = Twitter()
pos = lambda d: ['/'.join(p) for p in t.pos(d)]
texts_ko = [pos(doc) for doc in docs_ko]


#train
from gensim.models import word2vec
wv_model_ko = word2vec.Word2Vec(texts_ko)
wv_model_ko.init_sims(replace=True)

wv_model_ko.save('ko_word2vec_e.model')

#test
print(wv_model_ko.most_similar(pos('메르스')))
