import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;


class Simbolo{

   public byte token;
   public String lexema;
   public int end;

   Simbolo(byte token, String lexema){
   
      this.token = token;
      this.lexema = lexema;
   
   
   }

   Simbolo(String lexema){
      this.lexema = lexema;
   }
   
   Simbolo(int end, String lexema){
      this.lexema = lexema;
      this.end = end;
   }
   
   Simbolo(){
      this.token = 0;
      this.lexema = "";
   }



}

class Lexico{

   public static final int ESTADO_INICIAL = 0;
   public static final int ESTADO_FINAL = 2;
   public Simbolo simb = null;
   public char [] programa_fonte = new char[10000];
   public TabelaDeSimbolos tabela = new TabelaDeSimbolos();
   public int n = 0;
   public int linha_count = 1;
   public static int estado = 0;
   public int pos = 0;
   public String lex = "";
   public int len = 0;
   
   //construtor da classe
   Lexico(){
   
   }


   public boolean eLetra(char c){
   
      if((c >= 65 && c <= 90) ||
      c >= 97 && c <= 122){
         return true;
      }else{
         return false;
      }
   
   
   }
   
   public boolean eDigito(char c){
   
      if(c >= 48 && c <= 57){
         return true;
      }else{
         return false;
      }
   
   
   }
   
   
   public boolean eSublinhado(char c){
   
      if(c == '_'){
         return true;
      }else{
         return false;
      }
   
   
   }
   
   public boolean eHexa(char c){
   
      if((c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F')
      || (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f')){
         return true;
      }else{
         return false;
      }
   
   
   }
   
   public boolean ePalavraReservada(char c){
   
      if(c == '(' || c == ')' || c == ','|| c == '+' || c == '-' || c == '*' || c == ';'
      || c == '%' || c == '[' || c == ']'){
         return true;
      }else{
         return false;
      }
   }
   
   
   public boolean eCaracterValido(char c){
   
      if(c == '.' || c == ',' || c == ';'|| c == ':' || c == '(' || c == ')' || c == '[' || c == '*'
      || c == ']' || c == '{' || c == '}' || c == '+' || c == '-'|| c == '/' || c == 92 || c == 32
      || c == '@' || c == 39 || c == '&' || c == '!' || c == '<' || c == '>' || c == '=' || c == 10 || c == 13){
         return true;
      }else{
         return false;
      }
   }
   
   public void printLexema(String lex){
      System.out.println(""+linha_count);
      System.out.println("lexema nao identificado ["+lex+"].");
      System.exit(1);
   }
   
   
   
   public void printCharInvalido(){
      System.out.println(""+linha_count);
      System.out.println("caractere invalido.");
      System.exit(1);
   }
   
   
   public void printFimdeArq(){
      System.out.println(""+linha_count);
      System.out.println("fim de arquivo nao esperado.");
      System.exit(1);
   }






   public Simbolo proxToken(){
   
      
      estado = ESTADO_INICIAL;
      char c;
      String lex = "";
      int casas = 0;
   
   
      while(estado != ESTADO_FINAL){
      
         c = programa_fonte[pos];
         //System.out.println("Estado = "+ estado + " --> "+(int)c);
      
      // ESTADO 0 ***********************************************
         if(estado == 0 && (eHexa(c))){
            lex += "" + programa_fonte[pos];
            estado = 18;
            ++pos;
         }else if(estado == 0 && (eDigito(c))){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 21;
            ++pos;
         }else if(estado == 0 && (eLetra(c) || eSublinhado(c))){
            lex += "" + programa_fonte[pos];
            estado = 1;
            ++pos;
         }else if(estado == 0 && c == 32){ //espaço em branco 
            ++pos;
         }else if(estado == 0 && c == 10){ //quebra de linha
            ++linha_count;
            ++pos;
         }else if(estado == 0 && c == 13){ //quebra de linha
            ++pos;
         }else if(estado == 0 && c == '{'){ //comentario
            estado = 11;
            ++pos;
         }else if(estado == 0 && c == '"'){ //string
            estado = 28;
            ++pos;
         }else if(estado == 0 && ePalavraReservada(c)){ //palavra reservada
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 0 && c == '='){
            lex += "" + programa_fonte[pos];
            estado = 6;
            ++pos;
         }else if(estado == 0 && c == '>'){
            lex += "" + programa_fonte[pos];
            estado = 9;
            ++pos;
         }else if(estado == 0 && c == '<'){
            lex += "" + programa_fonte[pos];
            estado = 10;
            ++pos;
         }else if(estado == 0 && c == '!'){
            lex += "" + programa_fonte[pos];
            estado = 10;
            ++pos;
         }else if(estado == 0 && c == '/'){
            lex += "" + programa_fonte[pos];
            estado = 15;
            ++pos;
         }else if(estado == 0 && c == '.'){
            lex += "" + programa_fonte[pos];
            estado = 29;
            ++pos;
         }else if(estado == 0 && c == 0){
            ++linha_count;
            break;
         }else if(estado == 0){
            printCharInvalido();
         }
         
         
         //ESTADO 1 ************************************************
         else if(estado == 1 && (eLetra(c) || eDigito(c) || eSublinhado(c))){
            lex += "" + programa_fonte[pos];
            estado = 1;
            ++pos;
         }else if(estado == 1 && !(eLetra(c) || eDigito(c) || eSublinhado(c))){
            
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 6 Verificar se e = ou ==
         else if(estado == 6 && c == '='){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 6 && c != '='){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         //ESTADO 9 Verificar se e > ou >=
         else if(estado == 9 && c == '='){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 9 && c != '='){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 10 Verificar se e < ou <=
         else if(estado == 10 && c == '='){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 10 && c != '='){
            printLexema(lex);
         }
         
         
         
         
         //ESTADO 11 comentario
         else if(estado == 11 && c == '*'){
            estado = 12;
            ++pos;
         }else if(estado == 11 && c != '*'){
            lex = "{";
            printLexema(lex);
         }
         
         //ESTADO 12 comentario
         else if(estado == 12 && c=='*'){
            estado = 13;
            ++pos;
         }else if(estado == 12 && (c == 0 || c == 65535 || c == -1)){
            printFimdeArq();
         }else if(estado == 12 && c == 10){
            ++linha_count;
            estado = 12;
            ++pos;
         }else if(estado == 12 && (eLetra(c) || eDigito(c) || eCaracterValido(c) || eSublinhado(c) || ePalavraReservada(c) || c == 34)){
            estado = 12;
            ++pos;
         }else if(estado == 12){
            //System.out.println("Char: "+ c + " ---> "+(int)c);
            printCharInvalido();
         }
         //++anl.linha_count;
         
         
         //ESTADO 13 comentario
         else if(estado == 13 &&  c=='}'){
            estado = ESTADO_INICIAL;
            ++pos;
         }else if(estado == 13 && c == '*'){
            estado = 13;
            ++pos;
         }else if(estado == 13 && (eLetra(c) || eDigito(c) || eCaracterValido(c) || eSublinhado(c) || ePalavraReservada(c) || c == 34)){
            estado = 12;
            ++pos;
         }else if(estado == 13 && (c == 0 || c == 65535 || c == -1)){
            printFimdeArq();
         }else if(estado == 13){
            printCharInvalido();
         }
         
         
         //ESTADO 14 Verificar se e !=
         else if(estado == 14 && c == '='){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 14 && c != '='){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 15 Verificar se e / ou // //
         else if(estado == 15 && c == '/'){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 15 && c != '/'){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 18 verificar se hexa
         else if(estado == 18 && (eDigito(c))){
            lex += "" + programa_fonte[pos];
            estado = 20;
            ++pos;
         }else if(estado == 18 && (eHexa(c))){
            lex += "" + programa_fonte[pos];
            estado = 20;
            ++pos;
         }else if(estado == 18 && (eLetra(c) || eDigito(c) || eSublinhado(c))){
            lex += "" + programa_fonte[pos];
            estado = 1;
            ++pos;
         }
         
         
         //ESTADO 20 verificar se e hexa ou uma string ou lexema nao identificado
         else if(estado == 20 && c == 'h'){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 20 && c != 'h' && (eLetra(c) || eDigito(c) || eSublinhado(c))){
            lex += "" + programa_fonte[pos];
            estado = 1;
            ++pos;
         }
         
         
         //ESTADO 21 verificar se hexa
         else if(estado == 21 && (eDigito(c))){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 22;
            ++pos;
         }else if(estado == 21 && eHexa(c)){
            lex += "" + programa_fonte[pos];
            estado = 27;
            ++pos;
         }else if(estado == 21 && c == '.'){
            lex += "" + programa_fonte[pos];
            estado = 31;
            ++pos;
         }else if(estado == 21 && !(eDigito(c) || eHexa(c))){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 22 verificar se hexa
         else if(estado == 22 && (eDigito(c))){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 23;
            ++pos;
         }else if(estado == 22 && c == 'h'){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 22 && c == '.'){
            lex += "" + programa_fonte[pos];
            estado = 31;
            ++pos;
         }else if(estado == 22 && !(eDigito(c) || eHexa(c))){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         //ESTADO 23 verificar se inteiro
         else if(estado == 23 && (eDigito(c))){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 23;
            ++pos;
         }else if(estado == 23 && c == '.'){
            lex += "" + programa_fonte[pos];
            estado = 31;
            ++pos;
         }else if(estado == 23 && !(eDigito(c))){
            long x = Long.parseLong(""+lex);
            if(x <= Integer.MAX_VALUE){
               simb = new Simbolo(""+lex);
               lex = "";
               estado = ESTADO_FINAL;
            }else{
               printLexema(lex);
            }
         }
                  
         
         //ESTADO 27 verificar se e hexa ou uma string ou lexema nao identificado
         else if(estado == 27 && c == 'h'){
            lex += "" + programa_fonte[pos];
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
            ++pos;
         }else if(estado == 20 && c != 'h' && (eLetra(c) || eDigito(c) || eSublinhado(c))){
            //System.out.println("Erro!!!!");
            System.exit(1);
         }
         
         
         
         //ESTADO 28 strings
         else if(estado == 28 && c == '"'){
            lex += "" + (byte)0;
            if(lex.length() <= 255){
               simb = new Simbolo(""+lex);
               lex = "";
               estado = ESTADO_FINAL;
               ++pos;
            }else{
               printLexema(lex);
            } 
           
         }else if(estado == 28 && c == 10){
            printLexema(""+lex);
         }else if(estado == 28 && (eLetra(c) || eDigito(c) || eCaracterValido(c) || eSublinhado(c) || ePalavraReservada(c))){
            lex += "" + programa_fonte[pos];
            estado = 28;
            ++pos;
         }else if(estado == 28 && (c == 0 || c == 65535 || c == -1)){
            printFimdeArq();
         }else if(estado == 28){
            printCharInvalido();
         }
         
         
         
         //ESTADO 29 FLOAT
         else if(estado == 29 && eDigito(c)){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 31;
         }else if(estado == 29 && !eDigito(c)){
            printLexema(lex);
         }
         
         
         //ESTADO 30 DIGITO PARA FLOAT
         else if(estado == 30 && eDigito(c)){
            ++casas;
            lex += "" + programa_fonte[pos];
            estado = 30;
         }else if(estado == 30 && !eDigito(c)){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }
         
         
         //ESTADO 31 PARTE FLOAT
         else if(estado == 31 && eDigito(c)){
            ++casas;
            //System.out.println("Aqui");
            lex += "" + programa_fonte[pos];
            estado = 31;
            ++pos;
         }else if(estado == 31 && !eDigito(c) && casas <= 6){
            simb = new Simbolo(""+lex);
            lex = "";
            estado = ESTADO_FINAL;
         }else if(estado == 31){
            printLexema(lex);
         }
         
         
      
      }
   
   
      return simb;
   
   
   }



}


class TabelaDeSimbolos{

   
   public HashMap<String, Simbolo> tabela = new HashMap<>();
   public int end = -1;


   public TabelaDeSimbolos(){
      tabela.put("const" ,new Simbolo(++end, "integer"));
      tabela.put("integer" ,new Simbolo(++end, "integer"));
      tabela.put("char" ,new Simbolo(++end, "char"));
      tabela.put("while" ,new Simbolo(++end, "while"));
      tabela.put("if" ,new Simbolo(++end, "if"));
      tabela.put("float" ,new Simbolo(++end, "float"));
      tabela.put("else" ,new Simbolo(++end, "else"));
      tabela.put("and" ,new Simbolo(++end, "and"));
      tabela.put("or" ,new Simbolo(++end, "or"));
      tabela.put("not" ,new Simbolo(++end, "not"));
      tabela.put("=" ,new Simbolo(++end, "="));
      tabela.put("==" ,new Simbolo(++end, "=="));
      tabela.put("(" ,new Simbolo(++end, "("));
      tabela.put(")" ,new Simbolo(++end, ")"));
      tabela.put("<" ,new Simbolo(++end, "<"));
      tabela.put(">" ,new Simbolo(++end, ">"));
      tabela.put("!=" ,new Simbolo(++end, "!="));
      tabela.put(">=" ,new Simbolo(++end, ">="));
      tabela.put("<=" ,new Simbolo(++end, "<="));
      tabela.put("," ,new Simbolo(++end, ","));
      tabela.put("+" ,new Simbolo(++end, "+"));
      tabela.put("-" ,new Simbolo(++end, "-"));
      tabela.put("*" ,new Simbolo(++end, "*"));
      tabela.put("/" ,new Simbolo(++end, "/"));
      tabela.put(";" ,new Simbolo(++end, ";"));
      tabela.put("begin" ,new Simbolo(++end, "begin"));
      tabela.put("end" ,new Simbolo(++end, "end"));
      tabela.put("readln" ,new Simbolo(++end, "readln"));
      tabela.put("//" ,new Simbolo(++end, "//"));
      tabela.put("string" ,new Simbolo(++end, "string"));
      tabela.put("write" ,new Simbolo(++end, "write"));
      tabela.put("writeln" ,new Simbolo(++end, "writeln"));
      tabela.put("%" ,new Simbolo(++end, "%"));
      tabela.put("[" ,new Simbolo(++end, "["));
      tabela.put("]" ,new Simbolo(++end, "]"));  
      tabela.put("TRUE" ,new Simbolo(++end, "TRUE"));
      tabela.put("FALSE" ,new Simbolo(++end, "FALSE"));  
      tabela.put("boolean" ,new Simbolo(++end, "boolean"));  
         
   }
 
   public void setEnd(int end){
      this.end = end;
   }
   
   public int getEndereco(){
      return end;
   }
   

}//end Tabela_S()

public class Compilador{

   public static Lexico anl = new Lexico();
   public static Scanner scanner;
   public static String linha;
   public static Simbolo simb1 = null;


   public static void close(){
   
      if(anl.linha_count != 1){++anl.linha_count;}
      System.out.println(anl.linha_count + " linhas compiladas.");
   
   }


   public static void main(String []args){
   
      //scanner = new Scanner(System.in);
      //System.out.print("Arquivo: ");
      //linha = scanner.nextLine();
      int[] cadeia_caracteres = new int[100000];
      int pos = 0;
      
      try{
      
         //BufferedReader arquivo = new BufferedReader(new InputStreamReader(System.in));
         BufferedReader arquivo = new BufferedReader(new FileReader("teste.txt"));
         int c;
         while((c=arquivo.read())!=-1){
            char aux = (char)c;
            
            if(aux != 13)
               cadeia_caracteres[pos] = c; 
            anl.programa_fonte[pos] = aux;
            //System.out.println(""+(char)aux+" -> "+ ch);
            ++pos;     
         }
         
         
         System.in.read();
         
         anl.len = --pos;
         
         while(anl.pos < anl.len){
         
            
            simb1 = anl.proxToken();
            //System.out.println(""+simb1.lexema);
            //System.in.read();
            //System.out.println("oi");
         }
         
         close();
         
         
         
      
      
      }catch(Exception e){
      
      }
   
   
   
   
   }


}



