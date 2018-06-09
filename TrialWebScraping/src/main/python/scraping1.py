'''
Created on 2018/06/08

@author: umino
'''
import requests

r = requests.get('https://news.yahoo.co.jp')

print("header: {0}".format(r.headers))
print("--------")
print("encoing: {0}".format(r.encoding))
print("content: {0}".format(r.content))