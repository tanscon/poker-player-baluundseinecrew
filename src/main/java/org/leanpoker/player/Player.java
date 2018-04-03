package org.leanpoker.player;

//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
import com.google.gson.*;

import java.util.Map;

public class Player {

    static final String VERSION = "PDPS3_01";

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
    		
    		
    		
    		
    		int chenValue;
    		
    		chenValue = calcHand(p.hole_cards[0].rank, p.hole_cards[0].suit, p.hole_cards[1].rank, p.hole_cards[1].suit);
    		
    		if (chenValue >= 8 && chenValue <= 10){
    			bet = gs.current_buy_in;
    		}else if (chenValue >= 10){
    			bet = gs.current_buy_in + (p.stack/10);
    		}
    		
        return bet;
    }

    
    public static void showdown(JsonElement game) {
    }
    
    
    public static int calcHand(String n1, String c1, String n2, String c2) {
		
		
		
		
		float value = calcPair(n1, n2);
		if(isSuited(c1, c2))value += 2;
		value -= gap(n1,n2);
		
		return Math.round(value);
		
	
	
    }

    public static float getValue(String n1) {
	
	
	switch (n1) {
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
    public static boolean isPair(String n1, String n2) {
		if(n1.equals(n2))return true;
		else return false;
	}
	
    public static float calcPair(String n1, String n2) {
		if(isPair(n1,n2)) {
			return getValue(n1) *2;
		}else return getValue(n1);
	}
	
    public static boolean isSuited(String c1, String c2) {
		if(c1.equals(c2))return true;
		else return false;
	}
	
    public static int gap(String n1, String n2) {
		
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
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


