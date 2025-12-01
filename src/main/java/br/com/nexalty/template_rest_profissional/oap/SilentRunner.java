package br.com.nexalty.template_rest_profissional.oap;

public class SilentRunner {

    public static void run(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            // Optional: log or ignore
        }
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

}
