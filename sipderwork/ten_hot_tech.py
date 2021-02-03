#
# # coding= utf-8
# from pymongo import MongoClient
# from sklearn.feature_extraction.text import CountVectorizer
# import jieba
# import numpy as np
# import re
# import sys
#
# conn = MongoClient('mongodb://localhost:27017/')
# db = conn.article
#
# def get_data(city,key):
#     x = db.lagouInfo.find({'city':city,'key':key})
#     ls = []
#     for i in x:
#         ls.append(i["require"])
#     return ls
#
#
# def stop_list(stop_word_list,ls):
#     # 匹配英文分词
#     enPattern = re.compile('[a-zA-Z]+')
#     # 匹配中文分词
#     zhPattern = re.compile(u'[\u4e00-\u9fa5]+')
#
#     word = []
#     for i in ls:
#         segments = []
#         segs = jieba.cut(i)
#         for seg in segs:
#             if enPattern.search(seg):
#                 segments.append(seg)
#         i = ' '.join(segments)
#         word.append(i)
#
#     #实例化CountVectorizer转换器，并传入停用词词表
#     transform = CountVectorizer(stop_words=stop_word_list)
#     # 2.调用fit_transform()
#     X = transform.fit_transform(word)
#     #获取特征词列表
#     feature_name = transform.get_feature_names()
#     #将X矩阵化
#     h = X.toarray()
#
#     w = []
#     wIndex = 0
#     #获取关键词在所有文章中的出现次数，即将二维列表逐列相加，将关键字的出现次数添加到列表，（按照词表的顺序）
#     for i in range(0, len(h[0])):
#         for j in range(len(h)):
#             wIndex += h[j][i]
#         w.append(wIndex)
#         wIndex = 0
#
#     #将关键词出现次数列表排序，并取出前十
#     top_ten_index = np.argsort(np.array(w))[-10:]
#
#     #将排序的次数列表对应关键词列表
#     top_ten_name = []
#     top_ten_num = []
#     for i in top_ten_index:
#         #mongodb无法存储一个对象
#         # info = {'name': feature_name[i], 'num': w[i]}
#         top_ten_name.append(feature_name[i])
#         #mongodb无法保存整数型数组
#         top_ten_num.append(str(w[i]))
#
#     return {'name':top_ten_name,'num':top_ten_num}
#
# if __name__ == '__main__':
#     city = sys.argv[1]
#     key = 'java' if sys.argv[2]=='' else sys.argv[2]
#     stop_word_lists = [key]
#     text_list = get_data(city,key)
#     hot = stop_list(stop_word_lists,text_list)
#
#     if sys.argv[3]== 'true':
#         stop_word_lists.extend(db.technology.find_one({'key':key,'city':city})['stop_list'])
#         hot = stop_list(stop_word_lists, text_list)
#         print(hot)
#         db.technology.update_one({'key':key,'city':city},{"$set":{ 'hot_name': hot['name'],'hot_num':hot['num']}})
#     else:
# =======
# coding= utf-8
from pymongo import MongoClient
from sklearn.feature_extraction.text import CountVectorizer
import jieba
import numpy as np
import re
import sys

conn = MongoClient('mongodb://60.205.204.72:27017/')
db = conn.article

def get_data(city,key):
    x = db.lagouInfo.find({'city':city,'key':key})
    ls = []
    for i in x:
        ls.append(i["require"])
    return ls


def stop_list(stop_word_list,ls):
    # 匹配英文分词
    enPattern = re.compile('[a-zA-Z]+')
    # 匹配中文分词
    zhPattern = re.compile(u'[\u4e00-\u9fa5]+')

    word = []
    for i in ls:
        segments = []
        segs = jieba.cut(i)
        for seg in segs:
            if enPattern.search(seg):
                segments.append(seg)
        i = ' '.join(segments)
        word.append(i)

    #实例化CountVectorizer转换器，并传入停用词词表
    transform = CountVectorizer(stop_words=stop_word_list)
    # 2.调用fit_transform()
    X = transform.fit_transform(word)
    #获取特征词列表
    feature_name = transform.get_feature_names()
    #将X矩阵化
    h = X.toarray()

    w = []
    wIndex = 0
    #获取关键词在所有文章中的出现次数，即将二维列表逐列相加，将关键字的出现次数添加到列表，（按照词表的顺序）
    for i in range(0, len(h[0])):
        for j in range(len(h)):
            wIndex += h[j][i]
        w.append(wIndex)
        wIndex = 0

    #将关键词出现次数列表排序，并取出前十
    top_ten_index = np.argsort(np.array(w))[-10:]

    #将排序的次数列表对应关键词列表
    top_ten_name = []
    top_ten_num = []
    for i in top_ten_index:
        #mongodb无法存储一个对象
        # info = {'name': feature_name[i], 'num': w[i]}
        top_ten_name.append(feature_name[i])
        #mongodb无法保存整数型数组
        top_ten_num.append(str(w[i]))

    return {'name':top_ten_name,'num':top_ten_num}

def start(city,key,flag):
    key = 'java' if key == '' else key
    stop_word_lists = [key]
    text_list = get_data(city, key)
    hot = stop_list(stop_word_lists, text_list)

    if flag == 'true':
        stop_word_lists.extend(db.technology.find_one({'key': key, 'city': city})['stop_list'])
        hot = stop_list(stop_word_lists, text_list)
        print(hot)
        db.technology.update_one({'key': key, 'city': city}, {"$set": {'hot_name': hot['name'], 'hot_num': hot['num']}})
    else:
        db.technology.insert_one(
            {'key': key, 'city': city, 'hot_name': hot['name'], 'hot_num': hot['num'], 'stop_list': []})

# if __name__ == '__main__':
#     city = sys.argv[1]
#     key = 'java' if sys.argv[2]=='' else sys.argv[2]
#     stop_word_lists = [key]
#     text_list = get_data(city,key)
#     hot = stop_list(stop_word_lists,text_list)
#
#     if sys.argv[3]== 'true':
#         stop_word_lists.extend(db.technology.find_one({'key':key,'city':city})['stop_list'])
#         hot = stop_list(stop_word_lists, text_list)
#         print(hot)
#         db.technology.update_one({'key':key,'city':city},{"$set":{ 'hot_name': hot['name'],'hot_num':hot['num']}})
#     else:
#         db.technology.insert_one({'key':key,'city':city,'hot_name':hot['name'],'hot_num':hot['num'],'stop_list':[]})