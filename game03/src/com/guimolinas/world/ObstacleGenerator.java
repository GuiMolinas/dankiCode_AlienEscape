package com.guimolinas.world;

import java.awt.image.BufferedImage;
import com.guimolinas.entities.Entity;
import com.guimolinas.entities.Obstacle;
import com.guimolinas.main.Game;

public class ObstacleGenerator {

    public int time = 0;
    public int targetTime = 60;  // Tempo alvo para gerar os obstáculos

    public void tick() {
        time++;

        if (time >= targetTime) {
            // Gerar obstáculos com espaço aleatório entre eles
            int height1 = Entity.rand.nextInt(50 - 30 + 1) + 30;
            BufferedImage sprite1 = Game.spritesheet.getSprite(32, 0, 16, 31); // Sprite de cima
            Obstacle obstacle1 = new Obstacle(Game.WIDTH, 0, 20, height1, 1, sprite1, true);  // Obstáculo superior

            int height2 = Entity.rand.nextInt(50 - 30 + 1) + 30;
            BufferedImage sprite2 = Game.spritesheet.getSprite(16, 0, 16, 31); // Sprite de baixo
            Obstacle obstacle2 = new Obstacle(Game.WIDTH, Game.HEIGHT - height2, 20, height2, 1, sprite2, false);  // Obstáculo inferior (conta ponto)

            // Adiciona os obstáculos ao jogo
            Game.entities.add(obstacle1);
            Game.entities.add(obstacle2);

            // Reinicia o tempo
            time = 0;
        }
    }
}
