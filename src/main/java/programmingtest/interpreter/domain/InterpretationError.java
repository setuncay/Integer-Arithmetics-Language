package programmingtest.interpreter.domain;

import org.antlr.v4.runtime.Token;

public class InterpretationError extends CompilationError {
    public InterpretationError(int lineNumber, int charAt, String detailMessage, String sourceName) {
        super(lineNumber, charAt, detailMessage, sourceName);
    }

    @Override
    public String toString() {
        return "Interpretation Error: " + super.getSourceName() + " (" + super.getLineNumber() + "," + super.getCharAt() +") " + super.getDetailMessage();
    }

    public static InterpretationError fromToken(Token token, String detailMessage, String sourceName) {
        return new InterpretationError(
                token.getLine(),
                token.getCharPositionInLine(),
                detailMessage,
                sourceName);
    }
}
