package javassist;

import java.io.IOException;

public class TestJssistTiming {

    public static void main(String argv[]) {
        try {
            String classname = "javassist.Test";
            // 获取class文件
            CtClass clas = ClassPool.getDefault().get(classname);

            if (clas == null) {
                System.out.println("classname" + clas + "  not found.");
            } else {
                // 调用方法
                addTiming(clas, "method");
                //insertPhrase(clas, "method");
                clas.writeFile();
            }
        } catch (CannotCompileException ex) {
            ex.printStackTrace();
        } catch (NotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static void insertPhrase(CtClass cct, String method) throws NotFoundException, CannotCompileException {
        // 获取方法信息,如果方法不存在，则抛出异常
        CtMethod ctMethod = cct.getDeclaredMethod(method);
        ctMethod.insertAt(14, "System.out.println(s);");
    }
    private static void addTiming(CtClass cct, String method) throws NotFoundException, CannotCompileException {
        // 获取方法信息,如果方法不存在，则抛出异常
        CtMethod ctMethod = cct.getDeclaredMethod(method);
        // 将旧的方法名称进行重新命名
        String nname = method + "$impl";
        ctMethod.setName(nname);
        // 方法的副本，采用过滤器的方式
        CtMethod newCtMethod = CtNewMethod.copy(ctMethod, method, cct, null);

        // 为该方法添加时间过滤器来计算时间，并判断获取时间的方法是否有返回值
        String type = ctMethod.getReturnType().getName();
        StringBuffer body = new StringBuffer();
        body.append("{\n long start = System.currentTimeMillis();\n");

        body.append("System.out.println(\" " + nname + "_in_11\");");
        // 返回值类型不同
        if (!"void".equals(type)) {
            body.append(type + " result = ");
        }
        // 可以通过$$将传递给拦截器的参数，传递给原来的方法
        body.append(nname + "($$);\n");
        // 输出方法运行时间差
        // body.append("System.out.println(\"Call to method " + nname +
        // " took \" + \n (System.currentTimeMillis()-start) + " + "\" ms.\");\n");
        body.append("System.out.println(\" " + nname + "_out_11\");");
        if (!"void".equals(type)) {
            body.append("return result;\n");
        }

        body.append("}");
        // 替换拦截器方法的主体内容，并将该方法添加到class之中
        newCtMethod.setBody(body.toString());
        cct.addMethod(newCtMethod);

        // 输出拦截器的代码块
        System.out.println("拦截器方法的主体:");
        System.out.println(body.toString());
    }
}
