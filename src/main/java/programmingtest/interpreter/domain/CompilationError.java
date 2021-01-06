package programmingtest.interpreter.domain;

import org.antlr.v4.runtime.Token;

public class CompilationError {
    private final int lineNumber;
    private final int charAt;
    private final String detailMessage;
    private final String sourceName;

    public CompilationError(int lineNumber, int charAt, String detailMessage, String sourceName) {
        this.lineNumber = lineNumber;
        this.charAt = charAt;
        this.detailMessage = detailMessage;
        this.sourceName = sourceName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCharAt() {
        return charAt;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getSourceName() {
        return sourceName;
    }

    @Override
    public String toString() {
        return "Compilation Error: " + sourceName + " (" + lineNumber + "," + charAt +") " + detailMessage;
    }

    public static CompilationError fromToken(Token token, String detailMessage, String sourceName) {
        return new CompilationError(
                token.getLine(),
                token.getCharPositionInLine(),
                detailMessage,
                sourceName);
    }
}

