package backend.game.objects.units.res;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.Globals;

public class SpriteLoader implements Globals, Runnable{

	public static Image[][] sprites = new Image[4][23];
	public static Image[] maps = new Image[3];
	public static Image[] buildings = new Image[1];
	
	public SpriteLoader() throws Exception{

		buildings[0] = ImageIO.read(new File("src/backend/game/objects/units/res/castle.gif")).getScaledInstance(30,30, Image.SCALE_SMOOTH);
		
		maps[0] = ImageIO.read(new File("src/backend/game/objects/units/res/m1.gif")).getScaledInstance(350, 350, Image.SCALE_SMOOTH);
		maps[1] = ImageIO.read(new File("src/backend/game/objects/units/res/m2.gif")).getScaledInstance(350, 350, Image.SCALE_SMOOTH);
		maps[2] = ImageIO.read(new File("src/backend/game/objects/units/res/m3.gif")).getScaledInstance(350, 350, Image.SCALE_SMOOTH);
		
		//Barbarian

		sprites[TROOP_BASIC][ANIMATION_STAND_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Front_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Front_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Front_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Front_Up.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Front_Down.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_BASIC][ANIMATION_STAND_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Left_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Left_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Left_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Up_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Down_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_BASIC][ANIMATION_STAND_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Right_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Right_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Right_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Up_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Down_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_BASIC][ANIMATION_STAND_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Back_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Back_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_WALK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Back_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Back_Up.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_BASIC][ANIMATION_ATTACK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Barbarian_Attack_Back_Down.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		//Archer

		sprites[TROOP_ARCHER][ANIMATION_STAND_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Front_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Front_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Front_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Up_Front.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Down_Front.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_ARCHER][ANIMATION_STAND_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Left_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Left_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Left_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Up_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Down_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_ARCHER][ANIMATION_STAND_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Right_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Right_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Right_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Up_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Down_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		sprites[TROOP_ARCHER][ANIMATION_STAND_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Back_Stand.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Back_Walk_Left.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_WALK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Back_Walk_Right.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Back_Up.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		sprites[TROOP_ARCHER][ANIMATION_ATTACK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Archer_Attack_Back_Down.gif")).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		
		//Giant

		sprites[TROOP_GIANT][ANIMATION_STAND_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Front_Stand.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Front_Walk_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Front_Walk_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_DOWN] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Up_Front.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_DOWN_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Down_Front.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		
		sprites[TROOP_GIANT][ANIMATION_STAND_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Left_Stand.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Left_Walk_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Left_Walk_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_LEFT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Up_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_LEFT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Down_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		
		sprites[TROOP_GIANT][ANIMATION_STAND_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Right_Stand.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Right_Walk_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Right_Walk_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_RIGHT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Up_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_RIGHT_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Down_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		
		sprites[TROOP_GIANT][ANIMATION_STAND_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Back_Stand.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Back_Walk_Left.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_WALK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Back_Walk_Right.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_UP] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Back_Up.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		sprites[TROOP_GIANT][ANIMATION_ATTACK_UP_ALT] = ImageIO.read(new File("src/backend/game/objects/units/res/Giant_Attack_Back_Down.gif")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
	
//		Thread t = new Thread(this);
//		t.start();
		
	}

	@Override
	public void run() {
		
		Painter l = new Painter();
		JFrame f = new JFrame("Loading Resources");
		l.setPreferredSize(new Dimension(32,32));
		f.setContentPane(l);
		f.pack();
		f.setVisible(true);
		
		for(int i = 1; i < sprites.length; i++){
			for(int j = 1; j < sprites[i].length; j++){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				l.type = i;
				l.action = j;
				l.repaint();
			}
		}
		
		f.dispose();
		
	}
	
	
}

class Painter extends JPanel{
	
	public int type = 1;
	public int action = 1;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(SpriteLoader.sprites[type][action], 0, 0, null);
		
	}
	
}