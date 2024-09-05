//Pacote
package com.guimolinas.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//importando bibliotecas
import java.awt.image.BufferedImage;

import com.guimolinas.main.Game;
import com.guimolinas.main.Sound;
import com.guimolinas.world.World;

//Usa Entity
public class Player extends Entity {
	
	//Var para verificar botão pressionado
	public boolean isPressed = false;
	
	
	//Construtor
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		//Validando dados
		super(x, y, width, height, speed, sprite);
	}
	
	//Lógica do game
	public void tick() {
		
		depth = 2;
		
		//Verifica se botão está pressionado para "pular"
		if(!isPressed) {
			y += 2;
		}
		
		else {
			if(y > 0) {
				Sound.jump.play();
				y -= 2;
			}
		}
		
		//saiu da tela, perdeu o jogo
		if(y > Game.HEIGHT) {
			World.restartGame();
			return;
		}
		
		//Testando colisao
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			
			if(e != this) {
				if(Entity.isColliding(this, e)) {
					//Game Over
					World.restartGame();
					return;
				}
			}
		}
	}
	
	//Renderizandeo posicao player
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//Se não clica caidinha
		if(!isPressed) {
			g2.rotate(Math.toRadians(20), this.getX() + width/2, this.getY() + height/2);
			g2.drawImage(sprite, this.getX(), this.getY(), null);
			g2.rotate(Math.toRadians(-20), this.getX() + width/2, this.getY() + height/2);
		}
		
		//Se não, volta ao normal
		else {
			g2.rotate(Math.toRadians(-20), this.getX() + width/2, this.getY() + height/2);
			g2.drawImage(sprite, this.getX(), this.getY(), null);
			g2.rotate(Math.toRadians(20), this.getX() + width/2, this.getY() + height/2);
		}
		
		//g2.drawImage(sprite, this.getX(), this.getY(), null);
		
		//g.setColor(Color.red);
		
		//g.fillRect((int)this.getX(), (int)this.getY(), (int)width, (int)height);
	}
	
}
	
	
	