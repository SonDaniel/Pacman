package mainPackage;

import java.util.ArrayList;

import javafx.scene.Group;
import objectsPackage.ObjectBody;
import objectsPackage.Pellet;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public class CollisionContactListener implements ContactListener {

	private boolean colliding;
	private Group rootGroup;
	private ScorePanel scorePanel;
	private Pellet[] pellets;
	private ArrayList<Fixture> fixturesToRemove;
	private ArrayList<Pellet> pelletsToRemove;

	public boolean isColliding() {
		return colliding;
	}

	public CollisionContactListener(Group rootGroup, Pellet[] pellets,
			ScorePanel scorePanel) {
		colliding = false;
		this.rootGroup = rootGroup;
		this.pellets = pellets;
		this.scorePanel = scorePanel;
		this.fixturesToRemove = new ArrayList<Fixture>();
		this.pelletsToRemove = new ArrayList<Pellet>();
	}

	public void beginContact(Contact contact) {
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();
		if (f1.getBody().getUserData() == "PACMAN"
				&& f2.getBody().getUserData() == "PELLET") {
			colliding = true;

			fixturesToRemove.add(f2);

			// remove the pellet
			ObjectBody b = (ObjectBody) f2.getBody();
			for(int i=0; i<pellets.length; i++){
				if(pellets[i].getId() == b.getID()){
					pelletsToRemove.add(pellets[i]);
					break;
				}
			}




			scorePanel.incrementScore(10);
			System.out.println("pacman-pellet");
		} else if (f1.getBody().getUserData() == "PACMAN"
				&& f2.getBody().getUserData() == "BONUS_PELLET") {
			// remove the bonus pellet
			colliding = true;
			scorePanel.incrementScore(50);

			System.out.println("pacman-bonus pellet");
		}

		else if (f1.getBody().getUserData() == "PACMAN"
				&& f2.getBody().getUserData() == "GHOST"
				|| (f2.getBody().getUserData() == "GHOST" && f1.getBody()
				.getUserData() == "GHOST")) {
			// remove an extra pacman
			System.out.println("here");
			scorePanel.decrementLives();
			colliding = true;
			System.out.println("pacman-ghost");
		}
	}

	public void endContact(Contact contact) {
		colliding = false;
		System.out.println("Contact removed");
	}

	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Pellet> getPelletsToRemove() {
		return pelletsToRemove;
	}

	public ArrayList<Fixture> getFixturesToRemove() {
		return fixturesToRemove;
	}
}
