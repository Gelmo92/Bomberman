package game;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import game.Entity.Direction;

public class TestBomberman {

	private Map testMap;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void buildMap() throws FileNotFoundException, IllegalStateException {
		//exception.expect(FileNotFoundException.class);
		//exception.expect(IllegalStateException.class);
		testMap = new Map();		
	}
	
	@Test
	public void testEntity() {
		assertNotEquals(Direction.DEAD, Entity.Direction.getRandom());
		assertNotEquals(Direction.NONE, Entity.Direction.getRandom());
	}
	
	@Test
	public void testWall() {
		Wall testWall = new Wall(new Point(0,0), false, false);
		assertTrue(testWall != null);
		assertEquals(testWall.getPos(), new Point(0,0));
		assertFalse(testWall.getDestroyable());
		assertFalse(testWall.getPerimetral());
		testWall = new Wall(new Point(0,0), true, true);
		assertTrue(testWall.getDestroyable());
		assertTrue(testWall.getPerimetral());
		assertEquals(testWall.toString(), "WALL");
	}
	
	@Test
	public void testTerrain() {
		Terrain testTerrain = new Terrain(new Point(0,0));
		assertTrue(testTerrain != null);
		assertEquals(testTerrain.getPos(), new Point(0,0));
		assertFalse(testTerrain.getBurnt());
		testTerrain.setBurnt();
		assertTrue(testTerrain.getBurnt());
	}

	@Test
	public void testChest() {
		Chest testChest = testMap.getMyChests().get(0);
		assertTrue(testChest != null);
		assertEquals(testChest.getPos(), new Point(520,40));
		assertTrue(testMap.getMyChests().contains(testChest));
		testChest.destroy();
		assertFalse(testMap.getMyChests().contains(testChest));
		assertTrue(testMap.getMyBonus().get(0).getPos().equals(testChest.getPos()));
		assertEquals(testChest.toString(), "CHEST");
	}
	
	@Test
	public void testBonus() {
		Chest testChest = testMap.getMyChests().get(0);
		testChest.destroy();
		Bonus testBonus = testMap.getMyBonus().get(0);
		assertTrue(testBonus != null);
		assertTrue(testBonus.getPos().equals(testChest.getPos()));
		assertNotEquals(testBonus.getType(), Bonus.BonusType.LIFE);//Player ha tutte le vite
		assertEquals(testBonus.toString(), "BONUS");
		testBonus.getBonus();
		assertTrue(Bomb.getCanMove() || Explosion.getExplosionRate() == 2 || Bomb.getNumberBomb() == 2);
		assertFalse(Bomb.getCanMove() && Explosion.getExplosionRate() == 2);
		assertFalse(Bomb.getCanMove() && Bomb.getNumberBomb() == 2);
		assertFalse(Explosion.getExplosionRate() == 2 && Bomb.getNumberBomb() == 2);
		testBonus.destroy();
		testMap.getMyPlayer().harm();
		testChest = testMap.getMyChests().get(0);
		testChest.destroy();
		testBonus = testMap.getMyBonus().get(0);
		assertTrue(testBonus.getType() == Bonus.BonusType.LIFE);
		testBonus.getBonus();
		assertTrue(testMap.getMyPlayer().getLife() == Player.MAX_LIFE);
		testBonus.destroy();
		assertFalse(testMap.getMyBonus().contains(testBonus));
		assertEquals(testBonus.toString(), "BONUS");
	}
	
	@Test
	public void testBomb() {
		testMap.dropBomb();
		Bomb testBomb = testMap.getMyBombs().get(0);
		assertTrue(testBomb != null);
		assertTrue(Bomb.getDroppedBombs() == 1);
		assertEquals(testBomb.getPos(),testMap.getMyPlayer().getPos());
		assertFalse(Bomb.getCanMove());
		Bomb.setCanMove();
		assertTrue(Bomb.getCanMove());
		
		testBomb.move(0, Direction.UP);
		assertEquals(testBomb.getDirection(), Direction.UP);
		assertEquals(testBomb.getPos(),testMap.getMyPlayer().getPos());
		testBomb.move(MapView.CELL, testBomb.getDirection());
		assertEquals(testBomb.getPos(),testMap.getMyPlayer().getPos());
		assertFalse(testBomb.isMoving());
		
		testBomb.move(0, Direction.RIGHT);
		assertEquals(testBomb.getDirection(), Direction.RIGHT);
		assertEquals(testBomb.getPos(),testMap.getMyPlayer().getPos());
		testBomb.move(MapView.CELL, testBomb.getDirection());
		assertNotEquals(testBomb.getPos(),testMap.getMyPlayer().getPos());
		assertTrue(testBomb.isMoving());
		
		testBomb.dominoEffect();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		assertEquals(testBomb.getDirection(), Direction.NONE);
		assertFalse(testBomb.isMoving());
		assertFalse(testMap.getMyBombs().contains(testBomb));
		assertTrue(Bomb.getDroppedBombs() == 0);
		
		assertTrue(Bomb.getNumberBomb() == 1);
		Bomb.increaseNumberBomb();
		assertTrue(Bomb.getNumberBomb() == 2);
		assertEquals(testBomb.toString(), "BOMB");
		Bomb.resetStatic();
		assertTrue(Bomb.getCanMove() == false && Bomb.getNumberBomb() == 1 && Bomb.getDroppedBombs() == 0);
	}
	
	@Test
	public void testExplosion() {
		testMap.dropBomb();
		Bomb testBomb = testMap.getMyBombs().get(0);
		testBomb.dominoEffect();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		Explosion testExplosion = testMap.getMyExplosion().get(0);
		assertTrue(testExplosion != null);
		assertTrue(testMap.getMyPlayer().getLife() == 2);
		assertTrue(testExplosion.getPropagation().get(0).equals(testBomb.getPos()));
		assertTrue(Explosion.getExplosionRate() == 1);
		Explosion.increaseRate();
		assertTrue(Explosion.getExplosionRate() == 2);
		assertEquals(testExplosion.toString(), "EXPLOSION");
		testExplosion.destroy();
		assertFalse(testMap.getMyExplosion().contains(testExplosion));
		Explosion.resetStatic();
		assertTrue(Explosion.getExplosionRate() == 1);
	}
}
