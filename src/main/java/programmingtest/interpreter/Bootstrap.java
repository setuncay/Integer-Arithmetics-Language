package programmingtest.interpreter;

import io.vavr.control.Either;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import programmingtest.interpreter.domain.CompilationError;
import programmingtest.interpreter.domain.Output;
import programmingtest.interpreter.service.ArithmeticExecutor;

import java.util.List;

import static java.lang.System.exit;

@SpringBootApplication
public class Bootstrap implements CommandLineRunner {
    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Bootstrap.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.out.println("You must provide the path to your program file as an argument");
            exit(1);
        } else {
            final ArithmeticExecutor executor = new ArithmeticExecutor();
            try {
                final Either<List<CompilationError>, List<Output>> result =  executor.run(args[0]);
                displayResult(result);
            } catch (Exception ex) {
                System.out.println("Error in Running Integer Arithmetic Program:");
                System.out.println(ex.getMessage());
            }
        }
    }

    private void displayResult(Either<List<CompilationError>, List<Output>> result) {
        if (result.isLeft()) {
            result.getLeft().forEach(System.out::println);
        } else {
            result.right().get().forEach(System.out::println);
        }
    }
}
