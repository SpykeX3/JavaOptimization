package agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class TPTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(final ClassLoader loader,
                            final String className,
                            final Class<?> classBeingRedefined,
                            final ProtectionDomain protectionDomain,
                            final byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;
        //System.out.println(className);
        if ("ru.fit.javaperf.TransactionProcessor".equals(className.replaceAll("/", "."))) {
            try {
                ClassPool cPool = ClassPool.getDefault();
                cPool.insertClassPath(new LoaderClassPath(loader));
                CtClass ctClass = cPool.get("ru.fit.javaperf.TransactionProcessor");
                CtField avgField = CtField.make("static long avg = 0L;", ctClass);
                ctClass.addField(avgField);
                CtField countField = CtField.make("static long cnt = 0L;", ctClass);
                ctClass.addField(countField);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getName().equals("processTransaction")) {
                        try {
                            method.addLocalVariable("st", CtClass.longType);
                            method.insertBefore("txNum+=99;\n" +
                                    "st = System.currentTimeMillis();");
                            method.insertAfter(
                                    "avg = (System.currentTimeMillis() - st + avg * cnt) / (cnt + 1);\n" +
                                            "cnt++;");
                        } catch (CannotCompileException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (method.getName().equals("main")) {
                        method.insertAfter("System.out.println(\"Average time is \"+avg+\" ms\");");
                    }
                }
                try {
                    byteCode = ctClass.toBytecode();
                    ctClass.detach();
                    return byteCode;
                } catch (IOException e) {
                    ctClass.detach();
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return byteCode;
    }
}
