package enumTest;


public enum ProcessType {
    L("启动器"),
    T("url任务抓取进程"),
    B("urlBody解析进程"),
    R("存入kafka任务进程"),
    C("启动存入采集规则进程"),
    E("启动内容抽取进程");
   public String desc;
   ProcessType(String description){
       this.desc = description;
   }

}
