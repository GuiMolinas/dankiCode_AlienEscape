//Pacote
package com.guimolinas.entities;

//importando bibliotecas
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

//importando classes
import com.guimolinas.main.Game;
import com.guimolinas.world.Camera;
import com.guimolinas.world.Node;
import com.guimolinas.world.Vector2I;
import com.guimolinas.world.World;

public class Entity {
	
	//Var entidade - x, y, largura e altura
	public double x;
	public double y;
	protected double width;
	protected double height;
	protected double speed;
	
	//caminho do inimigo
	protected List<Node> path;
	
	//Carrega imagem do sprite
	protected BufferedImage sprite;
	
	public static Random rand = new Random();
	
	//Usado para debug
	public boolean debug = false;
	
	//camada profundidade 
	public int depth;
	
	//sprite minhoca
	public static BufferedImage WORM_SPRITE = Game.spritesheet.getSprite(0, 16, 16, 16);
	
	//sprite inimigo
	public static BufferedImage ENEMY1 = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage ENEMY2 = Game.spritesheet.getSprite(32, 64, 16, 16);
	public static BufferedImage ENEMY3 = Game.spritesheet.getSprite(32, 96, 16, 16);
	public static BufferedImage ENEMY4 = Game.spritesheet.getSprite(32, 128, 16, 16);
	
	//Construtor
	public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		//Validando dados
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.sprite = sprite;		
	}
	
	//Sistema de camadas
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		public int compare(Entity n0, Entity n1) {
			if(n1.depth < n0.depth) {
				return +1;
			}
			
			if(n1.depth > n0.depth) {
				return -1;
			}
			
			return 0;
		}
	};
	
	public double calculateAngleToPlayer(Entity player) {
	    double dx = player.getX() - this.getX();
	    double dy = player.getY() - this.getY();
	    return Math.atan2(dy, dx);
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, (World.WIDTH*16) - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT*16) - Game.HEIGHT);
	}
	
		
	//Getters e setters - atributos privados	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return (int)this.width;
	}
	
	public int getHeight() {
		return (int)this.height;
	}
	
	//Lógica do game
	public void tick() {
		
	}
	
	//Inimigo segue player
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2I target = path.get(path.size() - 1).tile;
				
				if(x < target.x * 16 /*&& !isColliding(this.getX() + 1, this.getY())*/) {
					x++;
				}
				
				else if(x > target.x * 16 /*&& !isColliding(this.getX() - 1, this.getY())*/) {
					x--;
				}
				
				if(y < target.y * 16 /*&& !isColliding(this.getX(), this.getY() + 1)*/) {
					y++;
				}
				
				else if(y > target.y * 16 /*&& !isColliding(this.getX(), this.getY() - 1)*/) {
					y--;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}
				
			}
		}
	}
	
	//calcula distancia entre entidades
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	//Verifica colisao
	public static boolean isColliding(Entity e1, Entity e2) {
	    // Primeiro, verificamos a colisão por retângulo simples para melhorar o desempenho
	    Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
	    Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

	    // Se os retângulos não colidirem, evitamos a verificação pixel-perfect
	    if (!e1Mask.intersects(e2Mask)) {
	        return false;
	    }

	    // Caso contrário, procedemos com a verificação pixel-perfect
	    BufferedImage sprite1 = e1.sprite;
	    BufferedImage sprite2 = e2.sprite;

	    int xStart = Math.max(e1.getX(), e2.getX());
	    int xEnd = Math.min(e1.getX() + e1.getWidth(), e2.getX() + e2.getWidth());

	    int yStart = Math.max(e1.getY(), e2.getY());
	    int yEnd = Math.min(e1.getY() + e1.getHeight(), e2.getY() + e2.getHeight());

	    for (int x = xStart; x < xEnd; x++) {
	        for (int y = yStart; y < yEnd; y++) {
	            // Coordenadas relativas para os dois sprites
	            int e1SpriteX = x - e1.getX();
	            int e1SpriteY = y - e1.getY();
	            int e2SpriteX = x - e2.getX();
	            int e2SpriteY = y - e2.getY();

	            // Garantir que as coordenadas relativas estejam dentro dos limites da imagem
	            if (e1SpriteX >= 0 && e1SpriteX < sprite1.getWidth() &&
	                e1SpriteY >= 0 && e1SpriteY < sprite1.getHeight() &&
	                e2SpriteX >= 0 && e2SpriteX < sprite2.getWidth() &&
	                e2SpriteY >= 0 && e2SpriteY < sprite2.getHeight()) {

	                // Verificamos a transparência (alpha) do pixel em cada sprite
	                int pixel1 = sprite1.getRGB(e1SpriteX, e1SpriteY);
	                int pixel2 = sprite2.getRGB(e2SpriteX, e2SpriteY);

	                // Se ambos os pixels não forem transparentes (alpha != 0)
	                if (((pixel1 >> 24) & 0xff) != 0 && ((pixel2 >> 24) & 0xff) != 0) {
	                    return true; // Colisão pixel-perfect detectada
	                }
	            }
	        }
	    }

	    return false; // Nenhuma colisão detectada
	}


	
	//renderiza o jogo
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
}
