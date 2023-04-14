/*
Kevin Baron
4/18/13
CSE 143 Assignment #5
Grammar Solver
*/

import java.util.SortedMap;
import java.util.Random;
import java.util.List;
import java.util.TreeMap;

public class GrammarSolver {
	
	private SortedMap<String, String[][]> rules;//to store all nonterminals and their associated rules
	
	private static final Random r = new Random();//for generating a random rule
	
	//pre : given a non-empty list with unique nonterminal symbols. throws IllegalArgumentExcetion if not
	//post: the given list of strings has been stored as a map with nonterminal keys and 2-D array values
	//      which represent the possible rules that can result from a certain nonterminal
	public GrammarSolver(List<String> list) {
		if (list.isEmpty())
			throw new IllegalArgumentException("list is empty");
		rules = new TreeMap<String, String[][]>();
		//each String in list is a nonterminal and its possible rules. break down each one of these Strings
		//into its most basic pieces.
		for (String nonterminalAndRules : list) {
			//separate the nonterminal from its possible rules
			String[] parts = nonterminalAndRules.split(":");
			//store the nonterminal without leading/trailing whitespace
			String nonterminal = parts[0].trim();
			//check for duplicate nonterminals
			if (grammarContains(nonterminal))
				throw new IllegalArgumentException("two or more identical nonterminals: " + nonterminal);
			//all the possible rules are currently globbed together in a String, separated by |'s.
			//separate all the rules from one another
			String[] ruleOptions = parts[1].split("[|]");
			//each rule can have arbitrarily many component pieces, all of which are still globbed
			//together in a String in ruleOptions. rulePieces instead stores each rule as its own array
			//by splitting the rule in ruleOptions along whitespace.
			String[][] rulePieces = new String[ruleOptions.length][];
			for (int i = 0; i < ruleOptions.length; i++) {
				rulePieces[i] = ruleOptions[i].trim().split("[ \t]+");
			}//eo for
			//associates the nonterminal with its 2-D rule array
			rules.put(nonterminal, rulePieces);
		}//eo for each
	}//eo GrammarSolver constructor
	
	//post: returns true if the symbol is found in the grammar; false if not found
	public boolean grammarContains(String symbol) {
		return rules.containsKey(symbol);
	}//eo grammarContains
	
	//pre : given a valid symbol from the grammar and a non-negative number of repetions.
	//      throws IllegalArgumentException if not
	//post: returns an array filled with the desired number of random generations of the given nonterminal
	public String[] generate(String symbol, int times) {
		if (!grammarContains(symbol))
			throw new IllegalArgumentException("grammar does not contain nonterminal symbol: " + symbol);
		if (times < 0)
			throw new IllegalArgumentException("number of instances must be at least 0: " + times);
		String[] toReturn = new String[times];
		//use the private generate method to create each String
		for (int i = 0; i < toReturn.length; i++)
			//.trim() removes the one trailing space from the generated String before adding it to the array
			toReturn[i] = generate(symbol).trim();
		return toReturn;
	}//eo public String[] generate(String, int)
	
	//post: returns either the given symbol with a space added if the symbol is a terminal
	//      or returns the recursively generated rules of a nonterminal
	private String generate(String symbol) {
		//base case: symbol is terminal
		if (!grammarContains(symbol))
			return symbol + " ";
		//recursive case: symbol is nonterminal
		//summon the possible rules this symbol could use
		String[][] ruleOptions = rules.get(symbol);
		//select one of these possible rules at random
		String[] rule = ruleOptions[r.nextInt(ruleOptions.length)];
		//build a returnable String from scratch by adding on
		//the generation of each individual piece of the rule
		String toReturn = "";
		for (String rulePiece : rule)
			toReturn += generate(rulePiece);
		return toReturn;
	}//eo private String generate(String)
	
	//post: the sorted set of all the grammar's nonterminals has been returned
	public String getSymbols() {
		return rules.keySet().toString();
	}//eo getSymbols
	
}//eo GrammarSolver class