package com.manga.sql;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {

    private Lexer lexer;

    public QueryParser(String input) {
        lexer = new Lexer(input);
    }

    public List<Token> parse() {
        return lexer.tokenize();
    }


    public class Token {
        enum Type { SELECT, INSERT, INTO, FROM, WHERE, IDENTIFIER, NUMBER, STRING, COMMA, EQUALS, LPAREN, RPAREN, SEMICOLON, UNKNOWN, ASTERIX, TABLE, DATABASE }

        Type type;
        String value;

        Token(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("Token[type=%s, value='%s']", type, value);
        }

        public Type getType() {
            return this.type;
        }

        public String getValue() {
            return this.value;
        }
    }

    public class Lexer {
        private final String input;
        private int position;

        Lexer(String input) {
            this.input = input;
            this.position = 0;
        }

        private char peek() {
            return position < input.length() ? input.charAt(position) : '\0';
        }

        private char advance() {
            return position < input.length() ? input.charAt(position++) : '\0';
        }

        List<Token> tokenize() {
            List<Token> tokens = new ArrayList<>();
            while (position < input.length()) {
                char current = peek();
                if (Character.isWhitespace(current)) {
                    advance();
                    continue;
                } else if (Character.isLetter(current)) {
                    tokens.add(identifier());
                } else if (Character.isDigit(current)) {
                    tokens.add(number());
                } else {
                    switch (current) {
                        case ',':
                            tokens.add(new Token(Token.Type.COMMA, String.valueOf(advance())));
                            break;

                        case '=':
                            tokens.add(new Token(Token.Type.EQUALS, String.valueOf(advance())));
                            break;

                        case '*':
                            tokens.add(new Token(Token.Type.ASTERIX, String.valueOf(advance())));
                            break;
                        
                        case '(':
                            tokens.add(new Token(Token.Type.LPAREN, String.valueOf(advance())));
                            break;

                        case ')':
                            tokens.add(new Token(Token.Type.RPAREN, String.valueOf(advance())));
                            break;

                        case ';':
                            tokens.add(new Token(Token.Type.SEMICOLON, String.valueOf(advance())));
                            break;

                        case '\'':
                            tokens.add(string());
                        default:
                            tokens.add(new Token(Token.Type.UNKNOWN, String.valueOf(advance())));
                            break;
                    }
                }
            }
            return tokens;
        }

        private Token identifier() {
            StringBuilder sb = new StringBuilder();
            char current = peek();

            while (Character.isLetterOrDigit(current) || current == '_') {
                sb.append(advance());
                current = peek();
            }

            String word = sb.toString().toLowerCase();
            switch (word) {
                case "select":
                    return new Token(Token.Type.SELECT, word);
                case "insert":
                    return new Token(Token.Type.INSERT, word);
                case "into":
                    return new Token(Token.Type.INTO, word);
                case "from":
                    return new Token(Token.Type.FROM, word);
                case "where":
                    return new Token(Token.Type.WHERE, word);
                case "table":
                    return new Token(Token.Type.TABLE, word);
                case "database":
                    return new Token(Token.Type.DATABASE, word);
                default:
                    return new Token(Token.Type.IDENTIFIER, word);
            }
        }

        private Token number() {
            StringBuilder sb = new StringBuilder();
            char current = peek();
            while (Character.isDigit(current)) {
                sb.append(advance());
                current = peek();
            }

            return new Token(Token.Type.NUMBER, sb.toString());
        }

        private Token string() {
            StringBuilder sb = new StringBuilder();
            advance();
            char current = peek();
            while (current != '\'' && current != '\0') {
                sb.append(advance());
                current = peek();
            }
            advance();
            return new Token(Token.Type.STRING, sb.toString());
        }
    }
}
