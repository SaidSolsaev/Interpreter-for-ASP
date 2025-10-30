// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;
	private boolean hoppOver = false;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));
		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}

	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	/*
	 * henter nåværende symbol;
	 * dette symbolet er alltid det første symbolet i curLineTokens.
	 * Symbolet blir ikke fjernet. Om nødvendig, kaller curToken
	 * på readNextLine for å få lest inn flere linjer.
	 */
	public Token curToken() {
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	/*
	 * fjerner nåværende symbol, som altså er første symbol i
	 * curLineTokens.
	 */
	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	/*
	 * leser neste linje av Asp-programmet,
	 * deler den opp i symbolene og legger alle symbolene i curLineTokens
	 */
	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;
		try {
			line = sourceFile.readLine();
			if (line == null) {
				sourceFile.close();
				sourceFile = null;
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		//FIKSA SLIK AT ALT KJØRER I EN FOR LØKKE OG IKKE 3
		// -- Must be changed in part 1:
		if (line != null) {
			EnumSet<TokenKind> tokenKind = EnumSet.range(TokenKind.andToken, TokenKind.semicolonToken);

			//Om linja er tom eller det første på linja er # sett at det skal hoppes over.
			if (line.trim().length() == 0 || line.trim().charAt(0) == '#'){
				hoppOver = true;
			}

			var updated_tabs_line = expandLeadingTabs(line);

			//Om det ikke skal hoppes over sjekk alle tokens
			if (!hoppOver){
				checkIndentToken(updated_tabs_line); //Sjekk indents og dedents først.
				int i = 0;
				while (i < line.length()){

					String ordet = "";
					String tall = "";

					if (line.charAt(i) == '#'){
						break;
					}

					if (isLetterAZ(line.charAt(i))){
						int teller = i;
						
						try {
							
							while (teller < line.length() && (isLetterAZ(line.charAt(teller)) || isDigit(line.charAt(teller)))){
								ordet += line.charAt(teller);
								teller++;
							}
						} catch (Exception e) {
							System.err.println(e);
						}

						boolean matchedToken = false;

						// Sjekk om ordet samsvarer med en TokenKind.string
						for (TokenKind token : tokenKind) {
							if (token.image.equals(ordet)) {
								var t = new Token(token, curLineNum());
								t.name = ordet;
								curLineTokens.add(t);
								matchedToken = true;
							}
						}

						// Legg til nameToken hvis det ikke samsvarer med en TokenKind
						if (!matchedToken) {
							Token t = new Token(TokenKind.nameToken, curLineNum());
							t.name = ordet;
							curLineTokens.add(t);
						}
						
						i = teller-1;
					} 
					
					else if(line.charAt(i) == '\"'){
						// Håndterer det som er i mellom " " og ' '
						// FIXED..
						int teller = i;

						while (line.charAt(teller + 1) != '\"') {
							ordet += line.charAt(teller + 1);
							teller++;

							// Håndtere at det ikke var noe " etter første
							if (teller + 1 == line.length()) {
								writeError(line, i, line.charAt(i));
							}
						}

						Token t = new Token(TokenKind.stringToken, curLineNum());
						t.stringLit = ordet;
						curLineTokens.add(t);
						i = teller + 1;
					}
					
					else if(line.charAt(i) == '\''){
						// Håndterer det som er i mellom " " og ' '
						// FIXED..
						int teller = i;

						while (line.charAt(teller + 1) != '\'') {
							ordet += line.charAt(teller + 1);
							teller++;

							// Håndtere at det ikke var noe " etter første
							if (teller + 1 == line.length()) {
								writeError(line, i, line.charAt(i));
								
							}
						}

						Token t = new Token(TokenKind.stringToken, curLineNum());
						t.stringLit = ordet;
						curLineTokens.add(t);
						i = teller + 1;
					
					} 
					
					else if(isDigit(line.charAt(i))){
						try {
							// Får index out of bound om det ikke er try catch.
							while (i < line.length() && (isDigit(line.charAt(i)) || line.charAt(i) == '.')) {
								tall += line.charAt(i);
								i++;
							}

						} catch (Exception e) {
							System.err.println(e);
						}

						if (tall.indexOf('.') == -1) {
							Token t = new Token(TokenKind.integerToken, curLineNum());
							t.integerLit = Long.parseLong(tall);
							curLineTokens.add(t);
						} else {
							// double floatTall = Double.parseDouble(tall);
							// if (floatTall % 1 == 0) {
							// 	writeError(line, i, line.charAt(i));
							// }
							// Token t = new Token(TokenKind.floatToken, curLineNum());
							// t.floatLit = floatTall;
							// curLineTokens.add(t);
							try {
								double floatTall = Double.parseDouble(tall);
								Token t;
								
								if (Math.floor(floatTall) == floatTall) {
									// Tallet er et heltall, lagre det som en double
									t = new Token(TokenKind.floatToken, curLineNum());
									t.floatLit = (double) floatTall;
								} else {
									// Tallet har desimaler, lagre det som en float
									t = new Token(TokenKind.floatToken, curLineNum());
									t.floatLit = (double) floatTall;
								}
							
								curLineTokens.add(t);
							} catch (NumberFormatException e) {
								// Håndter feil parsing av tall her
								writeError(line, i, line.charAt(i));
							}
						}

						i = i-1;
					
					} else if (!isDigit(line.charAt(i)) || !isLetterAZ(line.charAt(i))) {
						//Ta hånd om dobbelt symboler og legg til tokens.
						if (line.charAt(i) == '$'){
							writeError(line, i, line.charAt(i));
						}
						if (line.charAt(i) == '=' && line.charAt(i+1) == '='){
							var t = new Token(TokenKind.doubleEqualToken, curLineNum());
							curLineTokens.add(t);
							i++; //Øk til neste char slik at det neste symbolet ikke blir lagt til som eget symbol.
							
						} else if (line.charAt(i) == '<' && line.charAt(i+1) == '='){
							var t = new Token(TokenKind.lessEqualToken, curLineNum());
							curLineTokens.add(t);
							i++;
						} else if (line.charAt(i) == '!' && line.charAt(i+1) == '='){
							var t = new Token(TokenKind.notEqualToken, curLineNum());
							curLineTokens.add(t);
							i++;
						} else if (line.charAt(i) == '>' && line.charAt(i+1) == '='){
							var t = new Token(TokenKind.greaterEqualToken, curLineNum());
							curLineTokens.add(t);
							i++;
						} else if (line.charAt(i) == '/' && line.charAt(i+1) == '/'){
							var t = new Token(TokenKind.doubleSlashToken, curLineNum());
							curLineTokens.add(t);
							i++;
						}
						//Ta hånd om resten av symbolene
						else {
							for (TokenKind token : tokenKind) {
								if (token.image.equals(String.valueOf(line.charAt(i)))) {
									var t = new Token(token, curLineNum());
									curLineTokens.add(t);
									break; // Avslutt løkken når et symbol er funnet
								}
							}
						}
					}
					
					i++;
				}
			}
			
			//Terminate line
			//Om linja ikke er tom, men det er bare blank eller # skal den hoppe over linja.
			if(!line.isEmpty() && !hoppOver){
				curLineTokens.add(new Token(newLineToken,curLineNum()));
            }
		} 
		
		else {
			while (indents.size() > 1) {
				indents.pop();
				curLineTokens.add(new Token(TokenKind.dedentToken));
			}

			curLineTokens.add(new Token(TokenKind.eofToken));
		}

		hoppOver = false;
		
		for (Token t : curLineTokens) {
			Main.log.noteToken(t);
		}
	}

	/* gir nåværende linjes linjenummer. */
	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int findIndent(String s) {
		int indent = 0;

		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	/*
	 * omformer innledende TAB-tegn til det riktige antall
	 * blanke; TESET OG FUNKER!
	 */
	private String expandLeadingTabs(String s) {
		// -- Must be changed in part 1:
		int n = 0; // teller
		String mellomrom = "";
		for (char c : s.toCharArray()) {

			if (c == ' ') {
				mellomrom += " "; // øker med hvert mellomrom
				n++; // øker tellern for hvert mellomrom
			} else if (c == '\t') {
				int sumBlanke = TABDIST - (n % TABDIST);

				// Fjerner TAB og replacer med mellomrom
				// For løkke for å replace med riktig antall mellomrom
				for (int j = 0; j < sumBlanke; j++) {
					mellomrom += " ";
				}

				n = n + sumBlanke; // Trekk fra teller med til riktig antall
			} else {
				int i = s.indexOf(c);
				mellomrom += s.substring(i); // Setter sammen mellomrommene med resten av strengen

				// trenger ikke å sjekke midt i en setning.
				break;
			}
		}

		return mellomrom;
	}
	//Hjelpemetode for å skrive errors
	private void writeError(String s, int i, char c){
		String where = "";
		for (int x = 0; x < i; x++){
			where += " ";
		}
		scannerError("Error with " + "\" " + c + " \"" + "! on line: " + curLineNum() + "\n" + s + "\n" + where + "^");
	}

	private void checkIndentToken(String s) {
		int indent = findIndent(s); // antall blanke
		int indentStack = indents.peek();

		if (indent > indentStack) {
			// Vi forsto det sånn at det skulle være en indent token selvom den er indentert
			// 98271 ganger
			indents.push(indent);
			curLineTokens.add(new Token(TokenKind.indentToken, curLineNum()));

		} else if (indent < indentStack) {

			while (indent < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
		} 
		
		if (indent != indents.peek()) {
			scannerError("Indenteringsfeil på linje: " + curLineNum());
		}
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == doubleEqualToken || k == greaterToken || k == lessToken || k ==greaterEqualToken || k ==lessEqualToken || k== notEqualToken ){
			return true;
		}
		return false;
	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == plusToken || k == minusToken){
			return true;
		}
		return false;
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == slashToken || k == doubleSlashToken || k == percentToken || k == astToken){
			return true;
		}
		return false;
	}

	//Hjelpemetode for å sjekke om det er comp stmt
	public boolean isCompoundStmt(){
		TokenKind k = curToken().kind;
		if (k == forToken || k == ifToken || k == whileToken || k == defToken){
			return true;
		}
		return false;
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		if (k == plusToken || k == minusToken){
			return true;
		}
		return false;
	}

	/*
	 * sjekker om det finnes et ulest ‘=’-symbol i den aktuelle
	 * setningen.
	 */
	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}
