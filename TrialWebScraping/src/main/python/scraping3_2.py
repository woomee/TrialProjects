'''

Pandasを使ってtableを取得(2)
https://note.nkmk.me/python-pandas-web-html-table-scraping/

Created on 2018/06/09

@author: umino
'''

import pandas as pd

url = 'https://ja.wikipedia.org/wiki/Python'
dfs = pd.read_html(url)

print("# 表の数")
print(len(dfs))

print("# 'リリース日'を含む表のみを取得した表の数")
dfs2 = pd.read_html(url, match='リリース日')
print(len(dfs2))
for i in range(0, len(dfs2)):
    print(dfs2[i])

print("# 先頭行を列インデクスにする")
dfs3 = pd.read_html(url, match='リリース日', header=0)
for i in range(0, len(dfs3)):
    print(dfs3[i])

print("# 行インデクスでマージして横に展開")
dfs4 = pd.merge(dfs3[0], dfs3[1], left_index=True, right_index=True)
print(dfs4)