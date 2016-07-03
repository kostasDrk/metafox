import static java.lang.System.out;
import java.util.*;

%%
%public
%class MyLexer
%standalone
%unicode
%line
%column

%eof{
  if(yystate()==STRING) throw new Error("unterminated string: "+ string.toString() + " in line "+ stringLine);
  if(openComments>0){
  for (int i = commentsState.size() - 1 ; i >= 0 ; i--) {
           if (commentsState.get(i).equals("Unclosed")) {
              throw new Error("unclosed comment in line: "+ commentsPositions.get(i));
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

letter = [A-Za-z]
intConst = [0-9]+
identifier = {letter}+({letter}|{intConst}|"_")*
realConst = [0-9]+\.[0-9]+
keyword = "and"|"not"|"or"|"true"|"false"|"nil"|"if"|"else"|"while"|"for"|"function"|"return"|"break"|"continue"
punctuation = "\{"|"\}"|"\["|"\]"|"\("|"\)"|"\;"|"\,"|"\:"|"\::"|"\."|"\.."
operator = ">"|"<"|">="|"<="|"="|"+"|"-"|"*"|"/"|"%"|"=="|"!="|"++"|"--"|"\!"|"~"
whiteSpace = [ \t\n]+
LineTerminator = \r|\n|\r\n
%state STRING
%state COMMENT
%state LINECOMMENT

%%

<YYINITIAL> {
 {keyword}			   {System.out.println("keyword: "+ yytext() + " in line "+ (yyline + 1)); }
 {identifier}	       {System.out.println("identifier: "+ yytext() + " in line "+ (yyline + 1)); }
 {intConst}			   {System.out.println("intConst: "+ yytext() + " in line "+ (yyline + 1)); }
 {realConst}		   {System.out.println("realConst: "+ yytext() + " in line "+ (yyline + 1)); }
 {punctuation}	       {System.out.println("punctuation: "+ yytext() + " in line "+ (yyline + 1)); }
 
 {operator}			   {System.out.println("operator: "+ yytext() + " in line "+ (yyline + 1)); }
 {whiteSpace}		   { }
 
 /*String mode*/
 "\""			   { string.setLength(0); stringLine = (yyline + 1); yybegin(STRING); }
 /*Comment mode*/
 "/*"   {openComments++; commentsPositions.add(yyline+1); commentsState.add("Unclosed");  yybegin(COMMENT); }

 "//"    { yybegin(LINECOMMENT); }
}

<LINECOMMENT>		{
	
{LineTerminator}	 {  yybegin(YYINITIAL); System.out.println("one line comment in line: " + (yyline + 1)); }

<<EOF>> {  yybegin(YYINITIAL); System.out.println("one line comment in line: " + (yyline + 1)); }
. 				 {  }
}

<STRING> {
    /*stop when " is reached (end of string)*/
    "\""			   { yybegin(YYINITIAL); System.out.println("string: "+ string.toString() + " in line "+ stringLine);}
   
    \\t				   { string.append("\t"); }
    \\n				   { string.append("\n"); }
    \\\"			   { string.append("\""); }
    \\\\			   { string.append("\\"); }
    { LineTerminator }    { string.append("\n"); }    
     .	   { string.append(yytext()); }
}

<COMMENT> {
	  "/*"			   { openComments++; commentsPositions.add(yyline+1); commentsState.add("Unclosed");}

	   "*/"		   { 

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
	   . { }

}
//error case
 . {throw new Error((yyline+1) + ":" + (yycolumn+1) + ": illegal character <"+ yytext()+">"); }
