/**
 * Metafox - A DYNAMIC, INTERPRETED, META-PROGRAMMING LANGUAGE, RUN AND
 * SUPPORTED BY ITS OWN INDEPENDENT INTERPRETER.
 *
 * UNIVERSITY OF CRETE (UOC)
 *
 * COMPUTER SCIENCE DEPARTMENT (UOC)
 *
 * https://www.csd.uoc.gr/
 *
 * CS-540 ADVANCED TOPICS IN PROGRAMMING LANGUAGES DEVELOPMENT
 *
 * LICENCE: This file is part of Metafox. Metafox is free: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * Metafox is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Metafox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016
 *
 */
package interpreter.lexer;

import static java.lang.System.out;
import java.util.*;
import java_cup.runtime.*;
import interpreter.parser.sym;

/**
 * LEXER
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
%%

%class lexer
%unicode
%public
%line
%column
%cup
%eofval{
    return symbol(sym.EOF);
%eofval}

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline+1, yycolumn+1);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
%}

%eof{
    if(yystate()==STRING) 
        throw new Error("@Lexer: Unterminated string: "+ string.toString() + " in line "+ stringLine+".");
    
    if(openComments>0){
        for (int i = commentsState.size() - 1 ; i >= 0 ; i--) {
            if (commentsState.get(i).equals("Unclosed")) {
                throw new Error("@Lexer: Unclosed comment in line: "+ commentsPositions.get(i)+".");
            }
        }
    }

    commentsState.clear();
    commentsPositions.clear();
%eof}

%eofclose
%{
    StringBuffer string = new StringBuffer(); /*string buffer to store dynamic strings*/
    int stringLine;
    List <Integer> commentsPositions = new ArrayList<Integer>(); /*store dynamic comment positins*/
    List <String> commentsState = new ArrayList<String>(); 
    int openComments=0;
%}

letter         = [A-Za-z]
intConst       = [0-9]+
identifier     = ({letter}|"_")+({letter}|{intConst}|"_")*
realConst      = [0-9]+\.[0-9]+
LineTerminator = \r|\n|\r\n
whiteSpace     = {LineTerminator} | [ \t\f] | " "
%state STRING
%state COMMENT
%state LINECOMMENT

%%

<YYINITIAL> {
  /*keywords*/
  "and"       { return symbol(sym.LOGIC_AND);}
  "not"       { return symbol(sym.NOT);}
  "or"        { return symbol(sym.LOGIC_OR);}
  "true"      { return symbol(sym.TRUE);}
  "false"     { return symbol(sym.FALSE);}
  "null"      { return symbol(sym.NULL);}
  "local"     { return symbol(sym.LOCAL);}
  "if"        { return symbol(sym.IF);}
  "else"      { return symbol(sym.ELSE);}
  "while"     { return symbol(sym.WHILE);}
  "for"       { return symbol(sym.FOR);}
  "function"  { return symbol(sym.FUNCTION);}
  "return"    { return symbol(sym.RETURN);}
  "break"     { return symbol(sym.BREAK);}
  "continue"  { return symbol(sym.CONTINUE);}

  {identifier}  { return symbol(sym.ID, yytext());}
  {intConst}    { return symbol(sym.INTEGER_LITERAL, Integer.valueOf(yytext()));}
  {realConst}   { return symbol(sym.REAL_LITERAL, Double.valueOf(yytext()));}


  /*Meta operators*/
  ".<"  { return symbol(sym.QUASI_OPEN);}
  ">."  { return symbol(sym.QUASI_CLOSE);}
  ".!"  { return symbol(sym.QUASI_EXEC);}
  ".~"  { return symbol(sym.QUASI_ESC);}
  ".@"  { return symbol(sym.QUASI_RUN);}
  ".eval" { return symbol(sym.QUASI_EVAL);}
  ".#"  { return symbol(sym.QUASI_TO_TEXT);}

  /*Punctuation*/
  "{"   { return symbol(sym.OPEN_BLOCK);}
  "}"   { return symbol(sym.CLOSE_BLOCK);}
  "["   { return symbol(sym.LEFT_BRACKET);}
  "]"   { return symbol(sym.RIGHT_BRACKET);}
  "("   { return symbol(sym.LEFT_PARENTHESIS);}
  ")"   { return symbol(sym.RIGHT_PARENTHESIS);}
  ";"   { return symbol(sym.SEMICOLON);}
  ","   { return symbol(sym.COMMA);}
  "::"  { return symbol(sym.DOUBLE_COLON);}
  ":"   { return symbol(sym.COLON);}
  ".."  { return symbol(sym.DOUBLE_DOT);}
  "."   { return symbol(sym.DOT);}

  /*Operators*/
   ">="  { return symbol(sym.GREATER_OR_EQUAL);}
  "<="  { return symbol(sym.LESS_OR_EQUAL);}
  ">"   { return symbol(sym.GREATER);}
  "<"   { return symbol(sym.LESS);}
  "=="  { return symbol(sym.CMP_EQUAL);}
  "="   { return symbol(sym.ASSIGN);}
  "!="  { return symbol(sym.NOT_EQUAL);}
  "++"  { return symbol(sym.PLUS_PLUS);}
  "--"  { return symbol(sym.MINUS_MINUS);}
  "+"   { return symbol(sym.PLUS);}
  "-"   { return symbol(sym.MINUS);}
  "*"   { return symbol(sym.MUL);}
  "/"   { return symbol(sym.DIV);}
  "%"   { return symbol(sym.MOD);}
  "!"   { return symbol(sym.NOT);}

 {whiteSpace}		   { }
 
 /*String mode*/
 "\""   { 
          string.setLength(0); 
          stringLine = (yyline + 1); 
          yybegin(STRING);
  }


 /*Comment mode - Block comment*/
 "/*"   {
          openComments++; 
          commentsPositions.add(yyline+1); 
          commentsState.add("Unclosed");  
          yybegin(COMMENT);
  }


 /*Comment mode - Line comment*/
 "//"   { yybegin(LINECOMMENT); }

}

<LINECOMMENT>		{
	
  {LineTerminator}  { 
                      yybegin(YYINITIAL); 
                      // System.out.println("@Lexer: Line comment in line: " + (yyline + 1)+".");
  }

  <<EOF>>           { 
                      yybegin(YYINITIAL); 
                      // System.out.println("@Lexer: Line comment in line: " + (yyline + 1)+".");
  }

  . 				        { }

}

<STRING> {
    /*stop when " is reached (end of string)*/
    "\""    { 
              yybegin(YYINITIAL); 
              return symbol(sym.STRING_LITERAL, string.toString());
    }
   
    \\t			{ string.append("\t"); }
    \\n			{ string.append("\n"); }
    \\\"	  { string.append("\""); }
    \\\\		{ string.append("\\"); }

    {LineTerminator}    { string.append("\n"); }    

    .	                  { string.append(yytext()); }
}

<COMMENT> {
	   "/*"    { 
              openComments++; 
              commentsPositions.add(yyline+1); 
              commentsState.add("Unclosed");
      }

	   "*/"    {
              for (int i = commentsState.size() - 1 ; i >= 0 ; i--) {
                if (commentsState.get(i).equals("Unclosed")) {
                    commentsState.set(i, "Closed");
                    openComments--;
                    break;
                  }
              }

    	       if(openComments == 0)
    	         yybegin(YYINITIAL);
	   }

	   .       { }
      {whiteSpace}      { }

}

/*Error case*/
. {throw new Error((yyline+1) + ":" + (yycolumn+1) + ": illegal character <"+ yytext()+">"); }
