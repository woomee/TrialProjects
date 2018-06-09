'''

Pandasを使ってtableを取得
https://note.nkmk.me/python-pandas-web-html-table-scraping/

Created on 2018/06/09

@author: umino
'''

import pandas as pd

url = 'https://info.finance.yahoo.co.jp/ranking/?kd=4'
dfs = pd.read_html(url)

# 先頭5行を表示
print("先頭の5行 (head())")
print(dfs[0].head())


# カラム一覧を表示
## 最後がUnnamedになっているので少しおかしい
print("カラム一覧")
print(dfs[0].columns)

# 名称と時価総額を表示するが、時価総額はおかしい
print("名称と時価総額")
print(dfs[0][[ '名称',  '時価総額（百万円）',]].head())

# '時刻'が抜けているので追加する
dfs[0].columns = ['順位', 'コード', '市場', '名称', '時刻', '取引値', '発行済み株式数', '時価総額（百万円）', '単元株数', '掲示板']
print(dfs[0][[ '名称',  '時価総額（百万円）',]].head())


