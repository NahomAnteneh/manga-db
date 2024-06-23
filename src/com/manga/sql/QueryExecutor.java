package com.manga.sql;

import java.util.List;
import com.manga.sql.QueryParser.Token;

public class QueryExecutor {

    private List<Token> tokens;

    public QueryExecutor(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    public void execute() {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Query Tokens cannot be empty.");
        }

        Token firstToken = tokens.get(0);
        switch (firstToken.getType()) {
            case SELECT:
                // tokens.remove(0);
                executeSelect();
                break;
            case INSERT:
                // tokens.remove(0);
                executeInsert();
                break;
            case DELETE:
                // tokens.remove(0);
                executeDelete();
                break;
            default:
                throw new IllegalArgumentException("Unsupported query type: " + firstToken.getValue());
        }
    }

    // This needs a connection to the database
    private void executeSelect() {
        for (Token token : tokens) {
            switch (token.getType()) {
                case ASTERIX:
                    break;
                case FROM:
                    break;
                case IDENTIFIER:
                    break;
                default:
                    break;
            }
        }
    }

    private void executeInsert() {
        
    }

    private void executeDelete() {
        
    }
}
