//Pacote
package com.guimolinas.graficos;

import java.awt.Color;
import java.awt.Font;
//importando bibliotecas
import java.awt.Graphics;

import com.guimolinas.main.Game;

public class UI {

	//Render UI
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD,18));
		
		g.drawString("Score: " + (int) Game.score, 20, 20);
		
	}
	
}
