/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kalang.compiler;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import kalang.antlr.KalangParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
/**
 *
 * @author Kason Yang <i@kasonyang.com>
 */
public class SemanticErrorException extends RuntimeException{
    
    ParseTree tree;
    
    Token token;
    private final SourceParser sourceParser;
    private String description;

    public SemanticErrorException(String description, Token token,ParseTree tree,SourceParser sourceParser) {
        super(description);
        this.tree = tree;
        this.token = token;
        this.sourceParser = sourceParser;
        this.description = description;
    }

    public ParseTree getTree() {
        return tree;
    }

    public Token getToken() {
        return token;
    }

    public SourceParser getSourceParser() {
        return sourceParser;
    }

    @Override
    public String toString() {
        return "SemanticErrorException{" + "tree=" + tree + ", token=" + token + ", sourceParser=" + sourceParser + ", description=" + description + '}';
    }

    public String getDescription() {
        return description;
    }

}
