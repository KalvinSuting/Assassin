/*Kalvin Suting
* 1/23/2016
* Section BC
* TA: Grace Chen
* Performs all necessary actions to maintain a game of Assassin.
*/

import java.util.*;
public class AssassinManager {
	private AssassinNode killRingFront;
	private AssassinNode graveyardFront;
	
	//pre:	requires a list of non zero names to be passed in. Throws an IllegalArgumentException
	//		if an empty list of names is passed in.
	//post:	constructs a killRing populated by the names passed in.
	public AssassinManager(List<String> names){
		AssassinNode current = null;
		if(names.isEmpty()){
			throw new IllegalArgumentException("Empty list");
		}
		for(String s: names){
			if (killRingFront == null) {
				killRingFront = new AssassinNode(s);
				current = killRingFront;
			} else {
				current.next = new AssassinNode(s);
				current = current.next;
			}
		}
	}
	
	//post:	prints the names of the members currently inside the killRing.
	void printKillRing(){
		AssassinNode current = killRingFront;
		while(current.next != null){
			System.out.println("    " + current.name + " is stalking " + current.next.name);
			current = current.next;
		}	//last person in the killRing stalks the first in the killRing
		System.out.println("    " + current.name + " is stalking " + killRingFront.name);
	}
	
	//post:	prints the names of the members currently inside the graveyard.
	public void printGraveyard(){
		if(graveyardFront != null){
			AssassinNode current = graveyardFront;
			while(current != null){
				System.out.println("    " + current.name + " was killed by " + current.killer);
				current = current.next;
			}
		}
	}
	
	//post:	returns true if desired name is in killRing, false otherwise. Ignores
	//		case when checking names.
	public boolean killRingContains(String name){
		AssassinNode current = killRingFront;
		return containsName(current,name);
	}
	
	//post:	returns true if desired name is in the graveyard, false otherwise. Ignores
	//		case when checking name.
	public boolean graveyardContains(String name){
		AssassinNode current = graveyardFront;
		return containsName(current,name);
	}
	
	//post:	returns true if game is over, false otherwise.
	public boolean gameOver(){
		return killRingFront.next == null;
	}
	
	//post:	returns the name of the winner of the game, returns null if game isn't over.
	public String winner(){
		if(gameOver()){
			return killRingFront.name;
		}
		return null;
	}
	
	//pre:	killRing must contain the name passed in, throws IllegalArgumentException if not.
	//		Game must also still be in progress, throws IllegalStateException if not. Ignores
	//		case of the name being assassinated.
	//post:	removes the specified name from the killRing and places them into the graveyard.
	void kill(String name){
		if(!killRingContains(name)){
			throw new IllegalArgumentException("killRing does not contain name");
		}
		if(gameOver()){
			throw new IllegalStateException("Game is over");
		}
		if(killRingFront.name.equalsIgnoreCase(name)){
			AssassinNode current = killRingFront;
			AssassinNode killer = killRingFront.next;
			
			while(killer.next != null){
				killer = killer.next;
			}
			current.killer = killer.name;
			killRingFront = current.next;
			manageGraveyard(current);
		} else { //all of the cases other than the very first in the killRing
			AssassinNode current = killRingFront.next;	//since first in ring was checked.
			AssassinNode last = killRingFront;
			
			while(!current.name.equalsIgnoreCase(name)){
				last = current;
				current = current.next;
			}
			current.killer = last.name;
			last.next = current.next;
			manageGraveyard(current);
		}
	}
	
	//post:	checks to see if the given name exists in either the graveyard or killRing
	//		is used in both graveYardContains() and killRingContains().
	private boolean containsName(AssassinNode current, String name ){
		while(current != null){
			if(current.name.equalsIgnoreCase(name)){
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	//post: performs the actual operation of moving the person from the killRing to the graveyard.
	private void manageGraveyard(AssassinNode current){
		if(graveyardFront == null){
			graveyardFront = current;
			graveyardFront.next = null;
		}
		else{
			current.next = graveyardFront;
			graveyardFront = current;
		}
	}
}
