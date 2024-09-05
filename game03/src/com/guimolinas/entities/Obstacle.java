package com.guimolinas.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.guimolinas.main.Game;
import com.guimolinas.main.Sound;

public class Obstacle extends Entity {
    private boolean scored = false;
    private boolean isUpperObstacle;  // Novo atributo para identificar se é o obstáculo superior

    public Obstacle(double x, double y, int width, int height, double speed, BufferedImage sprite, boolean isUpperObstacle) {
        super(x, y, width, height, speed, sprite);
        this.isUpperObstacle = isUpperObstacle;  // Inicializa o atributo
    }

    public void tick() {
        x--;  // Movimento para a esquerda
        
        // Verifica se o player passou o obstáculo de baixo (só conta o ponto no obstáculo de baixo)
        if (!scored && !isUpperObstacle && x + width < Game.player.getX()) {
        	Game.score += 1;  // Conta o ponto
        	Sound.point.play();
            scored = true;    // Marca como contado
        }
        
        // Remove o obstáculo quando sair da tela
        if (x + width <= 0) {
            Game.entities.remove(this);
        }
    }

    public void render(Graphics g) {
        // Desenhando o obstáculo
        if (sprite != null) {
            g.drawImage(sprite, (int) this.getX(), (int) this.getY(), this.getWidth(), this.getHeight(), null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect((int) x, (int) y, (int) width, (int) height);
        }
    }
}
