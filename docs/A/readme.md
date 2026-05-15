## 命令说明

### 命令 - 编译

```
这是我本机native编译命令

$env:JAVA_HOME="C:\xyprogram\graalvm-jdk-25.0.3+9.1"
& "C:\xyprogram\JetBrains\idea-2025.3.4.win\plugins\maven\lib\maven3\bin\mvn.cmd" `
  -s D:\data\maven\settings.xml `
  native:compile `
  -DskipTests `
  2>&1 | Tee-Object -FilePath build-native.txt
  
生成的exe见excel-formula-engine.zip
```



### 命令 - 启动

```
application.yml问题没有解决，所以需要手工指定端口

$env:MICRONAUT_SERVER_PORT="8082"
& .\excel-formula-engine.exe 2>&1 | Tee-Object -FilePath "service.log" -Append
```



### 命令 - curl

```
curl -X POST http://localhost:8080/api/formula \
  -H "Content-Type: application/json" \
  -d '{"formula": "SUM(A1, A2)", "a1": 10, "a2": 20}'
```



