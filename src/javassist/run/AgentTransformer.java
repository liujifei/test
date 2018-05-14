package javassist.run;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.LoaderClassPath;


public class AgentTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        /**
         * 此处使用javassist API对classfileBuffer进行修改
         */
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new LoaderClassPath(loader));
        try {
            CtClass cls = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            
            //获取构造函数数组
            CtConstructor[] ccs = cls.getDeclaredConstructors();
            //构造函数方法体开始时添加的代码
            String codeStrBefore = "System.out.println(\"This code is inserted before constructor "+className+"\");";
            System.out.println(codeStrBefore);
            //构造函数方法体结束前添加的代码
            String codeStrAfter = "System.out.println(\"This code is inserted after constructor "+className+"\");";
            System.out.println(codeStrAfter);
            for (CtConstructor cc : ccs) {
                cc.insertBefore(codeStrBefore);
                cc.insertAfter(codeStrAfter, true);
            }
            return cls.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
