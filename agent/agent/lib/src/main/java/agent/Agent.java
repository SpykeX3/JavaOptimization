package agent;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class Agent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        List<String> classes = new ArrayList<>();
        instrumentation.addTransformer(new TPTransformer(classes));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Loaded " + classes.size() + " classes");
            System.out.println();
            classes.forEach(System.out::println);
        }));
    }


}
