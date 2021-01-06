package programmingtest.interpreter.service.antlr;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import programmingtest.interpreter.domain.CompilationError;

import java.util.ArrayList;
import java.util.List;

public class ErrorListener extends BaseErrorListener {
    private final String sourceName;
    private final List<CompilationError> errors;

    public ErrorListener(String sourceName) {
        this.sourceName = sourceName;
        this.errors = new ArrayList<>();
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        if (e instanceof NoViableAltException && offendingSymbol instanceof Token) {
            final Token offendingToken = (Token) offendingSymbol;
            errors.add(CompilationError.fromToken(
                    offendingToken,
                    "Syntax Error At " + offendingToken.getText(),
                    sourceName));
        } else if (offendingSymbol instanceof Token) {
            final Token offendingToken = (Token) offendingSymbol;
            errors.add(CompilationError.fromToken(
                    offendingToken,
                    msg,
                    sourceName));
        } else {
            super.syntaxError(
                    recognizer,
                    offendingSymbol,
                    line,
                    charPositionInLine,
                    msg,
                    e);
        }
    }
}
