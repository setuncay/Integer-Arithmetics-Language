package programmingtest.interpreter.service.antlr;

import io.vavr.control.Either;
import lang.IntegerArithmeticLexer;
import lang.IntegerArithmeticParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import programmingtest.interpreter.domain.CompilationError;

import java.util.ArrayList;
import java.util.List;

public class Compiler {
    public Either<List<CompilationError>, IntegerArithmeticParser.ProgramContext> compile(String source, String sourceName) {
        final ErrorListener errorListener = new ErrorListener(sourceName);
        final IntegerArithmeticLexer lexer = new IntegerArithmeticLexer(CharStreams.fromString(source));
        lexer.removeErrorListeners();
        final IntegerArithmeticParser parser = new IntegerArithmeticParser(new CommonTokenStream(lexer));
        parser.addErrorListener(errorListener);

        final List<CompilationError> compilerErrors = new ArrayList<>();
        IntegerArithmeticParser.ProgramContext ctx = null;
        try {
            ctx = parser.program();
        } catch (Exception ex) {
            compilerErrors.add(CompilationError.fromToken(
                    parser.getCurrentToken(),
                    "An exception occurred in the compilation process",
                    sourceName));
        }

        if (!errorListener.getErrors().isEmpty() || !compilerErrors.isEmpty()) {
            final List<CompilationError> totalErrors = new ArrayList<>();
            totalErrors.addAll(errorListener.getErrors());
            totalErrors.addAll(compilerErrors);
            return  Either.left(totalErrors);
        }
        return Either.right(ctx);
    }
}
