package org.leanpoker.player;

//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
import com.google.gson.*;

import java.util.Map;

public class Player {

    static final String VERSION = "PDPS3_01";
	static boolean raised = false; 
    public static int betRequest(JsonElement request) {
    		Gson gson = new Gson();
    		GameState gs = gson.fromJson(request, GameState.class);

    		int bet = 0;
    		int mySelfID = gs.in_action;
    		
    		System.err.println("All cards");
    		PlayerObj p = gs.players[mySelfID];
    		System.err.println(p.name);	
    		for (int j=0; j<p.hole_cards.length; j++)
    		{
    			System.err.println(p.hole_cards[j]);
    		}
    		
    		
    		Hand h = new Hand(p.hole_cards[0].rank, p.hole_cards[0].suit, p.hole_cards[1].rank, p.hole_cards[1].suit);
    		
    		int chenValue;
    		chenValue = h.getValue();
    		System.err.println("Round:");
    		System.err.println(gs.round);
    	//	System.err.println(chenValue);
    		
    
    		
    		if(gs.community_cards.length == 0) {
        		if (chenValue >= 6 && chenValue <= 10){
        			bet = gs.current_buy_in;
        		}else if (chenValue >= 10 && !raised){
        			bet = gs.current_buy_in + (p.stack/10);
        			raised = true;
        		}else if (chenValue >= 10){
        			bet = gs.current_buy_in;
        		}
        		
        		if(gs.current_buy_in >= p.stack && p.stack > 200 && chenValue < 15) {
        			bet = 0;
        		}
        		if(activePlayer(gs) >= 2 && chenValue < 14) {
        			bet = 0;
        		}
        		
    		}else if (gs.community_cards.length == 3){
    			
    			raised = false;
    		
    			chenValue += h.checkPair(gs.community_cards[0].rank);
    			chenValue += h.checkPair(gs.community_cards[1].rank);
    			chenValue += h.checkPair(gs.community_cards[2].rank);
    			chenValue += h.checkFlush(gs.community_cards);
    			chenValue += h.checkPair(gs.community_cards);
        		if (chenValue >= 9 && chenValue <= 14){
        			bet = gs.current_buy_in;
        		}else if (chenValue >= 14){
        			bet = gs.current_buy_in + (p.stack/10);
        		}
        		if(activePlayer(gs) >= 2 && chenValue < 20) {
        			bet = 0;
        		}
        		
    		}else if (gs.community_cards.length == 4){
    			
    			raised = false;
    		
    			chenValue += h.checkPair(gs.community_cards[0].rank);
    			chenValue += h.checkPair(gs.community_cards[1].rank);
    			chenValue += h.checkPair(gs.community_cards[2].rank);
    			chenValue += h.checkPair(gs.community_cards[3].rank);
    			chenValue += h.checkFlush(gs.community_cards);
    			chenValue += h.checkPair(gs.community_cards);
        		if (chenValue >= 9 && chenValue <= 14){
        			bet = gs.current_buy_in;
        		}else if (chenValue >= 14 && chenValue < 20){
        			bet = gs.current_buy_in + (p.stack/10);
        		}else if(chenValue >= 20) {
        			bet = p.stack;
        		}
        		if(activePlayer(gs) >= 2 && chenValue < 20) {
        			bet = 0;
        		}
        		
    		}else if (gs.community_cards.length == 5){
    			
    			raised = false;
    		
    			chenValue += h.checkPair(gs.community_cards[0].rank);
    			chenValue += h.checkPair(gs.community_cards[1].rank);
    			chenValue += h.checkPair(gs.community_cards[2].rank);
    			chenValue += h.checkPair(gs.community_cards[3].rank);
    			chenValue += h.checkPair(gs.community_cards[4].rank);
    			chenValue += h.checkFlush(gs.community_cards);
    			chenValue += h.checkPair(gs.community_cards);
        		if (chenValue >= 9 && chenValue <= 14){
        			bet = gs.current_buy_in;
        		}else if (chenValue >= 14 && chenValue < 20){
        			bet = gs.current_buy_in + (p.stack/10);
        		}else if(chenValue >= 20) {
        			bet = p.stack;
        		}
        		if(activePlayer(gs) >= 2 && chenValue < 20) {
        			bet = 0;
        		}
        		
    		}else {
    			bet = gs.current_buy_in;
    		}
    		
        return bet;
    }

    
    public static void showdown(JsonElement game) {
    }

   public static int activePlayer(GameState gs) {
    	int count = 0;
    	for(int i = 0; i < gs.players.length; i++) {
    		if(gs.players[i].status.equals("out")) {
    			count++;
    		}
    	}
    	return gs.players.length - count;
    }
  
}




class GameState {
	public String tournament_id;
	public String game_id;
	public  int round;
	public int bet_index;
	public int small_blind;
	public int current_buy_in;
	public int pot;
	public int minimum_raise;
	public int dealer;
	public int orbits;
	public int in_action;
	public PlayerObj[] players;
	public CardObj[] community_cards;
}

class PlayerObj {
	public int id;
	public String name;
	public String status;
	public String version;
	public int stack;
	public int bet;
	public CardObj[] hole_cards;
}

	
class CardObj {
	public String suit;
	//spades, hearts, clubs
	public String rank;	
	// 2,3,4,5,6,7,8,9,10,J,Q,K,A
	
	public String toString()
	{
		return suit + "/" + rank;
	}
	
}
	
	
	
	
	
	
	
	
	

class Hand {

	String n1, n2, c1, c2;
	int numb1, numb2;
	int value;
	
	public Hand(String n1, String c1, String n2, String c2) {
		this.n1 = n1;
		this.n2 = n2;
		this.c1 = c1;
		this.c2 = c2;
		
		numb1 = setIntValue(n1);
		numb2 = setIntValue(n2);
		
		if(numb1 >= numb2)value = calcHand(n1,c1,n2,c2);
		else value = calcHand(n2,c2,n1,c1);
		

	}
	
	public int getValue() {
		return value;
	}
	
	
	int setIntValue(String n1){
		switch(n1) {
		case "A":
			return 14;
		case "K":
			return 13;
		case "Q":
			return 12;
		case "J":
			return 11;
		default:
			return Integer.parseInt(n1);
		}
	}
	
	int calcHand(String n1, String c1, String n2, String c2) {
		
		
		
		
		float value = calcPair(n1, n2);
		if(isSuited(c1, c2))value += 2;
		value -= Math.abs(gap(n1,n2)) * 0.25;
		
		return Math.round(value);
		
	
	
}
	
	
	

float getValue(String number) {
	
	
	switch (number) {
	case "A": 
		return 10;
	
	case "K": 
	return 8;
	case "Q": 
	return 7;
	case "J": 
return 6;
	default:
		return Integer.parseInt(number) / 2;
}
}	
	boolean isPair(String n1, String n2) {
		if(n1.equals(n2))return true;
		else return false;
	}
	
	float calcPair(String n1, String n2) {
		if(isPair(n1,n2)) {
			return getValue(n1) *3;
		}else return getValue(n1);
	}
	
	boolean isSuited(String c1, String c2) {
		if(c1.equals(c2))return true;
		else return false;
	}
	
	int gap(String n1, String n2) {
		
		if(n1.equalsIgnoreCase("A")) {
		 n1 = "14"	;
		}else if(n1.equalsIgnoreCase("K")) {
			n1 = "13";
		}else if(n1.equalsIgnoreCase("Q")) {
			n1 = "12";
		}else if(n1.equalsIgnoreCase("J")) {
			n1 ="11";
		}
		
		if(n2.equalsIgnoreCase("A")) {
			 n2 = "14"	;
			}else if(n2.equalsIgnoreCase("K")) {
				n2 = "13";
			}else if(n2.equalsIgnoreCase("Q")) {
				n2 = "12";
			}else if(n2.equalsIgnoreCase("J")) {
				n2 ="11";
			}
		
	
	return Math.abs(Integer.parseInt(n1) - Integer.parseInt(n2));
		
	}

	public int checkPair(String no1) {
		
		if(isPair(no1, n1)) {
			return 6;
		}else if(isPair(no1, n2)) {
			return 6;
		}else return -1;
	
	}
	
	public int checkFlush(CardObj[] cc) {
		int oCo = 1;
		int tCo = 2;
		
		for(int i = 0; i < cc.length; i++) {
			if(cc[i].suit == c1)oCo ++;
			if(cc[i].suit == c2)tCo ++;
		}
		
		if(oCo >= 5)return 20;
		else if(tCo >= 5)return 20;
		else return 0;
	}
	
	public int checkStraight(CardObj[] cc) {
		int lowest;
		if(numb1 < numb2)lowest = numb1;
		else lowest = numb2;
		
		for(int i = 0; i < cc.length; i++) {
			if(setIntValue(cc[i].rank) < lowest) {
				lowest = setIntValue(cc[i].rank);
			}
		}
		
		count  = 1;
		for(int o = 0; o <  8; o++) {
			for(int i = 0; i < cc.length; i++) {
				if(cc[i] == lowest+1) {
					count ++;
					lowest ++;
				}
				
				if(lowest +1 == numb1 || lowest +1 == numb2) {
					count++;
					lowest ++;
				}
			}
		}
		
		if(count >= 5)return 20;
		else return 0;
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


