package interfaces;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Utils.Global;
import Utils.Sfondo;
import bubbleMaster.Start;
import dataButton.SimpleButton;
import dataObstacles.Ostacolo;
import dataObstacles.Player;

public class End 
{
	// i bottoni dell'interfaccia
	private SimpleButton replay, begin, choose;
	/*immagine del cursore*/
	private Image cursor;
	/**array contenente i bottoni della schermata*/
	private ArrayList<SimpleButton> buttons;
	/*posizione del cursore*/
	private int indexCursor;
	/*dimensioni del cursore*/
	private int widthC, heightC;
	// vettore dei giocatori partecipanti alla partita
	private ArrayList<Ostacolo> players, ostacoli;
	// lo sfondo del livello
	private Sfondo sfondo = null;
	
	public End() throws SlickException
		{
			cursor = new Image( "./data/Image/cursore.png" ); 
			//lunghezza e altezza del cursore
			widthC = Global.H*10/133;
			heightC = Global.H/24;
			
			buttons = new ArrayList<SimpleButton>();
			
			replay = new SimpleButton( Global.W/5, Global.H*3/4, "RIGIOCA", Color.orange );
			begin = new SimpleButton( Global.W/2, Global.H*3/4, "TORNA ALLA SCHERMATA PRINCIPALE", Color.orange );
			choose = new SimpleButton( Global.W*10/33, Global.H*6/7, "TORNA ALLA SCELTA LIVELLI", Color.orange );
			
			buttons.add( replay );
			buttons.add( begin );
			buttons.add( choose );

			indexCursor = -1;
			
			players = InGame.players;
			ostacoli = InGame.ostacoli;
		}
	
	public void draw( GameContainer gc ) throws SlickException
		{
			
			Graphics g = gc.getGraphics();			

			if(sfondo == null)				
				sfondo = Global.sfondo;
			sfondo.draw( gc );
			
			for(int i = 0; i < players.size(); i++)
				players.get( i ).draw( g );

			for(int i = 0; i < ostacoli.size(); i++)
				ostacoli.get( i ).draw( g );
			
			Image fine = new Image( "./data/Image/vuoto.png" );
			Color black = new Color( 0, 0, 0, 185 );
			fine.draw( 0, 0, Global.W, Global.H, black );

			// ascissa e ordinata delle stringhe da stampare
			int x = Global.H/8, y = Global.H/6;

			// TODO COMPLETARE CON TUTTE LE STATISTICHE
			
			//trasformo il tempo da millisecondi a secondi
			int timing = (int)Start.stats.getTempo()/1000;
			int h = timing/3600;
			int m = (timing - (h*3600))/60;
			int s = timing - h*3600 - m*60;
			String seconds = "TEMPO IMPIEGATO =     " + h + "h : " + m + "m : " + s + "s";
			g.drawString( seconds, x, y );
			
			int offset = Global.W/10, width = Global.W/17, height = Global.H/10;
			for(int i = 0; i < players.size(); i++)
				((Player) players.get( i )).getImage().draw( Global.W*10/26 + (width + offset) * i, Global.H/25, Global.W/17, height );
			// TODO CAPIRE COME USARLI AL MEGLIO
			
			/*String colpi = "COLPI SPARATI =       " + shots;
			g.drawString( colpi, x, y + 50 );
			
			String vite = "VITE PERSE =          " + (Global.lifes - lifes);
			g.drawString( vite, x, y + 100 );
			
			String punti = "PUNTEGGIO OTTENUTO =   ";
			g.drawString( punti, x, y + 150 );*/
			
			for(int i = 0; i < buttons.size(); i++)
				buttons.get( i ).draw( gc.getGraphics() );
			
			if(indexCursor >= 0)
				cursor.draw( buttons.get( indexCursor ).getX() - widthC, buttons.get( indexCursor ).getY(), widthC, heightC );
		}

	public void update(GameContainer gc) throws SlickException
		{
			Input input = gc.getInput();
			int mouseX = input.getMouseX();
			int mouseY = input.getMouseY();	

			if((input.isKeyPressed( Input.KEY_UP ) || input.isKeyPressed( Input.KEY_DOWN ) || input.isKeyPressed( Input.KEY_LEFT ) || input.isKeyPressed( Input.KEY_RIGHT )))
				{
					if(indexCursor < 0)
						indexCursor = 0;
					else if(indexCursor == 0)
						indexCursor = 1;
					else
						indexCursor = 0;
				}

			if((replay.checkClick( mouseX, mouseY ) && input.isMousePressed( Input.MOUSE_LEFT_BUTTON )) || (input.isKeyPressed( Input.KEY_ENTER ) && buttons.get( indexCursor ).getName().equals( "RITENTA" )))
				{
					indexCursor = -1;
					Start.ig.addOstacoli( Begin.livelli.get( Start.cl.getIndexLevel() ).getElements(), Begin.livelli.get( Start.cl.getIndexLevel() ).getImage(), gc );
					Global.drawCountdown = true;
					Start.stats.startTempo();
					Global.inGame = true;
					Start.endGame = 0;
					Start.startGame = 1;
				}
			
			else if((begin.checkClick( mouseX, mouseY ) && input.isMousePressed( Input.MOUSE_LEFT_BUTTON )) || (input.isKeyPressed( Input.KEY_ENTER ) && buttons.get( indexCursor ).getName().startsWith( "TORNA ALLA SCHERMATA" )))
				{
					indexCursor = -1;
					Start.endGame = 0;
					Start.begin = 1;
				}
			
			else if((choose.checkClick( mouseX, mouseY ) && input.isMousePressed( Input.MOUSE_LEFT_BUTTON )) || (input.isKeyPressed( Input.KEY_ENTER ) && buttons.get( indexCursor ).getName().startsWith( "TORNA ALLA SCELTA" )))
				{
					indexCursor = -1;
					Start.endGame = 0;
					Start.chooseLevel = 1;
				}
		}
}
