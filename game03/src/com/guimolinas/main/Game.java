//Pacote
package com.guimolinas.main;

//importando bibliotecas
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


import com.guimolinas.entities.Entity;
import com.guimolinas.entities.Player;
import com.guimolinas.graficos.Spritesheet;
import com.guimolinas.graficos.UI;
import com.guimolinas.world.ObstacleGenerator;

//Para fazer um gradiente
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {


	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	//não muda tamanho
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	
	
	public static Player player;
	
	public static double score = 0;
	
	public UI ui;
	
	//Var para imagem de fundo
	private BufferedImage background;
	
	public static ObstacleGenerator obstacleGenerator;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		Sound.background.loop();
		
		//tamanho da janela
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Iniciando objeto
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		player = new Player((WIDTH / 2) - 30, HEIGHT / 2 ,16 ,16 , 2, spritesheet.getSprite(0, 0, 16, 16));
		obstacleGenerator = new ObstacleGenerator();

		ui = new UI();
		
		
		entities.add(player);
		
		// Carregando a imagem de fundo da pasta "res"
	    try {
	        background = ImageIO.read(getClass().getResource("/background_space.jpg"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void initFrame() {
		frame = new JFrame("Alien Escape");
		frame.add(this);
		//Não pode redimensionar a janela
		frame.setResizable(false);
		//Calcula dimensões
		frame.pack();
		//Janela no centro
		frame.setLocationRelativeTo(null);
		//Ao fechar, finaliza
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Icon janela
		Image img = null;
				
		try {
			img = ImageIO.read(getClass().getResource("/icon.png"));
		}
				
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame.setIconImage(img);
		
		//Ao iniciar, fica visivel
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		Game game = new Game();
		game.start();
	}
	
	//lógica do jogo
	public void tick() {
		
		obstacleGenerator.tick();
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}
		
	public void render() {
		//Lida com gráficos de forma mais profissional, visando desempeenho
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
			
		//usado para renderizar
		Graphics g = image.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		// Desenha o fundo
	    g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);

	    // Ordena e desenha as entidades
	    Collections.sort(entities, Entity.nodeSorter);
	    for (int i = 0; i < entities.size(); i++) {
	        Entity e = entities.get(i);
	        e.render(g);
	    }

	    g.dispose();
	    g = bs.getDrawGraphics();
	    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
	    ui.render(g);
	    
	    bs.show();

		/*
	    // Definindo as cores no formato hexadecimal
	    Color color1 = new Color(0x27, 0x1a, 0x46); // Cor #271a46
	    Color color2 = new Color(0x58, 0x3f, 0x92); // Cor #583f92
	    Color color3 = new Color(0x9e, 0x85, 0xd6); // Cor #9e85d6

	    // Criando um gradiente linear com três cores
	    float[] fractions = {0.0f, 0.5f, 1.0f};
	    Color[] colors = {color1, color2, color3};

	    // Gradiente que vai do topo (0, 0) até o fundo da tela (WIDTH, HEIGHT)
	    LinearGradientPaint gradient = new LinearGradientPaint(
	        new Point2D.Float(0, 0),
	        new Point2D.Float(WIDTH, HEIGHT),
	        fractions, colors
	    );

	    g2d.setPaint(gradient);
	    g2d.fillRect(0, 0, WIDTH, HEIGHT);

	    Collections.sort(entities, Entity.nodeSorter);

	    for(int i = 0; i < entities.size(); i++) {
	        Entity e = entities.get(i);
	        e.render(g);
	    }

	    g.dispose();
	    g = bs.getDrawGraphics();
	    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
	    ui.render(g);
		 */


	    
		
			
		bs.show();
		}
		
	
	public void run() {
		requestFocus();
		//Limitando FPS
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		//Verifica os 60 FPS
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >=1) {
				tick();
				render();
				frames++;
				delta--;
			}
				
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
					timer += 1000;
			}
		}
			
			stop();
			
		}

		//Eventos de teclado
		
	public void keyTyped(KeyEvent e) {
			
	}

	public void keyPressed(KeyEvent e) {
		//Determina espaço como tecla
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//Pressionado
			player.isPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		//Determina espaço como tecla
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					//Solto
					player.isPressed = false;
				}	
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
			
	}

	public void mousePressed(MouseEvent e) {
		
	}

		
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
			
	}

		
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
			
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
			
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseMoved(MouseEvent e) {
			
	}
	
}
