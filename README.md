# hps
2018-8-20
1. 由于新版本的mysql 8 会验证时区的合法性，所以jdbc链接串中需要明确指定：serverTimezone=GMT%2B8
修改的文件为DBConnectionFacotry.java
另外现场环境的jdbc连接方式，是直接从applicationContext-resources.xml中指定的（为啥这么做的忘了，可能当时不会弄，直接对付上的）
2. bug：维修费导出的报表将取消的报表也导出来了，原因是没有增加charge_state=0这个条件
对应修改的文件为maintain_charge_record_final.jrxml
