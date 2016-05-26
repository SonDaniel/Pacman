package objectsPackage;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mainPackage.Properties;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Pacman extends Piece {
	private Node node;
	private final int width = 3; // square - same width and height
	private final int height = 3;
	private final BodyType bodyType = BodyType.DYNAMIC;

	private Image imageClose = new Image(getClass().getResourceAsStream(
			"/pacman-closed.png"));
	private Image imageOpen = new Image(getClass().getResourceAsStream(
			"/pacman-opened.png"));
	private Image[] images = new Image[] { imageClose, imageOpen };
	private int imgNum;
	private boolean colliding = false;
	private Image image;
	//starts moving to the left
	private Vec2 currDirection = new Vec2(-20.0f, 0.0f);
	private int currDegree = 180;

	public Pacman(int posX, int posY, World world) {
		super(posX, posY, world, "PACMAN");
		node = create();
	}

	private Node create() {
		Image img = images[0];
		ImagePattern imagePattern = new ImagePattern(img);

		Rectangle pacman = new Rectangle(
				(Properties.jBoxtoPixelWidth(width) * 2),
				(Properties.jBoxtoPixelHeight(height) * 2));
		pacman.setFill(imagePattern);
		pacman.setLayoutX(Properties.jBoxToFxPosX(getPosX())
				- Properties.jBoxtoPixelWidth(width));
		pacman.setLayoutY(Properties.jBoxToFxPosY(getPosY())
				- Properties.jBoxtoPixelHeight(height));
		pacman.setCache(true); // Cache this object for better performance

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width, height);

		body = createBodyAndFixture(bodyType, ps);
		super.setUserData();

		pacman.setUserData(body);
		return pacman;

	}

	public void resetLayoutX(float x) {
		node.setLayoutX(x - Properties.jBoxtoPixelWidth(width));
	}

	public void resetLayoutY(float y) {
		node.setLayoutY(y - Properties.jBoxtoPixelWidth(height));
	}

	@Override
	public Node getNode() {
		return node;
	}

	public void setDirection(Vec2 newDirection, int degree) {
		currDirection = newDirection;
		currDegree = degree;
		body.setLinearVelocity(currDirection);
		node.setRotate(degree);
	}

	public void resetSpeed() {
		body.setLinearVelocity(currDirection);
		node.setRotate(currDegree);
	}

	public void setImage(Image image) {
		Image img = image;
		ImagePattern imagePattern = new ImagePattern(img);
		((Shape) node).setFill(imagePattern);
	}

	public void animatePacman(double time) {
		double duration = 0.100;
		int index = (int) ((time % (images.length * duration)) / duration);
		this.setImage(images[index]);
		// if(++imgNum == 3){
		// imgNum= 0;
		// this.setImage(images[imgNum]);
		// }else{
		// this.setImage(images[imgNum++]);
		// }
	}

	public void setOpenPacman() {
		this.setImage(imageOpen);
	}

	public void setColliding(boolean col) {
		this.colliding = col;
	}

	public boolean isColliding() {
		return colliding;
	}
}