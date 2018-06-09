'''
Created on 2018/06/09

@author: umino
'''

import requests
from bs4 import BeautifulSoup

r = requests.get("https://news.yahoo.co.jp/")

soup = BeautifulSoup(r.content, "html.parser")

# print(soup.select("p.ttl"))
for i in soup.select("p.ttl"):
    print(i.getText())