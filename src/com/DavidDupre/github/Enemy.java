package com.DavidDupre.github;

import java.util.List;

import com.DavidDupre.github.utils.Boundry;
import com.DavidDupre.github.utils.Square;
import com.DavidDupre.github.utils.Vector2D;

public class Enemy {
	public Vector2D position = new Vector2D();
	public int wobble = 0;
	public double azimuth = 0;

	public boolean dead = false;
	private int metaIndex = -1;

	public Square image;

	private double speed = 4.5;
	private List<Player> players;

	public Enemy(int x, int y, int size, String imageUrl,
			List<Boundry> boundries, List<Player> players) {
		position.set(x, y);
		this.image = new Square(size, imageUrl, boundries);
		this.players = players;
	}

	public void killNotify(int metaIndex) { // Make actually notification system
		dead = true;
		this.metaIndex = metaIndex;
	}

	public int deadFlag() {
		return metaIndex;
	}

	public void update() {
		calcMove();
		image.boundryDetection(position);

		if (Math.cos(Math.toRadians(azimuth)) < 0) {
			this.image.theta = 180;
		} else {
			this.image.theta = 0;
		}

		this.image.theta += Math.toDegrees(Math.sin(wobble)) * .1;
		
		int nullEnemy = -1;
		for (Player p : players) {
			if (image.collided(p.image)) {
				nullEnemy = players.indexOf(p);
				p.killNotify(nullEnemy);
			}
		}
	}

	public void draw() {
		if (azimuth > 90 || azimuth < -90) {
			this.image.isFlipped = true;
		} else {
			this.image.isFlipped = false;
		}
		this.image.position.set(this.position);
		this.image.draw();
	}

	public void calcMove() {
		double distance = 0;
		int target = 0;
		for (Player p : players) {
			double foo = Math.sqrt(Math.pow(p.position.x - position.x, 2)
					+ Math.pow(p.position.y - position.y, 2));
			if (distance > foo || distance == 0) {
				distance = foo;
				target = players.indexOf(p);
			}
		}
		azimuth = Math.toDegrees(Math.atan2(players.get(target).position.y
				- position.y, players.get(target).position.x - position.x));
		position.add(new Vector2D(speed * Math.cos(Math.toRadians(azimuth)),
				speed * Math.sin(Math.toRadians(azimuth))));

		wobble++;
	}
}
