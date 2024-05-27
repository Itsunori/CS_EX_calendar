参考リンクは[こちら](https://www.javadrive.jp/servlet/schedule/)

## 使い方
```bash
docker compose up -d
```
[http://localhost:8080/](http://localhost:8080/)を開く

`schedule/WEB-INF/classes/*.java`をコンパイルするには以下のコマンドを実行
```bash
bash rehash.sh
```


## MAVEN

jsonのパースを標準ライブラリだけでやるのはちょっと...ということMAVENを入れました！(気が向いた人はぜひ自作してもらって)

インストールはお願いします🥹


いずれのコマンドもCS_EX_calendar/backendで実行してください！
コンパイル
```bash
mvn clean install
```
実行(db)
```
mvn exec:java -PjajujoDB
```
実行(backend)
```
mvn exec:java -Pbackend 
```
