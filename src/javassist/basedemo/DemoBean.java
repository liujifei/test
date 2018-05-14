package javassist.basedemo;

import javassist.Test;


public class DemoBean extends Test implements Runnable{

    private String id;
    private String name;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    public String hello(String name){
        System.out.println("HELLO " + name);
        return "Hello " + name;
    }
}
