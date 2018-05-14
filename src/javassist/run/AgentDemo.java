package javassist.run;

import java.lang.instrument.Instrumentation;


public class AgentDemo {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new AgentTransformer());
    }

}
