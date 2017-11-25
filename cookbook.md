在我写的Character class 中，android有一个SQLiteOpenHelper类可以进行相关的数据库操作。

在你想调用数据库进行操作的.java文件中，需要使用一下语句调用本地的数据库

```java
mydb = new Character(this);
```

我的getCharacterData 函数返回的Cursor类型实际上是我从数据库调用select语句后得到的那些数据，可以像一下语句一样提取其中的数据， 可以调用Cursor.isNull()看看是否为空，所以也可用于搜索：

```java
Cursor res = mydb.getCharacterData(character_name);
int sex = res.getInt(res.getColumnIndex(Character.SEX));
String birthday = res.getString(res.getColumnIndex(Character.BIRTHDAY))；
//There can some familiar code to get the data
if (res.isNull()) {
  boolean noSuchCharacter = true;
}
if (!res.isClosed()) {
  res.close();
}
```

由于使用的是android自身所有的类，我看了定义，他每次都会先检查本地是否有对应的数据库，如果有则打开，没有则创建，所以数据得以保持一致。