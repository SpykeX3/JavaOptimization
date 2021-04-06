package agent;

import javassist.*;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

public class TPTransformer implements ClassFileTransformer {

    private final List<String> classes;

    public TPTransformer(List<String> classes) {
        this.classes = classes;
    }

    @Override
    public byte[] transform(final ClassLoader loader,
                            final String className,
                            final Class<?> classBeingRedefined,
                            final ProtectionDomain protectionDomain,
                            final byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;
        classes.add(className);
        if ("ru.fit.javaperf.TransactionProcessor".equals(className.replaceAll("/", "."))) {
            try {
                ClassPool cPool = ClassPool.getDefault();
                cPool.insertClassPath(new LoaderClassPath(loader));
                CtClass ctClass = cPool.get("ru.fit.javaperf.TransactionProcessor");
                CtField avgField = CtField.make("static long avg = 0L;", ctClass);
                ctClass.addField(avgField);
                CtField countField = CtField.make("static long cnt = 0L;", ctClass);
                ctClass.addField(countField);
                CtField minField = CtField.make("static long min = 0x7FFFFFFFFFFFFFFFL;", ctClass);
                ctClass.addField(minField);
                CtField maxField = CtField.make("static long max = 0L;", ctClass);
                ctClass.addField(maxField);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.getName().equals("processTransaction")) {
                        try {
                            method.addLocalVariable("st", CtClass.longType);
                            method.addLocalVariable("fin", CtClass.longType);
                            method.insertBefore("txNum+=99;\n" +
                                    "st = System.currentTimeMillis();");
                            method.insertAfter(
                                    "fin = System.currentTimeMillis();\n" +
                                            "avg = (fin - st + avg * cnt) / (cnt + 1);\n" +
                                            "if (fin - st > max) max = fin-st;\n" +
                                            "if (fin - st < min) min = fin-st;\n" +
                                            "cnt++;");
                        } catch (CannotCompileException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (method.getName().equals("main")) {
                        method.insertAfter("System.out.println(\"Average time is \"+avg+\" ms\");\n" +
                                "System.out.println(\"Maximum time is \"+max+\" ms\");\n" +
                                "System.out.println(\"Minimum time is \"+min+\" ms\");\n");
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
