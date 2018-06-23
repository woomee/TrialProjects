
# 概要
- WebScrapingを行うトライアル。Pythonのrequest, BeautifulSoup4, pandas.read_html()あたりを利用している。


# ポイント
- <TABLE>タグはpandas.read_html()で取得できる
- HTTP通信が失敗する場合があるため、リクエスト後は少しsleep()を入れた方がより
-- リクエストして取得したhtmlはローカルにキャッシュして利用した方がよい


# 参照
- Pandas.read_html()を使って<TABLE>タグをDataFrameとして取得
- https://note.nkmk.me/python-pandas-web-html-table-scraping/


